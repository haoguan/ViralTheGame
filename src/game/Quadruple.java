/*Quadruple.java*/

package game;


//this class is mainly used for a key to the Hashmap to find the correct tile.
public class Quadruple {
	private double angleL;
	private double angleH;
	private double radiusL;
	private double radiusH;
	
	public Quadruple(double angleL, double angleH, double radiusL, double radiusH) {
		this.angleL = angleL;
		this.angleH = angleH;
		this.radiusL = radiusL;
		this.radiusH = radiusH;
	}
	
	public double getAngleL() {
		return angleL;
	}
	
	public double getAngleH() {
		return angleH;
	}
	
	public double getRadiusL() {
		return radiusL;
	}
	
	public double getRadiusH() {
		return radiusH;
	}
	
	public void setAngleL(double angle) {
		angleL = angle;
	}
	
	public void setAngleH(double angle) {
		angleH = angle;
	}
	
	public void setRadiusL(double radius) {
		radiusL = radius;
	}
	
	public void setRadiusH(double radius) {
		radiusH = radius;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(angleH);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(angleL);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(radiusH);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(radiusL);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Quadruple other = (Quadruple) obj;
		if (Double.doubleToLongBits(angleH) != Double
				.doubleToLongBits(other.angleH))
			return false;
		if (Double.doubleToLongBits(angleL) != Double
				.doubleToLongBits(other.angleL))
			return false;
		if (Double.doubleToLongBits(radiusH) != Double
				.doubleToLongBits(other.radiusH))
			return false;
		if (Double.doubleToLongBits(radiusL) != Double
				.doubleToLongBits(other.radiusL))
			return false;
		return true;
	}
}


