package game;

import java.util.ArrayList;
import java.util.HashMap;

public class DataLayer {
	double ringIncrement = 35;
	double startRadius = 0;
	double endRadius = 40;
	
	HashMap<Quadruple, Tile> tiles = new HashMap<Quadruple, Tile>();
	HashMap<Integer, ArrayList<Tile>> ringList = new HashMap<Integer, ArrayList<Tile>>();
	HashMap<Integer, ArrayList<Tile>> elementsList = new HashMap<Integer, ArrayList<Tile>>();
	HashMap<Tile, ArrayList<ArrayList<Tile>>> possibleMovesPaths = new HashMap<Tile, ArrayList<ArrayList<Tile>>>();
	
	HashMap<Integer, ArrayList<RenderObject>> renderObjects = new HashMap<Integer, ArrayList<RenderObject>>();
	ArrayList<RenderObject> renderObjectsArray = new ArrayList<RenderObject>(); //used for iteration in rendering.
	ArrayList<Tile> possibleMoves = new ArrayList<Tile>();
	
	
	public HashMap<Integer, ArrayList<Tile>> getElements() {
		return elementsList;
	}
	public  HashMap<Quadruple, Tile> getTiles() {
		return tiles;
	}
	public HashMap<Tile, ArrayList<ArrayList<Tile>>> getPossibleMovesPaths() {
		return possibleMovesPaths;
	}
	public HashMap<Integer, ArrayList<Tile>> getRingList() {
		return ringList;
	}
	public void setRingList(Integer key, ArrayList<Tile> tileList) {
		ringList.put(key, tileList);
	}
	public void setElementList(Integer key, ArrayList<Tile> elementList) {
		elementsList.put(key, elementList);
	}
	public double getRingIncrement() {
		return ringIncrement;
	}
	public void setRingIncrement(double ringIncrement) {
		this.ringIncrement = ringIncrement;
	}
	public double getStartRadius() {
		return startRadius;
	}
	public void setStartRadius(double startRadius) {
		this.startRadius = startRadius;
	}
	public double getEndRadius() {
		return endRadius;
	}
	public void setEndRadius(double endRadius) {
		this.endRadius = endRadius;
	}
	public ArrayList<Tile> getPossibleMoves() {
		return possibleMoves;
	}
	
	public HashMap<Integer, ArrayList<RenderObject>> getRenderObjects() {
		return renderObjects;
	}
	public void setRenderObjects(HashMap<Integer, ArrayList<RenderObject>> renderObjects) {
		this.renderObjects = renderObjects;
	}
	public ArrayList<RenderObject> getRenderObjectsArray() {
		return renderObjectsArray;
	}
	public void setRenderObjectsArray(ArrayList<RenderObject> renderObjectsArray) {
		this.renderObjectsArray = renderObjectsArray;
	}
}
