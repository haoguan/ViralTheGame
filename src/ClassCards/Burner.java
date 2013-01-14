package ClassCards;

import main.GamePlayState.STATES;
import game.Tile;

public class Burner extends ActivateClass {

	public void runEffect() {
		init thread = new init();
		name = "Burner";
		thread.start();
	}

	class init extends Thread {

		public void run() {
			
			Tile targetTile;
			int cellCount = 0;
			while (exist) {
				
				try {
					if (player.getFireCount() >= 0 && player.getClassCards().contains(name)) {
						writeInfo();
						setMetRequirements(true);
						setCounter(true);
						
						if (playergui.getClassActivate()) {
							
							System.out.println(player.getCellsLeft());
							if (player.getCellsLeft() <= 9) {
								playergui.setTextPane("Pick three cells to sacrafice\n");
								spellFinder.getGracePeriod().pauseTime();
							} else {
								playergui.setTextPane("Not enough cells\n");
							}
							if (player.getCellsLeft() <= 9) {
								STATES returnState = gps.getCurrentState();
								gps.setState(STATES.USE_SPELL_STATE);
								while (cellCount != 1) {
									gps.setTargetTile(null);
									
									while (gps.getTargetTile() == null) {
										sleep(5);
									}
									targetTile = gps.getTargetTile();
									if (targetTile.removeCell(player)) {
										cellCount++;
									} else {
										playergui.setTextPane("You do not have a cell here\n");
									}
								}
								spellFinder.getGracePeriod().resumeTime();
								spellFinder.setImmune();
								gps.setState(returnState);
								cellCount = 0;
							}
						}
						playergui.setClassActivate(false);
						
						sleep(5);

					} else {
						setCounter(false);
						setMetRequirements(false);
						sleep(5);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
