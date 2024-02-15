import { visualizeTree } from './treeManipulation.js';

export async function fetchAndVisualizeTree() {
    try {
        const response = await fetch('/api/dialognode/get');
        const treeData = await response.json();
        console.log(treeData);
        if (!treeData) {
            throw new Error('No tree data');
        }

        d3.select("#database-map").selectAll("*").remove();

        return visualizeTree(treeData[0]);
    } catch (error) {
        console.error('Error fetching tree data:', error);
    }
}

fetchAndVisualizeTree();