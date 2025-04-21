document.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.getElementById("loginForm");
    const loginMessage = document.getElementById("loginMessage");

    loginForm.addEventListener("submit", function (e) {
        e.preventDefault();

        const payload = {
            username: document.getElementById("username").value,
            password: document.getElementById("password").value
        };

        fetch("/api/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        })
            .then(response => {
                if (response.ok) return response.json();
                throw new Error("Login failed");
            })
            .then(data => {
                localStorage.setItem("token", data.token);
                loginMessage.textContent = "Login successful!";
                window.location.href = "/me";
            })
            .catch(err => {
                loginMessage.textContent = err.message;
            });
    });
});
