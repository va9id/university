# Neural Networks
This repository contains the code for assignment 2 of computer vision COMP 4102.
## Requirements
- [PyTorch](https://pypi.org/project/torch/)
- [torchvision](https://pypi.org/project/torchvision/)
- [NumPy](https://numpy.org/install/)
- [OpenCV](https://pypi.org/project/opencv-python/)
- [matplotlib](https://matplotlib.org/stable/users/installing/index.html)
## Question 1: CNN vs MLP
Run the CNN for 10 epochs (# of passes through the training set) and record the accuracy of the test set per epoch. Then, drop the conv layers and modify the code to get a simple neural network. Change the number of hidden layers from 0 to 4 and record the models accuracy per epoch. Compare the accuracy of the CNN with the simple neural networks with 0, 1, 2, 3 and 4 hidden layers. Each hidden layer has 120 nodes with Relu as non-linearity.
## Question 2: Learning Rate
Run the CNN model with different learning rates values (0.0001, 0.001, 0.01, 0.1).
## Question 3: Batch Size
Run the CNN model with different batch sizes (1, 4, 16, 64, 256).
## Question 4: Activation Functions
Create a new model by replacing the Relu units with Sigmoid units in the CNN.
## Question 5: Filter Size and Convulution Type
The provided CNN network uses 5x5 filters with valid convolution. Design three new models by changing the filter size to 3x3 and/or the convolution type to same convolution. Run the new models for 10 epochs.