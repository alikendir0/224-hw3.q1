In the defined system there is one source (0), and 4 load point (1,2,3,4). In the graph the locations 
of the source and load is given find the smallest path of connection to loads and source. (Minimum 
Spanning Tree of the undirected graph)

![image](https://github.com/alikendir0/224-hw3.q1/assets/115409752/7522ebcf-53a4-4ad4-9005-02c62441a14d)


>Sample Output: 

V=5 
E=10 

0 1 120  
0 2 160  
0 3 300  
0 4 450  
1 0 120  
1 2 200  
1 3 410  
1 4 500  
2 0 160  
2 1 200  
2 3 420  
2 4 400  
3 0 300  
3 1 410  
3 2 420  
3 4 510  
4 0 450  
4 1 500  
4 2 400  
4 3 510  

>Expected Output:

0 1 120  
0 2 160  
0 3 300  
2 4 400  
