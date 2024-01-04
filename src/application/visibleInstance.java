package application;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;

public class visibleInstance {
	private final SimpleStringProperty license = new SimpleStringProperty();
	private final SimpleStringProperty type = new SimpleStringProperty();
	private final SimpleStringProperty startTime = new SimpleStringProperty();
	private final SimpleStringProperty endTime = new SimpleStringProperty();
	private final SimpleStringProperty elapsedTime = new SimpleStringProperty();
	private final SimpleStringProperty block = new SimpleStringProperty();
	
	public visibleInstance(String license, String type, Timestamp startTime, Timestamp endTime, String elapsedTime, String block) {
		this.license.set(license);
		this.type.set(type);
		this.startTime.set(startTime.toString());
		if (endTime == null) this.endTime.set(null); else this.endTime.set(endTime.toString());
		this.elapsedTime.set(elapsedTime);
		this.block.set(block);
	}
	
	public visibleInstance(parkingInstance i) throws EmptyFieldException {
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0");
		this.license.set(i.getVehicle().getLicensePlate());
		this.type.set(i.getVehicle().getType());
		this.startTime.set(i.getStartTime().format(df));
		if (i.getEndTime() != null) this.endTime.set(i.getEndTime().format(df));
		if (i.getElapsedTime() != null) this.elapsedTime.set(i.getElapsedTime());
		this.block.set(String.valueOf(i.getParkingblock().getBlock()));
	}
	public boolean equals(visibleInstance vi) {
		return this.getLicenseProp().toString().equals(vi.getLicenseProp().toString()) && this.getTypeProp().toString().equals(vi.getTypeProp().toString());
	}
	public static boolean hasEnded(visibleInstance vi) {
		return vi.getEndProp().isNotNull().getValue() && vi.getElapsedProp().isNotNull().getValue();
	}
	public SimpleStringProperty getLicenseProp() {
		return license;
	}
	public SimpleStringProperty getTypeProp() {
		return type;
	}
	public SimpleStringProperty getStartProp() {
		return startTime;
	}
	public SimpleStringProperty getEndProp() {
		return endTime;
	}
	public SimpleStringProperty getElapsedProp() {
		return elapsedTime;
	}
	public SimpleStringProperty getBlockProp() {
		return block;
	}
}
