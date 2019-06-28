package ir.savdecor.omid.savdecor.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.savdecor.omid.savdecor.Activities.RequestActivity;
import ir.savdecor.omid.savdecor.Models.RequestList;
import ir.savdecor.omid.savdecor.R;
import ir.savdecor.omid.savdecor.Utilities.Helper;

public class NewCommentFragment extends Fragment {
    View v;

    public Button cancel_comment_btn, insert_comment_btn;
    public EditText message;

    public NewCommentFragment() {
    }

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_new_comment, container, false);
        cancel_comment_btn = v.findViewById(R.id.cancel_comment_btn);
        insert_comment_btn = v.findViewById(R.id.insert_comment_btn);
        message = v.findViewById(R.id.message);

        insert_comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert_comment(getContext());
            }
        });

        cancel_comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(NewCommentFragment.this).commit();

            }
        });
        return v;


    }

    public void insert_comment(final Context context) {

        String URL = Helper.basUrl + "api/insert_comment";
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


                                getFragmentManager().beginTransaction().remove(NewCommentFragment.this).commit();
//                                getActivity().onBackPressed();
                            }
                            Helper.message(context, message);

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                        Toast.makeText(context, error.getMessage() + "lll", Toast.LENGTH_LONG).show();

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

                params.put("product_id", "61");
                params.put("message", message.getText().toString());
                params.put("reply_id", "0");

                return params;
            }
        };
        postRequest.setShouldCache(false);
        queue.add(postRequest);


    }


}
