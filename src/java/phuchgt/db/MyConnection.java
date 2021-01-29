/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phuchgt.db;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author mevrthisbang
 */
public class MyConnection implements Serializable{
    public static Connection getMyConnection() throws Exception{
        Connection conn=null;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        conn=DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=LAB1HANASHOP", "sa", "2!Idolphuc");
        return conn;
    }
}
