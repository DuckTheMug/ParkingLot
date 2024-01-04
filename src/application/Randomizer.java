package application;

import java.util.ArrayList;
import java.util.Random;

public class Randomizer {
	private final String n = "0123456789";
	private final Random r = new Random();
	private final StringBuilder str = new StringBuilder();
	public void randomInstance(parkingInstance i, ArrayList<parkingBlock> ar1, ArrayList<Ticket> ar2){
		String type = randomType();
		switch (type) {
		case "Car" ->  {
			i.setVehicle(new Car());
                        break;
		}
		case "Motorbike" -> {
			i.setVehicle(new Motorbike());
                        break;
		}
		default -> {
                    break;
                }
		}
		i.getVehicle().setLicensePlate(randomLicense());
		i.setParkingblock(randomBlock(ar1));
        i.setTicket(randomTicket(ar2));
	}
        
	public String randomLicense() {
		Random r_tmp = new Random();
		for (int i = 0; i < r_tmp.nextInt(4, 6); i++) {
			str.append(n.charAt(r.nextInt(10)));
		}
		return str.toString();
	}
	public parkingBlock randomBlock(ArrayList<parkingBlock> ar) {
            if (ar.isEmpty()) return null;
            else return ar.get(r.nextInt(ar.size()));	
	}
	public String randomType() {
		boolean a = r.nextBoolean();
		if (a) {
                    return "Car";
		} else return "Motorbike";
	}
        public parkingInstance chooseInstance(ArrayList<parkingInstance> ar){
            	parkingInstance i = ar.get(r.nextInt(ar.size()));
            	if (i.getEndTime() != null && i.getElapsedTime() != null) {
    				return this.chooseInstance(ar);
    			} else return i;
        }
        
        public Ticket randomTicket(ArrayList<Ticket> ar) {
            return ar.get(r.nextInt(ar.size()));
        }
        
        public Vehicle randomVehicle(ArrayList<Vehicle> ar) {
            return ar.get(r.nextInt(ar.size()));
        }
}
