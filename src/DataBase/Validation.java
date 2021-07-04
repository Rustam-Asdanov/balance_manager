package DataBase;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class Validation {
    public static boolean isEmptyField(TextField...textFields){
        for(TextField tf : textFields){
            if(tf.getText().isEmpty()) {
                tf.setStyle("-fx-border-color: red;");
                return true;
            }
        }
        return false;
    }

    public static boolean isEmptyDate(DatePicker...datePickers){
        for(DatePicker dp : datePickers)
            if(dp.getValue() == null){
                dp.setStyle("-fx-border-color: red;");
                return true;
            }
        return false;
    }

    public static boolean isNumber(String text){
        try{
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException nex){
            return false;
        }
    }
    public static void startWithNum(TextField textField){
        textField.textProperty().addListener((observable, oldValue, newValue) ->{
            if(Character.isAlphabetic(newValue.charAt(0))) textField.setStyle("-fx-border-color: none;");
            else textField.setStyle("-fx-border-color: red;");
        });
    }
}
