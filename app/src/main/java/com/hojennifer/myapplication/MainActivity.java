package com.hojennifer.myapplication;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    EditText query;
    Button searchButton;
    RadioGroup filterRadio;
    RadioButton selectedFilter;
    TextView topResult;
    TextView linkTV;
    WebView displayVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        query = findViewById(R.id.queryET);
        searchButton = findViewById(R.id.searchButton);
        filterRadio = findViewById(R.id.rg);
        topResult = findViewById(R.id.topResult);
        linkTV = findViewById(R.id.blank);
        displayVideo = findViewById(R.id.webView);
    }

    public void searchVideos(View view) {
        int selectedId = filterRadio.getCheckedRadioButtonId();

        selectedFilter = findViewById(selectedId);
        String sortingBy = selectedFilter.getText().toString();
        String term = query.getText().toString();

        Log.i("selected id", String.valueOf(selectedId));
        String clickLink = "";
        if(sortingBy.equals("Relevance")){ //don't filter
            Log.i("selected filter", sortingBy);
            String filtRelevance = "https://youtube.com/results?search_query=" + query.getText().toString();
            clickLink = " <a href=" + filtRelevance + ">YouTube videos on \"" + term + "\" - sorted by relevance</a>";

        }
        else if(sortingBy.equals("Upload date")){ //either relevance, upload date, view count
            Log.i("selected filter", sortingBy);
            String filtUpload = "https://youtube.com/results?search_query=" + query.getText().toString() + "&sp=CAI%253D";
            clickLink = " <a href=" + filtUpload + ">YouTube videos on \"" + term + "\" - sorted by upload date</a>";

        }
        else{
            Log.i("selected filter", sortingBy);
            String filtViews = "https://youtube.com/results?search_query=" + query.getText().toString() + "&sp=CAMSAhAB";
            clickLink = " <a href=" + filtViews + ">YouTube videos on \"" + term + "\" - sorted by view count</a>";
        }
        linkTV.setMovementMethod(LinkMovementMethod.getInstance());
        //linkTV.setText(Html.fromHtml(clickLink));
        linkTV.setTextColor(getResources().getColor(R.color.defaultGray));
        //if (linkTV.getVisibility() == View.VISIBLE)
           // linkTV.setVisibility(View.INVISIBLE);
        //else
            topResult.setVisibility(View.VISIBLE);
            linkTV.setVisibility(View.VISIBLE);

            double height = displayVideo.getHeight() / 2.623;
            Log.i("webview Height", String.valueOf(height)) ;
            //int adjustedHeight = (int)height;
            double width = displayVideo.getWidth() / 2.623;
            Log.i("webview Width", String.valueOf(width));
        String frameVideo = "<html><body><br> <iframe width=" + (int)width+" height="+(int)height+" src=\"https://www.youtube.com/embed/5LMRbAiRkdY\" frameborder=\"0\" allowfullscreen></iframe></body></html>";


        displayVideo.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings webSettings = displayVideo.getSettings();
        webSettings.setJavaScriptEnabled(true);
        displayVideo.loadData(frameVideo, "text/html", "utf-8");
        displayVideo.setVisibility(View.VISIBLE);



    }
}
