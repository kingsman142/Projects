# -*- coding: utf-8 -*-
"""
Created on Sun May 22 02:17:57 2016

@author: kingsman142
"""

import pandas as pd
import numpy as np
import sklearn as skl
import skimage as ski
import cv2
import time

from sklearn.decomposition import PCA
from sklearn.cluster import KMeans
from sklearn.naive_bayes import GaussianNB
from sklearn.linear_model import LinearRegression
from pandas import DataFrame
from pandas import read_csv
from sklearn.svm import SVC

imagesTrain = []
imagesTest = []

drivers = DataFrame(read_csv("driver_imgs_list.csv/driver_imgs_list.csv"))
driversHead = drivers.head(3000)

imageFilenames = []
imageClasses = []

for i in driversHead['img']:
    imageFilenames.append(i)
for j in driversHead['classname']:
    imageClasses.append(j)

timerStart = time.time()
for i in range(0, len(imageFilenames)):
    filePath = "imgs/train/" + imageClasses[i] + "/" + imageFilenames[i]
    img = cv2.imread(filePath, 0)
    img = np.array(img)
    imagesTrain.append(img)
timerEnd = time.time()
timer = timerEnd - timerStart
print "Total time to read in training images: {}".format(timer)
    
imagesArrTrain = np.array(imagesTrain).reshape(-1, 307200).astype(np.float32)
classArr = []
classDict = {'c0': 0, 'c1': 1, 'c2': 2, 'c3': 3, 'c4': 4, 'c5': 5, 'c6': 6, 'c7': 7, 'c8': 8, 'c9': 9}

for i in imageClasses:
    classArr.append(classDict[i])
    
timerStart = time.time()
testSize = 200
for i in range(0, testSize):
    filePath = "imgs/test/img_" + str(i) + ".jpg"
    img = cv2.imread(filePath, 0)
    if img != None:
        img = np.array(img)
        imagesTest.append(img)
timerEnd = time.time()
timer = timerEnd - timerStart
print "Total time to read in testing images: {}".format(timer)

timerStart = time.time()
imagesArrTest = np.array(imagesTest).reshape(-1, 307200).astype(np.float32)
timerEnd = time.time()
timer = timerEnd - timerStart
print "Total time to reshape test images array: {}".format(timer)
    
classArr = np.array(classArr)

deg = 3
gam = .001
SVMparams = {'kernel_type': cv2.SVM_POLY, 'degree': deg, 'gamma': gam}

#
#Create the SVM
#
timerStart = time.time()
#gsb = GaussianNB()
#SVM = cv2.SVM(trainData = imagesArrTrain, responses = classArr, params = SVMparams)
skSVM = SVC(kernel = 'poly', degree = deg, gamma = gam)
timerEnd = time.time()
timer = timerEnd - timerStart
print "Total time to create SVM: {}".format(timer)

#Train the OpenCV SVM
timerStart = time.time()
#SVM.train(trainData = imagesArrTrain[i*700:(i+1)*700-1], responses = classArr[i*700:(i+1)*700-1], params = SVMparams)
timerEnd = time.time()
timer = timerEnd - timerStart
print "Total OpenCV training time: {}".format(timer)

#Train the SKLearn SVM
timerStart = time.time()
skSVM.fit(imagesArrTrain, classArr)
timerEnd = time.time()
timer = timerEnd - timerStart
print "Total SKLearn training time: {}".format(timer)

#
#Test the OpenCV SVM
#
timerStart = time.time()
predictions = np.array([])
#predictions = SVM.predict_all(samples = imagesArrTest, results = predictions)
timerEnd = time.time()
timer = timerEnd - timerStart
print "Total OpenCV testing time: {}".format(timer)

#
#Test the SKLearn SVM
#
timerStart = time.time()
skPredictions = skSVM.predict(imagesArrTest)
timerEnd = time.time()
timer = timerEnd - timerStart
print "Total SKLearn testing time: {}".format(timer)

#
#Save the data to text files
#
'''timerStart = time.time()
np.savetxt("testImages.txt", imagesArrTest, newline="\n")
np.savetxt("testPredictions.txt", predictions, newline="\n")
timerEnd = time.time()
timer = timerEnd - timerStart
print "Total time to save data: {}".format(timer)'''
#for i in range(0, 100):
    #print "OpenCV: {}".format(predictions[i])
for i in range(0, 100):
    print "SKLearn: {}".format(skPredictions[i])
    
#
#Check accuracy
#
'''timerStart = time.time()
mask = predictions==classArr
correct = np.count_nonzero(mask)
timerEnd = time.time()
timer = timerEnd - timerStart
print "Total time to calculate score: {}".format(timer)
print "Score of the SVM: {}".format(correct*100.0/predictions.size)'''
correctTraining = [5, 2, 4, 6, 3, 3, 5, 5, 1, 1, 2, 4, 5, 6, 5, 6, 0, 0, 0, 2, 3, 1, 6, 7, 0, 4, 2, 1, 9, 1, 4, 1, 9, 0, 6, 8, 9, 5, 5, 6, 0, 2, 0, 0, 1, 9, 6, 3, 7, 5, 9, 7, 7, 4, 6, 6, 3, 1, 1, 0, 3, 3, 5, 6, 5, 8, 0, 7, 2, 2, 6, 3, 0, 4, 6, 2, 4, 1, 6, 5, 0, 0, 0, 5, 0, 0, 2, 6, 0, 0, 7, 7, 2, 4, 5, 3, 6, 3, 0, 3]
openCorrect = 0
skCorrect = 0
for i in range(0, 100):
    #if correctTraining[i] == predictions[i]:
        #openCorrect += 1
    if correctTraining[i] == skPredictions[i]:
        skCorrect += 1
print "SKCorrect: {}".format(skCorrect)
print "OpenCorrect: {}".format(openCorrect)