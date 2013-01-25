package ClassCards;

import java.util.Iterator;

import main.GamePlayState;
import main.GamePlayState.STATES;
import game.Cell;
import game.Player;
import game.Tile;

public class Theif extends ActivateClass {

	public void runEffect() {
		setTargetRequired(true);
		init thread = new init();
		name = "Theif";
		thread.start();
	}

	class init extends Thread {

		public void run() {

			Tile targetTile;
			boolean windSelect = false;
			boolean earthSelect = false;
			boolean waterSelect = false;
			boolean checkExist = false;
			boolean printOne = true;
			boolean stolen = false;
			
			while (exist) {
				try {
					if (gps.getCurrentPlayer().getWindCount() >= 0 && gps.getCurrentPlayer().getClassCards().contains(name)) {
						writeInfo();
						setMetRequirements(true);
						if (playergui.getClassActivate()) {

							/* Checks if there are cells on checkpoint tiles */
							for (Tile chkpt : dlayer.getRingList().get(7)) {
								if (chkpt.hasCells()) {
									for (Iterator<Cell> iter = chkpt.getCellStorage().keySet().iterator(); iter.hasNext();) {
										Cell nextCell = iter.next();
										if (chkpt.getCellStorage().get(nextCell) == playergui.getTargetPlayer()) {
											checkExist = true;
											
											break;
										}
									}
								}
							}

							if (player.getEarthCount() >= 1 && player.getWaterCount() >= 1 && player.getWindCount() >= 1) {
								if (checkExist && playergui.getTargetPlayer() != player) {
									playergui.setTextPane("Please select one water, one wind, and one earth to steal a checkpoint piece.\n");
									STATES returnState = gps.getCurrentState();
									gps.setState(STATES.USE_SPELL_STATE);
									while (!windSelect || !earthSelect || !waterSelect) {
										gps.setSpellTargetTile(null);
										while (gps.getSpellTargetTile() == null) {
											sleep(5);
										}
										targetTile = gps.getSpellTargetTile();
										switch (targetTile.getElement()) {
										case Tile.WATER:
											if (!waterSelect) {
												if (targetTile.removeCell(player, true)) {
													waterSelect = true;
												} else {
													playergui.setTextPane("You do not have a cell on this tile.\n");
												}

											} else {
												playergui.setTextPane("You've already selected a water cell.\n");
											}
											break;
										case Tile.WIND:
											if (!windSelect) {
												if (targetTile.removeCell(player, true)) {
													windSelect = true;
												} else {
													playergui.setTextPane("You do not have a cell on this tile.\n");
												}
											} else {
												playergui.setTextPane("You've already selected a water cell.\n");
											}
											break;
										case Tile.EARTH:
											if (!earthSelect) {
												if (targetTile.removeCell(player, true)) {
													earthSelect = true;
												} else {
													playergui.setTextPane("You do not have a cell on this tile.\n");
												}
											} else {
												playergui.setTextPane("You've already selected a water cell.\n");
											}
											break;
										default:
											playergui.setTextPane("Please select a wind, water, or earth tile only.\n");
											break;
										}
									}
									playergui.setTextPane("Please select a checkpoint tile to steal a cell.\n");
									while(!stolen){
										gps.setSpellTargetTile(null);
										while (gps.getSpellTargetTile() == null) {
											sleep(5);
										}
										targetTile = gps.getSpellTargetTile();
										switch (targetTile.getElement()) {
											case Tile.CHECKPOINT:
												for (Iterator<Cell> iter = targetTile.getCellStorage().keySet().iterator(); iter.hasNext();) {
													Cell nextCell = iter.next();
													if (targetTile.getCellStorage().get(nextCell) == playergui.getTargetPlayer()) {
														targetTile.removeCell(playergui.getTargetPlayer(), true);
														targetTile.addCell(player, gps);
														stolen = true;
														break;
													}
												}
												if(!stolen){
													playergui.setTextPane("There are no cell(s) from targeted Player. Please select another tile.\n");
												}
												break;
											default:
												playergui.setTextPane("Please select a checkpoint tile.\n");
												break;
										}
									}
									stolen = false;
									checkExist = false;
									gps.setState(returnState);
								} else {
									playergui.setTextPane("Cannot steal from this Player. Please select another player.\n");
								}
							} else {
								playergui.setTextPane("Not enough cells.\n");
							}
							playergui.setClassActivate(false);
							checkExist = false;
							resetTargetList();
							sleep(5);
							
						}
						sleep(5);

					} else {
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
