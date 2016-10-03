import Queue
import itertools
from heapq import heappush,heappop

class PriorityQueue(object):
    '''
    Modified on top of ... 
    Reference :  https://docs.python.org/2/library/heapq.html
    '''
    def __init__(self):
        self.pq = []                         # list of entries arranged in a heap
        self.entry_finder = {}               # mapping of tasks to entries
        self.REMOVED = '<removed-task>'      # placeholder for a removed task
        self.counter = itertools.count()     # unique sequence count

    def add_task(self,task,priority=0,taskData=None):
        '''
        Add a new task or update the priority of an existing task.
        Based on given priority return is element updated or not.
        '''
        if taskData==None:
            taskData=task
        else:
            taskData=[task,taskData]
            
        flagPreviousEntryRemoved=True
        if task in self.entry_finder:
            flagPreviousEntryRemoved=False
            if self.entry_finder[task][0] > priority:
                self.remove_task(task)
                flagPreviousEntryRemoved=True
                
        if flagPreviousEntryRemoved:
            count = next(self.counter)
            entry = [priority, count, taskData]
            self.entry_finder[task] = entry
            heappush(self.pq, entry)
        
        return flagPreviousEntryRemoved

    def remove_task(self,task):
        'Mark an existing task as REMOVED.  Raise KeyError if not found.'
        entry = self.entry_finder.pop(task)
        entry[-1] = self.REMOVED

    def pop_task(self):
        'Remove and return the lowest priority task. Raise KeyError if empty.'
        while self.pq:
            priority, count, task = heappop(self.pq)
            if task is not self.REMOVED:
                if isinstance(task,list):
                    del self.entry_finder[task[0]]
                else:
                    del self.entry_finder[task]
                return (task,priority)
        raise KeyError('pop from an empty priority queue')
    
    def length(self):
        'length of non deleted elements'
        return len(self.entry_finder)
    
    def isElementPresent(self,elementName):
        'is element present in priority queue'
        return elementName in self.entry_finder
    
class Graph(object):
    def __init__(self,graphDict=None,heuristic=None):
        if graphDict==None:
            graphDict={}
        if heuristic==None:
            heuristic={}
        self.graphDict=graphDict
        self.heuristic={}
        
    def addEdge(self,nameOfFirstNode, nameOfSecondNode, distance):
        #order maintained by creating list
        if nameOfFirstNode not in self.graphDict:
            self.graphDict[nameOfFirstNode]=[]
        if nameOfSecondNode not in self.graphDict:
            self.graphDict[nameOfSecondNode]=[]
        self.graphDict[nameOfFirstNode].append([nameOfSecondNode,distance])
    
    def getAllNodes(self):    
        return self.graphDict.keys()
    
    def giveNodeInfo(self,nameOfNode):
        if nameOfNode not in self.graphDict:
            return None
        else:
            return self.graphDict[nameOfNode]
    
    def addHeuristics(self,heuristicMap):
        self.heuristic=heuristicMap
        
    def printGraph(self):
        print self.graphDict

    def bfs(self,startNode,goalNode):
        #same start and goal node
        if startNode==goalNode:
            return [startNode,]
        
        allNodes = self.getAllNodes()
        t=Tree(startNode)
        queueToTraverse= Queue.Queue()
        isNodeVisited={}
        
        #if start or goal node out of map
        if (startNode not in allNodes) or (goalNode not in allNodes):
            return [startNode,]

        #initialize all nodes
        for eachNode in allNodes:
            isNodeVisited[eachNode]=0
        
        queueToTraverse.put(startNode)
        isNodeVisited[startNode]=1
        
        while not queueToTraverse.empty():
            tempNode = queueToTraverse.get()
            #check if goal
            if tempNode==goalNode:
                break
            
            allFurtherNodes = self.giveNodeInfo(tempNode)
            for eachFurtherNode in allFurtherNodes:
                nodeName=eachFurtherNode[0]
                nodeDistance=eachFurtherNode[1]
                if not isNodeVisited[nodeName]:
                    t.addChildToParentRelation(nodeName, tempNode, nodeDistance)
                    queueToTraverse.put(nodeName)
                    isNodeVisited[nodeName]=1
                     
        
        roadFromParentToChild = t.giveLinkBetweenChildAndParent(goalNode)
        return roadFromParentToChild
    
    def dfs(self,startNode,goalNode):
        #same start and goal node
        if startNode==goalNode:
            return [startNode,]
        
        allNodes = self.getAllNodes()
        stackTraversed = []
        isNodeVisited={}
        t=Tree(startNode)
        
        #if start or goal node out of map
        if (startNode not in allNodes) or (goalNode not in allNodes):
            return [startNode,]

        #initialize all nodes
        for eachNode in allNodes:
            isNodeVisited[eachNode]=0
        
        stackTraversed.append(startNode)
        isNodeVisited[startNode]=1
        
        while len(stackTraversed)!=0:
            fromNode =  stackTraversed.pop()
            #check if goal
            if fromNode==goalNode:
                break
            listOfFurtherNodes = self.giveNodeInfo(fromNode)
            #order of putting should be reversed
            listOfFurtherNodes.reverse()
            
            for eachNodeToBeExplored in listOfFurtherNodes:
                nameOfCurrentNode = eachNodeToBeExplored[0]
                nodeDistance=eachNodeToBeExplored[1]
                #check not repeated 
                if not isNodeVisited[nameOfCurrentNode]:
                    #set its parent
                    t.addChildToParentRelation(nameOfCurrentNode, fromNode, nodeDistance)
                    #put it in stack
                    stackTraversed.append(nameOfCurrentNode)
                    #add to visited
                    isNodeVisited[nameOfCurrentNode]=1

        roadFromParentToChild = t.giveLinkBetweenChildAndParent(goalNode)
        return roadFromParentToChild
    
    def ucs(self,startNode,goalNode):
        #same start and goal node
        if startNode==goalNode:
            return [(startNode,0),]
        
        frontier = PriorityQueue()
        explored={} #explored will contain entry {nodename:1}
        flagGoalFound=False
        mapPlaceAndDistance=[]
        t=Tree(startNode)
        frontier.add_task(startNode, 0)
        while frontier.length()!=0:
            currentElement = frontier.pop_task()
            currentNodeName=currentElement[0]
            currentDistanceTravelled=currentElement[1]
            if currentNodeName==goalNode:
                flagGoalFound=True
                break
            explored[currentNodeName]=1
            nodeInfo = self.giveNodeInfo(currentNodeName)
            for eachNode in nodeInfo:
                nameOfEachNode=eachNode[0]
                if nameOfEachNode not in explored:
                    #add task and distance, 
                    #queue will take care of updating priority accordingly.
                    distanceCovered = currentDistanceTravelled + eachNode[1]
                    flagPreviousEntryRemoved = frontier.add_task(nameOfEachNode,distanceCovered)
                    if flagPreviousEntryRemoved:
                        #update parent (for tracking road)
                        t.addChildToParentRelation(nameOfEachNode, currentNodeName, distanceCovered)
                    
        if flagGoalFound==True:
            #reverse tree traversal
            mapPlaceAndDistance = t.giveLinkBetweenChildAndParent(goalNode, True)
        return mapPlaceAndDistance
    
    def astar(self,startNode,goalNode):
        #same start and goal node
        if startNode==goalNode:
            return [(startNode,0),]
        
        frontier = PriorityQueue()
        explored={} #explored will contain entry {nodename:1}
        distanceFromSource = {startNode:0} #need to check later already explored get less cost
        flagGoalFound=False
        mapPlaceAndDistance=[]
        t=Tree(startNode)
        
        currentDistanceTravelled=0
        heuristicOfNode= currentDistanceTravelled + self.heuristic[startNode]
        frontier.add_task(startNode,heuristicOfNode,currentDistanceTravelled)

        while frontier.length()!=0:
            currentElementInfo = frontier.pop_task()
            #[[elementName,distanceTravelled],heuristic]
            currentElementName = currentElementInfo[0][0]
            currentElementDistanceTravelled = currentElementInfo[0][1]
            currentElementHeuristic = currentElementInfo[1]
            
            if currentElementName==goalNode:
                flagGoalFound=True
                break
            
            explored[currentElementName]=1
            furtherNodeInfo = self.giveNodeInfo(currentElementName)
            for eachNode in furtherNodeInfo:
                nameOfEachNode=eachNode[0]
                if nameOfEachNode not in explored:
                    #add task, distance, and heuristic
                    #queue will take care of updating priority accordingly.
                    distanceCovered = currentElementDistanceTravelled + eachNode[1]
                    heuristicDistanceToGoal = distanceCovered + self.heuristic[nameOfEachNode]
                    flagPreviousEntryRemoved = frontier.add_task(nameOfEachNode,heuristicDistanceToGoal,distanceCovered)
                    if flagPreviousEntryRemoved:
                        #update parent (for tracking road)
                        t.addChildToParentRelation(nameOfEachNode, currentElementName, distanceCovered)
                        distanceFromSource[nameOfEachNode]=distanceCovered
                else:
                    #already explored but later found cost is less
                    distanceCovered = currentElementDistanceTravelled + eachNode[1]
                    if distanceCovered < distanceFromSource[nameOfEachNode]:
                        #make it unexplored
                        explored[nameOfEachNode]=0
                        #Put in priority queue again
                        heuristicDistanceToGoal = distanceCovered + self.heuristic[nameOfEachNode]
                        flagPreviousEntryRemoved = frontier.add_task(nameOfEachNode,heuristicDistanceToGoal,distanceCovered)
                        if flagPreviousEntryRemoved:
                            #update parent (for tracking road)
                            t.addChildToParentRelation(nameOfEachNode, currentElementName, distanceCovered)
                            distanceFromSource[nameOfEachNode]=distanceCovered
                        
        if flagGoalFound==True:
            #reverse tree traversal
            mapPlaceAndDistance = t.giveLinkBetweenChildAndParent(goalNode, True)
        return mapPlaceAndDistance
                
class Tree(object):
    def __init__(self,parent,treeDict=None,):
        if treeDict==None:
            treeDict={}
        self.treeDict=treeDict
        #No super parent
        self.treeDict[parent]=None 
        
    def addChildToParentRelation(self,childName,parentName,distance=1):
        self.treeDict[childName]=(parentName,distance)
        
    def giveLinkBetweenChildAndParent(self,childName,giveDistance=False):
        linksList = []
        if childName not in self.treeDict:
            linksList=[]
            return linksList
        if giveDistance:
            #For UCS, AStar
            while True:
                if self.treeDict[childName]!=None:
                    distanceToParent = self.treeDict[childName][1]
                else:
                    distanceToParent = 0
                linksList.append((childName,distanceToParent))
                if self.treeDict[childName]==None:
                    break
                childName=self.treeDict[childName][0] #parent present from above check 
            linksList.reverse()
            return linksList            
        else:
            #For BFS, DFS
            while True:
                linksList.append(childName)
                if self.treeDict[childName]==None:
                    break
                childName=self.treeDict[childName][0]         
            linksList.reverse()
            return linksList
        
def readInput(inputFile="input.txt"):
    heuristicValue = {}
    roadDistances = []

    dataRead = open(inputFile, "r")
    algorithmName = dataRead.readline().strip()
    startState = dataRead.readline().strip()
    goalState = dataRead.readline().strip()

    #road distances reader
    numberOfLiveTrafficLines = int(dataRead.readline())
    for eachLine in range(0,numberOfLiveTrafficLines):
        roadDistance = dataRead.readline().strip("\n").split(" ")
        roadDistances.append([roadDistance[0],roadDistance[1],float(roadDistance[2])])
        
    #heuristics reader    
    numberOfHeuristics = int(dataRead.readline())
    for eachLine in range(0,numberOfHeuristics):
        eachPlaceHeuristic = dataRead.readline().strip("\n").split(" ")
        heuristicValue[eachPlaceHeuristic[0]] = float(eachPlaceHeuristic[1])
    
    dataRead.close()
    return {"algorithmName":algorithmName,
            "startState":startState,
            "goalState":goalState,
            "roadDistances":roadDistances,
            "heuristicValue":heuristicValue}
 
def writeOutput(outputFile="output.txt",stringToWrite=""):
    dataWrite = open(outputFile, "w")
    dataWrite.write(stringToWrite)
    dataWrite.close()

def mainAlgo(inputFile="input.txt",outputFile="output.txt",isWrite=True): 
    dataValues = readInput(inputFile)
    mapRoadDistances=dataValues['roadDistances']
    g=Graph()
    for eachEdgeInMap in mapRoadDistances:
        g.addEdge(eachEdgeInMap[0],eachEdgeInMap[1], eachEdgeInMap[2])
    g.addHeuristics(dataValues['heuristicValue'])
    ans=""
    if dataValues['algorithmName']=="BFS":
        listOfValue= g.bfs(dataValues['startState'],dataValues['goalState'])
        count = 0
        for eachValue in listOfValue:
            ans = ans + eachValue + " "+ str(count)+"\n"
            count=count+1
    elif dataValues['algorithmName']=="DFS":
        listOfValue= g.dfs(dataValues['startState'],dataValues['goalState'])
        count = 0
        for eachValue in listOfValue:
            ans = ans + eachValue + " "+ str(count)+"\n"
            count=count+1
    elif dataValues['algorithmName']=="UCS":
        listOfValue = g.ucs(dataValues['startState'],dataValues['goalState'])
        for eachLocation in listOfValue:
            ans = ans + eachLocation[0]+" "+str(int(eachLocation[1]))+"\n"
    elif dataValues['algorithmName']=="A*":
        listOfValue = g.astar(dataValues['startState'],dataValues['goalState'])
        for eachLocation in listOfValue:
            ans = ans + eachLocation[0]+" "+str(int(eachLocation[1]))+"\n" 
    else:
        print "None"

    if isWrite:
        writeOutput(outputFile, ans)
    return ans

if __name__ == '__main__':
    mainAlgo()