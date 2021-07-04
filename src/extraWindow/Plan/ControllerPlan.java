package extraWindow.Plan;

import java.net.URL;
import java.sql.Date;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.stream.DoubleStream;

import DataBase.Validation;
import extraWindow.extraBase;
import inside.Base;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ControllerPlan {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private DatePicker planStartDate;

    @FXML
    private DatePicker planEndDate;

    @FXML
    private TextField planNameField;

    @FXML
    private ListView<String> planAllCategList;

    @FXML
    private ListView<Double> planAllMoneyList;

    @FXML
    private Label currentBalanceLabel;

    @FXML
    private Label sumOfMoneyLabel;

    @FXML
    private TextField moneyField;

    ObservableList<String> allCategoriesList = FXCollections.observableArrayList();
    ObservableList<Double> consumptionForCateg = FXCollections.observableArrayList();

    @FXML
    void congirmFunction(ActionEvent event) {
        if (Validation.isEmptyField(planNameField)) return;
        if (Validation.isEmptyDate(planStartDate, planEndDate)) return;
        Alert alert = new Alert(Alert.AlertType.NONE, "Вы уверены?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES){}
            extraBase.createPlan(
                    planNameField.getText(), allCategoriesList, consumptionForCateg,
                    Date.valueOf(planStartDate.getValue()), Date.valueOf(planEndDate.getValue()), planNameField
            );
    }

    private static String currentBalance = ""+0.0;

    @FXML
    void initialize() {
        currentBalanceLabel.setText(currentBalance);
        allCategoriesList.addAll(Base.getCategories("income"));
        allCategoriesList.addAll(Base.getCategories("consumption"));
        planAllCategList.setItems(allCategoriesList);
        for(int i = 0; i<allCategoriesList.size(); i++){
            consumptionForCateg.add(0.0);
        }
        planAllMoneyList.setItems(consumptionForCateg);

        planAllCategList.getSelectionModel().selectedItemProperty().addListener( (event) ->{
            planAllMoneyList.getSelectionModel().select(planAllCategList.getSelectionModel().getSelectedIndex());
            moneyField.setText(""+planAllMoneyList.getSelectionModel().getSelectedItem());
        });

        planAllMoneyList.getSelectionModel().selectedItemProperty().addListener( (event) ->{
            moneyField.setText("" + planAllMoneyList.getSelectionModel().getSelectedItem());
        });

        moneyField.textProperty().addListener((observable, oldValue, newValue) ->{
            int index = planAllMoneyList.getSelectionModel().getSelectedIndex();
                if(!Validation.isNumber(newValue)) moneyField.setStyle("-fx-border-color: red;");
                else if(!newValue.isEmpty()) {
                    consumptionForCateg.set(index, Double.parseDouble(newValue));
                    moneyField.setStyle("-fx-border-color: none;");
                    planAllMoneyList.setItems(consumptionForCateg);
                    double sum = 0;
                    for (double el : consumptionForCateg) sum += el;
                    sumOfMoneyLabel.setText("" + sum);
                }
        });
    }

    public static void setLabel(String text){
        currentBalance = text;
    }
}
