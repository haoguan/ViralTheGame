/* Tile.java */

package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import main.GamePlayState;

public class Tile {

	//constants
	public final static int FIRE = 0;
	public final static int WATER = 1;
	public final static int WIND = 2;
	public final static int EARTH = 3;
	public final static int BLANK = 4;
	public final static int SPELLCARD = 5;
	public final static int CLASSCARD = 6;
	public final static int CHECKPOINT = 7;
//	public final static int CELL_LIMIT = 2;
	
	//Limits
	public final static int BOARD_MAX_CELLS = 2;
	public final static int CHKPT_MAX_CELLS = 4;
	
	public final static double HRADCENTER = 40;
	public final static double HRADRING1 = 75;
	public final static double HRADRING2 = 110;
	public final static double HRADRING3 = 145;
	public final static double HRADRING4 = 180;
	public final static double HRADRING5 = 215;
	public final static double HRADRING6 = 250;
	public final static double HRADRING7 = 285;

	//checkpoint tile constants.
	public final static int ADJCHK1 = 3;
	public final static int ADJCHK2 = 12;
	public final static int ADJCHK3 = 19;
	public final static int ADJCHK4 = 28;
	boolean locked = false; //used to lock and unlock for planting in checkpoints.

	boolean positionOneUsed = false;
	boolean positionTwoUsed = false;
	boolean positionThreeUsed = false;
	boolean positionFourUsed = false;

	//player and cell rendering variables.
	double oneEighthRadius;
	double sevenEighthRadius;
	double oneHalfRadius;
	double difference;
	Position renderObjLoc;
	RenderObject renderObj = null;
	RenderObject beaconObj = null;

	HashMap<Cell, Player> cellStorage = new HashMap<Cell, Player>();
	ArrayList<Position> playerPositions = new ArrayList<Position>();
	ArrayList<Position> cellPositions = new ArrayList<Position>();
	ArrayList<AdjacentTile> availableTiles = new ArrayList<AdjacentTile>();
	ArrayList<AdjacentTile> unavailableTiles = new ArrayList<AdjacentTile>();
	ArrayList<AdjacentTile> storedUnavailTiles = new ArrayList<AdjacentTile>();
	HashMap<Tile, AdjacentTile> storedAvailTiles = new HashMap<Tile, AdjacentTile>(); //these are additional availables due to card effects.

	//coordinates to locate the tile.
	final double angleL; //ranges from 0-360 degrees in coordinate system.0
	final double angleH;
	final double radiusL;
	final double radiusH;

	//data located within tile.
	int numPlayers = 0;
	int numCells = 0;
	int element = BLANK;
//	Player player = null;
	Integer ringNum;
	int tileID;
	Double position;
	DataLayer dlayer = new DataLayer();
	Sound removeCell;
	
	//special variable to change roll.
	int alterRoll = -1; //normally want to decrement by 1, but if special card changes this, can extend your turn.

	/**
	 * Constructs an empty tile with the low and high angles along with the low and high radiuses.
	 *
	 * @param lowAngle is the smaller of the angles.
	 * @param highAngle is the larger of the angles.
	 * @param lowRadius is the smaller of the radiuses.
	 * @param highRadius is the larger of the radiuses.
	 */
	public Tile(double lowAngle, double highAngle, double lowRadius, double highRadius) {
		angleL = lowAngle;
		angleH = highAngle;
		radiusL = lowRadius;
		radiusH = highRadius;
		difference = highRadius - lowRadius;

		oneHalfRadius = difference/2 + lowRadius;
		oneEighthRadius = lowRadius + difference*0.45;
		sevenEighthRadius = highRadius - difference*0.3;
		try {
			removeCell = new Sound("res/GamePlayState/Sounds/removeCells.wav");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
//	public Tile(Tile clone) {
//		angleL = clone.getLowAngle();
//		angleH = clone.getHighAngle();
//		radiusL = clone.getLowRadius();
//		radiusH = clone.getHighRadius();
//		difference = clone.getHighRadius() - clone.getLowRadius();
//		
//		oneHalfRadius = difference/2 + clone.getLowRadius();
//		oneEighthRadius = clone.getLowRadius() + difference*0.45;
//		sevenEighthRadius = clone.getHighRadius() - difference*0.3;
//		
//		renderObjLoc = clone.getRenderObjLoc();
//		renderObj = clone.getRenderObj();
//		
//		cellStorage = clone.getCellStorage();
//		playerPositions = clone.getPlayerParts();
//		cellPositions = clone.getCellParts();
//		availableTiles = clone.getAvailableAdjTiles();
//		unavailableTiles = clone.getUnAvailableAdjTiles();
//		storedUnavailTiles = clone.getStoredUnavailTiles();
//		storedAvailTiles = clone.getStoredAdjTiles();
//		
//		numPlayers = clone.getNumPlayers();
//		numCells = clone.getNumCells();
//		element = clone.getElement();
//		ringNum = clone.getRingNum();
//		tileID = clone.getTileID();
//		dlayer = clone.getDlayer();
//	}


//	public static void main (String[] args) {
//		Tile tile = new Tile(0, 360, 0, 27.8);
//		tile.setPlayerParts();
//		tile.setCellParts();
//		for (Position pos: tile.playerPositions) {
//			System.out.println("Radius: " + pos.oneFourthRadius + " Angle: " + pos.positionAngle);
//		}
//		System.out.println(tile.oneFourthRadius);
//	}

	//getting and setting cellStorage
	public HashMap<Cell, Player> getCellStorage() {
		return cellStorage;
	}

	public void setCellStorage(HashMap<Cell, Player> cellStorage) {
		this.cellStorage = cellStorage;
	}

	//getting the angles
	public double getLowAngle() {
		return angleL;
	}

	public double getHighAngle() {
		return angleH;
	}

	public double getLowRadius() {
		return radiusL;
	}

	public double getHighRadius() {
		return radiusH;
	}

	//getting and setting the element
	public void setElement(int element) {
		this.element = element;
	}

	public int getElement() {
		return element;
	}

	// Get value of one fourth of radius between high and low radius (counting from top bound) from the center.
	public double getOneEighthRadius(){
		return oneEighthRadius;
	}

	public double getSevenEighthRadius(){
		return sevenEighthRadius;
	}

	public double getOneHalfRadius() {
		return oneHalfRadius;
	}

	//getting center renderObj position.
	public Position getRenderObjLoc() {
		return renderObjLoc;
	}

	public RenderObject getRenderObj() {
		return renderObj;
	}

	public void setRenderObj(RenderObject renderObj) {
		this.renderObj = renderObj;
	}
	
	//getting and setting alterRoll
	public int getAlterRoll() {
		return alterRoll;
	}

	public void setAlterRoll(int alterRoll) {
		this.alterRoll = alterRoll;
	}
	
	//getting and setting beacon
	public Position getBeaconObjLoc() {
		return renderObjLoc;
	}
	
	public RenderObject getBeaconObj() {
		return beaconObj;
	}
	
	public void setBeaconObj(RenderObject beaconObj) {
		this.beaconObj = beaconObj;
	}
	
	// Getting and setting the ring numbers and tileIDs.
	public Integer getRingNum() {
		return ringNum;
	}
	public void setRingNum(Integer ringNum){
		this.ringNum = ringNum;
	}
	public void setTileID(int ID){
		tileID = ID;
	}
	public int getTileID(){
		return tileID;
	}
	
	//chkpoint locked booleans.
	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	//getting various arrayLists and hashmaps.
	public ArrayList<AdjacentTile> getAvailableAdjTiles() {
		return availableTiles;
	}

	public ArrayList<AdjacentTile> getUnAvailableAdjTiles() {
		return unavailableTiles;
	}

	public ArrayList<AdjacentTile> getStoredUnavailTiles() {
		return storedUnavailTiles;
	}

	public ArrayList<Position> getCellParts() {
		return cellPositions;
	}
	public ArrayList<Position> getPlayerParts(){
		return playerPositions;
	}

	public HashMap<Tile, AdjacentTile> getStoredAdjTiles() {
		return storedAvailTiles;
	}

	//setting Dlayer
	public void setDlayer(DataLayer dlayer){
		this.dlayer = dlayer;
	}
	
	public DataLayer getDlayer() {
		return dlayer;
	}

	
	public void setAdjacentTiles() {

		//case for center
		if (ringNum == 0) {
			ArrayList<Tile> tiles = findUpperAdjCenter();
			for (Tile tile : tiles) {
				AdjacentTile adjTile = new AdjacentTile(tile, true);
				availableTiles.add(adjTile);
			}
		}
		else {
			ArrayList<Tile> upperTiles = findUpperAdjTiles(this);
			ArrayList<Tile> lowerTiles = findLowerAdjTiles(this);
			
			for (Tile tile : upperTiles) {
				AdjacentTile adjTile = new AdjacentTile(tile, true);
				availableTiles.add(adjTile);
			}
			for (Tile tile : lowerTiles) {
				AdjacentTile adjTile = new AdjacentTile(tile, true);
				availableTiles.add(adjTile);
			}
			if (ringNum != 0 && ringNum != 7) {
				addAdjOnSameRing();
			}
		}
	}

	/**
	 * findUpperAdjCenter() is a function used only to find the upper adjacents for the center tile.
	 * @return an ArrayList of the upper adjacent tiles. Will return an empty list if the tile called on
	 * is NOT the center.
	 */
	public ArrayList<Tile> findUpperAdjCenter() {
		ArrayList<Tile> upperAdjs = new ArrayList<Tile>();
		if (ringNum == 0) {
			for (int i = 0; i < 4; i++) {
				upperAdjs.add(dlayer.getRingList().get(1).get(i));
			}
		}
		return upperAdjs;
	}
	

	public ArrayList<Tile> findUpperAdjTiles(Tile curTile) {
		Tile adjTile1 = null;
		Tile adjTile2 = null;
		int higherTileIdx;

		switch(curTile.getRingNum()) {
		case 1:
			higherTileIdx = curTile.getTileID() * 2;
			adjTile1 = dlayer.getRingList().get(curTile.getRingNum()+1).get(higherTileIdx);
			adjTile2 = dlayer.getRingList().get(curTile.getRingNum()+1).get(++higherTileIdx);
			break;
		case 2:
			//find tiles adjacent that are above ring 2.
			higherTileIdx = curTile.getTileID(); //there is one tile in ring 3 that has same tileID.
			adjTile2 = dlayer.getRingList().get(curTile.getRingNum()+1).get(higherTileIdx);

			//special case for first tile in ring 2, where upper is last tile in ring 3.
			if (curTile.getTileID() == 0) {
				higherTileIdx = dlayer.getRingList().get(curTile.getRingNum()+1).size() - 1;
			}
			else {
				higherTileIdx = curTile.getTileID() - 1;
			}
			adjTile1 = dlayer.getRingList().get(curTile.getRingNum()+1).get(higherTileIdx);
			break;
		case 3:
			//find tiles adjacent that are above ring 3: one will always be tileNum*2 + 1, and other will be +1 of it.
			higherTileIdx = (curTile.getTileID() * 2) + 1;
			adjTile1 = dlayer.getRingList().get(curTile.getRingNum()+1).get(higherTileIdx);

			//exception for last tile, where higherTileIdx is 0, not higherTileIdx + 1.
			if (curTile.getTileID() == dlayer.getRingList().get(curTile.getRingNum()).size() - 1) {
				higherTileIdx = 0;
			}
			else {
				higherTileIdx++;
			}
			adjTile2 = dlayer.getRingList().get(curTile.getRingNum()+1).get(higherTileIdx);
			break;
		case 4:
			//find tiles adjacent above ring 4: one will always be same as TileID. Other will be TileID-1.
			higherTileIdx = curTile.getTileID();
			adjTile2 = dlayer.getRingList().get(curTile.getRingNum()+1).get(higherTileIdx);

			//exception for when tileID = 0. Higher is actually last tile in ring 5.
			if (curTile.getTileID() == 0) {
				higherTileIdx = dlayer.getRingList().get(curTile.getRingNum()+1).size() - 1;
			}
			else {
				higherTileIdx--;
			}
			adjTile1 = dlayer.getRingList().get(curTile.getRingNum()+1).get(higherTileIdx);
			break;
		case 5:
			//find tiles adjacent above ring 5: one will be (tileID * 2) + 1, other will be +1 that.
			higherTileIdx = (curTile.getTileID()*2) + 1;
			adjTile1 = dlayer.getRingList().get(curTile.getRingNum()+1).get(higherTileIdx);

			//exception for last tile in ring 5, where next upper tile is 0.
			if (curTile.getTileID() == dlayer.getRingList().get(curTile.getRingNum()).size() - 1) {
				higherTileIdx = 0;
			}
			else {
				higherTileIdx++;
			}
			adjTile2 = dlayer.getRingList().get(curTile.getRingNum()+1).get(higherTileIdx);
			break;
		case 6:
			//find tiles above ring 6 for select tiles.
			if (curTile.getTileID() == ADJCHK1) {
				adjTile1 = dlayer.getRingList().get(curTile.getRingNum()+1).get(0);
			}
			else if (curTile.getTileID() == ADJCHK2) {
				adjTile1 = dlayer.getRingList().get(curTile.getRingNum()+1).get(1);
			}
			else if (curTile.getTileID() == ADJCHK3) {
				adjTile1 = dlayer.getRingList().get(curTile.getRingNum()+1).get(2);
			}
			else if (curTile.getTileID() == ADJCHK4) {
				adjTile1 = dlayer.getRingList().get(curTile.getRingNum()+1).get(3);
			}
		}
		ArrayList<Tile> upperAdjs = new ArrayList<Tile>();
		if (adjTile1 != null) {
			upperAdjs.add(adjTile1);
		}
		if (adjTile2 != null) {
			upperAdjs.add(adjTile2);
		}
		return upperAdjs;
	}

	public ArrayList<Tile> findLowerAdjTiles(Tile curTile) {
		Tile adjTile1 = null;
		Tile adjTile2 = null;
		int lowerTileIdx;

		switch(curTile.getRingNum()) {
		case 1:
			adjTile1 = dlayer.getRingList().get(ringNum-1).get(0);
			break;
		case 2:
			lowerTileIdx = tileID/2;
			adjTile1 = dlayer.getRingList().get(ringNum-1).get(lowerTileIdx);
			break;
		case 3:
			//find tiles below ring 3: one tile in ring2 with same TileID. Take TileID+1 too.
			lowerTileIdx = tileID;
			adjTile1 = dlayer.getRingList().get(ringNum-1).get(lowerTileIdx);

			//exception for last tile, where TileID+1 is 0 in ring2.
			if (tileID == dlayer.getRingList().get(ringNum).size() - 1) {
				lowerTileIdx = 0;
			}
			else {
				lowerTileIdx = tileID + 1;
			}
			adjTile2 = dlayer.getRingList().get(ringNum-1).get(lowerTileIdx);
			break;
		case 4:
			//find tiles adjacent that are below ring 4: after every two iterations, increment idx by 1.
			lowerTileIdx = 0;
			//exception for when tileID = 0. Lower is last tile in ring 3.
			if (tileID == 0) {
				lowerTileIdx = dlayer.getRingList().get(ringNum-1).size() - 1;
			}
			else {
				if (tileID % 2 == 0) {
					lowerTileIdx = (tileID/2) - 1;
				}
				else {
					lowerTileIdx = tileID/2;
				}
			}
			adjTile1 = dlayer.getRingList().get(ringNum-1).get(lowerTileIdx);
			break;
		case 5:
			//find tiles adjacent below ring 5: one will always be TileID. Other will be TileID+1.
			lowerTileIdx = tileID;
			adjTile1 = dlayer.getRingList().get(ringNum-1).get(lowerTileIdx);

			//exception for last tile in ring 5, where next lower tile is 0.
			if (tileID == dlayer.getRingList().get(ringNum).size() - 1) {
				lowerTileIdx = 0;
			}
			else {
				lowerTileIdx++;
			}
			adjTile2 = dlayer.getRingList().get(ringNum-1).get(lowerTileIdx);
			break;
		case 6:
			//find tiles adjacent below ring 6
			lowerTileIdx = 0;
			if (tileID == 0) {
				lowerTileIdx = 15;
			}
			else {
				if (tileID % 2 == 0) {
					lowerTileIdx = (tileID/2) - 1;
				}
				else {
					lowerTileIdx = tileID/2;
				}
			}
			adjTile1 = dlayer.getRingList().get(ringNum-1).get(lowerTileIdx);
			break;
		default:
			if (tileID == 0) {
				adjTile1 = dlayer.getRingList().get(ringNum-1).get(ADJCHK1);
			}
			else if (tileID == 1) {
				adjTile1 = dlayer.getRingList().get(ringNum-1).get(ADJCHK2);
			}
			else if (tileID == 2) {
				adjTile1 = dlayer.getRingList().get(ringNum-1).get(ADJCHK3);
			}
			else {
				adjTile1 = dlayer.getRingList().get(ringNum-1).get(ADJCHK4);
			}

		}
		ArrayList<Tile> lowerAdjs = new ArrayList<Tile>();
		if (adjTile1 != null) {
			lowerAdjs.add(adjTile1);
		}
		if (adjTile2 != null) {
			lowerAdjs.add(adjTile2);
		}
		return lowerAdjs;
	}


	public void addAdjOnSameRing() {
		AdjacentTile newTile;
		AdjacentTile newTile2;
		int lowerTileIdx = tileID - 1;
		int higherTileIdx = tileID + 1;
		if (tileID == 0) {
			lowerTileIdx = dlayer.getRingList().get(ringNum).size() - 1;
		}
		//System.out.println(this.ringNum);
		if (tileID == dlayer.getRingList().get(ringNum).size() - 1) {
			higherTileIdx = 0;
		}
		if (ringNum != 6) {
			newTile = new AdjacentTile(dlayer.getRingList().get(ringNum).get(lowerTileIdx), false);
			newTile2 = new AdjacentTile(dlayer.getRingList().get(ringNum).get(higherTileIdx), false);
			unavailableTiles.add(newTile);
			unavailableTiles.add(newTile2);
		}
		else {
			newTile = new AdjacentTile(dlayer.getRingList().get(ringNum).get(lowerTileIdx), true);
			newTile2 = new AdjacentTile(dlayer.getRingList().get(ringNum).get(higherTileIdx), true);
			availableTiles.add(newTile);
			availableTiles.add(newTile2);
		}
	}


	/**
	 * setPlayerParts() finds four locations within the tile to render the potential positions of up to four players.
	 */
	public void setPlayerParts(){
		double angle;
		double firstAngle;
		double midAngle;

		//special case for center.
		if (angleL == 0 && angleH == 360) {
			setParts("player", 0, 45, 20);
			setParts("player", 1, 135, 20);
			setParts("player", 2, 225, 20);
			setParts("player", 3, 315, 20);
		}
		else {
			firstAngle = getLowAngle();
			angle = (getHighAngle() - getLowAngle())/2; //get half of tile

			midAngle = firstAngle + (angle)/2;

			setParts("player", 0, midAngle, getOneEighthRadius());
			setParts("player", 1, midAngle + angle, getOneEighthRadius());
			setParts("player", 2, midAngle, getSevenEighthRadius());
			setParts("player", 3, midAngle + angle, getSevenEighthRadius());
		}
	}

	/**
	 * setCellParts() finds two locations (four for certain effects) within the tile to render up to two potential cells placed by players.
	 */
	public void setCellParts(boolean exception) {
		double angle;
		double firstAngle;
		double midAngle;

		firstAngle = getLowAngle();
		angle =(getHighAngle() - getLowAngle())/2;
		midAngle = firstAngle + (angle)/2;
		//exception for checkpoints and certain cards.
		if(exception){
			setParts("cell", 0, midAngle, getOneEighthRadius());
			setParts("cell", 1, midAngle + angle, getOneEighthRadius());
			setParts("cell", 2, midAngle, getSevenEighthRadius());
			setParts("cell", 3, midAngle + angle, getSevenEighthRadius());
		}else{
			setParts("cell", 0, midAngle, getOneHalfRadius());
			setParts("cell", 1, midAngle + angle, getOneHalfRadius());
		}

	}

	/**
	 * setRenderObjLoc() creates the location for the renderObj depending on the tile.
	 */
	public void setRenderObjLoc() {

		//special case for center
		if (angleL == 0 && angleH == 360) {
			renderObjLoc = new Position();
			renderObjLoc.setAngle(0);
			renderObjLoc.setPositionRadius(0);
		}
		else {
			renderObjLoc = new Position();
			renderObjLoc.setAngle((angleL + angleH)/2);
			renderObjLoc.setPositionRadius((radiusL + radiusH)/2);
		}
	}


	//set Parts for positions
	public void setParts(String name, int index, double angle, double rad){
		if(name.equals("player")){
			playerPositions.add(new Position());
			playerPositions.get(index).setAngle(angle);
			playerPositions.get(index).setPositionRadius(rad);
		}else if(name.equals("cell")){
			cellPositions.add(new Position());
			cellPositions.get(index).setAngle(angle);
			cellPositions.get(index).setPositionRadius(rad);
		}


	}

	//Get cell's position and occupy the position
	public Position getCellPosition(Cell cell) {
		numCells++;
		for(Position pos : cellPositions) {
			if (pos.getCell() == null) {
				pos.setCell(cell);
				return pos;
			}
		}
		return null; //means we have more than 2 cells in the tile, which should not happen!
	}

	//Take a cell off its respective position
	public void leaveCell(Cell cell) {
		numCells--;
		for (int i = 0; i < cellPositions.size(); i++) {
			if (cellPositions.get(i).getCell() == cell) {
				cellPositions.get(i).setCell(null);
			}
		}
	}

	//Get player's position and occupy the position
	public Position getPlayerPosition(Player player){
		numPlayers++;
		for(Position pos : playerPositions){
			if(pos.getPlayer() == null){
				pos.setPlayer(player);
				return pos;
			}
		}
		return null; //Means we have more than 4 players, which should not happen.
	}

	//Take a player off their respective position
	public void leavePlayer(Player player){
		numPlayers--;
		for(int i = 0 ; i < playerPositions.size(); i++){
			if(playerPositions.get(i).getPlayer() == player){
				playerPositions.get(i).setPlayer(null);
			}
		}
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	public int getNumCells() {
		return numCells;
	}
	
	public boolean hasCells() {
		return numCells > 0;
	}
	
	public boolean hasPlayers() {
		return numPlayers > 0;
	}

	public boolean isOccupied() {
		return hasCells() || hasPlayers();
	}

	//Used for sorting
	public Double getPosition(){
		position = getRingNum() + (getTileID()/100.0);
		return position;

	}

	public void addAdjTiles(Tile tile){
		AdjacentTile adjTile = new AdjacentTile(tile, true);
		availableTiles.add(adjTile);
		storedAvailTiles.put(tile, adjTile);
	}
	public void removeAdjTiles(Tile tile){
		availableTiles.remove(storedAvailTiles.get(tile));
	}
	public void addToCellStorage(Player player, Cell cell) {
		cellStorage.put(cell, player);
	}

//	public void removeFromCellStorage(Cell cell) {
//		cellStorage.remove(cell);
//	}
	
	/**
	 * addCell(2 params) adds one cell to targetTile for currentPlayer.
	 * @param currentPlayer is the player that plants the cell.
	 * @param gps is the GamePlayState instance where cell will be controlled.
	 */
	public void addCell(Player currentPlayer, GamePlayState gps) {
		Cell newCell = new Cell(currentPlayer, this);
		float[] cellCoords = gps.getTileFinder().angleToPixels(newCell.getCurrentPosition().getPositionRadius(), newCell.getCurrentPosition().getAngle());
		newCell.setCellCoords(cellCoords);
		newCell.setImage(gps.getCellImage(currentPlayer));
		currentPlayer.getCellList().add(newCell);
		// add element to player's repository of elements.
		int element = getElement();
		currentPlayer.addElement(element);
		// decrease cell count
		addToCellStorage(currentPlayer, newCell);
		currentPlayer.decrementCellLeft();
	}

	/**
	 * addCell (3 params) adds a variable amount of cells to targetTile for currentPlayer.
	 * @param currentPlayer is the player that plants the cell.
	 * @param targetTile is the tile where the cell will be located.
	 * @param gps is the GamePlayState instance where cell will be controlled.
	 * @param numToAdd is the number of cells to add to the tile.
	 */
	public void addCell(Player currentPlayer, GamePlayState gps, int numToAdd) {
		for (int i = 0; i < numToAdd; i++) {
			Cell newCell = new Cell(currentPlayer, this);
			float[] cellCoords = gps.getTileFinder().angleToPixels(newCell.getCurrentPosition().getPositionRadius(), newCell.getCurrentPosition().getAngle());
			newCell.setCellCoords(cellCoords);
			newCell.setImage(gps.getCellImage(currentPlayer));
			currentPlayer.getCellList().add(newCell);
			// add element to player's repository of elements.
			int element = getElement();
			currentPlayer.addElement(element);
			// decrease cell count
			addToCellStorage(currentPlayer, newCell);
			currentPlayer.decrementCellLeft();
		}
	}

	public boolean removeCell(Player player, boolean defaultSound) {
		boolean found = false;
		if (cellStorage.size() != 0) {
			for (Iterator<Cell> iter = cellStorage.keySet().iterator(); iter.hasNext(); ) {
				Cell nextCell = iter.next();
				if (player == cellStorage.get(nextCell)) {
					if(defaultSound){
						removeCell.play();
					}
					player.incrementCellLeft();
					player.removeElement(getElement());
					leaveCell(nextCell);
					player.getCellList().remove(nextCell);
					iter.remove();
					found = true;
					break;
				}
			}
		}
		//went through entire cellStorage and not found or size == 0.
		return found;
	}
	
	//remove all cells for specific player.
	public void removeAllCells(Player player) {
		if (cellStorage.size() != 0) {
			for (Iterator<Cell> iter = cellStorage.keySet().iterator(); iter.hasNext(); ) {
				Cell nextCell = iter.next();
				if (player == cellStorage.get(nextCell)) {
					player.incrementCellLeft();
					player.removeElement(getElement());
					leaveCell(nextCell);
					player.getCellList().remove(nextCell);
					iter.remove();
				}
			}
		}
	}
	
	//remove all cells for all players.
	public void removeAllCells(Tile tile, GamePlayState gps) {
		for (Player player : gps.getPlayers())
			tile.removeAllCells(player);
	}
	
	//find how many cells inside a certain tile.
	public int findNumCells(Player player) {
		int playerCells = 0;
		for (Iterator<Cell> iter = cellStorage.keySet().iterator(); iter.hasNext(); ) {
			if (player == cellStorage.get(iter.next())) {
				playerCells++;
			}
		}
		return playerCells;
	}
	
	public boolean containsCell(Player player) {
		for (Iterator<Cell> iter = cellStorage.keySet().iterator(); iter.hasNext(); ) {
			if (player == cellStorage.get(iter.next())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(angleH);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(angleL);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(radiusH);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(radiusL);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((ringNum == null) ? 0 : ringNum.hashCode());
		result = prime * result + tileID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tile other = (Tile) obj;
		if (Double.doubleToLongBits(angleH) != Double.doubleToLongBits(other.angleH))
			return false;
		if (Double.doubleToLongBits(angleL) != Double.doubleToLongBits(other.angleL))
			return false;
		if (Double.doubleToLongBits(radiusH) != Double.doubleToLongBits(other.radiusH))
			return false;
		if (Double.doubleToLongBits(radiusL) != Double.doubleToLongBits(other.radiusL))
			return false;
		if (ringNum == null) {
			if (other.ringNum != null)
				return false;
		} else if (!ringNum.equals(other.ringNum))
			return false;
		if (tileID != other.tileID)
			return false;
		return true;
	}

}
