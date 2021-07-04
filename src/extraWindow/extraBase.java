package extraWindow;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import extraWindow.Details.Table;
import inside.Base;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

public class extraBase {
    private static Connection conn = null;
    private static Statement statement = null;

    static{
        conn = Base.getConnection();
        try {
            statement = conn.createStatement();
            statement.executeUpdate("SET SQL_SAFE_UPDATES = 0;");
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }

    public static boolean addInfo(String category, String categoryName, String categroyFullName, java.sql.Date date, Double amount){
        System.out.println(date);
        String querry = String.format("UPDATE %s SET " +
                "%s_name = '%s', %s_date = '%tF', %s_money = %.2f " +
                "WHERE %s_category = '%s';",category,category,categroyFullName,category,date,category,amount,category,categoryName);
        System.out.println(querry);
        try {
            statement.executeUpdate(querry);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    public static ObservableList<Table> getTableDate(String category, Date startDate, Date endDate){
        ObservableList<Table> tableList = FXCollections.observableArrayList();
        String querry = String.format("select * from %s where %s_date > '%tF' AND %s_date < '%tF';",category,category,startDate,category,endDate);
        int count = 1;
        try {
            ResultSet resultSet = statement.executeQuery(querry);
            while(resultSet.next()){
                tableList.add(new Table(
                        count++,resultSet.getString(category+"_category"),resultSet.getString(category+"_name"),
                        resultSet.getDouble(category+"_money"),
                        resultSet.getDate(category+"_date")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return tableList;
    }

    public static void createPlan
            (String nameOfPlan, ObservableList<String> categName, ObservableList<Double> consumption, Date startDate, Date endDate, TextField field)
    {
        String querryOne = String.format("create table %s (" +
                "id int AUTO_INCREMENT primary key," +
                "category_name varchar(255) not null," +
                "consumption double," +
                "start_date date," +
                "end_date date)",nameOfPlan);
        try {
            statement.execute(querryOne);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("problem with create table");
        }

        for(int i=0; i<categName.size();i++){
            String querryTwo = String.format("insert into %s" +
                    "(category_name, consumption, start_date, end_date) values " +
                    "('%s', %.2f, '%tF', '%tF');", nameOfPlan, categName.get(i), consumption.get(i),startDate,endDate);
            try {
                statement.executeUpdate(querryTwo);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                System.out.println("Problem with insert");
                field.setPromptText("Такая категория уже существует.");
            }
        }
    }

    public static void getPlanName(ListView<String> listView){
        try {
            ObservableList<String> list = FXCollections.observableArrayList();
            ResultSet resultSet = conn.getMetaData().getTables(conn.getCatalog(),null,"%",null);
            while(resultSet.next()){
                if(resultSet.getString(3).equalsIgnoreCase("income") ||
                resultSet.getString(3).equalsIgnoreCase("consumption")) continue;
                list.add(resultSet.getString(3));
            }

            listView.setItems(list);} catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    public static void choosePlan(ListView listView, String nameOfPlan){
        ObservableList endOfPlan = FXCollections.observableArrayList();
//        listView.cellFactoryProperty().
        String query = String.format("select * from " + nameOfPlan + ";");
        try {
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                endOfPlan.add(
                        resultSet.getString("category_name")  +
                                ": Выделено денег " + resultSet.getDouble("consumption")  +
                                (new ProgressBar(0.2)).getProgress() + " Потраченная сумма: 0.0"
                );

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        listView.setItems(endOfPlan);
    }

    public static void delete(String elem){
        String query = "drop table " + elem +";";
        try {
            statement.execute(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
