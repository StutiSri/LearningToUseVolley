package project.stutisrivastava.learningtousevolley.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import project.stutisrivastava.learningtousevolley.listener.ConfirmationListener;


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

}
