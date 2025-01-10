#!/usr/bin/env python
import cv2
import math
import numpy
import sys
from numpy import matrix


R = numpy.array(
    [
        [0.902701, 0.051530, 0.427171],
        [0.182987, 0.852568, -0.489535],
        [-0.389418, 0.520070, 0.760184],
    ],
    numpy.float32,
)

rvec = cv2.Rodrigues(R)[0]
print("Initial Rotation")
print(R)

cameraMatrix = numpy.array(
    [
        [-1000.000000, 0.000000, 0.000000],
        [0.000000, -2000.000000, 0.000000],
        [0.000000, 0.000000, 1.000000],
    ],
    numpy.float32,
)

print("Initial Camera Matrix")
print(cameraMatrix)

tvec = numpy.array([10, 15, 20], numpy.float32)

print("Initial Translation")
print(tvec)


objectPoints = numpy.array(
    [
        [0.1251, 56.3585, 19.3304],
        [80.8741, 58.5009, 47.9873],
        [35.0291, 89.5962, 82.2840],
        [74.6605, 17.4108, 85.8943],
        [71.0501, 51.3535, 30.3995],
        [1.4985, 9.1403, 36.4452],
        [14.7313, 16.5899, 98.8525],
        [44.5692, 11.9083, 0.4669],
        [0.8911, 37.7880, 53.1663],
        [57.1184, 60.1764, 60.7166],
    ],
    numpy.float32,
)

print("Initial ObjectPoints")
print(objectPoints)

imagepoints, jac = cv2.projectPoints(objectPoints, rvec, tvec, cameraMatrix, None)
print("Image Points")
print(imagepoints)

# Question 1


def computeProjectionMatrix(image, object):
    r = 0
    A = numpy.zeros((len(object) * 2, 12))
    for i, o in zip(image, object):
        A[r] = [
            o[0],
            o[1],
            o[2],
            1,
            0,
            0,
            0,
            0,
            -i[0][0] * o[0],
            -i[0][0] * o[1],
            -i[0][0] * o[2],
            -i[0][0],
        ]
        A[r + 1] = [
            0,
            0,
            0,
            0,
            o[0],
            o[1],
            o[2],
            1,
            -i[0][1] * o[0],
            -i[0][1] * o[1],
            -i[0][1] * o[2],
            -i[0][1],
        ]

        r += 2

    u, s, v = numpy.linalg.svd(A)

    min_index = 0
    min_val = s[0]

    for i in range(len(s)):
        if s[i] < min_val:
            min_val = s[i]
            min_index = i

    m = v[min_index]
    return m.reshape((3, 4))


def decomposeProjectionMatrix(m):
    r = numpy.zeros((3, 3))
    k = r
    t = numpy.zeros((1, 3))

    y = math.sqrt(pow(m[2][0], 2) + pow(m[2][1], 2) + pow(m[2][2], 2))
    normalized = m / y
    Tz = normalized[2][3]
    if Tz < 0:
        normalized *= -1
    Tz = normalized[2][3]
    for i in range(0, 3):
        r[2][i] = normalized[2][i]

    q1 = numpy.transpose([normalized[0][0], normalized[0][1], normalized[0][2]])
    q2 = numpy.transpose([normalized[1][0], normalized[1][1], normalized[1][2]])
    q3 = numpy.transpose([normalized[2][0], normalized[2][1], normalized[2][2]])
    q4 = numpy.transpose([normalized[0][3], normalized[1][3], normalized[2][3]])

    # Parameters
    ox = numpy.dot(numpy.transpose(q1), q3)
    oy = numpy.dot(numpy.transpose(q2), q3)

    fx = math.sqrt(numpy.dot(numpy.transpose(q1), q1) - ox**2)
    fy = math.sqrt(numpy.dot(numpy.transpose(q2), q2) - oy**2)

    for i in range(0, 3):
        r[0][i] = (ox * normalized[2][i] - normalized[0][i]) / fx
        r[1][i] = (oy * normalized[2][i] - normalized[1][i]) / fy

    Tx = (ox * Tz - normalized[0][3]) / fx
    Ty = (oy * Tz - normalized[1][3]) / fy

    k = [[-fx, 0, ox], [0, -fy, oy], [0, 0, 1]]

    t[0][0] = Tx
    t[0][1] = Ty
    t[0][2] = Tz

    return k, r, t


M = computeProjectionMatrix(imagepoints, objectPoints)
K, rot, T = decomposeProjectionMatrix(M)

with open("assign3-out.txt", "w") as file:
    file.write(f"Camera Matrix:\n{str(cameraMatrix)}\n")
    file.write(f"\nRotation Matrix:\n{str(R)}\n")
    file.write(f"\nTranslation Matrix:\n{str(tvec)}\n")
    file.write(f"\nObject points:\n{str(objectPoints)}\n")
    file.write(f"\nImage points:\n{str(imagepoints)}\n")
    file.write(f"\nComputed Camera Matrix:\n{str(K)}\n")
    file.write(f"\nComputed Rotation Matrix:\n{str(rot)}\n")
    file.write(f"\nComputed Translation Matrix:\n{str(T)}\n")
