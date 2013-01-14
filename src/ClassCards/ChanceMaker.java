package ClassCards;

import main.GamePlayState.STATES;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class ChanceMaker extends ActivateClass{ 
	boolean chanceMaker = true;
	boolean changeState = true;
	
	public void runEffect(){
		init thread = new init();
		name = "Chance Maker";
		thread.start();
	}
	
	class init extends Thread{
		
		public void run(){
			while(exist){
				try {
					if(gps.getCurrentPlayer().getEarthCount() >= 1 && gps.getCurrentPlayer().getClassCards().contains(name)){
						writeInfo();
						if(gps.getCurrentState() == STATES.PRE_MOVEMENT_STATE){
							if(!playergui.getRollDice().isEnabled() && chanceMaker){
								gps.setTrackMouse(false);
								playergui.setRollDice(true);
								chanceMaker = false;
							}
						}
						if(!chanceMaker && !playergui.getRollDice().isEnabled() && changeState){
							gps.setState(STATES.ROLL_STATE);
							changeState = false;
						}
						sleep(5);
					}else{
						chanceMaker = true;
						changeState = true;
						sleep(5);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

