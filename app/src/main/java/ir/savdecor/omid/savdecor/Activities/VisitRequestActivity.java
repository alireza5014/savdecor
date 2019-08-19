package ir.savdecor.omid.savdecor.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.savdecor.omid.savdecor.Adapters.RequestAdapter;
import ir.savdecor.omid.savdecor.Fragment.NewRequestFragment;
import ir.savdecor.omid.savdecor.Fragment.NewVisitRequestFragment;
import ir.savdecor.omid.savdecor.Models.RequestList;
import ir.savdecor.omid.savdecor.R;
import ir.savdecor.omid.savdecor.Utilities.Helper;


public class VisitRequestActivity extends AppCompatActivity {
    public JSONArray responseData;
    public Button new_visit_request;
    public TextView action_bar_title;
    public ImageView back, search, basket;
    public static List data;
    public RequestList requestList;

    public static RecyclerView recyclerView;

    public  static RequestAdapter requestAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_request);

        action_bar_title = findViewById(R.id.action_bar_title);

        action_bar_title.setText(" درخواست نصاب " );

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


        new_visit_request = findViewById(R.id.new_visit_request);
        new_visit_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewVisitRequestFragment fragmentEditProfile = new NewVisitRequestFragment();
                FragmentTransaction t = getSupportFragmentManager().beginTransaction();
//                t.setCustomAnimations(R.anim.inter_anime, R.anim.exit_to_left_);
                t.replace(R.id.fragment_new_visit_request_id, fragmentEditProfile);
                t.commit();


            }
        });


        requestList = new RequestList();
        data = new ArrayList();
        requestAdapter = null;


        recyclerView = findViewById(R.id.rcv_visit_request_list_id);

        requestAdapter = new RequestAdapter(getApplicationContext(), data);


        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
//        layoutManager2.setReverseLayout(true);

        recyclerView.setLayoutManager(layoutManager2);
//        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(requestAdapter);

        getMyVisitRequest(getApplicationContext());
    }


    public void getMyVisitRequest(final Context context) {

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.GET, Helper.basUrl + "api/my_visit_requests",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response

                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);


                            responseData = obj.getJSONArray("data");

                            for (int i = 0; i < responseData.length(); i++) {

                                try {
                                    JSONObject jsonObject = responseData.getJSONObject(i);
                                    JSONObject jsonObject2 = jsonObject.getJSONObject("request_type");

                                    RequestList c = new RequestList();
                                    c.setId(jsonObject.getInt("id"));
                                    c.setCity(jsonObject.getString("city"));
                                    c.setDate(jsonObject.getString("date"));
                                    c.setState(jsonObject.getString("state"));
                                    c.setDescription(jsonObject.getString("description"));
                                    c.setCreated_at(jsonObject.getString("created_at"));
                                    c.setRequest_type(jsonObject2.getString("title"));

                                    data.add(c);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //   progressDialog2.dismiss();
                                }


                            }
                            requestAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Log.d("Response", response1 + "");
                    }
                },
                new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // TODO Auto-generated method stub
                        Log.e("ERROR", "error => " + error.toString());
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
            queue.getCache().remove(Helper.basUrl + "api/my_visit_requests");
        }

        queue.add(postRequest);

    }

}
