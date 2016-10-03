#!/usr/bin/env python
# coding=utf-8

import os
import time

os.system('mkdir -p results')

for i in xrange(165):
    os.system('cp ./cases/input{0}.txt ./input.txt'.format(i))
    print("-->On test case #{0}<--".format(i))
    start_time = time.time()
    os.system('./test_target > /dev/null')
    print("Runing time: {0}ms".format(int((time.time() - start_time) * 1000)))
    os.system('diff ./output.txt ./cases/output{0}.txt'.format(i))
    os.system('cp ./output.txt ./results/output{0}.txt'.format(i))

os.system('rm input.txt output.txt')
