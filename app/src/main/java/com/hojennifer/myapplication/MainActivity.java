package com.hojennifer.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    EditText query;
    Button searchButton;
    //RadioGroup filterRadio;
    //RadioButton selectedFilter;
    TextView topResult;
    TextView linkTV;
    WebView displayVideo;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        query = findViewById(R.id.queryET);
        searchButton = findViewById(R.id.searchButton);
        //filterRadio = findViewById(R.id.rg);
        topResult = findViewById(R.id.topResult);
        linkTV = findViewById(R.id.blank);
        displayVideo = findViewById(R.id.webView);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPrefs.edit();
        editor.putInt("Relevance", 0);
        editor.putInt("Upload date", 0);
        editor.putInt("View count", 0);
        editor.apply();
    }
    private List<SearchResult> loadVideo(String queryTerm) {
        try {
            // This object is used to make YouTube Data API requests. The last
            // argument is required, but since we don't need anything
            // initialized when the HttpRequest is initialized, we override
            // the interface and provide a no-op function.
            YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("You Tube Search Player").build();//app name string

            // Define the API request for retrieving search results.
            YouTube.Search.List search = youtube.search().list("id,snippet");

            // Set your developer key from the Google Developers Console for
            // non-authenticated requests. See:
            // https://console.developers.google.com/
            //search.setKey(Config.YOUTUBE_API_KEY);
            search.setKey("AIzaSyAwrXtqcPC3JqQU5wHzkKtd62S6wNkiXBY");
            search.setQ(queryTerm);

            // Restrict the search results to only include videos. See:
            // https://developers.google.com/youtube/v3/docs/search/list#type
            search.setType("video");

            // To increase efficiency, only retrieve the fields that the
            // application uses.
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/description,snippet/thumbnails/default/url,snippet/thumbnails/medium/url)");
            search.setMaxResults((long)1);

            // Call the API and print results.
            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            if (searchResultList != null) {
                return searchResultList;
            }
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }


        return null;

    }

    public void searchVideos(View view) {
        //int selectedId = filterRadio.getCheckedRadioButtonId();

        //selectedFilter = findViewById(selectedId);
        //String sortingBy = selectedFilter.getText().toString();
        //String term = query.getText().toString();

        /*Log.i("selected id", String.valueOf(selectedId));
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
*/

        List<SearchResult> videoResult =  loadVideo(query.getText().toString());
        String videoID; //FNKjIcHHRw4
        if(videoResult != null && videoResult.size() > 0) {
            videoID = videoResult.get(0).getId().getVideoId();


            linkTV.setMovementMethod(LinkMovementMethod.getInstance());
            //linkTV.setText(Html.fromHtml(clickLink));
            linkTV.setTextColor(getResources().getColor(R.color.defaultGray));
            //if (linkTV.getVisibility() == View.VISIBLE)
            // linkTV.setVisibility(View.INVISIBLE);
            //else
            topResult.setVisibility(View.VISIBLE);
            linkTV.setVisibility(View.VISIBLE);

            double height = displayVideo.getHeight() / 2.623;
            Log.i("webview Height", String.valueOf(height));
            //int adjustedHeight = (int)height;
            double width = displayVideo.getWidth() / 2.623;
            Log.i("webview Width", String.valueOf(width));
            String frameVideo = "<html><body><br> <iframe width=" + ((int) width-1) + " height=" + ((int) height-1) + " src=\"https://www.youtube.com/embed/" + videoID + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";


            displayVideo.setWebViewClient(new WebViewClient() {
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
        else{
            Context context = getApplicationContext();
            CharSequence text = "Sorry, \"" + query.getText().toString() + "\" did not yield any results. Try different keywords.";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        //PRACTICE USING GSON
        Snake danger = new Snake();
        danger.species = "inland taipan";
        danger.toxicity = "venomous";
        Snake safe = new Snake();
        safe.species = "garter";
        safe.toxicity = "nonvenomous";

        Gson gson = new Gson();
        String json1 = gson.toJson(danger);
        String json2 = gson.toJson(safe);
        Log.i("danger GSON", json1);
        Log.i("safe GSON", json2);
        //
    }

    public class Snake{
        public String species = null;
        public String toxicity = null;
    }
    
}
