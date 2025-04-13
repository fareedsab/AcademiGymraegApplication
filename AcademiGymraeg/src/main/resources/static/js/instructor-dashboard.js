let nounGenders = [];
let allNouns = [];
let currentEditingNounId = null;

document.addEventListener('DOMContentLoaded', function () {
    if (!checkAuth()) return;

    fetchNounGenders()
        .then(() => fetchNouns())
        .catch(error => {
            console.error('Initialization error:', error);
            showToast('Error', 'Failed to load initial data', false);
        });

    document.getElementById('addNounBtn').addEventListener('click', function () {
        currentEditingNounId = null;
        populateGenderDropdown();
        const modal = new bootstrap.Modal(document.getElementById('addNounModal'));
        modal.show();
    });

    // Initialize form validation
    const forms = document.querySelectorAll('.requires-validation');
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', function (event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });

    document.getElementById('addNounForm').addEventListener('submit', function (e) {
        e.preventDefault();

        if (this.checkValidity()) {
            const userData = JSON.parse(localStorage.getItem('userData'));
            const formData = {
                welshNoun: this.elements.welshNoun.value,
                englishNoun: this.elements.englishNoun.value,
                nounGenderId: parseInt(this.elements.nounGender.value),
                isActive: this.elements.isActive.checked,
                createdBy: userData.userId,
                updatedBy: userData.userId
            };

            if (currentEditingNounId) {
                formData.id = currentEditingNounId;
                updateNoun(formData);
            } else {
                addNoun(formData);
            }
        }
    });
});

function fetchNouns() {
    fetch('/api/nouns/allActiveNouns')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(apiResponse => {
            if (apiResponse && apiResponse.data && Array.isArray(apiResponse.data)) {
                allNouns = apiResponse.data;
                populateNounsTable(allNouns);
            } else {
                throw new Error('Invalid data structure received');
            }
        })
        .catch(error => {
            console.error('Error fetching nouns:', error);
            document.getElementById('nounsTableBody').innerHTML = `
                <tr>
                    <td colspan="6" class="text-center text-danger">Error loading noun data: ${error.message}</td>
                </tr>
            `;
        });
}

function fetchNounGenders() {
    return fetch('/api/nouns/nounGenders')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(apiResponse => {
            if (apiResponse && apiResponse.data && Array.isArray(apiResponse.data)) {
                nounGenders = apiResponse.data;
                return Promise.resolve();
            } else {
                throw new Error('Invalid genders data structure');
            }
        })
        .catch(error => {
            console.error('Error fetching noun genders:', error);
            showToast('Error', 'Failed to load noun genders', false);
            return Promise.reject(error);
        });
}

function populateNounsTable(nouns) {
    const tableBody = document.getElementById('nounsTableBody');
    tableBody.innerHTML = '';

    nouns.forEach(noun => {
        const row = document.createElement('tr');

        // Determine status badge
        const statusClass = noun.isActive ? 'bg-success' : 'bg-secondary';
        const statusText = noun.isActive ? 'Active' : 'Inactive';

        // Find gender name
        const gender = nounGenders.find(g => g.id === noun.nounGenderId);
        const genderName = gender ? gender.genderName : 'N/A';

        // Action buttons
        const actions = `
            <div class="action-btns">
                <button class="btn btn-sm btn-warning me-1 edit-btn" data-id="${noun.id}">
                    <i class="fas fa-edit"></i>
                </button>
<!--                <button class="btn btn-sm btn-danger delete-btn" data-id="${noun.id}">-->
<!--                    <i class="fas fa-trash"></i>-->
<!--                </button>-->
            </div>
        `;

        row.innerHTML = `
            <td>${noun.id}</td>
            <td>${noun.welshNoun || 'N/A'}</td>
            <td>${noun.englishNoun || 'N/A'}</td>
            <td>${genderName}</td>
            <td><span class="badge ${statusClass}">${statusText}</span></td>
            <td>${actions}</td>
        `;

        tableBody.appendChild(row);
    });

    // Add event listeners to action buttons
    document.querySelectorAll('.edit-btn').forEach(btn => {
        btn.addEventListener('click', function () {
            const nounId = this.getAttribute('data-id');
            editNoun(nounId);
        });
    });

    document.querySelectorAll('.delete-btn').forEach(btn => {
        btn.addEventListener('click', function () {
            const nounId = this.getAttribute('data-id');
            deleteNoun(nounId);
        });
    });
}

function editNoun(nounId) {
    currentEditingNounId = nounId;
    const nounToEdit = allNouns.find(noun => noun.id == nounId);

    if (nounToEdit) {
        const form = document.getElementById('addNounForm');
        form.elements.welshNoun.value = nounToEdit.welshNoun || '';
        form.elements.englishNoun.value = nounToEdit.englishNoun || '';
        form.elements.isActive.checked = nounToEdit.isActive;

        // Set gender if available
        if (nounToEdit.nounGenderId) {
            const genderSelect = form.elements.nounGender;
            // Wait for genders to be loaded
            const checkGenderLoaded = setInterval(() => {
                if (genderSelect.options.length > 1) {
                    clearInterval(checkGenderLoaded);
                    for (let i = 0; i < genderSelect.options.length; i++) {
                        if (genderSelect.options[i].value == nounToEdit.nounGenderId) {
                            genderSelect.selectedIndex = i;
                            break;
                        }
                    }
                }
            }, 100);
        }

        // Update modal title
        document.getElementById('addNounModalLabel').textContent = 'Edit Noun';

        // Show modal
        const modal = new bootstrap.Modal(document.getElementById('addNounModal'));
        modal.show();
    } else {
        showToast('Error', 'Noun data not found', false);
    }
}

function deleteNoun(nounId) {
    if (confirm('Are you sure you want to delete this noun?')) {
        fetch(`/api/nouns/deleteNoun/${nounId}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (!response.ok) {
                    alert('Failed to delete noun');
                    throw new Error('Failed to delete noun');
                } else {
                    fetchNouns();
                }
            })
            .catch(error => {
                console.error('Error deleting noun:', error);
                alert('Error deleting noun: ' + error.message);
            });
    }
}

function populateGenderDropdown() {
    const genderSelect = document.querySelector('#addNounForm select[name="nounGender"]');
    genderSelect.innerHTML = '<option selected disabled value="">Loading genders...</option>';

    setTimeout(() => {
        genderSelect.innerHTML = '<option selected disabled value="">Select Gender</option>';
        nounGenders.forEach(gender => {
            const option = document.createElement('option');
            option.value = gender.id;
            option.textContent = gender.genderName;
            genderSelect.appendChild(option);
        });
    }, 100);
}

function addNoun(nounData) {
    fetch('/api/nouns/addUpdateNoun', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(nounData)
    })
        .then(response => response.json())
        .then(data => {
            if (data.status === 200 || data.status === 201) {
                showToast('Success', 'Noun added successfully', true);
                fetchNouns();
                bootstrap.Modal.getInstance(document.getElementById('addNounModal')).hide();
            } else {
                showToast('Error', data.message || 'Operation failed', false);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showToast('Error', 'An error occurred', false);
        });
}

function updateNoun(nounData) {
    fetch('/api/nouns/addUpdateNoun', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(nounData)
    })
        .then(response => response.json())
        .then(data => {
            if (data.status === 200) {
                showToast('Success', 'Noun updated successfully', true);
                fetchNouns();
                bootstrap.Modal.getInstance(document.getElementById('addNounModal')).hide();
            } else {
                showToast('Error', data.message || 'Update failed', false);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showToast('Error', 'An error occurred while updating', false);
        });
}

function closeModal() {
    const modalElement = document.getElementById('addNounModal');
    let modalInstance = bootstrap.Modal.getInstance(modalElement);

    if (!modalInstance) {
        modalInstance = new bootstrap.Modal(modalElement);
    }

    modalInstance.hide();
    document.getElementById('addNounForm').reset();
    document.getElementById('addNounForm').classList.remove('was-validated');
    currentEditingNounId = null;
}

function showToast(title, message, isSuccess) {
    // Implement your toast notification system here
    alert(`${title}: ${message}`);
}