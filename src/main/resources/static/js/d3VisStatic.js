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

    svg.attr("viewBox", `0 0 ${newWidth + margin.left + margin.right} ${newHeight + margin.top + margin.bottom + 100}`);
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
    // Fetch the latest data from the server
    fetch('http://localhost:8080/api/dialognode', {
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
    
function createNode(coordinates, root, svg, treemap, rectWidth, rectHeight, rectRoundness, i, depthSize, margin) {
    // Prompt the user for the new node's data
    var newNodeId = prompt('Enter Parent ID of Node that you want be of leaf of:');
    if (newNodeId === null) {
        // User cancelled the dialog, stop the node creation process
        return;
    }
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
        id: newNodeId,
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
    root.children.push(newNode);

    root = d3.hierarchy(root.data, function(d) { return d.children; });
    // Update the tree
    update(svg, root, treemap, rectWidth, rectHeight, rectRoundness, i, depthSize, margin);
        
    // Set the values of the hidden form elements
    document.getElementById('parentID').value = newNodeId;
    document.getElementById('msgText').value = newNodeData.msgText;
    document.getElementById('dialogNodeText').value = newNodeData.dialogText;
        
    // Call doCreate to send the new node data to the server
    console.log('doCreate called in D3VisStatic.js');
    window.doCreate();
    
}

function visualizeTree(treeData) {

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

    svgContainer.on('contextmenu', function (event) {
        event.preventDefault();
        event.stopPropagation(); // Add this line
        var coordinates = d3.pointer(event);
        // Show the context menu
        var contextMenu = d3.select('#context-menu');
        contextMenu.style('display', 'block')
            .style('left', `${event.pageX}px`)
            .style('top', `${event.pageY}px`);

        d3.select('#create-node').on('click', function () {
            contextMenu.style('display', 'none');
            createNode(coordinates, root, svg, treemap, rectWidth, rectHeight, rectRoundness, i, depthSize, margin);
        });
    });

    d3.select('body').on('click', function () {
        d3.select('#context-menu').style('display', 'none');
    });
}
function update(svg, root, treemap, rectWidth, rectHeight, rectRoundness, i, depthSize, margin) {
        
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
            
    nodeEnter.append('rect')
        .attr('class', 'node')
        .attr('width', rectWidth)
        .attr('height', rectHeight)
        .attr('rx', rectRoundness)
        .attr('ry', rectRoundness)
        .attr('x', -rectWidth / 2)
        .attr('y', -rectHeight / 2)
        .style("fill", function(d) {
            return (!d.children && !d._children) ? "lightgreen" : (d._children ? "lightsteelblue" : "#fff");
        });

    nodeEnter.append('title')
        .text(function(d) {
            var tooltipText = 'ID: ' + d.data.id;
            tooltipText += "\nDialog: " + d.data.dialogText;
            if (d.data.msgText) {
                tooltipText += "\nMessage: " + d.data.msgText;
            }
            if (d.parent && d.parent.data.id !== "root") {
                tooltipText += "\nParent ID: " + d.parent.data.id;
            }
            return tooltipText;
        });

    var nodeText = nodeEnter.append('text')
        .attr("x", -rectWidth / 2 + 10)
        .attr("y", -rectHeight / 2 + 5)
        .attr("text-anchor", "start")
        .style("fill", "black")
        .style("font-size", "10px");
        
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
        .attr('d', function(d) {
            return diagonal(d, d.parent);
        });

    var linkUpdate = linkEnter.merge(link);

    linkUpdate.transition()
    .duration(750)
    .attr('d', function(d){ return diagonal(d, d.parent) });

    nodes.forEach(function(d){
        d.x0 = d.x;
        d.y0 = d.y;
    });
}