package SpellCards;

import game.DataLayer;
import game.Tile;
import main.GamePlayState;
import main.GamePlayState.STATES;

public class ElementalDistortion extends ActivateSpell {
	
	public ElementalDistortion(DataLayer dlayer, GamePlayState gps) {
		this.dlayer = dlayer;
		this.gps = gps;
	}
	
	@Override
	public boolean runEffect() {
		resetDefaultState();
		init thread = new init(dlayer, gps);
		thread.start();
		while (!checkSuccess) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {}
		}
		return isSuccessRun();
	}
	
	class init extends Thread{
		
		DataLayer dlayer;
		GamePlayState gps;
		
		public init(DataLayer dlayer, GamePlayState gps) {
			this.dlayer = dlayer;
			this.gps = gps;
		}
		
		public void run() {
			STATES returnState = gps.getCurrentState();
			Tile targetTile = null;
			Tile lastTargetTile = null;
			int origElement = -1;
			boolean selected = false;
			boolean first = true;
			while(active){
				try {
					if (first) {
						playergui.setTextPane("Select an element tile to convert.\n");
						gps.setNewInput(false);
						gps.setState(STATES.USE_SPELL_STATE);
						while (!gps.getNewInput()) {
							sleep(5);
						}
						targetTile = gps.getSpellTargetTile();
						
						//conditions for eligibility.
						if (targetTile.getElement() >= Tile.BLANK) {
							gps.setState(returnState);
							failureToPlay("You selected an invalid tile.\n");
						}
						else {
							setCheckSuccess(true);
							lastTargetTile = targetTile;
							playergui.setTextPane("Select a tile with the element you want.\n");
							
							while (!selected) {
								gps.setNewInput(false);
								while (!gps.getNewInput()) {
									sleep(5);
								}
								targetTile = gps.getSpellTargetTile();
								
								if (targetTile.getElement() >= Tile.BLANK) {
									playergui.setTextPane("Select a valid tile.\n");
								}
								else {
									selected = true;
									origElement = lastTargetTile.getElement();
									lastTargetTile.setElement(targetTile.getElement());
									storeRenderObject(targetTile.getElement(), lastTargetTile, gps.getElementImage(targetTile.getElement()));
									writeToAllPlayers("Tile has been converted to a new element.\n");
									gps.setState(returnState);
									first = false;
								}
							}	
						}
					}
					
					if (gps.getCurrentState() == STATES.CHANGE_PLAYER_STATE) {
						removeRenderObject(lastTargetTile.getElement(), lastTargetTile);
						lastTargetTile.setElement(origElement);
						writeToAllPlayers("Elemental Distortion has worn out.\n");
						setActive(false);
					}
					else {
						sleep(5);
					}
				} catch (InterruptedException e) {}	
			}
			return;
		}
	}
}
