package ClassCards;

import main.GamePlayState.STATES;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class CoreTransporter extends ActivateClass{ 
	
	Sound fx;
	public void runEffect(){
		init thread = new init();
		name = "Core Transporter";
		try {
			fx = new Sound("res/GamePlayState/Sounds/coretransporter.wav");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		thread.start();
	}
	
	class init extends Thread{
		
		public void run(){
			while(exist){
				try {
					if(gps.getCurrentPlayer().getWindCount() >= 0 && gps.getCurrentPlayer().getClassCards().contains(name)){
						writeInfo();
						setMetRequirements(true);
						if(playergui.getClassActivate()){
							fx.play();							
							player.getCurrentTile().leavePlayer(player);
							player.setCurrentTile(dlayer.getRingList().get(0).get(0));
							player.setCurrentPosition(player.getCurrentTile().getPlayerPosition(player));
							player.setPlayerCoords(gps.getTileFinder().angleToPixels(player.getCurrentPosition().getPositionRadius(), player.getCurrentPosition().getAngle()));
							playergui.setClassActivate(false);
							resetRolled();
						}
						sleep(5);
					}else{
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

