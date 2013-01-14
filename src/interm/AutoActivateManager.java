package interm;
import javax.swing.SwingWorker;

import game.PlayerGui;

/**
 * AutoActivateManager is the background thread used to run cards that are auto-activated
 * from the PlayerGui's DrawCardListener class. This class is used to prevent the Event
 * Dispatch Thread (EDT) from being bogged down and unable to process new input.
 * @author Howard Guan
 */
public class AutoActivateManager extends SwingWorker<Boolean, Boolean>{
	
	PlayerGui gui;
	int id;
	
	/**
	 * Two parameter constructor takes a PlayerGui object and integer representing the
	 * current card being played.
	 * @param gui is the player's gui.
	 * @param currCardID is the integer representation of the card being played.
	 */
	public AutoActivateManager(PlayerGui gui, int currCardID) {
		this.gui = gui;
		id = currCardID;
	}

	/**
	 * doInBackground() details the work that needs to be done in the background.
	 * In this case, we want to activate the card to see if the user picks a valid
	 * move, then remove the card from their hand.
	 * @return true because once the procedure will finish--only concerned with when.
	 */
	protected Boolean doInBackground() throws Exception {
		// TODO Auto-generated method stub
		String currCard = gui.getDeck().getSpellCardList().get(id).getName();
		if (gui.activateCards()) {
			gui.setCurrCardID(gui.getCardID().get(currCard)); // need to pretend we actually clicked it to set currCardID.
			gui.removeCardFromHand();
		}
		return true;
	}

}
