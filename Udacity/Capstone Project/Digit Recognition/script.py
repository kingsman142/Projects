# -*- coding: utf-8 -*-
"""
Created on Thu Jul 07 19:58:37 2016

@author: kingsman142
"""

import pandas as pd
import sklearn as skl
import skimage as ski
from sklearn.decomposition import PCA
from sklearn.cluster import KMeans
from sklearn.naive_bayes import GaussianNB
from sklearn.linear_model import LinearRegression

import numpy as np
import time
from pandas import DataFrame
from pandas import read_csv
from sklearn.svm import SVC

imagesTrainX = []
imagesTrainY = []
imagesTest = []

timerStart = time.time()
trainCSV = DataFrame(read_csv("train.csv"))
testCSV = DataFrame(read_csv("test.csv"))
#trainCSV = trainCSV.head(42000)
timerEnd = time.time()
timer = timerEnd - timerStart
print "Total time to read in data: {}".format(timer)

timerStart = time.time()
for index, row in trainCSV.iterrows():
    imagesTrainY.append(row["label"])
    
    imgArr = []
    for i in range(0, 783):
        currentPixel = "pixel" + str(i)
        imgArr.append(row[currentPixel])
    imagesTrainX.append(np.array(imgArr))
    
    if index%5000 == 0:
        print "\t*Finished {} images*".format(index)
timerEnd = time.time()
timer = timerEnd - timerStart
print "Total time to organize training data and labels: {}".format(timer)

timerStart = time.time()
for index, row in testCSV.iterrows():
    imgArr = []
    for i in range(0, 783):
        currentPixel = "pixel" + str(i)
        imgArr.append(row[currentPixel])
    imagesTest.append(np.array(imgArr))
timerEnd = time.time()
timer = timerEnd - timerStart
print "Total time to organize testing data and labels: {}".format(timer)

timerStart = time.time()
imagesTrainX = np.array(imagesTrainX).astype(np.float32)
imagesTrainY = np.array(imagesTrainY)
timerEnd = time.time()
timer = timerEnd - timerStart
print "Total time to reshape training data: {}".format(timer)

timerStart = time.time()
imagesTest = np.array(imagesTest).astype(np.float32)
timerEnd = time.time()
timer = timerEnd - timerStart
print "Total time to reshape testing data: {}".format(timer)

timerStart = time.time()
deg = 1
gam = .5
skSVM = SVC(kernel = 'poly', degree = deg, gamma = gam)
timerEnd = time.time()
timer = timerEnd - timerStart
print "Total time to create SVM: {}".format(timer)

timerStart = time.time()
skSVM.fit(imagesTrainX, imagesTrainY)
timerEnd = time.time()
timer = timerEnd - timerStart
print "Total time to train SVM: {}".format(timer)

timerStart = time.time()
skPredictions = skSVM.predict(imagesTest)
skPredictions = np.array(skPredictions)
timerEnd = time.time()
timer = timerEnd - timerStart
print "Total time to test SVM: {}".format(timer)

#for i in range(0, 100):
#    print "Image {}: {}".format(i, skPredictions[i])
    
timerStart = time.time()
saveData = {"ImageId": np.arange(1, len(skPredictions)+1), "Label": skPredictions}
saveDataFrame = DataFrame(data = saveData)
timerEnd = time.time()
timer = timerEnd - timerStart
print "Total time to create data {}".format(timer)

timerStart = time.time()
saveDataFrame.to_csv("official_submission.csv", index = False)
timerEnd = time.time()
timer = timerEnd - timerStart
print "Total time to save data {}".format(timer)
