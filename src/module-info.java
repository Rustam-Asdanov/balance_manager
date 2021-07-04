module balance_manager {
    requires javafx.controls;
    requires javafx.base;
    requires javafx.fxml;
    requires java.sql;

    opens sample;
    opens inside;
    opens extraWindow.Details;
    opens extraWindow.fillCategory;
    opens extraWindow.Plan;
}