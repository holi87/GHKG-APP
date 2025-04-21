document.addEventListener("DOMContentLoaded", function () {
    const token = localStorage.getItem("token");

    if (token) {
        document.getElementById("meLink").style.display = "list-item";
        const loginLink = document.getElementById("loginLink");
        if (loginLink) loginLink.style.display = "none";

        const authActions = document.getElementById("authActions");
        if (authActions) {
            const logoutBtn = document.createElement("button");
            logoutBtn.textContent = "Logout";
            logoutBtn.className = "btn btn-danger";
            logoutBtn.onclick = function () {
                localStorage.removeItem("token");
                window.location.href = "/";
            };
            authActions.appendChild(logoutBtn);
        }

        fetch("/api/me", {
            headers: {
                "Authorization": "Bearer " + token
            }
        })
            .then(response => {
                if (!response.ok) {
                    console.warn("Token seems invalid or expired.");
                    localStorage.removeItem("token");
                    return null;
                }
                return response.json();
            })
            .then(data => {
                if (!data) return;
                const roles = data.roles || [];
                if (roles.includes("ADMIN")) {
                    document.getElementById("adminLink").style.display = "list-item";
                }
            })
            .catch(err => {
                console.error("Error fetching user info:", err);
                localStorage.removeItem("token");
            });
    }
});
