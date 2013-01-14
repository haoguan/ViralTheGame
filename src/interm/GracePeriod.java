package interm;

import game.Player;
import game.PlayerGui;

import javax.swing.SwingUtilities;

public class GracePeriod extends Thread {
	Player[] players;
	PlayerGui pg;
	int time = 0;
	SpellFinder sf;

	public GracePeriod(SpellFinder sf, Player[] players) {
		this.sf = sf;
		// this.pg = pg;
		this.players = players;
	}

	public void run() {
		try {
			// pg.setActivateCard(false);
			// pg.setPlayerList(false);
			for (time = 0; time <= 100; time++) {
				if (time < 0) {
					time = 0;
				}
				final int percent = time;
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						players[0].getPlayerGui().updateBar(percent);
						players[1].getPlayerGui().updateBar(percent);
						players[2].getPlayerGui().updateBar(percent);
						players[3].getPlayerGui().updateBar(percent);
					}
				});
				Thread.sleep(100);

			}

			// for (int i = 20; i >= 0; i--) {
			// pg.setTextPane(i + ".. ");
			// Thread.sleep(1000);
			// }

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < players.length; i++) {
			players[i].getPlayerGui().updateBar(0);
		}
		sf.setGracePeriod(false);
		sf.setPeriod(true);
		time = 0;
		return;
	}

	public synchronized void pauseTime() {

		try {
			suspend();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized void resumeTime(){
		try {
			resume();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addTime() {
		time -= 20;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}
}