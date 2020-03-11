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
    TextView linkTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        query = findViewById(R.id.queryET);
        searchButton = findViewById(R.id.searchButton);
        filterRadio = findViewById(R.id.rg);
        topResult = findViewById(R.id.topResult);
        linkTV = findViewById(R.id.blank);
    }

    public void searchVideos(View view) {
        int selectedId = filterRadio.getCheckedRadioButtonId();
        selectedFilter = findViewById(selectedId);
        String sortingBy = selectedFilter.getText().toString();
        Log.i("selected id", String.valueOf(selectedId));
        String clickLink = "";
        if(selectedId == -1 || sortingBy.equals("Relevance")){ //don't filter
            Log.i("selected filter", sortingBy);
            String filtRelevance = "https://youtube.com/results?search_query=" + query.getText().toString();
            clickLink = " <a href=" + filtRelevance + ">YouTube videos - sorted by relevance</a>";

        }
        else if(sortingBy.equals("Upload date")){ //either relevance, upload date, view count
            Log.i("selected filter", sortingBy);
            String filtUpload = "https://youtube.com/results?search_query=" + query.getText().toString() + "&sp=CAI%253D";
            clickLink = " <a href=" + filtUpload + ">YouTube videos - sorted by upload date</a>";

        }
        else{
            Log.i("selected filter", sortingBy);
            String filtViews = "https://youtube.com/results?search_query=" + query.getText().toString() + "&sp=CAMSAhAB";
            clickLink = " <a href=" + filtViews + ">YouTube videos - sorted by view count</a>";
        }
        linkTV.setMovementMethod(LinkMovementMethod.getInstance());
        linkTV.setText(Html.fromHtml(clickLink));
        linkTV.setTextColor(getResources().getColor(R.color.defaultGray));
        //if (linkTV.getVisibility() == View.VISIBLE)
           // linkTV.setVisibility(View.INVISIBLE);
        //else
            topResult.setVisibility(View.VISIBLE);
            linkTV.setVisibility(View.VISIBLE);






    }
}
