const authMessage = document.getElementById("authMessage");
const registerForm = document.getElementById("registerForm");

showLoginStatusMessage();

if (registerForm) {
    registerForm.addEventListener("submit", async (event) => {
        event.preventDefault();
        const username = document.getElementById("registerUsername").value.trim();
        const password = document.getElementById("registerPassword").value;

        try {
            const response = await fetch("/api/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Accept: "application/json"
                },
                body: JSON.stringify({ username, password })
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || `Registration failed with status ${response.status}`);
            }

            showAuthMessage("Account created. You can sign in now.", "success");
            registerForm.reset();
        } catch (error) {
            console.error("Failed to register user", error);
            showAuthMessage("Failed to create account. Try another username.", "error");
        }
    });
}

function showLoginStatusMessage() {
    const params = new URLSearchParams(window.location.search);
    if (params.has("error")) {
        showAuthMessage("Invalid username or password.", "error");
    } else if (params.has("logout")) {
        showAuthMessage("You have signed out.", "success");
    }
}

function showAuthMessage(message, type) {
    if (!authMessage) {
        return;
    }
    authMessage.textContent = message;
    authMessage.className = `help-text ${type}`;
}
