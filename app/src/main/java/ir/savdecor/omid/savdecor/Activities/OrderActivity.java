package ir.savdecor.omid.savdecor.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import ir.savdecor.omid.savdecor.Adapters.CardAdapter;
import ir.savdecor.omid.savdecor.Adapters.OrderAdapter;
import ir.savdecor.omid.savdecor.Models.CardList;
import ir.savdecor.omid.savdecor.Models.OrderList;
import ir.savdecor.omid.savdecor.R;
import ir.savdecor.omid.savdecor.Utilities.Helper;


public class OrderActivity extends AppCompatActivity {
    public TextView action_bar_title;
    public ImageView back, search, basket;



    public JSONArray responseData;
    public JSONArray responseProducts;

    public List data;
    public OrderList orderList;

    public RecyclerView recyclerView;

    public static OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        action_bar_title = findViewById(R.id.action_bar_title);

        action_bar_title.setText("سوابق خرید");

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






        orderList = new OrderList();
        data = new ArrayList();
        orderAdapter = null;


        recyclerView = findViewById(R.id.rcv_order_list_id);

        orderAdapter = new OrderAdapter(getApplicationContext(), data);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager2);
//        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(orderAdapter);

        getOrders(getApplicationContext());
    }

    public void getOrders(final Context context) {


        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.GET, Helper.basUrl + "api/my_order",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response

                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);

                            responseData = obj.getJSONArray("data");

                            if (responseData.length() > 0) {

                                for (int i = 0; i < responseData.length(); i++) {

                                    try {
                                        JSONObject jsonObject = responseData.getJSONObject(i);
                                        OrderList c = new OrderList();

                                        c.setId(jsonObject.getInt("id"));
                                        c.setDiscount(jsonObject.getInt("discount"));
                                        c.setTotal_price(jsonObject.getInt("total_price"));
                                        c.setCreated_at(jsonObject.getString("created_at"));
                                        c.setProducts(jsonObject.getJSONArray("products"));


                                        data.add(c);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        //   progressDialog2.dismiss();
                                    }


                                }
                                orderAdapter.notifyDataSetChanged();
                            }

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
                        Log.d("ERROR", "error => " + error.toString());
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("status", "1");


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
//        postRequest.setShouldCache(false);
        postRequest.setShouldCache(false);
        if (Helper.internetIsConnected()) {
            queue.getCache().remove(Helper.basUrl + "api/my_order");
        }

        queue.add(postRequest);

    }

}
