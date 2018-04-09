import cv2
import numpy as np
import json
from matplotlib import pyplot as plt

def kMeansColor(img, numColors):

	Z = img.reshape((-1,3))

	# convert to np.float32
	Z = np.float32(Z)
	# define criteria, number of clusters(K) and apply kmeans()
	criteria = (cv2.TERM_CRITERIA_EPS + cv2.TERM_CRITERIA_MAX_ITER, 10, 1.0)
	K = numColors
	ret,label,center=cv2.kmeans(Z,K,None,criteria,10,cv2.KMEANS_RANDOM_CENTERS)
	# Now convert back into uint8, and make original image
	center = np.uint8(center)
	res = center[label.flatten()]
	res2 = res.reshape((img.shape))

	return center,res2

def getLeftMask(contour):
    mincord = contour[0]
    for coord in contour:
        if coord[0][0] < mincord[0][0]:
            mincord = coord
    return mincord

def convertResults(results):
    days = ["SunM","SunN","MonM","MonN","TueM","TueN","WedM","WedN","ThuM","ThuN","FriM","FriN","SatM","SatN"]
    output = {}
    output["error"] = False
    for i in xrange(14):
        output[days[i]] = results[i]
    return json.dumps(output)

def answer(img):
	img =cv2.resize(img, (0,0), fx=0.1, fy=0.1)

	######

	colors, segmented = kMeansColor(img,4)

	#####

	blue = np.array([95, 42, 10])
	red = np.array([17,8,98])

	cos_sim = lambda a,b: np.dot(a, b)/(np.linalg.norm(a)*np.linalg.norm(b))
	bluedist = float('inf')
	blueseg = None

	colors = [np.array(color) for color in colors]

	for color in colors:
	    currdistance = np.linalg.norm(color-blue)
	    if currdistance < bluedist:
	        bluedist = currdistance
	        blueseg = color

	reddist = float('inf')
	redseg = None

	for color in colors:
	    currdistance = np.linalg.norm(color-red)
	    if currdistance < reddist:
	        reddist = currdistance
	        redseg = color
	        
	white1set = False

	for color in colors:

	    if not np.array_equal(color,redseg) and not np.array_equal(color,blueseg) and not white1set:
	        white1 = color
	        white1set = True
	    
	    elif not np.array_equal(color,redseg) and not np.array_equal(color,blueseg):
	        white2 = color

	######
	walls = cv2.inRange(segmented, blueseg,blueseg)
	##

	kernel = np.ones((7,7),np.uint8)
	edges = cv2.dilate(walls,kernel,iterations = 1)
	#######
	kernel = np.ones((7,7),np.uint8)
	edgesthin = cv2.erode(edges,kernel,iterations = 1)
	###########
	imgcontours = edgesthin.copy()
	_, contours,h = cv2.findContours(imgcontours,cv2.RETR_TREE,cv2.CHAIN_APPROX_NONE)

	parentfreqs= {}
	for contour in h[0]:
	    parent = contour[-1]
	    parentfreqs[parent] = parentfreqs[parent] + 1 if parent in parentfreqs else 1
	    
	par = -1
	for key in parentfreqs:
	    if parentfreqs[key] == 14:
	        par = key
	        break


	if par == -1:
	    output = {}
	    output["error"] = True
	    return json.dumps(output)

	contours = [contours[i] for i in xrange(len(contours)) if h[0][i][-1]==par]
	#######
	contours.sort(key = lambda contour: getLeftMask(contour)[0][0])
	for x in xrange(7):
	    if getLeftMask(contours[2*x])[0][1] > getLeftMask(contours[2*x+1])[0][1]:
	        contours[2*x],contours[2*x+1]= contours[2*x+1],contours[2*x]

	##########
	imgcontours = img.copy()
	masks = [None]*14
	for x in xrange(14):
	    masks[x] = np.zeros(edgesthin.shape, np.uint8)
	for i in xrange(14):
	    cv2.drawContours(masks[i],contours,i,(255),-1)

	########
	results = []

	for x in xrange(14):
	    
	    cell = cv2.bitwise_and(segmented,segmented,mask=masks[x])
	    whitecell1 = cv2.inRange(cell,white1,white1)
	    cell = cv2.inRange(cell,white2,white2)
	    cell = cv2.bitwise_or(cell,whitecell1,mask=masks[x])
	    whitesize = cv2.countNonZero(cell)
	    cellsize = cv2.countNonZero(masks[x])
	    results.append(whitesize*1.0/cellsize>0.04)

	return convertResults(results)

file_path = 'original3.png'
img = cv2.imread(file_path)
print answer(img)