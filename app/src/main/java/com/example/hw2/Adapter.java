package com.example.hw2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static android.os.FileUtils.copy;
import static androidx.core.content.ContextCompat.startActivity;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    JSONArray data;
    boolean[] clicked;
    Drawable f;
    Drawable n;

    public Adapter(JSONArray j, Drawable fav, Drawable notFav){
        data = j;
        clicked = new boolean[j.length()];
        f = fav;
        n = notFav;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.beer_info, viewGroup, false);
        return new ViewHolder(v, clicked, f, n);
    }


    @Override
    public void onBindViewHolder(ViewHolder vh, final int position){
        try{
            //get info
            JSONObject obj = (JSONObject) data.get(position);
            String imgSrc = (String) obj.get("image_url");
            String name = (String) obj.get("name");
            String description = (String) obj.get("description");
            //set data
            vh.name.setText(name);
            vh.info.setText(description.length() > 75 ? description.substring(0, 75) + "..." : description);
            vh.obj = obj;
            //set image
            new DownloadImageTask((ImageView) vh.pic)
                    .execute(imgSrc);
            //set star icon
            vh.icon.setImageDrawable(n);
        }
        catch(JSONException e){
            Log.e("error", e.toString());
        }

    }


    @Override
    public int getItemCount() {
        return data.length();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView info;
        ImageView pic;
        ImageView icon;
        JSONObject obj;

        public ViewHolder(View view, boolean[] clicked, Drawable f, Drawable n) {
            super(view);
            name = view.findViewById(R.id.navigation_header_container);
            info = view.findViewById(R.id.body);
            pic = view.findViewById(R.id.image);
            icon = view.findViewById(R.id.icon);
            pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), FourthActivity.class);
                    i.putExtra("info", obj.toString());
                    startActivity(v.getContext(), i, null);
                }
            });
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clicked[getAdapterPosition()]){
                        icon.setImageDrawable(n);
                        clicked[getAdapterPosition()] = false;
                    }
                    else{
                        icon.setImageDrawable(f);
                        clicked[getAdapterPosition()] = true;
                    }
                }
            });
        }


    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
