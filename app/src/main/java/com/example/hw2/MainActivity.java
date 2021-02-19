package com.example.hw2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout L = findViewById(R.id.labeled);
        makeImage(L);
        addButton(L);
    }

    public void nextActivity(){
        Intent i = new Intent(this, SecondActivity.class);
        startActivity(i);
    }

    public void makeImage(LinearLayout L){
        try {
            ImageView iw= new ImageView(this);
            InputStream ims = getAssets().open("beer.jpg");
            Drawable d = Drawable.createFromStream(ims, null);
            iw.setImageDrawable(d);
            ims.close();
            L.addView(iw);
        }catch(IOException ex)
        {
            return;
        }
    }

    public void addButton(LinearLayout L){
        Button b = new Button(this);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nextActivity();
            }
        });
        b.setText("Get Started");
        L.addView(b);
    }
}