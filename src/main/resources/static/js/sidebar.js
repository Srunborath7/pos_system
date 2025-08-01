// sidebar.js

// Utility: decode JWT payload
function parseJwt(token) {
    try {
        const base64Payload = token.split('.')[1];
        const jsonPayload = atob(base64Payload);
        return JSON.parse(jsonPayload);
    } catch (e) {
        console.error('Invalid JWT token:', e);
        return null;
    }
}

// Sidebar toggle elements
const sidebar = document.getElementById('sidebar');
const sidebarOverlay = document.getElementById('sidebarOverlay');
const sidebarToggleBtn = document.getElementById('sidebarToggle');

// Sidebar toggle handler
if (sidebarToggleBtn) {
    sidebarToggleBtn.addEventListener('click', () => {
        sidebar.classList.toggle('open');
        sidebarOverlay.classList.toggle('active');
    });
}

if (sidebarOverlay) {
    sidebarOverlay.addEventListener('click', () => {
        sidebar.classList.remove('open');
        sidebarOverlay.classList.remove('active');
    });
}

// Logout function and listeners
function logout() {
    localStorage.removeItem('token');
    window.location.href = '/index.html';
}

const logoutBtns = document.querySelectorAll('#logoutBtn');
logoutBtns.forEach(btn => btn.addEventListener('click', logout));

// Autofill createdBy input if present
function autofillCreatedBy() {
    const token = localStorage.getItem('token');
    if (!token) return;
    const payload = parseJwt(token);
    if (!payload) return;
    const createdByInput = document.getElementById('createdBy');
    if (createdByInput) createdByInput.value = payload.sub || '';
}

// Fill username and token (for dashboard page)
function fillUserInfoFromToken() {
    const token = localStorage.getItem('token');
    if (!token) return;
    const payload = parseJwt(token);
    if (!payload) return;

    const rolesSpan = document.getElementById('userRoles');
    const usernameSpan = document.getElementById('username');
    const tokenTextarea = document.getElementById('token');

    if (usernameSpan) usernameSpan.textContent = payload.sub || 'User';
    if (tokenTextarea) tokenTextarea.value = token;

    if (rolesSpan) {
        if (Array.isArray(payload.roles)) {
            rolesSpan.textContent = payload.roles.join(', ');
        } else if (typeof payload.roles === 'string') {
            // Just in case roles is a string (rare)
            rolesSpan.textContent = payload.roles;
        } else {
            rolesSpan.textContent = 'N/A';
        }
    }
}
function setSidebarMenuByRole() {
    const token = localStorage.getItem('token');
    if (!token) return;

    const payload = parseJwt(token);
    if (!payload) return;

    const roles = Array.isArray(payload.roles) ? payload.roles : [];

    // Extract role names in case roles are objects
    const roleNames = roles.map(r => (typeof r === 'object' ? r.name : r));

    console.log('Roles from token:', roleNames); // Debugging

    const adminManagerAllowed = [
        'dashboard.html', 'pos.html', 'customer.html', 'category.html',
        'product.html', 'inventory.html', 'report.html', 'register.html', 'settings.html'
    ];
    const saleAllowed = ['dashboard.html','pos.html', 'report.html','settings.html'];
    const inventoryManagerAllowed = ['category.html','product.html','inventory.html', 'dashboard.html','settings.html'];

    let allowedLinks = [];

    if (roleNames.includes('ROLE_ADMIN') || roleNames.includes('ROLE_MANAGER')) {
        allowedLinks = adminManagerAllowed;
    } else if (roleNames.includes('ROLE_SALE')) {
        allowedLinks = saleAllowed;
    } else if (roleNames.includes('ROLE_INVENTORY_MANAGER')) {
        allowedLinks = inventoryManagerAllowed;
    } else {
        allowedLinks = [];
    }

    const sidebarLinks = document.querySelectorAll('#sidebar ul.nav.flex-column > li');

    sidebarLinks.forEach(li => {
        const aTag = li.querySelector('a');
        if (!aTag) return;

        const href = aTag.getAttribute('href').split('/').pop();

        if (allowedLinks.includes(href)) {
            li.style.display = '';
        } else {
            li.style.display = 'none';
        }
    });
}


document.addEventListener('DOMContentLoaded', () => {
    setSidebarMenuByRole();
});



// Run on page load
document.addEventListener('DOMContentLoaded', () => {
    autofillCreatedBy();
    fillUserInfoFromToken();

});
