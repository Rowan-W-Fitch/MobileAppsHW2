package com.example.hw2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        String[] state = new String[4];
        LinearLayout L = findViewById(R.id.layout);
        addInputs(L, state);
        //add search button
        addGo(state);
    }

    public void addInputs(LinearLayout L, String[] state){
        //add name input
        EditText name = new EditText(this);
        name.setHint("search beer by name...");
        name.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                state[0] = sanitize(s.toString());
                Log.d("state", Arrays.toString(state));
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        name.setSingleLine();
        L.addView(name);
        //add date inputs
        addDateInputs(L, state);
        //add radio button
        addRadio(L, state);
    }

    public String sanitize(String s){
        String trim = s.trim();
        trim = trim.replaceAll(" ", "_");
        return trim;
    }

    public void addDateInputs(LinearLayout L, String[] state){
        LinearLayout h = new LinearLayout(this);
        h.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        h.setOrientation(LinearLayout.HORIZONTAL);
        //labels
        TextView brewed = new TextView(this);
        brewed.setText("brewed from: ");
        TextView to = new TextView(this);
        to.setText("to: ");
        //start date
        EditText start = new EditText(this);
        start.setHint("mm/yyyy");
        start.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                state[1] = s.toString();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        start.setSingleLine();
        //end date
        EditText _end = new EditText(this);
        _end.setHint("mm/yyyy");
        _end.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                state[2] = s.toString();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        _end.setSingleLine();
        //add in sequential order
        h.addView(brewed);
        h.addView(start);
        h.addView(to);
        h.addView(_end);
        //add to main layout
        L.addView(h);
    }

    public void addRadio(LinearLayout L, String[] state){
        LinearLayout h = new LinearLayout(this);
        h.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        h.setOrientation(LinearLayout.HORIZONTAL);
        //create label
        TextView high = new TextView(this);
        high.setText("high point (ABV >= 4.0)");
        //create radio button
        RadioGroup radioGrp = new RadioGroup(this);
        RadioButton rb = new RadioButton(this);
        rb.setId(0);
        radioGrp.addView(rb);
        radioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedRadioButtonId = radioGrp.getCheckedRadioButtonId();
                RadioButton radioBtn = (RadioButton) findViewById(checkedRadioButtonId);
                state[3] = "True";
            }
        });
        //add elements
        h.addView(high);
        h.addView(radioGrp);
        L.addView(h);
    }

    public void addGo(String[] state){
        Button b = findViewById(R.id.confirm_button);
        b.setText("get results");
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(state.length > 0 && checkInputs(state)) {
                    nextActivity(state);
                }
                else{
                    showToast();
                }
            }
        });
    }

    public void showToast(){
        String msg = "Please ensure date inputs are formatted mm/yyyy";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, msg, duration);
        toast.show();
    }


    public boolean checkInputs(String[] arr){
        String after = arr[1];
        String before = arr[2];
        if(!checkForm1(after) || !checkForm1(before)) return false;
        if(!checkForm2(after) || !checkForm2(before)) return false;
        return checkForm3(after) && checkForm3(before);
    }

    public boolean checkForm1(String after){
        Log.d("string", after);
        if(after == null || after.length() == 0) return true;
        if(after != null && after.length() > 0 && after.length() != 7) {
            Log.d("here", "173");
            return false;
        }
        return true;
    }

    public boolean checkForm2(String after){
        Log.d("string", after);
        if(after == null || after.length() == 0) return true;
        if(after != null && after.length() > 0 && !after.contains("/")){
            Log.d("here", "181");
            return false;
        }
        return true;
    }

    public boolean checkForm3(String after){
        if(after == null || after.length() == 0) return true;
        String[] sp1 = after.split("/");
        if(sp1[0].length() != 2 || sp1[1].length() != 4) return false;
        return isNum(sp1[0]) && isNum(sp1[1]);
    }

    public boolean isNum(String s){
        Log.d("stirng", s);
        if(s == null || s.length() == 0) return true;
        for(int i=0; i<s.length(); i++){
            if(!Character.isDigit(s.charAt(i))) return false;
        }
        return true;
    }

    public void nextActivity(String[] state){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = buildUrl("https://api.punkapi.com/v2/beers", state);
        JsonArrayRequest Req = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        String res = response.toString();
                        Intent i = new Intent(getApplicationContext(), ThirdActivity.class);
                        i.putExtra("results", res);
                        startActivity(i);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.toString());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(Req);
    }

    public String fixDate(String s){
        return s.replaceAll("/", "-");
    }

    public String buildUrl(String base, String[] state){
        if(state.length == 0 ) return base;
        base += "?";
        if(state[0]!= null && state[0].length() > 0){
            base += "beer_name="+state[0]+"&";
        }
        if(state[1]!= null && state[1].length() > 0){
            base += "brewed_after="+fixDate(state[1])+"&";
        }
        if(state[2]!= null && state[2].length() > 0){
            base += "brewed_before="+fixDate(state[2])+"&";
        }
        if(state[3]!= null && state[3].length() > 0){
            base += "abv_gt=3.9"+"&";
        }
        Log.d("url", base);
        return base;
    }
}