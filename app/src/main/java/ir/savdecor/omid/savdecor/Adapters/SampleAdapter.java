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

import java.util.List;

import ir.savdecor.omid.savdecor.Activities.ProductActivity;
import ir.savdecor.omid.savdecor.Activities.SampleActivity;
import ir.savdecor.omid.savdecor.Models.HomeList;
import ir.savdecor.omid.savdecor.Models.SampleList;
import ir.savdecor.omid.savdecor.R;
import ir.savdecor.omid.savdecor.Utilities.Helper;

public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.MyViewHolder> {


    private Context context;

    private List<SampleList> data;

    public SampleAdapter(Context context, List data) {
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_item, parent, false);


        return new SampleAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final SampleList sampleList = data.get(position);

        holder.title.setText(sampleList.getTitle());
        Glide.with(context)
                .load(Helper.basUrl + sampleList.getImage_path())
                .thumbnail(0.2f)
                // .centerCrop()
//                .transform(new CircleTransform(context))
                // .error(R.drawable.profile)
                //   .transform( new BlurTransformation( context ),new CircleTransform(context) )


                .placeholder(Color.BLUE)
                .crossFade()
                .into(holder.image_path);

        holder.image_path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProductActivity.class);

                i.putExtra("service_title", sampleList.getService_title());
                i.putExtra("service_id", sampleList.getService_id());
                i.putExtra("sample_id", sampleList.getId());
                i.putExtra("sample_title", sampleList.getTitle());
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

        public MyViewHolder(View itemView) {

            super(itemView);
            image_path = itemView.findViewById(R.id.image_path);
            title = itemView.findViewById(R.id.title);
        }
    }
}
