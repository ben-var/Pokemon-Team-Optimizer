package edu.miracosta.cs113;
/**
 * PokeTeam.java
 * 		This class handles holding the pokemon objects,
 * 		adding them to the team, comparing the pokemon, 
 * 		and keeping track of the teams balance from its
 * 		weaknesses and resistances.
 * 
 * Authors: Michael McDermott, Ben Vargas, 
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.HashSet;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
public class PokeTeam {
	
	final int MAX_SIZE = 6; // size of pokemon team can only have 6 pokemon
	public int tier;		// highest tier of pokemon in the team (tier of the team)
	char[] setOfStyles;		// styles in the team
	//int numberOfPlayers;	// how many players are in the team out of 6 //Josh C
	private MatrixGraph<Pokemon, Double> PokeGraph;	//graph used to find the best suggestion for the team
	public ArrayList<Pokemon> allPokemon = new ArrayList<Pokemon>();	//list of all the gen1 pokemon that is narrowed down for the suggestion
	public ArrayList<Pokemon> team = new ArrayList<Pokemon>(MAX_SIZE);	//actual team of 6 pokemon that user creates
	TypeRatioGenerator typeGen = new TypeRatioGenerator();				// used to get ratios of all the types to pick the best type
	
    public Map<Double, String> TeamTypeOptions = new HashMap<Double, String>();
    public Map<String, Double> TeamRatiosByType = new HashMap<String, Double>();
    ArrayDeque<String> typeRanking = suggestType();
	/**Default constructor, loads in the list of all pokmemon, 
	 * does not add any pokemon to the team
	 */
	public PokeTeam() {
		loadPokemonList("PokeData.txt");
		tier = 0;
		// PokeGraph is only created when a suggestion is being made. 

	}
	
	/**Adds a pokemon object to the arraylist of players
	 * pre: the list does not have the pokemon in it
	 * post: the pokemon is added to the list
	 * @param newPlayer the pokemon to be added to the team
	 */
	public void addPlayer(Pokemon newPlayer) {
	
		//has to check to make sure there aren't already 6 players on the team
		if(team.size() < MAX_SIZE) {
			// add the pokemon to the team
			team.add(newPlayer);
			// increment how many players are on the team
			//numberOfPlayers++;
		}
		else {
			// there's already 6 players on the team.
			System.out.println("Team can have at most 6 players.");
		}	
	}
	
	/**Gets the Pokemon in the location of the ArrayList
	 * 
	 * @param location location of the pokemon
	 * @return Pokemon in the specified location of the ArrayList
	 */
	public Pokemon getPlayer(int location) {
		return team.get(location);
	}
	
	/**Gives the arraylist of the team members
	 * 
	 * @return the arraylist of pokemon in the team
	 */
	public ArrayList <Pokemon> getTeam() {
		return team;
	}
	
	/**calls private helper methods to look at the 
	 * current team, determine the best tier,
	 * style, and type needed, then narrows down
	 * a set of pokemon objects. A graph is generated
	 * from that set using different weights
	 * TODO: FIGURE OUT HOW TO HANDLE THE TEAM HAVING NO POKEMON IN IT AND MAKING A SUGGESTION
	 * 		THE GRAPH NEEDS TO HAVE ONE POKEMON ALREADY IN THE TEAM TO ADD MORE NODES TO THE GRAPH
	 * 		AND TO PUT EDGES WITH WEIGHTS BETWEEN THEM
	 * 
	 * pre: uber tier needs to be patched if any other uber pokemon are added
	 */
	public Pokemon makeSuggestion(){
		Pokemon suggestion;
		// if there are no pokemon in the team at all, a default first suggestion is made(zapdos)
		if(team.isEmpty()) {
		suggestion = allPokemon.get(145);
		}
		else {
			Queue<Pokemon> candidates = new ArrayDeque<Pokemon>();
			char styleNeeded = calcAtkStyle();
			int tier = calcTier();
			ArrayDeque<String> suggestedTypes = suggestType();
			
			Pokemon dummyPokemon = new Pokemon();	
			
			boolean resetAlready = false;
			
			while(candidates.isEmpty()) {
				
				// if all types left have been exhausted previously used types become allowed.
				if(suggestedTypes.isEmpty()) {
					suggestedTypes = typeGen.getDefaultBestDefTypes();
					
					//will lower the tier if all types have been checked already.
					if (resetAlready) {
						tier--;
						resetAlready = false;
					} else {
						resetAlready = true;
					}
				}
				
				//loop through list of pokemon to find eligible ones for set
				for(Pokemon candidate: allPokemon) {
					//checks the pokemons details to see if eligible (if the pokemon has the style, attack type, tier, and type)
					String[] candidateTypes = candidate.getTypesArray();			
					
					if( (candidate.getTier() == tier) && 
							(candidate.getStyle() == styleNeeded) && 
							(!this.hasPokemon(candidate.getID())) &&
							(suggestedTypes.peek().equalsIgnoreCase(candidateTypes[0]) || 
							  suggestedTypes.peek().equalsIgnoreCase(candidateTypes[1]) ) ) {
							//add the pokemon candidate to the queue of candidates	
							candidates.add(candidate);
					}
				}
				
				suggestedTypes.poll(); // pops the type off the ArrayDeque
			}
			
			// Polls a candidate from the queue, generates the weight for it, then adds it to the graph
			// with the edge between the candidate and the first team member
			// add dummy pokemon before adding all the vertices and edges
			PokeGraph = new MatrixGraph<Pokemon, Double>(candidates.size()+1,false);
			PokeGraph.addVertex(dummyPokemon);	
			while(!candidates.isEmpty()) {
				Pokemon possibleTeamMember = candidates.poll();
				//generate the weight for possible team member and first team member
				double weight = generateWeight(possibleTeamMember);
				// add the possible team member to the graph and add an edge between them	
				PokeGraph.addVertex(possibleTeamMember);
				PokeGraph.addEdge(dummyPokemon, possibleTeamMember, weight);
			}
			/**
			 * after graph is made, call the graphs GetSmallestEdge(Pokemon source)
			 * which should return a pokemon, 
			 */
			 suggestion = PokeGraph.getSmallestEdge(dummyPokemon);
			}
		
	System.out.println("Pokemon Suggested: " + suggestion.getName());
	return suggestion;
	}
	
	/**
	 * hasPokemon
	 * <p>
	 * Looks for a pokemon in a poketeam using ID.
	 * @param id
	 * 		- int ID number of a pokemon to search for
	 * @return
	 * 		- true if pokemon is in team, false otherwise.
	 */
	private boolean hasPokemon(int id) {
		for (Pokemon p : team) {
			if (p.getID() == id) {
				return true;
			}
		}
		return false;
	}
	
	
	/** 
	* method used to suggest a type based on the current team
	*
	* Returns an ArrayDeque that has a FIFO order of best types to add
	*
	* TODO to write up formal documentation for this method.
	*/
	private ArrayDeque<String> suggestType(){
		if(team.size() == 0) {
			return typeGen.getDefaultBestDefTypes();
		}
		ArrayDeque<String> suggestionQueue;
		
		HashSet<String> existingTypeSet = this.getExistingTypes();	
		
		ArrayList<String> existingTypeList = new ArrayList<String>();
		for (String s : existingTypeSet) {
			existingTypeList.add(s);
		}
		
		suggestionQueue  = typeGen.getTypeSuggestionQueue(existingTypeList);
		return suggestionQueue;
	}
	
	
	/**looks at a pokemon and the team and generates a weight for the new pokemon 
	 * The smaller the weight, the better of a pair they are.
	 * Weight is generated by looking at their types and how balanced 
	 * 
	 * TODO: need to figure out how to actually determine the values for each characteristic
	 * 		of a pokemon compared to the team.
	 * 
	 * @param subject, the pokemon being compared to the team
	 * @return weight 	the weight for that pokemon
	 */
	private double generateWeight(Pokemon subject){
		// weight starts at infinity	
		double weight = Double.POSITIVE_INFINITY;
		//need to get both types of the pokemon
		// compare both types and find a numerical value to add for the two types
		String[] subjectTypes = subject.getTypesArray();
		
		double value = 1;
		while(!typeRanking.isEmpty()){
			TeamRatiosByType.put(typeRanking.poll(), value);
			value++;
		}	
		TeamRatiosByType.put("null", 0.0);
		weight = TeamRatiosByType.get(subjectTypes[0]) + TeamRatiosByType.get(subjectTypes[1]);
		return weight;
	}
	
	/**
	 * 
	 * @return
	 */
	public HashSet<String> getExistingTypes() {
		HashSet<String> existingTypes = new HashSet<String>();
		String[] types;
		
		for (Pokemon p : team) {
			types = p.getTypesArray();
			existingTypes.add(types[0]);
			
			if (types[1] != "null") {
				existingTypes.add(types[1]);
			}
		}
		return existingTypes;
	}
	
	/**Outputs the list of all 151 pokemon and their data
	 */
	public void displayAllPokemonData(){
		for(Pokemon pokemon : allPokemon) {
			System.out.println(pokemon.toString());
		}
	}
	
	/**Removes a player from the team at the given index
	 * @param index	the location of the pokemon to be removed
	 */
	public void removePlayer(int index) {
		team.remove(index);	
	}
	
	/**Goes through an external file and reads in all the data
	 * to create new pokemon objects and add them to an existing 
	 * arraylist
	 * @param file	the file specified by program to read in
	 */
	private void loadPokemonList(String file) {
		int id;
		String name;
		String type1;
		String type2;
		char atkStyle;
		String tier;	
		int arLocation = 0;
		try{
			Scanner pokemonFile = new Scanner(new FileInputStream(file));
			while(pokemonFile.hasNext()) {
			 id = pokemonFile.nextInt();
			 name = pokemonFile.next();
			 type1 = pokemonFile.next();
			 type2 = pokemonFile.next();
			 atkStyle = pokemonFile.next().charAt(0);
			 tier = pokemonFile.next();	 
			Pokemon newPokemon = new Pokemon(id, name, type1, type2, atkStyle, tier);
			allPokemon.add(arLocation, newPokemon);
			arLocation++;
			}
			pokemonFile.close();
		}
		catch(FileNotFoundException e) {
        	System.err.println("PokeData file was not found");
        	throw new IllegalStateException();
        } catch (NoSuchElementException e) {
        	System.err.println(e.getMessage());
        	System.err.println(e.getClass());
        	// need to determine what is causing this exception to be thrown
        }
	}
	
	/**Iterates over the ArrayList of team
	 * and compares each pokemon objects tier
	 * and returns the largest tier integer
	 * @return highestTier the largest tier found
	 * in the team of pokemon
	 */
	private int calcTier() {
		int highestTier = 1; 
		if(team.isEmpty()) {
			highestTier = 4;
		}	
		else {
			for(Pokemon element : team) {
			
			if (element.getTier() > highestTier)
				highestTier = element.getTier();
			}
		}
		return highestTier;
	}

	/**Iterates over the ArrayList of team
     * and compares each pokemon objects attack style
     * and returns the suggested attack style
     * @return atkStyleNeeded
     * in the team of pokemon
     */
    private char calcAtkStyle() {
        char atkStyleNeeded;
       int a = 0;
       int d = 0;
       int s = 0;
       int smallestNum = 100;
       	for(Pokemon element: team) {
        	
            if(element.getStyle() == 'A'){
            	a++;
            }
            else if(element.getStyle() == 'D') {
               d++;
            }
            else if(element.getStyle() == 'S') {
               s++;
            }
        }
       	if(a < smallestNum) {
       		smallestNum = a;
       	}
       	if(d < smallestNum) {
       		smallestNum = d;
       	}
       	if(s < smallestNum) {
       		smallestNum = s;
       	}
       	if(smallestNum == a) {
       		atkStyleNeeded = 'A';
       	}
       	else if(smallestNum == d) {
       		atkStyleNeeded = 'D';
       	}
       	else {
       		atkStyleNeeded = 'S';
       	}
        
        return atkStyleNeeded;
    }
    
    /**Loops and adds pokemon to the ArrayList team
     * until the team has reached its max size
     * 
     * @return team the ArrayList of the pokemon in the team
     */
    public ArrayList<Pokemon> fillTeam(){
    	Pokemon sugg;
    	while(team.size() < MAX_SIZE) {
    		sugg = makeSuggestion();
    		team.add(sugg);
    	}
    	return team;
    }
}
