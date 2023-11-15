function toggleChildren(nodeElement) {
    const children = nodeElement.nextElementSibling;
    if (children) {
        children.classList.toggle('hidden');
    }
}

function createNodeElement(entity) {
    const nodeDiv = document.createElement('div');
    nodeDiv.className = 'node';
    nodeDiv.innerHTML = `<span class="dialog-text">${entity.dialogText}</span> - <span class="msg-text">${entity.msgText}</span>`;
    nodeDiv.onclick = () => toggleChildren(nodeDiv);
    return nodeDiv;
}

function createMap(entities, parentElement) {
    entities.forEach(entity => {
        const nodeElement = createNodeElement(entity);
        parentElement.appendChild(nodeElement);

        if (entity.children && entity.children.length) {
            const childrenContainer = document.createElement('div');
            childrenContainer.className = 'connection hidden';
            parentElement.appendChild(childrenContainer);
            createMap(entity.children, childrenContainer);
        }
    });
}

function fetchDataAndDisplayMap() {
    fetch('/api/dialognode/get')
        .then(response => response.json())
        .then(data => {
            const container = document.getElementById('map-container');
            createMap(data, container);
        })
        .catch(error => {
            console.error('Error fetching data:', error);
        });
}

fetchDataAndDisplayMap();
