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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import phuchgt.db.MyConnection;
import phuchgt.dto.FoodDTO;
import phuchgt.dto.OrderDTO;

/**
 *
 * @author mevrthisbang
 */
public class OrderDAO implements Serializable {

    private Connection conn;
    private PreparedStatement preStmOrder;
    private PreparedStatement preStmOrderDetail;
    private ResultSet rs;

    private void closeConnection() throws Exception {
        if (rs != null) {
            rs.close();
        }
        if (preStmOrderDetail != null) {
            preStmOrderDetail.close();
        }
        if (preStmOrder != null) {
            preStmOrder.close();
        }
        if (conn != null) {
            conn.close();
        }
    }

    public String getLastOrderIDByCustomer(String customerID) throws Exception {
        String result = null;
        try {
            conn = MyConnection.getMyConnection();
            String sql = "Select orderID From ORDERS\n"
                    + "Where buyDate=(Select MAX(buyDate)\n"
                    + "From ORDERS Where customer=?)";
            preStmOrder = conn.prepareStatement(sql);
            preStmOrder.setString(1, customerID);
            rs = preStmOrder.executeQuery();
            if (rs.next()) {
                result = rs.getString("orderID");
            }
        } finally {
            closeConnection();
        }
        return result;
    }

    public boolean insertOrder(OrderDTO order, HashMap<String, FoodDTO> listOrderDetail) throws Exception {
        boolean check = false;
        try {
            conn = MyConnection.getMyConnection();
            String sqlInsertOrder = "Insert Into ORDERS(orderID, customer, buyerName, paymentMethod, phone, shipAddress, status)\n"
                    + "Values(?,?,?,?,?,?,?)";
            String sqlInsertOrderDetail = "Insert Into ORDERDETAIL(orderlineID, orderID, foodID, quantity, price)\n"
                    + "Values(?, ?, ?, ?, ?)";
            preStmOrder=conn.prepareStatement(sqlInsertOrder);
            preStmOrderDetail=conn.prepareStatement(sqlInsertOrderDetail);
            conn.setAutoCommit(false);
            preStmOrder.setString(1, order.getOrderID());
            preStmOrder.setString(2, order.getCustomer());
            preStmOrder.setString(3, order.getBuyerName());
            preStmOrder.setString(4, order.getPaymentMethod());
            preStmOrder.setString(5, order.getPhone());
            preStmOrder.setString(6, order.getAddress());
            preStmOrder.setString(7, order.getStatus());
            int insertOrder=preStmOrder.executeUpdate();
            int insertOrderLine=0;
            int count=1;
            for(FoodDTO food: listOrderDetail.values()){
                preStmOrderDetail.setString(1, order.getOrderID()+"-"+count);
                count++;
                preStmOrderDetail.setString(2, order.getOrderID());
                preStmOrderDetail.setString(3, food.getId());
                preStmOrderDetail.setInt(4, food.getQuantity());
                preStmOrderDetail.setFloat(5, food.getPrice());
                insertOrderLine+=preStmOrderDetail.executeUpdate();
            }
            conn.commit();
            conn.setAutoCommit(true);
            check=insertOrder>0&&insertOrderLine>0;
        } finally {
            closeConnection();
        }
        return check;
    }

    public List<OrderDTO> getOrderHistoryByUsername(String username) throws Exception {
        List<OrderDTO> result = null;
        String orderID, buyerName, paymentMethod, phone, shippingAddress, status;
        Date buyDate;
        try {
            conn = MyConnection.getMyConnection();
            String sql = "Select orderID, buyerName, buyDate, paymentMethod, phone, shipAddress, status\n"
                    + "From ORDERS\n"
                    + "Where customer=?\n";
            preStmOrder = conn.prepareStatement(sql);
            preStmOrder.setString(1, username);
            rs = preStmOrder.executeQuery();
            result = new ArrayList<>();
            while (rs.next()) {
                orderID = rs.getString("orderID");
                buyerName = rs.getString("buyerName");
                buyDate = rs.getDate("buyDate");
                paymentMethod = rs.getString("paymentMethod");
                phone = rs.getString("phone");
                shippingAddress = rs.getString("shipAddress");
                status = rs.getString("status");
                OrderDTO order = new OrderDTO(orderID, username, buyerName, paymentMethod, phone, shippingAddress);
                order.setBuyDate(buyDate);
                order.setStatus(status);
                result.add(order);
            }
        } finally {
            closeConnection();
        }
        return result;
    }

    public List<OrderDTO> searchOrderHistory(String search, String date, String username) throws Exception {
        List<OrderDTO> result = null;
        String orderID, buyerName, paymentMethod, phone, shippingAddress, status;
        Date buyDate;
        try {
            conn = MyConnection.getMyConnection();
            String sql;
            if (date.isEmpty()) {
                sql = "Select DISTINCT O.orderID, O.buyerName, O.buyDate, O.paymentMethod, O.phone, O.shipAddress, O.status\n"
                        + "From FOODANDDRINK F JOIN ORDERDETAIL OD ON F.itemID=OD.foodID JOIN ORDERS O ON O.orderID=OD.orderID\n"
                        + "Where F.name LIKE ? AND customer=?";
                preStmOrder = conn.prepareStatement(sql);
                preStmOrder.setString(1, "%" + search + "%");
                preStmOrder.setString(2, username);
            } else {
                sql = "Select DISTINCT O.orderID, O.buyerName, O.buyDate, O.paymentMethod, O.phone, O.shipAddress, O.status\n"
                        + "From FOODANDDRINK F JOIN ORDERDETAIL OD ON F.itemID=OD.foodID JOIN ORDERS O ON O.orderID=OD.orderID\n"
                        + "Where F.name LIKE ? AND CAST(buyDate AS Date )<=? AND customer=?";
                preStmOrder = conn.prepareStatement(sql);
                preStmOrder.setString(1, "%" + search + "%");
                preStmOrder.setString(2, date);
                preStmOrder.setString(3, username);
            }
            rs = preStmOrder.executeQuery();
            result = new ArrayList<>();
            while (rs.next()) {
                orderID = rs.getString("orderID");
                buyerName = rs.getString("buyerName");
                buyDate = rs.getDate("buyDate");
                paymentMethod = rs.getString("paymentMethod");
                phone = rs.getString("phone");
                shippingAddress = rs.getString("shipAddress");
                status = rs.getString("status");
                OrderDTO order = new OrderDTO(orderID, username, buyerName, paymentMethod, phone, shippingAddress);
                order.setBuyDate(buyDate);
                order.setStatus(status);
                result.add(order);
            }
        } finally {
            closeConnection();
        }
        return result;
    }
}
