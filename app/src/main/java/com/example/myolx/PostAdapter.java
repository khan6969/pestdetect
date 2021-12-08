package com.example.myolx;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;


import model.PostModel;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private Context context;
    private ArrayList<PostModel> postList;

    public PostAdapter(Context context, ArrayList<PostModel> postList) {
        this.context = context;
        this.postList = postList;
    }


    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.post2item,parent,false);
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.secondlayout,parent,false);
        //        View view = LayoutInflater.from(context)
//                .inflate(R.layout.postitem,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        final PostModel L = postList.get(position);
        String imageUrl;
        holder.tvname.setText(L.getName());
        holder.TPrice.setText(L.getPrice());
        holder.Tphone.setText(L.getNumber());
        holder.tvCondition.setText(L.getCondition());
       // holder.tvTime.setText(L.getTimeAdd().toString());
        // source for TimeStamp https://medium.com/@shaktisinh/time-a-go-in-android-8bad8b171f87 ....
        String timeAgo = (String) DateUtils
                .getRelativeTimeSpanString(L
                .getTimeAdd()
                .getSeconds() * 1000);
        holder.tvTime.setText(timeAgo);
        holder.Tphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // lead to phone deal from post ...
//                Intent callIntent = new Intent(Intent.ACTION_DIAL);
//                callIntent.setData(Uri.parse("tel:" + L.getNumber()));
//                context.startActivity(callIntent);
              //   openWhatsap(L.getNumber());
                // this click leads from click number to whatsapp
                boolean installed = appInstalledOrNot("com.whatsapp");
                if (installed){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+"+92"+L.getNumber()));
                    context.startActivity(intent);
                }else {
                    Toast.makeText(context, "Whats app not installed on your device", Toast.LENGTH_SHORT).show();

                }

            }
        });

        imageUrl= L.getImageURi();
//        Picasso.get()
//
//                .load(imageUrl)
//                .placeholder(R.drawable.sale)
//                .fit()
//                .into(holder.PostImage);
        RequestOptions options = new RequestOptions();
        int corner_size = 40;
        options.placeholder(R.drawable.ic_launcher_background)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(R.drawable.ic_launcher_foreground)
                .transform(new CenterCrop())
                .transform(new RoundedCorners(corner_size));

        Glide.with(context).load(imageUrl)
                .apply(options)
                .into(holder.PostImage);
//        final Transformation transformation = new RoundedCornersTransformation(50, 5);
//        Picasso.get().load(imageUrl).transform(transformation).into(holder.PostImage);
    }

    private boolean appInstalledOrNot(String s) {
     //in fragment
        PackageManager packageManager =  context.getPackageManager();
     //in activity
      //  PackageManager packageM =  getPackageManager();
        boolean app_installed;
        try {
            packageManager.getPackageInfo(s,PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }catch (PackageManager.NameNotFoundException e){
            app_installed = false;
        }
        return app_installed;

    }

    private void openWhatsap(String number) {
            try {
               // number = number.replace(" ", "").replace("+", "");

                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));
                sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(number)+"@s.whatsapp.net");
                context.startActivity(sendIntent);

            } catch(Exception e) {
                Log.e("whatsapperror", "ERROR_OPEN_MESSANGER"+e.toString());
                Toast.makeText(context, "whatsappError"+e.toString(), Toast.LENGTH_LONG).show();
            }

    }

    @Override
    public int getItemCount() {
        return postList == null? 0:postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
      public ImageView PostImage;
      public TextView tvTime,tvCondition,tvname, Tphone,TPrice;
        public CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            PostImage = itemView.findViewById(R.id.PostImage);
            tvname = itemView.findViewById(R.id.tvname);
            tvCondition = itemView.findViewById(R.id.tvCondition);
            tvTime = itemView.findViewById(R.id.tvTime);
            cardView = itemView.findViewById(R.id.Crview);
            Tphone =itemView.findViewById(R.id.Tvphone);
            TPrice =itemView.findViewById(R.id.TvPrice);

        }
//      public ViewHolder(@NonNull View itemView,Context ctx) {
//            super(itemView);
//            context =ctx;
//            PostImage = itemView.findViewById(R.id.PostImage);
//                tvname = itemView.findViewById(R.id.tvname);
//            tvCondition = itemView.findViewById(R.id.tvCondition);
//            tvTime = itemView.findViewById(R.id.tvTime);
//            cardView = itemView.findViewById(R.id.Crview);
//
//      }
    }
}
