# -*- coding: utf-8 -*-
"""
Created on Tue May 24 00:43:40 2016

@author: kingsman142
"""

import pandas as pd
import matplotlib.pyplot as plt
import matplotlib
matplotlib.style.use('ggplot')
from sklearn.svm import SVC
from sklearn.cluster import KMeans
from sklearn.grid_search import GridSearchCV

train = pd.read_csv("train.csv/train.csv")

#plt.figure()
#train.plot(train.x, train.y)
#plt.scatter(train.x, train.y)
#plt.show()

#grid = GridSearchCV(train)

clf = SVC(random_state = 0)
clf.fit(train.accuracy, train.place_id)
print clf.score()

clf2 = KMeans(n_clusters = 10, random_state = 0)
clf2.fit(train.accuracy, train.place_id)
print clf2.score()