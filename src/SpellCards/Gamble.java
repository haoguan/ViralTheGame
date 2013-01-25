package SpellCards;

import game.DataLayer;
import game.Tile;
import main.GamePlayState;
import main.GamePlayState.STATES;

public class Gamble extends ActivateSpell {
	
	DataLayer dlayer;
	GamePlayState gps;
	
	public Gamble (DataLayer dlayer, GamePlayState gps) {
		this.dlayer = dlayer;
		this.gps = gps;
	}

	@Override
	public boolean runEffect() {
		resetDefaultState();
		init thread = new init(dlayer, gps);
		thread.start();
		return isSuccessRun(); //no way for this card to fail.
	}
	
	class init extends Thread{
		
		DataLayer dlayer;
		GamePlayState gps;
		
		public init(DataLayer dlayer, GamePlayState gps) {
			this.dlayer = dlayer;
			this.gps = gps;
		}
		
		public void run(){
			STATES returnState = gps.getCurrentState();
			Tile targetTile;
			try {
				while(active){
					playergui.setTextPane("Please roll the die.\n");
					playergui.setRolled(false);
					playergui.setRollDice(true);
					gps.setState(STATES.ROLL_STATE);					
					while (gps.getCurrentState() != STATES.PRE_MOVEMENT_STATE) {
						sleep(5);
					}
					int roll = playergui.getPlayer().getRoll();
					gps.setState(STATES.USE_SPELL_STATE);
					int numCheckpoints = playergui.getPlayer().getCheckPoints();
					int halfChkpt = Math.round((float)numCheckpoints/2);
					//greater than 3, halve checkpoint tiles. 
					if (roll >= 3) {
						playergui.setTextPane("Simply unfortunate.\n");
						playergui.setTextPane("Choose " + halfChkpt + " checkpoint cells to remove.\n");
						while (halfChkpt > 0) {
							gps.setNewInput(false);
							while (!gps.getNewInput()) {
								sleep(5);
							}
							targetTile = gps.getSpellTargetTile();
							if (targetTile.getElement() != Tile.CHECKPOINT || !targetTile.containsCell(playergui.getPlayer())) {
								playergui.setTextPane("The tile is not an appropriate target.\n");
							}
							else {
								targetTile.removeCell(playergui.getPlayer(), true);
								halfChkpt--;
							}
						}
						playergui.setTextPane("Successfully removed cells.\n");
					}
					//less than 3, double checkpoint tiles.
					else {
						playergui.setTextPane("It is your lucky day!\n");
						playergui.setTextPane("Choose " + halfChkpt + " checkpoint tiles to place new cells.\n");
						while (halfChkpt > 0) {
							gps.setNewInput(false);
							while (!gps.getNewInput()) {
								sleep(5);
							}
							targetTile = gps.getSpellTargetTile();
							if (targetTile.getElement() != Tile.CHECKPOINT) {
								playergui.setTextPane("Please select a checkpoint tile.\n");
							}
							else if (targetTile.getNumCells() == Tile.CHKPT_MAX_CELLS) {
								playergui.setTextPane("Checkpoint tile is already full.\n");
							}
							else {
								targetTile.addCell(playergui.getPlayer(), gps);
								halfChkpt--;
							}
						}
						playergui.setTextPane("Successfully added cells.\n");
					}
					gps.setState(returnState);
					setActive(false);
				}
				return;
			} catch (InterruptedException e) {}
		}
	}
}