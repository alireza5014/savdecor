package ir.savdecor.omid.savdecor.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import ir.savdecor.omid.savdecor.MainActivity;
import ir.savdecor.omid.savdecor.R;
import ir.savdecor.omid.savdecor.Utilities.Helper;

public class LoginActivity extends AppCompatActivity {
    public TextView register_txt, forgotten_password_txt;
    public Button login_btn;
    public ProgressBar login_progressbar;
    public JSONObject login_obj;
    public EditText mobile, password;
    public ImageView back, search, basket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SearchActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(i);
            }
        });

        basket = findViewById(R.id.basket);
        basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CardActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(i);
            }
        });


        login_progressbar = findViewById(R.id.login_progressbar);

        mobile = findViewById(R.id.mobile);
        password = findViewById(R.id.password);
        forgotten_password_txt = findViewById(R.id.forgotten_password_txt);
        register_txt = findViewById(R.id.register_txt);

        login_btn = findViewById(R.id.login_btn);
        register_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(i);
                finish();
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin(getApplicationContext(), Helper.basUrl + "api/login", mobile.getText().toString(), password.getText().toString());
            }
        });

    }


    public void userLogin(final Context context, String URL, final String mobile, final String password) {
        login_progressbar.setVisibility(View.VISIBLE);


        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
//                         Toast.makeText(context, response + "", Toast.LENGTH_LONG).show();

                        JSONArray errors = null;

                        JSONObject data = null;
                        try {
                            login_obj = new JSONObject(response);
                            errors = login_obj.getJSONArray("error");
                            data = login_obj.getJSONObject("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {

                            int status = login_obj.getInt("status");
                            String message = login_obj.getString("message");


                            if (status == 1) {
                                String user_token = "Bearer " + data.getString("token");
                                String fname = data.getString("fname");
                                String lname = data.getString("lname");
                                String mobile = data.getString("mobile");
                                int user_id = data.getInt("id");

                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("user_id", user_id);
                                editor.putString("fname", fname);
                                editor.putString("lname", lname);
                                editor.putString("mobile", mobile);
                                editor.putString("user_token", user_token);
                                editor.putBoolean("is_register", true);
                                editor.apply();
                                editor.commit();

                                Intent i = new Intent(context, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                context.startActivity(i);
                                finish();

                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, errors + "", Toast.LENGTH_LONG).show();

                            }
                            login_progressbar.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            login_progressbar.setVisibility(View.GONE);

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

                params.put("password", password);
                params.put("mobile", mobile);


                return params;
            }


        };
        postRequest.setShouldCache(false);


        queue.add(postRequest);

    }

}
