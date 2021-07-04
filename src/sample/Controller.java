package sample;

import DataBase.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Controller {

    @FXML
    public AnchorPane anchor;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label warningLabel;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    String login;
    String password;

    @FXML
    void removeButton(ActionEvent event) {
        if(!getData()) {
            return;
        }
        boolean status = DataBase.check(login,password);
        warningLabel.setVisible(true);
        if (status) {
            DataBase.remove(login,password);
            warningLabel.setText("Пользователь удачно удалён.");
        } else {
            warningLabel.setText("Ошибка!Такого пользователя не существует.");
        }
    }

    //войти
    @FXML
    void signInButton(ActionEvent event) {
        if(!getData()) {
            return;
        }
        boolean status = DataBase.check(login, password);
        if(status){
            try {
                Parent root = (Parent) FXMLLoader.load(getClass().getResource("/inside/sample.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(root,800,600));
                stage.setTitle("Расчёты пользователя: " + login);
                stage.setResizable(false);
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/money.png")));
                stage.show();
                ((Stage) anchor.getScene().getWindow()).close();
            } catch (IOException e) {
                e.printStackTrace();
                warningLabel.setVisible(true);
                warningLabel.setText("Ощибка какая та.");
            }
        } else {
            warningLabel.setVisible(true);
            warningLabel.setText("Такого пользователя не существует.");
        }
    }

    //пройти регистрацию
    @FXML
    void signUpButton(ActionEvent event) {
        if(!getData()) {
            return;
        }
        boolean status = DataBase.check(login, password);
        warningLabel.setVisible(true);
        if(status){
            warningLabel.setText("Такой пользователь уже существует.");
        } else {
            warningLabel.setText("Вы успешно прошли регистрацию.");
            DataBase.addUser(login,password);
        }
    }

    @FXML
    void initialize() {
        warningLabel.setVisible(false);
        Validation.startWithNum(loginField);
    }

    public boolean getData(){
        login = loginField.getText();
        password = passwordField.getText();
        if(login.isEmpty() || password.isEmpty()) {
            warningLabel.setText("Заполнить все поля.");
            warningLabel.setVisible(true);
            return false;
        }
        warningLabel.setVisible(false);
        return true;
    }
}
