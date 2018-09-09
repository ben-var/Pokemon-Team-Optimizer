package edu.miracosta.cs113;
/**
 * Pokemon.java
 * 		This class contains the constructors, variables, and methods
 * 		needed for Pokemon objects that will be used in the PokeTeam.
 * 
 * 
 */

public class Pokemon  {

	//Data Fields
	private int ID;
	public String name;
	public String[] types = new String[2];
	public char atkStyle;
	public int tier;
	
	
	/** Default constructor
	 * initializes all variables to default */
	public  Pokemon() {
		setName("Missingno.");
		setTypes("null", "null");
		setStyle('z');
		setTier(" ");
		setID(0);	
	}
	
	/** Constructor containing just the name */
	public Pokemon(String name) {
		setID(0);
		setName(name);
		setTypes("null", "null");
		setStyle('z');
		setTier(" ");
		
	}
	
	/** Constructor containing just name and pokemon types */
	public Pokemon(String name, String type1, String type2) {
		setID(0);
		setName(name);
		setTypes(type1, type2);
		setStyle('z');
		setTier(" ");
		
	}
	
	/** Constructor containing just name, pokemon types, and attack styles */
	public Pokemon(String name, String type1, String type2, char style) {
		setID(0);
		setName(name);
		setTypes(type1, type2);
		setStyle(style);
		setTier(" ");
		
	}
	
	/** Constructor containing just name, pokemon types, attack styles, and its tier
	 */
	public  Pokemon(String name, String type1, String type2, char style, String tier) {
		setID(0);
		setName(name);
		setTypes(type1, type2);
		setStyle(style);
		setTier(tier);	
	}
	
	/** Constructor containing just name, pokemon types, attack styles, its tier and ID
	 */
	public Pokemon(int ID, String name, String type1, String type2, char style, String tier) {
		setID(ID);
		setName(name);
		setTypes(type1, type2);
		setStyle(style);
		setTier(tier);
	}
	
	/** Mutator method to set the name
	 * @param name	The name for the pokemon
	 * @return void
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/** Accessor method to get the name
	 * @return name	The name of the pokemon
	 */
	public String getName(){
		return name;
	}
	
	/** Mutator method to set the two types
	 * @param type1	the first type
	 * @param type2 the second type
	 * @return void
	 */
	public void setTypes(String type1, String type2) {
		types[0] = type1;
		types[1] = type2;
		
	}
	
	/** Accessor method to get the types
	 * @return the types toString method
	 */
	public String getTypes() {
		return types[0] + "/" + types[1];
	}
	
	public String[] getTypesArray() {
		return types;
	}
	
	/** Mutator method to set the tier
	 * @param tier The tier of the pokemon
	 * @return void
	 */
	public void setTier(String tier) {
		
		switch(tier){
			case " ":
				this.tier = 0;	
				break;
			case "LC":
				this.tier = 1;
				break;
			case "UU":
				this.tier = 2;
				break;
			case "OU":
				this.tier = 3;
				break;
			case "UBER":
				this.tier = 4;
				break;	
		}
	}
	
	/** Accessor method to get the tier
	 * @return tier	an integer of the tier
	 */
	public int getTier() {
		return tier;
	}
	
	/** Mutator method to set the attack style
	 * @param style	The attack style of the pokemon
	 * @return void
	 */
	public void setStyle(char style) {
		atkStyle = style;
	}
	
	/** Accessor method to get the attack style
	 * @return the attack style
	 */
	public char getStyle() {
		return atkStyle;
	}
	
	/** Mutator method to set the ID
	 * @param ID	the id for the pokemon
	 * @return void
	 */
	public void setID(int ID) {
		this.ID = ID;
	}
	
	/** Accessor method to get the ID
	 * @return ID
	 */
	public int getID() {
		return ID;
	}
	
	public String toString() {
		return "Name: " + name + "\n" + "Type: " + types[0] + ", " +types[1]  + "\n" + "Tier: " + tier + "\n";
	}
}
