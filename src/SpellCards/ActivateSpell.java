package SpellCards;

import game.DataLayer;
import game.Player;
import game.PlayerGui;
import game.RenderObject;
import game.SpellCard;
import game.Tile;

import java.util.ArrayList;

import main.GamePlayState;
import main.GamePlayState.STATES;

import org.newdawn.slick.Image;

public abstract class ActivateSpell extends SpellCard {
	
	PlayerGui playergui;
	ArrayList<STATES> playableState;
	GamePlayState gps;
	DataLayer dlayer;
	boolean active = true; //boolean for thread-like cards that extend multiple turns. Must need to reset this everytime.
	boolean checkSuccess = false; //used for cards that last many many turns and can fail.
	boolean successfulRun = true;
	boolean immediateActivate = false;
	boolean targetRequired = false; //defaults to false, certain cards will change this.
	boolean counter;
	
	public void setActive(boolean status) {
		active = status;
	}
	public void setPlayerGui(PlayerGui playergui) {
		this.playergui = playergui;
	}
	
	public void setDlayer(DataLayer dlayer){
		this.dlayer = dlayer;
	}
	
	public void setGamePlayState(GamePlayState gps){
		this.gps = gps;
	}
	
	//this function dictates which states the card is allowed to be played. 
	public void setPlayableState(ArrayList<STATES> state) {
		playableState = state;
	}
	
	public void setTargetRequired(boolean status) {
		targetRequired = status;
	}
	
	public void setSuccessRun(boolean status) {
		successfulRun = status;
	}
	
	public boolean isCheckSuccess() {
		return checkSuccess;
	}
	public void setCheckSuccess(boolean checkSuccess) {
		this.checkSuccess = checkSuccess;
	}
	public ArrayList<STATES> getPlayableState() {
		return playableState;
	}
	
	public boolean getTargetRequired() {
		return targetRequired;
	}
	
	public boolean isSuccessRun() {
		return successfulRun;
	}
	
	public boolean isImmediateActivate() {
		return immediateActivate;
	}
	public void setImmediateActivate(boolean immediateActivate) {
		this.immediateActivate = immediateActivate;
	}
	
	public void resetTargetList() {
		playergui.setPlayerList(false);
		playergui.playerList.setSelectedIndex(0);
	}
	
	public void resetRolled() {
		dlayer.getPossibleMoves().clear();
		dlayer.getPossibleMovesPaths().clear();
		gps.setState(STATES.ROLL_STATE);
	}
	
	public void resetDefaultState() {
		active = true; //boolean for thread-like cards that extend multiple turns. Must need to reset this everytime.
		successfulRun = true;
		immediateActivate = false;
		checkSuccess = false;
		targetRequired = false; //defaults to false, certain cards will change this.
	}
	
	public void failureToPlay(String message) {
		playergui.setTextPane(message);
		setSuccessRun(false);
		setActive(false);
		setCheckSuccess(true);
	}
	
	//Writes to every player gui with a customized string.
	public void writeToAllPlayers(String msg) {
		Player[] players = gps.getPlayers();
		for (Player player : players) {
			player.getPlayerGui().setTextPane(msg);
		}
	}
	
	public void storeRenderObject(int key, Tile targetTile, Image image) {
		float[] coords = gps.getTileFinder().angleToPixels(targetTile.getRenderObjLoc().getPositionRadius(), targetTile.getRenderObjLoc().getAngle());
		RenderObject obj = new RenderObject(key, targetTile, image, coords);
		targetTile.setRenderObj(obj);
		dlayer.getRenderObjectsArray().add(obj);
		if (!dlayer.getRenderObjects().containsKey(key)) {
			ArrayList<RenderObject> newList = new ArrayList<RenderObject>();
			newList.add(obj);
			dlayer.getRenderObjects().put(key, newList);
		}
		else {
			ArrayList<RenderObject> currList = dlayer.getRenderObjects().get(key);
			currList.add(obj);
		}
	}
	
	public void storeBeaconObject(int key, Tile targetTile, Image image) {
		float[] coords = gps.getTileFinder().angleToPixels(targetTile.getRenderObjLoc().getPositionRadius(), targetTile.getRenderObjLoc().getAngle());
		RenderObject obj = new RenderObject(key, targetTile, image, coords);
		targetTile.setBeaconObj(obj);
		dlayer.getRenderObjectsArray().add(obj);
		if (!dlayer.getRenderObjects().containsKey(key)) {
			ArrayList<RenderObject> newList = new ArrayList<RenderObject>();
			newList.add(obj);
			dlayer.getRenderObjects().put(key, newList);
		}
		else {
			ArrayList<RenderObject> currList = dlayer.getRenderObjects().get(key);
			currList.add(obj);
		}
	}
	
	public void removeRenderObject(int key, Tile targetTile) {
		RenderObject toRemove = targetTile.getRenderObj();
		dlayer.getRenderObjects().get(key).remove(toRemove);
		dlayer.getRenderObjectsArray().remove(toRemove);
		targetTile.setRenderObj(null);
	}
	public boolean isCounter(){
		return counter;
	}
	public void setCounter(boolean counter){
		this.counter = counter;
	}
	public abstract boolean runEffect();
	
}
