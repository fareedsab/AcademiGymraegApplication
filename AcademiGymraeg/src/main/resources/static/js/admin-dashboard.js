let userRoles = [];
let allUsers = [];
let currentEditingUserId = null;
document.addEventListener('DOMContentLoaded', function () {
    if (!checkAuth()) return;

    fetchUserRoles()
        .then(() => fetchUsers())
        .catch(error => {
            console.error('Initialization error:', error);
            showToast('Error', 'Failed to load initial data', false);
        });

    document.getElementById('addUserBtn').addEventListener('click', function () {
        // Add user functionality
        currentEditingUserId = null; // Reset editing state
        populateRoleDropdown(); // This populates the dropdown before showing modal
        const modal = new bootstrap.Modal(document.getElementById('addUserModal'));
        modal.show()
    });
    // addUserModal.addEventListener('hidden.bs.modal', function() {
    //     // Reset form and ensure proper focus
    //     document.getElementById('addUserForm').reset();
    //     document.getElementById('addUserForm').classList.remove('was-validated');
    //     this.inert = true;
    // });


    // Initialize form validation
    const forms = document.querySelectorAll('.requires-validation');
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', function (event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
                console.log("validated")
            }
            form.classList.add('was-validated');
        }, false);
    });

    document.getElementById('addUserBtn').addEventListener('click', function () {
        const modal = new bootstrap.Modal(document.getElementById('addUserModal'));
        modal.show();
    });
    document.getElementById('addUserForm').addEventListener('submit', function (e) {
        e.preventDefault();

        if (this.checkValidity()) {
            const formData = {
                fullname: this.elements.fullname.value,
                username: this.elements.username.value,
                email: this.elements.email.value,
                password: this.elements.password.value,
                roleId: parseInt(this.elements.role.value),
                active: true
            };
            handleUserSubmit()
        }
    });
});

function fetchUsers() {
    fetch('/api/users/allActiveUsers')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(apiResponse => {
            // Check if response has the expected structure
            if (apiResponse && apiResponse.data && Array.isArray(apiResponse.data)) {
                allUsers = apiResponse.data;
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

function populateUsersTable(users) {
    const tableBody = document.getElementById('usersTableBody');
    tableBody.innerHTML = '';

    users.forEach(user => {
        const row = document.createElement('tr');

        // Determine status badge
        const statusClass = user.active ? 'bg-success' : 'bg-secondary';
        const statusText = user.active ? 'Active' : 'Inactive';

        // Action buttons
        const actions = `
            <div class="action-btns">
                <button class="btn btn-sm btn-warning me-1 edit-btn" data-id="${user.id}">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="btn btn-sm btn-danger delete-btn" data-id="${user.id}">
                    <i class="fas fa-trash"></i>
                </button>
            </div>
        `;

        row.innerHTML = `
            <td>${user.id}</td>
            <td>${user.fullname || 'N/A'}</td>
            <td>${user.username}</td>
            <td>${user.email}</td>
            <td>${user.userRole?.roleName || 'N/A'}</td>
<!--            <td><span class="badge ${statusClass}">${statusText}</span></td>-->
            <td>${actions}</td>
        `;

        tableBody.appendChild(row);
    });

    // Add event listeners to action buttons
    document.querySelectorAll('.edit-btn').forEach(btn => {
        btn.addEventListener('click', function () {
            const userId = this.getAttribute('data-id');
            const username = this.getAttribute('data-username');
            populateRoleDropdown();
            editUser(userId,username);
        });
    });

    document.querySelectorAll('.delete-btn').forEach(btn => {
        btn.addEventListener('click', function () {
            const userId = this.getAttribute('data-id');
            deleteUser(userId);
        });
    });
}

function editUser(userId) {
    currentEditingUserId = userId;
    // document.getElementById('addUserForm').innerText = "Update User"
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
        document.getElementById('addUserModalLabel').title = 'Edit User';

        // Show modal
        const modal = new bootstrap.Modal(document.getElementById('addUserModal'));
        modal.show();
    } else {
        showToast('Error', 'User data not found', false);
    }
}

function deleteUser(userId) {
    if (confirm('Are you sure you want to delete this user?')) {
        fetch(`/api/users/deleteUser/${userId}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (!response.ok) {
                    alert('Failed to delete user')
                    throw new Error('Failed to delete user');
                }
                else
                {
                    fetchUsers();
                }
                // Refresh the table
                // return response.json();
            })
            .catch(error => {
                console.error('Error deleting user:', error);
                alert('Error deleting user: ' + error.message);
            });
    }
}

function populateRoleDropdown() {
    const roleSelect = document.querySelector('#addUserForm select[name="role"]');
    roleSelect.innerHTML = '<option selected disabled value="">Loading roles...</option>';

    // Small delay to ensure modal is shown before populating
    setTimeout(() => {
        roleSelect.innerHTML = '<option selected disabled value="">Select Role</option>';
        userRoles.forEach(role => {
            if (role.active) {
                const option = document.createElement('option');
                option.value = role.id;
                option.textContent = formatRoleName(role.roleName);
                roleSelect.appendChild(option);
            }
        });
    }, 100);
}

function formatRoleName(roleName) {
    return roleName.charAt(0) + roleName.slice(1).toLowerCase();
}

function addUser(userData) {
    fetch('/api/users/addUpdateUserDetails', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData)
    })
        .then(response => response.json())
        .then(data => {
            if (data.status === 200 || data.status === 201) {
                fetchUsers();
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

function updateUser(userData) {
    fetch('/api/users/addUpdateUserDetails', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData)
    })
        .then(response => response.json())
        .then(data => {
            if (data.status === 200) {
                showToast('Success', 'User updated successfully', true);
                fetchUsers();
                bootstrap.Modal.getInstance(document.getElementById('addUserModal')).hide();
            } else {
                showToast('Error', data.message || 'Update failed', false);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showToast('Error', 'An error occurred while updating', false);
        });
}

function handleUserSubmit() {
    const form = document.getElementById('addUserForm');

    // if (!form.checkValidity()) {
    //     form.classList.add('was-validated');
    //     return;
    // }
    console.log("Handle Submit User");
    const formData = {
        username: form.elements.username.value,
        fullname: form.elements.fullname.value,
        email: form.elements.email.value,
        password: form.elements.password.value,
        roleId: parseInt(form.elements.role.value),
        active: true
    };

    if (currentEditingUserId) {
        formData.id = currentEditingUserId;
        updateUser(formData);
    } else {
        addUser(formData);
    }
}

function resetAndPrepareModal() {
    resetForm();
    populateRoleDropdown();
}

function closeModal() {
    const modalElement = document.getElementById('addUserModal');
    let modalInstance = bootstrap.Modal.getInstance(modalElement);

    if (!modalInstance) {
        modalInstance = new bootstrap.Modal(modalElement);
    }

    modalInstance.hide();
}
