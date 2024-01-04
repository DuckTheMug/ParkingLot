package application;

import java.util.UUID;

public class Ticket {
	private String id;
	private boolean isActive = false;
	
	public Ticket() {
		this.initializeID();
	}
	
	private void initializeID() {
		this.id = UUID.randomUUID().toString();
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getID() {
		return id;
	}

	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public static boolean isValid(Ticket ticket1, Ticket ticket2) {
		return (ticket1.getID() == null ? ticket2.getID() == null : ticket1.getID().equals(ticket2.getID())) || ticket1 == null || ticket2 == null;
	}
}