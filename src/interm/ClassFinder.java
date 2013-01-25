package interm;

import game.DataLayer;
import game.PlayerGui;

import java.util.ArrayList;
import java.util.HashMap;

import main.GamePlayState;
import main.GamePlayState.STATES;
import ClassCards.ActivateClass;
import ClassCards.Burner;
import ClassCards.ChanceMaker;
import ClassCards.CoreTransporter;
import ClassCards.Monster;
import ClassCards.Procreator;
import ClassCards.StreamTraveler;
import ClassCards.Theif;

/**
 * ClassFinder manages the storage and execution of the class spells.
 * @author Jonathan Wu
 */
public class ClassFinder {
	HashMap<String, ActivateClass> cards = new HashMap<String, ActivateClass>();
	GamePlayState gps;
	DataLayer dlayer;
	
	/**
	 * This constructor takes in a DataLayer and GamePlayState instance.
	 * @param dlayer is the shared data layer.
	 * @param gps is the main GamePlayState instance.
	 */
	public ClassFinder(DataLayer dlayer, GamePlayState gps){
		this.dlayer = dlayer;
		this.gps = gps;
//		activateCard.put(key, value)
		storeAllCards();
	}
	
	/**
	 * doEffect() will find activated card and implement effects under the context of the current player.
	 * @param card is the name of the card activated by current player.
	 * @param playergui is the current player's gui.
	 */
	public boolean doEffect(String card, SpellFinder sf, PlayerGui playergui) {
		ActivateClass classCard = cards.get(card);
		classCard.setSpellFinder(sf);
		classCard.setDlayer(dlayer);
		classCard.setGamePlayState(gps);
		classCard.setPlayerGui(playergui);
		classCard.setPlayer(playergui.getPlayer());
		classCard.runEffect();
		return true;
	}
	
	/**
	 * storeAllCards() stores all the class cards inside the HashMap for later access. In addition,
	 * the states where the class cards effects can be activated are stored in the Playable class
	 * for each class card's instance. 
	 */
	public void storeAllCards() {
		
		//Movement Restriction.
		cards.put("Stream Traveler", new StreamTraveler());
		cards.put("Core Transporter", new CoreTransporter());
		cards.put("Burner", new Burner());
		cards.put("Procreator", new Procreator());
		cards.put("Chance Maker", new ChanceMaker());
		cards.put("Theif", new Theif());
		cards.put("Monster", new Monster());
		
		setStates("Stream Traveler");
		setStates("Theif", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		setStates("Core Transporter", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		setStates("Burner", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		setStates("Monster", GamePlayState.STATES.ACTIONS_STATE);
		setStates("Procreator");
		setStates("Chance Maker");
	}

	public HashMap<String, ActivateClass> getCards() {
		return cards;
	}
	
	/**
	 * setStates() stores the playable states for each class card initialized in the HashMap.
	 * @param name is the name of the class card.
	 * @param statesToAdd is the variable number of STATES.
	 */
	public void setStates(String name, STATES...statesToAdd) {	
		ArrayList<STATES> playable = new ArrayList<STATES>();
		for (int i = 0; i < statesToAdd.length; i++) {
			playable.add(statesToAdd[i]);
		}
		cards.get(name).setPlayableState(playable);
	}

	
}
