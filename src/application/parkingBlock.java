package application;

import java.sql.SQLException;

public class parkingBlock {
	private char block;
	private int currentVehicle = 0;
	private int maxVehicle;
	//private Connection conn;
	
        public parkingBlock() {
//		super();
		//conn = DatabaseController.connect(conn);
	}
        
	public parkingBlock(char block, int maxVehicle) {
//		super();
		this.block = block;
		this.maxVehicle = maxVehicle;
		//conn = DatabaseController.connect(conn);
	}


	public parkingBlock(char block, int currentVehicle, int maxVehicle) {
		super();
		this.block = block;
		this.currentVehicle = currentVehicle;
		this.maxVehicle = maxVehicle;
		//conn = DatabaseController.connect(conn);
	}

	public char getBlock() {
		return block;
	}

	public void setBlock(char block) {
		this.block = block;
	}

	public int getCurrentVehicle() {
		return currentVehicle;
	}

	public void setCurrentVehicle(int currentVehicle) {
		this.currentVehicle = currentVehicle;
	}

	public int getMaxVehicle() {
		return maxVehicle;
	}

	public void setMaxVehicle(int maxVehicle) {
		this.maxVehicle = maxVehicle;
	}
	
	public void vehicleCheckin() throws ParkingBlockException, SQLException{
		//if (!DatabaseController.isDBConnected(conn)) throw new SQLException();
		if (!this.isFull()) {
			this.currentVehicle++;
			//DatabaseController.blockNewVehicle(this.conn, this.block, this.currentVehicle);
		}
		else throw new ParkingBlockException();
	}
	
	public void vehicleCheckout() throws ParkingBlockException, SQLException{
		//if (!DatabaseController.isDBConnected(conn)) throw new SQLException();
		if (this.getCurrentVehicle() > 0) {
			this.currentVehicle--;
			//DatabaseController.blockOutVehicle(this.conn, this.block, this.currentVehicle);
		}
		else throw new ParkingBlockException();
	}
	
	public boolean isFull() {
		return this.getCurrentVehicle() == this.getMaxVehicle();
	}
	
	public static boolean isFull(parkingBlock parkingblock) {
		return parkingblock.isFull();
	}
}
