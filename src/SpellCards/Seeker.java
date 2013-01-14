package SpellCards;

import game.Cell;
import game.DataLayer;
import game.Player;
import game.RenderObject;
import game.Tile;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import main.GamePlayState;
import main.GamePlayState.STATES;

public class Seeker extends ActivateSpell{

	DataLayer dlayer;
	GamePlayState gps;
	
	public Seeker(DataLayer dlayer, GamePlayState gps) {
		this.dlayer = dlayer;
		this.gps = gps;
		setImmediateActivate(true);
	}
	
	@Override
	public boolean runEffect() {
		init thread = new init(dlayer, gps);
		thread.start();
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
			Random rand = new Random();
			boolean first = true;
			boolean removed = false;
			resetDefaultState();
			while(active){
				try {
					if (first) {
						writeToAllPlayers("Seeker has spawned in the center! Destroy it by having a player land on it!\n");
						//store the new object to render.
						Tile center = dlayer.getRingList().get(0).get(0);
						storeRenderObject(RenderObject.SEEKER, center, new Image("res/Seeker.png"));
						first = false;
					}
					
					if (gps.getCurrentState() == STATES.PRE_MOVEMENT_STATE) {
						removed = false; 
					}
					
					if (gps.getCurrentState() == STATES.CHANGE_PLAYER_STATE && !removed){
						//remove a random cell from each player.
						ArrayList<Cell> validCells = new ArrayList<Cell>();
						for (Player player : gps.getPlayers()) {
							//only if the player actually has a cell to remove that is not chkpoint piece.
							for (Cell cell : player.getCellList()) {
								if (cell.getCurrentTile().getRingNum() != 7) {
									validCells.add(cell);
								}
							}
							if (validCells.size() > 0) {
								int cellNum = rand.nextInt(validCells.size());
								Cell cell = player.getCellList().get(cellNum);
								cell.getCurrentTile().leaveCell(cell);
								player.getCellList().remove(cell);
							}
						}
						removed = true;
					}
					else if (gps.getCurrentState() == STATES.ACTIONS_STATE) {
						if (dlayer.getRingList().get(0).get(0).getNumPlayers() > 0) {
							Tile center = dlayer.getRingList().get(0).get(0);
							removeRenderObject(RenderObject.SEEKER, center);
							writeToAllPlayers("Seeker has been neutralized.");
							setActive(false);
						}	
					}
					else {
						sleep(5);
					}
				} catch (InterruptedException e) {} catch (SlickException e) {}	
			}
			return;
		}
	}
}