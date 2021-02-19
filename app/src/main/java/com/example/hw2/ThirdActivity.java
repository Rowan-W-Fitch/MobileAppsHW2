package com.example.hw2;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;

public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        RecyclerView RV = findViewById(R.id.scrollView);
        Intent i = getIntent();
        String data = i.getExtras().getString("results");
        JSONArray jsn = null;
        try {
            jsn = new JSONArray(data);
            resultCount(jsn);
            Drawable fav = makeImage("star_fill.png");
            Drawable notFav = makeImage("star_empty.jpg");
            Adapter adpt = new Adapter(jsn, fav, notFav);
            RV.setAdapter(adpt);
            RV.setLayoutManager(new LinearLayoutManager(this));
        } catch (JSONException e) {
            Log.d("error", "line 36 3rd activity");
            e.printStackTrace();
        }
    }



    public void resultCount(JSONArray j){
        int length = j.length();
        Log.d("length", " "+ length);
        TextView tv = findViewById(R.id.text);
        tv.setText("We found " + length + " results");
    }

    public Drawable makeImage(String file){
        try {
            InputStream ims = getAssets().open(file);
            Drawable d = Drawable.createFromStream(ims, null);
            ims.close();
            return d;
        }catch(IOException ex)
        {
            return null;
        }
    }
}
