package hmw2;


public class Edge {
	private String source;
	private String destination;
	private int weight;
	private int usage;
	
	public Edge(String source, String destination, int weight) {
		this.source = source;
		this.destination = destination;
		this.weight = weight;
		this.usage = 0;
	}
	
	public Edge(String source, String destination, int weight, int usage) {
		this.source = source;
		this.destination = destination;
		this.weight = weight;
		this.usage = usage;
	}

	public int getWeight() { // return capacity of edge
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getUsage() { //return edge's usage of capacity
		return usage;
	}

	public void setUsage(int usage) {
		this.usage = usage;
	}
	
	public void addUsage(int add) { // add a integer to the usage
		this.usage += add;
	}

	public String getSource() { //return name of source vertex
		return source;
	}

	public String getDestination() {	//return name of destination vertex
		return destination;
	}

	public String getName() { //return the name of edge (Example: "A-B")
		return source + "-" + destination;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}
	
}

