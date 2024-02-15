window.onload = function () {
    showCreateForm(); // Trigger showCreateForm() when the page loads
};

function showCreateForm() {
    document.getElementById("createNodeForm").style.display = "block";
    document.getElementById("modifyNodeForm").style.display = "none";
    document.getElementById("deleteNodeForm").style.display = "none";
    document.getElementById("submitCreate").style.display = "block";
    document.getElementById("submitModify").style.display = "none";
    document.getElementById("submitDelete").style.display = "none";
}

function showModifyForm() {
    document.getElementById("createNodeForm").style.display = "none";
    document.getElementById("modifyNodeForm").style.display = "block";
    document.getElementById("deleteNodeForm").style.display = "none";
    document.getElementById("submitCreate").style.display = "none";
    document.getElementById("submitModify").style.display = "block";
    document.getElementById("submitDelete").style.display = "none";
}

function showDeleteForm() {
    document.getElementById("createNodeForm").style.display = "none";
    document.getElementById("modifyNodeForm").style.display = "none";
    document.getElementById("deleteNodeForm").style.display = "block";
    document.getElementById("submitCreate").style.display = "none";
    document.getElementById("submitModify").style.display = "none";
    document.getElementById("submitDelete").style.display = "block";
}

function doModify() {

    // Get values from form elements
    const nodeId = document.getElementById('idNode').value;
    const parentID = document.getElementById('nodeParent').value;
    const msgText = document.getElementById('newMsgText').value;
    const dialogNodeText = document.getElementById('newNodeText').value;

    // Prepare data for POST request
    const data = {
        dialogNodeId: nodeId,
        parentNodeId: parentID,
        msgText: msgText,
        dialogNodeText: dialogNodeText
    };

    // Log the request details to the console
    console.log('Request Details:', {
        method: 'POST',
        url: '../api/dialognode/modifyChild',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data),
    });

    // Make POST request using Fetch API
    fetch('../api/dialognode/modify', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data),
    })
        .then(response => response.json())
        .then(apiResponse => {
            // Log the API response.
            console.log('API Response:', apiResponse);
            const resultBox = document.getElementById('resultBox');
            resultBox.innerText = `Request sent + ${apiResponse.id? "modify successful" : "failure, see console for more details."}`;
        })
        .catch(error => {
            // Log errors
            console.error('Error:', error);
        });
}
function doDelete() {
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
}


function doCreate(){
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
        url: '../api/dialognode/createChild',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    });

    // Make POST request using Fetch API
    fetch('../api/dialognode/createChild', {
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
}

function handleFile() {
    const fileInput = document.getElementById('fileInput');
    const file = fileInput.files[0];

    if (file) {
        const reader = new FileReader();

        reader.onload = function(e) {
            const contents = e.target.result;
            processCSV(contents);
        };

        reader.readAsText(file);
    }
}

async function processCSV(contents) {
    // Split CSV content into rows
    let rows = contents.split('\n');
    let csvIdToRealId = new Map();  // Using Map instead of an object
    let data = {};

    // Process each row
    rows = rows.slice(1, rows.length);

    // Utility function to handle API calls sequentially
    const handleAPI = (row) => {
        return new Promise(async (resolve, reject) => {
            const columns = row.split(',');

            let parentId;

            if (columns[3].startsWith("QN")) {
                parentId = columns[3].replace('\r', '');
            } else {
                console.log(csvIdToRealId.get("n1"))
                console.log(csvIdToRealId.get(columns[3].replace('\r', '')));
                parentId = csvIdToRealId.get(columns[3].replace('\r', '')) || ''; // Using the Map's get method
            }

            // Setting up the data object
            data = {
                parentNodeId: parentId,
                msgText: columns[2],
                dialogNodeText: columns[1],
            };

            try {
                // Make the API call for the row
                const response = await fetch('../api/dialognode/createChild', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(data),
                });

                const apiResponse = await response.json();

                console.log('API Response:', apiResponse);

                // Populate the Map after getting the API response
                csvIdToRealId.set(columns[0], apiResponse.id);

                resolve(apiResponse);

            } catch (error) {
                console.error('Error:', error);
                reject(error);
            }
        });
    };

    // Chain API calls sequentially
    for (const row of rows) {
        try {
            await handleAPI(row);
        } catch (error) {
            console.error('Error processing row:', error);
        }
    }

}