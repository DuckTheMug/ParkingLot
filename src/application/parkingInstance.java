package application;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
//import java.time.Instant;
import java.time.LocalDateTime;
//import java.time.LocalTime;
import java.util.UUID;

public class parkingInstance {
	private String id;
	private Ticket ticket;
	private parkingBlock parkingblock;
	private LocalDateTime startTime = null;
	private LocalDateTime endTime = null;
	private Duration elapsedTime;
	private Vehicle vehicle;
	
	public parkingInstance(Ticket ticket, Vehicle vehicle, parkingBlock parkingblock) {
		this.initializeID();
		this.ticket = ticket;
		this.initializeStartTime(startTime);
		this.vehicle = vehicle;
		this.parkingblock = parkingblock;
	}
	
	public parkingInstance() {
		ticket = new Ticket();
		this.initializeStartTime(startTime);
		this.initializeID();
		vehicle = null;
	}
	
	private void initializeID() {
		this.id = UUID.randomUUID().toString();
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
	
	public void setElapsedTime(Duration elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public void setElapsedTime(String elapsedTime) {
		String[] str = elapsedTime.split(":");
		this.elapsedTime = Duration.ZERO;
		this.elapsedTime.plusHours(Long.parseLong(str[0]));
		this.elapsedTime.plusMinutes(Long.parseLong(str[1]));
	}
	
	public String getID() {
		return id;
	}
	
	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}
	
	public parkingBlock getParkingblock() {
		return parkingblock;
	}

	public void setParkingblock(parkingBlock parkingblock) {
		this.parkingblock = parkingblock;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	private void initializeStartTime(LocalDateTime startTime) {
		startTime = LocalDateTime.now();
		this.startTime = startTime;
	}

	public String getElapsedTime()  {
            if (this.elapsedTime == null) return null;
            else {
            	int h = this.elapsedTime.toHoursPart();
            	int m = elapsedTime.toMinutesPart();
                return String.format("%02d", h) + ":" + String.format("%02d", m);
            }
	}

	private void initializeElpased() throws EmptyFieldException {
		if (startTime != null || endTime != null) {
			elapsedTime = Duration.between(this.startTime,this.endTime);
		} else throw new EmptyFieldException();
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	
	public void startInstance(Connection conn) throws EmptyFieldException, SQLException, ParkingBlockException {
		if (!DatabaseController.isDBConnected(conn)) throw new SQLException();
		if (this.ticket != null && this.vehicle != null && this.parkingblock != null) {
			parkingblock.vehicleCheckin();
			DatabaseController.startInstance(conn, this.id, this.ticket, this.vehicle, this.parkingblock, this.startTime);
			ticket.setActive(true);
		} else throw new EmptyFieldException();
	}
	
	public void startInstance(LocalDateTime startTime, Connection conn) throws EmptyFieldException, SQLException, ParkingBlockException {
		if (!DatabaseController.isDBConnected(conn)) throw new SQLException();
		if (this.ticket != null && this.vehicle != null && this.parkingblock != null) {
			parkingblock.vehicleCheckin();
			DatabaseController.startInstance(conn, this.id, this.ticket, this.vehicle, this.parkingblock, startTime);
			ticket.setActive(true);
		} else throw new EmptyFieldException();
	}
        
	public void endInstance(Connection conn) throws EmptyFieldException, SQLException, ParkingBlockException {
		if (!DatabaseController.isDBConnected(conn)) throw new SQLException();
		if (this.startTime != null) {
			parkingblock.vehicleCheckout();
			this.endTime = LocalDateTime.now();
			this.initializeElpased();
			DatabaseController.endInstance(conn, this.id, this.endTime, this.getElapsedTime());
			ticket.setActive(false);
		} else throw new EmptyFieldException();
	}
	
	public void endInstance(LocalDateTime endTime, Connection conn) throws EmptyFieldException, SQLException, ParkingBlockException {
		if (!DatabaseController.isDBConnected(conn)) throw new SQLException();
		if (this.startTime != null) {
			parkingblock.vehicleCheckout();
			this.endTime = endTime;
			initializeElpased();
			DatabaseController.endInstance(conn, this.id, this.endTime, this.getElapsedTime());
			ticket.setActive(false);
		} else throw new EmptyFieldException();
	}
	
	public LocalDateTime getEndTime() {
		return endTime;
	}
}
