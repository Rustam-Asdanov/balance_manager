package extraWindow.Details;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import extraWindow.extraBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.AnchorPane;

public class ControllerMoreInfo {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane extraAnchor;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Table> table;

    @FXML
    private TableColumn<Table, Integer> idColumn;

    @FXML
    private TableColumn<Table, String> noteColumn;

    @FXML
    private TableColumn<Table, String> catColumn;

    @FXML
    private TableColumn<Table, Integer> sumColumn;

    @FXML
    private TableColumn<Table, Date> dateColumn;

    @FXML
    private Label amountLabel;

    @FXML
    private Label sumLabel;

    ObservableList<Table> tableList = FXCollections.observableArrayList();
    static String category = "";

    @FXML
    void filterFunction(ActionEvent event) {
        if(startDate.getValue() != null && endDate.getValue() != null){
            tableList = extraBase.getTableDate(category,java.sql.Date.valueOf(startDate.getValue()),java.sql.Date.valueOf(endDate.getValue()));
            if(tableList.isEmpty()) table.setPlaceholder(new Label("Таблица пустая, выберите даты."));
            else setLabels(tableList);
        }
        else if(startDate.getValue() == null) startDate.setStyle("-fx-border-color:red;");
        else endDate.setStyle("-fx-border-color:red;");

        FilteredList<Table> filteredList = new FilteredList<>(tableList, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) ->{
            try{
                filteredList.setPredicate(p-> p.getSum() == Double.parseDouble(newValue));
            } catch (NumberFormatException ex) {
                filteredList.setPredicate(p -> p.getCategory().toLowerCase().startsWith(newValue.toLowerCase().trim()));
            } finally {
                setLabels(filteredList);
            }
        });

    }

    public void setLabels(ObservableList<Table> list){
        table.setItems(list);
        amountLabel.setText(""+list.size());
        double sum = 0.0;
        for(Table t : list) sum+=t.getSum();
        sumLabel.setText(""+sum);
    }

    @FXML
    void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<Table, Integer>("id"));
        catColumn.setCellValueFactory(new PropertyValueFactory<Table, String>("category"));
        noteColumn.setCellValueFactory(new PropertyValueFactory<Table, String>("note"));
        sumColumn.setCellValueFactory(new PropertyValueFactory<Table, Integer>("sum"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Table, Date>("date"));
    }

    public static void setCategory(String text) {category = text;}

    public void searchFunction(InputMethodEvent inputMethodEvent) {

    }
}
