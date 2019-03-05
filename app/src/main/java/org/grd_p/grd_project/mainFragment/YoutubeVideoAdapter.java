package org.grd_p.grd_project.mainFragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import org.grd_p.grd_project.Authorization;
import org.grd_p.grd_project.R;

import java.util.ArrayList;

public class YoutubeVideoAdapter extends RecyclerView.Adapter<YoutubeViewHolder> {
    private static final String TAG = "DBGLOG "+YoutubeVideoAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList;

    public YoutubeVideoAdapter(Context context, ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList) {
        this.context = context;
        this.youtubeVideoModelArrayList = youtubeVideoModelArrayList;
    }

    @NonNull
    @Override
    public YoutubeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view  = layoutInflater.inflate(R.layout.video_recyler_items,parent,false);
        return new YoutubeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YoutubeViewHolder holder, int i) {
        final YoutubeVideoModel youtubeVideoModel = youtubeVideoModelArrayList.get(i);

        holder.videoTitle.setText(youtubeVideoModel.getTitle());
        holder.viewNum.setText(youtubeVideoModel.getViewNum());
        holder.videoPostedTime.setText(youtubeVideoModel.getPostedTime());

        holder.videoThumbnail.initialize(Authorization.DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            //초기화 성공적인 경우
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(youtubeVideoModel.getVideoID());

                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    //썸네일 로드 성공적인 경우
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        youTubeThumbnailLoader.release();
                    }

                    @Override
                    //썸네일 로드 실패하는 경우
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                        Log.e(TAG,"Youtube Thumbnail Error");
                    }
                });
            }

            @Override
            //초기화 실패하는 경우
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                Log.e(TAG,"Youtube Initialization Failure");
            }
        });
    }

    @Override
    public int getItemCount() {
        return youtubeVideoModelArrayList !=null ? youtubeVideoModelArrayList.size():0;
    }
}

class YoutubeViewHolder extends RecyclerView.ViewHolder{
    public YouTubeThumbnailView videoThumbnail;
    public CardView youtubeCardView;
    public TextView videoTitle, viewNum, videoPostedTime;

    public YoutubeViewHolder(View itemView) {
        super(itemView);
        videoThumbnail = itemView.findViewById(R.id.video_thumbnail);
        youtubeCardView = itemView.findViewById(R.id.youtube_card_view);
        videoTitle = itemView.findViewById(R.id.video_title);
        viewNum = itemView.findViewById(R.id.video_viewNum);
        videoPostedTime = itemView.findViewById(R.id.videoPostedTime);

    }
}