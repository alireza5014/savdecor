package ir.savdecor.omid.savdecor.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.savdecor.omid.savdecor.Adapters.ProductAdapter;
import ir.savdecor.omid.savdecor.Models.ProductList;
import ir.savdecor.omid.savdecor.R;
import ir.savdecor.omid.savdecor.Utilities.Helper;
import ir.savdecor.omid.savdecor.Utilities.MinMaxFilter;

public class ProductDetailActivity extends AppCompatActivity {
    public ImageView back, search, basket;
    public int product_id;
    public String product_title;
    public TextView action_bar_title, discount_label;
    public Button add_to_card_btn, plus, mines;

    public JSONArray responseData;

    public List data;
    public ProductList productList;

    public LinearLayout unit_count_and_meter,unit_box;
    public RecyclerView recyclerView;

    public ProductAdapter productAdapter;
    public Button comment_btn;
    public EditText edt_count;

    public TextView price, discount_price, tilte, description;
    public ImageView image_path, favourite;
    public ProgressBar progressBar;
    public int favourite_count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        final Intent intent = getIntent();
        product_id = intent.getIntExtra("product_id", 0);
        product_title = intent.getStringExtra("product_title");

        favourite = findViewById(R.id.favourite);

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (favourite.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.favorite1).getConstantState()) {
                    favourite.setImageResource(R.drawable.favorite0);
                    make_favourite(getApplicationContext(), product_id, 0);
                } else {
                    favourite.setImageResource(R.drawable.favorite1);
                    make_favourite(getApplicationContext(), product_id, 1);

                }


            }
        });

        comment_btn = findViewById(R.id.comment_btn);
        add_to_card_btn = findViewById(R.id.add_to_card_btn);
        edt_count = findViewById(R.id.edt_count);
        edt_count.setFilters(new InputFilter[]{new MinMaxFilter("1", "999")});
        add_to_card_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Helper.is_register(getApplicationContext())) {

                    add_to_card(getApplicationContext(), product_id);


                } else {
                    new MaterialDialog.Builder(ProductDetailActivity.this)
                            .typeface("iransansmobile2.ttf", "IRANSansMobile.ttf")
                            .iconRes(R.drawable.logout)
                            .limitIconToDefaultSize()
                            .title("ورود به حساب کاربری")
                            .titleGravity(GravityEnum.END)
                            .content("برای خرید باید وارد حساب کاربری خود شوید.")
                            .contentGravity(GravityEnum.END)
                            .positiveText("ورود به حساب ")
                            .negativeText("ثبت نام")
                            .theme(Theme.LIGHT)


                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {

                                    Intent ii = new Intent(getApplicationContext(), LoginActivity.class);
                                    ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                    startActivity(ii);

                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    Intent ii = new Intent(getApplicationContext(), RegisterActivity.class);
                                    ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                    startActivity(ii);
                                }
                            })
                            .show();
                }
            }
        });
        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), CommentActivity.class);
                intent1.putExtra("product_id", product_id);
                intent1.putExtra("product_title", product_title);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent1);
            }
        });
        progressBar = findViewById(R.id.product_detail_progress);
        price = findViewById(R.id.price);
        discount_price = findViewById(R.id.discount_price);
        discount_label = findViewById(R.id.discount_label);
        tilte = findViewById(R.id.title);
        description = findViewById(R.id.description);
        image_path = findViewById(R.id.image_path);
        plus = findViewById(R.id.plus);
        mines = findViewById(R.id.mines);


        unit_count_and_meter = findViewById(R.id.unit_count_and_meter);
        unit_box = findViewById(R.id.unit_box);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int edt_count_;
                String edt_text = edt_count.getText().toString();
                try {
                    edt_count_ = Integer.parseInt(edt_text);
                } catch (Exception e) {
                    edt_count_ = 1;
                }
                if (edt_count_ > 0) {
                    edt_count_ += 1;

                }
                edt_count.setText(edt_count_ + "");
            }
        });

        mines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int edt_count_;
                String edt_text = edt_count.getText().toString();
                try {
                    edt_count_ = Integer.parseInt(edt_text);
                } catch (Exception e) {
                    edt_count_ = 1;
                }
                if (edt_count_ > 1) {
                    edt_count_ -= 1;

                }
                edt_count.setText(edt_count_ + "");
            }
        });
        action_bar_title = findViewById(R.id.action_bar_title);
        action_bar_title.setText(product_title);

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


        recyclerView = findViewById(R.id.rcv_similar_product_list_id);

        productAdapter = new ProductAdapter(getApplicationContext(), data);


        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        layoutManager2.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager2);
//        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(productAdapter);
//Log.e("GGGGGGG",Helper.getUserToken(getApplicationContext()));
        getProductDetail(getApplicationContext());


    }


    public void getProductDetail(final Context context) {


        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.GET, Helper.basUrl + "api/product/detail/" + product_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response

                        JSONObject obj = null;
                        JSONObject service = null;
                        try {
                            obj = new JSONObject(response);
                            service = obj.getJSONObject("service");
                            String unit = service.getString("unit");
                            Log.e("unit_unit", unit);


                            switch (unit) {
                                case "متر مربع":
                                    unit_count_and_meter.setVisibility(View.VISIBLE);
                                    break;

                                case "بسته":
                                    unit_box.setVisibility(View.VISIBLE);

                                    break;

                                case "عدد":
                                    unit_count_and_meter.setVisibility(View.VISIBLE);

                                    break;


                            }
                            favourite_count = obj.getInt("favourite_count");
                            if (favourite_count > 0) {
                                favourite.setImageResource(R.drawable.favorite1);
                            } else {
                                favourite.setImageResource(R.drawable.favorite0);

                            }

                            tilte.setText(obj.getString("title"));
                            description.setText(obj.getString("description"));
                            price.setText(obj.getString("price") + " تومان ");

                            if (obj.getString("discount").equals("۰")) {
                                discount_price.setVisibility(View.GONE);
                                discount_label.setVisibility(View.GONE);
                            } else {

                                price.setTextColor(getResources().getColor(R.color.colorGray));

                                price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                int dis = Helper.getDiscountValue(obj.getString("price"), obj.getString("discount"));
                                discount_price.setText(dis + " تومان ");
                                discount_label.setText(obj.getString("discount") + " درصد ");
                                discount_price.setVisibility(View.VISIBLE);
                                discount_label.setVisibility(View.VISIBLE);


                            }
                            if (obj.getString("price").equals("۰") || obj.getInt("count") == 0) {
                                add_to_card_btn.setEnabled(false);
                            } else {
                                add_to_card_btn.setEnabled(true);

                            }

                            Glide.with(context)
                                    .load(Helper.basUrl + obj.getString("image_path"))
                                    .thumbnail(0.2f)
                                    .placeholder(Color.BLUE)
                                    .crossFade()
                                    .listener(new RequestListener<String, GlideDrawable>() {
                                        @Override
                                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                            progressBar.setVisibility(View.GONE);
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                            progressBar.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })

                                    .into(image_path);


                            responseData = obj.getJSONArray("similar_product");

                            for (int i = 0; i < responseData.length(); i++) {

                                try {
                                    JSONObject jsonObject = responseData.getJSONObject(i);
                                    ProductList c = new ProductList();
                                    c.setId(jsonObject.getInt("id"));
                                    c.setDiscount(jsonObject.getString("discount"));

                                    c.setTitle(jsonObject.getString("title"));
                                    c.setPrice(jsonObject.getString("price"));
                                    c.setImage_path(jsonObject.getString("image_path"));


                                    c.setCount(jsonObject.getInt("count"));

                                    data.add(c);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //   progressDialog2.dismiss();
                                }


                            }
                            productAdapter.notifyDataSetChanged();

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
            queue.getCache().remove(Helper.basUrl + "api/product/detail/" + product_id);
        }

        queue.add(postRequest);

    }


    public void make_favourite(final Context context, final int product_id, final int is_favourite) {

        String URL = Helper.basUrl + "api/make_favourite";
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
                        Helper.message(context, "برای اضافه کردن به  لیست علاقه مندی ها باید وارد حساب شوید ");
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

                params.put("product_id", product_id + "");
                params.put("is_favourite", is_favourite + "");


                return params;
            }


        };
        postRequest.setShouldCache(false);


        queue.add(postRequest);


    }

    public void add_to_card(final Context context, final int product_id) {

        String URL = Helper.basUrl + "api/add_to_card";
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

                                Intent i = new Intent(getApplicationContext(), CardActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(i);
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

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("product_id", product_id + "");
                params.put("count", edt_count.getText().toString() + "");


                return params;
            }


        };
        postRequest.setShouldCache(false);


        queue.add(postRequest);

    }

}
