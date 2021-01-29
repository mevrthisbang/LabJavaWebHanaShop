/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phuchgt.paypal;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import java.util.ArrayList;
import java.util.List;
import phuchgt.dto.CartObj;
import phuchgt.dto.FoodDTO;

/**
 *
 * @author mevrthisbang
 */
public class PaymentServices {

    private static final String CLIENT_ID = "Ae00bQM1gXOmKGMPAsYBnaz-O63ZtORdD_BtOmwS2bXfhGCcyQJAI3p1R7OlqeDpMaW210Gv46CHagaw";
    private static final String CLIENT_SECRET = "EFH4fOYCFPVhsHDiDiPbN2st8Qr9LWDFzLm4eIiDuDQ1hZfT-iGbSircMPmET7-zCoV37flGT26Xz8Xp";
    private static final String MODE = "sandbox";

    public String authorizePayment(CartObj cart) throws PayPalRESTException {
        RedirectUrls redirectUrls = getRedirectUrls();
        List<Transaction> listTransaction = getTransactionInformation(cart);
        Payment requestPayment = new Payment();
        requestPayment.setTransactions(listTransaction);
        requestPayment.setRedirectUrls(redirectUrls);
        requestPayment.setPayer(getPayerInformation());
        requestPayment.setIntent("sale");
        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
        Payment approvedPayment = requestPayment.create(apiContext);
        return getApprovalLink(approvedPayment);
    }

    private RedirectUrls getRedirectUrls() {
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setReturnUrl("http://localhost:8084/SE141133_HanaShop/MainController?action=paypal");
        redirectUrls.setCancelUrl("http://localhost:8084/SE141133_HanaShop/cart.jsp");
        return redirectUrls;
    }

    private List<Transaction> getTransactionInformation(CartObj cart) {
        List<Transaction> listTransaction = null;
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(String.format("%.2f", cart.getTotal()));
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        ItemList itemList = new ItemList();
        List<Item> items = new ArrayList<>();
        for (FoodDTO food : cart.getCart().values()) {
            Item item = new Item();
            item.setPrice(food.getPrice() + "");
            item.setQuantity(food.getQuantity() + "");
            item.setCurrency("USD");
            item.setName(food.getName());
            items.add(item);
        }
        itemList.setItems(items);
        transaction.setItemList(itemList);
        listTransaction = new ArrayList();
        listTransaction.add(transaction);
        return listTransaction;
    }

    private Payer getPayerInformation() {
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        return payer;
    }

    private String getApprovalLink(Payment approvedPayment) {
        String approvalLink = null;
        List<Links> links = approvedPayment.getLinks();
        for (Links link : links) {
            if (link.getRel().equalsIgnoreCase("approval_url")) {
                approvalLink = link.getHref();
                break;
            }
        }
        return approvalLink;
    }
    public Payment executePayment(String paymentID, String payerID) throws PayPalRESTException{
        PaymentExecution paymentExecution=new PaymentExecution();
        paymentExecution.setPayerId(payerID);
        Payment payment=new Payment().setId(paymentID);
        APIContext apiContext=new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
        return payment.execute(apiContext, paymentExecution);
    }
}
