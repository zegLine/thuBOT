import { updateMap } from './treeManipulation.js';

export function doCreate() {
    
    const parentID = document.getElementById('parentID').value;
    const msgText = document.getElementById('msgText').value;
    const dialogNodeText = document.getElementById('dialogNodeText').value;
    const data = {
        parentNodeId: parentID,
        msgText: msgText,
        dialogNodeText: dialogNodeText,
    };
    const serverURL = 'http://localhost:8080/api/dialognode/createChild';
    
    console.log('Request Details:', {
        method: 'POST',
        url: serverURL,
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    });

    fetch(serverURL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.text();
        })
        .then(data => {
            try {
                let jsonData = JSON.parse(data);
                console.log('API Response:', jsonData);
                const resultBox = document.getElementById('resultBox');
                resultBox.innerText = `Request sent + ${jsonData.id ? "success" : "failure, see console for more details."}`;

                if (jsonData.id) {
                    updateMap();
                }

            } catch (error) {
                console.error('Parsing error:', error);
                console.error('Raw response:', data);
            }
        })
        .catch(error => {
            console.error('Fetch error:', error);
        });
}


export function doModify() {
    
    const dialogNodeId = document.getElementById('parentID').value;
    const parentNodeId = document.getElementById('newParentID').value;
    const msgText = document.getElementById('msgText').value;
    const dialogNodeText = document.getElementById('dialogNodeText').value;
    const data = {
        dialogNodeId: dialogNodeId,
        parentNodeId: parentNodeId,
        msgText: msgText,
        dialogNodeText: dialogNodeText

    };

    console.log('Request Details:', {
        method: 'POST',
        url: 'http://localhost:8080/api/dialognode/modify',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    });

    fetch('http://localhost:8080/api/dialognode/modify', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    })
        .then(response => response.json())
        .then(apiResponse => {
            console.log('API Response:', apiResponse);
            const resultBox = document.getElementById('resultBox');
            resultBox.innerText = `Request sent + ${apiResponse.id ? "modify successful" : "failure, see console for more details."}`;

            if (apiResponse.id) {
                updateMap();
            }

        })
        .catch(error => {
            console.error('Error:', error);
        });
}


export function doDelete() {
    
    const nodeId = document.getElementById('deleteNodeID').value;
    const data = {
        dialogNodeId: nodeId
    };

    fetch('http://localhost:8080/api/dialognode/delete', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    })
        .then(response => response.json())
        .then(apiResponse => {
            console.log('API Response:', apiResponse);
            const resultBox = document.getElementById('resultBox');
            resultBox.innerText = `Request sent + ${apiResponse.id ? "delete successful" : "failure, see console for more details."}`;

            if (apiResponse.id) {
                updateMap();
            }

        })
        .catch(error => {
            console.error('Error:', error);
        });
}
