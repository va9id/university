window.onload = function () {

    /* Highlight Button functionailites and action listener */
    var hideHighlightButton = document.getElementById("hide");
    hideHighlightButton.style.visibility = 'hidden';
    var highlightButton = document.getElementById("highlight");
    highlightButton.addEventListener("click", function () {
        walkDOM(document);
        function walkDOM(node) {
            var children = node.childNodes;
            for (var i = 0; i < children.length; i++) {
                walkDOM(children[i]);
                if (children[i].nodeType == 1) {
                    if (children.item(i).nodeName.toUpperCase() !== "HTML" && children.item(i).nodeName.toUpperCase() !== "HEAD" && children.item(i).nodeName.toUpperCase() !== "BODY") {
                        var newElement = document.createElement("span");
                        newElement.className = "hoverNode";
                        newElement.innerText = children[i].tagName;
                        children[i].appendChild(newElement);
                    }
                }
            }
        }
        addAlert();
        highlightButton.style.visibility = 'hidden';
        hideHighlightButton.style.visibility = 'initial';
    });

    /* Adds alert to each hoverNode class element */
    function addAlert() {
        var newChilds = document.getElementsByClassName("hoverNode");
        for (var i = 0; i < newChilds.length; i++) {
            newChilds[i].addEventListener('click', function (e) {
                var n = e.target.parentNode;
                alert("TAG: " + n.tagName + "\nClass: " + n.className
                    + "\nID:" + n.id + "\ninnerHTML:" + n.innerHTML);
            });
        }
    }

    /* Hide Highlight functionalities and action Listener 
    to Remove hoverNode class elements */
    hideHighlightButton.addEventListener("click", function () {
        control = false;
        highlightButton.style.visibility = 'initial';
        hideHighlightButton.style.visibility = 'hidden';
        var removeChilds = document.getElementsByClassName("hoverNode");
        while (removeChilds.length > 0) {
            removeChilds[0].remove();
        }
    });
}
