package ir.savdecor.omid.savdecor.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ir.savdecor.omid.savdecor.R;

public class AboutUsActivity extends AppCompatActivity {
    public TextView action_bar_title;
    public ImageView back, search, basket;




    public ImageView instagram,telegram;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);


        action_bar_title = findViewById(R.id.action_bar_title);

        action_bar_title.setText("سوابق خرید");

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SearchActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(i);
            }
        });

        basket = findViewById(R.id.basket);
        basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CardActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(i);
            }
        });




        instagram=findViewById(R.id.instagram);
        telegram=findViewById(R.id.telegram);

        telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telegram = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Poshtibani_savdecor"));
                telegram.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(telegram);
            }
        });


        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent instagram = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/savdecor"));
                instagram.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(instagram);
            }
        });
    }
}
