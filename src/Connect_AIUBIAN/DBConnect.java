/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connect_AIUBIAN;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author alger
 */
public class DBConnect {
    Connection conn = null;
    public static Connection DBConnect(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/connect_aiubian","root","");
            return conn;
        }catch(Exception ex){
            System.out.println(ex);
            return null;
        }
            
    }
}
