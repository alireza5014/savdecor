package ir.savdecor.omid.savdecor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import ir.savdecor.omid.savdecor.Activities.AboutUsActivity;
import ir.savdecor.omid.savdecor.Activities.CardActivity;
import ir.savdecor.omid.savdecor.Activities.FavouriteActivity;
import ir.savdecor.omid.savdecor.Activities.LoginActivity;
import ir.savdecor.omid.savdecor.Activities.OrderActivity;
import ir.savdecor.omid.savdecor.Activities.ProductListActivity;
import ir.savdecor.omid.savdecor.Activities.ProfileActivity;
import ir.savdecor.omid.savdecor.Activities.RequestActivity;
import ir.savdecor.omid.savdecor.Activities.SearchActivity;
import ir.savdecor.omid.savdecor.Adapters.HomeAdapter;
import ir.savdecor.omid.savdecor.Adapters.ProductAdapter;
import ir.savdecor.omid.savdecor.Models.HomeList;
import ir.savdecor.omid.savdecor.Models.ProductList;
import ir.savdecor.omid.savdecor.Utilities.Helper;

public class MainActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    public Button nav_login_register;
    public RelativeLayout nav_user_info;
    Boolean doubleBackToExitPressedOnce = false;


    public ImageView search, basket;
    public JSONArray responseData;
    public JSONArray responseData2;
    public JSONArray responseData3;

    public List data;
    public HomeList homeList;

    public RecyclerView GroupListRecyclerView;

    public RelativeLayout main_loading;
    public HomeAdapter homeAdapter;


    public List data2;
    public ProductList productList;

    public RecyclerView recyclerView;

    public ProductAdapter productAdapter;


    public List data3;
    public ProductList discount_productList;

    public RecyclerView discount_recyclerView;

    public ProductAdapter discount_productAdapter;


    /*JFP-*/
    private Toolbar toolbar;
    private ListView listView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    String[] titles;
    int[] images;


    SliderLayout sliderLayout;
    HashMap<String, String> Hash_file_maps;

    @Override
    public void onBackPressed() {

        try {


            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                finish();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Helper.message(getApplicationContext(), "برای خروج دوباره کلیک کنید!");
            // Helper.clearStack(getSupportFragmentManager());
            new android.os.Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        } catch (Exception e) {

        }

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        main_loading = findViewById(R.id.main_loading);
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


        /*nav*/


//        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
//        getSupportActionBar().setCustomView(R.layout.action_bar);
//
//
//        View view = getSupportActionBar().getCustomView();
        ImageView name = findViewById(R.id.bill);

        //

        listView = findViewById(R.id.listView);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.nav_header_main, listView, false);
        listView.addHeaderView(header, null, false);

        images = new int[]{
                R.drawable.list_profile,
                R.drawable.list_product,
                R.drawable.list_order,

                R.drawable.list_favourite,
                R.drawable.list_search,

                R.drawable.list_installer,
                R.drawable.list_visitor,
                R.drawable.list_contact_us,
                R.drawable.list_about_us,

        };

        titles = new String[]{
                "پروفایل من",
                "لیست محصولات",
                "سوابق خرید",
                "مورد علاقه من",
                "جستجو",

                "درخواست نصاب",
                "درخواست بازدید از پروژه",

                "تماس با ما",
                "درباره ما",


        };

        MyAdapter myAdapter = new MyAdapter(getApplicationContext());
        listView.setAdapter(myAdapter);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.opened, R.string.closed) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        listView.setOnItemClickListener(new DrawerItemClickListener());

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.closeDrawers();
                }
//                Toast.makeText(MainActivity.this, "You have clicked tittle", Toast.LENGTH_LONG).show();
            }
        });
        homeList = new HomeList();
        data = new ArrayList();
        homeAdapter = null;


        GroupListRecyclerView = findViewById(R.id.rcv_group_list_id);

        homeAdapter = new HomeAdapter(getApplicationContext(), data);


        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position % 3 == 0)
                    return 2;
                else
                    return 1;
            }
        });
        GroupListRecyclerView.setLayoutManager(layoutManager);
        GroupListRecyclerView.setNestedScrollingEnabled(false);
        GroupListRecyclerView.setAdapter(homeAdapter);


        productList = new ProductList();
        data2 = new ArrayList();
        productAdapter = null;


        recyclerView = findViewById(R.id.rcv_new_product_list_id);

        productAdapter = new ProductAdapter(getApplicationContext(), data2);


        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        layoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(layoutManager2);
//        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(productAdapter);


        discount_productList = new ProductList();
        data3 = new ArrayList();
        discount_productAdapter = null;


        discount_recyclerView = findViewById(R.id.rcv_discount_product_list_id);

        discount_productAdapter = new ProductAdapter(getApplicationContext(), data3);


        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        layoutManager.setReverseLayout(true);

        discount_recyclerView.setLayoutManager(layoutManager3);
//        recyclerView.setNestedScrollingEnabled(false);
        discount_recyclerView.setAdapter(discount_productAdapter);


//        GroupListRecyclerView.setLayoutManager(new SpannedGridLayoutManager(
//                new SpannedGridLayoutManager.GridSpanLookup() {
//                    @Override
//                    public SpannedGridLayoutManager.SpanInfo getSpanInfo(int position) { if (position % 3 != 0 ) {
//                        return new SpannedGridLayoutManager.SpanInfo(2, 2);
//                    } else {
//                        return new SpannedGridLayoutManager.SpanInfo(4, 2);
//                    }                    }
//                },
//
//                4/* Three columns */,
//                1f /* We want our items to be 1:1 ratio */));
//        GroupListRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        getHomePage(getApplicationContext());


        // slider

        showSilder();


//        final View container = new View(getApplicationContext());
//        login_register=  container.findViewById(R.id.login_btn);


        nav_login_register = findViewById(R.id.nav_login);
        nav_user_info = findViewById(R.id.nav_user_info);

        nav_login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(login);
            }
        });


        if (Helper.is_register(getApplicationContext())) {

            ImageView nav_logout = findViewById(R.id.nav_logout);
            TextView nav_fullname = findViewById(R.id.nav_fullname);
            TextView nav_mobile = findViewById(R.id.nav_mobile);

            nav_fullname.setText(Helper.getFullName(getApplicationContext()));
            nav_mobile.setText(Helper.getMobile(getApplicationContext()));


            nav_logout.setVisibility(View.VISIBLE);
            nav_login_register.setVisibility(View.GONE);
            nav_user_info.setVisibility(View.VISIBLE);

            nav_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    new MaterialDialog.Builder(MainActivity.this)
                            .typeface("iransansmobile2.ttf", "IRANSansMobile.ttf")
                            .iconRes(R.drawable.logout)
                            .limitIconToDefaultSize()
                            .title("خروج از حساب کاربری")
                            .titleGravity(GravityEnum.END)
                            .content("برای خروج از حساب کاربری خود اطمینان دارید؟")
                            .contentGravity(GravityEnum.END)
                            .positiveText("بله")
                            .negativeText("خیر")
                            .theme(Theme.LIGHT)


                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {

                                    Helper.Logout(getApplicationContext());
                                    nav_login_register.setVisibility(View.VISIBLE);
                                    nav_user_info.setVisibility(View.GONE);

                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    // TODO
                                }
                            })
                            .show();


                }
            });


        }


    }

    @Override
    protected void onStop() {

        sliderLayout.stopAutoCycle();

        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {

        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflaer;

        public MyAdapter(Context context) {
            mInflaer = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
//            Typeface typeface = Typeface.createFromAsset( getAssets(),"iransansmobile.ttf");
            Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.iransansmobile);
            if (convertView == null) {
                view = mInflaer.inflate(R.layout.list_items, null);
            } else {
                view = convertView;
            }
            TextView textView = (TextView) view.findViewById(R.id.list_text);
            ImageView imageView = (ImageView) view.findViewById(R.id.list_image);
            textView.setText(titles[position]);

            textView.setTypeface(typeface);
            imageView.setImageResource(images[position]);
            return view;
        }
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            selectitem(position);
        }

        private void selectitem(int position) {

            switch (position) {


                case 1:
                    Intent intent;
                    if (Helper.is_register(getApplicationContext())) {
                        intent = new Intent(getApplicationContext(), ProfileActivity.class);

                    } else {
                        intent = new Intent(getApplicationContext(), LoginActivity.class);
                    }
                    startActivity(intent);
                    //open another Activity :D -JFP
                    mDrawerLayout.closeDrawers();
                    break;
                case 2:
                    Intent productList = new Intent(getApplicationContext(), ProductListActivity.class);
                    startActivity(productList);
                    //open another Activity :D -JFP
                    mDrawerLayout.closeDrawers();
                    break;
                case 3:

                    Intent order = new Intent(getApplicationContext(), OrderActivity.class);
                    startActivity(order);
                    //open another Activity :D -JFP
                    mDrawerLayout.closeDrawers();
                    break;


                case 4:
                    //open another Activity :D -JFP
                    Intent fav = new Intent(getApplicationContext(), FavouriteActivity.class);
                    startActivity(fav);
                    mDrawerLayout.closeDrawers();
                    break;

                case 5:
                    //open another Activity :D -JFP
                    mDrawerLayout.closeDrawers();
                    Intent i = new Intent(getApplicationContext(), SearchActivity.class);
                    startActivity(i);
                    break;


                case 6:


                    Intent request = new Intent(getApplicationContext(), RequestActivity.class);
                    startActivity(request);
                    //open another Activity :D -JFP


                    //open another Activity :D -JFP
                    mDrawerLayout.closeDrawers();
                    break;


                case 7:
                    mDrawerLayout.closeDrawers();
                    break;
                case 8:

                    if (isPermissionGranted()) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:02166201716"));
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        startActivity(callIntent);

                    }
                    break;

                case 9:


                    Intent about_us = new Intent(getApplicationContext(), AboutUsActivity.class);
                    startActivity(about_us);
                    //open another Activity :D -JFP
                    mDrawerLayout.closeDrawers();
                    break;


            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                if (!mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                    mDrawerLayout.openDrawer(GravityCompat.END);
                } else {
                    mDrawerLayout.closeDrawers();
                }

                break;


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);


        return true;
    }


    public void showSilder() {
        Hash_file_maps = new HashMap<String, String>();

        sliderLayout = findViewById(R.id.slider);

        Hash_file_maps.put("رمضان مبارک", Helper.basUrl + "/images/ramadan1.png");
        Hash_file_maps.put("جدیدترین محصولات پارکت لمینت ۱۳۹۸", Helper.basUrl + "/images/slide1.png");


        for (String name : Hash_file_maps.keySet()) {

            TextSliderView textSliderView = new TextSliderView(MainActivity.this);
            textSliderView
                    .description(name)
                    .image(Hash_file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);


            sliderLayout.addSlider(textSliderView);

            sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            sliderLayout.setCustomAnimation(new DescriptionAnimation());
            sliderLayout.setDuration(6000);
            sliderLayout.addOnPageChangeListener(this);
        }
    }

    public void getHomePage(final Context context) {

        main_loading.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.GET, Helper.basUrl + "api/home",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response

                        JSONObject obj = null;
                        try {


                            obj = new JSONObject(response);


                            responseData = obj.getJSONArray("data");
                            responseData2 = obj.getJSONArray("new_product");
                            responseData3 = obj.getJSONArray("discount_product");

                            for (int i = 0; i < responseData.length(); i++) {

                                try {
                                    JSONObject jsonObject = responseData.getJSONObject(i);
                                    HomeList c = new HomeList();
                                    c.setId(jsonObject.getInt("id"));
                                    c.setTitle(jsonObject.getString("title"));
                                    c.setImage_path(jsonObject.getString("image_path"));
                                    c.setSamples_count(jsonObject.getInt("samples_count"));

                                    data.add(c);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //   progressDialog2.dismiss();
                                }


                            }
                            homeAdapter.notifyDataSetChanged();

                            for (int i = 0; i < responseData2.length(); i++) {

                                try {
                                    JSONObject jsonObject = responseData2.getJSONObject(i);
                                    ProductList c = new ProductList();
                                    c.setId(jsonObject.getInt("id"));
                                    c.setDiscount(jsonObject.getString("discount"));
                                    c.setCount(jsonObject.getInt("count"));

                                    c.setTitle(jsonObject.getString("title"));
                                    c.setImage_path(jsonObject.getString("image_path"));
                                    c.setPrice(jsonObject.getString("price"));

                                    data2.add(c);


                                } catch (JSONException e) {
                                    e.printStackTrace();

                                    //   progressDialog2.dismiss();
                                }


                            }
                            productAdapter.notifyDataSetChanged();


                            for (int i = 0; i < responseData3.length(); i++) {

                                try {
                                    JSONObject jsonObject = responseData3.getJSONObject(i);
                                    ProductList c = new ProductList();
                                    c.setId(jsonObject.getInt("id"));
                                    c.setDiscount(jsonObject.getString("discount"));
                                    c.setCount(jsonObject.getInt("count"));

                                    c.setTitle(jsonObject.getString("title"));
                                    c.setImage_path(jsonObject.getString("image_path"));
                                    c.setPrice(jsonObject.getString("price"));

                                    data3.add(c);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("CCC", e.getMessage());

                                    //   progressDialog2.dismiss();
                                }


                            }
                            discount_productAdapter.notifyDataSetChanged();

                            new android.os.Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    main_loading.setVisibility(View.GONE);

                                }
                            }, 500);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            main_loading.setVisibility(View.GONE);

                            Log.e("BBBB", e.getMessage());
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
            queue.getCache().remove(Helper.basUrl + "api/home");
        }

        queue.add(postRequest);

    }


    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }
}
