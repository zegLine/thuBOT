const chatbox = document.querySelector(".chatbox");
const chatbotToggler = document.querySelector(".chatbot-toggler");
const chatbotCloseBtn = document.querySelector(".close-btn");
const messageInput = document.querySelector('.message-input');
const sendIcon = document.querySelector('#send-btn');
const maxMessageLength = 50;

let isBotTyping = false;
let currentDialogNodeId = "QN0000";

const startBotTyping = () => {
    isBotTyping = true;
    showTypingIndicator();

    messageInput.disabled = true;
    messageInput.placeholder = "Thinking...";

    console.log("Bot Started Typing");
};

const endBotTyping = () => {
    isBotTyping = false;
    document.getElementById('typing-indicator').remove();

    messageInput.disabled = false;
    messageInput.placeholder = "Enter your message here";
    console.log("Bot Stopped Typing");
};

const createChatBubble = (text, isBot = true) => {
    const chatLi = document.createElement("li");
    chatLi.classList.add("chat", isBot ? "incoming" : "outgoing");

    let chatElement;
    chatElement = document.createElement("p");
    // Handle URLS differently
    if (isValidHttpUrl(text)) {
        const aelem = document.createElement("a");
        aelem.href = text;
        aelem.innerText = "Here's what I found on the THU website";
        chatElement.appendChild(aelem);
    } else {
        // Create a paragraph element for text
        chatElement.innerText = text.length > maxMessageLength ? text.slice(0, maxMessageLength) + '...' : text;

        // If the text is too long, add a "Read more" button
        if (text.length > maxMessageLength) {
            const readMoreBtn = document.createElement("button");
            readMoreBtn.classList.add("read-more-btn");
            readMoreBtn.innerText = "Read more";
            // Function to toggle the message text
            readMoreBtn.onclick = () => {
                chatElement.innerText = text;
                readMoreBtn.remove();
            };
            chatElement.appendChild(readMoreBtn);
        }
        // Append the paragraph to the chat list item

    }

    chatLi.appendChild(chatElement);

    if(isBot) {
        const img = document.createElement("img");
        img.src = "https://i.imgur.com/0N92vb3.png";
        img.alt = "Logo";
        img.classList.add("chat-logo");
        chatLi.insertBefore(img, chatElement);
    }

    return chatLi;
};

const clearChat = () => {
    chatbox.innerHTML = ''; // Clears the chat history
};
const toggleText = (button, fullText) => {
    const p = button.previousElementSibling; // The <p> element with the text
    p.innerText = fullText;
    button.remove(); // Remove the button after expanding the text
};

const showTypingIndicator = () => {
    const typingBubble = createChatBubble("THUBot is typing...", true);
    typingBubble.id = 'typing-indicator';
    chatbox.appendChild(typingBubble);
};

// Function to handle the sending of messages and receiving of responses
// Function to handle the sending of messages and receiving of responses
const sendMessage = async (message) => {
    if (message) {
        chatbox.appendChild(createChatBubble(message, false));
        messageInput.value = '';
        chatbox.scrollTop = chatbox.scrollHeight;


        try {
            const params = { userInput: message, currentNodeId: currentDialogNodeId };
            startBotTyping();
            const response = await fetch('../api/input/ask?' + new URLSearchParams(params).toString(), {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' }
            });
            const responseData = await response.json();
            endBotTyping();
            if (responseData && responseData.msgText) {
                handleNodeSelection(responseData);
            } else {
                chatbox.appendChild(createChatBubble("Sorry, I didn't understand that."));
            }
        } catch (error) {
            console.error('Error sending message:', error);
            chatbox.appendChild(createChatBubble("There was an error processing your message."));
        }
        chatbox.scrollTop = chatbox.scrollHeight;
    }
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
    currentDialogNodeId = node.id;
    console.log('Current Node Id: ' + currentDialogNodeId);
    chatbox.appendChild(createChatBubble(node.msgText));
    if (node.children && node.children.length !== 0) {
        chatbox.appendChild(createResponseButtons(node.children));
    } else {
        chatbox.appendChild(createEndNode());
    }
    chatbox.scrollTop = chatbox.scrollHeight;
};

const createEndNode = () => {
    const buttonsDiv = document.createElement("div");
    buttonsDiv.classList.add("response-buttons");
    const button = document.createElement("button");
    button.classList.add("btn-response");
    button.textContent = "Start Again";
    button.onclick = () => fetchAndDisplayRootNodes();
    buttonsDiv.appendChild(button);
    return buttonsDiv;
}

const fetchAndDisplayRootNodes = async () => {
    try {
        startBotTyping();
        const response = await fetch('../api/dialognode/get');
        const data = await response.json();
        endBotTyping();
        if (data && data.length > 0) {
            handleNodeSelection(data[0]);
        }
    } catch (error) {
        console.error('Error fetching data:', error);
    }
};

messageInput.addEventListener('keypress', (event) => {
    if (event.key === 'Enter') {
        event.preventDefault();
        sendMessage(messageInput.value.trim());
    }
});

sendIcon.addEventListener("click", () => {
   sendMessage(messageInput.value.trim());
});

let isChatbotOpenedBefore = false;

// Updated chatbotToggler event listener
chatbotToggler.addEventListener("click", () => {
    // Toggle the chatbot visibility
    document.body.classList.toggle("show-chatbot");

    // Check if the chatbot is being opened
    if (document.body.classList.contains("show-chatbot")) {
        // If it's the first time opening the chatbot, load the root nodes
        if (!isChatbotOpenedBefore) {
            fetchAndDisplayRootNodes();
            isChatbotOpenedBefore = true; // Set to true after first opening
        }
    }
});

//Button full size and closing
document.addEventListener('DOMContentLoaded', function() {
    const chatbot = document.querySelector('.chatbot');
    chatbotCloseBtn.style.display = "none";
    // Toggle full screen function
    function toggleFullScreen() {
        if (chatbot.classList.toggle('open-in-full')) {
            chatbotCloseBtn.style.display = "inline-block";
            console.log("fullscreen");
        } else {
            chatbotCloseBtn.style.display = "none";
            console.log("small screen");

        }
    }
    
    document.querySelector('.open-in-full').addEventListener('click', toggleFullScreen);
    document.querySelector('.close-btn').addEventListener('click', toggleFullScreen);
});

function isValidHttpUrl(string) {
    let url;

    try {
        url = new URL(string);
    } catch (_) {
        return false;
    }

    return url.protocol === "http:" || url.protocol === "https:";
}



