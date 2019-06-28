package ir.savdecor.omid.savdecor.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ir.savdecor.omid.savdecor.R;
import ir.savdecor.omid.savdecor.Utilities.Helper;

public class RegisterActivity extends AppCompatActivity {

    public Button register_btn;
    public ProgressBar register_progressbar;
    public TextView fname, lname, mobile, password;
    public JSONObject register_obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_btn = findViewById(R.id.register_btn);
        register_progressbar = findViewById(R.id.register_progressbar);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        mobile = findViewById(R.id.mobile);
        password = findViewById(R.id.password);

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRegister(getApplicationContext(), Helper.basUrl + "api/first_request_register", fname.getText().toString(), lname.getText().toString(), mobile.getText().toString(), password.getText().toString());
            }
        });
    }


    public void userRegister(final Context context, String URL, final String fname, final String lname, final String mobile, final String password) {
        register_progressbar.setVisibility(View.VISIBLE);


        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Toast.makeText(context, response + "", Toast.LENGTH_LONG).show();

                        JSONArray errors = null;

                        JSONObject data = null;


                        try {
                            register_obj = new JSONObject(response);
                            errors = register_obj.getJSONArray("error");
                            data = register_obj.getJSONObject("data");

                            int status = register_obj.getInt("status");
                            String message = register_obj.getString("message");


                            if (status == 200) {

                                int message_id = data.getInt("message_id");
                                Intent i = new Intent(context, VerifyActivity.class);
                                i.putExtra("message_id", message_id);
                                i.putExtra("mobile", mobile);
                                i.putExtra("password", password);
                                startActivity(i);
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, "k", Toast.LENGTH_LONG).show();

                            }
                            register_progressbar.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            register_progressbar.setVisibility(View.GONE);

//                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();


                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("fname", fname);
                params.put("lname", lname);
                params.put("password", password);
                params.put("mobile", mobile);


                return params;
            }


        };
        postRequest.setShouldCache(false);


        queue.add(postRequest);

    }

}
