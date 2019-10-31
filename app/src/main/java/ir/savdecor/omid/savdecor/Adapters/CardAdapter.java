package ir.savdecor.omid.savdecor.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.savdecor.omid.savdecor.Activities.CardActivity;
import ir.savdecor.omid.savdecor.Models.CardList;
import ir.savdecor.omid.savdecor.R;
import ir.savdecor.omid.savdecor.Utilities.Helper;
import ir.savdecor.omid.savdecor.Utilities.MinMaxFilter;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {


    private Context context;
    public int global_delete_status = 0;
    private List<CardList> data;

    public CardAdapter(Context context, List data) {
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);


        return new CardAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final CardList cardList = data.get(position);


//        List<String> list = new ArrayList<>();
//        list.add("1");
//        list.add("2");
//        list.add("3");
//        list.add("4");
//        list.add("5");
//        list.add("6");
//        list.add("7");
//        list.add("8");
//        list.add("9");
//        list.add("10");
//        holder.count_spinner.setItems(list);
//        holder.count_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
//            @Override
//            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
//                Toast.makeText(context, id + "Clicked " + item, Toast.LENGTH_LONG).show();
//                String total_price = holder.price.getText().toString().replace(",", "");
//                int tp = Integer.parseInt(total_price)*Integer.parseInt(item);
//
//                holder.total_price.setText(tp+"");
//
//                change_product_count(cardList.getOrder_product_id(),item);
//            }
//        });


        double width = cardList.getWidth();
        double height = cardList.getHeight();

        holder.card_edt_width.setText(width + "");
        holder.card_edt_height.setText(height + "");

        holder.card_edt_width.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                double w = 1, h = 1;
                BigDecimal tp;

                try {
                    w = Double.valueOf(holder.card_edt_width.getText().toString());
                    h = Double.valueOf(holder.card_edt_height.getText().toString());

                } catch (Exception e) {

                }

                int price = Helper.getDiscountValue(cardList.getPrice(), cardList.getDiscount());

                tp = new BigDecimal(w * h * (double) price);

//                tp = new BigDecimal(w * h * Double.valueOf(cardList.getPrice()));
//                tp = Math.ceil(tp);

                holder.total_price.setText(tp + " تومان ");

                holder.txt_width_mul_height.setText(w * h + " متر مربع ");

                change_product_count(cardList.getOrder_product_id(), "0" + "", w, h, tp);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        holder.card_edt_height.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                double w = 1, h = 1;
                BigDecimal tp;

                try {
                    w = Double.valueOf(holder.card_edt_width.getText().toString());
                    h = Double.valueOf(holder.card_edt_height.getText().toString());

                } catch (Exception e) {

                }

                int price = Helper.getDiscountValue(cardList.getPrice(), cardList.getDiscount());

                tp = new BigDecimal(w * h * (double) price);

//                tp = Math.ceil(tp);
                holder.txt_width_mul_height.setText(w * h + " متر مربع ");

                holder.total_price.setText(tp + " تومان ");

                change_product_count(cardList.getOrder_product_id(), "0" + "", w, h, tp);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        holder.card_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int edt_count_;
                String edt_text = holder.card_edt_count.getText().toString();
                try {
                    edt_count_ = Integer.parseInt(edt_text);
                } catch (Exception e) {
                    edt_count_ = 1;
                }
                if (edt_count_ > 0) {
                    edt_count_ += 1;

                }
                holder.card_edt_count.setText(edt_count_ + "");

                int price = Helper.getDiscountValue(cardList.getPrice(), cardList.getDiscount());

//                int price = Integer.parseInt(holder.discount_price.getText().toString().replace(",", ""));
//                double tp = 0;
                BigDecimal tp = new BigDecimal(0);


                switch (cardList.getUnit()) {


                    case "بسته":
                        double box_count = Math.ceil(edt_count_ / cardList.getSize());
//                        tp = price * box_count;
                        tp = new BigDecimal(price * box_count);


                        holder.txt_count_mul_meter.setText(box_count + "   بسته ");
                        change_product_count(cardList.getOrder_product_id(), edt_count_ + "", 0, 0, tp);

                        break;
                    case "عدد":
                        holder.txt_count_mul_meter.setText("");
                        tp = new BigDecimal(price * edt_count_);


//                        tp = price * edt_count_;
                        change_product_count(cardList.getOrder_product_id(), edt_count_ + "", 0, 0, tp);

                        break;

                    default:
                        holder.txt_count_mul_meter.setText("");

                        break;


                }


                holder.total_price.setText(tp + " تومان ");

            }
        });


        holder.card_mines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int edt_count_;
                String edt_text = holder.card_edt_count.getText().toString();
                try {
                    edt_count_ = Integer.parseInt(edt_text);
                } catch (Exception e) {
                    edt_count_ = 1;
                }
                if (edt_count_ > 1) {
                    edt_count_ -= 1;

                }
                holder.card_edt_count.setText(edt_count_ + "");

                int price = Helper.getDiscountValue(cardList.getPrice(), cardList.getDiscount());

//                int price = Integer.parseInt(holder.price.getText().toString().replace(",", ""));
//                double tp = 0;
                BigDecimal tp = new BigDecimal(0);
                switch (cardList.getUnit()) {


                    case "بسته":
                        double box_count = Math.ceil(edt_count_ / cardList.getSize());
//                        tp = price * box_count;
                        tp = new BigDecimal(price * box_count);

                        holder.txt_count_mul_meter.setText(box_count + "   بسته ");
                        change_product_count(cardList.getOrder_product_id(), box_count + "", 0, 0, tp);

                        break;
                    case "عدد":
                        holder.txt_count_mul_meter.setText("");
                        tp = new BigDecimal(price * edt_count_);

//                        tp = price * edt_count_;
                        change_product_count(cardList.getOrder_product_id(), edt_count_ + "", 0, 0, tp);

                        break;


                }


                // Assigning the converted value of bg to d

                holder.total_price.setText(tp + " تومان ");


            }
        });


        switch (cardList.getUnit()) {
            case "متر مربع":
                holder.unit_width_height.setVisibility(View.VISIBLE);
                holder.unit_count_and_meter.setVisibility(View.GONE);

                break;

            case "بسته":
                holder.unit_count_and_meter.setVisibility(View.VISIBLE);
                holder.unit_width_height.setVisibility(View.GONE);
                holder.txt_count_mul_meter.setText(cardList.getBoxCount() + "   بسته ");
                break;

            case "عدد":
                holder.unit_count_and_meter.setVisibility(View.VISIBLE);
                holder.unit_width_height.setVisibility(View.GONE);


                break;


        }


        DecimalFormat df = new DecimalFormat("#.00");
        String width_mul_height = df.format(width * height);


        holder.txt_width_mul_height.setText(width_mul_height + " متر مربع ");
        holder.title.setText(cardList.getTitle());


        int discount_price = Helper.getDiscountValue(cardList.getPrice(), cardList.getDiscount());
        holder.price.setText(cardList.getPrice() + "  ");
        holder.discount_price.setText("" + discount_price + "  ");


        if (cardList.getDiscount().equals("۰")) {
            holder.price.setTextColor(context.getResources().getColor(R.color.colorGreen));
//            holder.discount_price.setVisibility(View.GONE);

        } else {
            holder.price.setTextColor(context.getResources().getColor(R.color.colorGray));
            holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


            holder.discount_price.setVisibility(View.VISIBLE);
        }

        holder.price.setText(cardList.getPrice());
        Log.e("cardList", cardList.getCount() + "__");
        if (cardList.getCount() == 0) {
            holder.card_edt_count.setText("1");

        } else {
            holder.card_edt_count.setText(cardList.getCount() + "");

        }
        holder.total_price.setText(cardList.getTotalPrice() + " تومان ");
        Glide.with(context)
                .load(Helper.basUrl + cardList.getImage_path())
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
        holder.delete_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardList cardList1 = data.get(position);

                delete(cardList1.getOrder_product_id(),position);


            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, price, discount_price, total_price, delete_card, txt_width_mul_height, txt_count_mul_meter;
        public ImageView image_path;
        public ProgressBar progressBar;
        public RelativeLayout card_layout;
        public MaterialSpinner count_spinner;
        public LinearLayout unit_count_and_meter, unit_width_height;
        public EditText card_edt_count, card_edt_width, card_edt_height;
        public Button card_plus, card_mines;


        public MyViewHolder(View itemView) {

            super(itemView);
            title = itemView.findViewById(R.id.title);
            txt_width_mul_height = itemView.findViewById(R.id.txt_width_mul_height);
            txt_count_mul_meter = itemView.findViewById(R.id.txt_count_mul_meter);
            image_path = itemView.findViewById(R.id.image_path);
            price = itemView.findViewById(R.id.price);
            discount_price = itemView.findViewById(R.id.discount_price);
            total_price = itemView.findViewById(R.id.total_price);

            progressBar = itemView.findViewById(R.id.card_progress);
            delete_card = itemView.findViewById(R.id.delete_card);
            card_layout = itemView.findViewById(R.id.card_layout);
            count_spinner = itemView.findViewById(R.id.count_spinner);
            card_plus = itemView.findViewById(R.id.card_plus);
            card_mines = itemView.findViewById(R.id.card_mines);
            card_edt_count = itemView.findViewById(R.id.card_edt_count);
            card_edt_width = itemView.findViewById(R.id.edt_width);
            card_edt_height = itemView.findViewById(R.id.edt_height);
            unit_count_and_meter = itemView.findViewById(R.id.unit_count_and_meter);
            unit_width_height = itemView.findViewById(R.id.unit_width_height);

            card_edt_count.setFilters(new InputFilter[]{new MinMaxFilter("1", "999")});

        }
    }


    public void delete(final int order_product_id, final int position) {

        String URL = Helper.basUrl + "api/delete_order";
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("___0", response);
//                        JSONObject obj = null;
                        try {
                            JSONObject obj = new JSONObject(response);


//                            JSONObject jsonObject1 = obj.getJSONObject("detail") ;
//
//                            int my_total_price = jsonObject1.getInt("total_price");
//                            int my_total_percent_price = jsonObject1.getInt("total_percent_price");
//                            int my_final_price = jsonObject1.getInt("final_price");
//


                              global_delete_status = obj.getInt("status");
                            String message = obj.getString("message");

                            if(global_delete_status==1){


                                data.remove(position);
                                CardActivity.cardAdapter.notifyDataSetChanged();
                                global_delete_status=0;



                            }

                            Helper.message(context, message);

//                            String detail = obj.getString("detail");


//                            if (status1 == 1) {
//                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();

//                                CardActivity.sum_total_price.setText(my_total_price + " تومان 222");
//                                CardActivity.discount_price.setText(my_total_percent_price + " تومان ");
//                                CardActivity.final_price.setText(my_final_price + " تومان ");

//                                Log.e("KKKLLL",my_final_price+"gg");

//                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Helper.message(context, error.getMessage());
                        Log.d("ERROR", "error => " + error.toString());
                    }
                }
        ) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", Helper.getUserToken(context));
                params.put("Accept-Language", "fa");

                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("order_product_id", order_product_id + "");


                return params;
            }


        };
        postRequest.setShouldCache(false);


        queue.add(postRequest);


    }

    public void change_product_count(final int order_product_id, final String count, final double width, final double height, final BigDecimal total_price) {

        String URL = Helper.basUrl + "api/change_product_count";
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("response", response);
                        try {
                            JSONObject obj = new JSONObject(response);


                            JSONObject jsonObject1 = obj.getJSONObject("detail");

                            int my_total_price = jsonObject1.getInt("total_price");
                            int my_total_percent_price = jsonObject1.getInt("total_percent_price");
                            int my_final_price = jsonObject1.getInt("final_price");


                            int status = obj.getInt("status");
//                            String message = obj.getString("message");
//                            String detail = obj.getString("detail");
                            if (status == 1) {
//                                Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                                CardActivity.sum_total_price.setText(my_total_price + " تومان ");
                                CardActivity.discount_price.setText(my_total_percent_price + " تومان ");
                                CardActivity.final_price.setText(my_final_price + " تومان ");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Helper.message(context, error.getMessage());
                        Log.d("ERROR", "error => " + error.toString());
                    }
                }
        ) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", Helper.getUserToken(context));
                params.put("Accept-Language", "fa");

                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("order_product_id", order_product_id + "");
                params.put("count", count);
                params.put("width", width + "");
                params.put("height", height + "");
                params.put("price", total_price + "");


                return params;
            }


        };
        postRequest.setShouldCache(false);


        queue.add(postRequest);


    }


}
