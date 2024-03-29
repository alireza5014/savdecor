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
import ir.savdecor.omid.savdecor.Activities.VisitRequestActivity;
import ir.savdecor.omid.savdecor.Models.RequestList;
import ir.savdecor.omid.savdecor.R;
import ir.savdecor.omid.savdecor.Utilities.Helper;

public class NewVisitRequestFragment extends Fragment {
    View v;
    public ImageView back1;
    public Button request_btn;
    public ProgressBar prgressbar;
    public EditText state, city, description, date;
    public MaterialSpinner request_type_spinner;
    public int request_type_id = 0;
    public String request_type = "درخواست بازدید از پروژه";

    public NewVisitRequestFragment() {
    }

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_new_visit_request, container, false);


        back1 = v.findViewById(R.id.back);

        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(NewVisitRequestFragment.this).commit();

            }
        });
        prgressbar = v.findViewById(R.id.request_btn_progressbar);
        request_type_spinner = v.findViewById(R.id.request_type_spinner);
        state = v.findViewById(R.id.state);
        city = v.findViewById(R.id.city);
        description = v.findViewById(R.id.description);
        request_btn = v.findViewById(R.id.request_btn);





        request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                make_visit_request(getContext());
            }
        });
        return v;


    }



    public void make_visit_request(final Context context) {
        request_type_id += 1;
        prgressbar.setVisibility(View.VISIBLE);

        String URL = Helper.basUrl + "api/make_visit_request";
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
                                RequestList c = new RequestList();
                                c.setId(44);
                                c.setCity(city.getText().toString());
                                c.setDate("");
                                c.setState(state.getText().toString());
                                c.setDescription(description.getText().toString());
                                c.setCreated_at("");
                                c.setRequest_type(request_type);

                                VisitRequestActivity.data.add(0,c);
                                VisitRequestActivity.requestAdapter.notifyDataSetChanged();


                                getFragmentManager().beginTransaction().remove(NewVisitRequestFragment.this).commit();
//                                getActivity().onBackPressed();
                            }
                            Helper.message(context,message+"____");

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        prgressbar.setVisibility(View.GONE);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                        prgressbar.setVisibility(View.GONE);
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

                params.put("request_type_id", request_type_id + "");
                params.put("state", state.getText().toString());
                params.put("city", city.getText().toString());
                params.put("description", description.getText().toString());
                params.put("date", "1398/12/12");

                return params;
            }
        };
        postRequest.setShouldCache(false);
        queue.add(postRequest);


    }


}
