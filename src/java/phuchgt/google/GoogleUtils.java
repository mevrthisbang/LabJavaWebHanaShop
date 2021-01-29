/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phuchgt.google;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

/**
 *
 * @author mevrthisbang
 */
public class GoogleUtils {

    public static String GOOGLE_CLIEND_ID = "939804373285-dosi8pg5biocp08k7id7d35o1iarj0fm.apps.googleusercontent.com";
    public static String GOOGLE_CLIENT_SECRET = "EJ5xziATB2X85lc2g93Z-ooN";
    public static String GOOGLE_REDIRECT_URI = "http://localhost:8084/SE141133_HanaShop/MainController?action=loginGmail";
    public static String GOOGLE_LINK_GET_TOKEN = "https://accounts.google.com/o/oauth2/token";
    public static String GOOGLE_LINK_GET_USER_INFO = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";
    public static String GOOGLE_GRANT_TYPE = "authorization_code";

    public static String getToken(final String code) throws ClientProtocolException, IOException {
        String response = Request.Post(GOOGLE_LINK_GET_TOKEN).bodyForm(Form.form().add("client_id", GOOGLE_CLIEND_ID)
                .add("client_secret", GOOGLE_CLIENT_SECRET).add("redirect_uri", GOOGLE_REDIRECT_URI).add("code", code)
                .add("grant_type", GOOGLE_GRANT_TYPE).build()).execute().returnContent().asString();
        JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
        String accessToken = jobj.get("access_token").toString().replaceAll("\"", "");
        return accessToken;
    }

    public static GooglePojo getUserInfo(final String accessToken) throws ClientProtocolException, IOException {
        String link = GOOGLE_LINK_GET_USER_INFO + accessToken;
        String response = Request.Get(link).execute().returnContent().asString();
        GooglePojo googlePojo = new Gson().fromJson(response, GooglePojo.class);
        return googlePojo;
    }
}
