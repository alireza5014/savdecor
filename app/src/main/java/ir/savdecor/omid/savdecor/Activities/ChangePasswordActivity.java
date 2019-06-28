package ir.savdecor.omid.savdecor.Activities;

import android.content.Context;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ir.savdecor.omid.savdecor.R;
import ir.savdecor.omid.savdecor.Utilities.Helper;

public class ChangePasswordActivity extends AppCompatActivity {

    public EditText password, password_confirmation;
    public Button change_password_btn;
    public ProgressBar change_password_progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        change_password_progressbar = findViewById(R.id.change_password_progressbar);
        change_password_btn = findViewById(R.id.change_password_btn);
        password = findViewById(R.id.password);
        password_confirmation = findViewById(R.id.password_confirmation);

        change_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                change_password(getApplicationContext(), Helper.basUrl + "api/change_password", password.getText().toString(), password_confirmation.getText().toString());
            }
        });
    }


    public void change_password(final Context context, String URL, final String password, final String password_confirmation) {
        change_password_progressbar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response


                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray error = obj.getJSONArray("error");
                            int status = obj.getInt("status");
                            String message = obj.getString("message");
                            if (status==1) {

                                Helper.message(context,message);
                                finish();
                            } else {
                                Helper.message(context,error.get(0) + "");

                            }
                            change_password_progressbar.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            change_password_progressbar.setVisibility(View.GONE);

//                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), "qqq", Toast.LENGTH_LONG).show();


                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "aaa", Toast.LENGTH_LONG).show();
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("password", password);
                params.put("password_confirmation", password_confirmation);


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", Helper.getUserToken(context));
                params.put("Accept-Language", "fa");

                return params;
            }
        };
        postRequest.setShouldCache(false);


        queue.add(postRequest);

    }

}
