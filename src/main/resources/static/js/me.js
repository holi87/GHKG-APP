document.addEventListener("DOMContentLoaded", () => {
    const userInfoDiv = document.getElementById("userInfo");
    const token = localStorage.getItem("token");

    if (!token) {
        userInfoDiv.innerHTML = `<div class="alert alert-danger">You are not authenticated. Please <a href="/login">login</a>.</div>`;
        return;
    }

    fetch("/api/me", {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token
        }
    })
        .then(res => {
            if (!res.ok) {
                if (res.status === 403 || res.status === 401) {
                    throw new Error("Access denied. Please log in again.");
                }
                throw new Error("Failed to fetch user info.");
            }
            return res.json();
        })
        .then(data => {
            userInfoDiv.innerHTML = `
                <p><strong>Username:</strong> ${data.username}</p>
                <p><strong>Roles:</strong> ${data.roles.join(', ')}</p>
            `;
        })
        .catch(err => {
            userInfoDiv.innerHTML = `<div class="alert alert-danger">${err.message}</div>`;
        });
});
