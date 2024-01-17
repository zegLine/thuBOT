import { doCreate, doModify, doDelete } from './apiInteractions.js';
import { update } from './treeManipulation.js';

export function createNode(coordinates, root, svg, treemap, rectWidth, rectHeight, rectRoundness, i, depthSize, margin, selectedNode) {

    var selectedNodeId = selectedNode.data.id;
    var newNodeDialogText = prompt('Enter dialog text for the new node:');

    if (newNodeDialogText === null) {
        return;
    }

    var newNodeMsgText = prompt('Enter message text for the new node:');

    if (newNodeMsgText === null) {
        return;
    }

    var newNodeData = {
        id: selectedNodeId,
        dialogText: newNodeDialogText,
        msgText: newNodeMsgText,
        children: []
    };
    
    var newNode = d3.hierarchy(newNodeData);
    newNode.x = coordinates[0] - margin.left;
    newNode.y = coordinates[1] - margin.top;
    
    if (!selectedNode.children) {
        selectedNode.children = [];
    }

    selectedNode.children.push(newNode);
    root = d3.hierarchy(root.data, function (d) { return d.children; });

    update(svg, root, treemap, rectWidth, rectHeight, rectRoundness, i, depthSize, margin);

    document.getElementById('parentID').value = selectedNodeId;
    document.getElementById('msgText').value = newNodeData.msgText;
    document.getElementById('dialogNodeText').value = newNodeData.dialogText;

    doCreate();
}

export function modifyNode(selectedNode, root, svg, treemap, rectWidth, rectHeight, rectRoundness, i, depthSize, margin, newParentNodeId) {

    var selectedNodeId = selectedNode.data.id;

    if (newParentNodeId === null) {
        return;
    }

    var newNodeDialogText = prompt('Enter new dialog text for the node:');

    if (newNodeDialogText === null) {
        d3.selectAll('.node-modifying').classed('node-modifying', false);
        return;
    }

    var newNodeMsgText = prompt('Enter new message text for the node:');

    if (newNodeMsgText === null) {
        d3.selectAll('.node-modifying').classed('node-modifying', false);
        return;
    }

    selectedNode.data.dialogText = newNodeDialogText;
    selectedNode.data.msgText = newNodeMsgText;

    update(svg, root, treemap, rectWidth, rectHeight, rectRoundness, i, depthSize, margin);

    document.getElementById('parentID').value = selectedNodeId;
    document.getElementById('newParentID').value = newParentNodeId;
    document.getElementById('msgText').value = newNodeMsgText;
    document.getElementById('dialogNodeText').value = newNodeDialogText;

    console.log('doModify  called in d3Map.js');
    doModify();

    return newParentNodeId;
}

export function deleteNode(selectedNodeId) {
    console.log('deleteNode called with ID:', selectedNodeId);

    var element = document.getElementById('deleteNodeID');

    if (element !== null) {
        element.value = selectedNodeId;
    } else {
        console.log('Element with ID deleteNodeID not found');
    }

    console.log('doDelete called in d3Map.js');
    doDelete();
}