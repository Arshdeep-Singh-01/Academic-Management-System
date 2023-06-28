package software.database;

import java.sql.Connection;
import java.sql.DriverManager;

import software.student.Constants;

public class DBfunctions {
    public Connection connect_to_db(String DBname,String user ,String pass){
        Constants constants = new Constants();
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(constants.DB_URL+DBname,user,pass);
            if(conn!=null){
                
                System.out.println("Connection to "+ DBname + ", Success");
            }
            else{
                System.out.println("Connection to "+ DBname + ", Failed");
            }
        } catch (Exception e) {
            // handle exception
            System.out.println("Error at Connection Stage: "+e);
        }
        return conn;
    }
}
