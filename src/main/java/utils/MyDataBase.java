package utils;

import java.sql.*;

public class MyDataBase {
    private Connection conn;
    String url="jdbc:mysql://localhost:3306/pi_projet";
    String user="root";
    String pwd="";
    private static MyDataBase instance;
    public MyDataBase(){
        try {
            conn= DriverManager.getConnection(url, user, pwd);
            System.out.println("Connected");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static MyDataBase getInstance(){
        if(instance == null){
            instance=new MyDataBase();
        }
        return instance;
    }
    public Connection getconn(){
        return conn;
    }
}
