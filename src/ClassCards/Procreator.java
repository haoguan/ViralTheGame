package ClassCards;

import main.GamePlayState.STATES;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class Procreator extends ActivateClass{ 
	boolean procreate = true;
	public void runEffect(){
		init thread = new init();
		name = "Procreator";
		thread.start();
	}
	
	class init extends Thread{
		
		public void run(){
			while(exist){
				try {
					if(gps.getCurrentPlayer().getEarthCount() >= 1 && gps.getCurrentPlayer().getClassCards().contains(name)){
						writeInfo();
						if(gps.getCurrentState() == STATES.PLANT_CELL_STATE){
							if(!playergui.getPlantCell().isEnabled() && procreate){
								playergui.setPlantCell(true);
								procreate = false;
							}
						}
						sleep(5);
					}else{
						procreate = true;
						sleep(5);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

