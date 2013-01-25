package SpellCards;

import game.AdjacentTile;
import game.DataLayer;
import game.RenderObject;
import game.Tile;

import java.util.ArrayList;
import java.util.Iterator;

import main.GamePlayState;
import main.GamePlayState.STATES;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class MarkOfTheVoid extends ActivateSpell{
	
	DataLayer dlayer;
	GamePlayState gps;
	
	public MarkOfTheVoid(DataLayer dlayer, GamePlayState gps) {
		this.dlayer = dlayer;
		this.gps = gps;
	}

	@Override
	public boolean runEffect() {
		init thread = new init(dlayer, gps);
		thread.start();
		//this while loop allows for thread to not be finished and still return correct result earlier.
		while (!checkSuccess) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {}
		}
		return isSuccessRun();  //no possible way for exception, so just return true.
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
			resetDefaultState();
			try {
				while(active){
					playergui.setTextPane("Please select an empty tile (except checkpoint and adjacent to checkpoint tiles) to mark.\n");
					gps.setSpellTargetTile(null);
					gps.setState(STATES.USE_SPELL_STATE);
					while (gps.getSpellTargetTile() == null) {
						sleep(10);
					}
					targetTile = gps.getSpellTargetTile();
					//conditions for eligibility
					if (targetTile.getRingNum() == 7 || (targetTile.getRingNum() == 6 && targetTile.getTileID() == Tile.ADJCHK1 || targetTile.getTileID() == Tile.ADJCHK2 || 
							targetTile.getTileID() == Tile.ADJCHK3 || targetTile.getTileID() == Tile.ADJCHK4) || targetTile.isOccupied()) {
						gps.setSpellTargetTile(null);
						gps.setState(returnState);
						failureToPlay("Target tile cannot be used. Make sure requirements are met.\n");
					}
					else {
						setCheckSuccess(true);
						gps.setState(returnState);
						//store new unavailables to the tile.
						for (AdjacentTile avail : targetTile.getAvailableAdjTiles()) {
							avail.getTile().getStoredUnavailTiles().add(new AdjacentTile(targetTile, false));
						}
						for (AdjacentTile unavail : targetTile.getUnAvailableAdjTiles()) {
							unavail.getTile().getStoredUnavailTiles().add(new AdjacentTile(targetTile, false));
						}
						//check if current possibleMoves use the marked tile and delete them.
						for (Iterator<Tile> move = dlayer.getPossibleMoves().iterator(); move.hasNext(); ) {
							Tile curTile = move.next();
							for (Iterator<ArrayList<Tile>> pathForMove = dlayer.getPossibleMovesPaths().get(curTile).iterator(); pathForMove.hasNext(); ) {
								ArrayList<Tile> path = pathForMove.next();
								for (Iterator<Tile> tileInPath = path.iterator(); tileInPath.hasNext(); ) {
									if (tileInPath.next().equals(targetTile)) {
										pathForMove.remove();
										if (!pathForMove.hasNext()) {
											move.remove();
										}
										break;
									}
								}
							}
						}
						//store the new object to render.
						storeRenderObject(RenderObject.MARK_OF_THE_VOID, targetTile, gps.getMarkOfTheVoid());
						setActive(false);
					}
				}
			} catch (InterruptedException e) {}
			return;
		}
	}

}
