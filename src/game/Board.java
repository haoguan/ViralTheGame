package game;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Board is the repository class that initializes all the tiles,
 * elements for each tile, and establishes the boundaries between the rings.
 * @author Hao Guan and Jonathan Wu
 */
public class Board {

	//HashMap with keys being a 4 element (Quadruple) that stores the 4 values that define a tile.
//	HashMap<Quadruple, Tile> tiles = new HashMap<Quadruple, Tile>();
	public final static int FIRE = 0;
	public final static int WATER = 1;
	public final static int WIND = 2;
	public final static int EARTH = 3;
	public final static int BLANK = 4;
	public final static int SPELLCARD = 5;
	public final static int CLASSCARD = 6;
	public final static int CHECKPOINTS = 7;

	double[] radiiBounds = new double[9];
	double[] ringOneBounds = new double[5];
	double[] ringTwoBounds = new double[9];
	double[] ringThreeBounds = new double[9];
	double[] ringFourBounds = new double[17];
	double[] ringFiveBounds = new double[17];
	double[] ringSixBounds = new double[33];
	double[] ringSevenBounds = {33.75, 45, 135, 146.25, 213.75, 225, 315, 326.25};

	double centerStart;
	double centerEnd;

	double startRadius; //this changes after incrementRad()
	double endRadius; //this changes after incrementRad()
	double increment;
	int elemRingKey = 0;
	int ringKey = 0;
	int tileID = 0;

	DataLayer dlayer;

	/**
	 * The one parameter constructor requires a DataLayer object
	 * to store the tiles in a shared environment for other
	 * classes to access.
	 * @param dlayer is the shared DataLayer object.
	 */
	public Board(DataLayer dlayer) {
		this.dlayer = dlayer;
		centerStart = dlayer.getStartRadius();
		centerEnd = dlayer.getEndRadius();
		increment = dlayer.getRingIncrement();
		endRadius = dlayer.getEndRadius();
		startRadius = dlayer.getStartRadius();
		storeTiles();
		storeBounds();
	}


	/**
	 * getTiles() returns the datalayer's HashMap of tiles.
	 * @return a shared HashMap of tiles.
	 */
	public HashMap<Quadruple,Tile> getTiles() {
		return dlayer.getTiles();
	}

	/**
	 * getRadiiBounds() returns the array of radius
	 * bounds in pixels.
	 * @return the array of double radius bounds
	 */
	public double[] getRadiiBounds() {
		return radiiBounds;
	}

	/**
	 * getRingOneBounds() returns the array of
	 * angle bounds in the first ring.
	 * @return the double array of angle bounds.
	 */
	public double[] getRingOneBounds() {
		return ringOneBounds;
	}
	
	/**
	 * getRingTwoBounds() returns the array of
	 * angle bounds in the second ring.
	 * @return the double array of angle bounds.
	 */
	public double[] getRingTwoBounds() {
		return ringTwoBounds;
	}

	/**
	 * getRingThreeBounds() returns the array of
	 * angle bounds in the third ring.
	 * @return the double array of angle bounds.
	 */
	public double[] getRingThreeBounds() {
		return ringThreeBounds;
	}

	/**
	 * getRingFourBounds() returns the array of
	 * angle bounds in the fourth ring.
	 * @return the double array of angle bounds.
	 */
	public double[] getRingFourBounds() {
		return ringFourBounds;
	}

	/**
	 * getRingFiveBounds() returns the array of
	 * angle bounds in the fifth ring.
	 * @return the double array of angle bounds.
	 */
	public double[] getRingFiveBounds() {
		return ringFiveBounds;
	}

	/**
	 * getRingSixBounds() returns the array of
	 * angle bounds in the sixth ring.
	 * @return the double array of angle bounds.
	 */
	public double[] getRingSixBounds() {
		return ringSixBounds;
	}

	/**
	 * getRingSevenBounds() returns the array of
	 * angle bounds in the seventh ring.
	 * @return the double array of angle bounds.
	 */
	public double[] getRingSevenBounds() {
		return ringSevenBounds;
	}

	/**
	 * getCenterStart() returns the dlayer
	 * radius beginning value of the center circle.
	 * @return the center of the board in pixels.
	 */
	public double getCenterStart() {
		return centerStart;
	}

	/**
	 * getCenterEnd() returns the dlayer
	 * radius end value of the center circle.
	 * @return the center of the board in pixels.
	 */
	public double getCenterEnd() {
		return centerEnd;
	}

	/**
	 * getIncrement() returns increment value between
	 * each of the rings.
	 * @return the number of pixels between each ring.
	 */
	public double getIncrement() {
		return increment;
	}


	/**
	 * storeBounds() stores values in the class variables for radiiBounds and ringOne-ringSixBounds
	 */
	private void storeBounds() {
		//store radiiBounds
		double increment = dlayer.getEndRadius(); //special case for center;
		radiiBounds[0] = 0;
		for (int ii = 1; ii < radiiBounds.length; ii++) {
			radiiBounds[ii] = increment;
			increment+=dlayer.getRingIncrement();
			increment = roundTwoDecimals(increment);
		}

		//store ringOneBounds
		increment = 0;
		for (int ii = 0; ii < ringOneBounds.length; ii++) {
			ringOneBounds[ii] = increment;
			increment += 90;
		}

		//store ringTwoBounds
		increment = 0;
		for (int ii = 0; ii < ringTwoBounds.length; ii++) {
			ringTwoBounds[ii] = increment;
			increment+=45;
		}

		//store ringThreeBounds
		increment = 22.5;
		for (int ii = 0; ii < ringThreeBounds.length; ii++) {
			ringThreeBounds[ii] = increment;
			increment+=45;
		}

		//store ringFourBounds
		increment = 0;
		for (int ii = 0; ii < ringFourBounds.length; ii++) {
			ringFourBounds[ii] = increment;
			increment+=22.5;
		}

		//store ringFiveBounds
		increment = 11.25;
		for (int ii = 0; ii < ringFiveBounds.length; ii++) {
			ringFiveBounds[ii] = increment;
			increment+=22.5;
		}

		//store ringSixBounds
		increment = 0;
		for (int ii = 0; ii < ringSixBounds.length; ii++) {
			ringSixBounds[ii] = increment;
			increment+=11.25;
		}

	}

	/**
	 * storeTiles() stores values in the HashMap tiles.
	 */
	private void storeTiles() {
		double[] allAngles = {360,90,45,45,22.5,22.5,11.25};

		setTiles(0,360); //center
		incrementRad();
		setTiles(0,90); //ring 1
		incrementRad();

		setTiles(0,45); //ring 2
		setTwoElements(45,90, CLASSCARD, CLASSCARD);
		setTwoElements(90,135, SPELLCARD, SPELLCARD);
		incrementRad(); 

		setTiles(22.5,45); //ring 3
		setFourElements(22.5,67.5, WATER,EARTH,FIRE,WIND);
		incrementRad();

		setTiles(0,22.5); //ring 4
		setFourElements(22.5,45, CLASSCARD,CLASSCARD,CLASSCARD,CLASSCARD);
		setFourElements(45,67.5, SPELLCARD,SPELLCARD,SPELLCARD,SPELLCARD);
		incrementRad();

		setTiles(11.25,22.5); //ring 5
		setFourElements(33.75,56.25, FIRE ,WIND, WATER, EARTH);
		incrementRad();

		setTiles(0,11.25); //ring 6
		setFourElements(11.25,22.5, EARTH,WATER, EARTH, WATER);
		setFourElements(67.5,78.75, WIND,FIRE, WIND, FIRE);
		incrementRad();

		setCheckpoints(); //ring 7
	}


	/**
	 * setTiles() is a helper function that loops through each
	 * tile in a ring and sets the appropriate fields.
	 * @param start is the beginning angle of the ring.
	 * @param angle is the increment angle in the ring.
	 */
	public void setTiles(double start, double angle){
		ArrayList<Tile> tileList = new ArrayList<Tile>();
		for(double i = start; i < 360; i+= angle){
			Tile tile = new Tile(i, i+angle,startRadius, endRadius);

			tile.setRingNum(ringKey);
			tile.setTileID(tileID);
			tile.setDlayer(dlayer);
			tile.setPlayerParts();
			tile.setCellParts(false);
			tile.setRenderObjLoc();
			Quadruple key = new Quadruple(i, i+angle,startRadius, endRadius);
			dlayer.getTiles().put(key, tile);
			tileList.add(tile);

			tileID++;
		}
		dlayer.setRingList(ringKey++, tileList);
		tileID = 0;
	}
	
	/**
	 * setCheckpoints() is a special function that initializes the checkpoint
	 * tiles and its associated fields.
	 */
	public void setCheckpoints(){
		ArrayList<Tile> tileList = new ArrayList<Tile>();
		double angleA;
		double angleB;
		for(int i = 0; i < ringSevenBounds.length; i++){
			angleA = ringSevenBounds[i];
			angleB = ringSevenBounds[++i];

			Tile tile = new Tile(angleA, angleB,startRadius, endRadius);

			tile.setRingNum(ringKey);
			tile.setTileID(tileID);
			tile.setElement(Tile.CHECKPOINT);
			tile.setDlayer(dlayer);
			tile.setPlayerParts();
			tile.setCellParts(true);
			tile.setLocked(true);

			Quadruple key = new Quadruple(angleA, angleB,startRadius, endRadius);
			dlayer.getTiles().put(key, tile);
			dlayer.getTiles().get(key).setElement(CHECKPOINTS);
			tileList.add(tile);

			tileID++;
		}
		dlayer.setRingList(ringKey++, tileList);
		tileID = 0;
	}


	/**
	 * setTwoElements() is a helper function that finds two tiles in a ring
	 * and sets the elements according to the int parameters.
	 * @param curAngleL is the low angle of the first element tile.
	 * @param curAngleH is the high angle of the first element tile.
	 * @param element is the element for the first element tile.
	 * @param element2 is the element for the second element tile.
	 */
	public void setTwoElements(double curAngleL, double curAngleH, int element, int element2){
		double curRadiusL = startRadius;
		double curRadiusH = endRadius;
		
		//check if need new list.
		ArrayList<Tile> currElems = dlayer.getElements().get(elemRingKey);
		if (currElems == null)
			currElems = new ArrayList<Tile>();
		Quadruple setColor = new Quadruple(curAngleL, curAngleH, curRadiusL, curRadiusH);
		//add first element.
		Tile eTile = dlayer.getTiles().get(setColor);
		eTile.setElement(element);
		currElems.add(eTile);
		//find second in ring.
		setColor.setAngleL(curAngleL+180);
		setColor.setAngleH(curAngleH+180);
		eTile = dlayer.getTiles().get(setColor);
		eTile.setElement(element2);
		currElems.add(eTile);
		
		dlayer.setElementList(elemRingKey, currElems);
	}


	/**
	 * setFourElements() is a helper functions that finds four tiles in a ring
	 * and setst he elements according to the int parameters.
	 * @param curAngleL is the low angle of the first element tile.
	 * @param curAngleH is the high angle of the first element tile.
	 * @param element is the element for the first element tile.
	 * @param element2 is the element for the second element tile.
	 * @param element3 is the element for the third element tile.
	 * @param element4 is the element for the fourth element tile.
	 */
	public void setFourElements(double curAngleL, double curAngleH, int element, int element2, int element3, int element4){
		double curRadiusL = startRadius;
		double curRadiusH = endRadius;
		
		//check if need new list.
		ArrayList<Tile> currElems = dlayer.getElements().get(elemRingKey);
		if (currElems == null)
			currElems = new ArrayList<Tile>();
		Quadruple setColor = new Quadruple(curAngleL, curAngleH, curRadiusL, curRadiusH);

		//add first element
		Tile eTile = dlayer.getTiles().get(setColor);
		eTile.setElement(element);
		currElems.add(eTile);
		//add second in ring.
		setColor.setAngleL(curAngleL+=90);
		setColor.setAngleH(curAngleH+=90);
		eTile = dlayer.getTiles().get(setColor);
		eTile.setElement(element2);
		currElems.add(eTile);
		//add third in ring.
		setColor.setAngleL(curAngleL+=90);
		setColor.setAngleH(curAngleH+=90);
		eTile = dlayer.getTiles().get(setColor);
		eTile.setElement(element3);
		currElems.add(eTile);
		//add fourth in ring.
		setColor.setAngleL(curAngleL+=90);
		setColor.setAngleH(curAngleH+=90);
		eTile = dlayer.getTiles().get(setColor);
		eTile.setElement(element4);
		currElems.add(eTile);
		
		dlayer.setElementList(elemRingKey, currElems);
	}


	/**
	 * incrementRad() is a helper function that increments
	 * the radius and ring value as storeTiles() looks at each ring.
	 */
	public void incrementRad(){
		startRadius = endRadius;
		endRadius += increment;
		endRadius = roundTwoDecimals(endRadius);
		elemRingKey++;
	}

	/**
	 * roundTwoDecimals() is a helper function that rounds
	 * a double value to two decimals. It is mainly used
	 * for consistency throughout the board.
	 * @param d is the double value to be rounded.
	 * @return a double value with only two decimal places.
	 */
	double roundTwoDecimals(double d) {
    	DecimalFormat twoDForm = new DecimalFormat("#.##");
    	return Double.valueOf(twoDForm.format(d));
	}
}
