package ir.savdecor.omid.savdecor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import ir.savdecor.omid.savdecor.Activities.ProductDetailActivity;
import ir.savdecor.omid.savdecor.Models.OrderDetailList;
import ir.savdecor.omid.savdecor.Models.ProductList;
import ir.savdecor.omid.savdecor.R;
import ir.savdecor.omid.savdecor.Utilities.Helper;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyViewHolder> {


    private Context context;

    private List<OrderDetailList> data;

    public OrderDetailAdapter(Context context, List data) {
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail_item, parent, false);


        return new OrderDetailAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final OrderDetailList orderDetailList = data.get(position);

        String txt_discount = "";
        if (!orderDetailList.getDiscount().equals("۰")) {
            txt_discount = "("+orderDetailList.getDiscount() + " درصد تخفیف) ";
        }
        else{
            txt_discount = "";
        }
        holder.id.setText(orderDetailList.getId() + "");
        holder.title.setText(orderDetailList.getTitle());
        holder.price.setText(orderDetailList.getPrice() + " تومان ");


        holder.unit_value.setText(orderDetailList.getUnit_value() + "  " + orderDetailList.getUnit());
        holder.total_price.setText(orderDetailList.getTotal_price() + " تومان "+txt_discount);

        Glide.with(context)
                .load(Helper.basUrl + orderDetailList.getImage_path())
                .thumbnail(0.2f)
                // .centerCrop()
//                .transform(new CircleTransform(context))
                // .error(R.drawable.profile)
                //   .transform( new BlurTransformation( context ),new CircleTransform(context) )

                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .placeholder(Color.BLUE)
                .crossFade()
                .into(holder.image_path);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView id, title, price, unit_value, total_price;
        public ImageView image_path;


        public MyViewHolder(View itemView) {

            super(itemView);
            id = itemView.findViewById(R.id.id);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            total_price = itemView.findViewById(R.id.total_price);
            unit_value = itemView.findViewById(R.id.unit_value);
            image_path = itemView.findViewById(R.id.image_path);

        }
    }
}
