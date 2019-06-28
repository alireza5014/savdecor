package ir.savdecor.omid.savdecor.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ir.savdecor.omid.savdecor.Models.OrderList;
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


        holder.id.setText(position+1+"");
        holder.total_price.setText(orderList.getTotla_price()+"");
        holder.order_no.setText("NO_"+orderList.getId()+"");
        holder.created_at.setText(orderList.getCreated_at());

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView total_price, id, order_no, created_at;

        public MyViewHolder(View itemView) {
            super(itemView);

            created_at = itemView.findViewById(R.id.created_at);
            order_no = itemView.findViewById(R.id.order_no);
            id = itemView.findViewById(R.id.id);
            total_price = itemView.findViewById(R.id.total_price);

        }
    }
}
