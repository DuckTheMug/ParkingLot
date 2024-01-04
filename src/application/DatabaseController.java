	package application;
	
	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.sql.Timestamp;
	import java.time.LocalDateTime;
	import java.time.ZoneOffset;
	import java.util.ArrayList;
	
	public class DatabaseController {
		private static final String DB_URL = "jdbc:mysql://root@localhost:3306/parkingLot";
		private static final String USER = "root";
		private static final String PASSWORD = "1220021234";
                //ket noi vs database
		public static Connection connect(Connection connection) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return connection;
		}
		//bat dau luot gui
		public static void startInstance(Connection conn, String id, Ticket ticket, Vehicle vehicle, parkingBlock parkingblock, LocalDateTime startTime) {
			Timestamp timestamp = Timestamp.from(startTime.toInstant(ZoneOffset.ofHours(7)));
			try {
				PreparedStatement stmt = conn.prepareStatement("Insert into `parkinglot`.`instance` (`id`,`ticket`,`license`,`type`,`startTime`,`block`) VALUES (?,?,?,?,?,?)");
				stmt.setString(1, id);
				stmt.setString(2, ticket.getID());
				stmt.setString(3, vehicle.getLicensePlate());
				stmt.setString(4, vehicle.getType());
				stmt.setTimestamp(5, timestamp);
				stmt.setString(6, String.valueOf(parkingblock.getBlock()));
				stmt.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//ket thuc luot gui
		public static void endInstance(Connection conn, String id, LocalDateTime endTime, String elapsedTime) {
			Timestamp timestamp = Timestamp.from(endTime.toInstant(ZoneOffset.ofHours(7)));
			try {
				PreparedStatement stmt = conn.prepareStatement("UPDATE `parkinglot`.`instance` SET `elapsedTime` = ?, `endTime` = ? WHERE `id` = ?");
				stmt.setString(1, elapsedTime);
				stmt.setTimestamp(2, timestamp);
				stmt.setString(3, id);
				stmt.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//ve bat dau hoat dong
		public static void setTicketActive(Connection conn, Ticket t, boolean active) {
			t.setActive(active);
			try {
				PreparedStatement stmt = conn.prepareStatement("UPDATE `parkinglot`.`ticket` SET `active` = ? WHERE `id` = ?");
				stmt.setBoolean(1, active);
				stmt.setString(2, t.getID());
				stmt.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//kiem tra xem database co ket noi ko
		public static boolean isDBConnected(Connection connect) {
			try {
				return connect != null && !connect.isClosed();
				} catch (SQLException ignored) {}
			return false;
		}
		//lay du lieu luot gui tu database chuyen sang arraylist
		public static ArrayList<parkingInstance> toInstanceArrayList(ResultSet rs1, ResultSet rs2) throws SQLException {
			ArrayList<parkingInstance> data = new ArrayList <>();
			while (rs1.next()) {
				parkingInstance instance = new parkingInstance();
				instance.setId(rs1.getString("id"));
				instance.getTicket().setId(rs1.getString("ticket"));
				switch (rs1.getString(4)) {
				case "Car" -> {
                                    instance.setVehicle(new Car(rs1.getString(3)));
                                    break;
                                }
				case "Motorbike" -> {
                                    instance.setVehicle(new Motorbike(rs1.getString(3)));
                                    break;
                                }
				default -> {
                                    break;
                                }
				}
				instance.setStartTime(rs1.getTimestamp(5).toLocalDateTime());
				if(rs1.getTimestamp(6) != null) instance.setEndTime(rs1.getTimestamp(6).toLocalDateTime());
				if(rs1.getString(7) != null) instance.setElapsedTime(rs1.getString(7));
                                //tim block gui va gan luot gui 
				rs2.beforeFirst();
				while (rs2.next()) {
					if (rs1.getString(8).equals(rs2.getString(1))) {
						parkingBlock block = new parkingBlock(rs2.getString(1).charAt(0),rs2.getInt(2),rs2.getInt(3));
						instance.setParkingblock(block);
						break;
					}
				}
				data.add(instance);
			}
			return data;
		}
		//lay du lieu block gui tu database chuyen sang arraylist
		public static ArrayList<parkingBlock> toBlockArrayList (ResultSet resultset) throws SQLException {
			ArrayList<parkingBlock> data = new ArrayList <>();
			while (resultset.next()) {
				parkingBlock block = new parkingBlock();
				block.setBlock(resultset.getString("block").charAt(0));
				block.setCurrentVehicle(resultset.getInt("currentVehicle"));
				block.setMaxVehicle(resultset.getInt("maxVehicle"));
				data.add(block);
			}
			return data;
		}
                //lay du lieu ve tu database chuyen sang arraylist
                public static ArrayList<Ticket> toTicketArrayList(ResultSet rs) throws SQLException {
                    ArrayList<Ticket> data = new ArrayList<>();
                    while (rs.next()) {                        
                        Ticket t  = new Ticket();
                        t.setId(rs.getString(1));
                        t.setActive(rs.getBoolean(2));
                        data.add(t);
                    }
                    return data;
                } 
                //lay du lieu phuong tien tu database chuyen sang arraylist
                public static ArrayList<Vehicle> toVehicleArrayList(ResultSet rs) throws SQLException {
                    ArrayList<Vehicle> data = new ArrayList<>();
                    while (rs.next()) {                        
                        Vehicle v = null;
                        switch (rs.getString(4)) {
                            case "Car" -> {
                                v = new Car(rs.getString(3));
                                break;
                            }
                            case "Motorbike" -> {
                                v = new Motorbike(rs.getString(3));
                                break;
                            }
                        }
                        data.add(v);
                    }
                    return data;
                }
                //kiem tra xem database co rong ko
                public static boolean isEmpty(Connection conn, String q) throws SQLException {
                    ResultSet rs = conn.createStatement().executeQuery(q);
                    int c = 0;
                    while(rs.next()){
                        c = rs.getInt(1);
                    }
                    return c == 0; 
                }
	}
