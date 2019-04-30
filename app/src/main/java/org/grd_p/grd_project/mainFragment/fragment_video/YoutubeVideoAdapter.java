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


public class YoutubeVideoAdapter extends RecyclerView.Adapter<YoutubeVideoAdapter.YoutubeViewHolder> {
  private static final String TAG = "DBGLOG "+YoutubeVideoAdapter.class.getSimpleName();
  private Context context;
  private OnClickListener onClickListener;
  private final ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList;

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

  public YoutubeVideoAdapter(Context context, ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList) {
    this.context = context;
    this.youtubeVideoModelArrayList = youtubeVideoModelArrayList;
  }
  public void setOnClickListener(OnClickListener onClickListener){
    this.onClickListener = onClickListener;
  }


  @NonNull
  @Override
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
    if(youtubeVideoModel.isLiked()==0) //아직 좋아요 하지 않은 경우
      holder.likeButton.setPressed(false);
    else //이미 좋아요 한 경우
      holder.likeButton.setPressed(true);
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

  public interface OnClickListener {
    void onLikeButtonListener(View v, int position);
  }

  public int findIndex(String videoID){
    YoutubeVideoModel youtubeVideoModel;
    for(int i=0;i<youtubeVideoModelArrayList.size();i++){
      youtubeVideoModel = youtubeVideoModelArrayList.get(i);
      if (youtubeVideoModel.getVideoID().equals(videoID)){
        return i;
      }
    }
    return 0;
  }

  public void setUnliked(int index){
    youtubeVideoModelArrayList.get(index).setLiked(0);
  }

}