let sessionId = localStorage.getItem('sessionId') || generateSessionId();
localStorage.setItem('sessionId', sessionId);

document.getElementById("user-input").addEventListener("keypress", function (event) {
    if (event.key === "Enter") {
        sendMessage();
        event.preventDefault();
    }
});

function generateSessionId() {
    return Math.random().toString(36).substr(2, 9);
}

async function sendMessage() {
    const userInput = document.getElementById("user-input").value;
    if (userInput.trim() === "") return;

    const chatBox = document.getElementById("chat-box");
    chatBox.innerHTML += `<p class="user-message"><img src="images/user.png" class="avatar"> ${userInput}</p>`;
    document.getElementById("user-input").value = "";

    const typingIndicator = document.createElement("p");
    typingIndicator.id = "typing-indicator";
    typingIndicator.classList.add("bot-message");
    typingIndicator.innerHTML = `<img src="images/bot.png" class="avatar"> <span class="typing">...</span>`;
    chatBox.appendChild(typingIndicator);
    chatBox.scrollTop = chatBox.scrollHeight;

    setTimeout(async () => {
        const response = await fetch("/api/chat/send", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ message: userInput, sessionId: sessionId })
        });

        const data = await response.json();
        chatBox.removeChild(typingIndicator);
        if (data.response.toLowerCase().includes("resetată")) {
            chatBox.innerHTML = `<p class="bot-message"><img src="images/bot.png" class="avatar"> ${data.response}</p>`;
        } else {
            chatBox.innerHTML += `<p class="bot-message"><img src="images/bot.png" class="avatar"> ${data.response}</p>`;
        }
    }, 300);
}

function showLoginForm() {
    if (document.getElementById('adminLoginForm').style.display == 'block')
        document.getElementById('adminLoginForm').style.display = 'none';
    else
        document.getElementById('adminLoginForm').style.display = 'block';

}

// ✅ Trimitere date de autentificare către server
async function loginAdmin() {
    const username = document.getElementById('adminUsername').value.trim();
    const password = document.getElementById('adminPassword').value.trim();

    const response = await fetch('/admin/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password })
    });

    if (response.ok) {
        window.location.href = "admin.html";
    } else {
        alert("Username sau parolă incorecte.");
    }
}
document.addEventListener("DOMContentLoaded", () => {
    document.getElementById('fetchArtistsButton').addEventListener('click', fetchArtists);
});