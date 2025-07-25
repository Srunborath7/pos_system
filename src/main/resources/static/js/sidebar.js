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

    const usernameSpan = document.getElementById('username');
    const tokenTextarea = document.getElementById('token');
    if (usernameSpan) usernameSpan.textContent = payload.sub || 'User';
    if (tokenTextarea) tokenTextarea.value = token;
}

// Run on page load
document.addEventListener('DOMContentLoaded', () => {
    autofillCreatedBy();
    fillUserInfoFromToken();
});
