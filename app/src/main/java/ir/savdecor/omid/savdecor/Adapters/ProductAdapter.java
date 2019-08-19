package ir.savdecor.omid.savdecor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import ir.savdecor.omid.savdecor.Activities.ProductDetailActivity;
import ir.savdecor.omid.savdecor.Activities.SampleActivity;
import ir.savdecor.omid.savdecor.Models.ProductList;
import ir.savdecor.omid.savdecor.Models.SampleList;
import ir.savdecor.omid.savdecor.R;
import ir.savdecor.omid.savdecor.Utilities.Helper;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {


    private Context context;

    private List<ProductList> data;

    public ProductAdapter(Context context, List data) {
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);


        return new ProductAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final ProductList productList = data.get(position);

        holder.title.setText(productList.getTitle());

        if (productList.getPrice().equals("۰")) {
            holder.price.setText("تماس بگیرید");

        } else if (productList.getCount() == 0) {
            holder.price.setText("موجود نیست");

        } else {
            int discount_price = Helper.getDiscountValue(productList.getPrice(), productList.getDiscount());
            holder.price.setText(productList.getPrice() + " تومان ");
            holder.discount_price.setText(discount_price + " تومان ");

        }
        holder.discount.setText("%" + productList.getDiscount() + " تخفیف ");
        if (productList.getDiscount().equals("۰")) {
            holder.price.setTextColor(context.getResources().getColor(R.color.colorGreen));

            holder.discount.setVisibility(View.GONE);
            holder.discount_price.setVisibility(View.GONE);
        } else {
            holder.price.setTextColor(context.getResources().getColor(R.color.colorGray));

            holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            holder.discount_price.setVisibility(View.VISIBLE);
            holder.discount.setVisibility(View.VISIBLE);

        }

//        holder.progressBar.setVisibility(View.VISIBLE);
        Glide.with(context)
                .load(Helper.basUrl + productList.getImage_path())
                .thumbnail(0.2f)
                // .centerCrop()
//                .transform(new CircleTransform(context))
                // .error(R.drawable.profile)
                //   .transform( new BlurTransformation( context ),new CircleTransform(context) )


                .placeholder(Color.BLUE)
                .crossFade()
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
                .into(holder.image_path);

        holder.product_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(context, ProductDetailActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("product_id", productList.getId());
                    i.putExtra("product_title", productList.getTitle());
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(i);
                } catch (Exception e) {
                    Log.e("_____", e.getMessage());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image_path;
        public TextView title, price, discount_price, discount;

        public RelativeLayout product_layout;
        public ProgressBar progressBar;

        public MyViewHolder(View itemView) {

            super(itemView);
            image_path = itemView.findViewById(R.id.image_path);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            discount_price = itemView.findViewById(R.id.discount_price);
            product_layout = itemView.findViewById(R.id.product_layout);
            discount = itemView.findViewById(R.id.discount);
            progressBar = itemView.findViewById(R.id.progress);
        }
    }
}
