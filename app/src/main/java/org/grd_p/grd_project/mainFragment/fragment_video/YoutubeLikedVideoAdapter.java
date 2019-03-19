package org.grd_p.grd_project.mainFragment.fragment_video;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import org.grd_p.grd_project.Authorization;
import org.grd_p.grd_project.R;

import java.util.ArrayList;

public class YoutubeLikedVideoAdapter extends RecyclerView.Adapter<YoutubeLikedVideoAdapter.YoutubeViewHolder>{
    private Context context;
    private likedOnClickListener onClickListener;
    private ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList;

    public class YoutubeViewHolder extends RecyclerView.ViewHolder{
        public YouTubeThumbnailView videoThumbnail;
        public CardView youtubeCardView;
        public TextView videoTitle, viewNum, videoPostedTime;
        public Button likeButton;


        public YoutubeViewHolder(View itemView) {
            super(itemView);
            videoThumbnail = itemView.findViewById(R.id.video_thumbnail);
            youtubeCardView = itemView.findViewById(R.id.youtube_card_view);
            videoTitle = itemView.findViewById(R.id.video_title);
            viewNum = itemView.findViewById(R.id.video_viewNum);
            videoPostedTime = itemView.findViewById(R.id.videoPostedTime);
            likeButton = itemView.findViewById(R.id.video_likeButton);

            //likeButton.setOnClickListener(this);
        }
    }

    public void setOnClickListener(likedOnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public YoutubeLikedVideoAdapter(Context context, ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList) {
        this.context = context;
        this.youtubeVideoModelArrayList = youtubeVideoModelArrayList;
    }
    //보여질 카드 레이아웃 xml 가져오는 역할
    public YoutubeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view  = layoutInflater.inflate(R.layout.video_recyler_items,parent,false);
        return new YoutubeViewHolder(view);
    }
    @Override
    //display the data at the specified position (카드 레이아웃 내 데이터 값 설정)
    public void onBindViewHolder(@NonNull final YoutubeViewHolder holder, final int i) {
        final YoutubeVideoModel youtubeVideoModel = youtubeVideoModelArrayList.get(i);

        holder.videoTitle.setText(youtubeVideoModel.getTitle());
        //비디오 title에 대한 click event
        holder.videoTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context,youtubePlayerActivity.class)
                        .putExtra("video_id",youtubeVideoModel.getVideoID()));
            }
        });
        holder.viewNum.setText(youtubeVideoModel.getViewNum());
        holder.videoPostedTime.setText(youtubeVideoModel.getPostedTime());
        holder.likeButton.setPressed(true); //좋아요 버튼 눌린 상태
        holder.likeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onClickListener.onLikeButtonListener(view,i);
            }
        });

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
                    }
                });
            }

            @Override
            //초기화 실패하는 경우
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
            }
        });
        //비디오 썸네일(이미지)에 대한 click event
        holder.videoThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context,youtubePlayerActivity.class)
                        .putExtra("video_id",youtubeVideoModel.getVideoID()));
            }
        });
    }
    @Override
    public int getItemCount() {
        return youtubeVideoModelArrayList !=null ? youtubeVideoModelArrayList.size():0;
    }

    public interface likedOnClickListener {
        void onLikeButtonListener(View v, int position);
    }

    public void addVideo(YoutubeVideoModel video) {
        Log.d("DBGLOG","likedAdapter_addVideo: "+video.getVideoID()+", "+video.getTitle());
        youtubeVideoModelArrayList.add(0,video);
    }

    public void removeVideo(String videoID){
        Log.d("DBGLOG","likedAdapter_removeVideo: "+videoID);
        YoutubeVideoModel youtubeVideoModel;
        for(int i=0;i<youtubeVideoModelArrayList.size();i++){
            youtubeVideoModel = youtubeVideoModelArrayList.get(i);
            if (youtubeVideoModel.getVideoID().equals(videoID)){
                youtubeVideoModelArrayList.remove(i);
                break;
            }
        }
    }




}
