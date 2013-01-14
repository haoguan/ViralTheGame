package ClassCards;

import game.Player;
import game.PlayerGui;

public class StreamTraveler extends ActivateClass{ 
	
	
	int waterIDone[] = {6,9,3,0,5,9,6,25};
	int waterIDtwo[] = {3,0,6,9,5,9,6,25};
	int waterIDthree[] = {5,9,6,9,3,0,6,25};
	int waterIDfour[] = {6,25,6,9,3,0,5,9};
	public void runEffect(){
		init thread = new init();
		name = "Stream Traveler";
		thread.start();
	}
	
	class init extends Thread{
		boolean add = true;
		boolean remove = false;
		
		public void run(){
			while(exist){
				try {
					if(gps.getCurrentPlayer().getWaterCount() >= 1 && gps.getCurrentPlayer().getClassCards().contains(name)){
						writeInfo();
						if(add){
							setStream(waterIDone);
							setStream(waterIDtwo);
							setStream(waterIDthree);
							setStream(waterIDfour);
							remove = true;
						}
						add = false;
						sleep(5);
					}else{
						if(remove){
							removeStream(waterIDone);
							removeStream(waterIDtwo);
							removeStream(waterIDthree);
							removeStream(waterIDfour);
							add = true;
						}
						sleep(5);
						remove = false;
						
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public void setStream(int ID[]){
		dlayer.getRingList().get(ID[0]).get(ID[1]).addAdjTiles(dlayer.getRingList().get(ID[2]).get(ID[3]));
		dlayer.getRingList().get(ID[0]).get(ID[1]).addAdjTiles(dlayer.getRingList().get(ID[4]).get(ID[5]));
		dlayer.getRingList().get(ID[0]).get(ID[1]).addAdjTiles(dlayer.getRingList().get(ID[6]).get(ID[7]));
	}
	public void removeStream(int ID[]){
		dlayer.getRingList().get(ID[0]).get(ID[1]).removeAdjTiles(dlayer.getRingList().get(ID[2]).get(ID[3]));
		dlayer.getRingList().get(ID[0]).get(ID[1]).removeAdjTiles(dlayer.getRingList().get(ID[4]).get(ID[5]));
		dlayer.getRingList().get(ID[0]).get(ID[1]).removeAdjTiles(dlayer.getRingList().get(ID[6]).get(ID[7]));
		
	}
}

