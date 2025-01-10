# Assignment 1 - Java Threads

## Problem Statement

Consider a system with three chef threads and one agent thread. Each chef continuously makes a sandwich and then eats it. But to make and eat a sandwich, the chef needs three ingredients: bread, peanut butter, and jam. One of the chef threads has an infinite supply of bread, another has peanut butter, and the third has jam. The agent has an infinite supply of all three ingredients. The agent randomly selects two of the ingredients and places them on a table. The chef who has the remaining ingredient then makes and eats a sandwich, signalling the agent on completion. The agent then puts out another two of the three ingredients, and the cycle repeats.

## Usage 

- Open the project in IntelliJ and run `Main.java`. 