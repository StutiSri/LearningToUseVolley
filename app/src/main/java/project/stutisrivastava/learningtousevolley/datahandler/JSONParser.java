package project.stutisrivastava.learningtousevolley.datahandler;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import project.stutisrivastava.learningtousevolley.pojo.Product;
import project.stutisrivastava.learningtousevolley.util.Constants;
import project.stutisrivastava.learningtousevolley.util.SystemManager;

/**
 * Created by stutisrivastava on 14/11/15.
 * Contains methods to parse JSON response using sdk classes - JSONObject, JSONArray etc
 */
public class JSONParser {

    private static final String TAG = "JSONParser";
    private static JSONObject resultError;
    private static JSONArray resultArray;
    private static JSONObject resultObj;

    public static ArrayList<Product> getProductListFromJson(JSONObject response) throws JSONException {
        ArrayList<Product> productList = null;

        resultArray = response.getJSONArray(Constants.RESULTS);
        productList = new ArrayList<>();

        String nextURL = response.getString(Constants.NEXTURL);
        SystemManager.setProductListURL(nextURL);

        Product product;
        int records = resultArray.length();

        for (int i = 0; i < records; i++) {
            resultObj = (JSONObject)resultArray.get(i);
            product = new Product();

            product.setProductName(resultObj.getString(Constants.NAME));
            product.setProductImgUrl(resultObj.getString(Constants.IMGURL));

            Log.e(TAG, product.toString());
            productList.add(product);
        }
        Log.e(TAG, "getProductListFromJson finished");
        return productList;
    }
}
