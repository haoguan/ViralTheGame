package game;

public class SpellCard {
	
	private String name;
	private String usage;
	private String description;
	private String requirements = "none";
	
	public SpellCard() {
		
	}
	
	public SpellCard(String name, String usage, String description, String requirements) {
		this.name = name;
		this.usage = usage;
		this.description = description;
		this.requirements = requirements;
	}
	
	public void setName(String name){
		this.name = name;
	}
	public void setDescription(String description){
		this.description = description; 
	}
	public void setUsage(String usage){
		this.usage = usage;
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
	public String getUsage(){
		return usage;
	}
	public String getRequirements(){
		return requirements;
	}
}
