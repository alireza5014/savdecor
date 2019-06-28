package ir.savdecor.omid.savdecor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.savdecor.omid.savdecor.Activities.CardActivity;
import ir.savdecor.omid.savdecor.Activities.ProductDetailActivity;
import ir.savdecor.omid.savdecor.Activities.VerifyActivity;
import ir.savdecor.omid.savdecor.Models.CardList;
import ir.savdecor.omid.savdecor.Models.ProductList;
import ir.savdecor.omid.savdecor.R;
import ir.savdecor.omid.savdecor.Utilities.Helper;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {


    private Context context;

    private List<CardList> data;

    public CardAdapter(Context context, List data) {
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);


        return new CardAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final CardList cardList = data.get(position);


        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");
        list.add("10");
        holder.count_spinner.setItems(list);
        holder.count_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Toast.makeText(context, id + "Clicked " + item, Toast.LENGTH_LONG).show();
                String total_price = holder.price.getText().toString().replace(",", "");
                int tp = Integer.parseInt(total_price)*Integer.parseInt(item);

                holder.total_price.setText(tp+"");

                change_product_count(cardList.getId(),item);
            }
        });

        holder.title.setText(cardList.getTitle());
        holder.price.setText(cardList.getPrice());
        holder.total_price.setText(cardList.getPrice());
        Glide.with(context)
                .load(Helper.basUrl + cardList.getImage_path())
                .thumbnail(0.2f)
                // .centerCrop()
//                .transform(new CircleTransform(context))
                // .error(R.drawable.profile)
                //   .transform( new BlurTransformation( context ),new CircleTransform(context) )
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })

                .placeholder(Color.BLUE)
                .crossFade()
                .into(holder.image_path);
        holder.delete_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardList cardList1 = data.get(position);

                data.remove(position);
                delete(cardList1.getId());
                CardActivity.cardAdapter.notifyDataSetChanged();
                Helper.message(context, "حذف با موفقیت انجام شد");


            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, price, total_price, delete_card;
        public ImageView image_path;
        public ProgressBar progressBar;
        public RelativeLayout card_layout;
        public MaterialSpinner count_spinner;


        public MyViewHolder(View itemView) {

            super(itemView);
            title = itemView.findViewById(R.id.title);
            image_path = itemView.findViewById(R.id.image_path);
            price = itemView.findViewById(R.id.price);
            total_price = itemView.findViewById(R.id.total_price);
            progressBar = itemView.findViewById(R.id.card_progress);
            delete_card = itemView.findViewById(R.id.delete_card);
            card_layout = itemView.findViewById(R.id.card_layout);
            count_spinner = itemView.findViewById(R.id.count_spinner);


        }
    }


    public void delete(final int product_id) {

        String URL = Helper.basUrl + "api/delete_order";
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Toast.makeText(context, response + "", Toast.LENGTH_LONG).show();


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


                return params;
            }


        };
        postRequest.setShouldCache(false);


        queue.add(postRequest);


    }
    public void change_product_count(final int product_id,final  String  count) {

        String URL = Helper.basUrl + "api/change_product_count";
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response

                        try {
                            JSONObject obj = new JSONObject(response);

                            int status=obj.getInt("status");
                            String message=obj.getString("message");
                            if(status==1){
                                Toast.makeText(context,    message, Toast.LENGTH_LONG).show();

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
                params.put("count",count);


                return params;
            }


        };
        postRequest.setShouldCache(false);


        queue.add(postRequest);


    }


}
