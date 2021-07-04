package inside;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import extraWindow.Details.ControllerMoreInfo;
import extraWindow.Plan.ControllerPlan;
import extraWindow.extraBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Controller {

    @FXML
    public static Label balanceLabel = new Label();

    @FXML
    public AnchorPane mainAnchor;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label warning;

    @FXML
    void initialize() {
        mainAnchor.getChildren().add(balanceLabel);
        balanceLabel.setLayoutX(527);
        balanceLabel.setLayoutY(22);
        balanceLabel.setFont(new Font("System Bold Italic",18));
        balanceLabel.setTextFill(Color.web("#b23fbf"));
        System.out.println(Base.conn);
        getData("all");
        setBalance();
        extraBase.getPlanName(planList);

        planList.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) ->{
            if(!newValue.isEmpty()) extraBase.choosePlan(finalList, newValue);
        });
    }

    public static void setBalance(){
        balanceLabel.setText(""+Base.getSum());
    }

    public boolean isEmptyField(){
        if(!inField.getText().isEmpty() || !outField.getText().isEmpty()) return false;
        warning.setText("Поле не может быть пустым.");
        return true;
    }

    public void getData(String category){
        if(category.equals("income") || category.equals("all")){
            inObList = Base.getCategories("income");
            if(inObList.isEmpty()) {
                inList.setItems(null);
                inList.setPlaceholder(new Label("Лист пустой."));
            }
            else inList.setItems(inObList);
        }
        if(category.equals("consumption") || category.equals("all")){
            outObList = Base.getCategories("consumption");
            if(outObList.isEmpty()) {
                outList.setItems(null);
                outList.setPlaceholder(new Label("Лист пустой."));
            }
            else outList.setItems(outObList);
        }
    }

    public void changeCategoryName(String category){
        String oldName = "";
        String newName = "";
        if(isEmptyField())return;
        if(category.equals("income")) {
            oldName = inList.getSelectionModel().getSelectedItem();
            newName = inField.getText();
        } else {
            oldName = outList.getSelectionModel().getSelectedItem();
            newName = outField.getText();
        }
        Base.changeNameofCat(category,oldName,newName);
        getData(category);
    }

    public void createNewCategory(String category){
        if(isEmptyField())return;
        String newCategoryName = "";
        if(category.equals("income")) newCategoryName = inField.getText();
        else newCategoryName = outField.getText();

        if(Base.hasDuplicate(category,newCategoryName)){
            warning.setText("Такое поле уже существует.");
        } else{
            Base.creteCat(category, newCategoryName);
            getData(category);
            warning.setText("Новая категория удачно добавлено.");
        }
    }

    public void removeCategory(String category){
        String categoryName = "";
        if(category.equals("income")) categoryName = inList.getSelectionModel().getSelectedItem();
        else categoryName = outList.getSelectionModel().getSelectedItem();
        Base.removeCategory(category,categoryName);
        getData(category);
    }

    public void openWindow(String category){
        try {
            Parent root = (Parent) FXMLLoader.load(getClass().getResource("/extraWindow/fillCategory/appearance.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root,550,330));
            stage.setTitle("Окно для регистрации нового " + category);
            stage.setResizable(false);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/money.png")));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openDetailsWindow(String titleText){
        try {
            Parent root = (Parent) FXMLLoader.load(getClass().getResource("/extraWindow/Details/appearanceMoreInfo.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root,650,360));
            stage.setTitle("Окно для расчёта "+titleText);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Income category

    @FXML
    private ListView<String> inList;

    @FXML
    private TextField inField;

    ObservableList<String> inObList = FXCollections.observableArrayList();

    @FXML
    void inChange(ActionEvent event) {
        changeCategoryName("income");
    }

    @FXML
    void inNewCat(ActionEvent event) {
        createNewCategory("income");
    }

    @FXML
    void inRemove(ActionEvent event) {
        removeCategory("income");
    }

    @FXML
    void inNewIncome(ActionEvent event) {
        if(inList.getSelectionModel().getSelectedItem() == null) {
            warning.setText("Выберите категорию дохода.");
        } else {
            extraWindow.fillCategory.Controller.fillEmptyStrings("income", inList.getSelectionModel().getSelectedItem());
            openWindow("дохода");
        }
    }

    @FXML
    void inDetails(ActionEvent event) {
        openDetailsWindow("дохода");
        ControllerMoreInfo.setCategory("income");
    }

    //Consumption category

    @FXML
    private ListView<String> outList;

    @FXML
    private TextField outField;

    ObservableList<String> outObList = FXCollections.observableArrayList();

    @FXML
    void outNewCat(ActionEvent event) {
        createNewCategory("consumption");
    }

    @FXML
    void outRemove(ActionEvent event) {
        removeCategory("consumption");
    }

    @FXML
    void outChange(ActionEvent event) {
        changeCategoryName("consumption");
    }

    @FXML
    void outDetails(ActionEvent event) {
        openDetailsWindow("расхода");
        ControllerMoreInfo.setCategory("consumption");
    }

    @FXML
    void outNewConsumption(ActionEvent event) {
        if(outList.getSelectionModel().getSelectedItem() == null) {
            warning.setText("Выберите категорию расхода.");
        } else {
            extraWindow.fillCategory.Controller.fillEmptyStrings("consumption", outList.getSelectionModel().getSelectedItem());
            openWindow("расхода");
        }
    }

    //Plan category

    @FXML
    private ListView<String> planList;

    public void planNew(ActionEvent actionEvent) {
        ControllerPlan.setLabel(balanceLabel.getText());
        try {
            Parent root = (Parent) FXMLLoader.load(getClass().getResource("/extraWindow/Plan/samplePlan.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 600, 400));
            stage.setResizable(false);
            stage.setTitle("Новый план");
            stage.show();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void planOld(ActionEvent actionEvent) {
    }

    public void planActive(ActionEvent actionEvent) {
    }

    public void deleteElement(ActionEvent actionEvent) {
        extraBase.delete(planList.getSelectionModel().getSelectedItem());
        extraBase.getPlanName(planList);
    }
    //Other elements on application

    @FXML
    private ListView<?> finalList;

    @FXML
    private Label overall;

    @FXML
    private Label startData;

    @FXML
    private Label endData;


}