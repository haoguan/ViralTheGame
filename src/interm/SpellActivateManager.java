package interm;
import game.PlayerGui;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import SpellCards.ActivateSpell;

/**
 * SpellActivateManager is the background thread used to run cards from the PlayerGui's
 * ActivateListener. This class is used to prevent the Event Dispatch Thread (EDT) 
 * from being bogged down and unable to process new input.
 * @author Howard Guan
 */
public class SpellActivateManager extends SwingWorker<Boolean, Boolean>{
	
	PlayerGui gui;
	
	/**
	 * Two parameter constructor takes a PlayerGui object.
	 * @param gui is the player's gui.
	 */
	public SpellActivateManager(PlayerGui gui) {
		this.gui = gui;
	}

	/**
	 * doInBackground() details the work that needs to be done in the background.
	 * In this case, we want to check whether the player has appropriately selected
	 * a target if needed, then activate the card to see if the user picks a valid
	 * move, and finally remove the card from their hand.
	 * @return true because once the procedure will finish--only concerned with when.
	 */
	protected Boolean doInBackground() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Activate: " + gui.getDeck().getSpellCardList().get(gui.getCurrCardID()).getName());
		if (gui.getTargetPlayer() == null && gui.getSpellFinder().getCards().get(gui.getCardToTest()).getTargetRequired()) {
			gui.setTextPane("Please Select A Player.\n");
		} else if (gui.activateCards()) {
			gui.removeCardFromHand();
		}
		return true;
	}

	
	
	

}
