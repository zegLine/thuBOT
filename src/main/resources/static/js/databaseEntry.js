function showCreateForm() {
    document.getElementById("createNodeForm").style.display = "block";
    document.getElementById("modifyNodeForm").style.display = "none";
    document.getElementById("deleteNodeForm").style.display = "none";
}

function showModifyForm() {
    document.getElementById("createNodeForm").style.display = "none";
    document.getElementById("modifyNodeForm").style.display = "block";
    document.getElementById("deleteNodeForm").style.display = "none";
}

function showDeleteForm() {
    document.getElementById("createNodeForm").style.display = "none";
    document.getElementById("modifyNodeForm").style.display = "none";
    document.getElementById("deleteNodeForm").style.display = "block";
}

document.getElementById('deleteNodeForm').addEventListener('submit', function (event) {
    event.preventDefault();
    const nodeId = document.getElementById('deleteNodeID').value;
    const data = {
        "dialogNodeId": nodeId
    }
    // Make POST request using Fetch API
    fetch('../api/dialognode/delete', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    })
        .then(response => response.json())
        .then(apiResponse => {
            // Log the API response.
            console.log('API Response:', apiResponse);
            const resultBox = document.getElementById('resultBox');
            resultBox.innerText = `Request sent + ${apiResponse.id? "success" : "failure, see console for more details."}`;``
        })
        .catch(error => {
            // Log errors
            console.error('Error:', error);
        });
});


document.getElementById('createNodeForm').addEventListener('submit', function (event) {
    event.preventDefault();

    // Get values from form elements
    const parentID = document.getElementById('parentID').value;
    const msgText = document.getElementById('msgText').value;
    const dialogNodeText = document.getElementById('dialogNodeText').value;

    // Prepare data for POST request
    const data = {
        parentNodeId: parentID,
        msgText: msgText,
        dialogNodeText: dialogNodeText,
    };

    // Log the request details to the console
    console.log('Request Details:', {
        method: 'POST',
        url: '../api/dialognode/create',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    });

    // Make POST request using Fetch API
    fetch('../api/dialognode/create', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    })
        .then(response => response.json())
        .then(apiResponse => {
            // Log the API response.
            console.log('API Response:', apiResponse);
            const resultBox = document.getElementById('resultBox');
            resultBox.innerText = `Request sent + ${apiResponse.id? "success" : "failure, see console for more details."}`;``
        })
        .catch(error => {
            // Log errors
            console.error('Error:', error);
        });
});
