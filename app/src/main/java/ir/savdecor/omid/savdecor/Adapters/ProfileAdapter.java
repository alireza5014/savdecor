package ir.savdecor.omid.savdecor.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import ir.savdecor.omid.savdecor.Activities.ChangePasswordActivity;
import ir.savdecor.omid.savdecor.Activities.EditProfileActivity;

import ir.savdecor.omid.savdecor.Activities.FavouriteActivity;
import ir.savdecor.omid.savdecor.Activities.OrderActivity;
import ir.savdecor.omid.savdecor.Activities.ProfileActivity;
import ir.savdecor.omid.savdecor.MainActivity;
import ir.savdecor.omid.savdecor.Models.ProfileList;
import ir.savdecor.omid.savdecor.R;
import ir.savdecor.omid.savdecor.Utilities.Helper;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder> {
    private Context context;
    private List<ProfileList> data;


    public ProfileAdapter(Context context, List data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_item, parent, false);


        return new ProfileAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final ProfileList profileList = data.get(position);

        holder.title.setText(profileList.getTitle());

        Glide.with(context)
                .load(Helper.basUrl + profileList.getImage_path())
                .thumbnail(0.2f)
                // .centerCrop()
//                .transform(new CircleTransform(context))
                // .error(R.drawable.profile)
                //   .transform( new BlurTransformation( context ),new CircleTransform(context) )


                .placeholder(Color.BLUE)
                .crossFade()

                .into(holder.image_path);
        holder.ll_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = null;

                switch (position) {
                    case 0:
                        intent = new Intent(context, EditProfileActivity.class);
                        break;
                    case 1:
                        intent = new Intent(context, OrderActivity.class);
                        break;
                    case 2:
                        intent = new Intent(context, FavouriteActivity.class);
                        break;
                    case 3:
                        intent = new Intent(context, ChangePasswordActivity.class);
                        break;



                }
                if (intent != null) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(intent);

                }

                Helper.message(context, position + "");
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
        public LinearLayout ll_profile;


        public MyViewHolder(View itemView) {
            super(itemView);
            image_path = itemView.findViewById(R.id.image_path);
            title = itemView.findViewById(R.id.title);
            ll_profile = itemView.findViewById(R.id.ll_profile);


        }
    }

}
