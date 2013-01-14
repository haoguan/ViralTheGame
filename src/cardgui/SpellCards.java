package cardgui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class SpellCards {
	private String name;
	private String description;
	private String usage;
	private String requirements = "none";
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
