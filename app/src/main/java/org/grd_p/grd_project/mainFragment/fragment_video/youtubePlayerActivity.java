package org.grd_p.grd_project.mainFragment.fragment_video;

import android.os.Bundle;
import android.util.Log;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.grd_p.grd_project.Authorization;
import org.grd_p.grd_project.R;

public class youtubePlayerActivity extends YouTubeBaseActivity {
    private static final String TAG = "DBGLOG "+youtubePlayerActivity.class.getSimpleName();
    private String videoID;
    private YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_youtubeplayer);
        videoID = getIntent().getStringExtra("video_id");
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        initializeYoutubePlayer();
    }

    private void initializeYoutubePlayer(){
        youTubePlayerView.initialize(Authorization.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if(!b){
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

                    youTubePlayer.loadVideo(videoID);
                    youTubePlayer.setFullscreen(true);
                    youTubePlayer.play();
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.e(TAG,"Youtube Player View initialization failed");
            }
        });
    }
}
