document.addEventListener("DOMContentLoaded", function () {
    const token = localStorage.getItem("token");
    if (!token) {
        alert("You must be logged in as admin to access this page.");
        window.location.href = "/login";
        return;
    }

    function apiFetch(url, options = {}) {
        return fetch(url, {
            ...options,
            headers: {
                ...(options.headers || {}),
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            }
        });
    }

    document.getElementById("resetPasswordForm").addEventListener("submit", function (e) {
        e.preventDefault();
        const username = document.getElementById("resetUsername").value;
        const newPassword = document.getElementById("resetNewPassword").value;

        apiFetch(`/api/admin/users/${username}/password`, {
            method: "PATCH",
            body: JSON.stringify({newPassword})
        })
            .then(res => res.json())
            .then(data => document.getElementById("resetPasswordMessage").textContent = data.message)
            .catch(() => document.getElementById("resetPasswordMessage").textContent = "Error resetting password.");
    });

    document.getElementById("addRoleForm").addEventListener("submit", function (e) {
        e.preventDefault();
        const username = document.getElementById("addRoleUsername").value;
        const role = document.getElementById("addRole").value;

        apiFetch(`/api/admin/users/${username}/roles`, {
            method: "PATCH",
            body: JSON.stringify({role})
        })
            .then(res => res.json())
            .then(data => document.getElementById("roleMessage").textContent = data.message)
            .catch(() => document.getElementById("roleMessage").textContent = "Error adding role.");
    });

    document.getElementById("setRolesForm").addEventListener("submit", function (e) {
        e.preventDefault();
        const username = document.getElementById("setRolesUsername").value;
        const rolesStr = document.getElementById("setRoles").value;
        const roles = rolesStr.split(",").map(r => r.trim());

        apiFetch(`/api/admin/users/${username}/roles`, {
            method: "PUT",
            body: JSON.stringify({roles})
        })
            .then(res => res.json())
            .then(data => document.getElementById("roleMessage").textContent = data.message)
            .catch(() => document.getElementById("roleMessage").textContent = "Error setting roles.");
    });

    document.getElementById("deleteUserForm").addEventListener("submit", function (e) {
        e.preventDefault();
        const username = document.getElementById("deleteUsername").value;

        apiFetch(`/api/admin/users/${username}`, {
            method: "DELETE"
        })
            .then(res => {
                if (!res.ok) throw new Error("Delete failed");
                return res.text();
            })
            .then(msg => document.getElementById("deleteMessage").textContent = msg)
            .catch(() => document.getElementById("deleteMessage").textContent = "Error deleting user.");
    });

    document.getElementById("loadInfoBtn").addEventListener("click", () => {
        fetch("/actuator/info")
            .then(res => res.json())
            .then(data => document.getElementById("infoResult").textContent = JSON.stringify(data, null, 2))
            .catch(() => document.getElementById("infoResult").textContent = "Failed to load info.");
    });
});
