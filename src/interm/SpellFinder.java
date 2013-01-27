package interm;
import game.DataLayer;
import game.Player;
import game.PlayerGui;

import java.util.ArrayList;
import java.util.HashMap;

import main.GamePlayState;
import main.GamePlayState.STATES;
import SpellCards.*;

/**
 * SpellFinder is responsible for finding the correct spell card that needs to be played
 * and implementing the effect with the functionality of a grace period for counters.
 * @author Hao Guan and Jonathan Wu
 *
 */
public class SpellFinder {
	HashMap<String, ActivateSpell> cards = new HashMap<String, ActivateSpell>();

	GamePlayState gps;
	DataLayer dlayer;
	boolean immune = true;
	boolean period = true;
	boolean run = true;
	boolean gracePeriod = false;
	GracePeriod gp;

	/**
	 * The two parameter constructor that takes in a DataLayer and GamePlayState objects.
	 * @param dlayer is the DataLayer object.
	 * @param gps is the GamePlayState object.
	 */
	public SpellFinder(DataLayer dlayer, GamePlayState gps) {
		this.dlayer = dlayer;
		this.gps = gps;
		storeAllCards();
	}

	/**
	 * doEffect() will find activated card and implement effects under the context of the current player
	 * while having a grace period before activation.
	 * @param card is the name of the card activated by current player.
	 * @param playergui is the current player's gui.
	 * @return whether the card was successfully activated.
	 */
	public boolean doEffect(String card, PlayerGui playergui, Player[] players) {
		ActivateSpell spellCard = cards.get(card);
		spellCard.setPlayerGui(playergui);
		spellCard.setDlayer(dlayer);
		spellCard.setGamePlayState(gps);
//		if (period) {
//			try {
//				period = false;
//				gracePeriod = true;
//				gp = new GracePeriod(this, players);
//				gp.start();
//				gp.join();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//		gp.addTime();

//		if (immune) {
			run = spellCard.runEffect();
//		}
//		if(!gracePeriod){
//			setImmune(true);
//		}
			
		if (run) {
			playedCard(card, playergui.getPlayer());
		}
		return run;
	}

	/**
	 * playedCard() writes to every PlayerGui that a card has been played.
	 * @param card is the name of the card.
	 * @param cardPlayer is the Player who activated the card.
	 */
	public void playedCard(String card, Player cardPlayer) {
		Player[] players = gps.getPlayers();
		for (Player player : players) {
			if (player != cardPlayer)
				player.getPlayerGui().setTextPane("Player " + cardPlayer.getColor() + " played " + card + "!\n");
		}
	}

	/**
	 * storeAllCards() initializes all the card class instances with card names as keys
	 * within the HashMap. Additionally, the playable states for each card are also
	 * initialized.
	 */
	public void storeAllCards() {
		
		//Movement Restriction.
		cards.put("Lethargy", new Lethargy(dlayer, gps));
		cards.put("Slippery Slope", new SlipperySlope(dlayer, gps));
		cards.put("Updraft", new Updraft(dlayer, gps));
		cards.put("Forest Walks", new ForestWalks(dlayer, gps));
		cards.put("Mirror Mirror", new MirrorMirror(dlayer, gps));
		cards.put("Gravitational Pull", new GravitationalPull(dlayer, gps));
		cards.put("Exhaustion", new Lethargy(dlayer, gps)); //same effect as Lethargy, so create a lethargy is fine.
		cards.put("Ring Pull", new RingPull(dlayer, gps));
		cards.put("Ring Push", new RingPush(dlayer, gps));
//		//Walls	
		cards.put("Prohibition", new Prohibition(dlayer, gps));
		cards.put("The Great Ring Wall", new TheGreatRingWall(dlayer, gps));
		cards.put("Elemental Diffusion", new ElementalDiffusion(dlayer, gps));
//		cards.put("Barrier", new Barrier(dlayer, gps));
		cards.put("Mark of the Void", new MarkOfTheVoid(dlayer, gps));
//		//Damage
		cards.put("Seeker", new Seeker(dlayer, gps));
		cards.put("Tsunami", new Tsunami(dlayer, gps));
		cards.put("Chain Lightning", new ChainLightning(dlayer, gps));
//		cards.put("Jackhammer", new Jackhammer(dlayer, gps));
//		cards.put("Russian Roulette", new RussianRoulette(dlayer, gps));
		cards.put("Infiltrate", new Infiltrate(dlayer, gps));
//		cards.put("Assassinate", new Assassinate(dlayer, gps));
		cards.put("Key", new Key(dlayer, gps));
		cards.put("Death Dice", new DeathDice(dlayer, gps));
		cards.put("Menacing Touch", new MenacingTouch(dlayer, gps));
//		//Utility
//		cards.put("Oracle", new Oracle(dlayer, gps));
//		cards.put("Wheel of Fate", new WheelOfFate(dlayer, gps));
		cards.put("Loaded Dice", new LoadedDice(dlayer, gps));
		cards.put("Planted Bomb", new PlantedBomb(dlayer, gps));
		cards.put("Payday", new Payday(dlayer, gps));
//		cards.put("Gravedigger", new Gravedigger(dlayer, gps));
		cards.put("Portal", new Portal(dlayer, gps));
		cards.put("Gamble", new Gamble(dlayer, gps));
		cards.put("Constellation", new Constellation(dlayer, gps));
		cards.put("Rabbit's Foot", new RabbitsFoot(dlayer, gps));
		cards.put("Implosion", new Implosion(dlayer, gps));
//		cards.put("Reversal", new Reversal(dlayer, gps));
		cards.put("Class Taxation", new ClassTaxation(dlayer, gps));
		cards.put("Roll Twice", new RollTwice(dlayer, gps));
		cards.put("Elemental Distortion", new ElementalDistortion(dlayer, gps));
//		cards.put("Target Disruption", new TargetDisruption(dlayer, gps));
		cards.put("Incorrect Census", new IncorrectCensus(dlayer, gps));
		cards.put("Peasants' Dream", new PeasantsDream(dlayer, gps));
		cards.put("Beacon of Light", new BeaconOfLight(dlayer, gps));
		cards.put("Beacon of Reclamation", new BeaconOfReclamation(dlayer, gps));
		cards.put("Beacon of Haste", new BeaconOfHaste(dlayer, gps));
		cards.put("Beacon of Void", new BeaconOfVoid(dlayer, gps));
//		cards.put("Law Abiding Citizen?", new LawAbidingCitizen(dlayer, gps));
//		cards.put("Uncapped Mitosis", new UncappedMitosis(dlayer, gps));
//		cards.put("Fruitless Endeavor", new FruitlessEndeavor(dlayer, gps));
//		cards.put("Rearrange The Spoils", new RearrangeTheSpoils(dlayer, gps));
//		cards.put("Cruise Control", new CruiseControl(dlayer, gps));
//		cards.put("Twin Teleport", new TwinTeleport(dlayer, gps));
//		cards.put("Dare Devil", new DareDevil(dlayer, gps));
//		cards.put("Rage Quit", new RageQuit(dlayer, gps));
//		cards.put("Kurby", new Kurby(dlayer, gps));
//		//Weather
//		cards.put("Rain", new Rain(dlayer, gps));
//		cards.put("Drought", new Drought(dlayer, gps));
//		cards.put("Industrialization", new Industrialization(dlayer, gps));
//		cards.put("Barricade", new Barricade(dlayer, gps));
//		//Disadvantage
//		cards.put("Cannabalism", new Cannibalism(dlayer, gps));
//		cards.put("A New Challenger Approaches!", new ANewChallengerApproaches(dlayer, gps));
//		cards.put("Pit Stop", new PitStop(dlayer, gps));
//		cards.put("1% Tax", new 1PercentTax(dlayer, gps));
//		cards.put("Baby Boomer", new BabyBoomer(dlayer, gps));
		
		//Set playable states for each.
		setStates("Lethargy", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE);
		setStates("Slippery Slope", GamePlayState.STATES.PRE_MOVEMENT_STATE);
		setStates("Updraft", GamePlayState.STATES.PRE_MOVEMENT_STATE);
		setStates("Forest Walks", GamePlayState.STATES.PRE_MOVEMENT_STATE);
		setStates("Mirror Mirror", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		setStates("Gravitational Pull", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		setStates("Exhaustion", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.ACTIONS_STATE); 
		setStates("Ring Pull", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		setStates("Ring Push", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		setStates("Prohibition", GamePlayState.STATES.PRE_MOVEMENT_STATE);
		setStates("The Great Ring Wall", GamePlayState.STATES.PRE_MOVEMENT_STATE);
		setStates("Elemental Diffusion", GamePlayState.STATES.PRE_MOVEMENT_STATE);
		setStates("Mark of the Void", GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		setStates("Seeker", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		setStates("Tsunami", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		setStates("Chain Lightning", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		setStates("Infiltrate", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		setStates("Key", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		setStates("Death Dice", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		setStates("Menacing Touch", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		setStates("Loaded Dice", GamePlayState.STATES.PRE_MOVEMENT_STATE);
		setStates("Planted Bomb", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		setStates("Payday", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		setStates("Portal", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		setStates("Gamble", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		setStates("Constellation", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		setStates("Rabbit's Foot", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		setStates("Implosion", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		setStates("Class Taxation", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		setStates("Roll Twice", GamePlayState.STATES.PRE_MOVEMENT_STATE);
		setStates("Elemental Distortion", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		setStates("Incorrect Census", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		setStates("Peasants' Dream", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		setStates("Beacon of Light", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		setStates("Beacon of Reclamation", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		setStates("Beacon of Haste", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		setStates("Beacon of Void", GamePlayState.STATES.ROLL_STATE, GamePlayState.STATES.PRE_MOVEMENT_STATE, GamePlayState.STATES.ACTIONS_STATE);
		
	}

	// public void setStates(String name, int...numsToAdd) {
	// ArrayList<Integer> playable = new ArrayList<Integer>();
	// for (int i = 0; i < numsToAdd.length; i++) {
	// playable.add(numsToAdd[i]);
	// }
	// cards.get(name).setPlayableState(playable);
	// }
	
	/**
	 * setStates() is the helper function associated with setting 
	 * the appropriate playable states for each card.
	 * @param name is the name of the card.
	 * @param statesToAdd are all the possible states.
	 */
	public void setStates(String name, STATES... statesToAdd) {
		ArrayList<STATES> playable = new ArrayList<STATES>();
		for (int i = 0; i < statesToAdd.length; i++) {
			playable.add(statesToAdd[i]);
		}
		cards.get(name).setPlayableState(playable);
	}

	/**
	 * getCards() returns the HashMap that stores all the mappings
	 * of each card with its name.
	 * @return HashMap with all the cards.
	 */
	public HashMap<String, ActivateSpell> getCards() {
		return cards;
	}
	
	/**
	 * getGracePeriod() returns the GracePeriod object associated
	 * with determining the waiting period between card plays.
	 * @return GracePeriod object.
	 */
	public GracePeriod getGracePeriod() {
		return gp;
	}

	/**
	 * isGracePeriod() returns the boolean that determines 
	 * whether a grace period is needed.
	 * @return boolean that defaults to false.
	 */
	public boolean isGracePeriod() {
		return gracePeriod;
	}

	/**
	 * setImmune() sets the opposite value for the
	 * immune field.
	 */
	public void setImmune() {
		if (immune) {
			immune = false;
		} else if (!immune) {
			immune = true;
		}
	}

	/**
	 * setGracePlayers() provides the player objects for the
	 * GracePeriod object to manipulate.
	 * @param players is the Player array of all the players in the game.
	 */
	public void setGracePlayers(Player[] players) {
		gp.setPlayers(players);
	}

	/**
	 * setPeriod() sets the period field to the value of the parameter.
	 * @param status is the new boolean.
	 */
	public void setPeriod(boolean status) {
		period = status;
	}
	
	/**
	 * setGracePeriod() sets the gracePeriod field to the value
	 * of the parameter.
	 * @param status is the new boolean.
	 */
	public void setGracePeriod(boolean status) {
		gracePeriod = status;
	}
	
	/**
	 * setImmune() sets the immune field to the value
	 * of the parameter.
	 * @param status is the new boolean.
	 */
	public void setImmune(boolean status) {
		immune = status;
	}
}
