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
import android.widget.Toast;

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

import ir.savdecor.omid.savdecor.Adapters.HomeAdapter;
import ir.savdecor.omid.savdecor.Adapters.SampleAdapter;
import ir.savdecor.omid.savdecor.Models.HomeList;
import ir.savdecor.omid.savdecor.Models.SampleList;
import ir.savdecor.omid.savdecor.R;
import ir.savdecor.omid.savdecor.Utilities.Helper;

public class SampleActivity extends AppCompatActivity {

    public int service_id;
    public String title;


    public JSONArray responseData;

    public List data;
    public SampleList sampleList;

    public RecyclerView recyclerView;

    public SampleAdapter sampleAdapter;
    public TextView action_bar_title;
    public ImageView back,search,basket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);


        Intent intent = getIntent();
        service_id = intent.getIntExtra("service_id", 0);
        title = intent.getStringExtra("service_title");




        action_bar_title = findViewById(R.id.action_bar_title);

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
                 Intent i=new Intent(getApplicationContext(),SearchActivity.class);
                 startActivity(i);
            }
        });

        basket = findViewById(R.id.basket);
        basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent i=new Intent(getApplicationContext(),CardActivity.class);
                 startActivity(i);
            }
        });

        action_bar_title.setText(title);




        sampleList = new SampleList();
        data = new ArrayList();
        sampleAdapter = null;


        recyclerView = findViewById(R.id.rcv_sample_list_id);

        sampleAdapter = new SampleAdapter(getApplicationContext(), data);

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
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(sampleAdapter);
        getSamples(getApplicationContext());


        Toast.makeText(getApplicationContext(), service_id + "", Toast.LENGTH_SHORT).show();
    }


    public void getSamples(final Context context) {


        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.GET, Helper.basUrl + "api/samples/" + service_id,
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
                                    SampleList c = new SampleList();
                                    c.setService_title(title);
                                    c.setService_id(service_id);
                                    c.setId(jsonObject.getInt("id"));
                                    c.setTitle(jsonObject.getString("title"));
                                    c.setImage_path(jsonObject.getString("image_path"));

                                    data.add(c);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //   progressDialog2.dismiss();
                                }


                            }
                            sampleAdapter.notifyDataSetChanged();

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
            queue.getCache().remove(Helper.basUrl + "api/samples/" + service_id);
        }

        queue.add(postRequest);

    }

}
