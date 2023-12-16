
function fetchAndVisualizeTree() {
    fetch('/api/dialognode/tree')
    .then(response => response.json())
    .then(treeData => {
        console.log(treeData);
        if (!treeData) {
            throw new Error('No tree data');
        }
        
        d3.select("#tree-container").selectAll("*").remove();

        visualizeTree(treeData[0]); 
    })
    .catch(error => {
        console.error('Error fetching tree data:', error);
    });
}

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

fetchAndVisualizeTree();

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
        console.log('API Response:', apiResponse);
        if(apiResponse.id) {
            
            fetchAndVisualizeTree();
        } else {
            throw new Error('Node creation failed');
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
});
