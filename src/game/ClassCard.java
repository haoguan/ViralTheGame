package game;

/**
 * ClassCard is the template class for the definitions of
 * each class card. This is mainly used for displaying 
 * the information in the PlayerGui.
 * @author Jonathan Wu
 */
public class ClassCard {

	private String name;
	private String description;
	private String requirements;
	
	/**
	 * Default constructor.
	 */
	public ClassCard() {
	}
	
	/**
	 * Three parameter constructor requires the String name of the card,
	 * a String containing the requirements to play, and a String of the
	 * description and effects.
	 * @param name is the name of the card
	 * @param requirements are the requirements in order to activate.
	 * @param description is the details of the effects.
	 */
	public ClassCard(String name, String requirements, String description) {
		this.name = name;
		this.requirements = requirements;
		this.description = description;
	}
	
	/**
	 * setName() sets the name of the card.
	 * @param name is the name of the card
	 */
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 * setDescription() sets the effects of the card.
	 * @param description is the details of the effect.
	 */
	public void setDescription(String description){
		this.description = description; 
	}
	
	/**
	 * setRequirements() sets the new requirements of the card.
	 * @param requirements details the requirements for the card.
	 */
	public void setRequirements(String requirements){
		this.requirements = requirements;
	}
	
	/**
	 * getName() returns the name of the card.
	 * @return the name of the card
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * getDescription() returns the description of the card.
	 * @return the description of the card.
	 */
	public String getDescription(){
		return description;
	}
	
	/**
	 * getRequirements() returns the requirements of the card.
	 * @return the requirements of the card.
	 */
	public String getRequirements(){
		return requirements;
	}
}
