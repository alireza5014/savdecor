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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import ir.savdecor.omid.savdecor.Activities.ProductActivity;
import ir.savdecor.omid.savdecor.Models.CommentList;
import ir.savdecor.omid.savdecor.Models.SampleList;
import ir.savdecor.omid.savdecor.R;
import ir.savdecor.omid.savdecor.Utilities.CircleTransform;
import ir.savdecor.omid.savdecor.Utilities.Helper;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {


    private Context context;

    private List<CommentList> data;

    public CommentAdapter(Context context, List data) {
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);


        return new CommentAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final CommentList commentList = data.get(position);

        holder.comment.setText(commentList.getMessage());
        holder.created_at.setText(commentList.getCreated_at());

        Glide.with(context)
                .load(Helper.basUrl + commentList.getImage_path())
                .thumbnail(0.2f)
                  .centerCrop()
                .transform(new CircleTransform(context))
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


        holder.fullname.setText(commentList.getFname() + " " + commentList.getLname());


    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView fullname, created_at, comment;
        public ImageView image_path;

        public MyViewHolder(View itemView) {

            super(itemView);
            fullname = itemView.findViewById(R.id.fullname);
            comment = itemView.findViewById(R.id.comment_txt);
            created_at = itemView.findViewById(R.id.created_at);
            image_path = itemView.findViewById(R.id.image_path);
        }
    }
}
