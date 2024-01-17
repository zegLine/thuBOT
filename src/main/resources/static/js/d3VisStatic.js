var nodeToModify = null;
var isModifying = false;

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

    svg.attr("viewBox", `0 0 ${newWidth + margin.left + margin.right} ${newHeight + margin.top + margin.bottom}`);
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
    
    fetch('http://localhost:8080/api/dialognode/get', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => response.json())
        .then(data => {
            fetchAndVisualizeTree(data);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

window.doCreate = function () {
    
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

function createNode(coordinates, root, svg, treemap, rectWidth, rectHeight, rectRoundness, i, depthSize, margin, selectedNode) {

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

    window.doCreate();
}

window.doModify = function () {
    
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

function modifyNode(selectedNode, root, svg, treemap, rectWidth, rectHeight, rectRoundness, i, depthSize, margin, newParentNodeId) {

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

    console.log('doModify  called in D3VisStatic.js');
    window.doModify();

    return newParentNodeId;
}

window.doDelete = function () {
    
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

function deleteNode(selectedNodeId) {
    console.log('deleteNode called with ID:', selectedNodeId);

    var element = document.getElementById('deleteNodeID');

    if (element !== null) {
        element.value = selectedNodeId;
    } else {
        console.log('Element with ID deleteNodeID not found');
    }

    console.log('doDelete called in D3VisStatic.js');
    window.doDelete();
}

function visualizeTree(treeData) {

    const rectWidth = 200;
    const rectHeight = 80;
    const rectRoundness = 5;
    var i = 0;
    var margin = { top: 50, right: 90, bottom: 30, left: 90 };
    var containerHeight = window.innerHeight * 2;

    var root = d3.hierarchy(treeData, function (d) {
        return d.children;
    });

    var nodes = root.descendants();
    var height = containerHeight - margin.top - margin.bottom;
    var depthSize = 180;
    var width = nodes.length * rectWidth;
    root.x0 = width / 2;
    root.y0 = height / 2;
    var treemap = d3.tree().size([width, height]);

    var svgContainer = d3.select("#tree-cont-static").append("svg")
        .attr("width", '100%')
        .attr("height", '100%')
        .attr("viewBox", `0 0 ${width + margin.right + margin.left} ${height}`);

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
        
        if (!d3.select(event.target).classed('node-selected') && !d3.select(event.target).classed('context-menu')) {
            d3.selectAll('.node-selected').classed('node-selected', false);
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
        .data(nodes, function (d) {
            return d.id || (d.id = ++i);
        });

    var nodeEnter = node.enter().append('g')
        .attr('class', 'node')
        .attr("transform", function (d) {
            return "translate(" + d.x + "," + d.y + ")";
        })

    d3.select('body').on('click', function (event) {
    
        if (!d3.select(event.target).classed('node-selected') && !d3.select(event.target).classed('context-menu')) {
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

        .on('click', function (_, d) {
            if (isModifying) {
                if (nodeToModify !== null) {
                    newParentNodeId = d.data.id;
                    modifyNode(nodeToModify, root, svg, treemap, rectWidth, rectHeight, rectRoundness, i, depthSize, margin, newParentNodeId);
                    d3.select(this).classed('node-modifying', false);
                    nodeToModify = null;
                    newParentNodeId = null;
                    isModifying = false;
                    d3.selectAll('.node').classed('node-hover-enabled', false);
                }
            } else {
                nodeToModify = d;
            }
        })
        

        .on('contextmenu', function (event, d) {
            console.log('contextmenu event triggered');
            event.preventDefault();
            event.stopPropagation();
            d3.select(this).classed('node-modifying', true);
            var coordinates = d3.pointer(event);
            var contextMenu = d3.select('#context-menu');

            contextMenu.style('display', 'block')
                .style('left', `${event.pageX}px`)
                .style('top', `${event.pageY}px`);

            d3.select('#create-node').on('click', function (event) {
                event.stopPropagation();
                contextMenu.style('display', 'none');
                d3.selectAll('.node-selected').classed('node-selected', false);
                createNode(coordinates, root, svg, treemap, rectWidth, rectHeight, rectRoundness, i, depthSize, margin, d);
            });

            d3.select('#modify-node').on('click', function (event) {
                event.stopPropagation();
                contextMenu.style('display', 'none');
                d3.selectAll('.node-selected').classed('node-selected', false);
                nodeToModify = d;
                isModifying = true;
                d3.selectAll('.node').classed('node-hover-enabled', true);
            });

            d3.select('#delete-node').on('click', function (event) {
                console.log('Delete Node clicked. nodeToModify:', nodeToModify);
                event.stopPropagation();
                contextMenu.style('display', 'none');
                d3.selectAll('.node-selected').classed('node-selected', false);
                nodeToModify = d;

                if (nodeToModify !== null) {
                    deleteNode(nodeToModify.data.id);
                }

            });
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
        .text(function (d) { return 'ID: ' + d.data.id; });

    nodeText.append('tspan')
        .attr('x', -rectWidth / 2 + 10)
        .attr('dy', '1.5em')
        .text(function (d) {
            return 'Dialog: ' + trimText(nodeEnter, d.data.dialogText || '', rectWidth - 20);
        });

    nodeText.append('tspan')
        .attr('x', -rectWidth / 2 + 10)
        .attr('dy', '1.5em')
        .text(function (d) {
            return 'Message: ' + trimText(nodeEnter, d.data.msgText || '', rectWidth - 20);
        });

    nodeText.append('tspan')
        .attr('x', -rectWidth / 2 + 10)
        .attr('dy', '1.5em')
        .text(function (d) {
            return d.parent && d.parent.data.id !== "root" ? 'Parent ID: ' + d.parent.data.id : '';
        });

    var nodeUpdate = nodeEnter.merge(node);

    nodeUpdate.transition()
        .duration(750)
        .attr("transform", function (d) {
            return "translate(" + d.x + "," + d.y + ")";
        });

    var nodeExit = node.exit().transition()
        .duration(750)
        .attr("transform", function (d) {
            return "translate(" + d.x + "," + d.y + ")";
        })
        .remove();

    nodeExit.select('text')
        .style('fill-opacity', 1e-6);

    var link = svg.selectAll('path.link')
        .data(links, function (d) {
            return d.id;
        });

    var linkEnter = link.enter().insert('path', "g")
        .attr("class", "link")
        .style("stroke", "white")
        .attr('d', function (d) {
            var o = { x: d.x, y: d.y }
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

    nodes.forEach(function (d) {
        d.x0 = d.x;
        d.y0 = d.y;
    });
}