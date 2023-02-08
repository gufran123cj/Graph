package hmw2;

import java.util.ArrayList;

public class Vertex {
	private String name;
	private ArrayList<Edge> outEdges;
	private ArrayList<Edge> inEdges;
	
	public Vertex(String name) {
		this.name = name;
		outEdges = new ArrayList<Edge>();
		inEdges = new ArrayList<Edge>();
	}
	
	public void addInEdge(Edge e) { //adds an inner edge to list
		inEdges.add(e);
	}
	
	public void addOutEdge(Edge e) { //adds an out edge to list
		outEdges.add(e);
	}
	
	public void changeInEdge(Edge newE) {  //changes an inner edge with a new edge
		for (int i = 0; i < inEdges.size(); i++) {
			if(newE.getName().equals(inEdges.get(i).getName())) {
				inEdges.remove(i);
				inEdges.add(i, newE);
				break;
			}
		}
	}
	
	public void changeOutEdge(Edge newE) {	//changes an out edge with a new edge
		for (int i = 0; i < outEdges.size(); i++) {
			if(newE.getName().equals(outEdges.get(i).getName())) {
				outEdges.remove(i);
				outEdges.add(i, newE);
				break;
			}
		}
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<Edge> getInEdges(){ // return inner edges of vertex
		return inEdges;
	}
	
	public ArrayList<Edge> getOutEdges(){ // return out edges of vertex
		return outEdges;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOutEdges(ArrayList<Edge> outEdges) {
		this.outEdges = outEdges;
	}

	public void setInEdges(ArrayList<Edge> inEdges) {
		this.inEdges = inEdges;
	}
	
}

