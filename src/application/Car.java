package application;

public final class Car extends Vehicle {
	private final String type = "Car";
	public Car() {
		super();
	}

	public Car(String licensePlate) {
		super(licensePlate);
	}

	@Override
	public String getType() {
		return this.type;
	}
	
}
