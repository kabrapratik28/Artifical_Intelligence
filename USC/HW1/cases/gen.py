import random
import networkx as nx
import sys
if sys.argv[1] == 'er':
	#er=nx.erdos_renyi_graph(100,0.15)
	g=nx.erdos_renyi_graph(int(sys.argv[4]), float(sys.argv[5]))
elif sys.argv[1] == 'ws':
	#ws=nx.watts_strogatz_graph(30,3,0.1) 
	g=nx.watts_strogatz_graph(int(sys.argv[4]), int(sys.argv[5]), float(sys.argv[6])) 
elif sys.argv[1] == 'ba':
	#ba=nx.barabasi_albert_graph(100,5)
	g=nx.barabasi_albert_graph(int(sys.argv[4]), int(sys.argv[5]))
elif sys.argv[1] == 'rl':
	#red=nx.random_lobster(100,0.9,0.9)
	g=nx.random_lobster(int(sys.argv[4]), float(sys.argv[5]), float(sys.argv[6]))

fout = open('input%s.txt' % sys.argv[2], 'w')
fout.write('%s\n' % sys.argv[3])
#fout.write("%s\n"%sys.argv[4])
fout.write('N0\n')
NN = len(g.nodes())
MM = len(g.edges())
#fout.write('N%s\n' % (int(sys.argv[4])-1))
fout.write('N%s\n' % (int(NN)-1))
fout.write('%s\n' % len(g.edges()))
for x,y in g.edges():
	fout.write("N%s N%s %s\n" % (x, y, random.randint(1,100)))
#fout.write('%s\n' % sys.argv[4])
fout.write('%s\n' % NN)
#for i in range((int(sys.argv[4]))):
for i in range((int(NN))):
	fout.write("N%s %s\n" % (i, random.randint(1, 100)))
fout.close()
