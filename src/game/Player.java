package game;

import interm.ClassFinder;
import interm.SpellFinder;

import java.util.ArrayList;

import main.GamePlayState;

import org.newdawn.slick.Image;

public class Player {

	// player color constants
	public static final int PINK = 1;
	public static final int GREEN = 2;
	public static final int BLUE = 3;
	public static final int BLACK = 4;

	String name = null;
	ArrayList<String> spellCards = new ArrayList<String>();
	ArrayList<String> classCards = new ArrayList<String>();
	Image sprite = null;
	int color;

	float picScale = 1;
	int fireCount = 0;
	int waterCount = 0;
	int windCount = 0;
	int earthCount = 0;
	int checkPoints = 0;
	int classCount = 0;

	int cellsLeft = 10;
	int classLeft = 2;
	int roll = -1;
	PlayerGui playergui;
	Tile currentTile;
	Position currentPosition;
	float playerCoords[];

	// cell info
	ArrayList<Cell> cellList = new ArrayList<Cell>(); // need to max cap it at 10.

	boolean myTurn = false;

	/**
	 * Constructor for Player class
	 * 
	 * @param name
	 *            is the name of the player.
	 */
	public Player(String name, int color, GamePlayState gps, SpellFinder spells, ClassFinder classes, Deck deck) {
		this.name = name;
		this.color = color;
		playergui = new PlayerGui(gps, this, spells, classes, deck);
	}

	/**
	 * addElement() adds an element to the player's repository of elements.
	 * 
	 * @param element
	 *            is the integer representation of the four elements.
	 */
	public void addElement(int element) {
		switch (element) {
		case 0:
			fireCount++;
			System.out.println("Fire" + fireCount);
			break;
		case 1:
			waterCount++;
			System.out.println("Water" + waterCount);
			break;
		case 2:
			windCount++;
			System.out.println("wind" + windCount);
			break;
		case 3:
			earthCount++;
			System.out.println("earth" + earthCount);
			break;
		case 6:
			classCount++;
			break;
		case 7:
			checkPoints++;
			break;
		default:
			// add nothing because blank.
		}
	}
	public void removeElement(int element) {
		switch (element) {
		case 0:
			fireCount--;
			System.out.println("Fire" + fireCount);
			break;
		case 1:
			waterCount--;
			System.out.println("Water" + waterCount);
			break;
		case 2:
			windCount--;
			System.out.println("wind" + windCount);
			break;
		case 3:
			earthCount--;
			System.out.println("earth" + earthCount);
			break;
		case 6:
			classCount--;
			break;
		case 7:
			checkPoints--;
			break;
		default:
			// add nothing because blank.
		}
	}
	public void incrementCellLeft() {
		cellsLeft++;
	}
	public void decrementCellLeft() {
		cellsLeft--;
	}

	public void decrementClassLeft() {
		classLeft--;
	}

	/*
	 * Setter functions.
	 */

	public void setTurn(boolean turn) {
		myTurn = turn;
	}

	public void setImage(Image image) {
		sprite = image;
	}

	public void setCurrentTile(Tile tile) {
		currentTile = tile;
	}

	public void setCurrentPosition(Position position) {
		currentPosition = position;
	}

	public void setPlayerCoords(float[] playerCoords) {
		this.playerCoords = playerCoords;
	}

	public void setScale(float scale) {
		picScale = scale;
	}

	/*
	 * Getter functions.
	 */
	
	public int getNumCells() {
		return cellList.size();
	}

	public ArrayList<Cell> getCellList() {
		return cellList;
	}

	public float[] getPlayerCoords() {
		return playerCoords;
	}

	public boolean getTurn() {
		return myTurn;
	}

	public Image getImage() {
		return sprite;
	}

	public Tile getCurrentTile() {
		return currentTile;
	}

	public Position getCurrentPosition() {
		return currentPosition;
	}

	public void setPlayerGui() {
		playergui.init(name);
	}

	public PlayerGui getPlayerGui() {
		return playergui;
	}

	public void setRoll(int roll) {
		this.roll = roll;
	}

	public int getRoll() {
		return roll;
	}

	public float getScale() {
		return picScale;
	}

	public int getColor() {
		return color;
	}

	public int getFireCount() {
		return fireCount;
	}

	public int getWaterCount() {
		return waterCount;
	}

	public int getWindCount() {
		return windCount;
	}

	public int getEarthCount() {
		return earthCount;
	}

	public int getCheckPoints() {
		return checkPoints;
	}
	public ArrayList<String> getClassCards(){
		return classCards;
	}
	public ArrayList<String> getSpellCards(){
		return spellCards;
	}
}
