package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Deck {
	ArrayList<SpellCard> spellCardList = new ArrayList<SpellCard>();
	ArrayList<ClassCard> classCardList = new ArrayList<ClassCard>();
	ArrayList<Integer> repetition = new ArrayList<Integer>();
	ArrayList<Integer> classRepetition = new ArrayList<Integer>();

	public Deck() {

	}

	public void readFile(String filename, String type) {
		File fileName = new File(filename);
		try {
			BufferedReader fr = new BufferedReader(new FileReader(fileName));
			String sLine;
			while ((sLine = fr.readLine()) != null) {
				String[] split = sLine.split("\t");
				if(type.equals("spell"))
					setSpellCard(split);
				else if(type.equals("class"))
					setClassCard(split);
					
			}
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setSpellCard(String[] split) {

		SpellCard spellCard = new SpellCard();
		spellCard.setName(split[0]);
		spellCard.setUsage(split[1]);
		spellCard.setDescription(split[2]);
		if (split.length == 4) {
			spellCard.setRequirements(split[3]);
		}
		spellCardList.add(spellCard);
	}
	public void setClassCard(String[] split){
		ClassCard classCard = new ClassCard();
		classCard.setName(split[0]);
		classCard.setRequirements(split[1]);
		classCard.setDescription(split[2]);
		classCardList.add(classCard);
	}

	public ArrayList<SpellCard> getSpellCardList() {
		return spellCardList;
	}
	public ArrayList<ClassCard> getClassCardList() {
		return classCardList;
	}

	public ArrayList<Integer> getRepetition() {
		return repetition;
	}
	public ArrayList<Integer> getClassRepetition() {
		return classRepetition;
	}
}
