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
		//requires four different threads for each of the different player gui's to complete separate work.
		Player player1 = gps.getPlayers()[0];
		Player player2 = gps.getPlayers()[1];
		Player player3 = gps.getPlayers()[2];
		Player player4 = gps.getPlayers()[3];
		init thread1 = new init(dlayer, gps, player1.getPlayerGui());
		init thread2 = new init(dlayer, gps, player2.getPlayerGui());
		init thread3 = new init(dlayer, gps, player3.getPlayerGui());
		init thread4 = new init(dlayer, gps, player4.getPlayerGui());
		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();
		try {
			thread1.join();
			thread2.join();
			thread3.join();
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
		
		public init(DataLayer dlayer, GamePlayState gps, PlayerGui gui) {
			this.dlayer = dlayer;
			this.gps = gps;
		}
		
		public boolean checkConstellation(int numPlayerCells) {
			boolean fire = false;
			boolean water = false;
			boolean wind = false;
			boolean earth = false;
			for (Integer ring : dlayer.getElements().keySet()) {
				for (Tile tile : dlayer.getElements().get(ring)) {
					switch (tile.getElement()) {
					case Tile.FIRE:
						fire = true;
						break;
					case Tile.WATER:
						water = true;
						break;
					case Tile.WIND:
						wind = true;
						break;
					case Tile.EARTH:
						earth = true;
						break;
					}
				}
			}
			if (fire && water && wind && earth) 
				return true;
			return false;
		}
		
		public void run(){
			STATES returnState = gps.getCurrentState();
//			ArrayList<Tile> targetedTiles = new ArrayList<Tile>();
			Tile targetTile;
			Tile lastTargetTile;
			resetDefaultState();
			try {
				while(active){
					if (checkConstellation(playergui.getPlayer().getNumCells())) {
						playergui.setTextPane("Already completed constellation.\n");
						gps.setState(returnState);
						setActive(false);
					}
					else {
						playergui.setTextPane("Please reorganize your cells to having at least one in each element.\n");
						gps.setState(STATES.USE_SPELL_STATE);
						while (!checkConstellation(playergui.getPlayer().getNumCells())) {
							gps.setNewInput(false);
							playergui.setTextPane("Please select an element tile with your cells.\n");
							while (!gps.getNewInput()) {
								sleep(5);
							}
							targetTile = gps.getTargetTile();
							if (!targetTile.containsCell(playergui.getPlayer()) || targetTile.getElement() >= Tile.EARTH) {
								playergui.setTextPane("Please select an appropriate tile.\n");
							}
							else {
								lastTargetTile = targetTile;
								gps.setNewInput(false);
								playergui.setTextPane("Please select a destination element tile.\n");
								while (!gps.getNewInput()) {
									sleep(5);
								}
								targetTile = gps.getTargetTile();
								if (targetTile.getElement() >= Tile.EARTH) {
									playergui.setTextPane("Please select an appropriate tile.\n");
								}
								else {
									//remove one cell from old element tile and add to new one.
									lastTargetTile.removeCell(playergui.getPlayer());
									gps.addCell(playergui.getPlayer(), targetTile, gps);
								}
							}
						}
						playergui.setTextPane("Successfully completed constellation.\n");
						gps.setState(returnState);
						setActive(false);
					}
				}
			} catch (InterruptedException e) {}
			return;
		}
	}
}
