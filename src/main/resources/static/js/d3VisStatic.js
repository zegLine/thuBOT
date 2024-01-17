var nodeToModify = null;
function setInitialDepths(root, depthIncrement) {
    if (root.children) {
        root.children.forEach(function (child) {
            child.y = (root.depth + 1) * depthIncrement;
            setInitialDepths(child, depthIncrement); 
        });
    }
}

function onResize(svg, treemap, margin, root, rectWidth, rectHeight, rectRoundness, i, depthSize) {
    var newContainerWidth = d3.select("#tree-cont-static").node().getBoundingClientRect().width;
    var newContainerHeight = window.innerHeight * 2;
    var newWidth = newContainerWidth - margin.left - margin.right;
    var newHeight = newContainerHeight - margin.top - margin.bottom;

    var nodes = root.descendants();
    var newWidth = nodes.length * rectWidth;

    svg.attr("viewBox", `0 ${-newHeight/2} ${newWidth + margin.left + margin.right} ${newHeight + margin.top + margin.bottom + 100}`);
    treemap.size([newWidth, newHeight]);

    update(svg, root, treemap, rectWidth, rectHeight, rectRoundness, i, depthSize, margin);
}

function trimText(nodeEnter, text, maxWidth) {
    var svgText = nodeEnter.append("text")
                     .attr("visibility", "hidden")
                     .text(text);

    if (svgText.node().getComputedTextLength() > maxWidth) {
        text = text.slice(0, text.length - (svgText.node().getComputedTextLength() - maxWidth) / 6); 
        text = text.slice(0, text.lastIndexOf(" ")); // 
        text += "...";
    }

    svgText.remove(); 
    return text;
}

function diagonal(s, d) {
    var path = `M ${s.x} ${s.y} L ${d.x} ${d.y}`; 
    return path;
    }

function updateMap() {
    
    console.log('About to fetch the latest data from the server');
    // Fetch the latest data from the server
    fetch('http://localhost:8080/api/dialognode/get', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => response.json())
        .then(data => {
            // Remove the old tree and visualize the new tree
            fetchAndVisualizeTree(data);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

window.doCreate = function () {
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

    const serverURL = 'http://localhost:8080/api/dialognode/createChild';
    // Log the request details to the console
    console.log('Request Details:', {
        method: 'POST',
        url: serverURL,
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    });

    // Make POST request using Fetch API
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
            return response.text();  // use text(), not json()
        })
        .then(data => {
            try {
                let jsonData = JSON.parse(data);
                // Log the API response.
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
    
function createNode(coordinates, root, svg, treemap, rectWidth, rectHeight, rectRoundness, i, depthSize, margin, selectedNode) {
    // Prompt the user for the new node's data
    var selectedNodeId = selectedNode.data.id;
    var newNodeDialogText = prompt('Enter dialog text for the new node:');
    if (newNodeDialogText === null) {
        // User cancelled the dialog, stop the node creation process
        return;
    }
    var newNodeMsgText = prompt('Enter message text for the new node:');
    if (newNodeMsgText === null) {
        // User cancelled the dialog, stop the node creation process
        return;
    }
    
    // Create a new node data object
    var newNodeData = {
        id: selectedNodeId, // This should be a new unique ID, not the selected node's ID
        dialogText: newNodeDialogText,
        msgText: newNodeMsgText,
        children: []
    };
    // Create a new node
    var newNode = d3.hierarchy(newNodeData);
    // Set the node's x and y coordinates
    newNode.x = coordinates[0] - margin.left;
    newNode.y = coordinates[1] - margin.top;
    // Add the new node to the root node's children
    if (!selectedNode.children) {
        selectedNode.children = [];
    }
    
    
    selectedNode.children.push(newNode);

    root = d3.hierarchy(root.data, function(d) { return d.children; });
    // Update the tree
    update(svg, root, treemap, rectWidth, rectHeight, rectRoundness, i, depthSize, margin);
        
    // Set the values of the hidden form elements
    document.getElementById('parentID').value = selectedNodeId;
    document.getElementById('msgText').value = newNodeData.msgText;
    document.getElementById('dialogNodeText').value = newNodeData.dialogText;
        
    // Call doCreate to send the new node data to the server
    console.log('doCreate called in D3VisStatic.js');
    window.doCreate();
    
}

window.doModify = function () {
    // Get values from form elements
    const dialogNodeId = document.getElementById('parentID').value;
    const parentNodeId = document.getElementById('newParentID').value; // Add this line
    const msgText = document.getElementById('msgText').value;
    const dialogNodeText = document.getElementById('dialogNodeText').value;

    // Prepare data for PUT request
    const data = {
        dialogNodeId: dialogNodeId,
        parentNodeId: parentNodeId, // Add this line
        msgText: msgText,
        dialogNodeText: dialogNodeText

    };

    // Log the request details to the console
    console.log('Request Details:', {
        method: 'POST',
        url: 'http://localhost:8080/api/dialognode/modify',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    });

    // Make POST request using Fetch API
    fetch('http://localhost:8080/api/dialognode/modify', {
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
            resultBox.innerText = `Request sent + ${apiResponse.id ? "modify successful" : "failure, see console for more details."}`;

            if (apiResponse.id) {
                updateMap();
            }
        })
        .catch(error => {
            // Log errors
            console.error('Error:', error);
        });
}

function modifyNode(selectedNode, root, svg, treemap, rectWidth, rectHeight, rectRoundness, i, depthSize, margin, newParentNodeId) {
    // Prompt the user for the new node's data
    var selectedNodeId = selectedNode.data.id;
    //var newParentNodeId = prompt('Enter the new parent node ID:');
    if (newParentNodeId === null) {
        // User cancelled the dialog, stop the node modification process
        return;
    }
    var newNodeDialogText = prompt('Enter new dialog text for the node:');
    if (newNodeDialogText === null) {
        // User cancelled the dialog, stop the node modification process
        return;
    }
    var newNodeMsgText = prompt('Enter new message text for the node:');
    if (newNodeMsgText === null) {
        // User cancelled the dialog, stop the node modification process
        return;
    }
    

    // Update the node's data
    selectedNode.data.dialogText = newNodeDialogText;
    selectedNode.data.msgText = newNodeMsgText;

    // Update the tree
    update(svg, root, treemap, rectWidth, rectHeight, rectRoundness, i, depthSize, margin);
        
    // Set the values of the hidden form elements
    document.getElementById('parentID').value = selectedNodeId;
    document.getElementById('newParentID').value = newParentNodeId; // Add this line
    document.getElementById('msgText').value = newNodeMsgText;
    document.getElementById('dialogNodeText').value = newNodeDialogText;
    
        
    // Call a function to send the updated node data to the server
    console.log('doModify  called in D3VisStatic.js');
    window.doModify();
    return newParentNodeId;
}

window.doDelete = function () {
    // Get the node ID from the hidden input field
    const nodeId = document.getElementById('deleteNodeID').value;

    // Prepare data for POST request
    const data = {
        dialogNodeId: nodeId
    };

    // Make POST request using Fetch API
    fetch('http://localhost:8080/api/dialognode/delete', {
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
            resultBox.innerText = `Request sent + ${apiResponse.id ? "delete successful" : "failure, see console for more details."}`;

            if (apiResponse.id) {
                updateMap();
            }
        })
        .catch(error => {
            // Log errors
            console.error('Error:', error);
        });
}

function deleteNode(selectedNodeId) {
    console.log('deleteNode called with ID:', selectedNodeId);

    // Get the element by its ID
    var element = document.getElementById('deleteNodeID');

    // Check if the element exists before trying to set its value
    if (element !== null) {
        element.value = selectedNodeId;
    } else {
        console.log('Element with ID deleteNodeID not found');
    }

    // Call a function to send the delete request to the server
    console.log('doDelete called in D3VisStatic.js');
    window.doDelete();
}

function visualizeTree(treeData) {

    var newParentNodeId = null;
    const rectWidth = 200;
    const rectHeight = 80;
    const rectRoundness = 5;
    var i = 0;
    var margin = { top: 50, right: 90, bottom: 30, left: 90 };

    var containerWidth = d3.select("#tree-cont-static").node().getBoundingClientRect().width;
    var containerHeight = window.innerHeight * 2;

    var root = d3.hierarchy(treeData, function (d) {
        return d.children;
    });

    var nodes = root.descendants();
    //var width = containerWidth - margin.left - margin.right;
    var height = containerHeight - margin.top - margin.bottom;
    var depthSize = 180;



    var width = nodes.length * rectWidth;

    root.x0 = width / 2;
    root.y0 = height / 2;

    var treemap = d3.tree().size([width, height]);

    var svgContainer = d3.select("#tree-cont-static").append("svg")
        .attr("width", '100%')
        .attr("viewBox", `0 0 ${width + margin.right + margin.left} ${height + margin.top + margin.bottom + 100}`);

    var svg = svgContainer.append("g")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    var zoom = d3.zoom()
        .scaleExtent([0.1, 10])
        .on("zoom", function (event) {
            svg.attr("transform", event.transform);
        });

    svgContainer.call(zoom);
    zoom.scaleTo(svgContainer, 0.7);



    setInitialDepths(root, depthSize);
    update(svg, root, treemap, rectWidth, rectHeight, rectRoundness, i, depthSize, margin);
    window.addEventListener('resize', function () {
        onResize(svg, treemap, margin, root, rectWidth, rectHeight, rectRoundness, i, depthSize);
    });


    d3.select('body').on('click', function (event) {
        // Check if the click was not on a node or the context menu
        if (!d3.select(event.target).classed('node-selected') && !d3.select(event.target).classed('context-menu')) {
            // Remove the 'node-selected' class from all nodes
            d3.selectAll('.node-selected').classed('node-selected', false);
            // Hide the context menu
            d3.select('#context-menu').style('display', 'none');
        }
    });

    d3.select('svg').on('contextmenu', function (event) {
        event.preventDefault();
    });
}
function update(svg, root, treemap, rectWidth, rectHeight, rectRoundness, i, depthSize, margin) {
   
    var tooltip = d3.select("body").append("div")
        .attr("class", "tooltip")
        .style("opacity", 0);
        
    var treeData = treemap(root);
    var nodes = treeData.descendants();
    var links = treeData.descendants().slice(1);

    var node = svg.selectAll('g.node')
        .data(nodes, function(d) { 
            return d.id || (d.id = ++i);
        });

    var nodeEnter = node.enter().append('g')
        .attr('class', 'node')
        .attr("transform", function(d) {
            return "translate(" + d.x + "," + d.y + ")";
        })

    d3.select('body').on('click', function (event) {
        // Check if the click was not on a node or the context menu
        if (!d3.select(event.target).classed('node-selected') && !d3.select(event.target).classed('context-menu')) {
            // Remove the 'node-selected' class from all nodes
            d3.selectAll('.node-selected').classed('node-selected', false);
        }
    });

    nodeEnter.append('rect')
        .attr('class', 'node')
        .attr('pointer-events', 'all')
        .attr('width', rectWidth)
        .attr('height', rectHeight)
        .attr('rx', rectRoundness)
        .attr('ry', rectRoundness)
        .attr('x', -rectWidth / 2)
        .attr('y', -rectHeight / 2)
        .style("fill", function (d) {
            return (!d.children && !d._children) ? "lightgreen" : (d._children ? "lightsteelblue" : "#fff");
        })
        .on('click', function (event, d) {
            if (nodeToModify !== null) {
                // A node is selected for modification, set this node as the new parent
                newParentNodeId = d.data.id;
                modifyNode(nodeToModify, root, svg, treemap, rectWidth, rectHeight, rectRoundness, i, depthSize, margin, newParentNodeId);
                // Reset nodeToModify and newParentNodeId
                nodeToModify = null;
                newParentNodeId = null;
                d3.selectAll('.node').classed('node-hover-enabled', false);
            } else {
                // No node is selected for modification, select this node
                nodeToModify = d;
            }
        })
        .on('contextmenu', function (event, d) {
            console.log('contextmenu event triggered');
            event.preventDefault();
            event.stopPropagation(); // Prevent the event from bubbling up to the svgContainer

            d3.select(this).classed('node-selected', true);
            var coordinates = d3.pointer(event);
            // Show the context menu
            var contextMenu = d3.select('#context-menu');
            contextMenu.style('display', 'block')
                .style('left', `${event.pageX}px`)
                .style('top', `${event.pageY}px`);

                d3.select('#create-node').on('click', function (event) {
                    event.stopPropagation(); // Add this line
                    contextMenu.style('display', 'none');
                    d3.selectAll('.node-selected').classed('node-selected', false);
                    // Pass the selected node
                    createNode(coordinates, root, svg, treemap, rectWidth, rectHeight, rectRoundness, i, depthSize, margin, d);
                });
                d3.select('#modify-node').on('click', function (event) {
                    event.stopPropagation();
                    contextMenu.style('display', 'none');
                    d3.selectAll('.node-selected').classed('node-selected', false);
                   
                    nodeToModify = d;
                    d3.selectAll('.node').classed('node-hover-enabled', true);
                });
                d3.select('#delete-node').on('click', function (event) {
                    console.log('Delete Node clicked. nodeToModify:', nodeToModify);
                    event.stopPropagation();
                    contextMenu.style('display', 'none');
                    d3.selectAll('.node-selected').classed('node-selected', false);
                   
                    // Set nodeToModify to the selected node
                    nodeToModify = d;
                
                    if (nodeToModify !== null) {
                        // Call the delete node function with the ID of the selected node
                        deleteNode(nodeToModify.data.id);
                    }
                });

            // Set node ID in form
            document.getElementById('parentID').value = d.data.id;
        })
        .on('mouseover', function (event, d) {
            tooltip.transition()
                .duration(200)
                .style("opacity", .9);
            tooltip.html(function () {
                var tooltipText = 'ID: ' + d.data.id;
                tooltipText += "<br>Dialog: " + d.data.dialogText;
                if (d.data.msgText) {
                    tooltipText += "<br>Message: " + d.data.msgText;
                }
                if (d.parent && d.parent.data.id !== "root") {
                    tooltipText += "<br>Parent ID: " + d.parent.data.id;
                }
                return tooltipText;
            })
                .style("left", (event.pageX) + "px")
                .style("top", (event.pageY - 28) + "px");
        })
        .on('mouseout', function (d) {
            tooltip.transition()
                .duration(500)
                .style("opacity", 0);
        });

    var nodeText = nodeEnter.append('text')
        .attr("x", -rectWidth / 2 + 10)
        .attr("y", -rectHeight / 2 + 5)
        .attr("text-anchor", "start")
        .style("fill", "black")
        .style("font-size", "10px")
        .style("pointer-events", "none");
        
    nodeText.append('tspan')
        .attr('x', -rectWidth / 2 + 10)
        .attr('dy', '1em')
        .text(function(d) { return 'ID: ' + d.data.id; });
        
    nodeText.append('tspan')
        .attr('x', -rectWidth / 2 + 10)
        .attr('dy', '1.5em') 
        .text(function(d) { 
            return 'Dialog: ' + trimText(nodeEnter, d.data.dialogText || '', rectWidth - 20); 
        });

    nodeText.append('tspan')
        .attr('x', -rectWidth / 2 + 10)
        .attr('dy', '1.5em')
        .text(function(d) { 
            return 'Message: ' + trimText(nodeEnter, d.data.msgText || '', rectWidth - 20); 
        });

    nodeText.append('tspan')
        .attr('x', -rectWidth / 2 + 10)
        .attr('dy', '1.5em') 
        .text(function(d) {
            return d.parent && d.parent.data.id !== "root" ? 'Parent ID: ' + d.parent.data.id : '';
        });
  
    var nodeUpdate = nodeEnter.merge(node);

    nodeUpdate.transition()
        .duration(750)
        .attr("transform", function(d) {
            return "translate(" + d.x + "," + d.y + ")";
        });

    var nodeExit = node.exit().transition()
        .duration(750)
        .attr("transform", function(d) {
            return "translate(" + d.x + "," + d.y + ")";
        })
        .remove();

    nodeExit.select('text')
        .style('fill-opacity', 1e-6);

    var link = svg.selectAll('path.link')
        .data(links, function(d) { 
            return d.id; 
        });

    var linkEnter = link.enter().insert('path', "g")
        .attr("class", "link")
        .style("stroke", "white")
        .attr('d', function(d){
            var o = {x: d.x, y: d.y}
            return diagonal(o, o)
        });

    linkEnter.merge(link).transition()
        .duration(750)
        .attr('d', function (d) {
            if (d.parent) {
                return diagonal(d, d.parent);
            } else {
                return null;
            }
        });
    
    var linkUpdate = linkEnter.merge(link);

    linkUpdate.transition()
        .duration(750)
        .attr('d', function (d) {
            if (d.parent) {
                return diagonal(d, d.parent);
            } else {
                return null;
            }
        });

    nodes.forEach(function(d){
        d.x0 = d.x;
        d.y0 = d.y;
    });
}