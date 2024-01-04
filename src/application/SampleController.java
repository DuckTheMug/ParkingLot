package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class SampleController implements Initializable{

    @FXML
    private TableView<visibleBlock> BlockTableView;

    @FXML
    private TableView<visibleInstance> InstanceTableView;
    
    private ObservableList<visibleBlock> dataBlock;
    
    private ObservableList<visibleInstance> dataInstance;
    
    private final TableColumn<visibleInstance, String> colBlock = new TableColumn<>("Block");
    
    private final TableColumn<visibleInstance, String> colElapsed = new TableColumn<>("Elapsed Time");
    
    private final TableColumn<visibleInstance, String> colEnd = new TableColumn<>("End Time");

    private final TableColumn<visibleInstance, String> colLicense = new TableColumn<>("License");

    private final TableColumn<visibleInstance, String> colStart = new TableColumn<>("Start Time");

    private final TableColumn<visibleInstance, String> colType = new TableColumn<>("Type");
    
    private final TableColumn<visibleBlock, String> colBlockPrim = new TableColumn<>("Block");

    private final TableColumn<visibleBlock, String> colCur = new TableColumn<>("Current Vehicle");
    
    private final TableColumn<visibleBlock, String> colMax = new TableColumn<>("Max Vehicle");
    
    @FXML
    private Button chkinbtn;

    @FXML
    private Button chkoutbtn;
    
    @FXML
    private Button statbtn;
    
    private Connection conn = null;
        
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        assert BlockTableView != null : "fx:id=\"BlockTableView\" was not injected: check your FXML file 'Sample.fxml'.";
        assert InstanceTableView != null : "fx:id=\"InstanceTableView\" was not injected: check your FXML file 'Sample.fxml'.";
        assert colBlock != null : "fx:id=\"colBlock\" was not injected: check your FXML file 'Sample.fxml'.";
        assert colElapsed != null : "fx:id=\"colElapsed\" was not injected: check your FXML file 'Sample.fxml'.";
        assert colEnd != null : "fx:id=\"colEnd\" was not injected: check your FXML file 'Sample.fxml'.";
        assert colLicense != null : "fx:id=\"colLicense\" was not injected: check your FXML file 'Sample.fxml'.";
        assert colStart != null : "fx:id=\"colStart\" was not injected: check your FXML file 'Sample.fxml'.";
        assert colType != null : "fx:id=\"colType\" was not injected: check your FXML file 'Sample.fxml'.";
    	conn = DatabaseController.connect(conn);
    	try {
            if (DatabaseController.isEmpty(conn, "SELECT count(*) FROM `parkinglot`.`parkingblock`")) {
                conn.createStatement().execute("insert into `parkinglot`.`parkingblock` (`block`, `currentVehicle`, `maxVehicle`) values ('A', 0, 100);");
                conn.createStatement().execute("insert into `parkinglot`.`parkingblock` (`block`, `currentVehicle`, `maxVehicle`) values ('B', 0, 100);");
                conn.createStatement().execute("insert into `parkinglot`.`parkingblock` (`block`, `currentVehicle`, `maxVehicle`) values ('C', 0, 100);");
                conn.createStatement().execute("insert into `parkinglot`.`parkingblock` (`block`, `currentVehicle`, `maxVehicle`) values ('D', 0, 100);");
            }
            if (DatabaseController.isEmpty(conn, "SELECT count(*) FROM `parkinglot`.`ticket`")) {
                for (int i = 0; i < 500; i++) {
                    Ticket t = new Ticket();
                    PreparedStatement stmt = conn.prepareStatement("insert into `parkinglot`.`ticket` (`id`,`active`) values (?,?)");
                    stmt.setString(1, t.getID());
                    stmt.setBoolean(2, t.isActive());
                    stmt.execute();
                }
            }
            if (DatabaseController.isEmpty(conn, "SELECT count(*) FROM `parkinglot`.`parkingblock` WHERE `currentVehicle` != `maxVehicle`")) chkinbtn.setDisable(true);
            if (DatabaseController.isEmpty(conn, "SELECT count(*) FROM `parkinglot`.`parkingblock` WHERE `currentVehicle` != 0") || DatabaseController.isEmpty(conn, "SELECT count(*) FROM `parkinglot`.`instance` WHERE `endTime` IS NULL AND `elapsedTime` IS NULL")) chkoutbtn.setDisable(true);
            getInstancefromDB();
            getBlockfromDB();
	} catch (SQLException e) {
            e.printStackTrace();
	}
    }
        
    public void getInstancefromDB() throws SQLException {
    	colLicense.setCellValueFactory(cellData -> cellData.getValue().getLicenseProp());
    	colType.setCellValueFactory(cellData -> cellData.getValue().getTypeProp());
    	colBlock.setCellValueFactory(cellData -> cellData.getValue().getBlockProp());
    	colStart.setCellValueFactory(cellData -> cellData.getValue().getStartProp());
    	colEnd.setCellValueFactory(cellData -> cellData.getValue().getEndProp());
    	colElapsed.setCellValueFactory(cellData -> cellData.getValue().getElapsedProp());

        try (ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM parkinglot.instance")) {
            this.dataInstance = FXCollections.observableArrayList();
            while (rs.next()) {
                visibleInstance vi = new visibleInstance(
                        rs.getString(3),
                        rs.getString(4),
                        rs.getTimestamp(5),
                        rs.getTimestamp(6),
                        rs.getString(7),
                        rs.getString(8)
                );
                this.dataInstance.add(vi);
            }
        }   	
    	InstanceTableView.setItems(this.dataInstance);
    	InstanceTableView.getColumns().addAll(colLicense, colType, colStart, colEnd, colElapsed, colBlock);
    }
    
    public void chkinbtnclk() throws SQLException, EmptyFieldException, ParkingBlockException {
    	parkingInstance instance = new parkingInstance();
    	Randomizer r = new Randomizer();
        ResultSet rs1 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery("SELECT * FROM parkinglot.parkingblock;");
        ResultSet rs2 = conn.createStatement().executeQuery("SELECT * FROM parkinglot.ticket;");
        ArrayList<parkingBlock> blocklist = DatabaseController.toBlockArrayList(rs1);
        ArrayList<Ticket> ticketlist = DatabaseController.toTicketArrayList(rs2);
        //random va bat dau luot gui
        r.randomInstance(instance, blocklist, ticketlist);
    	instance.startInstance(conn);
        //Day du lieu len database
    	DatabaseController.setTicketActive(conn, instance.getTicket(), true);
    	dataInstance.add(new visibleInstance(instance));
    	InstanceTableView.setItems(this.dataInstance);
    	InstanceTableView.refresh();
    	PreparedStatement stmt = conn.prepareStatement("UPDATE `parkingblock` SET `currentVehicle` = ? WHERE `block` = ?");
    	rs1.beforeFirst();
    	while (rs1.next()) {
    		if (rs1.getString(1).equals(String.valueOf(instance.getParkingblock().getBlock()))) {
		    	stmt.setInt(1, rs1.getInt(2) + 1);
		    	stmt.setString(2, String.valueOf(instance.getParkingblock().getBlock()));
		    	stmt.execute();
	    		visibleBlock vb = new visibleBlock(rs1.getString(1), rs1.getInt(2) + 1, rs1.getInt(3));
	    		BlockTableView.getItems().set(rs1.getRow() - 1, vb);
	    		break;
    		} 
    	}
    	BlockTableView.refresh();
        //kiem tra block xe
    	if (DatabaseController.isEmpty(conn, "SELECT count(*) FROM `parkinglot`.`parkingblock` WHERE `currentVehicle` != `maxVehicle`")) chkinbtn.setDisable(true);    	
    	if (chkoutbtn.isDisable()) chkoutbtn.setDisable(false);
    }
    
    public void chkoutbtnclk() throws SQLException, EmptyFieldException, ParkingBlockException {
        ResultSet rs1 = conn.createStatement().executeQuery("SELECT * FROM parkinglot.instance");
        ResultSet rs2 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery("SELECT * FROM parkinglot.parkingblock");
        ArrayList<parkingInstance> ar1 = DatabaseController.toInstanceArrayList(rs1,rs2);
        Randomizer r = new Randomizer();
        //chon ngau nhien luot gui va ket thuc luot gui ay
        parkingInstance i = r.chooseInstance(ar1);
        visibleInstance vi = new visibleInstance(i);
        i.endInstance(conn);
    	DatabaseController.setTicketActive(conn, i.getTicket(), false);
        //tim kiem luot gui da chon
        int idx = -1;
        for (int j = 0; j < dataInstance.size(); j++) {
        	if (dataInstance.get(j).equals(vi) && !visibleInstance.hasEnded(dataInstance.get(j))) {
        		idx = j;
        		break;
        	}
        }
        InstanceTableView.getItems().set(idx, new visibleInstance(i));
        rs2.beforeFirst();
        //day len database
    	PreparedStatement stmt = conn.prepareStatement("UPDATE `parkingblock` SET `currentVehicle` = ? WHERE `block` = ?");
    	while (rs2.next()) {
    		if (rs2.getString(1).equals(String.valueOf(i.getParkingblock().getBlock()))) {
		    	stmt.setInt(1, rs2.getInt(2) - 1);
		    	stmt.setString(2, String.valueOf(i.getParkingblock().getBlock()));
		    	stmt.execute();
	    		visibleBlock vb = new visibleBlock(rs2.getString(1), rs2.getInt(2) - 1, rs2.getInt(3));
	    		BlockTableView.getItems().set(rs2.getRow() - 1, vb);
	    		break;
    		}
    	}
    	BlockTableView.refresh();
        //kiem tra block xe
    	if (DatabaseController.isEmpty(conn, "SELECT count(*) FROM `parkinglot`.`parkingblock` WHERE `currentVehicle` != 0") || DatabaseController.isEmpty(conn, "SELECT count(*) FROM `parkinglot`.`instance` WHERE `endTime` IS NULL AND `elapsedTime` IS NULL")) chkoutbtn.setDisable(true);
    	if (chkinbtn.isDisable()) chkinbtn.setDisable(false);
    }
    
    public void incomebtnclk(ActionEvent actionEvent) throws IOException {
        //tao cua so moi -> IncomeController.java
    	Parent root = FXMLLoader.load(getClass().getResource("Income.fxml"));
    	Scene scene = new Scene(root);
    	Stage stage = new Stage();
    	stage.setTitle("Income");
    	stage.setScene(scene);
        stage.setResizable(false);
    	stage.show();
    }
    
    public void getBlockfromDB() throws SQLException {
        colBlockPrim.setCellValueFactory(cellData -> cellData.getValue().getBlock());
        colCur.setCellValueFactory(cellData -> cellData.getValue().getCurr().asString());
        colMax.setCellValueFactory(cellData -> cellData.getValue().getMax().asString());
    	String query = "SELECT * FROM parkinglot.parkingblock";
        try (ResultSet rs = conn.createStatement().executeQuery(query)) {
            this.dataBlock = FXCollections.observableArrayList();
            while(rs.next()) {
                visibleBlock vb = new visibleBlock(rs.getString(1), rs.getInt(2), rs.getInt(3));
                dataBlock.add(vb);
            }
        }
    	BlockTableView.setItems(dataBlock);
    	BlockTableView.getColumns().addAll(colBlockPrim,colCur,colMax);
    }
}
