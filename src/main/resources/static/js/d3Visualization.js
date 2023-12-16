function visualizeTree(treeData) {
    
    var i = 0; 
    
    var margin = {top: 20, right: 90, bottom: 30, left: 90},
        width = 960 - margin.left - margin.right,
        height = 500 - margin.top - margin.bottom;

    var svg = d3.select("#tree-container").append("svg")
        .attr("width", width + margin.right + margin.left)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform", "translate("
              + margin.left + "," + margin.top + ")");

    var treemap = d3.tree()
        .size([height, width]);
    
    var root = d3.hierarchy(treeData, function(d) { 
        return d.children; 
    });

    root.x0 = height / 2;
    root.y0 = 0;

    collapse(root); 

    update(root);

    function collapse(d) {
        if(d.children) {
            d._children = d.children;
            d._children.forEach(collapse);
            d.children = null;
        }
    }

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
                
                return "translate(" + source.y0 + "," + source.x0 + ")";
            })
            .on('click', click);

        nodeEnter.append('circle')
            .attr('class', 'node')
            .attr('r', 10) 
            .style("fill", function(d) {
                return (!d.children && !d._children) ? "lightgreen" : (d._children ? "lightsteelblue" : "#fff");
            });

        nodeEnter.append('text')
            .attr("dy", "-1.5em") 
            .attr("text-anchor", "middle")
            .style("fill", "white")
            .style("font-size", "10px")
            .text(function(d) {
                return d.data.id; 
            });

        nodeEnter.append('title')
            .text(function(d) {
                var tooltipText = d.data.dialogText; 
                if (d.data.msgText) {
                    tooltipText += "\nMessage: " + d.data.msgText; 
                }
                if (d.parent && d.parent.data.id !== "root") { 
                    tooltipText += "\nParent ID: " + d.parent.data.id; 
                }
                return tooltipText; 
            });

        var nodeUpdate = nodeEnter.merge(node);

        nodeUpdate.transition()
            .duration(750)
            .attr("transform", function(d) {
                return "translate(" + d.y + "," + d.x + ")";
            });

        nodeUpdate.select('circle.node')
            .attr('r', 10)
            .style('fill', function(d) {
                return d.isLeaf ? 'lightgreen' : (d._children ? 'lightsteelblue' : '#fff');
            });

        var nodeExit = node.exit().transition()
            .duration(750)
            .attr("transform", function(d) {
                return "translate(" + source.y + "," + source.x + ")";
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
        path = `M ${s.y} ${s.x}
                C ${(s.y + d.y) / 2} ${s.x},
                ${(s.y + d.y) / 2} ${d.x},
                ${d.y} ${d.x}`
        


        return path;
        }

        nodes.forEach(function(d){
            d.x0 = d.x;
            d.y0 = d.y;
        });
    }
}