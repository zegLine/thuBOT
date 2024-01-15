const chatbox = document.querySelector(".chatbox");
const chatbotToggler = document.querySelector(".chatbot-toggler");
const chatbotCloseBtn = document.querySelector(".close-btn");
const messageInput = document.querySelector('.message-input');

const maxMessageLength = 50;

const createChatBubble = (text, isBot = true) => {
    const chatLi = document.createElement("li");
    chatLi.classList.add("chat", isBot ? "incoming" : "outgoing");

    // Create a paragraph element for text
    const p = document.createElement("p");
    p.innerText = text.length > maxMessageLength ? text.slice(0, maxMessageLength) + '...' : text;

    // If the text is too long, add a "Read more" button
    if (text.length > maxMessageLength) {
        const readMoreBtn = document.createElement("button");
        readMoreBtn.classList.add("read-more-btn");
        readMoreBtn.innerText = "Read more";
        // Function to toggle the message text
        readMoreBtn.onclick = () => {
            p.innerText = text;
            readMoreBtn.remove();
        };
        p.appendChild(readMoreBtn);
    }

    // Append the paragraph to the chat list item
    chatLi.appendChild(p);

    if(isBot) {
        const img = document.createElement("img");
        img.src = "https://i.imgur.com/0N92vb3.png";
        img.alt = "Logo";
        img.classList.add("chat-logo");
        chatLi.insertBefore(img, p);
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
    chatbox.appendChild(typingBubble);
    setTimeout(() => { typingBubble.remove(); }, 2000);
};

// Function to handle the sending of messages and receiving of responses
// Function to handle the sending of messages and receiving of responses
const sendMessage = async (message) => {
    if (message) {
        chatbox.appendChild(createChatBubble(message, false));
        messageInput.value = '';
        chatbox.scrollTop = chatbox.scrollHeight;
        showTypingIndicator(); // Show typing indicator

        try {
            const params = { userInput: message };
            const response = await fetch('../api/input/ask?' + new URLSearchParams(params).toString(), {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' }
            });
            const responseData = await response.json();
            chatbox.querySelector('.chat:last-child').remove(); // Remove typing indicator immediately after getting the response

            if (responseData && responseData.msgText) {
                chatbox.appendChild(createChatBubble(responseData.msgText));
                if (responseData.children) {
                    chatbox.appendChild(createResponseButtons(responseData.children));
                }
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
    showTypingIndicator();
    setTimeout(() => {
        chatbox.appendChild(createChatBubble(node.msgText));
        if (node.children) {
            chatbox.appendChild(createResponseButtons(node.children));
        }
        chatbox.scrollTop = chatbox.scrollHeight;
    }, 2000);
};

const fetchAndDisplayRootNodes = async () => {
    try {
        const response = await fetch('../api/dialognode/get');
        const data = await response.json();
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

chatbotCloseBtn.addEventListener("click", () => {
    clearChat();
    document.body.classList.remove("show-chatbot");
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
    
    // Toggle full screen function
    function toggleFullScreen() {
        chatbot.classList.toggle('open-in-full');
    }
    
    document.querySelector('.open-in-full').addEventListener('click', toggleFullScreen);
    document.querySelector('.close-btn').addEventListener('click', toggleFullScreen);
});



