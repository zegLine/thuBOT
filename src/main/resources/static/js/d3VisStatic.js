function visualizeTree(treeData) {
    
    const rectWidth = 200;
    const rectHeight = 80;
    const rectRoundness = 5;

    var i = 0;

    var margin = {top: 50, right: 90, bottom: 30, left: 90};
    var containerWidth = d3.select("#tree-cont-static").node().getBoundingClientRect().width;
    var containerHeight = window.innerHeight;
    var width = containerWidth - margin.left - margin.right;
    var height = containerHeight - margin.top - margin.bottom;

    var svg = d3.select("#tree-cont-static").append("svg")
        .attr("width", '100%')
        .attr("height", height + margin.top + margin.bottom)
        .attr("viewBox", `0 0 ${containerWidth} ${height}`)
        .append("g")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    var treemap = d3.tree().size([width - margin.right, height - margin.bottom]);
    
    var root = d3.hierarchy(treeData, function(d) { 
        return d.children; 
    });

    root.x0 = width / 2;
    root.y0 = height / 2;

    collapse(root); 

    update(root);

    function collapse(d) {
        if(d.children) {
            d._children = d.children;
            d._children.forEach(collapse);
            d.children = null;
        }
    }

    function onResize() {
        var newContainerWidth = d3.select("#tree-cont-static").node().getBoundingClientRect().width;
        var newContainerHeight = window.innerHeight;
        var newWidth = newContainerWidth - margin.left - margin.right;
        var newHeight = newContainerHeight - margin.top - margin.bottom;
    
        svg.attr("viewBox", `0 0 ${newContainerWidth} ${newContainerHeight}`);
        treemap.size([newWidth, newHeight]);
        update(root);
    }

    window.addEventListener('resize', onResize);

    function update(source) {
    
        var treeData = treemap(root);

        var nodes = treeData.descendants();
            
        var links = treeData.descendants().slice(1);

        nodes.forEach(function(d) {
            d.isLeaf = !d.children && !d._children;
        });

        var node = svg.selectAll('g.node')
            .data(nodes, function(d) { 
                return d.id || (d.id = ++i);
             });

        var nodeEnter = node.enter().append('g')
            .attr('class', 'node')
            .attr("transform", function(d) {
                
                return "translate(" + source.x0 + "," + source.y0 + ")";
            })
            .on('click', click);

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
            .attr("y", -rectHeight / 2 + 5) // Start the text closer to the top of the rectangle
            .attr("text-anchor", "start")
            .style("fill", "black") // Changed text color to black for visibility
            .style("font-size", "10px");
        
        nodeText.append('tspan')
            .attr('x', -rectWidth / 2 + 10)
            .attr('dy', '1em')
            .text(function(d) { return 'ID: ' + d.data.id; });
        
        nodeText.append('tspan')
            .attr('x', -rectWidth / 2 + 10)
            .attr('dy', '1.5em') 
            .text(function(d) { 
                return 'Dialog: ' + trimText(d.data.dialogText || '', rectWidth - 20); 
            });
        
        nodeText.append('tspan')
            .attr('x', -rectWidth / 2 + 10)
            .attr('dy', '1.5em')
            .text(function(d) { 
                return 'Message: ' + trimText(d.data.msgText || '', rectWidth - 20); 
            });

        nodeText.append('tspan')
            .attr('x', -rectWidth / 2 + 10)
            .attr('dy', '1.5em') 
            .text(function(d) {
                return d.parent && d.parent.data.id !== "root" ? 'Parent ID: ' + d.parent.data.id : '';
            });

        function trimText(text, maxWidth) {
            var svgText = svg.append("text")
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
        
        var nodeUpdate = nodeEnter.merge(node);

        nodeUpdate.transition()
            .duration(750)
            .attr("transform", function(d) {
                return "translate(" + d.x + "," + d.y + ")";
            });

        nodeUpdate.select('circle.node')
            .attr('r', 10)
            .style('fill', function(d) {
                return d.isLeaf ? 'lightgreen' : (d._children ? 'lightsteelblue' : '#fff');
            });

        var nodeExit = node.exit().transition()
            .duration(750)
            .attr("transform", function(d) {
                return "translate(" + source.x + "," + source.y + ")";
            })
            .remove();

        nodeExit.select('circle')
            .attr('r', 1e-6);

        nodeExit.select('text')
            .style('fill-opacity', 1e-6);

        function click(event, d) {
            if (d.children) {
                d._children = d.children;
                d.children = null;
            } else {
                d.children = d._children;
                d._children = null;
            }
            update(d);
        }

        var link = svg.selectAll('path.link')
            .data(links, function(d) { 
                return d.id; 
            });

        var linkEnter = link.enter().insert('path', "g")
            .attr("class", "link")
            .style("stroke", "white")
            .attr('d', function(d){
                var o = {x: source.x0, y: source.y0}
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

        var linkExit = link.exit().transition()
        .duration(750)
        .attr('d', function(d) {
            var o = {x: source.x, y: source.y}
            return diagonal(o, o)
        })
        .remove();

        function diagonal(s, d) {
        var path = `M ${s.x} ${s.y} L ${d.x} ${d.y}`; 
        return path;
        }


        nodes.forEach(function(d){
            d.x0 = d.x;
            d.y0 = d.y;
        });
    }
}