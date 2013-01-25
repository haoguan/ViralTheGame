package ClassCards;

import game.DataLayer;
import game.Player;
import game.PlayerGui;
import interm.SpellFinder;

import java.util.ArrayList;

import main.GamePlayState;
import main.GamePlayState.STATES;

public abstract class ActivateClass {
	PlayerGui playergui;
	Player player;
	boolean exist = true;	
	DataLayer dlayer;
	GamePlayState gps;
	String name;
	ArrayList<STATES> playableState;
	boolean metRequirements = false;
	boolean information = true;
	boolean counter = false;
	boolean targetRequired;
	SpellFinder spellFinder;
	
	public void setPlayableState(ArrayList<STATES> state) {
		playableState = state;
	}
	public ArrayList<STATES> getPlayableState() {
		return playableState;
	}
	public void setDlayer(DataLayer dlayer){
		this.dlayer = dlayer;
	}
	
	public void setGamePlayState(GamePlayState gps){
		this.gps = gps;
	}
	
	public void setExist(boolean status){
		exist = status;
	}
	
	public void setPlayerGui(PlayerGui playergui) {
		this.playergui = playergui;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	public void resetRolled(){
		playergui.setMoveButton(false);
		dlayer.getPossibleMoves().clear();
		dlayer.getPossibleMovesPaths().clear();
		gps.setState(STATES.ROLL_STATE);
	}
	public boolean isMetRequirements(){
		return metRequirements;
	}
	public void setMetRequirements(boolean status){
		metRequirements = status;
	}
	public boolean isInformation() {
		return information;
	}
	public void setInformation(boolean information) {
		this.information = information;
	}
	public void writeInfo(){
		if(information){
			for(Player eachPlayer : gps.getPlayers()){
				eachPlayer.getPlayerGui().setTextPane("Player " + player.getColor() + " has become a "+name +"\n");
			}
			information = false;
		}
	}
	public boolean isCounter(){
		return counter;
	}
	public void setCounter(boolean counter){
		this.counter = counter;
	}
	public void setSpellFinder(SpellFinder sf){
		spellFinder = sf;
	}
	public void setTargetRequired(boolean status) {
		targetRequired = status;
	}
	public boolean getTargetRequired() {
		return targetRequired;
	}
	public void resetTargetList() {
		playergui.setPlayerList(false);
		playergui.playerList.setSelectedIndex(0);
	}
	public abstract void runEffect();
}
