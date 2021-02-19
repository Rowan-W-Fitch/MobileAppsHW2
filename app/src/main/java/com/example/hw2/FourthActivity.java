package com.example.hw2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class FourthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);
        Intent i = getIntent();
        String data = i.getExtras().getString("info");
        JSONObject jsn = null;
        try{
            jsn = new JSONObject(data);
            //need abv, first_brewed, image_url, description, food_pairing, brewers_tips
            String name = jsn.getString("name");
            double abv = jsn.getDouble("abv");
            String first_brewed = jsn.getString("first_brewed");
            String url = jsn.getString("image_url");
            String description = jsn.getString("description");
            String food = jsn.getString("food_pairing");
            JSONArray foods = new JSONArray(food);
            String foood = "";
            for(int x=0; x<foods.length(); x++){
                foood += (String) foods.get(x) + " ";
            }
            String tips = jsn.getString("brewers_tips");
            populateView(name, abv, first_brewed, url, description, foood, tips);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void populateView(String name, double abv, String first, String url, String description, String food, String tips){
        TextView tv1 = findViewById(R.id.text);
        tv1.setText(name);
        TextView tv2 = findViewById(R.id.text2);
        tv2.setText("abv: " + Double.toString(abv));
        TextView tv3 = findViewById(R.id.action_text);
        tv3.setText("first brewed: " + first);
        //set imageview src
        new DownloadImageTask((ImageView) findViewById(R.id.image))
                .execute(url);
        TextView tv4 = findViewById(R.id.decelerate);
        tv4.setText("description: " + description);
        TextView tv5 = findViewById(R.id.center);
        tv5.setText("food pairings: "+food);
        TextView tv6 = findViewById(R.id.chain);
        tv6.setText("brewers tips: "+ tips);
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
