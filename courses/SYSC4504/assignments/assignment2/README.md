# Assignment 2: JavaScript and DOM

## Problem Statement 

Write JavaScript code that recursively iterates the DOM and visually identifies the tag names of all elements on the page. 

1. Add event handlers to the two buttons at the bottom of `assignment2.html`. Event handlers must execute only after the page has loaded.

2. The handler for the `Highlight Nodes` button should navigate every element in the DOM, and for each element within the body, determine whether it is an element node (`nodeType == 1`) element.

3. If it is an element node, add a new child node to it. This child node should be a `<span>` element with `class=hoverNode` as attribute. Its `innerText` should be equal to its parent’s tag name.

4. Add an event listener for this child node so that when the user clicks on the new span, an alert popup displays the following information about its parent node: `id`, `tag name`, `class name`, and `innerHTML`.

5. The `Highlight Nodes` button should hide when the user clicks on it. The `Hide Highlight` button should then be displayed. When the page is first displayed, the `Hide Highlight` button should be hidden.

6. When the user clicks the `Hide Highlight` button, all the `<span>` elements with `class=hoverNode` should be removed. The `Hide Highlight` button should then be hidden and the `Highlight Node` button should be displayed.


## Usage 

- Open `assignment2.html` in a web browser