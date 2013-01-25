package SpellCards;

import game.DataLayer;
import game.Player;
import game.PlayerGui;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;

import main.GamePlayState;

public class MenacingTouch extends ActivateSpell{
	
	DataLayer dlayer;
	GamePlayState gps;
	
	public MenacingTouch(DataLayer dlayer, GamePlayState gps) {
		this.dlayer = dlayer;
		this.gps = gps;
		setTargetRequired(true);
	}
	
	@Override
	public boolean runEffect() {
		resetDefaultState();
		init thread = new init(dlayer, gps);
		thread.start();
		//this while loop allows for thread to not be finished and still return correct result earlier.
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
		
		public void run(){
			while(active){
				if (playergui.getTargetPlayer().getPlayerGui().getSpellList().size() == 0) {
					failureToPlay("Target player does not have cards to select!\n");
				}
				else {
					setCheckSuccess(true);
					Random rand = new Random();
					Player targetPlayer = playergui.getTargetPlayer();
					PlayerGui tGui = targetPlayer.getPlayerGui();
					ArrayList<JButton> nonEmptyButtons = new ArrayList<JButton>();
					for (JButton button : tGui.getSpellList()) {
						if (!button.getText().equals("empty")) {
							nonEmptyButtons.add(button);
						}
					}
					int select = rand.nextInt(nonEmptyButtons.size());
					JButton button = nonEmptyButtons.get(select);
					targetPlayer.getSpellCards().remove(tGui.getDeck().getSpellCardList().get(tGui.getCurrCardID()).getName());
					button.setEnabled(false);
					button.setText("empty");
					tGui.decrementCardCount();
					if (tGui.getCardCount() < tGui.getMaxSpellCards()) {
						tGui.getDrawCard().setEnabled(true);
					}
					try {
						tGui.getPlayable().setRunning(false);
					} catch (Exception e) {}
					tGui.getActivateCard().setEnabled(false);
					resetTargetList();
					setActive(false);
				}
			}
			return;
		}
	}
}
