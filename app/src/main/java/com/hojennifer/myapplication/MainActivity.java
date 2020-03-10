package com.hojennifer.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    EditText query;
    Button searchButton;
    RadioGroup filterRadio;
    RadioButton selectedFilter;
    TextView topResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        query = findViewById(R.id.queryET);
        searchButton = findViewById(R.id.searchButton);
        filterRadio = findViewById(R.id.rg);
        topResult = findViewById(R.id.topResult);
    }

    public void searchVideos(View view) {
        int selectedId = filterRadio.getCheckedRadioButtonId();
        Log.i("selected id", String.valueOf(selectedId));

        if(selectedId == -1){ //don't filter
            String ytNoFilt = "https://youtube.com/results?search_query=" + query.getText().toString();
            String clickLink = " <a href=" + ytNoFilt + ">Your YouTube link!</a>";
            topResult.setMovementMethod(LinkMovementMethod.getInstance());
            topResult.setText(Html.fromHtml(clickLink));
        }
        else{ //either relevance, upload date, view count
            selectedFilter = findViewById(selectedId);
            Log.i("selected filter", selectedFilter.getText().toString());

        }






    }
}
