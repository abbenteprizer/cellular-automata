# cellular-automata
Simple 2D cell automata with states; healty, sick, immune and dead. Disease spreads to all adjacent nodes (8 neighbors for each node).
Don't forget to compile.

To run program with default arguments type the following command.

> java Sim

To run with different arguments you need to give the commands in the following order:
java Sim dimension probSick probDead simDays minDays maxDays sickInit x1,y2… seed

Example:
> java Sim 5 0.06 0.2 20 2 3 2 2,2 1,1 1324687123045 
