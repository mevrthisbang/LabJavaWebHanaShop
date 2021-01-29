/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phuchgt.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import phuchgt.dao.FoodDAO;
import phuchgt.dao.OrderDAO;
import phuchgt.dto.AccountDTO;
import phuchgt.dto.CartObj;
import phuchgt.dto.FoodDTO;
import phuchgt.dto.OrderDTO;
import phuchgt.dto.OrderErrorObject;
import phuchgt.paypal.PaymentServices;

/**
 *
 * @author mevrthisbang
 */
public class OrderController extends HttpServlet {

    private static final String ERROR = "error.jsp";
    private static final String INVALID = "cart.jsp";
    private static final String SUCCESS = "SearchController";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR;
        try {
            HttpSession session = request.getSession();
            AccountDTO loginUser = (AccountDTO) session.getAttribute("USER");
            if (loginUser != null && loginUser.getRole().equals("user")) {
                String paymentMethod = request.getParameter("paymentMethod");

                CartObj cart = (CartObj) session.getAttribute("CART");
                //Check loi quantity
                boolean check = true;
                for (FoodDTO food : cart.getCart().values()) {
                    FoodDAO foodDAO = new FoodDAO();
                    int quantityInStock = foodDAO.getQuantityByFoodID(food.getId());
                    if (quantityInStock - food.getQuantity() < 0) {
                        check = false;
                        food.setDescription("Quantity in stock: " + quantityInStock);
                    }
                }
                if (paymentMethod.equals("PayPal")) {
                    if (check) {
                        PaymentServices paymentServices = new PaymentServices();
                        String approvalLink = paymentServices.authorizePayment(cart);
                        response.sendRedirect(approvalLink);
                    } else {
                        session.setAttribute("CART", cart);
                        request.getRequestDispatcher(INVALID).forward(request, response);
                    }

                } else {
                    String fullname = request.getParameter("txtFullname");
                    String phone = request.getParameter("txtPhone");
                    String address = request.getParameter("txtAddress");
                    OrderErrorObject errorObj = new OrderErrorObject();
                    if (fullname.isEmpty()) {
                        check = false;
                        errorObj.setFullnameError("Not supposed to be empty");
                    }
                    if (phone.isEmpty()) {
                        check = false;
                        errorObj.setPhoneError("Not supposed to be empty");
                    }
                    if (!phone.matches("[0-9]{10}")) {
                        check = false;
                        errorObj.setPhoneError("Phone number must be 10 number");
                    }
                    if (address.isEmpty()) {
                        check = false;
                        errorObj.setAddressError("Not supposed to be empty");
                    }
                    if (check) {

                        OrderDAO dao = new OrderDAO();
                        String lastOrderIDByCustomer = dao.getLastOrderIDByCustomer(loginUser.getUsername());
                        String orderID;

                        if (lastOrderIDByCustomer != null) {
                            int count = Integer.parseInt(lastOrderIDByCustomer.split("_")[2]);
                            orderID = "OD_" + loginUser.getUsername() + "_" + (count + 1);
                        } else {
                            orderID = "OD_" + loginUser.getUsername() + "_1";
                        }
                        OrderDTO order = new OrderDTO(orderID, loginUser.getUsername(), fullname, "Cash", phone, address);
                        order.setStatus("waiting");
                        if (dao.insertOrder(order, cart.getCart())) {
                            //Cap nhat quantity
                            url = SUCCESS;
                            request.setAttribute("SUCCESS", "Order Successfully!");
                            session.removeAttribute("CART");
                        }
                    } else {
                        url = INVALID;
                        request.setAttribute("INVALID", errorObj);
                        session.setAttribute("CART", cart);
                    }
                    request.getRequestDispatcher(url).forward(request, response);
                }
            }else{
                request.setAttribute("ERROR", "You do not have permission to do this.");
                request.getRequestDispatcher(url).forward(request, response);
            }

        } catch (Exception e) {
            log("ERROR at OrderController: " + e.getMessage());
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
