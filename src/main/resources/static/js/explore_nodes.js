function hideSiblingsExcept(element) {
    let sibling = element.parentElement.firstChild;
    while (sibling) {
        if (sibling !== element && sibling.classList.contains('node')) {
            sibling.style.display = 'none';
        }
        sibling = sibling.nextSibling;
    }
}

function showChildren(nodeElement) {
    const childrenContainer = nodeElement.nextElementSibling;
    if (childrenContainer && childrenContainer.classList.contains('connection')) {
        childrenContainer.style.display = 'block';
    }
}

function nodeClickHandler(nodeElement) {
    console.log('Node clicked:', nodeElement); // Log to check if the click is registered
    hideSiblingsExcept(nodeElement);
    showChildren(nodeElement);
}

function createNodeElement(entity) {
    const nodeDiv = document.createElement('div');
    nodeDiv.className = 'node';
    nodeDiv.innerHTML = `<span class="dialog-text">${entity.dialogText}</span> - <span class="msg-text">${entity.msgText}</span>`;
    nodeDiv.onclick = () => nodeClickHandler(nodeDiv);
    return nodeDiv;
}

function createMap(entities, parentElement, isRoot = false) {
    const container = document.createElement('div');
    container.className = 'connection';
    if (!isRoot) {
        container.classList.add('hidden');
    }
    parentElement.appendChild(container);

    entities.forEach(entity => {
        const nodeElement = createNodeElement(entity);
        container.appendChild(nodeElement);

        if (entity.children && entity.children.length) {
            createMap(entity.children, nodeElement);
        }
    });
}

function fetchDataAndDisplayMap() {
    fetch('http://localhost:8080/api/dialognode/get')
        .then(response => response.json())
        .then(data => {
            console.log('Data received:', data); // Log to check if data is received correctly
            const container = document.getElementById('map-container');
            createMap(data, container, true);
        })
        .catch(error => {
            console.error('Error fetching data:', error);
        });
}

fetchDataAndDisplayMap();
