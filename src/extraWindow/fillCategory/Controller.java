package extraWindow.fillCategory;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

import extraWindow.extraBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Controller {

    @FXML
    public Label labelCategory;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane extraAnchor;

    @FXML
    private TextField categoryField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField sumField;

    @FXML
    private DatePicker dataField;

    @FXML
    private Label warningLabel;

    private static String category = "";
    private static String categoryName = "";

    @FXML
    void submitFunction(ActionEvent event) {
        if(check()) return;
        try{
            Double.parseDouble(sumField.getText());
        } catch (NumberFormatException nex){
            warningLabel.setText("В поле \"Сумма " + category + "\" должно быть числовое значение." );
            return;
        }
        if(!nameField.getText().isEmpty() && !sumField.getText().isEmpty() && !dataField.getValue().equals(0)){
            extraBase.addInfo(category,categoryName,nameField.getText(),Date.valueOf(dataField.getValue()),Double.parseDouble(sumField.getText()));
        }
        inside.Controller.setBalance();
        ((Stage) extraAnchor.getScene().getWindow()).close();
    }

    @FXML
    void initialize() {
        categoryField.setText(categoryName);
        if(category.equals("income"))labelCategory.setText("дохода:");
        else labelCategory.setText("расхода:");
    }

    public static void fillEmptyStrings(String categ, String categName){
        category = categ;
        categoryName = categName;
    }

    public boolean check(){
        boolean status = false;
        if(nameField.getText().isEmpty()) {
            nameField.setStyle("-fx-border-color: red;");
            status = true;
        }
        if(dataField.getValue() == null){
            dataField.setStyle("-fx-border-color: red");
            status = true;
        }
        if(sumField.getText().isEmpty()){
            sumField.setStyle("-fx-border-color: red");
            status = true;
        }
        return status;
    }
}
