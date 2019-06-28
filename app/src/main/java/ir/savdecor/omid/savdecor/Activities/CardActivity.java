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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
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
import ir.savdecor.omid.savdecor.Models.CardList;
import ir.savdecor.omid.savdecor.R;
import ir.savdecor.omid.savdecor.Utilities.Helper;


public class CardActivity extends AppCompatActivity {


    public ImageView back, search, basket;


    public JSONArray responseData;
    public JSONArray responseProducts;

    public List data;
    public CardList cardList;

    public RecyclerView recyclerView;
    public LinearLayout ll_total_price;

    public static CardAdapter cardAdapter;
    public TextView action_bar_title;
    public Button final_buy_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);


        TextView card_login = findViewById(R.id.card_login);
        if (Helper.is_register(getApplicationContext())) {
            card_login.setVisibility(View.GONE);
        } else {
            card_login.setVisibility(View.VISIBLE);

        }
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

        final_buy_btn = findViewById(R.id.final_buy_btn);
        final_buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(CardActivity.this)
                        .typeface("iransansmobile2.ttf", "IRANSansMobile.ttf")
                        .iconRes(R.drawable.logout)
                        .limitIconToDefaultSize()
                        .title("تایید خرید")
                        .titleGravity(GravityEnum.END)
                        .content("برای نهایی کردن خرید خود اطمینان دارید؟")
                        .contentGravity(GravityEnum.END)
                        .positiveText("بله")
                        .negativeText("خیر")
                        .theme(Theme.LIGHT)


                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {

                                final_buy(getApplicationContext());
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {

                            }
                        })
                        .show();
            }
        });


        cardList = new CardList();
        data = new ArrayList();
        cardAdapter = null;


        recyclerView = findViewById(R.id.rcv_card_list_id);
        ll_total_price = findViewById(R.id.ll_total_price);

        cardAdapter = new CardAdapter(getApplicationContext(), data);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager2);
//        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(cardAdapter);
        getProducts(getApplicationContext());


    }


    public void getProducts(final Context context) {


        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.GET, Helper.basUrl + "api/my_order",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response

                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            TextView empty_card = findViewById(R.id.empty_card);

                            responseData = obj.getJSONArray("products");

                            if (responseData.length() == 0) {
                                empty_card.setVisibility(View.VISIBLE);

                            } else {
                                empty_card.setVisibility(View.GONE);
                                final_buy_btn.setVisibility(View.VISIBLE);
                                ll_total_price.setVisibility(View.VISIBLE);

                                for (int i = 0; i < responseData.length(); i++) {

                                    try {
                                        JSONObject jsonObject = responseData.getJSONObject(i);
                                        CardList c = new CardList();

                                        c.setId(jsonObject.getInt("id"));
                                        c.setTitle(jsonObject.getString("title"));
                                        c.setImage_path(jsonObject.getString("image_path"));
                                        c.setPrice(jsonObject.getString("price"));

                                        data.add(c);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        //   progressDialog2.dismiss();
                                    }


                                }
                                cardAdapter.notifyDataSetChanged();
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

                params.put("status", "0");


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

    public void final_buy(final Context context) {

        String URL = Helper.basUrl + "api/final_buy";
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
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Helper.message(context, error.getMessage());
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

//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//
//                params.put("product_id", product_id + "");
//
//
//                return params;
//            }


        };
        postRequest.setShouldCache(false);


        queue.add(postRequest);


    }

}
