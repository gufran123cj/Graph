package hmw2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class pathflow {			//This class makes Ford Fulkerson's max-flow algorithm and use this graph for task2 and task3
	static graph DirectedGraph;
	static Vertex sourceVertex;
	static Vertex lastVertex;
	static int maxFlow;
	static ArrayList<Path> flowPaths; //task1
	static HashMap<String, Edge> minCut; //task2
	HashMap<String, Integer> maximizeEdges; //task3
	
	public pathflow(graph directedGraph, String sVertex, String lastVertex) {
		DirectedGraph = directedGraph;
		this.sourceVertex = DirectedGraph.vertices.get(sVertex);
		this.lastVertex = DirectedGraph.vertices.get(lastVertex);
		this.maxFlow = 0;
		this.flowPaths = new ArrayList<Path>();
		this.minCut = new HashMap<String, Edge>();
		this.maximizeEdges = new HashMap<String, Integer>();
	}

	class Path {
		private ArrayList<Edge> pathEdges;
		private ArrayList<Vertex> pathVertices;
		
		public Path(ArrayList<Edge> pathEdges, ArrayList<Vertex> pathVertices) {	// path object helps for save a flow path, so ford fulkerson's algorithm can work faster
			this.pathEdges = pathEdges;
			this.pathVertices = pathVertices;
		}

		public ArrayList<Edge> getPathEdges() { //return path's edges
			return pathEdges;
		}

		public ArrayList<Vertex> getPathVertices() { // return path's vertices
			return pathVertices;
		}
		
		public int getBottleneckValue() {	// calculates and returns bottleneck value of path
			int bottleneck = Integer.MAX_VALUE;
			
			for (int i = 0; i < pathVertices.size()-1; i++) {	// for normal flow edges
				if(pathEdges.get(i).getSource().equals(pathVertices.get(i).getName())) {
					if(bottleneck > (pathEdges.get(i).getWeight() - pathEdges.get(i).getUsage()))
						bottleneck = pathEdges.get(i).getWeight() - pathEdges.get(i).getUsage();
				}
				else {	// for reverse flow edges
					if(bottleneck > (pathEdges.get(i).getUsage()))
						bottleneck = pathEdges.get(i).getUsage();
				}

			}
			return bottleneck;
		}
		
		public void printPath() { //prints the path
			int bottleneck = getBottleneckValue();
			
			for (int i = 0; i < pathVertices.size()-1; i++) {
				if(pathEdges.get(i).getSource().equals(pathVertices.get(i).getName())) {
					System.out.println("Step [" + (i) +"] = " + pathEdges.get(i).getSource()+">["+bottleneck+"]>"+pathEdges.get(i).getDestination());
				}
				else {
					System.out.println("Step [" + (i) +"] = " + pathEdges.get(i).getDestination()+">["+(-bottleneck)+"]>"+pathEdges.get(i).getSource());
				}
			}
		}
	}
	
	//https://www.programiz.com/dsa/ford-fulkerson-algorithm
	public void FindMaxFlow(boolean wantToSeeThePaths) { // max flow (FORD FULKERSON'S ALGORITHM)
		int number = 1;
		System.out.println("->> { Task.1 MAX FLOW } <<- \n");
		
		while(true) {
			Path tempPath = new Path(null, null);
			tempPath = findFlowPath(sourceVertex,lastVertex);
			
			if(tempPath != null) {
				flowPaths.add(new Path(tempPath.getPathEdges(), tempPath.getPathVertices()));
				
				if(wantToSeeThePaths) {
					System.out.println("-	["+number+"].Path	-\n");
					tempPath.printPath();
					System.out.println();
				}
				
				setFlow(tempPath,tempPath.getBottleneckValue());
			}
			else {
				System.out.println("\n>>>	MAX-FLOW:	" + maxFlow + "\n");
				break;
			}
			number++;
		}	
	}
	
	public void FindMinCutEdges() { // minimum cut edges
		System.out.println();
		System.out.println("->> { Task.2 MIN CUT } <<- \n");
		int number = 1;
		minCut = getMinCuts(flowPaths);
		
		for (String eName : minCut.keySet()) { //print all minimum cut edges
			System.out.println("[" + number + "]	edge:	" + eName);
			number++;
		}
	}
	
	public void maximizeTheEdges() { //Maximize the minimum cut edges
		System.out.println();
		System.out.println("->> { Task.3 MAXIMIZE } <<- \n");
		int number = 1;
		int extraCap = 0;
		
		setGraphDefault();
		
		while(true) {
			Path tempPath = new Path(null, null);
			tempPath = findFlowPath(sourceVertex,lastVertex);
			
			if(tempPath != null) {
				speacialSetFlow(tempPath,getSpecialBottleneck(tempPath));
			}
			else {
				break;
			}
		}
		
		for (String eName : minCut.keySet()) {
			extraCap = (maximizeEdges.get(eName) == null || maximizeEdges.get(eName) < DirectedGraph.edges.get(eName).getWeight())? 0 : (maximizeEdges.get(eName)-DirectedGraph.edges.get(eName).getWeight());
			System.out.println("[" + number + "]	edge:	" + eName + "		capacity increase:	" + extraCap );
			number++;
		}
	}
		
	private Path findFlowPath(Vertex source, Vertex last) {		//This function returns a path between source to last (if there is no path between them returns null)
		
		Queue<Vertex> flowVertices = new LinkedList<Vertex>();	//queue for BFS algorithm
		HashMap<String, Edge> visitedVertices = new HashMap<String, Edge>(); 	// (String = vertex name) & (Edge = vertex and father of vertex's path)
		
		Stack<Vertex> vertexOrganizer = new Stack<Vertex>(); // flip the path
		Stack<Edge> edgeOrganizer = new Stack<Edge>(); //	flip the path
		
		ArrayList<Edge> edgePath = new ArrayList<Edge>(); //path's edges
		ArrayList<Vertex> vertexPath = new ArrayList<Vertex>(); //path's vertices
		
		flowVertices.add(source);
		visitedVertices.put(source.getName(), null);
		
		while((!flowVertices.isEmpty()) && (visitedVertices.get(last.getName()) == null)) {		// BFS's one turn
			Vertex v = (flowVertices.peek());
			
			for (Edge e : v.getOutEdges()) {	//search for flow
				if( ((e.getWeight() - e.getUsage()) > 0) && (visitedVertices.get(DirectedGraph.opposite(v, e).getName()) == null) ) {
					flowVertices.add(DirectedGraph.opposite(v, e));
					visitedVertices.put(DirectedGraph.opposite(v, e).getName(), e);
				}
			}
			
			for (Edge e : v.getInEdges()) {		//search for reverse flow (isInCut control is for only TASK3)
				if( (((e.getUsage()) > 0) || ((isInCut(e) && maximizeEdges.get(e.getName()) != null && maximizeEdges.get(e.getName()) > 0))) && (visitedVertices.get(DirectedGraph.opposite(v, e).getName()) == null) ) {
					flowVertices.add(DirectedGraph.opposite(v, e));
					visitedVertices.put(DirectedGraph.opposite(v, e).getName(), e);
				}
			}
			
			flowVertices.poll();
		}
		
		if(visitedVertices.get(last.getName()) == null) {	//if last vertex is not reachable
			return null;
		}
		else {	// have a path between source and last
			vertexOrganizer.add(last);
			
			while(!vertexOrganizer.peek().getName().equals(source.getName())) {		// find a path with reverse search from last vertex
				edgeOrganizer.add(visitedVertices.get(vertexOrganizer.peek().getName()));
				vertexOrganizer.add(DirectedGraph.opposite(vertexOrganizer.peek(), edgeOrganizer.peek()));
			}
			
			while(!edgeOrganizer.isEmpty()) { 		//Flip the path info
				edgePath.add(edgeOrganizer.pop());
			}
			while(!vertexOrganizer.isEmpty()) {		//Flip the path info
				vertexPath.add(vertexOrganizer.pop());
			}
			
			return new Path(edgePath, vertexPath);	//Return the path with edges and vertices
		}
	}
	
	
	
	private void setFlow(Path p, int bottleneck) {	// set the changes(add the bottleneck value to edges) to the graph
		maxFlow += bottleneck;
		
		for (int i = 0; i < p.getPathVertices().size()-1; i++) {	//Make changes between vertices
			if(p.getPathEdges().get(i).getSource().equals(p.getPathVertices().get(i).getName())) {	//for a normal flow edge' change
				DirectedGraph.edges.remove(p.getPathEdges().get(i).getName());
				p.getPathEdges().get(i).addUsage(bottleneck);
				DirectedGraph.edges.put(p.getPathEdges().get(i).getName(), p.getPathEdges().get(i));
			}
			else {																	//for a reverse flow edge's change
				DirectedGraph.edges.remove(p.getPathEdges().get(i).getName());
				p.getPathEdges().get(i).addUsage(-bottleneck);
				DirectedGraph.edges.put(p.getPathEdges().get(i).getName(), p.getPathEdges().get(i));
			}
		}
	}
	
	
	
	private HashMap<String, Edge> getMinCuts(ArrayList<Path> paths){ //FOR TASK 2  returns minimum cut edges to use
		HashMap<String, Edge> minCuts = new HashMap<String, Edge>();
		ArrayList<Vertex> nonreachableVertices = getNonreachableVertices(paths);
		
		for (Vertex v : nonreachableVertices) {
			for (Edge e : v.getInEdges()) {
				if(findFlowPath(sourceVertex, DirectedGraph.opposite(v, e)) != null) {
					minCuts.put(e.getName(), e);
				}
			}
		}
		return minCuts;
	}
	
	private ArrayList<Vertex> getNonreachableVertices(ArrayList<Path> paths){ //finds unreachable vertices to find minimum cut calculation
		ArrayList<Vertex> nonreachableVertices = new ArrayList<Vertex>();														//FOR TASK2
		HashMap<String, Vertex> addedVertices  = new HashMap<String, Vertex>();
		
		for (Path p : paths) {
			for (Vertex v : p.getPathVertices()) {
				if(addedVertices.get(v.getName()) == null){
					if(findFlowPath(sourceVertex, DirectedGraph.vertices.get(v.getName())) == null) {
						nonreachableVertices.add(DirectedGraph.vertices.get(v.getName()));
					}
					addedVertices.put(v.getName(), v);
				}
			}
		}
		return nonreachableVertices;
	}
	
	private boolean isInCut(Edge e) { // return a boolean (is a edge in minimum cut edges)
		return (minCut.get(e.getName()) != null);
	}
	
	//-----------------------------------[TASk3]----------------------------------------------
	
	private void setGraphDefault() { // set the graph default for task3's maximize functions
		for (Path p : flowPaths) {
			for (Edge e : p.getPathEdges()) {
				e.setUsage(0);
			}
		}
		flowPaths.clear();
	}
	
	private int getSpecialBottleneck(Path p) {	//returns a path's bottleneck but function can't control for minimum cut edges because that edges can be increase!
		int bottleneck = Integer.MAX_VALUE;													//FOR TASK3
		
		for (int i = 0; i < p.pathVertices.size()-1; i++) {	// for normal flow edges
			if(!isInCut(p.getPathEdges().get(i))) {
				if(p.getPathEdges().get(i).getSource().equals(p.pathVertices.get(i).getName())) {
					if(bottleneck > (p.getPathEdges().get(i).getWeight() - p.getPathEdges().get(i).getUsage()))
						bottleneck = p.getPathEdges().get(i).getWeight() - p.getPathEdges().get(i).getUsage();
				}
				else {	// for reverse flow edges
					if(bottleneck > (p.getPathEdges().get(i).getUsage()))
						bottleneck = p.getPathEdges().get(i).getUsage();
				}
			}
			else if(!(p.getPathEdges().get(i).getSource().equals(p.pathVertices.get(i).getName()))){ //�f there is a reverse flow on cut edges
				if(bottleneck > (maximizeEdges.get(p.getPathEdges().get(i).getName())))
					bottleneck = (maximizeEdges.get(p.getPathEdges().get(i).getName()));
			}
		}
		
		for (int i = 0; i < p.pathVertices.size()-1; i++) {		// save the increased edges and increase value to a hash map
			if(isInCut(p.getPathEdges().get(i))) {
				if((p.getPathEdges().get(i).getSource().equals(p.pathVertices.get(i).getName()))) {
					if(maximizeEdges.get(p.getPathEdges().get(i).getName()) == null)
						maximizeEdges.put(p.getPathEdges().get(i).getName(), bottleneck);
					else {
						int newMAX = (bottleneck + (maximizeEdges.get(p.getPathEdges().get(i).getName())));
						maximizeEdges.remove(p.getPathEdges().get(i).getName());
						maximizeEdges.put(p.getPathEdges().get(i).getName(), newMAX);
					}
				}
				else if(!(p.getPathEdges().get(i).getSource().equals(p.pathVertices.get(i).getName()))) { //if there is a reverse flow on a cut edge there will be negative increasing
					int newMax = maximizeEdges.get(p.getPathEdges().get(i).getName()) - bottleneck;
					
					maximizeEdges.remove(p.getPathEdges().get(i).getName());
					maximizeEdges.put(p.getPathEdges().get(i).getName(), newMax);
				}
			}
		}
		
		return bottleneck;
	}
	
	private void speacialSetFlow(Path p, int bottleneck) {	// adds the bottleneck value to the path's edges but not to minimum cut edges because they can increase
		for (int i = 0; i < p.getPathVertices().size()-1; i++) {	//Make changes between vertices													//FOR TASK3!!
			if(!isInCut(p.getPathEdges().get(i))) {
				if(p.getPathEdges().get(i).getSource().equals(p.getPathVertices().get(i).getName())) {	//for a normal flow edge' change
					DirectedGraph.edges.remove(p.getPathEdges().get(i).getName());
					p.getPathEdges().get(i).addUsage(bottleneck);
					DirectedGraph.edges.put(p.getPathEdges().get(i).getName(), p.getPathEdges().get(i));
				}
				else {																	//for a reverse flow edge's change
					DirectedGraph.edges.remove(p.getPathEdges().get(i).getName());
					p.getPathEdges().get(i).addUsage(-bottleneck);
					DirectedGraph.edges.put(p.getPathEdges().get(i).getName(), p.getPathEdges().get(i));
				}
			}
		}
	}
}


