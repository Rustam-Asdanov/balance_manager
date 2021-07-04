package inside;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import DataBase.DataBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Base {
    static Connection conn;
    static Statement statement;
    static {
        DataBase.run();
        conn = DataBase.getConn();
        try {
            statement = conn.createStatement();
            statement.executeUpdate("SET SQL_SAFE_UPDATES = 0;");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static Connection getConnection(){
        return conn;
    }

    public static void changeNameofCat(String category,String oldName,String newName){
        String querry = String.format("update %s " +
                "set %s_category='%s'" +
                "where %s_category='%s';",category,category,newName,category,oldName);
        try {
            statement.executeUpdate(querry);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Проблема с изменением имени категории");
        }
    }

    public static void creteCat(String category, String catName) {
        try {
            String querry = String.format("insert into %s(%s_category) value('%s');",category,category,catName);
            statement.executeUpdate(querry);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void removeCategory(String category, String categoryName){
        try {
            String querry = String.format("delete from %s where %s_category='%s';", category, category, categoryName);
            statement.executeUpdate(querry);
            statement.addBatch("SET @reset = 0;");
            statement.addBatch(String.format("UPDATE %s SET %s_id = @reset:= @reset + 1;",category,category));
            statement.addBatch(String.format("Alter table %s auto_increment = 1;",category));
            statement.executeBatch();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Проблема с удалением.");
        }
    }

    public static ObservableList<String> getCategories(String category){
        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            String querry = String.format("select %s_category from %s;",category,category);
            ResultSet resultSet = statement.executeQuery(querry);
            while(resultSet.next()){
                list.add(resultSet.getString(String.format("%s_category",category)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    public static Double getSum(){
        String querry = "select income_money from income;";
        Double sum = 0.0;
        try {
            ResultSet income_sum = statement.executeQuery("select sum(income_money) from income;");
            income_sum.next();
            sum += income_sum.getDouble(1);
            ResultSet consumption_sum = statement.executeQuery("select sum(consumption_money) from consumption;");
            consumption_sum.next();
            sum -= consumption_sum.getDouble(1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sum;
    }

    //income base

    public static boolean hasDuplicate(String category, String newCategoryName){
        Boolean status = false;
        try {
            String querry = String.format("select %s_category from %s;",category,category);
            ResultSet resultSet = statement.executeQuery(querry);
            while(resultSet.next()){
                if(newCategoryName.equals(resultSet.getString(String.format("%s_category",category)))){
                    status = true;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Problem with check.");
        }
        return status;
    }

    //consumption base


    //plan base
}
