package ir.savdecor.omid.savdecor.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    public TextView card_login;
    public JSONObject responseData;
    public JSONArray responseData1;
    public JSONArray responseProducts;

    public TextView empty_card;
    public EditText discount_edt;
    public List data;
    public static int my_discount_percent, my_discount_price, order_id;
    public CardList cardList;

    public RecyclerView recyclerView;
    public LinearLayout ll_total_price, ll_make_discount;

    public static CardAdapter cardAdapter;
    public TextView action_bar_title;
    public static TextView sum_total_price, discount_price, final_price;
    public Button final_buy_btn, discount_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);


        ll_make_discount = findViewById(R.id.ll_make_discount);
        card_login = findViewById(R.id.card_login);
        empty_card = findViewById(R.id.empty_card);
        discount_btn = findViewById(R.id.discount_btn);
        discount_edt = findViewById(R.id.discount_edt);
        discount_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String discount_txt = discount_edt.getText().toString();
                if (discount_txt.equals("")) {
                    Helper.message(getApplicationContext(), "کد تخفیف را وارد کنید");
                } else {
                    check_discount(getApplicationContext(), discount_edt.getText().toString());

                }
            }
        });
        card_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(login);
            }
        });
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

        sum_total_price = findViewById(R.id.sum_total_price);
        discount_price = findViewById(R.id.discount_price);
        final_price = findViewById(R.id.final_price);

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


        if (!Helper.is_register(getApplicationContext())) {
            ll_total_price.setVisibility(View.GONE);
            empty_card.setVisibility(View.GONE);
            card_login.setVisibility(View.VISIBLE);

        } else {
            card_login.setVisibility(View.GONE);

        }


        cardAdapter = new CardAdapter(getApplicationContext(), data);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager2);
//        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(cardAdapter);
        getProducts(getApplicationContext());


    }


    public void check_discount(final Context context, String code) {


        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.GET, Helper.basUrl + "api/check_discount/" + order_id + "/" + code,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("bbbbb", response);
                        JSONObject obj = null;

                        try {
                            obj = new JSONObject(response);

                            int status = obj.getInt("status");
                            String message = obj.getString("message");
                            if (status == 1) {

                                ll_make_discount.setVisibility(View.GONE);
                                JSONObject jsonObject1 = obj.getJSONObject("detail");

                                int my_total_price = jsonObject1.getInt("total_price");
                                int my_total_percent_price = jsonObject1.getInt("total_percent_price");
                                int my_final_price = jsonObject1.getInt("final_price");


                                sum_total_price.setText(my_total_price + " تومان ");
                                discount_price.setText(my_total_percent_price + " تومان ");
                                final_price.setText(my_final_price + " تومان ");


                            }


                            Helper.message(getApplicationContext(), message);


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

                        Log.e("aaaaaa", error.getMessage());

                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

//                params.put("status", "0");
//

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
            queue.getCache().remove(Helper.basUrl + "api/check_discount/" + order_id + "/" + code);
        }

        queue.add(postRequest);
    }


    public void getProducts(final Context context) {


        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.GET, Helper.basUrl + "api/my_order",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("______", response);
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            JSONObject jsonObject1 = obj.getJSONObject("total_price");

                            int my_total_price = jsonObject1.getInt("total_price");
                            int my_total_percent_price = jsonObject1.getInt("total_percent_price");
                            int my_final_price = jsonObject1.getInt("final_price");


                            sum_total_price.setText(my_total_price + " تومان ");
                            discount_price.setText(my_total_percent_price + " تومان ");
                            final_price.setText(my_final_price + " تومان ");

                            responseData = obj.getJSONObject("data");

                            order_id = responseData.getInt("id");
                            int discount = responseData.getInt("discount");
                            if (discount > 0) {
                                ll_make_discount.setVisibility(View.GONE);
                            } else {
                                ll_make_discount.setVisibility(View.VISIBLE);

                            }


                            responseData1 = responseData.getJSONArray("products");

                            if (responseData1.length() == 0) {
                                empty_card.setVisibility(View.VISIBLE);
                                ll_total_price.setVisibility(View.GONE);

                            } else {
                                empty_card.setVisibility(View.GONE);
                                final_buy_btn.setVisibility(View.VISIBLE);
                                ll_total_price.setVisibility(View.VISIBLE);

                                for (int i = 0; i < responseData1.length(); i++) {

                                    try {
                                        JSONObject jsonObject = responseData1.getJSONObject(i);
                                        JSONObject service = jsonObject.getJSONObject("service");
                                        double box_count = 0;
                                        double my_size = 0;
                                        JSONObject pivot = jsonObject.getJSONObject("pivot");

                                        CardList c = new CardList();

                                        c.setId(jsonObject.getInt("id"));
                                        c.setTitle(jsonObject.getString("title"));
                                        c.setImage_path(jsonObject.getString("image_path"));
                                        c.setDiscount(jsonObject.getString("discount"));
                                        c.setUnit(service.getString("unit"));


                                        c.setOrder_product_id(pivot.getInt("id"));


                                        int price = jsonObject.getInt("price");
                                        String discount_price = jsonObject.getString("discount");

                                        int price_with_discount = Helper.getDiscountValue(price + "", discount_price);
                                        int count = pivot.getInt("count");

                                        double width = pivot.getDouble("width");
                                        double height = pivot.getDouble("height");
                                        c.setWidth(width);
                                        c.setHeight(height);

                                        double total_price;
                                        switch (service.getString("unit")) {
                                            case "متر مربع":
                                                total_price = price_with_discount * width * height;

                                                break;

                                            case "بسته":

                                                my_size = jsonObject.getDouble("size");
                                                box_count = Math.ceil(count / my_size);

                                                total_price = price_with_discount * box_count;

                                                break;

                                            case "عدد":
                                                total_price = price_with_discount * count;

                                                break;

                                            default:
                                                total_price = 3;

                                                break;
                                        }

                                        c.setPrice(price + "");
                                        c.setSize(my_size);
                                        c.setTotalPrice(total_price + "");
                                        c.setCount(count);
                                        c.setBoxCount(box_count + "");
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
                            Log.e("ERRORERRORERRORERROR", "error => " + e.getMessage());

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
