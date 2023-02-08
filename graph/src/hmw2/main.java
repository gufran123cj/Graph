package hmw2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class main {

	public static void main(String[] args) {
		graph grapınhoynzi = new graph(); 
		pathflow electriccompany;
		String source,last,text = null;
		boolean seePaths;
		Scanner scanner = new Scanner(System.in);
		
		//------------[MENU]--------------
		System.out.print("Enter the .txt name: ");		
		text = (scanner.nextLine());
		text = (text.equals(""))? "graph.txt" : text;
		System.out.println("-> Graph is creating from (" + text + ")...");
		readTheGraph(grapınhoynzi, text);
		System.out.print("Enter the source vertex: ");		
		source = (scanner.nextLine());
		System.out.print("Enter the last vertex: ");		
		last = (scanner.nextLine());
		System.out.print("Do you want to see the task1's paths? (Y for yes): ");		
		seePaths = (scanner.nextLine().toUpperCase().equals("Y"));
		System.out.println("\n");
		scanner.close();
		
		// SOLVING
		electriccompany = new pathflow(grapınhoynzi, source, last); 
		electriccompany.FindMaxFlow(seePaths); //Task1
		electriccompany.FindMinCutEdges();	//Task2
		electriccompany.maximizeTheEdges();	//Task3
	}
	
	public static void readTheGraph(graph graph, String textName) { // Text reader method
		//Scanner settings
		Scanner scanner = new Scanner(System.in);
		File f = new File(textName);
		Scanner file = null;
		String reader = "";
		
		try {									// file control!
			file = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.out.println(textName + ".txt can not be founded! \n");
		}

		//Read the file and create graph
		while(file.hasNextLine()) {
			reader = file.nextLine();
			graph.addVertex(new Vertex(reader.split("	")[0]));
			graph.addVertex(new Vertex(reader.split("	")[1]));
			graph.addEdge(new Vertex(reader.split("	")[0]), new Vertex(reader.split("	")[1]), new Edge((reader.split("	")[0]), (reader.split("	")[1]), Integer.parseInt(reader.split("	")[2])));
		}				
		file.close();
	}
}
