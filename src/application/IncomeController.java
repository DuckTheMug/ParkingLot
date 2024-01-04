package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class IncomeController implements Initializable {
    @FXML
    private Label insLbl;
    @FXML
    private Label incLbl;
    @FXML
    private DatePicker startPicker;
    @FXML
    private DatePicker endPicker;
    @FXML
    private ChoiceBox<String> vehicleMenu = new ChoiceBox<>();
    @FXML
    private Button cfmbtn;
    
    private Connection conn = null;
    
    private final TableColumn<visibleInstance, String> colBlock = new TableColumn<>("Block");

    private final TableColumn<visibleInstance, String> colElapsed = new TableColumn<>("Elapsed Time");

    private final TableColumn<visibleInstance, String> colEnd = new TableColumn<>("End Time");

    private final TableColumn<visibleInstance, String> colLicense = new TableColumn<>("License");

    private final TableColumn<visibleInstance, String> colStart = new TableColumn<>("Start Time");

    private final TableColumn<visibleInstance, String> colType = new TableColumn<>("Type");
    
    @FXML
    private TableView<visibleInstance> InstanceTableView;
    
    private ObservableList<visibleInstance> dataInstance;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cfmbtn.setDisable(false);
    	vehicleMenu.getItems().add("Car");
    	vehicleMenu.getItems().add("Motorbike");
    	vehicleMenu.setValue("Car");
    	startPicker.setValue(LocalDate.now(ZoneId.of("+7")));
    	endPicker.setValue(LocalDate.now(ZoneId.of("+7")));
    	startPicker.valueProperty().addListener((arg,oldVal,newVal) -> {
    		if (endPicker.getValue().isBefore(newVal)) {
    			cfmbtn.setDisable(true);
    		} else cfmbtn.setDisable(false);
    	});
    	endPicker.valueProperty().addListener((arg,oldVal,newVal) -> {if(startPicker.getValue().isAfter(newVal)) cfmbtn.setDisable(true); else cfmbtn.setDisable(false);});
        colLicense.setCellValueFactory(cellData -> cellData.getValue().getLicenseProp());
        colType.setCellValueFactory(cellData -> cellData.getValue().getTypeProp());
        colBlock.setCellValueFactory(cellData -> cellData.getValue().getBlockProp());
        colStart.setCellValueFactory(cellData -> cellData.getValue().getStartProp());
        colEnd.setCellValueFactory(cellData -> cellData.getValue().getEndProp());
        colElapsed.setCellValueFactory(cellData -> cellData.getValue().getElapsedProp());
        InstanceTableView.getColumns().addAll(colLicense, colType, colStart, colEnd, colElapsed, colBlock);
    }
    
    public void cfmbtnclk() {
    	try {
            //tim kiem tong luot gui theo dieu kien va dua ra man hinh
            conn = DatabaseController.connect(conn);
            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM `parkinglot`.`instance` WHERE `type` = ? AND (`startTime` BETWEEN ? AND ?)");
            stmt.setTimestamp(2, Timestamp.valueOf(startPicker.getValue().atStartOfDay()));
            if(!startPicker.getValue().equals(endPicker.getValue())) stmt.setTimestamp(3, Timestamp.valueOf(endPicker.getValue().atStartOfDay(ZoneId.of("+7")).toLocalDateTime()));
            else stmt.setTimestamp(3, Timestamp.valueOf(endPicker.getValue().plusDays(1).atStartOfDay(ZoneId.of("+7")).toLocalDateTime()));
            stmt.setString(1, vehicleMenu.getValue());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                insLbl.setText(rs.getString(1));
            }
            rs.close();

            //tim kiem cac luot gui theo dieu kien va dua ra bang tren man hinh 
            PreparedStatement stmt1 = conn.prepareStatement("SELECT * FROM `parkinglot`.`instance` WHERE `type` = ? AND (`startTime` BETWEEN ? AND ?)");
            stmt1.setTimestamp(2, Timestamp.valueOf(startPicker.getValue().atStartOfDay()));
            if(!startPicker.getValue().equals(endPicker.getValue())) stmt1.setTimestamp(3, Timestamp.valueOf(endPicker.getValue().atStartOfDay(ZoneId.of("+7")).toLocalDateTime()));
            else stmt1.setTimestamp(3, Timestamp.valueOf(endPicker.getValue().plusDays(1).atStartOfDay(ZoneId.of("+7")).toLocalDateTime()));
            stmt1.setString(1, vehicleMenu.getValue());
            ResultSet rs1 = stmt1.executeQuery();
            
            this.dataInstance = FXCollections.observableArrayList();
            while (rs1.next()) {
                visibleInstance vi = new visibleInstance(
                        rs1.getString(3),
                        rs1.getString(4),
                        rs1.getTimestamp(5),
                        rs1.getTimestamp(6),
                        rs1.getString(7),
                        rs1.getString(8)
                );
                        this.dataInstance.add(vi);
        }
    	InstanceTableView.setItems(this.dataInstance);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
	}
    }
}

