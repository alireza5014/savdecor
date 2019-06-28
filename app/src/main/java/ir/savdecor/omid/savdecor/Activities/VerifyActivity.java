package ir.savdecor.omid.savdecor.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir.savdecor.omid.savdecor.MainActivity;
import ir.savdecor.omid.savdecor.R;
import ir.savdecor.omid.savdecor.Utilities.Helper;

public class VerifyActivity extends AppCompatActivity {
    public JSONObject verify_obj;
    private int message_id;
    private String mobile, password;
    public ProgressBar verify_progressbar;
    public EditText sms_code;
    public Button verify_btn;
    public TextView edit_number, sent_txt;


    public SmsVerifyCatcher smsVerifyCatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);


        Intent intent = getIntent();
        message_id = intent.getIntExtra("message_id", 0);

        mobile = intent.getStringExtra("mobile");
        password = intent.getStringExtra("password");

        verify_btn = findViewById(R.id.verify_btn);
        edit_number = findViewById(R.id.edit_number);
        sent_txt = findViewById(R.id.sent_txt);

        sent_txt.setText("کد تایید برای شماره " + mobile + " ارسال شد");
        edit_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        verify_progressbar = findViewById(R.id.verify_progressbar);
        sms_code = findViewById(R.id.et_code);

        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String code = parseCode(message);//Parse verification code
                sms_code.setText(code);//set code in edit text
                //then you can send verification code to server
            }
        });


        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userVerify(getApplicationContext(), Helper.basUrl + "api/userVerify", message_id + "", sms_code.getText().toString(), mobile, password);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();

        userVerify(getApplicationContext(), Helper.basUrl + "api/userVerify", message_id + "", sms_code.getText().toString(), mobile, password);
    }

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
        Toast.makeText(getApplicationContext(), message_id + "stoooop", Toast.LENGTH_SHORT).show();

    }

    /**
     * need for Android 6 real time permissions
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{5}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }


    public void userVerify(final Context context, String URL, final String message_id, final String sms_code, final String mobile, final String password) {
        verify_progressbar.setVisibility(View.VISIBLE);


        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONArray errors = null;

                        JSONObject data = null;

                        try {
                            verify_obj = new JSONObject(response);

                            errors = verify_obj.getJSONArray("error");
                            data = verify_obj.getJSONObject("data");


                            int status = verify_obj.getInt("status");
                            String message = verify_obj.getString("message");


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
                                startActivity(i);
                                finish();

                            }

                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                            verify_progressbar.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            verify_progressbar.setVisibility(View.GONE);
                            Log.e("BBBBB", e.getMessage());

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

                params.put("sms_code", sms_code);
                params.put("message_id", message_id);
                params.put("password", password);
                params.put("mobile", mobile);


                return params;
            }


        };
        postRequest.setShouldCache(false);


        queue.add(postRequest);

    }

}
