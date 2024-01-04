package application;

public class ParkingBlockException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ParkingBlockException() {
		super();
	}

	public ParkingBlockException(String message, Throwable err) {
		super(message, err);
	}

	public ParkingBlockException(String message) {
		super(message);
	}
}