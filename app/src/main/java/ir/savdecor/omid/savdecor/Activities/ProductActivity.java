package ir.savdecor.omid.savdecor.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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
import ir.savdecor.omid.savdecor.Adapters.SampleAdapter;
import ir.savdecor.omid.savdecor.Models.ProductList;
import ir.savdecor.omid.savdecor.Models.SampleList;
import ir.savdecor.omid.savdecor.R;
import ir.savdecor.omid.savdecor.Utilities.Helper;


public class ProductActivity extends AppCompatActivity {
    public int sample_id,service_id;
    public String service_title, sample_title;


    public JSONArray responseData;

    public List data;
    public ProductList productList;

    public RecyclerView recyclerView;

    public ProductAdapter productAdapter;
    public TextView action_bar_title;
    public ImageView back, search, basket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Intent intent = getIntent();
        sample_id = intent.getIntExtra("sample_id", 0);
        service_id = intent.getIntExtra("service_id", 0);
        service_title = intent.getStringExtra("service_title");
        sample_title = intent.getStringExtra("sample_title");
        if (!sample_title.equals("")){
            sample_title="( "+sample_title+" )";
        }

        Helper.message(getApplicationContext(),service_id+"_"+sample_id);
        action_bar_title = findViewById(R.id.action_bar_title);

        action_bar_title.setText("لیست محصولات " + service_title + " " + sample_title + "");

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


        productList = new ProductList();
        data = new ArrayList();
        productAdapter = null;


        recyclerView = findViewById(R.id.rcv_product_list_id);

        productAdapter = new ProductAdapter(getApplicationContext(), data);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position % 3 == 0)
                    return 1;
                else
                    return 1;
            }
        });
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(productAdapter);
        getProducts(getApplicationContext());


    }

    public void getProducts(final Context context) {

String URL=Helper.basUrl + "api/products?sample_id=" + sample_id+"&&service_id=" + service_id;
        Log.e("AAAAAAAAAA",""+URL);

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
                                    ProductList c = new ProductList();
                                    c.setId(jsonObject.getInt("id"));
                                    c.setCount(jsonObject.getInt("count"));
                                    c.setDiscount(jsonObject.getString("discount"));
                                    c.setTitle(jsonObject.getString("title"));
                                    c.setPrice(jsonObject.getString("price"));
                                    c.setImage_path(jsonObject.getString("image_path"));

                                    data.add(c);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //   progressDialog2.dismiss();
                                    Helper.message(context,"BBBB"+Helper.basUrl);

                                }


                            }
                            productAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("PRODUCTSSSSS_error",e.getMessage());

                        }

                        // Log.d("Response", response1 + "");
                    }
                },
                new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // TODO Auto-generated method stub
                        Log.d("ERROR111", "error => " + error.toString());

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
