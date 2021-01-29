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
import java.util.List;
import phuchgt.db.MyConnection;
import phuchgt.dto.FoodDTO;

/**
 *
 * @author mevrthisbang
 */
public class FoodDAO implements Serializable {

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

    public int getNumbersOfFoodBySearch(String search, float from, float to, String cateID) throws Exception {
        int result = 0;
        try {
            conn = MyConnection.getMyConnection();
            String sql;
            if (from > 0 && to <= 0) {
                sql = "Select Count(*) as NoOfRecords\n"
                        + "From (\n"
                        + "Select itemID, name, img, price, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active' AND quantity>0\n"
                        + "AND name LIKE ?\n"
                        + "INTERSECT\n"
                        + "Select itemID, name, img, price, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active' AND quantity>0\n"
                        + "AND price>=? \n"
                        + "AND price<=(Select MAX(price) From FOODANDDRINK)\n"
                        + "INTERSECT \n"
                        + "Select itemID, name, img, price, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active' AND quantity>0\n"
                        + "AND category LIKE ?\n"
                        + ") I";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, "%" + search + "%");
                preStm.setFloat(2, from);
                if (cateID.isEmpty()) {
                    preStm.setString(3, "%" + cateID + "%");
                } else {
                    preStm.setString(3, cateID);
                }
            } else if (from <= 0 && to <= 0) {
                sql = "Select Count(*) as NoOfRecords\n"
                        + "From (\n"
                        + "Select itemID, name, img, price, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active' AND quantity>0\n"
                        + "AND name LIKE ?\n"
                        + "INTERSECT\n"
                        + "Select itemID, name, img, price, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active' AND quantity>0\n"
                        + "AND price>=(Select MIN(price) From FOODANDDRINK) \n"
                        + "AND price<=(Select MAX(price) From FOODANDDRINK)\n"
                        + "INTERSECT \n"
                        + "Select itemID, name, img, price, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active' AND quantity>0\n"
                        + "AND category LIKE ?\n"
                        + ") I";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, "%" + search + "%");
                if (cateID.isEmpty()) {
                    preStm.setString(2, "%" + cateID + "%");
                } else {
                    preStm.setString(2, cateID);
                }
            } else {
                sql = "Select Count(*) as NoOfRecords\n"
                        + "From (\n"
                        + "Select itemID, name, img, price, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active' AND quantity>0\n"
                        + "AND name LIKE ?\n"
                        + "INTERSECT\n"
                        + "Select itemID, name, img, price, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active' AND quantity>0\n"
                        + "AND price>=? \n"
                        + "AND price<=?\n"
                        + "INTERSECT \n"
                        + "Select itemID, name, img, price, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active' AND quantity>0\n"
                        + "AND category LIKE ?\n"
                        + ") I";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, "%" + search + "%");
                preStm.setFloat(2, from);
                preStm.setFloat(3, to);
                if (cateID.isEmpty()) {
                    preStm.setString(4, "%" + cateID + "%");
                } else {
                    preStm.setString(4, cateID);
                }
            }
            rs = preStm.executeQuery();
            if (rs.next()) {
                result = rs.getInt("NoOfRecords");
            }
        } finally {
            closeConnection();
        }
        return result;
    }

    public boolean delete(String id, String updateBy) throws Exception {
        boolean check = false;
        try {
            conn = MyConnection.getMyConnection();
            String sql = "Update FOODANDDRINK\n"
                    + "Set status='Inactive', updateBy=?, updateDate=GETDATE()\n"
                    + "Where itemID=?";
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, updateBy);
            preStm.setString(2, id);
            check = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
        return check;
    }

    public List<FoodDTO> searchFood(int offset, int pageSize, String search, float from, float to, String cateID) throws Exception {
        List<FoodDTO> result = null;
        FoodDTO dto = null;
        String id, name, img;
        float price;
        try {
            conn = MyConnection.getMyConnection();
            String sql;
            if (from > 0 && to <= 0) {
                sql = "Select itemID, name, img, price, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active' AND quantity>0\n"
                        + "AND name LIKE ?\n"
                        + "INTERSECT\n"
                        + "Select itemID, name, img, price, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active' AND quantity>0\n"
                        + "AND price>=?\n"
                        + "AND price<=(Select MAX(price) From FOODANDDRINK)\n"
                        + "INTERSECT \n"
                        + "Select itemID, name, img, price, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active' AND quantity>0\n"
                        + "AND category LIKE ?\n"
                        + "ORDER BY createDate\n"
                        + "OFFSET ? ROWS\n"
                        + "FETCH NEXT ? ROWS ONLY";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, "%" + search + "%");
                preStm.setFloat(2, from);
                if (cateID.isEmpty()) {
                    preStm.setString(3, "%" + cateID + "%");
                } else {
                    preStm.setString(3, cateID);
                }
                preStm.setInt(4, offset);
                preStm.setInt(5, pageSize);
            } else if (from <= 0 && to <= 0) {
                sql = "Select itemID, name, img, price, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active' AND quantity>0\n"
                        + "AND name LIKE ?\n"
                        + "INTERSECT\n"
                        + "Select itemID, name, img, price, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active' AND quantity>0\n"
                        + "AND price>=(Select MIN(price) From FOODANDDRINK)\n"
                        + "AND price<=(Select MAX(price) From FOODANDDRINK)\n"
                        + "INTERSECT \n"
                        + "Select itemID, name, img, price, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active' AND quantity>0\n"
                        + "AND category LIKE ?\n"
                        + "ORDER BY createDate\n"
                        + "OFFSET ? ROWS\n"
                        + "FETCH NEXT ? ROWS ONLY";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, "%" + search + "%");
                if (cateID.isEmpty()) {
                    preStm.setString(2, "%" + cateID + "%");
                } else {
                    preStm.setString(2, cateID);
                }
                preStm.setInt(3, offset);
                preStm.setInt(4, pageSize);

            } else {
                sql = "Select itemID, name, img, price, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active' AND quantity>0\n"
                        + "AND name LIKE ?\n"
                        + "INTERSECT\n"
                        + "Select itemID, name, img, price, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active' AND quantity>0\n"
                        + "AND price>=?\n"
                        + "AND price<=?\n"
                        + "INTERSECT \n"
                        + "Select itemID, name, img, price, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active' AND quantity>0\n"
                        + "AND category LIKE ?\n"
                        + "ORDER BY createDate\n"
                        + "OFFSET ? ROWS\n"
                        + "FETCH NEXT ? ROWS ONLY";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, "%" + search + "%");
                preStm.setFloat(2, from);
                preStm.setFloat(3, to);
                if (cateID.isEmpty()) {
                    preStm.setString(4, "%" + cateID + "%");
                } else {
                    preStm.setString(4, cateID);
                }
                preStm.setInt(5, offset);
                preStm.setInt(6, pageSize);
            }
            rs = preStm.executeQuery();
            result = new ArrayList<>();
            while (rs.next()) {
                id = rs.getString("itemID");
                name = rs.getString("name");
                img = rs.getString("img");
                price = rs.getFloat("price");
                dto = new FoodDTO(id, name, img, price);
                result.add(dto);
            }
        } finally {
            closeConnection();
        }
        return result;
    }

    public int getNumbersOfFoodBySearchForAdmin(String search, float from, float to, String cateID) throws Exception {
        int result = 0;
        try {
            conn = MyConnection.getMyConnection();
            String sql;
            if (from > 0 && to <= 0) {
                sql = "Select Count(*) as NoOfRecords\n"
                        + "From (\n"
                        + "Select itemID, name, img, price, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active'\n"
                        + "AND name LIKE ?\n"
                        + "INTERSECT\n"
                        + "Select itemID, name, img, price, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active'\n"
                        + "AND price>=? \n"
                        + "AND price<=(Select MAX(price) From FOODANDDRINK)\n"
                        + "INTERSECT \n"
                        + "Select itemID, name, img, price, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active'\n"
                        + "AND category LIKE ?\n"
                        + ") I";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, "%" + search + "%");
                preStm.setFloat(2, from);
                if (cateID.isEmpty()) {
                    preStm.setString(3, "%" + cateID + "%");
                } else {
                    preStm.setString(3, cateID);
                }
            } else if (from <= 0 && to <= 0) {
                sql = "Select Count(*) as NoOfRecords\n"
                        + "From (\n"
                        + "Select itemID, name, img, price, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active'\n"
                        + "AND name LIKE ?\n"
                        + "INTERSECT\n"
                        + "Select itemID, name, img, price, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active'\n"
                        + "AND price>=(Select MIN(price) From FOODANDDRINK) \n"
                        + "AND price<=(Select MAX(price) From FOODANDDRINK)\n"
                        + "INTERSECT \n"
                        + "Select itemID, name, img, price, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active'\n"
                        + "AND category LIKE ?\n"
                        + ") I";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, "%" + search + "%");
                if (cateID.isEmpty()) {
                    preStm.setString(2, "%" + cateID + "%");
                } else {
                    preStm.setString(2, cateID);
                }
            } else {
                sql = "Select Count(*) as NoOfRecords\n"
                        + "From (\n"
                        + "Select itemID, name, img, price, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active'\n"
                        + "AND name LIKE ?\n"
                        + "INTERSECT\n"
                        + "Select itemID, name, img, price, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active'\n"
                        + "AND price>=? \n"
                        + "AND price<=?\n"
                        + "INTERSECT \n"
                        + "Select itemID, name, img, price, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active'\n"
                        + "AND category LIKE ?\n"
                        + ") I";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, "%" + search + "%");
                preStm.setFloat(2, from);
                preStm.setFloat(3, to);
                if (cateID.isEmpty()) {
                    preStm.setString(4, "%" + cateID + "%");
                } else {
                    preStm.setString(4, cateID);
                }
            }
            rs = preStm.executeQuery();
            if (rs.next()) {
                result = rs.getInt("NoOfRecords");
            }
        } finally {
            closeConnection();
        }
        return result;
    }

    public List<FoodDTO> searchFoodAdmin(int offset, int pageSize, String search, float from, float to, String cateID) throws Exception {
        List<FoodDTO> result = null;
        FoodDTO dto = null;
        String id, name, img;
        float price;
        int quantity;
        try {
            conn = MyConnection.getMyConnection();
            String sql;
            if (from > 0 && to <= 0) {
                sql = "Select itemID, name, img, price, quantity, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active'\n"
                        + "AND name LIKE ?\n"
                        + "INTERSECT\n"
                        + "Select itemID, name, img, price, quantity, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active'\n"
                        + "AND price>=?\n"
                        + "AND price<=(Select MAX(price) From FOODANDDRINK)\n"
                        + "INTERSECT \n"
                        + "Select itemID, name, img, price, quantity, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active'\n"
                        + "AND category LIKE ?\n"
                        + "ORDER BY createDate\n"
                        + "OFFSET ? ROWS\n"
                        + "FETCH NEXT ? ROWS ONLY";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, "%" + search + "%");
                preStm.setFloat(2, from);
                if (cateID.isEmpty()) {
                    preStm.setString(3, "%" + cateID + "%");
                } else {
                    preStm.setString(3, cateID);
                }
                preStm.setInt(4, offset);
                preStm.setInt(5, pageSize);
            } else if (from <= 0 && to <= 0) {
                sql = "Select itemID, name, img, price, quantity, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active'\n"
                        + "AND name LIKE ?\n"
                        + "INTERSECT\n"
                        + "Select itemID, name, img, price, quantity, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active'\n"
                        + "AND price>=(Select MIN(price) From FOODANDDRINK)\n"
                        + "AND price<=(Select MAX(price) From FOODANDDRINK)\n"
                        + "INTERSECT \n"
                        + "Select itemID, name, img, price, quantity, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active'\n"
                        + "AND category LIKE ?\n"
                        + "ORDER BY createDate\n"
                        + "OFFSET ? ROWS\n"
                        + "FETCH NEXT ? ROWS ONLY";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, "%" + search + "%");
                if (cateID.isEmpty()) {
                    preStm.setString(2, "%" + cateID + "%");
                } else {
                    preStm.setString(2, cateID);
                }
                preStm.setInt(3, offset);
                preStm.setInt(4, pageSize);

            } else {
                sql = "Select itemID, name, img, price, quantity, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active'\n"
                        + "AND name LIKE ?\n"
                        + "INTERSECT\n"
                        + "Select itemID, name, img, price, quantity, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active'\n"
                        + "AND price>=?\n"
                        + "AND price<=?\n"
                        + "INTERSECT \n"
                        + "Select itemID, name, img, price, quantity, createDate\n"
                        + "From FOODANDDRINK\n"
                        + "Where status='Active'\n"
                        + "AND category LIKE ?\n"
                        + "ORDER BY createDate\n"
                        + "OFFSET ? ROWS\n"
                        + "FETCH NEXT ? ROWS ONLY";
                preStm = conn.prepareStatement(sql);
                preStm.setString(1, "%" + search + "%");
                preStm.setFloat(2, from);
                preStm.setFloat(3, to);
                if (cateID.isEmpty()) {
                    preStm.setString(4, "%" + cateID + "%");
                } else {
                    preStm.setString(4, cateID);
                }
                preStm.setInt(5, offset);
                preStm.setInt(6, pageSize);
            }
            rs = preStm.executeQuery();
            result = new ArrayList<>();
            while (rs.next()) {
                id = rs.getString("itemID");
                name = rs.getString("name");
                img = rs.getString("img");
                price = rs.getFloat("price");
                quantity = rs.getInt("quantity");
                dto = new FoodDTO(id, name, img, price);
                dto.setQuantity(quantity);
                result.add(dto);
            }
        } finally {
            closeConnection();
        }
        return result;
    }

    public boolean create(FoodDTO food, String createBy) throws Exception {
        boolean check = false;
        try {
            conn = MyConnection.getMyConnection();
            String sql = "Insert Into FOODANDDRINK(itemID, name, price, img, description, quantity, createBy, category)\n"
                    + "Values(?,?,?,?,?,?,?,?)";
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, food.getId());
            preStm.setString(2, food.getName());
            preStm.setFloat(3, food.getPrice());
            preStm.setString(4, food.getImg());
            preStm.setString(5, food.getDescription());
            preStm.setInt(6, food.getQuantity());
            preStm.setString(7, createBy);
            preStm.setString(8, food.getCategory());
            check = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
        return check;
    }

    public String getLastFoodID() throws Exception {
        String result = null;
        try {
            conn = MyConnection.getMyConnection();
            String sql = "Select itemID From FOODANDDRINK\n"
                    + "Where createDate=(Select MAX(CreateDate)\n"
                    + "From FOODANDDRINK)";
            preStm = conn.prepareStatement(sql);
            rs = preStm.executeQuery();
            if (rs.next()) {
                result = rs.getString("itemID");
            }
        } finally {
            closeConnection();
        }
        return result;
    }

    public FoodDTO getFoodByPrimaryKey(String foodID) throws Exception {
        FoodDTO result = null;
        try {
            conn = MyConnection.getMyConnection();
            String sql = "Select name, price, img, description, status, quantity, category\n"
                    + "From FOODANDDRINK\n"
                    + "Where itemID=?";
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, foodID);
            rs = preStm.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String img = rs.getString("img");
                float price = rs.getFloat("price");
                String description = rs.getString("description");
                String status = rs.getString("status");
                int quantity = rs.getInt("quantity");
                String category = rs.getString("category");
                result = new FoodDTO(foodID, name, img, price);
                result.setCategory(category);
                result.setDescription(description);
                result.setQuantity(quantity);
                result.setStatus(status);
            }
        } finally {
            closeConnection();
        }
        return result;
    }

    public boolean update(FoodDTO food, String updateBy) throws Exception {
        boolean check = false;
        try {
            conn = MyConnection.getMyConnection();
            String sql = "Update FOODANDDRINK\n"
                    + "Set name=?, price=?, description=?, status=?, quantity=?, category=?, updateBy=?, updateDate=GETDATE()\n"
                    + "Where itemID=?";
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, food.getName());
            preStm.setFloat(2, food.getPrice());
            preStm.setString(3, food.getDescription());
            preStm.setString(4, food.getStatus());
            preStm.setInt(5, food.getQuantity());
            preStm.setString(6, food.getCategory());
            preStm.setString(7, updateBy);
            preStm.setString(8, food.getId());
            check = preStm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
        return check;
    }

    public List<FoodDTO> getFoodByOrderID(String orderID) throws Exception {
        List<FoodDTO> result = null;
        try {
            conn = MyConnection.getMyConnection();
            String sql = "Select F.name, O.price, O.quantity\n"
                    + "From FOODANDDRINK F JOIN ORDERDETAIL O ON F.itemID=O.foodID \n"
                    + "Where O.orderID=?";
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, orderID);
            rs = preStm.executeQuery();
            result = new ArrayList<>();
            while (rs.next()) {
                String name = rs.getString("name");
                float price = rs.getFloat("price");
                int quantity = rs.getInt("quantity");
                FoodDTO food = new FoodDTO();
                food.setName(name);
                food.setPrice(price);
                food.setQuantity(quantity);
                result.add(food);
            }
        } finally {
            closeConnection();
        }
        return result;
    }

    public int getQuantityByFoodID(String foodID) throws Exception {
        int result = 0;
        try {
            conn = MyConnection.getMyConnection();
            String sql = "Select quantity\n"
                    + "From FOODANDDRINK\n"
                    + "Where itemID=? and status='Active'";
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, foodID);
            rs = preStm.executeQuery();
            if (rs.next()) {
                result = rs.getInt("quantity");
            }
        } finally {
            closeConnection();
        }
        return result;
    }

    public List<FoodDTO> getFoodPurchasedMany() throws Exception {
        List<FoodDTO> result = null;
        FoodDTO dto = null;
        String id, name, img;
        float price;
        try {
            conn = MyConnection.getMyConnection();
            String sql = "Select TOP 8 itemID, name, price, img\n"
                    + "From FOODANDDRINK\n"
                    + "Where status='Active' AND quantity>0 AND counter>0"
                    + "Order by counter DESC";
            preStm = conn.prepareStatement(sql);
            rs = preStm.executeQuery();
            result = new ArrayList<>();
            while (rs.next()) {
                id = rs.getString("itemID");
                name = rs.getString("name");
                img = rs.getString("img");
                price = rs.getFloat("price");
                dto = new FoodDTO(id, name, img, price);
                result.add(dto);
            }
        } finally {
            closeConnection();
        }
        return result;
    }

    public List<FoodDTO> getFoodUserMayFavorite(String username) throws Exception {
        List<FoodDTO> result = null;
        FoodDTO dto = null;
        String id, name, img;
        float price;
        try {
            conn = MyConnection.getMyConnection();
            String sql = "Select top 8 itemID, name, price, img\n"
                    + "From FOODANDDRINK\n"
                    + "Where category IN(\n"
                    + "Select distinct category\n"
                    + "From FOODANDDRINK\n"
                    + "Where itemID IN(Select foodID\n"
                    + "From ORDERDETAIL\n"
                    + "Where orderID IN(Select orderID\n"
                    + "From ORDERS\n"
                    + "Where customer=?))) AND itemID NOT IN(Select foodID\n"
                    + "From ORDERDETAIL\n"
                    + "Where orderID IN(Select orderID\n"
                    + "From ORDERS\n"
                    + "Where customer=?)) and status='Active' and quantity>0";
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, username);
            preStm.setString(2, username);
            rs = preStm.executeQuery();
            result = new ArrayList<>();
            while (rs.next()) {
                id = rs.getString("itemID");
                name = rs.getString("name");
                img = rs.getString("img");
                price = rs.getFloat("price");
                dto = new FoodDTO(id, name, img, price);
                result.add(dto);
            }
        } finally {
            closeConnection();
        }
        return result;
    }
}
