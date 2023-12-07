const chatbox = document.querySelector(".chatbox");
const chatbotToggler = document.querySelector(".chatbot-toggler");
const chatbotCloseBtn = document.querySelector(".close-btn");

const createChatBubble = (text, isBot = true) => {
    const chatLi = document.createElement("li");
    chatLi.classList.add("chat", isBot ? "incoming" : "outgoing");
    chatLi.innerHTML = isBot ? `<img src="https://i.imgur.com/0N92vb3.png" alt="Logo" class="chat-logo"><p>${text}</p>` : `<p>${text}</p>`;
    return chatLi;
};

const createResponseButtons = (children) => {
    const buttonsDiv = document.createElement("div");
    buttonsDiv.classList.add("response-buttons");

    children.forEach(child => {
        const button = document.createElement("button");
        button.classList.add("btn-response");
        button.textContent = child.dialogText;
        button.onclick = () => handleNodeSelection(child);
        buttonsDiv.appendChild(button);
    });

    return buttonsDiv;
};

const handleNodeSelection = (node) => {
    chatbox.appendChild(createChatBubble(node.msgText));
    if (node.children && node.children.length > 0) {
        chatbox.appendChild(createResponseButtons(node.children));
    }
    chatbox.scrollTop = chatbox.scrollHeight;
};

const fetchAndDisplayRootNodes = async () => {
    try {
        const response = await fetch('http://localhost:8080/api/dialognode/get');
        const data = await response.json();
        if (data && data.length > 0) {
            // Automatically select the first root node
            handleNodeSelection(data[0]);
        }
    } catch (error) {
        console.error('Error fetching data:', error);
    }
};


chatbotCloseBtn.addEventListener("click", () => document.body.classList.remove("show-chatbot"));
chatbotToggler.addEventListener("click", () => {
    document.body.classList.toggle("show-chatbot");
    fetchAndDisplayRootNodes();
});
