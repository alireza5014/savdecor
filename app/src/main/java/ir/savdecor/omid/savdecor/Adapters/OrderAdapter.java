package ir.savdecor.omid.savdecor.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ir.savdecor.omid.savdecor.Models.OrderDetailList;
import ir.savdecor.omid.savdecor.Models.OrderList;
import ir.savdecor.omid.savdecor.Models.ProductList;
import ir.savdecor.omid.savdecor.R;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    public Context context;
    public List<OrderList> data;

    public OrderAdapter(Context context, List data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);


        return new OrderAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final OrderList orderList = data.get(position);


        final JSONArray ProductArr = orderList.getProducts();
        try {
            holder.orderDetailList = new OrderDetailList();
            holder.data_order_details = new ArrayList();


            holder.orderDetailAdapter = new OrderDetailAdapter(context, holder.data_order_details);
            holder.order_detail_rcv.setLayoutManager(new LinearLayoutManager(context));
            holder.order_detail_rcv.setAdapter(holder.orderDetailAdapter);

//            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
//            holder.order_detail_rcv.setLayoutManager(new GridLayoutManager(context, 3));
//            holder.order_detail_rcv.setLayoutManager(layoutManager);
//

            holder.order_detail_rcv.setLayoutManager(new LinearLayoutManager(context));
            holder.order_detail_rcv.setAdapter(holder.orderDetailAdapter);


            for (int i = 0; i < ProductArr.length(); i++) {

                try {
                    JSONObject jsonObject = ProductArr.getJSONObject(i);
                    JSONObject pivot = jsonObject.getJSONObject("pivot");

                    OrderDetailList u = new OrderDetailList();
                    u.setId(jsonObject.getInt("id"));
                    u.setTitle(jsonObject.getString("title"));
                    u.setImage_path(jsonObject.getString("image_path"));
                    u.setPrice(jsonObject.getInt("price"));
                    u.setCount(pivot.getInt("count"));


                    holder.data_order_details.add(u);


                } catch (JSONException e) {
                    e.printStackTrace();
                    //   progressDialog2.dismiss();

                    Log.e("____", "_____qq" + e.getMessage());

                }


            }
            holder.orderDetailAdapter.notifyDataSetChanged();


            holder.id.setText(position + 1 + "");
            holder.total_price.setText(orderList.getTotal_price() + "");
            holder.order_no.setText("NO_" + orderList.getId() + "");
            holder.created_at.setText(orderList.getCreated_at());
        } catch (Exception e) {
            Log.e("____", "_____" + e.getMessage());

        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView total_price, id, order_no, created_at;


        public List data_order_details;
        public OrderDetailList orderDetailList;
        public RecyclerView order_detail_rcv;
        public OrderDetailAdapter orderDetailAdapter;


        public MyViewHolder(View itemView) {
            super(itemView);

            created_at = itemView.findViewById(R.id.created_at);
            order_no = itemView.findViewById(R.id.order_no);
            id = itemView.findViewById(R.id.id);
            total_price = itemView.findViewById(R.id.total_price);


            order_detail_rcv = itemView.findViewById(R.id.rcv_order_detail_list_id);

        }
    }
}
