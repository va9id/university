# Camera Model: Projection Matrices
This repository contains the code for assignment 3 of computer vision COMP 4102.
## Requirements
- [NumPy](https://numpy.org/install/)
- [OpenCV](https://pypi.org/project/opencv-python/)
## Problem Statement
Perform calibration by first computing a projection matrix, and then decomposing that matrix to find the extrinsic and instrinsic camera parameters. 

The program takes 10 given 3D points and projects them into a 2D image using the given supplied camera calibration matrix, rotation matrix and translation vector.

Write functions `computeProjectionMatrix` and `decomposeProjectionMatrix` which compute the projection matrix and decompose the projection matrix into a camera calibration matrix, rotation matrix and translation vector. 
## Results
It should be the case that the computed camera matrix, rotation matrix and translation vector are the same (or very similar) to the original versions that were used to create the projected points.

The resulting output is stored in [`assign3-out`](/assignment3/assign3-out.txt).

## Usage
1. Run the projection_template python file.
    ```python
    python3 projection_template.py
    ```
2. View the output in the resulting text file [`assign3-out`](/assignment3/assign3-out.txt).
    ```bash
    cat assign3-out.txt
    ```