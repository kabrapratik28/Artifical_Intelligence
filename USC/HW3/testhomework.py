import unittest
import homework
import glob
import time
import os
'''
#Unification test cases
'''
class TestAI(unittest.TestCase):
    def test_Unification(self):
        self.assertDictEqual({'x': 'Jane'},homework.unification(['Knows','x','John'], ['Knows','Jane','John'], {}),"Fail1")
        self.assertDictEqual({'x': 'Jane'}, homework.unification(['Knows','John','Jane'], ['Knows','John','x'], {}),"Fail2")
        self.assertEqual(None, homework.unification(['Knows','John','x'], ['Knows','Jane','John'], {}),"Fail3")
        self.assertEqual( {'y': 'John', 'x': 'Jane'},homework.unification(['Knows','x','John'], ['Knows','Jane','y'], {}),"Fail4")
        self.assertEqual( {'x': 'Jane', 'z': 'y'},homework.unification(['Knows','x','z'], ['Knows','Jane','y'], {}),"Fail5")
        self.assertEqual( {'y': 'Jane', 'x': 'Jane'},homework.unification(['Knows','x','x'], ['Knows','Jane','y'], {}),"Fail6")
        self.assertEqual( None,homework.unification(['Knows','x','John'], ['Knows','Jane','x'], {}),"Fail6")
        self.assertEqual( {'y': 'w', 'x': 'z'},homework.unification(['Knows','x','y'], ['Knows','z','w'], {}),"Fail7")
        self.assertEqual( {'y': 'Jane', 'x': 'y'},homework.unification(['Knows','x','x'], ['Knows','y','Jane'], {}),"Fail8")
        self.assertEqual( {'y': 'Jane', 'x': 'y'},homework.unification(['Knows','x',], ['Knows','Jane'], {'y': 'Jane', 'x': 'y'}),"Fail9")
        self.assertEqual( {'x': 'Jane', 'l': 'y'},homework.unification(['l'], ['y'], { 'x': 'Jane'}),"Fail10")
        self.assertEqual( {},homework.unification('True','True', {}),"Fail11")
    
    def test_preprocessed(self):
        self.assertEqual('~A(John)',homework.preprocess('(~A(John))    ', True),"Fail Preprocessing")
        self.assertEqual('~A(John)',homework.preprocess('    (       ~A(   John     )  )', True),"Fail Preprocessing")
        self.assertEqual('~A(John)',homework.preprocess('    (       ~A(   John     )  )', True),"Fail Preprocessing")
        self.assertEqual('A(John)',homework.preprocess('(       A(   John     )  )', True),"Fail Preprocessing")
        self.assertEqual('A(John)',homework.preprocess('  A(   John     )', True),"Fail Preprocessing")
        self.assertEqual('A(John)',homework.preprocess(' A(John)', True),"Fail Preprocessing")

    def test_hw(self):
        
        self.nameOfFiles=[]
        os.chdir("testcases")
        for fileName in glob.glob("*.in"):
            self.nameOfFiles.append(fileName.split(".")[0])
        for eachFile in self.nameOfFiles:
            print "Running testcase ",eachFile
            start_time = time.time()
            ans = homework.main(eachFile+".in", "", False).strip()
            end_time = time.time()
            print "Running testcase ",eachFile, " took ",(end_time-start_time)
            with open(eachFile+".out", 'r') as myfile:
                expectedOutPut = myfile.read().strip()
            if ans != expectedOutPut:
                print eachFile
                print ans
                print "-----"
                print expectedOutPut
            self.assertEqual(expectedOutPut, ans, "Wrong output for "+eachFile+".in")
        
        print "Running test case suite 2"
        
        #Test case suite 2
        self.nameOfFiles=[]
        os.chdir("../test-cases")
        for fileName in glob.glob("in*"):
            self.nameOfFiles.append(fileName.strip("input").split(".")[0])
        for eachFile in self.nameOfFiles:
            print "Running testcase ",eachFile
            start_time = time.time()
            ans = homework.main("input"+eachFile+".txt", "", False).strip()
            end_time = time.time()
            print "Running testcase ",eachFile, " took ",(end_time-start_time)
            with open("output"+eachFile+".txt", 'r') as myfile:
                expectedOutPut = myfile.read().replace('\r','').strip()
            if ans != expectedOutPut:
                print eachFile
                print ans
                print expectedOutPut
            self.assertEqual(expectedOutPut, ans, "Wrong output for "+eachFile+".in")
        
    
if __name__ == '__main__':
    unittest.main()