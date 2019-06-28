package ir.savdecor.omid.savdecor.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ir.savdecor.omid.savdecor.Models.ProductList;
import ir.savdecor.omid.savdecor.R;
import ir.savdecor.omid.savdecor.Utilities.Helper;

public class EditProfileActivity extends AppCompatActivity {
    public EditText fname, lname, telephone, mobile, code_melli, address;
    public ProgressBar page_progressBar, edit_profile_progressbar;
    public Button edit_profile_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        telephone = findViewById(R.id.telephone);
        mobile = findViewById(R.id.mobile);
        code_melli = findViewById(R.id.code_melli);
        address = findViewById(R.id.address);
        page_progressBar = findViewById(R.id.page_progressbar);
        edit_profile_progressbar = findViewById(R.id.edit_profile_progressbar);
        edit_profile_btn = findViewById(R.id.edit_profile_btn);
        getProfile(getApplicationContext());

        edit_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modify_profile(getApplicationContext());
            }
        });
    }


    public void getProfile(final Context context) {

        page_progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.GET, Helper.basUrl + "api/profile",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response

                        JSONObject obj = null;
                        JSONObject responseData = null;


                        try {
                            obj = new JSONObject(response);
                            responseData = obj.getJSONObject("data");

                            fname.setText(responseData.getString("fname"));
                            lname.setText(responseData.getString("lname"));
                            mobile.setText(responseData.getString("mobile"));
                            telephone.setText(responseData.getString("telephone"));
                            code_melli.setText(responseData.getString("code_melli"));
                            address.setText(responseData.getString("address"));


                            page_progressBar.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            page_progressBar.setVisibility(View.GONE);

                            e.printStackTrace();
                        }

                        // Log.d("Response", response1 + "");
                    }
                },
                new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                        page_progressBar.setVisibility(View.GONE);

                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());
                    }
                }
        ) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", Helper.getUserToken(context));
                params.put("Accept-Language", "fa");

                return params;
            }
        };
//        postRequest.setShouldCache(false);
        postRequest.setShouldCache(true);
        if (Helper.internetIsConnected()) {
            queue.getCache().remove(Helper.basUrl + "api/profile");
        }

        queue.add(postRequest);

    }

    public void modify_profile(final Context context) {
        edit_profile_progressbar.setVisibility(View.VISIBLE);

        String URL = Helper.basUrl + "api/modify_profile";
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response

                        try {
                            JSONObject obj = new JSONObject(response);

                            int status = obj.getInt("status");
                            String message = obj.getString("message");
                            if (status == 1) {
                                Helper.message(context, message);
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        edit_profile_progressbar.setVisibility(View.GONE);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                        edit_profile_progressbar.setVisibility(View.GONE);

                        Log.d("ERROR", "error => " + error.toString());
                    }
                }
        ) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", Helper.getUserToken(context));
                params.put("Accept-Language", "fa");

                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("fname", fname.getText().toString());
                params.put("lname", lname.getText().toString());
                params.put("telephone", telephone.getText().toString());
                params.put("code_melli", code_melli.getText().toString());
                params.put("address", address.getText().toString());

                return params;
            }
        };
        postRequest.setShouldCache(false);
        queue.add(postRequest);


    }


}
