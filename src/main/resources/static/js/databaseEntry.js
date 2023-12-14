document.getElementById('createNodeForm').addEventListener('submit', function (event) {
    event.preventDefault();

    // Get values from form elements
    const parentID = document.getElementById('parentID').value;
    const msgText = document.getElementById('msgText').value;
    const dialogNodeText = document.getElementById('dialogNodeText').value;
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    // Prepare data for POST request
    const data = {
        parentNodeId: parentID,
        msgText: msgText,
        dialogNodeText: dialogNodeText,
    };

    // Log the request details to the console
    console.log('Request Details:', {
        method: 'POST',
        url: '../api/dialognode/createChild',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Basic ' + btoa(username + ':' + password),
        },
        body: JSON.stringify(data),
    });

    // Make POST request using Fetch API
    fetch('../api/dialognode/createChild', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Basic ' + btoa(username + ':' + password),
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
