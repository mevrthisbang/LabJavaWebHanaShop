/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phuchgt.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import phuchgt.dao.FoodDAO;
import phuchgt.dao.OrderDAO;
import phuchgt.dto.AccountDTO;
import phuchgt.dto.FoodDTO;
import phuchgt.dto.OrderDTO;

/**
 *
 * @author mevrthisbang
 */
public class GetShoppingHistoryController extends HttpServlet {

    private static final String ERROR = "error.jsp";
    private static final String OKAY = "shoppingHistory.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR;
        try {

            HttpSession session = request.getSession();
            AccountDTO loginUser = (AccountDTO) session.getAttribute("USER");
            if (loginUser != null && loginUser.getRole().equals("user")) {
                String date = request.getParameter("datePicker");
                String name = request.getParameter("txtName");
                OrderDAO orderDAO = new OrderDAO();
                List<OrderDTO> listOrders = null;
                if (date == null && name == null || date.isEmpty() && name.isEmpty()) {
                    listOrders = orderDAO.getOrderHistoryByUsername(loginUser.getUsername());
                } else {
                    listOrders = orderDAO.searchOrderHistory(name, date, loginUser.getUsername());
                }
                if (listOrders != null && !listOrders.isEmpty()) {
                    HashMap<OrderDTO, List<FoodDTO>> listOrderswithFood = new HashMap<>();
                    for (OrderDTO orderDTO : listOrders) {
                        FoodDAO foodDAO = new FoodDAO();
                        List<FoodDTO> listFoodByOrderID = foodDAO.getFoodByOrderID(orderDTO.getOrderID());
                        listOrderswithFood.put(orderDTO, listFoodByOrderID);
                    }
                    request.setAttribute("SHOPPINGHISTORY", listOrderswithFood);
                }
                url = OKAY;
            } else {
                request.setAttribute("ERROR", "You do not have permission to do this.");
            }

        } catch (Exception e) {
            log("ERROR at GetShoppingHistoryController: " + e.getMessage());
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
