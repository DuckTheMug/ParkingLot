package application;

public abstract class Vehicle {
	private String licensePlate;
	
	public Vehicle() {
		this.licensePlate = null;
	}

	public Vehicle(String licensePlate) {
		this.setLicensePlate(licensePlate);
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	
	public static boolean isValid(Vehicle vehicle1, Vehicle vehicle2) {
		return (vehicle1.getLicensePlate() == null ? vehicle2.getLicensePlate() == null : vehicle1.getLicensePlate().equals(vehicle2.getLicensePlate())) || vehicle1 == null || vehicle2 == null;
	}
	
	abstract public String getType();
}
