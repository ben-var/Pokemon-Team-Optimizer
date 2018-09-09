package edu.miracosta.cs113;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.List;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;

/**
 * TypeRatioGenerator
 * <p>
 * This class holds type data, according to the following chart: https://pokemondb.net/type
 * <p>
 * This class is used in order to drive the recommendation engine. The primary function
 * of the 
 * 
 * Post-Condition: There must be a file titled "pokeTypeWeaknessChart.txt"
 * that stores doubles in rows and columns according to the source matrix
 * from the link >> https://pokemondb.net/type  
 * The file must have the double values delimited by a single space, and a 
 * line break when a new row begins. There should be no headers and no other
 * punctuation other than the punctuation in the double values. There should
 * be a total of 18 rows and 18 columns, forming a 18x18 matrix.
 */
public class TypeRatioGenerator {
	
	/** Pokemon TYPE integer constants - correspond to columns and rows in matrix */
    public final int NORMAL = 0;
    public final int FIRE = 1;
    public final int WATER = 2;
    public final int ELECTRIC = 3;
    public final int GRASS = 4;
    public final int ICE = 5;
    public final int FIGHTING = 6;
    public final int POISON = 7;
    public final int GROUND = 8;
    public final int FLYING = 9;
    public final int PSYCHIC = 10;
    public final int BUG = 11;
    public final int ROCK = 12;
    public final int GHOST = 13;
    public final int DRAGON = 14;
    public final int DARK = 15;
    public final int STEEL = 16;
    public final int FAIRY = 17;

    // if FAIRY type is added, set this number to 18. 
    private final int NUM_OF_TYPES = 18;
    
    // weaknessRatings and attackRatings store average type advantage per type (in int form)
    private Map<Integer, Double> weaknessRatings = new HashMap<Integer, Double>();
    private Map<Integer, Double> attackRatings = new HashMap<Integer, Double>();
    
    // this table is the primary data structure for the TypeRatioGenerator, it holds all the source data
    private Map<ArrayList<Integer>, Double> masterMatrix = new HashMap<ArrayList<Integer>, Double>();

    /** Simple Tester for the TypeRatioGenerator class. */
    public static void main(String[] args) { 
    	TypeRatioGenerator test = new TypeRatioGenerator();
    	
    	// refer to link at class header to verify ATK ratios are correct for given types (row)
    	List<Double> normalRatiosATK = test.getAttackRatiosForType(test.NORMAL);
    	List<Double> fireRatiosATK = test.getAttackRatiosForType(test.FIRE);
    	List<Double> waterRatiosATK = test.getAttackRatiosForType(test.WATER);
    	
    	// refer to link at class header to verify DEF ratios are correct for given types (column)
    	List<Double> normalRatiosDEF = test.getDefenseRatiosForType(test.NORMAL);
    	List<Double> fireRatiosDEF = test.getDefenseRatiosForType(test.FIRE);
    	List<Double> waterRatiosDEF = test.getDefenseRatiosForType(test.WATER);
    	
    	// testing results of getAttackRatiosForType
    	System.out.println("Printing Normal, Fire, and Water ATTACK Ratios.");
    	System.out.println(normalRatiosATK);
    	System.out.println(fireRatiosATK);
    	System.out.println(waterRatiosATK);
    	
    	// testing results of getDefenseRatiosForType
    	System.out.println("\nPrinting Normal, Fire, and Water DEFENSE Ratios.");
    	System.out.println(normalRatiosDEF);
    	System.out.println(fireRatiosDEF);
    	System.out.println(waterRatiosDEF);
    	
    	// testing results of getBestDefenseType
    	System.out.println("\nBest defense type is: " + test.getBestDefenseType());
    	
    	/**
    	 * When testing the default SuggestionQueue of best types currently, 
    	 * there is a bug in the timing for when System.err displays 
    	 * the Type:Ratio listing. 
    	 * 
    	 * The first System.err within test statements
    	 * below is for the default defensive types, result will be all the 
    	 * types from least to greatest, to verify the correct ordering of final output.
    	 */
    	System.out.println("\nDefault order of best defensive types:");
    	ArrayDeque<String> testDefault = test.getDefaultBestDefTypes();
    	System.out.println(testDefault);
    	
    	// testing the SuggestionQueue method with a limited number of types
    	ArrayList<String> testWithSomeTypes = new ArrayList<String>();
    	testWithSomeTypes.add("Fire");
    	testWithSomeTypes.add("Water");
    	testWithSomeTypes.add("Steel");
    	testWithSomeTypes.add("Fairy");
    	testWithSomeTypes.add("Ghost");
    	testWithSomeTypes.add("Electric");
    	testWithSomeTypes.add("Poison");
    	
    	// printing TypeSuggestionQueue result with type exclusions passed.
    	System.out.println("\nOrder of best defensive types with exceptions passed as parameters:");
    	ArrayDeque<String> testWithArgs = test.getTypeSuggestionQueue(testWithSomeTypes);
    	System.out.println(testWithArgs);
    }
    
    /**
     * Default Constructor for TypeRatioGenerator object. This loads the data structure
     * instance variables stored in the TypeRatioGenerator object.
     * 
     * Precondition: This object MUST be accompanied by the poke-type text file, with 
     * requirements stipulated in the header of the class. 
     */
    public TypeRatioGenerator() {
    	// coordinates of master matrix, 18 x 18 = 324 unique coordinates
        ArrayList<Integer> coordinates;
        // ratio at a given coordinate
        double ratio;
        
        // instance variables for file IO.
        String line;
        String[] lineTokens = new String[18];
        try {
        	Scanner typeFile = new Scanner(new FileInputStream("pokeTypeWeaknessChart.txt"));
        	
            for (int i = 0; typeFile.hasNextLine(); i++) {
            	line = typeFile.nextLine();
            	lineTokens = line.split(" ");
            	
            	coordinates = new ArrayList<Integer>();
            	coordinates.add(i);
            	
            	// loading coordinates and corresponding doubles into matrix.
            	for (int j = 0; j < lineTokens.length; j++) {
            		coordinates.add(j);
            		ratio = Double.parseDouble(lineTokens[j]);
            		masterMatrix.put(coordinates, ratio);
            		coordinates = new ArrayList<Integer>();
            		coordinates.add(i);
            	}
            }
            typeFile.close();
            
            // filling ratio data for each type. higher ATK is good, lower DEF is good.
            ArrayList<Double> resistanceRatios;
            ArrayList<Double> atkRatios;
            for (int i = 0; i < this.getNumOfTypes(); i++) {
            	resistanceRatios = this.getDefenseRatiosForType(i);
            	atkRatios = this.getAttackRatiosForType(i);
            	weaknessRatings.put(i, this.getAvgRatio(resistanceRatios)); 
            	attackRatings.put(i, this.getAvgRatio(atkRatios));
            }
        } catch (FileNotFoundException e) {
        	System.err.println("typeChart file was not found");
        	throw new IllegalStateException();
        }
    }
    
    /**
     * getNumOfTypes
     * <p>
     * Gets the number of types the program is set to process.
     * @return 
     * 		Number of types the class is set to process
     */
    public int getNumOfTypes() {
    	return NUM_OF_TYPES;
    }
    
    /**
     * getDefenseRating 
     * <p>
     * Returns the average resistance ratio for a given type
     * @param type
     * 		- Type in integer format
     * @return 
     * 		the weakness ratings for a given type
     */
    public double getDefenseRating(int type) {
    	return weaknessRatings.get(type);
    }
    
    /**
     * getAttackRating
     * <p>
     * Returns the average attack ratio for a given type
     * @param type
     * 		- Type in integer format
     * @return
     * 		the attack ratio for a given type
     */
    public double getAttackRating(int type) {
    	return attackRatings.get(type);
    }
    
    /**
     * getAvgRatio
     * <p>
     * Calculates the average ratio for a given list of ratios.
     * @param ratios
     * 		- list of ratios (can be either attack or defense)
     * @return
     * 		the average ratio represented as a double
     */
    public double getAvgRatio(ArrayList<Double> ratios) {
    	double sum = 0.0;
    	for(Double ratio : ratios) {
    		sum += ratio;
    	}
    	double average = sum / ratios.size();
    	return average;
    }
    
    /**
     * getDefaultBestDefTypes
     * <p>
     * This method will provide an ordered Deque that can be used to poll
     * best types for a PokeTeam. It uses a variety of methods in order to 
     * calculate an overall type advantage ratio. The first elements of the 
     * Queue are the BEST additions to the team, whereas the last elements
     * of the Queue are the WORST additions to the team.
     * <p>
     * Precondition: This method should only be used with an assumption that
     * no other types currently exists in a poketeam. If the team already has
     * types of pokemon in their PokeTeam, use the getTypeSuggestionQueue(ArrayList<String>)
     * method with a list of all types currently in your PokeTeam of interest.
     *  
     * @return
     * 		An ArrayDeque Data structure that is ordered from best type to worst type
     * 		in terms of type resistances.
     */
    public ArrayDeque<String> getDefaultBestDefTypes() {
    	// adding all types to the list
    	ArrayList<String> allTypes = new ArrayList<String>();
    	for (int i = 0; i < this.NUM_OF_TYPES; i++) {
    		allTypes.add(this.getTypeString(i));
    	}
    	// null is passed to represent no type exceptions
    	ArrayDeque<String> defaultSuggest = this.getTypeSuggestionQueue(null);
    	return defaultSuggest;
    }
    
    /**
     * getTypeSuggestionQueue
     * <p>
     * Uses several helper methods in order to provide an ordered
     * queue of types. The first elements in the queue will be the
     * BEST types to add to the team based on DEF ratios. The end 
     * of the queue will conversely be the WORST.
     * @param types
     * 		- types already existing in a given team
     * @return
     * 		a ORDERED Queue (i.e., Deque) of types that has best -> worst order
     */
    public ArrayDeque<String> getTypeSuggestionQueue(ArrayList<String> types) {
    	ArrayList<String> typesLeft = new ArrayList<String>();
    	for (int i = 0; i < this.NUM_OF_TYPES; i++) {
    		typesLeft.add(this.getTypeString(i));
    	}
    	// removing types already in party - null signals none to remove
    	if (types != null) {
	    	for (String type : types) {
	    		typesLeft.remove(type);
	    	}
    	}
    	
    	HashMap<String, Double> ratios = this.createDefRatioTable(typesLeft);
    	ArrayList<TypeEntry> typeRatioArray = this.getTypeRatioList(ratios);
    	ArrayDeque<String> suggestionQueue = this.sortEntriesToQueue(typeRatioArray);
    	
    	return suggestionQueue;
    }
    
    //type --> Ratio table for defense values
    /**
     * createDefRatioTable
     * <p>
     * Creates a mapping between types and their average DEF ratio.
     * @param types
     * 		- types KEYS used to generate the map
     * @return
     * 		a hashtable of String keys and Double values, representing a type:ratio relationship
     */
    private HashMap<String, Double> createDefRatioTable(ArrayList<String> types) {
    	HashMap<String, Double> ratios = new HashMap<String, Double>();
    	
    	// if no types passed
    	if (types == null) {
    		return null;
    	}
    	
    	for (String type : types) {
    		ratios.put(type, this.getDefenseRating(this.getTypeInt(type)));
    	}
    	return ratios;
    }
    
    
    /**
     * getTypeRatioList
     * <p>
     * Helper method that returns a list of TypeEntry objects that can then be sorted.
     * @param ratioTable
     * 		- hashtable of types and corresponding ratios to be sorted.
     * @return
     * 		List of TypeEntry objects that can be sorted
     */
	private ArrayList<TypeEntry> getTypeRatioList(HashMap<String, Double> ratioTable) {
    	Set<Map.Entry<String, Double>> typeSet = ratioTable.entrySet();	
    	ArrayList<TypeEntry> typeRatioList = new ArrayList<TypeEntry>();
    	
    	for (Map.Entry<String, Double> entry : typeSet) {
    		typeRatioList.add(new TypeEntry(entry.getKey(), entry.getValue()));
    	}
    	return typeRatioList;
    }
    
    /**
     * sortEntriesToQueue
     * <p>
     * Returns Queue that has FIFO order of best types. This method is a helper method 
     * to break the suggestion algorithm into parts.
     * <p>
     * @param typeRatioArray
     * 		Input an array of TypeEntry objects that has a type, and its associated defense ratio
     * @return
     * 		A sorted Queue of types from best to worst that can be used for suggestion purposes.
     */
    @SuppressWarnings("unchecked")
	private ArrayDeque<String> sortEntriesToQueue(ArrayList<TypeEntry> typeRatioList) {
    	// sort the arrayList from Best -> Worst type (lowest weakness ratio --> highest weakness ratio)
    	Collections.sort(typeRatioList);
    	
    	// used for testing purposes to see if the sort is working correctly
    	// System.err.println(typeRatioList);
    	
    	ArrayDeque<String> typeQueue = new ArrayDeque<String>();
    	for (TypeEntry e : typeRatioList) {
    		typeQueue.offer(e.getType());
    	}
    	return typeQueue;
    }
    
    /**
     * getDefenseRatiosForType
     * <p>
     * This method will take a type as a parameter, and will return the defense
     * (i.e., the resistance ratio [lower the better]). Primary algorithm
     * is in the getDefenseRatiosForType(int typeConstant) method.
     * @param type
     * 		- a Pokemon type in String format
     * @return
     * 		an ArrayList of all the resistance ratios of a given type against the other types
     */
    public ArrayList<Double> getDefenseRatiosForType(String type) {
    	int typeInt = this.getTypeInt(type);
    	return getDefenseRatiosForType(typeInt);
    }
    
    /**
     * getDefenseRatiosForType(int parameter)
     * <p>
     * This method will take a type as a parameter, and will return the defense
     * (i.e., the resistance ratio [lower the better]). Invalid type integers
     * will result in an exception being thrown.
     * @param typeConstant
     * 		- a Pokemon type in integer format (String method also exists as helper)
     * @return
     * 		- an ArrayList of all the resistance ratios of a given type against the other types
     * @throws IllegalArgumentException
     * 		- If an invalid type is input as a parameter
     */
    public ArrayList<Double> getDefenseRatiosForType(int typeConstant) throws IllegalArgumentException {
    	// if invalid type
        if (typeConstant < 0 || typeConstant > 17) {
        	throw new IllegalArgumentException();
        }
        
        List<Integer> reference = new ArrayList<Integer>();
        ArrayList<Double> typeRatios = new ArrayList<Double>();
        
        for (int i = 0; i < NUM_OF_TYPES; i++) {
        	if (i == 0) {
        		//dummy value to be replaced by loop to avoid NullPointer on first iteration
        		reference.add(null); 
                reference.add(typeConstant);
        	}
        	reference.set(0, i);
        	typeRatios.add(masterMatrix.get(reference));
        }
        return typeRatios;
    }
    
    /**
     * getAttackRatiosForType
     * <p>
     * This method will take a type as a parameter, and will return the attack
     * (i.e., the offense ratio [higher the better]). Primary algorithm
     * is in the getAttackRatiosForType(int typeConstant) method.
     * @param type
     * 		- a Pokemon type in String format
     * @return
     * 		an ArrayList of all the attack ratios of a given type against the other types
     */
    public ArrayList<Double> getAttackRatiosForType(String type) {
    	int typeInt = this.getTypeInt(type);
    	return getAttackRatiosForType(typeInt);
    }
    
    /**
     * getAttackRatiosForType(int parameter)
     * <p>
     * This method will take a type as a parameter, and will return the attack
     * (the higher the better]). Invalid type integers will result in an exception being thrown.
     * @param typeConstant
     * 		- a Pokemon type in integer format (String method also exists as helper)
     * @return
     * 		- an ArrayList of all the resistance ratios of a given type against the other types
     * @throws IllegalArgumentException
     * 		If an invalid type is input as a parameter
     */
    public ArrayList<Double> getAttackRatiosForType(int typeConstant) {
        // if invalid type
        if (typeConstant < 0 || typeConstant > 17) {
        	throw new IllegalArgumentException();
        }
        
        List<Integer> reference = new ArrayList<Integer>();
        ArrayList<Double> typeRatios = new ArrayList<Double>();
        
        for (int i = 0; i < NUM_OF_TYPES; i++) {
        	if (i == 0) {
        		//dummy value to be replaced by loop to avoid NullPointer on first iteration
        		reference.add(typeConstant);
        		reference.add(null); 
        	}
        	reference.set(1, i);
        	typeRatios.add(masterMatrix.get(reference));
        }
        return typeRatios;
    }
    
    
    /**
     * getBestDefenseType
     * <p>
     * Method that will return the best defense type based off the type
     * resistance ratios. This is a flexible method that will work no matter
     * what types are allowed into the program (if different pokemon generations
     * would like to be implemented.
     * @return
     * 		a TypeEntry object that contains the best type and its corresponding resistance ratio.
     */
    public TypeEntry getBestDefenseType() {
    	ArrayList<Double> defAvgRatio = new ArrayList<Double>();
    	ArrayList<Double> defRatioForSingleType;
    	for(int i = 0; i < this.getNumOfTypes(); i++) {
    		defRatioForSingleType = this.getDefenseRatiosForType(i);
    		defAvgRatio.add(this.getAvgRatio(defRatioForSingleType));
    	}
    	// it is possible to have a tie for best type, in which case the first occurence is stored
    	Double min = Collections.min(defAvgRatio);
    	String bestType = this.getTypeString(defAvgRatio.indexOf(min));
    	
    	TypeEntry bestTypeEntry = new TypeEntry(bestType, min);
    	
    	return bestTypeEntry;
    }
    
    /**
     * getTypeString
     * <p>
     * Simple method to convert an integer value to its corresponding String type.
     * @param typeInt
     * 		- type in integer notation that needs to be converted.
     * @return
     * 		a String representation of the integer passed to the method.
     * @throws IllegalArgumentException
     * 		if an invalid integer is passed to the method.
     */
    public String getTypeString(int typeInt) throws IllegalArgumentException {
    	
    	String typeString;
    	
    	switch(typeInt) {
	        case(0):
	        	typeString = "Normal";
	            break;
	        case(1):
	        	typeString = "Fire";
	            break;
	        case(2):
	        	typeString = "Water";
	            break;
	        case(3):
	        	typeString = "Electric";
	            break;
	        case(4):
	        	typeString = "Grass";
	            break;
	        case(5):
	        	typeString = "Ice";
	            break;
	        case(6):
	        	typeString = "Fighting";
	            break;
	        case(7):
	        	typeString = "Poison";
	            break;
	        case(8):
	        	typeString = "Ground";
	            break;
	        case(9):
	        	typeString = "Flying";
	            break;
	        case(10):
	        	typeString = "Psychic";
	            break;
	        case(11):
	        	typeString = "Bug";
	            break;
	        case(12):
	        	typeString = "Rock";
	            break;
	        case(13):
	        	typeString = "Ghost";
	            break;
	        case(14):
	        	typeString = "Dragon";
	            break;
	        case(15):
	            typeString = "Dark";
	            break;
	        case(16):
	            typeString = "Steel";
	            break;
	        case(17):
	        	typeString = "Fairy";
	        	break;
	        default:
	            typeString = "NOT A VALID TYPE";
	            throw new IllegalArgumentException();
    	}
    	return typeString;
    }

    /**
     * getTypeInt
     * <p>
     * Simple method to retrieve a int notation of a type from its String notation.
     * @param type
     * 		- type in String notation
     * @return
     * 		- integer value of type
     * @throws IllegalArgumentException
     * 		if an invalid String type is passed as a parameter
     */
    public int getTypeInt(String type) throws IllegalArgumentException {
        // removing case sensitivity
        type = type.toLowerCase();
        // removing all spaces, numbers, and special characters from the input string
        type = type.replaceAll("\\P{L}", "");

        int typeInt;

        switch(type) {
            case("normal"):
                typeInt = NORMAL;
                break;
            case("fire"):
                typeInt = FIRE;
                break;
            case("water"):
                typeInt = WATER;
                break;
            case("electric"):
                typeInt = ELECTRIC;
                break;
            case("grass"):
                typeInt = GRASS;
                break;
            case("ice"):
                typeInt = ICE;
                break;
            case("fighting"):
                typeInt = FIGHTING;
                break;
            case("poison"):
                typeInt = POISON;
                break;
            case("ground"):
                typeInt = GROUND;
                break;
            case("flying"):
                typeInt = FLYING;
                break;
            case("psychic"):
                typeInt = PSYCHIC;
                break;
            case("bug"):
                typeInt = BUG;
                break;
            case("rock"):
                typeInt = ROCK;
                break;
            case("ghost"):
                typeInt = GHOST;
                break;
            case("dragon"):
                typeInt = DRAGON;
                break;
            case("dark"):
                typeInt = DARK;
                break;
            case("steel"):
                typeInt = STEEL;
                break;
            case("fairy"):
            	typeInt = FAIRY;
            	break;
            default:
                typeInt = -1;
                throw new IllegalArgumentException();
        }

        return typeInt;
    }
    
    /**
     * TypeEntry
     * <p>
     * Inner class that is used to avoid Java generic type compiler errors.
     * <p>
     * The class is meant to be a simple Entry object storing two variables,
     * String representing a pokemon type, and its corresponding DEF ratio.
     */
    @SuppressWarnings("rawtypes") // for Comparable
	private class TypeEntry implements Comparable {
    	
    	/** Instance Variable */
    	private String type;
    	private double ratio;
    	
    	/**
    	 * toString
    	 * <p>
    	 * Standard toString method that prints a formatted ratio.
    	 */
    	public String toString() {
    		String entry = String.format("(Type: %s, Ratio: %.2f)", type, ratio);
    		return entry;
    	}
    	
    	/**
    	 * Constructor for TypeEntry object.
    	 * <p>
    	 * Precondition: no default constructor should be used.
    	 * @param inType
    	 * 		- type in String notation
    	 * @param inRatio
    	 * 		- average DEF ratio in double notation
    	 */
    	public TypeEntry(String inType, double inRatio) {
    		this.setType(inType);
    		this.setRatio(inRatio);
    	}
    	
    	/**
    	 * compareTo
    	 * <p>
    	 * The compareTo method for a TypeEntry object
    	 * treats the double value as the comparable value.
    	 * <p>
    	 * This is used in order to rank types from best to worst.
    	 * @param obj
    	 * 		- other TypeEntry to compare with
    	 * @return
    	 * 		- 1 if this ratio is bigger, 0 if equal, -1 if this ratio is smaller
    	 */
    	@Override
    	public int compareTo(Object obj) {
    		if (!(obj instanceof TypeEntry)) {
    			throw new IllegalStateException("Comparing invalid type");
    		}
    		
    		TypeEntry other = (TypeEntry) obj;
    		
    		if (other.getRatio() > this.ratio) {
    			return -1;
    		} else if (other.getRatio() == this.ratio) {
    			return 0;
    		} else {
    			return 1;
    		}
    		
    	}
    	
    	/** Getter for ratio */
		public double getRatio() {
			return ratio;
		}
		
		/** Setter for ratio @param ratio - ratio to overwrite with */
		public void setRatio(double ratio) {
			this.ratio = ratio;
		}

		/** Getter for type */
		public String getType() {
			return type;
		}
		
		/** Getter for type @param type - type to overwrite with */
		public void setType(String type) {
			this.type = type;
		}
		
		/**
		 * equals
		 * <p>
		 * Standard equals method.
		 * @param obj
		 * 		- other object to compare this object with
		 * @return
		 * 		- true if other is the same object as this one, otherwise false.
		 */
		@Override
		public boolean equals(Object other) {
			if (!(other instanceof TypeEntry)) {
				return false;
			}
			
			TypeEntry otherEntry = (TypeEntry) other;
			
			return otherEntry.getRatio() == this.ratio
					&& otherEntry.getType().equals(this.type);
		}
    }
}
