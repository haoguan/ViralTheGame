package ClassCards;

import game.Player;
import main.GamePlayState.STATES;

public class Hopper extends ActivateClass{ 
	boolean hopper = true;
	int rollTemp;
	
	
	public void runEffect(){
		init thread = new init();
		name = "Hopper";
		thread.start();
	}
	
	class init extends Thread{
		
		public void run(){
			while(exist){
				try {
					if(gps.getCurrentPlayer().getEarthCount() >= 1 && gps.getCurrentPlayer().getClassCards().contains(name)){
						writeInfo();
						if(gps.getCurrentState() == STATES.PRE_MOVEMENT_STATE){
							if(!playergui.getRollDice().isEnabled() && hopper){
								rollTemp = player.getRoll();
								System.out.println(rollTemp);
								player.setRoll(++rollTemp);
								gps.setState(STATES.ROLL_STATE);
								hopper = false;
							}
						}
						
						sleep(5);
					}else{
						hopper = true;
						sleep(5);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

