# Scenario

Suppose that nodes (Figure 1) represent power distribution plants and weighted edges represent the maximum power of electric current that can be flowed through the given direction at a particular time.
What is the maximum power of electric current that can be sent from the plant A to the plant C in a day? 
At beginning of the day, the plant A can deliver maximum 18 kwatt power as the sum of outgoing edges’ weights (9+2+7=18). 
In the next step, plant B, D, and G should flow the electric current they have received. The plant B can send 4 kwatt power to E, 2 kwatt power to D, while the rest 3 kwatt power will be absorbed, since there is not enough capacity to transfer this power via the outgoing edges of B. 
This process will continue until the destination plant C receives all possible electric power via its incoming edges.

![image](https://user-images.githubusercontent.com/74301873/217537586-32479819-12ee-4a13-b384-b4f19b8c318e.png)

Figure 1: A sample electric current flow graph for power distribution plants


