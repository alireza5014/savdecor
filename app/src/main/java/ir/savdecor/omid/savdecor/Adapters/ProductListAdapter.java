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

import ir.savdecor.omid.savdecor.Activities.ProductActivity;
import ir.savdecor.omid.savdecor.Activities.ProductDetailActivity;
import ir.savdecor.omid.savdecor.Activities.SampleActivity;
import ir.savdecor.omid.savdecor.Models.HomeList;
import ir.savdecor.omid.savdecor.Models.ProductList;
import ir.savdecor.omid.savdecor.Models.ProductListList;
import ir.savdecor.omid.savdecor.R;
import ir.savdecor.omid.savdecor.Utilities.Helper;

//import ir.savdecor.omid.savdecor.Utilities.CircleTransform;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {

    private Context context;

    private List<ProductListList> data;


    public ProductListAdapter(Context context, List data) {
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item, parent, false);


        return new MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {


        final ProductListList productListList = data.get(position);

        holder.title.setText(productListList.getTitle());
        holder.products_count.setText(productListList.getProducts_count() + "");
        holder.product_list_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProductActivity.class);

                i.putExtra("sample_title", "");

                i.putExtra("service_id", productListList.getId());
                i.putExtra("service_title", productListList.getTitle());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, products_count;
        public RelativeLayout product_list_layout;


        public MyViewHolder(View itemView) {

            super(itemView);

            product_list_layout = itemView.findViewById(R.id.product_list_layout);
            title = itemView.findViewById(R.id.title);
            products_count = itemView.findViewById(R.id.products_count);

        }
    }
}
