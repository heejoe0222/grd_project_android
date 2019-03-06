package org.grd_p.grd_project.mainFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubeThumbnailView;

import org.grd_p.grd_project.R;
import org.grd_p.grd_project.sharedPreference;
import org.grd_p.grd_project.youtubePlayerActivity;

import java.util.ArrayList;
import java.util.Collections;

public class Fragment_video extends Fragment {
    private static final String LIST_VideoID_JSON ="Lists_VideoID";
    private static final String LIST_VideoLIKED_JSON ="Lists_VideoLiked";
    private static final String LIST_VideoTITLE_JSON ="Lists_VideoTitle";
    private static final String LIST_VideoVIEWNUM_JSON ="Lists_VideoViewNum";
    private static final String LIST_VideoPOSTEDTIME_JSON ="Lists_VideoPostedTime";

    private RecyclerView buttonView;
    private HorizontalAdapter adapter;
    private ArrayList<String> tagArray;
    private LinearLayoutManager layoutManager;

    private RecyclerView videoView;
    private com.google.android.youtube.player.YouTubeThumbnailView thumbnailView;
    private TextView videoTitle;
    private ArrayList<String> video_id_array = new ArrayList<>();
    private ArrayList<Integer> video_liked_array = new ArrayList<>();
    private ArrayList<String> video_title_array = new ArrayList<>();
    private ArrayList<String> video_viewNum_array = new ArrayList<>();
    private ArrayList<String> video_postedTime_array = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_video,container,false);

        new GestureDetector(getActivity(),new GestureDetector.SimpleOnGestureListener(){
           //누르고 뗄 때 한번만 인식하도록 하기 위해서
            public boolean onSingleTapUp(MotionEvent e){
               return true;
           }
        });

        buttonView = rootView.findViewById(R.id.recycler_view);
        buttonView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        buttonView.setLayoutManager(layoutManager);

        //태그부분
        //인터넷 연결될 경우 업데이트, 아니면 기존 정보로 띄우도록 하는 부분 필요
        generateTagList();
        adapter = new HorizontalAdapter(tagArray);
        buttonView.setAdapter(adapter);

        //비디오 리스트 부분
        setVideoInfoFromDevice();
        setUpRecyclerView(rootView);
        populateRecyclerView();
        return rootView;
    }

    private void setVideoInfoFromDevice(){
        //sharedPreference 말고 db로 저장하도록 바꿔야
        video_id_array = sharedPreference.getStringArrayPref(getContext(), LIST_VideoID_JSON);
        video_liked_array = sharedPreference.getIntArrayPref(getContext(), LIST_VideoLIKED_JSON);
        video_title_array = sharedPreference.getStringArrayPref(getContext(),LIST_VideoTITLE_JSON);
        video_viewNum_array = sharedPreference.getStringArrayPref(getContext(),LIST_VideoVIEWNUM_JSON);
        video_postedTime_array = sharedPreference.getStringArrayPref(getContext(),LIST_VideoPOSTEDTIME_JSON);

        //기존에 디바이스에 저장된 정보 없을 경우 string.xml에 미리 세팅된 값 가져와서 띄움
        if((video_id_array.isEmpty()) && (video_liked_array.isEmpty()) && (video_title_array.isEmpty())
                && (video_viewNum_array.isEmpty()) && (video_postedTime_array.isEmpty())){

            String[] videoIDArray = getResources().getStringArray(R.array.video_id_array);
            String[] videoTitleArray =getResources().getStringArray(R.array.video_title_array);
            String[] videoViewNumArray = getResources().getStringArray(R.array.video_viewNum_array);
            String[] videoPostedTimeArray = getResources().getStringArray(R.array.video_postedTime_array);
            int[] videoLikedArray = getResources().getIntArray(R.array.video_liked_array);

            for(int i=0;i<videoIDArray.length;i++) {
                video_id_array.add(videoIDArray[i]);
                video_title_array.add(videoTitleArray[i]);
                video_viewNum_array.add(videoViewNumArray[i]);
                video_postedTime_array.add(videoPostedTimeArray[i]);
                video_liked_array.add(videoLikedArray[i]);
            }
        }
    }

    private void setUpRecyclerView(View rootView){
        videoView = rootView.findViewById(R.id.recycler_view_video);
        videoView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        videoView.setLayoutManager(linearLayoutManager);
    }

    private void populateRecyclerView(){
        final ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList = generateVideoList();
        YoutubeVideoAdapter adapter = new YoutubeVideoAdapter(getActivity(), youtubeVideoModelArrayList, new OnClickListener() {
            @Override
            //이 부분 없어도 될듯 -> adapter-onBindViewHolder에서 처리했음
            public void onLikeButtonListener(View v, int i) {
                //좋아요 버튼 클릭하면 이 영상에 대한 liked 변수 값 반전 되도록(1->0/0->1)
                //위의 내용을 장치와 서버에 업데이트
                //좋아요 영상 재생목록에 추가되어야
            }
        });
        videoView.setAdapter(adapter);
        /*
        videoView.addOnItemTouchListener(new RecyclerViewOnClickListener(rootView.getContext(), new RecyclerViewOnClickListener.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                startActivity(new Intent(rootView.getContext(), youtubePlayerActivity.class)
                .putExtra("video_id",youtubeVideoModelArrayList.get(position).getVideoID()));
            }
        })); */

    }

    private ArrayList<YoutubeVideoModel> generateVideoList(){ //화면에 띄울 비디오 정보 삽입하는 부분
        ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList = new ArrayList<>();
        /*
        String[] videoIDArray = getResources().getStringArray(R.array.video_id_array);
        String[] videoTitleArray =getResources().getStringArray(R.array.video_title_array);
        String[] videoViewNumArray = getResources().getStringArray(R.array.video_viewNum_array);
        String[] videoPostedTimeArray = getResources().getStringArray(R.array.video_postedTime_array);
        int[] videoLikedArray = getResources().getIntArray(R.array.video_liked_array);

        for(int i=0;i<videoIDArray.length;i++){
            YoutubeVideoModel model = new YoutubeVideoModel();
            model.setVideoID(videoIDArray[i]);
            model.setTitle(videoTitleArray[i]);
            model.setViewNum(videoViewNumArray[i]);
            model.setPostedTime(videoPostedTimeArray[i]);
            model.setLiked(videoLikedArray[i]);

            youtubeVideoModelArrayList.add(model);
        }
        */
        for(int i=0;i<video_id_array.size();i++){
            YoutubeVideoModel model = new YoutubeVideoModel();
            model.setVideoID(video_id_array.get(i));
            model.setTitle(video_title_array.get(i));
            model.setViewNum(video_viewNum_array.get(i));
            model.setPostedTime(video_postedTime_array.get(i));
            model.setLiked(video_liked_array.get(i));

            youtubeVideoModelArrayList.add(model);
        }

        return youtubeVideoModelArrayList;
    }

    private void generateTagList(){
        tagArray = new ArrayList<>();

        String[] tagStrList = {"목 꺾임","눕는 자세","기대는 자세","굽은 어깨"}; //화면에 띄울 태그 리스트

        for(int i=0;i<tagStrList.length;i++){
            tagStrList[i] = "# ".concat(tagStrList[i]);
        }

        Collections.addAll(tagArray,tagStrList);
    }

    class HorizontalAdapter extends RecyclerView.Adapter<HorizontalViewHolder>{
        private ArrayList<String> ButtonDatas;

        public  HorizontalAdapter(ArrayList<String> tagArrayList){
            ButtonDatas = tagArrayList;
        }
        public void setData(ArrayList<String> list){
            ButtonDatas = list;
        }
        public HorizontalViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizon_recycler_items,parent,false);
            HorizontalViewHolder holder = new HorizontalViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull HorizontalViewHolder holder, int position) {
            String data = ButtonDatas.get(position);
            holder.button.setText(data);
        }

        @Override
        public int getItemCount() {
            return ButtonDatas.size();
        }
    }

    class HorizontalViewHolder extends RecyclerView.ViewHolder{
        public Button button;
        public HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);

            button = (Button)itemView.findViewById(R.id.tag_button);
        }
    }


}
