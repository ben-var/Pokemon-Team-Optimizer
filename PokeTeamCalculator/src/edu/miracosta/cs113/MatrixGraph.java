package edu.miracosta.cs113;
/** This class is the frame for which the graph will be built on.
 * A matrix graph should be more efficient if the graph were to be undirected 
 * with a lot of choices for pokemon it could become dense quickly.
 * 
 * Author: Michael McDermott
 * 
 */
import java.util.*;
public class MatrixGraph<V,W> {
	
	//Data Fields
	private Map<V, Integer> vertices;	// Map that uses the vertices as the key to retrieve the indexes
	private Map<Integer, V> indexes;	// Map that uses the indexes as a key to hold the vertices
	private int numVertices;				// number of vertices in graph
	private int numEdges;					// number of edges on graph
	private boolean directed;				// tells if directed graph or not
	private double edgesWeighted[][];		// 2D array to keep track of edges and stores the weighted values 
	
	
	public static void main(String args[]) {
		PokeTeam team= new PokeTeam();
		team.addPlayer(team.allPokemon.get(90));
		team.addPlayer(team.allPokemon.get(40));
		team.addPlayer(team.allPokemon.get(12));
		team.addPlayer(team.allPokemon.get(2));
		team.addPlayer(team.allPokemon.get(33));
		System.out.println("TEAM BEFORE SUGGESTION");
		System.out.println(team.getTeam().toString());
		System.out.println();
		System.out.println("MAKING SUGGESTION: ");
		System.out.println();
		Pokemon suggestion = team.makeSuggestion();
		System.out.println("SUGGESTED POKEMON: "+ suggestion.getName());
		team.addPlayer(suggestion);
		System.out.println("TEAM AFTER ADDING SUGGESTION");
		System.out.println();
		System.out.println(team.getTeam().toString());
		
		
	}
	
	
	/**Creates a standard empty graph with number of vertices
	 * 
	 * @param numV	the number of vertices
	 * @param directed	whether graph is directed or not
	 */
	public MatrixGraph(int numV, boolean directed) {
		this.directed = directed;
		numVertices = 0;
		edgesWeighted = new double[numV][numV];
		numEdges = 0;
		vertices = new HashMap<V, Integer>(numV);
		indexes = new HashMap<Integer, V>(numV);
		// sets all edges to positive infinity
		for(int i = 0; i < numV; i++) {
			for(int j = 0; j < numV; j++) {
				edgesWeighted[i][j] = Double.POSITIVE_INFINITY;	
			}
		}
	}
	
	/**Adds a vertex to the graph
	 * and updates the size of the matrix, number of vertices, and number of edges
	 * 
	 * @param monster
	 */
	public void addVertex(V vertex) {
		int index = numVertices;
		if(numVertices <= edgesWeighted.length){
			if(!vertices.containsKey(vertex)) {
				vertices.put(vertex, index);
				indexes.put(index, vertex);
				numVertices++;
			}	
		}
	}
	
	/**Removes a vertex from the graph and 
	 * also removes all edges associated with it
	 * 
	 * @param vertexToRemove
	 */
	public void removeVertex(V vertexToRemove){
		int index = vertices.get(vertexToRemove);
		vertices.remove(vertexToRemove);
		indexes.remove(index);
		removeAllEdgesFrom(vertexToRemove);
		numVertices--;	
	}
	
	/**Checks for presence of source and destination vertices in the maps, 
	 * then updates the matrix of weights and increments the number of edges.
	 * 
	 * @param src
	 * @param dest
	 * @param wght
	 */
	public void addEdge(V src, V dest, double wght) {
		//then makes sure both the source and destination vertice exist
		//if they dont exist, they are created and added to the graph

			if(!vertices.containsKey(src)){
				addVertex(src);
			}
			if(!vertices.containsKey(dest)) {
				addVertex(dest);
			}
		
		int srcNum = vertices.get(src);
		int destNum = vertices.get(dest);
		edgesWeighted[srcNum][destNum] =  wght;
		numEdges++;	
	}
	
	/**Removes one edge that is specified by src and dest vertices
	 * 
	 */
	public void removeEdge(V src, V dest){
		if(hasEdge(src, dest)) {
			numEdges--;
		}
		setWeight(src, dest, Double.POSITIVE_INFINITY);
	}
	
	/**Removes all edges attached to the vertex
	 * 
	 * @param vertex the vertex to remove all edges attached to it
	 */
	public void removeAllEdgesFrom(V vertex){
		// check to make vertex exists
		if(vertices.containsKey(vertex)){
			//find out where it is
			int index = vertices.get(vertex);
			// use that value to go through the 2D array and remove the values for the weights associated.
			for(int i = 0; i < edgesWeighted.length; i++) {
				edgesWeighted[index][i] = Double.POSITIVE_INFINITY;
			}
		}
	}
		
	/**Sets the weight for the edge between two adjacent vertices.
	 * Almost the same ass addEdge, but does not increment the number of edges
	 * @param src	the first vertice
	 * @param dest  second one its connected to 
	 * @param wght	the weight thats supposed to be on the edge
	 */
	public void setWeight(V src, V dest, double wght) {
		//then makes sure both the source and destination vertice exist
		//if they dont exist, they are created and added to the graph
			if(!vertices.containsKey(src)){
				addVertex(src);
			}
			if(!vertices.containsKey(dest)) {
				addVertex(dest);
			}
		int srcNum = vertices.get(src);
		int destNum = vertices.get(dest);
		edgesWeighted[srcNum][destNum] =  wght;
	}
	
	/**Checks to see if there is an edge between two vertices
	 * 
	 */
	public boolean hasEdge(V src, V dest){
		boolean hasEdge;
		int source = vertices.get(src);
		int destination = vertices.get(dest);
		if(edgesWeighted[source][destination] != Double.POSITIVE_INFINITY){
			hasEdge = true;
		}
		else {
			hasEdge = false;
		}
		return hasEdge;
	}
	
	/**Gets the vertex of the smallest edge attached to a specified vertex
	 * (sort of like djistraks alg)
	 * @param src the vertex to check all the edges attached to it
	 * @return the vertex that has the smallest edge
	 */
	public V getSmallestEdge(V src){
			
			int location = vertices.get(src);
			double smallestEdge = Double.POSITIVE_INFINITY;
			for(int i = 0; i < edgesWeighted.length; i++) {
				if(edgesWeighted[vertices.get(src)][i] <= smallestEdge) {			
					smallestEdge = edgesWeighted[vertices.get(src)][i];
					location = i;
				}
			}
			if(location > numVertices) {
				return null;
			}
			else{
				return indexes.get(location);
			}
		}
	
	
	/**Returns the number of vertices in the graph
	 * @return number of vertices
	 */
	public int getNumV() {
		return numVertices;
	}
	
	/**Tells if the graph is a directed graph or not
	 * @return directed boolean true if directed, false if not
	 */
	public boolean isDirected() {
		return directed;
	}
		
	
	public Iterator<V> edgeIterator(V source) {
		
		return null;
		
	}
	

	public class Edge<E> {
		public V dest;
		public V src;
		public double weight;
		
		public Edge(V src, V dest, double weight) {
			this.dest = dest;
			this.src = src;
			this.weight = weight;
		}
		
		public Edge(V src, V dest) {
			this.src = src;
			this.dest = dest;
			weight = 0;
		}
		
		public V getSource(){
			return this.src;
		}
		
		public V getDest(){
			return this.dest;
		}
		
		public String toString(){
			return "Source: " + src.toString() + ", Destination: " + dest.toString() + ", Weight: " + weight;	
		}	
	}
	
}
