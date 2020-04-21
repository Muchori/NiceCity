package com.example.nicedigitalcity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.nicedigitalcity.helper.Functions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    private MaterialButton btnRegister, btnLinkToLogin;
    private TextInputLayout inputFirstname, inputEmail, inputPassword, inputSecondname, inputPhonenumber, inputAge;
    private RadioGroup rdUser_Type;
    private RadioGroup rdGender;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputFirstname = findViewById(R.id.rTextFirstname);
        inputSecondname = findViewById(R.id.rTextSecondname);
        inputAge = findViewById(R.id.rTextAge);
        inputPhonenumber = findViewById(R.id.rTextPhonenumber);
        inputEmail = findViewById(R.id.rTextEmail);
        inputPassword = findViewById(R.id.rTextPassword);
        rdUser_Type = findViewById(R.id.rdUser_Type);
        rdGender = findViewById(R.id.rdGender);
        btnRegister = findViewById(R.id.btnRegister);
        btnLinkToLogin = findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();
    }

    private void init() {
        // Login button Click Event
        btnRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                // Hide Keyboard
                Functions.hideSoftKeyboard(RegisterActivity.this);

                String fname = inputFirstname.getEditText().getText().toString().trim();
                String sname = inputSecondname.getEditText().getText().toString().trim();
                String age = inputAge.getEditText().getText().toString().trim();
                String phone_number = inputPhonenumber.getEditText().getText().toString().trim();
                String email = inputEmail.getEditText().getText().toString().trim();
                String password = inputPassword.getEditText().getText().toString().trim();
                String user_type = ((RadioButton) findViewById(rdUser_Type.getCheckedRadioButtonId())).getText().toString().trim();
                String gender = ((RadioButton) findViewById(rdGender.getCheckedRadioButtonId())).getText().toString().trim();


                // Check for empty data in the form
                if (!fname.isEmpty() && !email.isEmpty() && !password.isEmpty() && !sname.isEmpty() && !age.isEmpty()
                        && !phone_number.isEmpty() && !gender.isEmpty() && !user_type.isEmpty()) {
                    if (Functions.isValidEmailAddress(email)) {
                        registerUser(fname, sname, email, age, gender, phone_number, user_type, password);
                    } else {
                        Toast.makeText(getApplicationContext(), "Email is not valid!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void registerUser( final String fname, final String sname,  final String email, final String age, final String gender,
                               final String phone_number, final String user_type, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Functions.REGISTER_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Functions logout = new Functions();
                        logout.logoutUser(getApplicationContext());

                        Bundle b = new Bundle();
                        b.putString("email", email);
                        Intent i = new Intent(RegisterActivity.this, EmailVerify.class);
                        //Toast.makeText(getApplicationContext(),  name + "You have successful registered"+ "your verification code is", Toast.LENGTH_LONG).show();
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtras(b);
                        startActivity(i);
                        pDialog.dismiss();
                        finish();

                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage(), error);
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("first_name", fname);
                params.put("second_name", sname);
                params.put("email", email);
                params.put("age", age);
                params.put("gender", gender);
                params.put("phone_number", phone_number);
                params.put("user_type", user_type);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
