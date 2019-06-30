package ir.savdecor.omid.savdecor.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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

import ir.savdecor.omid.savdecor.Adapters.CommentAdapter;
import ir.savdecor.omid.savdecor.Adapters.ProductAdapter;
import ir.savdecor.omid.savdecor.Fragment.NewCommentFragment;
import ir.savdecor.omid.savdecor.Fragment.NewRequestFragment;
import ir.savdecor.omid.savdecor.Models.CommentList;
import ir.savdecor.omid.savdecor.Models.ProductList;
import ir.savdecor.omid.savdecor.R;
import ir.savdecor.omid.savdecor.Utilities.Helper;

public class CommentActivity extends AppCompatActivity {
    public  int product_id;
    public String product_title;
    public Button new_comment_btn;


    public JSONArray responseData;

    public List data;
    public CommentList commentList;

    public RecyclerView recyclerView;

    public CommentAdapter commentAdapter;
    public TextView action_bar_title;
    public ImageView back, search, basket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);


        final Intent intent = getIntent();
        product_id = intent.getIntExtra("product_id", 0);

        product_title = intent.getStringExtra("product_title");


        new_comment_btn = findViewById(R.id.new_comment_btn);
        new_comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        new_comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putInt("product_id", product_id);


                NewCommentFragment fragment = new NewCommentFragment();
                fragment.setArguments(bundle);

                FragmentTransaction t = getSupportFragmentManager().beginTransaction();
//                t.setCustomAnimations(R.anim.inter_anime, R.anim.exit_to_left_);
                t.replace(R.id.new_comment_id, fragment);
                t.commit();


            }
        });


        action_bar_title = findViewById(R.id.action_bar_title);

        action_bar_title.setText("نظرات " + product_title);

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

        commentList = new CommentList();
        data = new ArrayList();
        commentAdapter = null;


        recyclerView = findViewById(R.id.rcv_comment_list_id);

        commentAdapter = new CommentAdapter(getApplicationContext(), data);


        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
//        layoutManager2.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager2);


//        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(commentAdapter);

        getComments(getApplicationContext());

    }

    public void getComments(final Context context) {


        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.GET, Helper.basUrl + "api/comments/" + product_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
Log.e("__)_",product_id+"");
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);


                            responseData = obj.getJSONArray("data");

                            for (int i = 0; i < responseData.length(); i++) {

                                try {
                                    JSONObject jsonObject = responseData.getJSONObject(i);
                                    JSONObject user = jsonObject.getJSONObject("user");
                                    CommentList c = new CommentList();
                                    c.setId(jsonObject.getInt("id"));
                                    c.setMessage(jsonObject.getString("message"));
                                    c.setCreated_at(jsonObject.getString("created_at"));
                                    c.setFname(user.getString("fname"));
                                    c.setLname(user.getString("lname"));
                                    c.setImage_path(user.getString("image_path"));

                                    data.add(c);


                                } catch (JSONException e) {
                                    e.printStackTrace();


                                    //   progressDialog2.dismiss();
                                }


                            }
                            commentAdapter.notifyDataSetChanged();

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
            queue.getCache().remove(Helper.basUrl + "api/comments/" + product_id);
        }

        queue.add(postRequest);

    }

}
