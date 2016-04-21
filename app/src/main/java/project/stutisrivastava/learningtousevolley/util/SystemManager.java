package project.stutisrivastava.learningtousevolley.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import project.stutisrivastava.learningtousevolley.R;
import project.stutisrivastava.learningtousevolley.listener.ConfirmationListener;
import project.stutisrivastava.learningtousevolley.pojo.Customer;


/**
 * Created by stutisrivastava on 28/12/15.
 */
public class SystemManager {

    private static Activity currentActivity;
    private static Context context;



    private static String productListURL;

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(Activity activity) {
        currentActivity = activity;
    }

    public static void setCurrentContext(Context ctx){
        context = ctx;
    }

    public static Context getCurrentContext() {
        return context;
    }


    public static boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkinfo = cm.getActiveNetworkInfo();
            if (networkinfo == null || !networkinfo.isConnected()) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }



    public static ConfirmationListener getNetworkConfirmationListener() {
        return networkConfirmationListener;
    }

    private static ConfirmationListener networkConfirmationListener = new ConfirmationListener() {
        @Override
        public void onConfirmationSet(boolean ret) {
            if(ret){
                Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getCurrentContext().startActivity(intent);
            }
            getCurrentActivity().finish();
        }
    };

    public static void setProductListURL(String nextProductListURL) {
        SystemManager.productListURL = nextProductListURL;
    }

    public static String getProductListURL() {
        return productListURL;
    }

    public static boolean isValidUserName(String userName) {
        Log.e("SysMngr", "username : " + userName);
        if(userName.isEmpty()){
            Toast.makeText(getCurrentContext(), R.string.blank_name_error, Toast.LENGTH_LONG).show();
            return false;
        }
        Pattern testPattern= Pattern.compile("^[a-zA-Z ]{2,30}$");
        Matcher teststring= testPattern.matcher(userName);
        if(!teststring.matches()) {
            Toast.makeText(getCurrentContext(), R.string.invalid_name_error, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public static boolean isValidEmailOrPhone(String emailOrPhone) {
        if(emailOrPhone.contains("@")){
            if(!emailOrPhone.contains(".")){
                Toast.makeText(getCurrentContext(), R.string.text_enter_valid_email, Toast.LENGTH_LONG).show();
                return false;
            }
        }
        else{
            Pattern testPattern= Pattern.compile("^[7-9][0-9]{9}");
            Matcher teststring= testPattern.matcher(emailOrPhone);

            if(!teststring.matches()){
                Toast.makeText(getCurrentContext(), R.string.text_enter_valid_email_or_phone, Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    public static boolean isValidPAssword(String password, String confirmPassword) {
        if(password.length()<6) {
            Toast.makeText(getCurrentContext(), R.string.text_enter_valid_password, Toast.LENGTH_LONG).show();
            return false;
        }
        if(!password.equals(confirmPassword)) {
            Toast.makeText(getCurrentContext(), R.string.text_pswd_cnfrm_pswd, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public static Cursor isRegistered(Customer customer) {
        return null;
    }

    public static String createRegisterQuery(Customer customer) {
        String baseQuery = "https://stutisrivastv.pythonanywhere.com/Test1/customer/api/tbl_customers";
        return null;
    }
}
