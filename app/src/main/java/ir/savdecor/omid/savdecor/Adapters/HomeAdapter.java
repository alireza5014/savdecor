package ir.savdecor.omid.savdecor.Adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;

import ir.savdecor.omid.savdecor.Activities.ProductActivity;
import ir.savdecor.omid.savdecor.Activities.SampleActivity;
import ir.savdecor.omid.savdecor.MainActivity;
import ir.savdecor.omid.savdecor.R;

import java.util.List;

import ir.savdecor.omid.savdecor.Models.HomeList;
//import ir.savdecor.omid.savdecor.Utilities.CircleTransform;
import ir.savdecor.omid.savdecor.Utilities.Helper;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    private Context context;

    private List<HomeList> data;


    public HomeAdapter(Context context, List data) {
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false);


        return new MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {


        final HomeList homeList = data.get(position);

        holder.title.setText(homeList.getTitle());
        Glide.with(context)
                .load(Helper.basUrl + homeList.getImage_path())
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

        holder.image_path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                if (homeList.getSamples_count() > 0) {
                    i = new Intent(context, SampleActivity.class);
                } else {
                    i = new Intent(context, ProductActivity.class);
                }
                i.putExtra("sample_title", "");

                i.putExtra("service_id", homeList.getId());
                i.putExtra("service_title", homeList.getTitle());
                context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image_path;
        public TextView title;
        public ProgressBar progressBar;

        public MyViewHolder(View itemView) {

            super(itemView);
            image_path = itemView.findViewById(R.id.image_path);
            title = itemView.findViewById(R.id.title);
            progressBar = itemView.findViewById(R.id.progress);
        }
    }
}
