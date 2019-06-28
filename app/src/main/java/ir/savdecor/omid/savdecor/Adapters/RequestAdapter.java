package ir.savdecor.omid.savdecor.Adapters;

import android.content.Context;
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
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.savdecor.omid.savdecor.Activities.CardActivity;
import ir.savdecor.omid.savdecor.Models.CardList;
import ir.savdecor.omid.savdecor.Models.RequestList;
import ir.savdecor.omid.savdecor.R;
import ir.savdecor.omid.savdecor.Utilities.Helper;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {


    private Context context;

    private List<RequestList> data;

    public RequestAdapter(Context context, List data) {
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item, parent, false);


        return new RequestAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final RequestList requestList = data.get(position);

        holder.date.setText(requestList.getDate());
        holder.request_type.setText(requestList.getRequest_type());
        holder.city.setText(requestList.getCity());
        holder.state.setText(requestList.getState());
        holder.description.setText(requestList.getDescription());
        holder.created_at.setText(requestList.getCreated_at());

        holder.state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestList cardList1 = data.get(position);

                data.remove(position);
//                delete(cardList1.getId());
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

        public TextView id, state, city, date,description,created_at,request_type;

        public RelativeLayout request_layout;


        public MyViewHolder(View itemView) {

            super(itemView);
            city = itemView.findViewById(R.id.city);
            state = itemView.findViewById(R.id.state);
            date = itemView.findViewById(R.id.date);
            created_at = itemView.findViewById(R.id.created_at);
            description = itemView.findViewById(R.id.description);
            request_type = itemView.findViewById(R.id.request_type);
            request_layout = itemView.findViewById(R.id.request_layout);

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

}
