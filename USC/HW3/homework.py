import ply.lex as lex
import ply.yacc as yacc
from copy import deepcopy
#safe side recursion depth increased!
import sys
sys.setrecursionlimit(100000)

def preprocess(inputStr,isStripOpenCloseBrackets=False):
    processedStr = inputStr.replace(" ", "").replace("\t","")
    if isStripOpenCloseBrackets:
        if processedStr[0]=='(' and processedStr[-1]==')':
            return processedStr[1:len(processedStr)-1]
    return processedStr
    
def readInput(fileName='input.txt'):
    inputData = open(fileName,'r')
    data = inputData.readlines()
    inputData.close()
    data = map(lambda s: s.strip(), data)
    numberOfQuestion = int(data[0])
    actualQuest = data[1:numberOfQuestion+1]
    kbSize = int(data[numberOfQuestion+1])
    kb = data[numberOfQuestion+2:]
    #remove spaces as preprocessing step
    preprocessedKB = []
    for eachKB in kb:
        preprocessedKB.append(preprocess(eachKB))
    preprocessedActualQues = []
    for eachQues in actualQuest:
        preprocessedActualQues.append(preprocess(eachQues,True)) #True (Pre-processed to remove brackets)
    return {"kb":preprocessedKB,"ques":preprocessedActualQues}

def writeOutput(answers,fileName='output.txt',isWrite=True):
    if isWrite:
        outputData = open(fileName,'w')
        outputData.write(answers)
        outputData.close()
        return True
    else:
        return answers
    
def predicateExtractor(predicateString):
    nameAndVar = predicateString.strip(')').split('(')
    if len(nameAndVar)==1:
        return {'name':nameAndVar[0],'vars':[]}
    return {'name':nameAndVar[0],'vars':nameAndVar[1].split(",")}

def predicateConsolidator(predicateAndListOfArgs):
    if len(predicateAndListOfArgs)==1:
        return predicateAndListOfArgs[0]
    else:
        predicate = predicateAndListOfArgs[0]
        restofOther = ','.join(predicateAndListOfArgs[1:])
        return predicate + "(" + restofOther + ")"

def isConstant(inputVal):
    if 'A'<= inputVal[0] <= 'Z':
        return True
    else:
        return False

def isAllElementInListConstant(listOfVars):
    for eachEle in listOfVars:
        if not isConstant(eachEle):
            return False
    return True

def loopDetect(dictionaryOfMapping,leftVar,rightConst):
    while rightConst in dictionaryOfMapping:
        rightConst = dictionaryOfMapping[rightConst]
        if rightConst == leftVar:
            return True
    return False

def unification_var(leftVar,rightConst,dictionaryOfMapping):
    if leftVar in dictionaryOfMapping:
        return unification(dictionaryOfMapping[leftVar],rightConst,dictionaryOfMapping)
    else:
        newDict = deepcopy(dictionaryOfMapping)
        if leftVar!=rightConst:
            newDict[leftVar] = rightConst
            if loopDetect(newDict, leftVar, rightConst):
                newDict.pop(leftVar, None)
        return newDict
        
def unification(leftPredicateList,rightPredicateList,dictionaryOfMapping):
    if dictionaryOfMapping==None:
        return None
    elif type(leftPredicateList)==list and type(rightPredicateList)==list:
        if len(leftPredicateList)!=len(rightPredicateList):
            return None
        if len(leftPredicateList)==1:
            ans = unification(leftPredicateList[0], rightPredicateList[0], dictionaryOfMapping)
            return ans
        elif len(leftPredicateList)>=2:
            ans = unification(leftPredicateList[0], rightPredicateList[0], dictionaryOfMapping)
            return unification(leftPredicateList[1:],rightPredicateList[1:],ans)
    elif type(leftPredicateList)==str and type(rightPredicateList)==str:
        if isConstant(leftPredicateList) and isConstant(rightPredicateList):
            if leftPredicateList==rightPredicateList:
                return dictionaryOfMapping
            else:
                return None
        elif leftPredicateList==rightPredicateList:
            return dictionaryOfMapping
        elif not isConstant(leftPredicateList):
            ans =  unification_var(leftPredicateList,rightPredicateList,dictionaryOfMapping)
            return ans
        elif not isConstant(rightPredicateList):
            ans =  unification_var(rightPredicateList,leftPredicateList,dictionaryOfMapping)
            return ans
    else:
        return None

def isNegStatment(statement):
    if statement[0]=='~':
        return True
    else:
        return False
    
class Resolution(object):
    def __init__(self,table):
        self.table = table
    
    def resolveTwoClauses(self,leftMap,rightMap,alreadtExistedMapping,leftSideAttension,isLeftPositive):
        rightPositiveEntries = list(rightMap["positives"])
        rightNegativeEntries = list(rightMap["negatives"])
        leftPositiveEntries  = list(leftMap["positives"])
        leftNegativeEntries = list(leftMap["negatives"])
        
        rightPositiveEntriesInChunks = []
        for eachRightPositiveEntry in rightPositiveEntries:
            mapOfNameVars = predicateExtractor(eachRightPositiveEntry)
            rightPositiveEntriesInChunks.append([mapOfNameVars['name']]+mapOfNameVars['vars'])
        rightNegativeEntriesInChunks = []
        for eachRightNegativeEntry in rightNegativeEntries:
            mapOfNameVars = predicateExtractor(eachRightNegativeEntry)
            rightNegativeEntriesInChunks.append([mapOfNameVars['name']]+mapOfNameVars['vars'])
        
        leftPositiveToRemove = []
        rightNegativeToRemove = []
        for eachLeftPositive in leftPositiveEntries:
            for eachRightNegative in rightNegativeEntriesInChunks:
                leftPositiveChunks = predicateExtractor(eachLeftPositive)
                nameVars = [leftPositiveChunks['name']]+leftPositiveChunks['vars']
                ansOfUnification = unification(nameVars, eachRightNegative, alreadtExistedMapping)
                if ansOfUnification!=None:
                    alreadtExistedMapping = ansOfUnification
                    leftPositiveToRemove.append(eachLeftPositive)
                    rightNegativeToRemove.append(predicateConsolidator(eachRightNegative))
        for eachLeftPositiveToRemove in leftPositiveToRemove:
            leftPositiveEntries.remove(eachLeftPositiveToRemove)
        for eachRightNegativeToRemove in rightNegativeToRemove:
            rightNegativeEntries.remove(eachRightNegativeToRemove)
            
        leftNegativeToRemove = []
        rightPositiveToRemove = []
        for eachLeftNegative in leftNegativeEntries:
            for eachRightPositive in rightPositiveEntriesInChunks:
                leftNegativeChunks = predicateExtractor(eachLeftNegative)
                nameVars = [leftNegativeChunks['name']]+leftNegativeChunks['vars']
                ansOfUnification = unification(nameVars, eachRightPositive, alreadtExistedMapping)
                if ansOfUnification!=None:
                    alreadtExistedMapping = ansOfUnification
                    leftNegativeToRemove.append(eachLeftNegative)
                    rightPositiveToRemove.append(predicateConsolidator(eachRightPositive))
        for eachLeftNegativeToRemove in leftNegativeToRemove:
            if eachLeftNegativeToRemove in leftNegativeEntries:
                leftNegativeEntries.remove(eachLeftNegativeToRemove)
        for eachRightPositiveToRemove in rightPositiveToRemove:
            if eachRightPositiveToRemove in rightPositiveEntries:
                rightPositiveEntries.remove(eachRightPositiveToRemove)
        
        if isLeftPositive:
            dataRemained = leftPositiveEntries
        else:
            dataRemained = leftNegativeEntries
            
        if leftSideAttension in dataRemained:
            return None
            
        return {
                "finalMap":{"positives":set(leftPositiveEntries[:]+rightPositiveEntries[:])
                            ,"negatives":set(leftNegativeEntries[:]+rightNegativeEntries[:])},
                "newMapping":alreadtExistedMapping
                }
    
    def isSeenGoal(self,mapOfRemainingGoals,seenGoalStates):
        positivePredicates = list(mapOfRemainingGoals["positives"])
        negativePredicates = list(mapOfRemainingGoals["negatives"])
        modifiedNegativePredicates = []
        for eachNegativePred in negativePredicates:
            modifiedNegativePredicates.append('~'+eachNegativePred)
        setOfAllPredicates = frozenset(positivePredicates+modifiedNegativePredicates)
        if setOfAllPredicates in seenGoalStates:
            return None
        else:
            seenGoalStates.add(setOfAllPredicates)
            return seenGoalStates
            
    def resolve(self,eachStat):
        isGivenStatementNeg = isNegStatment(eachStat)
        remainingMapToProve = {"positives":set(),"negatives":set()}
        #send in reverse
        if isGivenStatementNeg:
            remainingMapToProve["positives"].add(eachStat[1:]) #removed ~
        else:
            remainingMapToProve["negatives"].add(eachStat)
        mapOfVariableToConstant = {}
        seenGoalStates = set()
        return self.internalResolve(remainingMapToProve, mapOfVariableToConstant, seenGoalStates)
        
    def internalResolve(self,remainingMapToProve,mapOfVariableToConstant,seenGoalStates):
        '''
        This algorithm recursive DFS with infinite loop detection via seen goal states.
        remainingMapToProve = {"positives": set(...) , "negatives":set(...)} #all remained to prove
        mapOfVariableToConstant = {'k1':John,'k2':'k1' , etc ... }
        seenGoalStates = set (frozenset(...), frozenset(....))
        '''        
        for eachPositiveEntry in remainingMapToProve["positives"]:
            nameOfPositivePredicate = predicateExtractor(eachPositiveEntry)['name']
            #check for negative entry in table
            dataForPredicate = self.table[nameOfPositivePredicate]
            if None==dataForPredicate or len(dataForPredicate)==0:
                return False
            negativeEntriesInTable = dataForPredicate['negatives']
            #if no negative entry return false
            if None==negativeEntriesInTable or len(negativeEntriesInTable)==0:
                return False
            else :
                #for each negative entry
                for eachNegativeEntry in negativeEntriesInTable:
                    #unify positive and negatives O(n^2)                    
                    unifiedMap = self.resolveTwoClauses(remainingMapToProve, eachNegativeEntry, deepcopy(mapOfVariableToConstant), eachPositiveEntry, True)
                    
                    #goal failed to unify
                    if None == unifiedMap:
                        continue

                    #add new mapping of symbol to constant
                    newMapOfVariableToConstant = unifiedMap["newMapping"]
                    #add new remaining unifiers
                    newRemainingMapToProve= unifiedMap["finalMap"]
                    #add to seen goal state
                    newSeenGoal = self.isSeenGoal(newRemainingMapToProve, deepcopy(seenGoalStates))
                    #if already saw this goal state ... infinite loop detected ! (return false)
                    if None == newSeenGoal:
                        return False
                    #call internal resolve with deep copy
                    isResolved = self.internalResolve(deepcopy(newRemainingMapToProve), deepcopy(newMapOfVariableToConstant), deepcopy(newSeenGoal))
                    #if resolved then return true
                    if isResolved==True:
                        return True
                    
        for eachNegativeEntry in remainingMapToProve["negatives"]:
            nameOfNegativePredicate = predicateExtractor(eachNegativeEntry)['name']
            #check for negative entry in table
            dataForPredicate = self.table[nameOfNegativePredicate]
            if None==dataForPredicate or len(dataForPredicate)==0:
                return False
            positiveEntriesInTable = dataForPredicate['positives']
            #if no negative entry return false
            if None==positiveEntriesInTable or len(positiveEntriesInTable)==0:
                return False
            else :
                #for each negative entry
                for eachPositiveEntry in positiveEntriesInTable:
                    #unify positive and negatives O(n^2)                    
                    unifiedMap = self.resolveTwoClauses(remainingMapToProve, eachPositiveEntry, deepcopy(mapOfVariableToConstant), eachNegativeEntry, False)
                    
                    #goal failed to unify
                    if None == unifiedMap:
                        continue
                    
                    #add new mapping of symbol to constant
                    newMapOfVariableToConstant = unifiedMap["newMapping"]
                    #add new remaining unifiers
                    newRemainingMapToProve= unifiedMap["finalMap"]
                    #add to seen goal state
                    newSeenGoal = self.isSeenGoal(newRemainingMapToProve, deepcopy(seenGoalStates))
                    #if already saw this goal state ... infinite loop detected ! (return false)
                    if None == newSeenGoal:
                        return False
                    #call internal resolve with deep copy
                    isResolved = self.internalResolve(deepcopy(newRemainingMapToProve), deepcopy(newMapOfVariableToConstant), deepcopy(newSeenGoal))
                    #if resolved then return true
                    if isResolved==True:
                        return True
                    
        #no positive and negative values remained
        if len(remainingMapToProve["positives"])==0 and len(remainingMapToProve["negatives"])==0:
            return True
        else:
            return False
    
class PredicateTable(object):
    def __init__(self,):
        self.variableNameAlreadyUsed = []
        self.table ={}  #key:predicate, value:{ "Positive": [pointers to map of +ve-ve in which appear] ,"Negative":..... }
        self.variableNameCounter = 0  #k1, k2, k3, k4, k5, ..., etc.
        
    def fillEntries(self,eachEntry,posOrNeg,mapOfPositiveAndNegativePredicate):
        predInfo = predicateExtractor(eachEntry)
        predName = predInfo['name']
        predVars = predInfo['vars']
        isAllVarsConst = isAllElementInListConstant(predVars)
        if predName not in self.table:
            self.table[predName]={"positives":[],"negatives":[]} #list will contain maps where name predicate appear
        #Add constant predicates in front #helpful in resolution
        if isAllVarsConst:
            allPredicates = [mapOfPositiveAndNegativePredicate] + self.table[predName][posOrNeg]
            self.table[predName][posOrNeg] = allPredicates
        else:
            self.table[predName][posOrNeg].append(mapOfPositiveAndNegativePredicate)
    
    def variableChange(self,mapOfPositiveAndNegativePredicate):
        newEntryMapper = {}
        positiveEntries = mapOfPositiveAndNegativePredicate["positives"]
        negativeEntries = mapOfPositiveAndNegativePredicate["negatives"]
        allEntries = positiveEntries[:]+negativeEntries[:]
        for eachEntry in allEntries:
            predInfo = predicateExtractor(eachEntry)
                
            varsData = predInfo['vars']
            for eachVar in varsData:
                if not isConstant(eachVar):
                    if eachVar in newEntryMapper:
                        continue    #If already mapped then don't do anything People(x)=> King(x)  .. x already mapped
                    if eachVar in self.variableNameAlreadyUsed:
                        newEntryMapper[eachVar]='k'+str(self.variableNameCounter)
                        self.variableNameCounter = self.variableNameCounter +1
                    else:
                        newEntryMapper[eachVar]=eachVar #No replacement
                    self.variableNameAlreadyUsed.append(newEntryMapper[eachVar])
        
        newPositiveEntries = []
        #use new entry mapper to rename variable ... 
        for eachEntry in positiveEntries:
            predInfo = predicateExtractor(eachEntry)
                
            varsData = predInfo['vars']
            newReplacedVar = []
            for eachVar in varsData:
                if isConstant(eachVar):
                    newReplacedVar.append(eachVar)
                else: 
                    newReplacedVar.append(newEntryMapper[eachVar])
            newPositiveEntries.append(predicateConsolidator([predInfo['name']]+newReplacedVar))
                
        newNegativeEntries = []
        #use new entry mapper to rename variable ... 
        for eachEntry in negativeEntries:
            predInfo = predicateExtractor(eachEntry)
                
            varsData = predInfo['vars']
            newReplacedVar = []
            for eachVar in varsData:
                if isConstant(eachVar):
                    newReplacedVar.append(eachVar)
                else: 
                    newReplacedVar.append(newEntryMapper[eachVar])
            newNegativeEntries.append(predicateConsolidator([predInfo['name']]+newReplacedVar))
                
        mapOfPositiveAndNegativePredicate["positives"] = newPositiveEntries
        mapOfPositiveAndNegativePredicate["negatives"] = newNegativeEntries
        
    def buildTable(self,listOfForestCNF):
        for eachTreeOfCNF in listOfForestCNF:
            #list of predicate visited and respective positive and negative form
            mapOfPositiveAndNegativePredicate = eachTreeOfCNF.allPredicateChildNodes()
            
            #change variable names
            self.variableChange(mapOfPositiveAndNegativePredicate)
            
            #for each predicate make table entry
            positiveEntries = mapOfPositiveAndNegativePredicate["positives"]
            negativeEntries = mapOfPositiveAndNegativePredicate["negatives"]
            for eachPositiveEntry in positiveEntries:
                self.fillEntries(eachPositiveEntry, "positives", mapOfPositiveAndNegativePredicate)
            for eachNegativeEntry in negativeEntries:
                self.fillEntries(eachNegativeEntry, "negatives", mapOfPositiveAndNegativePredicate)

class Node(object):
    def __init__(self, data,isOperator=False,leftChild=None,rightChild=None,isPositive=True):
        self.data = data
        self.isOperator = isOperator
        self.leftChild = leftChild
        self.rightChild = rightChild
        self.isPositive = True
    
    def toggleSign(self):
        self.isPositive = self.isPositive ^ True
    
    def toggleAndOr(self):
        if self.isOperator:
            if self.data =='|':
                self.data='&'
            elif self.data =='&':
                self.data = '|'
    
    def moveNegationInward(self):
        if self.isPositive==False:
            #check left child present (only single present)
            if self.isOperator==False:
                None #can't change atomic sentence negation
            else:
                if self.leftChild !=None:
                    self.leftChild.toggleSign()

                #check right child present
                if self.rightChild !=None:
                    self.rightChild.toggleSign()
            
                #make self positive
                self.toggleSign()

                #if both child present toggle and or
                if self.leftChild !=None and self.rightChild!=None:
                    self.toggleAndOr()
    
    def recursivelyMoveNotInside(self): 
        self.moveNegationInward()
        if self.leftChild!=None:
            self.leftChild.recursivelyMoveNotInside()
        if self.rightChild!=None:
            self.rightChild.recursivelyMoveNotInside()   
    
    def distributiveAndOR(self):
        if self.isOperator and self.data=='|':
            #left precedence in distributing
            if self.leftChild!=None and self.leftChild.isOperator and self.leftChild.data=='&':
                #and is dual operator therefore both child will be present.
                grandLeftChildsLeftChild = deepcopy(self.leftChild.leftChild)
                grandLeftChildsRightChild = deepcopy(self.leftChild.rightChild)
                rightChildData = deepcopy(self.rightChild)
                
                #create new left and right child
                newLeftChild = Node('|',True,grandLeftChildsLeftChild,deepcopy(rightChildData))
                newRightChild = Node('|',True,grandLeftChildsRightChild,deepcopy(rightChildData))
                self.leftChild = newLeftChild
                self.rightChild = newRightChild
                self.data = '&'
            elif self.rightChild!=None and self.rightChild.isOperator and self.rightChild.data=='&':
                #swap children and can call above same method
                grandRightChildsLeftChild = deepcopy(self.rightChild.leftChild)
                grandRightChildsRightChild = deepcopy(self.rightChild.rightChild)
                leftChildData = deepcopy(self.leftChild)

                #create new left and right child
                newLeftChild = Node('|',True,deepcopy(leftChildData),grandRightChildsLeftChild)
                newRightChild = Node('|',True,deepcopy(leftChildData),grandRightChildsRightChild)
                self.leftChild = newLeftChild
                self.rightChild = newRightChild
                self.data = '&'
                
    def recursivelyDistributeAndOr(self):
        self.distributiveAndOR()
        if self.leftChild!=None:
            self.leftChild.recursivelyDistributeAndOr()
        if self.rightChild!=None:
            self.rightChild.recursivelyDistributeAndOr()   

    def breakOnAnd(self):
        if self!=None:
            if not self.isOperator : 
                return [deepcopy(self),] #returns tree
            if self.isOperator:
                if self.data=='|':
                    return [deepcopy(self),] #returns tree
                if self.data=='&':
                    leftChildResult = self.leftChild.breakOnAnd()
                    rightChildResult = self.rightChild.breakOnAnd()
                    return leftChildResult + rightChildResult
    
    def allPredicateChildNodes(self):
        childs = {"positives":[],"negatives":[]}
        if self==None:
            return childs
        if not self.isOperator:
            if self.isPositive:
                childs["positives"].append(self.data)
            else:
                childs["negatives"].append(self.data)
            return childs
        elif self.isOperator:
            leftChildData = self.leftChild.allPredicateChildNodes()
            rightChildData = self.rightChild.allPredicateChildNodes()
            return {"positives":leftChildData["positives"][:]+rightChildData["positives"][:],
                    "negatives":leftChildData["negatives"][:]+rightChildData["negatives"][:]}
            
    def __str__(self, level=0):
        isPos =''
        if self.isPositive==False:
            isPos='~'
        ret = "\t"*level+isPos+self.data+"\n"
        if self.leftChild!=None:
            ret += self.leftChild.__str__(level+1)
        if self.rightChild!=None:
            ret += self.rightChild.__str__(level+1)
        return ret
        #return str({"data":self.data,"isOperator":self.isOperator,"leftChild":str(self.leftChild),"rightChild":str(self.rightChild),"isPositive":self.isPositive})
  
# List of token names.   This is always required
tokens = (
   'NOT',
   'AND',
   'OR',
   'IMPLIES',
   'PREDICATE',
   'COMMA',
   'LPAREN',
   'RPAREN',
   'VARIABLE',
   'CONSTANT'
)

# Regular expression rules for simple tokens
t_NOT      = r'\~'
t_AND      = r'\&'
t_OR       = r'\|'
t_IMPLIES  = r'\=\>'
t_COMMA    = r','
t_LPAREN   = r'\('
t_RPAREN   = r'\)'
# A string containing ignored characters (spaces and tabs)
t_ignore  = ' \t'

def t_VARIABLE(t):
    r'[a-z]'
    t.type = 'VARIABLE'
    return t

def t_PREDICATE(t):
    r'[A-Z]+[A-Za-z0-9]*\([A-Za-z0-9,]+\)'
    t.type = 'PREDICATE'
    return t

def t_CONSTANT(t):
    r'[A-Z]+[A-Za-z0-9]*[^\(]'
    t.type = 'CONSTANT'
    return t

def t_newline(t):
    r'\n+'
    t.lexer.lineno += len(t.value) 
       
# Error handling rule
def t_error(t):
    print("Illegal character '%s'" % t.value[0])
    t.lexer.skip(1)

##YACC Parser
precedence = (
    ('left', 'IMPLIES'),
    ('left' , 'OR'     ),
    ('left' , 'AND'    ),
    ('left' , 'NOT'    ),
    ('left' , 'LPAREN', 'RPAREN')
)


def p_atomic_sentence_structure(p):
    'sentence : atomicSentence'
    p[0] = p[1]
    
def p_complex_sentence_structure(p):
    'sentence : complexSentence'
    p[0] = p[1]

def p_atomic_predicate_sentence_structure(p):
    'atomicSentence : PREDICATE'
    p[0] = Node(p[1])

def p_complex_paran(p):
    'complexSentence : LPAREN sentence RPAREN'
    p[0] = p[2]
    
def p_complex_not(p):
    'sentence : NOT sentence'
    p[2].toggleSign()
    p[0] = p[2]
    
def p_complex_and(p):
    'sentence : sentence AND sentence'
    p[0] = Node('&',True,p[1],p[3])
    
def p_complex_or(p):
    'sentence : sentence OR sentence'
    p[0] = Node('|',True,p[1],p[3])

def p_complex_implies(p):
    'sentence : sentence IMPLIES sentence'
    p[1].toggleSign()
    p[0] = Node('|',True,p[1],p[3])

def p_complex_variable(p):
    'sentence : VARIABLE'
    p[0] = Node(p[1])

# Error rule for syntax errors
def p_error(p):
    print p
    print("Syntax error in input!")

def main(inputFile='input.txt',outputFile='output.txt',isWrite=True):
    # Build the lexer
    lexer = lex.lex()
    # Build the parser
    parser = yacc.yacc()
    tableOfPredicate = PredicateTable()
    kbAndQues = readInput(inputFile)
    for eachKB in kbAndQues["kb"]:
        result = parser.parse(eachKB)
        result.recursivelyMoveNotInside()
        result.recursivelyDistributeAndOr()
        forestOfCNF = result.breakOnAnd()
        #each kb sentence forms multiple cnf and each one added.
        tableOfPredicate.buildTable(forestOfCNF)
        #print (result)
        #print(forestOfCNF)
        #print ("===========")
    #print (tableOfPredicate)
    resolution = Resolution(tableOfPredicate.table)
    answers=''
    for eachStat in kbAndQues["ques"]:
        eachAns = False
        try:
            eachAns = resolution.resolve(eachStat)
        except:
            eachAns = False
        if eachAns:
            answers=answers+"TRUE\n"
        else:
            answers=answers+"FALSE\n"
    return writeOutput(answers,outputFile,isWrite)
    
if __name__ == "__main__": 
    main()