function fetchAndVisualizeTree() {
    fetch('/api/dialognode/get')
        .then(response => response.json())
        .then(treeData => {
            console.log(treeData);
            if (!treeData) {
                throw new Error('No tree data');
            }

            d3.select("#tree-cont-static").selectAll("*").remove();

            visualizeTree(treeData[0]);
        })
        .catch(error => {
            console.error('Error fetching tree data:', error);
        });
}

fetchAndVisualizeTree();