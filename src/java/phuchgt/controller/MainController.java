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

/**
 *
 * @author mevrthisbang
 */
public class MainController extends HttpServlet {

    private static final String ERROR = "error.jsp";
    private static final String LOGIN = "LoginController";
    private static final String SEARCH = "SearchController";
    private static final String CREATE = "CreateController";
    private static final String DELETE = "DeleteController";
    private static final String LOADBYPRIMARYKEY = "GetFoodByPrimaryKeyController";
    private static final String UPDATE = "UpdateController";
    private static final String ADDTOCART = "AddtoCartController";
    private static final String REMOVE = "RemoveFromCartController";
    private static final String ORDER = "OrderController";
    private static final String UPDATECART = "UpdateCartController";
    private static final String LOGINGMAIL = "LoginByGmailController";
    private static final String LOGOUT = "LogoutController";
    private static final String SHOPPINGHISTORY = "GetShoppingHistoryController";
    private static final String PAYPAL="PaypalController";
    private static final String SUGGEST="SuggestFoodController";
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR;
        try {
            String action = request.getParameter("action");
            if (action == null || action.equals("Search")) {
                url = SEARCH;
            } else if (action.equals("Create")) {
                url = CREATE;
            } else if (action.equals("Delete")) {
                url = DELETE;
            } else if (action.equals("Edit")) {
                url = LOADBYPRIMARYKEY;
            } else if (action.equals("Update")) {
                url = UPDATE;
            } else if (action.equals("Login")) {
                url = LOGIN;
            } else if (action.equals("Add to Cart")) {
                url = ADDTOCART;
            } else if (action.equals("Remove")) {
                url = REMOVE;
            } else if (action.equals("Confirm Order")) {
                url = ORDER;
            } else if (action.equals("Update Cart")) {
                url = UPDATECART;
            } else if (action.equals("loginGmail")) {
                url = LOGINGMAIL;
            } else if (action.equals("Logout")) {
                url = LOGOUT;
            } else if (action.equals("shoppingHistory") || action.equals("Search History")) {
                url = SHOPPINGHISTORY;
            }else if(action.equals("paypal")){
                url=PAYPAL;
            }else if(action.equals("suggest")){
                url=SUGGEST;
            }else{
                request.setAttribute("ERROR", "Your action is invalid");
            }
        } catch (Exception e) {
            log("ERROR at MainController: " + e.getMessage());
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
