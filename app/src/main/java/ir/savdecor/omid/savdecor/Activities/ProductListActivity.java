package ir.savdecor.omid.savdecor.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

import ir.savdecor.omid.savdecor.Adapters.ProductAdapter;
import ir.savdecor.omid.savdecor.Adapters.ProductListAdapter;
import ir.savdecor.omid.savdecor.Models.ProductList;
import ir.savdecor.omid.savdecor.Models.ProductListList;
import  ir.savdecor.omid.savdecor.R;
import ir.savdecor.omid.savdecor.Utilities.Helper;

public class ProductListActivity extends AppCompatActivity {

    public JSONArray responseData;

    public List data;
    public ProductListList list;

    public RecyclerView recyclerView;

    public ProductListAdapter adapter;
    public TextView action_bar_title;
    public ImageView back, search, basket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);



        action_bar_title = findViewById(R.id.action_bar_title);

        action_bar_title.setText("لیست محصولات ");

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
                startActivity(i);
            }
        });

        basket = findViewById(R.id.basket);
        basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CardActivity.class);
                startActivity(i);
            }
        });


        list = new ProductListList();
        data = new ArrayList();
        adapter = null;


        recyclerView = findViewById(R.id.rcv_product_list_id_1);

        adapter = new ProductListAdapter(getApplicationContext(), data);


        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager2);
//        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        getProductList(getApplicationContext());

    }

    public void getProductList(final Context context) {

        String URL=Helper.basUrl + "api/product_list";
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.GET, URL,
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
                                    ProductListList c = new ProductListList();
                                    c.setId(jsonObject.getInt("id"));
                                    c.setProducts_count(jsonObject.getInt("products_count"));
                                    c.setTitle(jsonObject.getString("title"));


                                    data.add(c);


                                } catch (JSONException e) {
                                    e.printStackTrace();

                                    //   progressDialog2.dismiss();
                                }


                            }
                            adapter.notifyDataSetChanged();

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
            queue.getCache().remove(URL);
        }

        queue.add(postRequest);

    }

}
