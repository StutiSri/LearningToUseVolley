package project.stutisrivastava.learningtousevolley.ui;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import project.stutisrivastava.learningtousevolley.R;
import project.stutisrivastava.learningtousevolley.pojo.Customer;
import project.stutisrivastava.learningtousevolley.util.Constants;
import project.stutisrivastava.learningtousevolley.util.LearningToUseVolley;
import project.stutisrivastava.learningtousevolley.util.SystemManager;

/**
 * Created by stutisrivastava on 4/20/16.
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private CheckBox chkbocxTermsConditions;
    private Button btnRegister;
    private String userName;
    private String email;
    private String phone;
    private String password;
    private String TAG="RegisterActivity";

    LearningToUseVolley helper = LearningToUseVolley.getInstance();
    private String uniqueKeyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialize();
    }

    private void initialize() {
        etName = (EditText)findViewById(R.id.et_register_name);
        /**
         * Enter press in this edit text should automatically take to next edit text - etPassword.
         */
        etName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == 66) {
                    etEmail.requestFocus();
                }
                return false;
            }
        });

        etEmail = (EditText)findViewById(R.id.et_register_email);
        /**
         * Enter press in this edit text should automatically take to next edit text - etPassword.
         */
        etEmail.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == 66) {
                    etPhone.requestFocus();
                }
                return false;
            }
        });

        etPhone = (EditText)findViewById(R.id.et_register_phone);
        /**
         * Enter press in this edit text should automatically take to next edit text - etPassword.
         */
        etPhone.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == 66) {
                    etPassword.requestFocus();
                }
                return false;
            }
        });

        etPassword = (EditText)findViewById(R.id.et_register_password);
        /**
         * Enter press in this edit text should automatically take to next edit text
         */
        etPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == 66) {
                    etConfirmPassword.requestFocus();
                }
                return false;
            }
        });

        etConfirmPassword = (EditText)findViewById(R.id.et_register_confirm_password);
        /**
         * Enter press in this edit text should automatically take to next edit text
         */
        etConfirmPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == 66) {
                    chkbocxTermsConditions.requestFocus();
                }
                return false;
            }
        });
        chkbocxTermsConditions = (CheckBox)findViewById(R.id.chkboxtc);

        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValid())
                    notRegistered();
            }
        });
    }

    private boolean notRegistered() {
        uniqueKeyId = 'n'+email.replace(".","")+phone;
        String url = "https://stutisrivastv.pythonanywhere.com/Test1/customer/api/get_customer/"+uniqueKeyId;
        //String url = "https://stutisrivastv.pythonanywhere.com/Test1/customer/registration_api/tbl_customer.json";
        final Map<String,String> pars = new HashMap<>();
        pars.put("customer_unique_id", uniqueKeyId);
        Log.e(TAG,"parameters="+new JSONObject(pars));
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, new JSONObject(pars), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "Response: " + response.toString());
                        try {
                            if (response.getString("success").equals("OK"))
                                register();
                            else
                                Toast.makeText(RegisterActivity.this,response.getString("error_message"),Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
        //SystemManager.saveInSharedPref(Constants.NORMALLOGIN,customer,sharedPreference);
        String msg = request.getUrl() + ", " + request;
        Log.e(TAG, msg);
        helper.add(request);
        //Toast.makeText(getBaseContext(), R.string.text_logged_in,Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean isValid() {
        Log.e(TAG, "isValidMethod");
        userName = etName.getEditableText().toString();
        if(!SystemManager.isValidUserName(userName))
            return false;
        email = etEmail.getEditableText().toString();
        if(!SystemManager.isValidEmailOrPhone(email)) {
            return false;
        }
        phone = etPhone.getEditableText().toString();
        if(!SystemManager.isValidEmailOrPhone(phone)) {
            return false;
        }
        uniqueKeyId = 'n'+email.replace(".","")+phone;
        password = etPassword.getEditableText().toString();
        String confirmPassword = etConfirmPassword.getEditableText().toString();
        if(!SystemManager.isValidPAssword(password,confirmPassword))
            return false;
        if(!chkbocxTermsConditions.isChecked()){
            Toast.makeText(getApplicationContext(), R.string.text_agree_to_tnc, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void register() {
        Log.e(TAG, "register");
        Customer customer = new Customer();
        customer.setName(userName);
        customer.setEmail(email);
        customer.setPhoneNumber(phone);
        customer.setPassword(password);

        Cursor result = SystemManager.isRegistered(customer);
        if(result!=null){
            if(email!=null)
                Toast.makeText(getBaseContext(), R.string.text_email_exists,Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getBaseContext(), R.string.text_mobile_number_exists,Toast.LENGTH_LONG).show();
        }else{
            final Bundle parameters = new Bundle();
            parameters.putString("customer_unique_id", uniqueKeyId);
            parameters.putString("customer_email_id", email);
            parameters.putString("customer_phone_number", phone);
            parameters.putString("customer_name", userName);
            parameters.putString("customer_password", password);
            Log.e(TAG,""+parameters);
            String url = "https://stutisrivastv.pythonanywhere.com/Test1/customer/api/get_otp.json";
            JsonObjectRequest request = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e(TAG,"Response: " + response.toString());
                            try {
                                String otp = response.getString("otp");
                                parameters.putString("customer_otp",otp);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(RegisterActivity.this,ConfirmRegistrationActivity.class);
                            intent.putExtra("params",parameters);
                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub

                        }
                    });
            //SystemManager.saveInSharedPref(Constants.NORMALLOGIN,customer,sharedPreference);
            helper.add(request);
            //Toast.makeText(getBaseContext(), R.string.text_logged_in,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        SystemManager.setCurrentActivity(this);
        SystemManager.setCurrentContext(getApplicationContext());
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SystemManager.setCurrentActivity(null);
        //SystemManager.setCurrentContext(getApplicationContext());
    }

}



