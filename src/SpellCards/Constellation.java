package SpellCards;

import game.AdjacentTile;
import game.DataLayer;
import game.Player;
import game.PlayerGui;
import game.Tile;

import java.util.ArrayList;

import main.GamePlayState;
import main.GamePlayState.STATES;

public class Constellation extends ActivateSpell {

	DataLayer dlayer;
	GamePlayState gps;
	
	public Constellation (DataLayer dlayer, GamePlayState gps) {
		this.dlayer = dlayer;
		this.gps = gps;
	}

	@Override
	public boolean runEffect() {
		resetDefaultState();
		//requires four different threads for each of the different player gui's to complete separate work.
		Player player1 = gps.getPlayers()[0];
		Player player2 = gps.getPlayers()[1];
		Player player3 = gps.getPlayers()[2];
		Player player4 = gps.getPlayers()[3];
		try {
			init thread1 = new init(dlayer, gps, player1.getPlayerGui());
			thread1.start();
			thread1.join();
			init thread2 = new init(dlayer, gps, player2.getPlayerGui());
			thread2.start();
			thread2.join();
			init thread3 = new init(dlayer, gps, player3.getPlayerGui());
			thread3.start();
			thread3.join();
			init thread4 = new init(dlayer, gps, player4.getPlayerGui());
			thread4.start();
			thread4.join();		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isSuccessRun();
	}
	
	class init extends Thread{
		
		DataLayer dlayer;
		GamePlayState gps;
		PlayerGui gui;
		
		public init(DataLayer dlayer, GamePlayState gps, PlayerGui gui) {
			this.dlayer = dlayer;
			this.gps = gps;
			this.gui = gui;
		}
		
		public boolean checkConstellation(Player player) {
			boolean fire = false;
			boolean water = false;
			boolean wind = false;
			boolean earth = false;
			int numTrues = 0;
			for (Integer ring : dlayer.getElements().keySet()) {
				for (Tile tile : dlayer.getElements().get(ring)) {
					switch (tile.getElement()) {
					case Tile.FIRE:
						if (!fire && tile.containsCell(player)) {
							fire = true;
							numTrues++;
						}
						break;
					case Tile.WATER:
						if (!water && tile.containsCell(player)) {
							water = true;
							numTrues++;
						}
						break;
					case Tile.WIND:
						if (!wind && tile.containsCell(player)) {
							wind = true;
							numTrues++;
						}
						break;
					case Tile.EARTH:
						if (!earth && tile.containsCell(player)) {
							earth = true;
							numTrues++;
						}
						break;
					}
				}
			}
			if (player.getNumCells() < 4 && numTrues == player.getNumCells()) {
				return true;
			}
			else if (player.getNumCells() >= 4 && numTrues == 4) {
				return true;
			}
			else {
				return false;
			}
		}
		
		public void run(){
			STATES returnState = gps.getCurrentState();
			Tile targetTile;
			Tile lastTargetTile;
			boolean errorPrint = false;
			try {
				while(active){
					if (checkConstellation(gui.getPlayer())) {
						gui.setTextPane("Already completed constellation.\n");
						gps.setState(returnState);
						setActive(false);
					}
					else {
						gui.setTextPane("Please reorganize your cells to having at least one in each element.\n");
						gps.setState(STATES.USE_SPELL_STATE);
						while (!checkConstellation(gui.getPlayer())) {
							gps.setNewInput(false);
							if (!errorPrint) {
								gui.setTextPane("Please select an element tile with your cells.\n");
								errorPrint = false;
							}
							while (!gps.getNewInput()) {
								sleep(5);
							}
							targetTile = gps.getSpellTargetTile();
							if (!targetTile.containsCell(gui.getPlayer())) {
								gui.setTextPane("Please select an appropriate source.\n");
								errorPrint = true;
							}
							else {
								lastTargetTile = targetTile;
								gps.setNewInput(false);
								gui.setTextPane("Please select a destination element tile.\n");
								while (!gps.getNewInput()) {
									sleep(5);
								}
								targetTile = gps.getSpellTargetTile();
								if (targetTile.getElement() > Tile.EARTH) {
									gui.setTextPane("Please select an appropriate destination.\n");
									errorPrint = true;
								}
								else {
									//remove one cell from old element tile and add to new one.
									lastTargetTile.removeCell(gui.getPlayer(), true);
									targetTile.addCell(gui.getPlayer(), gps);
								}
							}
						}
						gui.setTextPane("Successfully completed constellation.\n");
						gps.setState(returnState);
						setActive(false);
					}
				}
			} catch (InterruptedException e) {}
			return;
		}
	}
}
