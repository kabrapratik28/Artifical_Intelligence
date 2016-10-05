import random
import os
import numpy #version 1.11.2
import unittest
import homework
#create folder
testcases="testcases"
currentDirectory = os.getcwd()
os.chdir(currentDirectory)
try:
    os.mkdir(testcases)
except :
    print "Ruuning test suite ... For creating test case delete folder!"
os.chdir(testcases)

#boundaries of testCases
N=15
problemType = ["MINIMAX","ALPHABETA"]
player = ["X","O"]
possibleMoves = [".","X","O"]
cellValue = 99
numberOfTestCases = 100
depthLimit = 5
minMaxDepthLimit = 2

#expected output generator
class TestAI(unittest.TestCase):

    def testCaseGenerator(self):
        #testcase generator
        for eachTestCase in range(numberOfTestCases):
            nameOfInputFile = str(eachTestCase)+".in"
            gridSize = random.randint(2,N)
            grid =""
            for i in range(gridSize):
                #row Generator
                eachRow =""
                for j in range(gridSize):
                    eachRow = eachRow + " "+str(random.randint(1,cellValue))
                eachRow = eachRow + "\n"
                grid = grid + eachRow.strip(" ")
            
            gridAquired = ""
            for i in range(gridSize):
                #row Generator
                eachRow =""
                for j in range(gridSize):
                    #X O . terms Generator with probablities
                    eachRow = eachRow +str(numpy.random.choice(possibleMoves, p=[0.5,.25,.25]))
                eachRow = eachRow + "\n"
                gridAquired = gridAquired + eachRow.strip(" ")
            gridAquired = gridAquired.strip(" ")
            
            youPlay = numpy.random.choice(player)
            algorithm = numpy.random.choice(problemType,)
            depth = 1
            
            #Don't get large problems ... 
            if gridSize > 1 and  gridSize <= 3  and algorithm =="MINIMAX":
                depth = numpy.random.choice([1,2,3,4])
            elif  gridSize > 3 and gridSize <= 6 and algorithm =="MINIMAX":
                depth = numpy.random.choice([1,2,])
            else:
                algorithm = "ALPHABETA"
                depth = numpy.random.choice(numpy.arange(1, depthLimit))
            
            question = str(gridSize)+"\n"+algorithm+"\n"+youPlay+"\n"+str(depth)+"\n"+grid+gridAquired
            inputFile = open(nameOfInputFile,"w")
            inputFile.write(question)
            inputFile.close()
                
        #testcase generator ends
        
        #test case output generator
        for eachTestCase in range(numberOfTestCases):
            print "Test case working on "+str(eachTestCase)
            nameOfInputFile = str(eachTestCase)+".in"
            nameOfOutputFile = str(eachTestCase)+".out"
            #takes input from nameOfInputFile and writes to nameOfOutputFile
            homework.runHomeWork(nameOfInputFile,nameOfOutputFile)
        
if __name__ == '__main__':
    unittest.main()      