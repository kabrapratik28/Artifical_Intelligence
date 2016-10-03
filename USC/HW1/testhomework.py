import unittest
import homework
import glob, os

class TestAI(unittest.TestCase):
    
    def test_cases_Runner(self):
        self.nameOfFiles=[]
        os.chdir("testcases")
        for fileName in glob.glob("*.in"):
            self.nameOfFiles.append(fileName.split(".")[0])
        for eachFile in self.nameOfFiles:
            print "Running testcase ",eachFile
            ans = homework.mainAlgo(eachFile+".in", "", False)
            with open(eachFile+".out", 'r') as myfile:
                expectedOutPut = myfile.read()
            if expectedOutPut!=ans:
                print ans
                print expectedOutPut
                print "========="
            self.assertEqual(expectedOutPut, ans, "Wrong output for "+eachFile+".in")
        
        print "Other Bunch of testcases"
        
        self.nameOfFiles=[]
        os.chdir("../cases/cases")
        for fileName in glob.glob("input*.txt"):
            self.nameOfFiles.append(fileName.split(".")[0].strip("input"))
        for eachFile in self.nameOfFiles:
            print "Running testcase ",eachFile
            ans = homework.mainAlgo("input"+eachFile+".txt", "", False)
            with open("output"+eachFile+".txt", 'r') as myfile:
                expectedOutPut = myfile.read()
            if expectedOutPut!=ans:
                print ans
                print expectedOutPut
                print "========="
            self.assertEqual(expectedOutPut, ans, "Wrong output for "+eachFile+".in")
            
    def test_PriorityQueue(self):
        pq = homework.PriorityQueue()
        pq.add_task('A', 5)
        self.assertListEqual([[5,0,'A']], pq.pq, "PQ is wrong")
        pq.add_task('A',10)
        self.assertListEqual([[5,0,'A']], pq.pq, "PQ is wrong")
        pq.add_task('A',1)
        self.assertListEqual([[1,1,'A'], [5, 0, '<removed-task>']], pq.pq, "PQ is wrong")
        pq.add_task('C',0)
        self.assertListEqual([[0, 2, 'C'], [5, 0, '<removed-task>'], [1, 1, 'A']], pq.pq, "PQ is wrong")
        self.assertTupleEqual(('C', 0),pq.pop_task())
        
        pq = homework.PriorityQueue()
        pq.add_task('A',5,2)
        pq.add_task('A',5,2)
        pq.add_task('A',10,2)
        self.assertListEqual([[5,0,['A',2]]], pq.pq, "PQ is wrong")
        pq.add_task('A',4,2)
        self.assertListEqual([[4,1,['A',2]],[5, 0, '<removed-task>']], pq.pq, "PQ is wrong")
        pq.add_task('C',2,1)
        self.assertListEqual([[2,2,['C',1]],[5, 0, '<removed-task>'],[4,1,['A',2]]], pq.pq, "PQ is wrong")
        self.assertTupleEqual((['C', 1], 2),pq.pop_task())

if __name__ == '__main__':
    unittest.main()