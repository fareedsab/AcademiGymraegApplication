let userRoles = [];
let currentEditingUserId = null;
let allUsers = []; // Store all users data

document.addEventListener('DOMContentLoaded', function() {
    if (!checkAuth()) return;

    fetchUserRoles()
        .then(() => fetchUsers())
        .catch(error => {
            console.error('Initialization error:', error);
            showToast('Error', 'Failed to load initial data', false);
        });

    // Rest of your existing initialization code...
});

function fetchUsers() {
    fetch('/api/users/allUsers')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(apiResponse => {
            if (apiResponse && apiResponse.data && Array.isArray(apiResponse.data)) {
                allUsers = apiResponse.data; // Store all users data
                populateUsersTable(allUsers);
            } else {
                throw new Error('Invalid data structure received');
            }
        })
        .catch(error => {
            console.error('Error fetching users:', error);
            document.getElementById('usersTableBody').innerHTML = `
                <tr>
                    <td colspan="7" class="text-center text-danger">Error loading user data: ${error.message}</td>
                </tr>
            `;
        });
}
function fetchUserRoles() {
    return fetch('/api/users/userRoles')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(apiResponse => {
            if (apiResponse && apiResponse.data && Array.isArray(apiResponse.data)) {
                userRoles = apiResponse.data;
                return Promise.resolve();
            } else {
                throw new Error('Invalid roles data structure');
            }
        })
        .catch(error => {
            console.error('Error fetching user roles:', error);
            showToast('Error', 'Failed to load user roles', false);
            return Promise.reject(error);
        });
}
function editUser(userId) {
    currentEditingUserId = userId;

    // Find the user in our stored data
    const userToEdit = allUsers.find(user => user.id == userId);

    if (userToEdit) {
        // Populate form
        const form = document.getElementById('addUserForm');
        form.elements.fullname.value = userToEdit.fullname || '';
        form.elements.username.value = userToEdit.username || '';
        form.elements.email.value = userToEdit.email || '';

        // Set role if available
        if (userToEdit.userRole) {
            const roleSelect = form.elements.role;
            // Wait for roles to be loaded
            const checkRoleLoaded = setInterval(() => {
                if (roleSelect.options.length > 1) { // More than just the default option
                    clearInterval(checkRoleLoaded);
                    for (let i = 0; i < roleSelect.options.length; i++) {
                        if (roleSelect.options[i].value == userToEdit.userRole.id) {
                            roleSelect.selectedIndex = i;
                            break;
                        }
                    }
                }
            }, 100);
        }

        // Update modal title
        document.getElementById('addUserModalLabel').textContent = 'Edit User';

        // Show modal
        const modal = new bootstrap.Modal(document.getElementById('addUserModal'));
        modal.show();
    } else {
        showToast('Error', 'User data not found', false);
    }
}

// Update the handleUserSubmit function to handle both add and edit
function handleUserSubmit() {
    const form = document.getElementById('addUserForm');

    if (!form.checkValidity()) {
        form.classList.add('was-validated');
        return;
    }

    const formData = {
        fullname: form.elements.fullname.value,
        username: form.elements.username.value,
        email: form.elements.email.value,
        roleId: parseInt(form.elements.role.value),
        active: true
    };

    // Only include password if it's a new user or if changed
    if (!currentEditingUserId || form.elements.password.value) {
        formData.password = form.elements.password.value;
    }

    if (currentEditingUserId) {
        formData.id = currentEditingUserId;
    }

    addUpdateUser(formData);
}

// Unified function for both add and update
function addUpdateUser(userData) {
    const isUpdate = !!userData.id;
    const url = '/api/users/addUpdateUserDetails';
    const method = 'POST';

    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData)
    })
        .then(response => response.json())
        .then(data => {
            if (data.status === 200 || data.status === 201) {
                showToast('Success', isUpdate ? 'User updated successfully' : 'User added successfully', true);
                fetchUsers(); // Refresh the table
                bootstrap.Modal.getInstance(document.getElementById('addUserModal')).hide();
            } else {
                showToast('Error', data.message || 'Operation failed', false);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showToast('Error', 'An error occurred', false);
        });
}

// Rest of your existing code...