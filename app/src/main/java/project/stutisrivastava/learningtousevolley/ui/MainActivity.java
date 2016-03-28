package project.stutisrivastava.learningtousevolley.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import project.stutisrivastava.learningtousevolley.R;
import project.stutisrivastava.learningtousevolley.adapter.ProductAdapter;
import project.stutisrivastava.learningtousevolley.datahandler.CustomJSONRequest;
import project.stutisrivastava.learningtousevolley.datahandler.JSONParser;
import project.stutisrivastava.learningtousevolley.pojo.Product;
import project.stutisrivastava.learningtousevolley.util.Alert;
import project.stutisrivastava.learningtousevolley.util.Constants;
import project.stutisrivastava.learningtousevolley.util.LearningToUseVolley;
import project.stutisrivastava.learningtousevolley.util.SystemManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView rv;
    private LinearLayoutManager llm;
    private ProgressDialog progressDialog;
    private ArrayList<Product> mProductList;
    private ProductAdapter adapter;

    LearningToUseVolley helper = LearningToUseVolley.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SystemManager.setCurrentActivity(this);
        SystemManager.setCurrentContext(getApplicationContext());
        SystemManager.setProductListURL(Constants.productListURL);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getPermissions();

        initialize();

    }

    private void initialize() {
        /**
         * Setting up recycler View for Category list
         */
        rv = (RecyclerView) findViewById(R.id.rvProductList);
        llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMore();
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * creates customJSON request and loads data for particular category
     */

    private void loadListOfProducts() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Products..");
        progressDialog.show();

        String productListURL = SystemManager.getProductListURL();
        Log.e(TAG, "URL : " + productListURL);

        CustomJSONRequest request = new CustomJSONRequest
                (Request.Method.GET, productListURL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            processResponse(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            handleError();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError();
                    }
                });
        request.setRetryPolicy(new DefaultRetryPolicy(
                120000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setPriority(Request.Priority.HIGH);
        helper.add(request);
    }

    /**
     * process
     * @param response through JSONParser class
     * @throws JSONException
     * If no data for search, then display an alert box. Handle new search and load more data.
     */

    private void processResponse(JSONObject response) throws JSONException {
        Log.e(TAG, "process Response : " + response);
        if(mProductList==null) {    //new search
            mProductList = JSONParser.getProductListFromJson(response);
            if(mProductList==null){
                progressDialog.dismiss();
                Alert.showInfo(this, "Oops!", getString(R.string.text_couldnt_search), "OK");
                return;
            }
            adapter = new ProductAdapter(mProductList);
            rv.setAdapter(adapter);
        }
        else { //load more data into list
            mProductList.addAll(JSONParser.getProductListFromJson(response));
            adapter.notifyDataSetChanged();
        }
        progressDialog.dismiss();
    }

    /**
     * Handles Volley errors and displays an alert box to the User, if user is trying to load more data
     * and then error occurs no alert is shown
     */

    private void handleError() {
        if(SystemManager.isNetworkConnected())
            Alert.showInfo(this,
                    getString(R.string.title_network_error),
                    getString(R.string.no_try_again),"OK");
    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemManager.setCurrentActivity(this);
        SystemManager.setCurrentContext(getApplicationContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void getPermissions() {
        Log.e(TAG,"getPermissions");
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "requesting permission");
            try {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                        Constants.MY_PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE);
            }catch(Exception e){
                Log.e(TAG,e.toString());
            }
        }else {
            Log.e(TAG, "hv permission");
            loadListOfProducts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.e(TAG,"requestCode "+requestCode);
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_ACCESS_NETWORK_STATE:{
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG,"permission granted");
                    loadListOfProducts();
                } else {
                    Log.e(TAG,"permission not granted");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
