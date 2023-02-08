package hmw2;

import java.util.HashMap;

public class graph implements Interface<Vertex, Edge>{ 
	
	static HashMap<String, Vertex> vertices;
	static HashMap<String, Edge> edges;

	public graph() {			//directed graph for maximum flow algorithm
		vertices = new HashMap<String, Vertex>();
		edges = new HashMap<String, Edge>();
	}

	@Override
	public Vertex addVertex(Vertex vertex){ //add a new vertex (it will not add if vertex were already added)
		if(vertices.get(vertex.getName()) == null) {
			vertices.put(vertex.getName(), vertex);
			return vertex;
		}
		else 
			return (vertices.get(vertex.getName()));
	}

	@Override
	public Edge addEdge(Vertex source, Vertex destination, Edge edge){ 	//add a new edge (it will add weight to old edge if edge were already added)
		
		if(vertices.get(source.getName()) == null){
			addVertex(source);
		}

		if(vertices.get(destination.getName()) == null){
			addVertex(destination);
		}
		
		if(edges.get(edge.getName()) == null) {
			Vertex tempDest = (vertices.get(destination.getName()));
			tempDest.addInEdge(edge);
			vertices.remove(destination.getName());
			addVertex(tempDest);
			
			Vertex tempSource = (vertices.get(source.getName()));
			tempSource.addOutEdge(edge);
			vertices.remove(source.getName());
			addVertex(tempSource);
			
			edges.put(edge.getName(), edge);
			return edge;
		}
		else {
			int newWeight = edge.getWeight() + edges.get(edge.getName()).getWeight();
			edge.setWeight(newWeight);
			edges.remove(edge.getName());
			edges.put(edge.getName(), edge);
			source = vertices.get(source.getName());
			destination = vertices.get(destination.getName());
			source.changeOutEdge(edge);
			destination.changeInEdge(edge);
			return edge;
		}
	}

	@Override
	public Vertex[] endVertices(Edge edge) { //return array of end point vertices of an edge
		return new Vertex[] {vertices.get(edge.getSource()), vertices.get(edge.getDestination())};
	}

	@Override
	public Edge getEdge(Vertex source, Vertex destination) { //return an edge between two vertices
		return edges.get(source.getName()+ "-" + destination.getName());
	}

	@Override
	public Vertex opposite(Vertex source, Edge edge) { //return the opposite side vertex of an edge
		return (edge.getSource().equals(source.getName()))? vertices().get(edge.getDestination()) : vertices().get(edge.getSource());
	}

	@Override
	public int numEdges() { //number of edges
		return edges.size();
	}

	@Override
	public int numVertices() { //number of vertices
		return vertices.size();
	}

	@Override
	public int inDegree(Vertex vertex) { //return number of inner edges of an vertex
		return vertex.getInEdges().size();
	}

	@Override
	public int outDegree(Vertex vertex) { //return number of out edges of an vertex
		return vertex.getOutEdges().size();
	}

	@Override
	public Iterable<Edge> outgoingEdges(Vertex vertex) { //return out edges of an vertex
		return vertex.getOutEdges();
	}

	@Override
	public Iterable<Edge> incomingEdges(Vertex vertex) { //return inner edges of an vertex
		return vertex.getInEdges();
	}

	@Override
	public HashMap<String, Edge> edges() { //return all edges of graph (hash map)
		return edges;
	}

	@Override
	public HashMap<String, Vertex> vertices() { //return all vertices of graph (hash map)
		return vertices;
	}
}

