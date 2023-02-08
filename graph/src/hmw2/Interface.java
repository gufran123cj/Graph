package hmw2;

import java.util.HashMap;
public interface Interface<V,E> {
	  
	V addVertex(V vertex); // add a new vertex
	
	E addEdge(V source, V destination, E edge); // add a new edge
	
	V[] endVertices(E edge); // returns an array of 2 end point vertex (source first)
	
	E getEdge(V source, V destination); // returns a edge between two vertex (source to destination)
	
	V opposite(V source, E edge); // returns the other vertex of edge
	
	int numEdges(); // number of edges
	
	int numVertices(); // number of vertices
	
	int inDegree(V vertex); // number of in degrees of a vertex
	
	int outDegree(V vertex); // number of out degrees of a vertex
	
	Iterable<E> outgoingEdges(V vertex); // Iteration of outgoing edges
	
	Iterable<E> incomingEdges(V vertex); // Iteration of incoming edges

	HashMap<String, Edge> edges(); // Iteration of all Edges
	
	HashMap<String, Vertex> vertices(); // Iteration of all Vertices
}
