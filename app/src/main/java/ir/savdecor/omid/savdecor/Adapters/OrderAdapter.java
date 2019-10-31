package ir.savdecor.omid.savdecor.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
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
import ir.savdecor.omid.savdecor.R;
import ir.savdecor.omid.savdecor.Utilities.Helper;

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

//       final int discount = orderList.getDiscount();
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
                    JSONObject service = jsonObject.getJSONObject("service");
                    int count = pivot.getInt("count");
                    int price = pivot.getInt("price");
                    double width = pivot.getInt("width");
                    double height = pivot.getInt("height");



                    double size = jsonObject.getDouble("size");

                    String unit = service.getString("unit");
                    int total_price = 0;
                    OrderDetailList u = new OrderDetailList();
                    u.setId(jsonObject.getInt("id"));
                    u.setTitle(jsonObject.getString("title"));
                    u.setImage_path(jsonObject.getString("image_path"));
                    u.setDiscount(jsonObject.getString("discount"));
                    u.setUnit(unit);
                    u.setPrice(jsonObject.getInt("price"));


                    u.setTotal_price(price);


                    switch (unit) {
                        case "متر مربع":
                            double res=width * height;

                            u.setUnit_value(res+"");

                            break;

                        case "بسته":
                            double  box_count = Math.ceil(count / size);

                            u.setUnit_value(box_count+"");
                            break;

                        case "عدد":
                            u.setUnit_value(count+"");



                            break;


                    }


                    holder.data_order_details.add(u);


                } catch (JSONException e) {
                    e.printStackTrace();
                    //   progressDialog2.dismiss();


                }


            }
            holder.orderDetailAdapter.notifyDataSetChanged();


            holder.id.setText(position + 1 + "");

            if (orderList.getDiscount() == 0) {
                holder.total_price.setTextColor(context.getResources().getColor(R.color.colorGreen));

                holder.discount_price.setVisibility(View.GONE);
            } else {
                holder.total_price.setTextColor(context.getResources().getColor(R.color.colorGray));

                holder.total_price.setPaintFlags(holder.discount_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                holder.discount_price.setVisibility(View.VISIBLE);

            }
            holder.total_price.setText(orderList.getTotal_price() + "");
            holder.discount_price.setText(Helper.getDiscountPrice(orderList.getTotal_price(), orderList.getDiscount()) + "");

            holder.order_no.setText("NO_" + orderList.getId() + "");
            holder.created_at.setText(orderList.getCreated_at());
        } catch (Exception e) {
            Log.e("____", "_____qq" + e.getMessage());

        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView total_price, discount_price, id, order_no, created_at;


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
            discount_price = itemView.findViewById(R.id.discount_price);


            order_detail_rcv = itemView.findViewById(R.id.rcv_order_detail_list_id);

        }
    }
}
