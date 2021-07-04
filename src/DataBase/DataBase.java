package DataBase;

import java.sql.*;

public class DataBase {
    private final static String URL = "jdbc:mysql://localhost:3306/balance_manager";
    private final static String LOGIN = "root";
    private final static String PASSWORD = "12345";
    private static Connection conn;
    static String urlForUser = "jdbc:mysql://localhost:3306/";

    public static void run(){
        try {
            conn = DriverManager.getConnection(URL,LOGIN,PASSWORD);
            System.out.println("success data");
        } catch (SQLException throwables) {
            throwables.getMessage();
        }
    }

    public static Connection getConn(){
        Connection userConn = null;
        try {
            userConn = DriverManager.getConnection(urlForUser,LOGIN,PASSWORD);
            Statement statement = userConn.createStatement();
            statement.addBatch("create table income (" +
                    "income_id int auto_increment primary key," +
                    "income_category varchar(255) not null," +
                    "income_name varchar(255)," +
                    "income_date date," +
                    "income_money double);");
            statement.addBatch("create table consumption (" +
                    "consumption_id int auto_increment primary key," +
                    "consumption_category varchar(255) not null," +
                    "consumption_name varchar(255)," +
                    "consumption_date date," +
                    "consumption_money double);");
            statement.executeBatch();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return userConn;
    }

    //add user
    public static boolean addUser(String login, String password){
        boolean status = true;
        String base = login+"base" + (int) (Math.random()*999999) + password;
        try {
            Statement statement = conn.createStatement();
            String querry = String.format(
                    "insert into User(user_login, user_password, user_dataBase) " +
                            "values ( '%s' , '%s', '%s');", login, password, base);
            statement.executeUpdate(querry);
            querry = "create database " + base + ";";
            statement.executeUpdate(querry);
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            status = false;
        }
        return status;
    }

    //remove user
    public static boolean remove(String login, String password){
        boolean status = true;
        try {
            Statement statement = conn.createStatement();
            String querry = String.format(
              "delete from User where user_login = '%s' and user_password = '%s';",login,password
            );
            statement.executeUpdate(querry);
            statement.addBatch("SET SQL_SAFE_UPDATES = 0;");
            statement.addBatch("SET @reset = 0;");
            statement.addBatch("UPDATE User SET user_id = @reset:= @reset + 1;");
            statement.addBatch("Alter table User auto_increment = 1;");
            statement.executeBatch();
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            status = false;
        }
        return status;
    }

    //check user
    public static boolean check(String login, String password){
        boolean status = false;
        try {
            Statement statement = conn.createStatement();
            String querry = "select * from User;";
            ResultSet resultSet = statement.executeQuery(querry);
            while(resultSet.next()){
                if(
                    login.equals(resultSet.getString("user_login")) &&
                    password.equals(resultSet.getString("user_password"))
                ){
                    urlForUser += resultSet.getString("user_dataBase");
                    status = true;
                }
            }
        } catch (SQLException throwables) {
            throwables.getMessage();
        }
        return status;
    }
}
