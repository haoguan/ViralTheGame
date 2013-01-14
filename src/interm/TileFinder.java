package interm;

import game.Board;
import game.DataLayer;
import game.Quadruple;
import game.Tile;

public class TileFinder {
	
	Board myBoard;
	DataLayer dlayer;
	public TileFinder(Board board, DataLayer dlayer) {
		myBoard = board;
		this.dlayer = dlayer;
		
	}
	
	

//	public static void main (String[] args) {
//		Board board = new Board();
//		TileFinder tileFinder = new TileFinder(board);
//		System.out.println("X: " + tileFinder.angleToPixels(100, 45)[0] + "   Y: " + tileFinder.angleToPixels(100, 45)[1]);
//		System.out.println("X: " + tileFinder.angleToPixels(35, 135)[0] + "   Y: " + tileFinder.angleToPixels(35, 135)[1]);
//		System.out.println("X: " + tileFinder.angleToPixels(100, 225)[0] + "   Y: " + tileFinder.angleToPixels(100, 225)[1]);
//		System.out.println("X: " + tileFinder.angleToPixels(100,315)[0] + "   Y: " + tileFinder.angleToPixels(100, 315)[1]);
//	}
	
	/**
	 * angleToPixels() converts the current searched location into a pixel form that is already 
	 * translated from the center of the board at (350, 350).
	 * @param radius is the magnitude of the radius from the center of board.
	 * @param angle is the angle between 0 degree line and radius line in degrees.
	 * @return
	 */
	public float[] angleToPixels(double radius, double angle) {
		float y = (float) Math.abs(radius * Math.sin(Math.toRadians(angle))); 
		float x = (float) Math.abs(radius * Math.cos(Math.toRadians(angle)));
		float[] coordinates = new float[2];
		// 4 cases for whether to add or subtract from 350. Assumes y axis is normal with computer standards.
		if (angle <= 90 || angle >= 360) {
			y = 350 - y;
			x = 350 + x;
		}
		else if (angle <= 180) {
			y = 350 - y;
			x = 350 - x;
		}
		else if (angle <= 270) {
			y = 350 + y;
			x = 350 - x;
		}
		else {
			y = 350 + y;
			x = 350 + x;
		}
		coordinates[0] = x;
		coordinates[1] = y;
		return coordinates;
		
	}
	
	public Tile findTile(double radius, double angle) {
		
		double[] radii;
		double[] angles;
		double lowRadius = 0;
		double highRadius = 0;
		double lowAngle = 0;
		double highAngle = 0;
		boolean chkptsCase = false;
		
		//base case for the center.
		if (radius <= dlayer.getEndRadius()) {
			highRadius = dlayer.getEndRadius();
			highAngle = 360;
			Quadruple key = new Quadruple(lowAngle, highAngle, lowRadius, highRadius);
			return myBoard.getTiles().get(key);
		}
		else {
			double subResult = 0;
			int ringNum = -1;
			radii = myBoard.getRadiiBounds();
			for (int i = 0; i < radii.length; i++) {
				subResult = radius - radii[i];
				if (subResult <= 0) {
					ringNum = i; //assumes upper bound of tile.
					highRadius = radii[i]; //sets the high radius.
					lowRadius = radii[i-1]; //sets the low radius as index - 1 of high radius.
//					System.out.println(highRadius);
//					System.out.println(lowRadius);
					break;
				}
			}
			
			switch(ringNum) {
			//case where border between center and ring one.
			case 0:
			case 1:
//				System.out.println("Case 1");
				highRadius = 60;
				highAngle = 360;
				Quadruple key = new Quadruple(lowAngle, highAngle, lowRadius, highRadius);
				return myBoard.getTiles().get(key);
			case 2:
//				System.out.println("Case 2");
				angles = myBoard.getRingOneBounds();
				break;
			case 3:
//				System.out.println("Case 3");
				angles = myBoard.getRingTwoBounds();
				break;
			case 4:
//				System.out.println("Case 4");
				angles = myBoard.getRingThreeBounds();
				break;
			case 5:
//				System.out.println("Case 5");
				angles = myBoard.getRingFourBounds();
				break;
			case 6:
//				System.out.println("Case 6");
				angles = myBoard.getRingFiveBounds();
				break;
			case 7:
//				System.out.println("Case 7");
				angles = myBoard.getRingSixBounds();
				break;
			case 8:
//				System.out.println("Case 8");
				angles = myBoard.getRingSevenBounds();
				chkptsCase = true;
				break;
			default:
				return null;	
			}
			
			double angleSub = 0;
			for (int i = 0; i < angles.length; i++) {
				angleSub = angle - angles[i];
				if (angleSub <= 0) {
					if ((ringNum == 4 || ringNum == 6) && (i == 0)) { //means 3rd ring and 5th ring b/c top bound of 3rd ring is 4th circle etc.
						highAngle = angles[angles.length - 1];
						lowAngle = angles[angles.length - 2];
					}
					else if (ringNum == 8) {
						if (i == 0 || i == 2 || i == 4 || i == 6 || i == 8) {
							return null;
						}
						else {
							highAngle = angles[i];
							lowAngle = angles[i-1];
//							System.out.println(highAngle);
//							System.out.println(lowAngle);
						}
					}
					else {
						//special case for when it is the first tile.
						if (i == 0) {
							highAngle = angles[i+1];
							lowAngle = angles[i];
						}
						else {
							highAngle = angles[i];
							lowAngle = angles[i-1];
						}
					}
					break;
				}
			}
			Quadruple key = new Quadruple(lowAngle, highAngle, lowRadius, highRadius);
			return myBoard.getTiles().get(key);
		}
	}	
}
