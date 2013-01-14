package game;

public class ClassCard {

	private String name;
	private String description;
	private String requirements;
	
	public ClassCard() {
		
	}
	
	public ClassCard(String name, String requirements, String description) {
		
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setDescription(String description){
		this.description = description; 
	}
	
	public void setRequirements(String requirements){
		this.requirements = requirements;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public String getRequirements(){
		return requirements;
	}
}
