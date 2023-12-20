const chatbox = document.querySelector(".chatbox");
const chatbotToggler = document.querySelector(".chatbot-toggler");
const chatbotCloseBtn = document.querySelector(".close-btn");
const messageInput = document.querySelector('.message-input');
const typingIndicator = document.getElementById('typingIndicator');

const createChatBubble = (text, isBot = true) => {
    const chatLi = document.createElement("li");
    chatLi.classList.add("chat", isBot ? "incoming" : "outgoing");
    chatLi.innerHTML = isBot ? `<img src="https://i.imgur.com/0N92vb3.png" alt="Logo" class="chat-logo"><p>${text}</p>` : `<p>${text}</p>`;
    return chatLi;
};
const showTypingIndicator = () => {
    // Create a new chat bubble as the typing indicator
    const typingBubble = createChatBubble("THUBot is typing...", true);
    typingBubble.id = 'typingIndicator'; // Assign the ID to the new element
    chatbox.appendChild(typingBubble); // Add the typing indicator to the chatbox
    setTimeout(() => {
        typingBubble.remove(); // Remove the typing indicator from the DOM after 2 seconds
    }, 4000);
};


// Modified sendMessage function to send the user message to the server
const sendMessage = async () => {
    const message = messageInput.value.trim();
    if (message) {
        // Display the user message in the chatbox
        chatbox.appendChild(createChatBubble(message, false));
        messageInput.value = ''; // Clear the input after sending
        chatbox.scrollTop = chatbox.scrollHeight; // Scroll to the bottom

        // Show typing indicator while waiting for response
        showTypingIndicator();

        // Send the message to the server for processing
        try {
            const params = {
                userInput: message
            }
            const response = await fetch('../api/input/ask?' + new URLSearchParams(params).toString(), {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                }
            });
            const responseData = await response.json();

            // Hide typing indicator now that response has been received
            typingIndicator.style.display = 'none';

            // Process the response and display it in the chatbox
            if (responseData && responseData.msgText) {
                chatbox.appendChild(createChatBubble(responseData.msgText));
                if (responseData.children && responseData.children.length > 0) {
                    chatbox.appendChild(createResponseButtons(responseData.children));
                }
            } else {
                // Handle any case where responseData is not as expected
                chatbox.appendChild(createChatBubble("Sorry, I didn't understand that."));
            }
        } catch (error) {
            // Hide typing indicator if there's an error
            typingIndicator.style.display = 'none';
            console.error('Error sending message:', error);
            chatbox.appendChild(createChatBubble("There was an error processing your message."));
        }

        chatbox.scrollTop = chatbox.scrollHeight; // Scroll to the bottom
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

    // Show typing indicator
    showTypingIndicator();
    // Use setTimeout to wait for typing indicator before showing the chat bubble
    setTimeout(() => {
        // Append chat bubble after typing indicator has finished
        chatbox.appendChild(createChatBubble(node.msgText));
        if (node.children && node.children.length > 0) {
            chatbox.appendChild(createResponseButtons(node.children));
        }
        chatbox.scrollTop = chatbox.scrollHeight;
    }, 4000); // This should match the timeout used in showTypingIndicator
}
const fetchAndDisplayRootNodes = async () => {
    try {
        const response = await fetch('../api/dialognode/get');
        const data = await response.json();
        if (data && data.length > 0) {
            // Automatically select the first root node
            handleNodeSelection(data[0]);
        }
    } catch (error) {
        console.error('Error fetching data:', error);
    }
};

// Event listener for the Enter key in the message input

messageInput.addEventListener('keypress', (event) => {
    if (event.keyCode === 13) { // 13 is the Enter key
        event.preventDefault(); // Prevent default to avoid any form submission
        sendMessage(); // Call the function to send the message
    }
});
chatbotCloseBtn.addEventListener("click", () => document.body.classList.remove("show-chatbot"));
chatbotToggler.addEventListener("click", () => {
    document.body.classList.toggle("show-chatbot");
    fetchAndDisplayRootNodes();
});
