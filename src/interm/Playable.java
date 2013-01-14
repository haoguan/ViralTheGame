package interm;

import game.PlayerGui;
import main.GamePlayState;
import main.GamePlayState.STATES;

public class Playable extends Thread {
	PlayerGui pg;
	GamePlayState gps;
	SpellFinder spells;
	ClassFinder classes;
	String spellName = null;
	String className = null;
	boolean able = true;
	boolean running = true;
	boolean gracePeriod = false;

	public Playable(PlayerGui pg, GamePlayState gps, SpellFinder spells, ClassFinder classes) {
		this.pg = pg;
		this.gps = gps;
		this.spells = spells;
		this.classes = classes;
	}

	public void setSpellName(String name) {
		spellName = name;
	}

	public void setClassName(String name) {
		className = name;
	}

	public void run() {
		while (running) {
			if (spells.isGracePeriod()) {
				if (spellName != null && spells.getCards().get(spellName).getPlayableState().contains(gps.getCurrentState()) && spells.getCards().get(spellName).isCounter()) {
					if (able) {
						pg.setActivateCard(true);
						able = false;
					}
				} else if (className != null && classes.getCards().get(className).isMetRequirements() && classes.getCards().get(className).getPlayableState().contains(gps.getCurrentState()) && classes.getCards().get(className).isCounter()) {
					if (able) {
						pg.setActivateCard(true);
						able = false;
					}
				} else {
					if (!able && gps.currentState != STATES.CHANGE_PLAYER_STATE) {
						pg.setActivateCard(false);
						able = true;
					}
				}
			} else {
				if (spellName != null && spells.getCards().get(spellName).getPlayableState().contains(gps.getCurrentState())) {
					if (able) {
						System.out.println("RUN!");
						pg.setActivateCard(true);
						able = false;
					}
				} else if (className != null && classes.getCards().get(className).isMetRequirements() && classes.getCards().get(className).getPlayableState().contains(gps.getCurrentState()) && !classes.getCards().get(className).isCounter()) {
					if (able) {
						pg.setActivateCard(true);
						able = false;
					}
				} else {
					if (!able && gps.currentState != STATES.CHANGE_PLAYER_STATE ) {
						pg.setActivateCard(false);
						able = true;
					}
				}
				try {
					sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void setGracePeriod(boolean status) {
		gracePeriod = status;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}
