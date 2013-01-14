package main;

import game.AdjacentTile;
import game.Board;
import game.Cell;
import game.DataLayer;
import game.Deck;
import game.Player;
import game.Position;
import game.Quadruple;
import game.RenderObject;
import game.Tile;
import interm.ClassFinder;
import interm.SpellFinder;
import interm.TileFinder;
import interm.TurnManager;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GamePlayState extends BasicGameState implements MouseListener {

	DataLayer dlayer;
	Random rand = new Random();
	int stateID = 1;
	Tile targetTile = null;

//	// State representations.
//	public static int START_GAME_STATE = 0;
//	public static int CHANGE_PLAYER_STATE = 1;
//	public static int ROLL_STATE = 2;
//	public static int DRAW_CARD_STATE = 3;
//	public static int PRE_MOVEMENT_STATE = 4;
//	public static int MOVEMENT_STATE = 5;
//	public static int ACTIONS_STATE = 6;
//	public static int PLANT_CELL_STATE = 7;
//	public static int USE_SPELL_STATE = 8;
//	public static int PAUSE_GAME_STATE = 9;
//	public static int GAME_OVER_STATE = 10;

	public enum STATES {
		START_GAME_STATE, CHANGE_PLAYER_STATE, ROLL_STATE, DRAW_CARD_STATE, PRE_MOVEMENT_STATE, MOVEMENT_STATE, ACTIONS_STATE, 
		PLANT_CELL_STATE, USE_SPELL_STATE, PAUSE_GAME_STATE, GAME_OVER_STATE
	}

	// initialize needed class instances.
	private Board board = null;
	private TileFinder tileFinder = null;
	private TurnManager turnManager = null;
	public Tile destination;
	public boolean confirmMove;
	public int numRings = 8;
	
	//renderObjects
	Image markOfTheVoid;
	Image seeker;

	// planting cell variables
	public boolean confirmCell;
	Image player0cell;
	Image player1cell;
	Image player2cell;
	Image player3cell;

	// player variables
	Player[] players = new Player[4];
	Player currentPlayer = null;
	Player targetPlayer = null;
	Position currPosition;
	SpellFinder spells;
	ClassFinder classes;
	Deck deck;

	Image player0pic;
	Image player1pic;
	Image player2pic;
	Image player3pic;
	Sound fx = null;
	Sound fx2 = null;

	double currAngle;
	double currRad;
	double proportionalRad;
	double proportionalAng;
	float picScale = 1;
	boolean status = true;

	ArrayList<Tile> checkRepitition = new ArrayList<Tile>();
	ArrayList<Tile> path = new ArrayList<Tile>();

	// state variables.
	public STATES currentState = null;
	boolean actionsInit = true;
	// boolean plantedCell = false;
	// boolean usedCard = false;
	// boolean usedClassSpell = false;

	// background rendering variables.
	Image background = null;
	TrueTypeFont trueTypeFont = null;

	// center reference
	int centerX = 350;
	int centerY = 350;

	// zoom variables
	int inputDelta = 0;
	float zoomFactorX = 1f;
	float zoomFactorY = 1f;
	int offsetX = 0; // starting offsets for background.
	int offsetY = 0;

	// state boolean variables
	boolean trackMouse = false;
	boolean newInput = false;

	// test variables
	int element;
	double mouseX = 0;
	double mouseY = 0;
	float testOffsetX = 0;
	float testOffsetY = 0;
	int deltaCounter = 100;
	boolean mouseClicked = false;
	boolean init = true;
	boolean testOnce = false;
	int tileCount = 1;
	int tileSpeed;
	// PlayerGui playergui = new PlayerGui(this);
	Integer[] tileNum = { 1, 4, 8, 8, 16, 16, 32, 4 };
	String spellFile = "res/Spell.txt";
	String classFile = "res/Class.txt";

	public GamePlayState(int stateID) {
		this.stateID = stateID;
	}
	

	@SuppressWarnings("deprecation")
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

		// load the board background.
		background = new Image("res/ViralBoardFinal.jpg"); // 15in x 15in
															// with 150
															// pix/inch
		background.setFilter(Image.FILTER_NEAREST);
		fx = new Sound("res/jump.wav");
		fx2 = new Sound("res/jump2.wav");

		// load players
		player0pic = new Image("res/slime0.png");
		player1pic = new Image("res/slime1.png");
		player2pic = new Image("res/slime2.png");
		player3pic = new Image("res/slime3.png");

		// load cell images
		player0cell = new Image("res/PinkCell.png");
		player1cell = new Image("res/GreenCell.png");
		player2cell = new Image("res/BlueCell.png");
		player3cell = new Image("res/BlackCell.png");
		
		//load renderObjects
		markOfTheVoid = new Image("res/MarkOfTheVoid.png");
		seeker = new Image("res/Seeker.png");

		// load the fonts to be used.
		Font font = new Font("Verdana", Font.BOLD, 20);
		trueTypeFont = new TrueTypeFont(font, true);

	}

	@Override
	public void enter(GameContainer gc, StateBasedGame sb) throws SlickException {
		super.enter(gc, sb);
		currentState = STATES.START_GAME_STATE;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		g.scale(zoomFactorX, zoomFactorY);

		// draw the background
		background.draw(offsetX, offsetY);
		
		//draw objects.
		for (RenderObject obj : dlayer.getRenderObjectsArray()) {
			obj.getImage().drawCentered(obj.getCoords()[0], obj.getCoords()[1]);
		}
		
		// draw players
		if (init) {
			for (int i = 0; i < players.length; i++) {
				players[i].setPlayerCoords(tileFinder.angleToPixels(players[i].getCurrentPosition().getPositionRadius(), players[i].getCurrentPosition().getAngle()));
			}
			init = false;
		}
		// System.out.println("1: " +
		// players[0].getCurrentPosition().getOneFourthRadius() + "angle: " +
		// players[0].getCurrentPosition().getAngle());
		// System.out.println("2: " +
		// players[1].getCurrentPosition().getOneFourthRadius() + "angle: " +
		// players[1].getCurrentPosition().getAngle());
		// System.out.println("3: "+players[2].getCurrentPosition().getOneFourthRadius()+
		// "angle: " + players[2].getCurrentPosition().getAngle());
		// System.out.println("4: "+players[3].getCurrentPosition().getOneFourthRadius()
		// +"angle: " + players[3 ].getCurrentPosition().getAngle());

		for (int i = 0; i < players.length; i++) {
			players[i].getImage().getScaledCopy(players[i].getScale()).drawCentered(players[i].getPlayerCoords()[0], players[i].getPlayerCoords()[1]);
		}
		// players[0].getImage().getScaledCopy(players[0].getScale()).drawCentered(players[0].getPlayerCoords()[0], players[0].getPlayerCoords()[1]);
		// players[1].getImage().getScaledCopy(players[1].getScale()).drawCentered(players[1].getPlayerCoords()[0], players[1].getPlayerCoords()[1]);
		// players[2].getImage().getScaledCopy(players[2].getScale()).drawCentered(players[2].getPlayerCoords()[0], players[2].getPlayerCoords()[1]);
		// players[3].getImage().getScaledCopy(players[3].getScale()).drawCentered(players[3].getPlayerCoords()[0], players[3].getPlayerCoords()[1]);

		// draw cells
		for (int i = 0; i < players.length; i++) {
			for (Cell cell : players[i].getCellList()) {
				cell.getImage().drawCentered(cell.getCellCoords()[0], cell.getCellCoords()[1]);
			}
		}

		// player0pic.drawCentered(players[0].getPlayerCoords()[0], players[0].getPlayerCoords()[1]);
		// player1pic.drawCentered(players[1].getPlayerCoords()[0], players[1].getPlayerCoords()[1]);
		// player2pic.drawCentered(players[2].getPlayerCoords()[0], players[2].getPlayerCoords()[1]);
		// player3pic.drawCentered(players[3].getPlayerCoords()[0], players[3].getPlayerCoords()[1]);

		// player0pic.draw(330, 330);
		// player1pic.draw(330, 350);
		// player2pic.draw(350, 330);
		// player3pic.draw(350, 350);
		//
		// trueTypeFont.drawString(10, 110, "(" + mouseX + ", " + mouseY + ")",
		// Color.orange);
		trueTypeFont.drawString(testOffsetX, testOffsetY, "(" + mouseX + ", " + mouseY + ")", Color.orange);
		trueTypeFont.drawString(10, 210, "Current Element is: " + element, Color.orange);

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		switch (currentState) {
		case START_GAME_STATE:
			// initializePlayer();
			initDataLayer();
			initializeBoard();
			initDeck();
			initSpells();
			initializePlayers();
			initializeAdjacent();
//			if (dlayer.getRingList().get(6) != null) {
//				for (int i = 0; i < dlayer.getRingList().get(6).size(); i++) {
//					System.out.println("High Angle: " + i + "  " + dlayer.getRingList().get(6).get(i).getHighAngle());
//					System.out.println("Low Angle: " + dlayer.getRingList().get(6).get(i).getLowAngle());
//					System.out.println("High Radius: " + dlayer.getRingList().get(6).get(i).getHighRadius());
//					System.out.println("Low Radius: " + dlayer.getRingList().get(6).get(i).getLowRadius());
//				}
//			}
			currentState = STATES.CHANGE_PLAYER_STATE;
			break;
		case CHANGE_PLAYER_STATE:
			dlayer.getPossibleMoves().clear();
			dlayer.getPossibleMovesPaths().clear();
			mouseClicked = false;
			currentPlayer = turnManager.nextPlayer();
			currentPlayer.getPlayerGui().setRollDice(true);
			actionsInit = true;
			currentState = STATES.ROLL_STATE;
			break;
		case ROLL_STATE:

			ArrayList<Tile> tempUnavailable = new ArrayList<Tile>();
			ArrayList<Tile> permUnavailable = new ArrayList<Tile>();
			Tile currTile = currentPlayer.getCurrentTile();
			int roll = currentPlayer.getRoll();
			
			// currentPlayer.getCurrentTile().getAdjacentTiles();
			// currentPlayer.getCurrentTile().getRingNum();
			// currentPlayer.getCurrentTile().getTileID();
			if (currentPlayer.getPlayerGui().getRolled()) {
				
				
				setPossibleMoves(roll, currTile, tempUnavailable, permUnavailable, false, false, false);
				Collections.sort(dlayer.getPossibleMoves(), new Comparator<Tile>() {
					public int compare(Tile a, Tile b) {
						return a.getPosition().compareTo(b.getPosition());
					}
				});
				// for (Tile tile : dlayer.getPossibleMoves()) {
				// System.out.println("Ring Num: " + tile.getRingNum() + "     TileID: " + tile.getTileID());
				// }

//				setTrackMouse(true);
				setConfirmMove(false);
				currAngle = currentPlayer.getCurrentPosition().getAngle();
				currRad = currentPlayer.getCurrentPosition().getPositionRadius();
				currentState = STATES.PRE_MOVEMENT_STATE;
			}
			break;
		case PRE_MOVEMENT_STATE:
			if (dlayer.getPossibleMoves().isEmpty()) {
				currentPlayer.getPlayerGui().setTextPane("You have no possible moves!\n");
				currentState = STATES.ACTIONS_STATE;
			}
			if (confirmMove) {
//				setTrackMouse(false);
				currentState = STATES.MOVEMENT_STATE;
			}
			break;
		case MOVEMENT_STATE:
			// check for if no possible movements are there.
			path = dlayer.getPossibleMovesPaths().get(getDestination()).get(0); //get the first path linked with targetTile.
			if (deltaCounter <= 0) {
				initAnimation();
			}
			deltaCounter -= delta;
			break;
		case ACTIONS_STATE:
//			if (!testOnce) {
//				dlayer.getRingList().get(6).get(4).removeAllCells(players[0]);
//				testOnce = true;
//			}
			if (actionsInit) {
				currentPlayer.getPlayerGui().setPlantCell(true);
				actionsInit = false;
			}
			if (confirmCell) {
				currentState = STATES.PLANT_CELL_STATE;
			}
			break;
		case PLANT_CELL_STATE:
//			boolean canPlant = true;
//			//check if chkpt tile and if so, make sure other chkpts have at least one cell too.
//			if (currentPlayer.getCurrentTile().getElement() == Tile.CHECKPOINT && currentPlayer.getCurrentTile().hasCells()) {
//				for (Tile chkpt : dlayer.getRingList().get(7)) {
//					if (!chkpt.hasCells()) {
//						currentPlayer.getPlayerGui().setTextPane("Cannot plant. Checkpoint is locked.\n");
//						canPlant = false;
//						break;
//					}
//				}
//			}
//			if (canPlant) {
				fx2.play();
				addCell(currentPlayer, currentPlayer.getCurrentTile(), this);
//				Cell newCell = new Cell(currentPlayer, currentPlayer.getCurrentTile());
//				float[] cellCoords = tileFinder.angleToPixels(newCell.getCurrentPosition().getPositionRadius(), newCell.getCurrentPosition().getAngle());
//				newCell.setCellCoords(cellCoords);
//				newCell.setImage(getCellImage(currentPlayer));
//				currentPlayer.getCellList().add(newCell);
//				// add element to player's repository of elements.
//				int element = currentPlayer.getCurrentTile().getElement();
//				currentPlayer.addElement(element);
//				// decrease cell count
//				currentPlayer.getCurrentTile().addToCellStorage(currentPlayer, newCell);
//				currentPlayer.decrementCellLeft();
//			}
			setConfirmCell(false);
			currentState = STATES.ACTIONS_STATE;
			break;
		case DRAW_CARD_STATE:
			break;
		case USE_SPELL_STATE:
			break;
		case PAUSE_GAME_STATE:
			break;
		case GAME_OVER_STATE:
			break;
		}

		// Test animation!
		// if (deltaCounter <= 0 && mouseClicked == true) {
		// testOffsetX += 1;
		// deltaCounter = 10;
		// if (testOffsetX >= 350) {
		// mouseClicked = false;
		// }
		// }
		// deltaCounter -= delta;
		// check if user wants to zoom.
		checkZoomAndDisplay(gc, delta);

	}
	
	/**
	 * addCell(3 params) adds one cell to targetTile for currentPlayer.
	 * @param currentPlayer is the player that plants the cell.
	 * @param targetTile is the tile where the cell will be located.
	 * @param gps is the GamePlayState instance where cell will be controlled.
	 */
	public void addCell(Player currentPlayer, Tile targetTile, GamePlayState gps) {
		Cell newCell = new Cell(currentPlayer, targetTile);
		float[] cellCoords = gps.getTileFinder().angleToPixels(newCell.getCurrentPosition().getPositionRadius(), newCell.getCurrentPosition().getAngle());
		newCell.setCellCoords(cellCoords);
		newCell.setImage(gps.getCellImage(currentPlayer));
		currentPlayer.getCellList().add(newCell);
		// add element to player's repository of elements.
		int element = targetTile.getElement();
		currentPlayer.addElement(element);
		// decrease cell count
		targetTile.addToCellStorage(currentPlayer, newCell);
		currentPlayer.decrementCellLeft();
	}

	/**
	 * addCell (4 params) adds a variable amount of cells to targetTile for currentPlayer.
	 * @param currentPlayer is the player that plants the cell.
	 * @param targetTile is the tile where the cell will be located.
	 * @param gps is the GamePlayState instance where cell will be controlled.
	 * @param numToAdd is the number of cells to add to the tile.
	 */
	public void addCell(Player currentPlayer, Tile targetTile, GamePlayState gps, int numToAdd) {
		for (int i = 0; i < numToAdd; i++) {
			Cell newCell = new Cell(currentPlayer, targetTile);
			float[] cellCoords = gps.getTileFinder().angleToPixels(newCell.getCurrentPosition().getPositionRadius(), newCell.getCurrentPosition().getAngle());
			newCell.setCellCoords(cellCoords);
			newCell.setImage(gps.getCellImage(currentPlayer));
			currentPlayer.getCellList().add(newCell);
			// add element to player's repository of elements.
			int element = targetTile.getElement();
			currentPlayer.addElement(element);
			// decrease cell count
			targetTile.addToCellStorage(currentPlayer, newCell);
			currentPlayer.decrementCellLeft();
		}
	}

	/**
	 * initDeck() initializes the new deck instance and processes card and class definitions.
	 */
	public void initDeck() {
		deck = new Deck();
		deck.readFile(spellFile, "spell");
		deck.readFile(classFile, "class");
	}

	/**
	 * initSpells() initializes new SpellFinder and ClassFinder instances.
	 */
	public void initSpells() {
		spells = new SpellFinder(dlayer, this);
		classes = new ClassFinder(dlayer, this);
	}
	
	/**
	 * initAnimation() is the helper function called in update to move player pieces from one tile to another.
	 * TileSpeed determines the speed of the tile in movement (lower the value = faster). In order to keep
	 * the scaling of the player is maintained by having the tileSpeed/2 set.
	 */
	public void initAnimation() {
		if (tileCount < path.size()) {
			// set initial data once for each destination
			if (status == true) {
				double difference;
				currentPlayer.getCurrentTile().leavePlayer(currentPlayer);
				currentPlayer.setCurrentTile(path.get(tileCount));
				currentPlayer.setCurrentPosition(currentPlayer.getCurrentTile().getPlayerPosition(currentPlayer));

				difference = (currentPlayer.getCurrentPosition().getAngle() - currAngle);
				if (difference > 180) {
					difference -= 360;
				} else if (Math.abs(difference) > 180) {
					difference += 360;
				}
				tileSpeed = 20;
				proportionalAng = difference / tileSpeed;
				proportionalRad = (currentPlayer.getCurrentPosition().getPositionRadius() - currRad) / tileSpeed;

				System.out.println(currentPlayer.getCurrentPosition().getAngle() + " = " + currAngle);
				System.out.println(currentPlayer.getCurrentPosition().getPositionRadius() + " = " + currRad);
				status = false;
			}

			// iterates through this 40 times and moves the player accordingly
			if (tileSpeed > 0) {
				deltaCounter = 10;
				currRad += proportionalRad;
				currAngle += proportionalAng;
				if (tileSpeed == 20) {
					fx.play();
				}
				if (currAngle < 0) {
					currAngle += 360;
				}
				if (currAngle > 360) {
					currAngle -= 360;
				}

				if (tileSpeed > 10) {
					picScale += 0.05;
					currentPlayer.setScale(picScale);
				} else {
					picScale -= 0.05;
					currentPlayer.setScale(picScale);
				}
				System.out.println("Cur Rad " + currRad);
				System.out.println("Cur Angle " + currAngle);
				currentPlayer.setPlayerCoords(tileFinder.angleToPixels(currRad, currAngle));
				tileSpeed--;
				// moves to the next destination
			} else {
				status = true;
				tileCount++;
				Thread t = new Thread();
				try {
					t.sleep(300);// How long to wait before each tile
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// resets all when all destinations have been reached
		} else {
			path.clear();
			confirmMove = false;
			tileCount = 1;
			switch (getDestination().getElement()) {
			case 5:
				if (currentPlayer.getPlayerGui().getCardCount() < 7) {
					currentPlayer.getPlayerGui().setDrawCard(true);
				}
				break;
			}
			currentPlayer.getPlayerGui().setEndButton(true);
			setTargetTile(null); //reset targetTile
			currentState = STATES.ACTIONS_STATE;
		}
	}

	/**
	 * setPossibleMoves() sets the possibleMoves ArrayList and possibleMovesPaths HashMap for the current player's roll.
	 * @param roll is the number of spaces to move.
	 * @param currTile is the tile where the player starts from.
	 * @param tempUnavailable stores the tiles that are unavailable for current recursion due to game rules. This list gets specially cleared on the 6th ring.
	 * @param permUnavailable stores the tiles that are currently in the analyzed path. We remove the currentTile as we end each recursive instance.
	 * @param hitSixth marks whether the current recursive call has already hit the 6th ring.
	 * @param hitSixthReset is true only on the first recursive call that the currTile is on the 6th ring.
	 * @param specialAdj is true when a spell or class card forbids the temporary banning of tiles.
	 */
	public void setPossibleMoves(int roll, Tile currTile, ArrayList<Tile> tempUnavailable, ArrayList<Tile> permUnavailable, boolean hitSixth, boolean hitSixthReset, boolean specialAdj) {

		if (currTile.getRingNum() == 6) {
			hitSixth = true;
			hitSixthReset = true;
		}
		if (roll == 0) {
			if (!dlayer.getPossibleMoves().contains(currTile)) {
				dlayer.getPossibleMoves().add(currTile);
			}
			ArrayList<Tile> path = new ArrayList<Tile>();
			for (Tile pathTile : permUnavailable) {
				path.add(pathTile);
			}
			path.add(currTile);
			if (dlayer.getPossibleMovesPaths().containsKey(currTile)) {
				ArrayList<ArrayList<Tile>> existingPaths = dlayer.getPossibleMovesPaths().get(currTile);
				existingPaths.add(path);
			}
			else {
				ArrayList<ArrayList<Tile>> newPaths = new ArrayList<ArrayList<Tile>>();
				newPaths.add(path);
				dlayer.getPossibleMovesPaths().put(currTile, newPaths);
			}
//			dlayer.getPossibleMovesPaths().put(currTile, path);
//			for (Tile t : dlayer.getPossibleMovesPaths().get(currTile)) {
//				System.out.print(t.getPosition() + ", ");
//			}
//			System.out.println();

		} else {
			int newRoll = roll - 1;
			ArrayList<Tile> banned = new ArrayList<Tile>();
			if (hitSixth && !specialAdj) {
				banned = currTile.findUpperAdjTiles(currTile);
			}
			for (AdjacentTile adjTile : currTile.getAvailableAdjTiles()) {
				// if ((adjTile.getTile().getRingNum() == 6 || adjTile.getTile().getRingNum() == 0) && !permUnavailable.contains(adjTile.getTile())
				// && ((hitSixth == true && adjTile.getTile().getRingNum() <= currTile.getRingNum()) || (hitSixth == false) || adjTile.getTile().getRingNum() == 7)) {
				if ((adjTile.getTile().getRingNum() == 6 || adjTile.getTile().getRingNum() == 0) && !permUnavailable.contains(adjTile.getTile()) && !currTile.getStoredUnavailTiles().contains(adjTile)
						&& ((hitSixth == true && !banned.contains(adjTile.getTile())) || (hitSixth == false) || adjTile.getTile().getRingNum() == 7)) {
					if (!permUnavailable.contains(currTile)) {
						permUnavailable.add(currTile);
					}
					ArrayList<Tile> newTempUnavailable = new ArrayList<Tile>();
					if (currTile.getStoredAdjTiles().containsValue(adjTile) || specialAdj) {
						setPossibleMoves(newRoll, adjTile.getTile(), newTempUnavailable, permUnavailable, hitSixth, false, true);
					}
					else {
						setPossibleMoves(newRoll, adjTile.getTile(), newTempUnavailable, permUnavailable, hitSixth, false, false);
					}

				} else if (adjTile.isAvailable() && !tempUnavailable.contains(adjTile.getTile()) && !permUnavailable.contains(adjTile.getTile()) && !currTile.getStoredUnavailTiles().contains(adjTile)
						&& ((hitSixth == true && !banned.contains(adjTile.getTile())) || (hitSixth == false) || adjTile.getTile().getRingNum() == 7)) {
					if (!permUnavailable.contains(currTile)) {
						permUnavailable.add(currTile);
					}
					for (AdjacentTile unadjTile : currTile.getUnAvailableAdjTiles()) {
						if (!tempUnavailable.contains(unadjTile.getTile())) {
							tempUnavailable.add(unadjTile.getTile());
						}
					}
					if (currTile.getStoredAdjTiles().containsValue(adjTile) || specialAdj) {
						setPossibleMoves(newRoll, adjTile.getTile(), tempUnavailable, permUnavailable, hitSixth, false, true);
					}
					else {
						setPossibleMoves(newRoll, adjTile.getTile(), tempUnavailable, permUnavailable, hitSixth, false, false);
					}
				}
			}
		}
		for (AdjacentTile unadjTile : currTile.getUnAvailableAdjTiles()) {
			tempUnavailable.remove(unadjTile.getTile());
		}
		// tempUnavailable.clear(); //clear out tempUnavailable to store new
		// ones for next recursive call.
		permUnavailable.remove(currTile);

		if (hitSixthReset) {
			hitSixth = false;
		}
	}
	
	/**
	 * initializeAdjacent() is used to set the adjacent tiles for every tile.
	 */
	private void initializeAdjacent() {
		for (int i = 0; i < numRings; i++) {
			for (int j = 0; j < tileNum[i]; j++) {
				dlayer.getRingList().get(i).get(j).setAdjacentTiles();
			}
		}
		// System.out.println("HA: " + dlayer.getRingList().get(7).get(0).getAvailableAdjTiles().get(0).getTile().getHighAngle());
		// System.out.println("LA: " + dlayer.getRingList().get(7).get(0).getAvailableAdjTiles().get(0).getTile().getLowAngle());
		// System.out.println("HR: " + dlayer.getRingList().get(7).get(0).getAvailableAdjTiles().get(0).getTile().getHighRadius());
		// System.out.println("LR: " + dlayer.getRingList().get(7).get(0).getAvailableAdjTiles().get(0).getTile().getLowRadius());

	}

	/**
	 * initDataLayer() sets the data layer for GamePlayState.
	 */
	public void initDataLayer() {
		dlayer = new DataLayer();
	}

	/**
	 * initializeBoard() sets up the board, tileFinder and turnManagers.
	 */
	public void initializeBoard() {
		board = new Board(dlayer);
		tileFinder = new TileFinder(board, dlayer);
		turnManager = new TurnManager(players);
	}

	/**
	 * initializePlayers() sets up the four players, their images, gui, and location on the board.
	 */
	public void initializePlayers() {
		for (int i = 0; i < 4; i++) {
			players[i] = new Player("Player " + (i + 1), i + 1, this, spells, classes, deck);
			players[i].setPlayerGui(); // initializes the gui for the player
			// quadrant 1 tests
			// players[i].setCurrentTile(board.getTiles().get(new Quadruple(135, 146.25, Tile.HRADRING5, Tile.HRADRING6))); // iffy
			// players[i].setCurrentTile(board.getTiles().get(new Quadruple(90, 135, Tile.HRADRING1, Tile.HRADRING2))); // iffy
			// players[i].setCurrentTile(board.getTiles().get(new Quadruple(78.75, 90, Tile.HRADRING5, Tile.HRADRING6))); // iffy.
			// players[i].setCurrentTile(board.getTiles().get(new Quadruple(67.5, 78.75, Tile.HRADRING5, Tile.HRADRING6)));
			// players[i].setCurrentTile(board.getTiles().get(new Quadruple(67.5, 112.5, Tile.HRADRING2, Tile.HRADRING3)));
			// players[i].setCurrentTile(board.getTiles().get(new Quadruple(78.75, 101.25, Tile.HRADRING4, Tile.HRADRING5)));
			// players[i].setCurrentTile(board.getTiles().get(new Quadruple(0, 11.25, Tile.HRADRING5, Tile.HRADRING6))); // VERY
			// // IFFY.
			// players[i].setCurrentTile(board.getTiles().get(new Quadruple(0, 22.5, Tile.HRADRING3, Tile.HRADRING4))); // VERY
			// // IFFY.
			// players[i].setCurrentTile(board.getTiles().get(new Quadruple(0, 360, 0, Tile.HRADCENTER))); // VERY//
			// // IFFY.
			// players[i].setCurrentTile(board.getTiles().get(new Quadruple(0, 90, Tile.HRADCENTER, Tile.HRADRING1))); // VERY
			// // //
			// // IFFY.
			// players[i].setCurrentTile(board.getTiles().get(new Quadruple(22.5, 67.5, Tile.HRADRING2, Tile.HRADRING3))); // VERY
			// // quadrant 2 tests
			// players[i].setCurrentTile(board.getTiles().get(new Quadruple(123.75, 146.25, Tile.HRADRING4, Tile.HRADRING5)));
			// players[i].setCurrentTile(board.getTiles().get(new Quadruple(90, 135, Tile.HRADRING1, Tile.HRADRING2)));
			// players[i].setCurrentTile(board.getTiles().get(new Quadruple(112.5, 157.5, Tile.HRADRING2, Tile.HRADRING3)));
			// players[i].setCurrentTile(board.getTiles().get(new Quadruple(157.5, 168.75, Tile.HRADRING5, Tile.HRADRING6)));
			// // //good!
			// players[i].setCurrentTile(board.getTiles().get(new Quadruple(135, 146.25, Tile.HRADRING5, Tile.HRADRING6))); // good.
			// players[i].setCurrentTile(board.getTiles().get(new Quadruple(168.75, 180, Tile.HRADRING5, Tile.HRADRING6))); // good.
			// players[i].setCurrentTile(board.getTiles().get(new Quadruple(157.5, 202.5, Tile.HRADRING2, Tile.HRADRING3)));
			// // //good.
			// players[i].setCurrentTile(board.getTiles().get(new Quadruple(168.75, 191.25, Tile.HRADRING4, Tile.HRADRING5)));
			// // good..
			// players[i].setCurrentTile(board.getTiles().get(new Quadruple(348.75, 371.25, Tile.HRADRING4, Tile.HRADRING5)));
			// // good..
			//
			// // quadrant 3 tests
			// players[i].setCurrentTile(board.getTiles().get(new Quadruple(180, 225, Tile.HRADRING1, Tile.HRADRING2))); // good.
			//
			// players[i].setCurrentTile(board.getTiles().get(new Quadruple(67.5, 90, Tile.HRADRING3, Tile.HRADRING4)));
//			 players[i].setCurrentTile(board.getTiles().get(new Quadruple(45, 56.25, Tile.HRADRING5, Tile.HRADRING6)));
			// players[i].setCurrentTile(board.getTiles().get(new Quadruple(45, 67.5, Tile.HRADRING3, Tile.HRADRING4)));
			// players[i].setCurrentTile(board.getTiles().get(new Quadruple(0, 90, 39.71, 39.71 + 33.14)));
			 players[i].setCurrentTile(board.getTiles().get(new Quadruple(33.75, 45, Tile.HRADRING5, Tile.HRADRING6)));
//			players[i].setCurrentTile(board.getTiles().get(new Quadruple(11.25, 22.5, Tile.HRADRING5, Tile.HRADRING6)));
//			 players[i].setCurrentTile(board.getTiles().get(new Quadruple(0, 360, board.getCenterStart(), board.getCenterEnd()))); // get the center as starting
			// // tile.
			// System.out.println(players[i].getCurrentTile().getHighAngle());
			// System.out.println(players[i].getCurrentTile().getLowAngle());
			players[i].setCurrentPosition(players[i].getCurrentTile().getPlayerPosition(players[i]));

			// players[i].setCurrentPosition(players[i].getCurrentTile().getPlayerPosition(players[i]));
			// //set player position in center tile.
			// System.out.println(tileFinder.angleToPixels(players[i].getCurrentPosition().getOneFourthRadius()[0],
			// players[i].getCurrentPosition().getOneFourthRadius()[0]))
		}
		players[0].setImage(player0pic);
		players[1].setImage(player1pic);
		players[2].setImage(player2pic);
		players[3].setImage(player3pic);
		for(int i = 0; i < 4; i++){
			players[i].getPlayerGui().setPlayerList(players);
		}
		
//		addCell(players[0], dlayer.getRingList().get(6).get(4), this, 2);
	}

	public void checkZoomAndDisplay(GameContainer gc, int delta) {
		inputDelta -= delta;
		Input myInput = gc.getInput();
		if (inputDelta <= 0) {
			if (myInput.isKeyDown(Input.KEY_0)) {
				if (zoomFactorX < 1.5) {
					zoomFactorX += 0.1f;
					zoomFactorY += 0.1f;
					inputDelta = 100;
				}
			} else if (myInput.isKeyDown(Input.KEY_9)) {
				if (zoomFactorX > 0.7f) {
					zoomFactorX -= 0.1f;
					zoomFactorY -= 0.1f;
					inputDelta = 100;
				}
			} else if (myInput.isKeyDown(Input.KEY_RIGHT)) {
				offsetX -= 5;
				inputDelta = 20;
			} else if (myInput.isKeyDown(Input.KEY_LEFT)) {
				offsetX += 5;
				inputDelta = 20;
			} else if (myInput.isKeyDown(Input.KEY_UP)) {
				offsetY += 5;
				inputDelta = 20;
			} else if (myInput.isKeyDown(Input.KEY_DOWN)) {
				offsetY -= 5;
				inputDelta = 20;
			}
		}
	}

	/**
	 * findRadius() computes the integer radius of a provided coordinate in pixels.
	 * @param mouseX is x coordinate of mouse.
	 * @param mouseY is y coordinate of mouse.
	 * @return radius between the center of the board to coordinate.
	 */

	private double findRadius(int mouseX, int mouseY) {
		int xLength = mouseX - centerX;
		int yLength = -(mouseY - centerY); // must invert y because the y axis
											// is the opposite direction.
		double radius = Math.sqrt(Math.pow(xLength, 2) + Math.pow(yLength, 2));
		return radius;
	}

	/**
	 * findAngle() computes the angle of a provided coordinate and center of board within the range of 0 <= a < 360 degrees. Note: Inverts y axis on purpose to make it more intuitive with Cartesian
	 * coordinate system.
	 * @param mouseX is x coordinate of mouse.
	 * @param mouseY is y coordinate of mouse.
	 * @return translated angle within appropriate range between board center and coordinate.
	 */
	private double findAngle(int mouseX, int mouseY) {
		int xLength = mouseX - centerX;
		int yLength = -(mouseY - centerY); // must invert y because the y axis
											// is the opposite direction.
		double angleInRads = Math.atan2(yLength, xLength);
		double angle = Math.toDegrees(angleInRads);
		/*
		 * 4 cases for translation: 1) if both x and y are positive, keep the same angle. 2) if x is positive and y is negative, add 360 degrees to angle. 3) if x is negative and y is positive, add
		 * 180 degrees to angle. 4) if both x and y are negative, add 180 degrees to angle.
		 */

		if (xLength < 0 && yLength < 0 || xLength > 0 && yLength < 0) {
			double finalAngle = angle + 360;
			return finalAngle;
		} else {
			double finalAngle = angle;
			// System.out.println("FinalAngle: " + finalAngle);
			return finalAngle;
		}
	}

	/**
	 * mouseClicked() is implemented to get mouse clicks from the user.
	 * @param button is the button the mouse clicked.
	 * @param x is the x coordinate of the location.
	 * @param y is the y coordinate of the location.
	 * @param clickCount is the number of times the mouse was clicked.
	 */
	public void mouseClicked(int button, int x, int y, int clickCount) {
		// TODO Auto-generated method stub
		if (currentState == STATES.PRE_MOVEMENT_STATE) {
			double targetRadius = findRadius(x, y);
			double targetAngle = findAngle(x, y);
			targetTile = tileFinder.findTile(targetRadius, targetAngle);
			if (!dlayer.getPossibleMoves().contains(targetTile)) {
				currentPlayer.getPlayerGui().setMoveButton(false);
				currentPlayer.getPlayerGui().setTextPane("Not a valid move! Try Again\n");
			} else {
				currentPlayer.getPlayerGui().setMoveButton(true);
				currentPlayer.getPlayerGui().setTextPane("You clicked (" + targetTile.getRingNum() + "," + targetTile.getTileID() + ").\n");
			}
			
			if (targetTile != null) {
				// System.out.println("Low Radius: " +
				// targetTile.getLowRadius());
				// System.out.println("High Radius: " +
				// targetTile.getHighRadius());
				// System.out.println("Low Angle: " + targetTile.getLowAngle());
				// System.out.println("High Angle: " +
				// targetTile.getHighAngle());
				element = targetTile.getElement();
			}
		} else if (currentState == STATES.USE_SPELL_STATE) {
			double targetRadius = findRadius(x, y);
			double targetAngle = findAngle(x, y);
			targetTile = tileFinder.findTile(targetRadius, targetAngle);
			setNewInput(true);
		}
		mouseClicked = true;
		mouseX = x;
		mouseY = y;
		// mouseX = x-centerX;
		// mouseY = y-centerY;
	}

	/**
	 * getID() returns the id of the current state for ViralGame to change states if necessary.
	 */
	public int getID() {
		// TODO Auto-generated method stub
		return stateID;
	}

	/**
	 * getCellImage() returns the correct image for the input player's piece.
	 * @param player is the player requesting the image.
	 * @return the Image of the player's piece.
	 */
	public Image getCellImage(Player player) {
		int playerNum = player.getColor();
		switch (playerNum) {
		case 1:
			return player0cell;
		case 2:
			return player1cell;
		case 3:
			return player2cell;
		default:
			return player3cell;
		}
	}

	public STATES getCurrentState() {
		return currentState;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public Player[] getPlayers() {
		return players;
	}

	public TurnManager getTurnManager() {
		return turnManager;
	}
	
	public TileFinder getTileFinder() {
		return tileFinder;
	}
	
	public DataLayer getDLayer() {
		return dlayer;
	}

	public void resetState() {
		this.currentState = STATES.CHANGE_PLAYER_STATE;
	}
	public void setState(STATES states){
		this.currentState = states;
	}

	public void setDestination() {
		this.destination = targetTile;
	}

	public Tile getDestination() {
		return destination;
	}

	public Tile getTargetTile() {
		return targetTile;
	}
	
	public void setTargetTile(Tile tile) {
		targetTile = tile;
	}
	
	public boolean getNewInput() {
		return newInput;
	}
	
	public void setNewInput(boolean status) {
		newInput = status;
	}

	public void setTrackMouse(boolean status) {
		trackMouse = status;
	}

	public void setConfirmMove(boolean status) {
		confirmMove = status;
	}

	public void setConfirmCell(boolean status) {
		confirmCell = status;
	}
}
