package com.silverorange.videoplayer.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Log;
import com.silverorange.videoplayer.Model.DataModel;
import com.silverorange.videoplayer.R;
import com.silverorange.videoplayer.RetrofitApi.GetDataService;
import com.silverorange.videoplayer.RetrofitApi.RetrofitClientInstance;
import com.silverorange.videoplayer.Utility.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import io.noties.markwon.Markwon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    // Init variable and assign values
    Activity activity;
    ArrayList<DataModel> videoList = new ArrayList<>();

    PlayerView playerView;
    ExoPlayer player;

    RelativeLayout controller;
    ImageButton play, pause, prev, next;
    int currentPlayingPos = 0;

    TextView txtTitle, txtDesc, txtAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

        // Bind component
        playerView = findViewById(R.id.playerView);

        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        controller = findViewById(R.id.controller);

        txtTitle = findViewById(R.id.txtTitle);
        txtDesc = findViewById(R.id.txtDesc);
        txtAuthor = findViewById(R.id.txtAuthor);

        // Init Exoplayer
        player = new ExoPlayer.Builder(activity).build();
        playerView.setPlayer(player);

        // Check internet and api call
        if (Utils.isNetworkAvailable(activity, true, false)) {
            getVideoData();
        }

        // Click event pf prev button
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If first position is playing, user can not go to previous
                if(currentPlayingPos != 0){
                    currentPlayingPos--;
                    initializePlayer(currentPlayingPos);
                }
                hideController();
            }
        });

        // Click event pf next button
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If last position is playing, user can not go to next
                if(currentPlayingPos != videoList.size()-1){
                    currentPlayingPos++;
                    initializePlayer(currentPlayingPos);
                }
                hideController();
            }
        });

        // Click event pf play button
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.play();
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                hideController();
            }
        });

        // Click event pf pause button
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.pause();
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
                hideController();
            }
        });

        playerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.setVisibility(View.VISIBLE);
                hideController();
            }
        });

    }

    // Hide controller after 5 seconds of displaying controller
    void hideController(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                controller.setVisibility(View.GONE);
            }
        }, 5000);
    }

    private void getVideoData() {

        // Api calling using retrofit and parse data

        ShowProgressDialog(activity, getResources().getString(R.string.loading));

        GetDataService apiService = RetrofitClientInstance.ApiClient().create(GetDataService.class);
        Call<ArrayList<DataModel>> call = apiService.getVideosData();

        call.enqueue(new Callback<ArrayList<DataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<DataModel>> call, Response<ArrayList<DataModel>> response) {
                hideProgressDialog();
                if (response.isSuccessful()) {

                    videoList = response.body();

                    // Sort the received list of videos by date.
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    Collections.sort(videoList, new Comparator<DataModel>() {
                        public int compare(DataModel o1, DataModel o2) {
                            try {
                                Date a = format.parse(o1.getPublishedAt());
                                Date b = format.parse(o2.getPublishedAt());
                                if (a == null || b == null)
                                    return 0;
                                return a.compareTo(b);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return 0;
                            }
                        }
                    });

                    // Load the first video into the UI by default.
                    initializePlayer(currentPlayingPos);
                } else {
                    Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.something_went_wrong_please_try_again));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DataModel>> call, Throwable t) {
                hideProgressDialog();
                Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.something_went_wrong_please_try_again));
            }
        });

    }

    private void initializePlayer(int pos) {

        //In the details section, show the returned description for the current video as rendered Markdown.
        Markwon title = Markwon.create(activity);
        title.setMarkdown(txtTitle, videoList.get(pos).getTitle());
        Markwon desc = Markwon.create(activity);
        desc.setMarkdown(txtDesc, videoList.get(pos).getDescription());
        txtAuthor.setText(videoList.get(pos).getAuthor().getName());

        MediaItem mediaItem = MediaItem.fromUri(videoList.get(pos).getFullURL());
        player.setMediaItem(mediaItem);
        player.prepare();
    }

}