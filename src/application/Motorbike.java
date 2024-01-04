package application;

public final class Motorbike extends Vehicle {
	private final String type = "Motorbike";
	
	public Motorbike() {
		super();	
	}

	public Motorbike(String licensePlate) {
		super(licensePlate);
	}

	@Override
	public String getType() {
		return this.type;
	}
	
}
