

const chatInput = document.querySelector(".chat-input textarea");
const sendChatBtn = document.querySelector(".chat-input span");
const chatbox = document.querySelector(".chatbox");
const chatbotToggler = document.querySelector(".chatbot-toggler");
const chatbotCloseBtn = document.querySelector(".close-btn");
let userMessage;

const createChatLi = (message, className) => {
    const chatLi = document.createElement("li");
    chatLi.classList.add("chat", className);
    let chatContent = className === "outgoing" ? `<p>${message}</p>` : `<img src="https://i.imgur.com/0N92vb3.png" alt="Logo" class="chat-logo"><p>${message}</p>`;
    chatLi.innerHTML = chatContent;
    return chatLi;
}

const handleChat = (e) => {
    if (e.type === "click" || e.key === "Enter") {
        userMessage = chatInput.value.trim();
        if (!userMessage) return;
        chatbox.appendChild(createChatLi(userMessage, "outgoing"));
        chatInput.value = "";
        chatbox.scrollTop = chatbox.scrollHeight;
    }
}


sendChatBtn.addEventListener("click", handleChat);
chatInput.addEventListener("keyup", handleChat);
chatbotCloseBtn.addEventListener("click", () => document.body.classList.remove("show-chatbot"));
chatbotToggler.addEventListener("click", () => document.body.classList.toggle("show-chatbot"));


