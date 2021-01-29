/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phuchgt.controller;

import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import phuchgt.dao.OrderDAO;
import phuchgt.dto.AccountDTO;
import phuchgt.dto.CartObj;
import phuchgt.dto.OrderDTO;
import phuchgt.paypal.PaymentServices;

/**
 *
 * @author mevrthisbang
 */
public class PaypalController extends HttpServlet {

    private static final String ERROR = "error.jsp";
    private static final String SUCCESS = "SearchController";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR;
        try {
            HttpSession session = request.getSession();
            AccountDTO loginUser = (AccountDTO) session.getAttribute("USER");
            if (loginUser != null && loginUser.getRole().equals("user")) {
                String paymentID = request.getParameter("paymentId");
                String payerID = request.getParameter("PayerID");
                PaymentServices paymentServices = new PaymentServices();
                Payment payment = paymentServices.executePayment(paymentID, payerID);
                if (payment != null) {
                    PayerInfo payerInfo = payment.getPayer().getPayerInfo();
                    OrderDAO dao = new OrderDAO();
                    String lastOrderIDByCustomer = dao.getLastOrderIDByCustomer(loginUser.getUsername());
                    String orderID;
                    if (lastOrderIDByCustomer != null) {
                        int count = Integer.parseInt(lastOrderIDByCustomer.split("_")[2]);
                        orderID = "OD_" + loginUser.getUsername() + "_" + (count + 1);
                    } else {
                        orderID = "OD_" + loginUser.getUsername() + "_1";
                    }
                    CartObj cart = (CartObj) session.getAttribute("CART");
                    Transaction transaction = payment.getTransactions().get(0);
                    OrderDTO order = new OrderDTO(orderID, loginUser.getUsername(),
                            payerInfo.getFirstName() + payerInfo.getLastName(), "Paypal", payerInfo.getPhone(),
                            transaction.getItemList().getShippingAddress().getLine1());
                    order.setStatus("paid");
                    if (dao.insertOrder(order, cart.getCart())) {
                        url = SUCCESS;
                        request.setAttribute("SUCCESS", "Order Successfully!");
                        session.removeAttribute("CART");
                    }
                } else {
                    request.setAttribute("ERROR", "Payment Paypal Error!");
                }
            } else {
                request.setAttribute("ERROR", "You do not have permission to do this.");
            }

        } catch (Exception e) {
            log("ERROR at PayPalController: " + e.getMessage());
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
