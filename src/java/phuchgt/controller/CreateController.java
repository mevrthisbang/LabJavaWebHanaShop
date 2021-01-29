/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phuchgt.controller;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import phuchgt.dao.FoodDAO;
import phuchgt.dto.AccountDTO;
import phuchgt.dto.FoodDTO;
import phuchgt.dto.FoodErrorObject;

/**
 *
 * @author mevrthisbang
 */
public class CreateController extends HttpServlet {

    private static final String INVALID = "createForm.jsp";
    private static final String ERROR = "error.jsp";
    private static final String SUCCESS = "SearchController";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR;
        try {
            HttpSession session = request.getSession();
            AccountDTO loginUser = (AccountDTO) session.getAttribute("USER");
            if (loginUser != null && loginUser.getRole().equals("admin")) {
                boolean isMultipart = ServletFileUpload.isMultipartContent(request);
                if (isMultipart) {
                    FileItemFactory factory = new DiskFileItemFactory();
                    ServletFileUpload upload = new ServletFileUpload(factory);
                    List<FileItem> items = upload.parseRequest(request);
                    Hashtable params = new Hashtable();
                    FileItem fileItem = null;
                    for (FileItem item : items) {
                        if (item.isFormField()) {
                            params.put(item.getFieldName(), item.getString());
                        } else {
                            fileItem = item;
                        }
                    }
                    //get parameter
                    String name = (String) params.get("txtName");
                    String inputPrice = (String) params.get("txtPrice");
                    String description = (String) params.get("txtDescription");
                    String inputQuantity = (String) params.get("txtQuantity");
                    String category = (String) params.get("cboCategory");
                    boolean valid = true;
                    FoodErrorObject errorObject = new FoodErrorObject();
                    if (name.isEmpty()) {
                        errorObject.setNameError("Name is not supposed to be empty");
                        valid = false;
                    }
                    float price = 0;
                    try {
                        price = Float.parseFloat(inputPrice);
                        if (price <= 0) {
                            throw new Exception();
                        }
                    } catch (NumberFormatException e) {
                        errorObject.setPriceError("Input number for price");
                        valid = false;
                    } catch (Exception e) {
                        errorObject.setPriceError("Price must > 0");
                        valid = false;
                    }
                    int quantity = 0;
                    try {
                        quantity = Integer.parseInt(inputQuantity);
                        if (quantity <= 0) {
                            throw new Exception();
                        }
                    } catch (NumberFormatException e) {
                        errorObject.setQuantityError("Input number for quantity");
                        valid = false;
                    } catch (Exception e) {
                        errorObject.setQuantityError("Quantity must > 0");
                        valid = false;
                    }
                    if (description.isEmpty()) {
                        errorObject.setDescriptionError("Description is not supposed to be empty");
                        valid = false;
                    }
                    if (fileItem != null && fileItem.getName().isEmpty()) {
                        errorObject.setImgError("Please choose image");
                        valid = false;
                    }
                    if (valid) {
                        FoodDAO dao = new FoodDAO();
                        String lastFoodID = dao.getLastFoodID();
                        String foodID;
                        //Auto inscreasing id
                        if (lastFoodID == null) {
                            foodID = "FD_1";
                        } else {
                            int count = Integer.parseInt(lastFoodID.split("_")[1].trim());
                            foodID = "FD" + "_" + (count + 1);
                        }
                        //upload file to server
                        String dirUrl = getServletContext().getRealPath("") + File.separator + "img";
                        File dir = new File(dirUrl);
                        if (!dir.exists()) {
                            dir.mkdir();
                        }
                        String fileImg = dirUrl + File.separator + foodID + ".jpg";
                        File savedFile = new File(fileImg);
                        fileItem.write(savedFile);
                        //New food
                        FoodDTO food = new FoodDTO(foodID, name, "", price);
                        food.setImg("img/" + foodID + ".jpg");
                        food.setCategory(category);
                        food.setDescription(description);
                        food.setQuantity(quantity);
                        //Create
                        if (dao.create(food, loginUser.getUsername())) {
                            int recordsPerPage = 4;
                            int noOfRecords = dao.getNumbersOfFoodBySearch("", 0, 0, "");
                            int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
                            request.setAttribute("noOfPages", noOfPages);
                            url = SUCCESS + "?page=" + noOfPages;
                        } else {
                            request.setAttribute("ERROR", "Create failed");
                        }
                    } else {
                        url = INVALID;
                        request.setAttribute("INVALID", errorObject);
                    }
                }
            }else{
                request.setAttribute("ERROR", "You do not have permission to do this.");
            }
        } catch (Exception e) {
            log("ERROR at CreateController: " + e.getMessage());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
