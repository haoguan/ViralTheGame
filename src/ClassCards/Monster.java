package ClassCards;

import java.util.Iterator;

import game.Cell;
import game.Tile;

public class Monster extends ActivateClass {
	boolean Monster = true;
	boolean changeState = true;
	boolean checkExist = false;

	public void runEffect() {
		setTargetRequired(true);
		init thread = new init();
		name = "Monster";
		thread.start();
	}

	class init extends Thread {
		Tile targetTile;

		public void run() {
			while (exist) {
				try {
					if (gps.getCurrentPlayer().getEarthCount() >= 0 && gps.getCurrentPlayer().getClassCards().contains(name)) {
						writeInfo();
						setMetRequirements(true);
						if (playergui.getClassActivate()) {
							if (player.getCurrentTile().removeCell(playergui.getTargetPlayer(), true) && playergui.getTargetPlayer() != player) {
								playergui.setTextPane("Cell has been destroy!\n");
								playergui.setPlantCell(false);
							} else {
								playergui.setTextPane("Cannot destroy cell! Choose another player.\n");
							}
							playergui.setClassActivate(false);
						}
						
					} else {
						sleep(5);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
