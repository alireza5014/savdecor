package ir.savdecor.omid.savdecor.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import ir.savdecor.omid.savdecor.Adapters.ProfileAdapter;
import ir.savdecor.omid.savdecor.Models.ProfileList;
import ir.savdecor.omid.savdecor.R;

public class ProfileActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    public ProfileAdapter profileAdapter;
    public ProfileList profileList;

    public List data;

    public JSONArray responseData;
    public TextView action_bar_title;
    public ImageView back, search, basket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        action_bar_title = findViewById(R.id.action_bar_title);
        action_bar_title.setText("مدیریت حساب کاربری");
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


        profileList = new ProfileList();
        data = new ArrayList();
        profileAdapter = null;


        profileList.setTitle("ویرایش اطلاعات");
        profileList.setImage_path("profile/edit.png");
        data.add(profileList);
        profileList = new ProfileList();

        profileList.setTitle("سفارشات من");
        profileList.setImage_path("profile/order.png");

        data.add(profileList);
        profileList = new ProfileList();
        profileList.setImage_path("profile/favourite.png");

        profileList.setTitle("لیست مورد علاقه");
        data.add(profileList);
        profileList = new ProfileList();
        profileList.setImage_path("profile/change_password.png");
        profileList.setTitle("تغییر  رمز عبور");
        data.add(profileList);
        profileList = new ProfileList();


        recyclerView = findViewById(R.id.rcv_product_list_id);

        profileAdapter = new ProfileAdapter(getApplicationContext(), data);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager2);
//        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(profileAdapter);
        profileAdapter.notifyDataSetChanged();

    }


}
