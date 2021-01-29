/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phuchgt.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import phuchgt.db.MyConnection;
import phuchgt.dto.AccountDTO;

/**
 *
 * @author mevrthisbang
 */
public class AccountDAO implements Serializable{
    private Connection conn;
    private PreparedStatement preStm;
    private ResultSet rs;

    private void closeConnection() throws Exception {
        if (rs != null) {
            rs.close();
        }
        if (preStm != null) {
            preStm.close();
        }
        if (conn != null) {
            conn.close();
        }
    }
    public AccountDTO checkLogin(String username, String password) throws Exception{
        AccountDTO result=null;
        try {
            conn=MyConnection.getMyConnection();
            String sql="Select fullname, phone, address, role, email\n"
                    + "From ACCOUNT\n"
                    + "Where username=? AND password=?";
            preStm=conn.prepareStatement(sql);
            preStm.setString(1, username);
            preStm.setString(2, password);
            rs=preStm.executeQuery();
            if(rs.next()){
                String fullname=rs.getString("fullname");
                String phone=rs.getString("phone");
                String address=rs.getString("address");
                String role=rs.getString("role");
                String email=rs.getString("email");
                result=new AccountDTO(username, fullname, phone, address, role);
                result.setEmail(email);
            }
        } finally{
            closeConnection();
        }
        return result;
    }
    public AccountDTO checkLoginByEmail(String email) throws Exception{
        AccountDTO result=null;
        try {
            conn=MyConnection.getMyConnection();
            String sql="Select username, fullname, phone, address, role\n"
                    + "From ACCOUNT\n"
                    + "Where email=?";
            preStm=conn.prepareStatement(sql);
            preStm.setString(1, email);
            rs=preStm.executeQuery();
            if(rs.next()){
                String username=rs.getString("username");
                String fullname=rs.getString("fullname");
                String phone=rs.getString("phone");
                String address=rs.getString("address");
                String role=rs.getString("role");
                result=new AccountDTO(username, fullname, phone, address, role);
            }
        } finally{
            closeConnection();
        }
        return result;
    }
}
