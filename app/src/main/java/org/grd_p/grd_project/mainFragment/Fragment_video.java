package org.grd_p.grd_project.mainFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import org.grd_p.grd_project.R;
import org.grd_p.grd_project.youtubePlayerActivity;

import java.util.ArrayList;
import java.util.Collections;

public class Fragment_video extends Fragment {
    private GestureDetector gestureDetector;
    private RecyclerView buttonView;
    private HorizontalAdapter adapter;
    private ArrayList<String> tagArray;
    private LinearLayoutManager layoutManager;

    private RecyclerView videoView;
    private ArrayList<String> youtubeVideoArray;
    private YouTubePlayer youTubePlayer;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_video,container,false);

        gestureDetector = new GestureDetector(getActivity(),new GestureDetector.SimpleOnGestureListener(){
           //누르고 뗄 때 한번만 인식하도록 하기 위해서
            public boolean onSingleTapUp(MotionEvent e){
               return true;
           }
        });

        buttonView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        buttonView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        buttonView.setLayoutManager(layoutManager);

        //태그부분
        //인터넷 연결될 경우 업데이트, 아니면 기존 정보로 띄우도록 하는 부분 필요
        generateTagList();
        adapter = new HorizontalAdapter(tagArray);
        buttonView.setAdapter(adapter);

        //비디오 리스트 부분
        setUpRecyclerView(rootView);
        populateRecyclerView(rootView);
        return rootView;
    }

    private void setUpRecyclerView(View rootView){
        videoView = (RecyclerView)rootView.findViewById(R.id.recycler_view_video);
        videoView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        videoView.setLayoutManager(linearLayoutManager);
    }

    private void populateRecyclerView(final View rootView){
        final ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList = generateVideoList();
        YoutubeVideoAdapter adapter = new YoutubeVideoAdapter(getActivity(),youtubeVideoModelArrayList);
        videoView.setAdapter(adapter);

        videoView.addOnItemTouchListener(new RecyclerViewOnClickListener(rootView.getContext(), new RecyclerViewOnClickListener.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                startActivity(new Intent(rootView.getContext(), youtubePlayerActivity.class)
                .putExtra("video_id",youtubeVideoModelArrayList.get(position).getVideoID()));
            }
        }));

    }

    private ArrayList<YoutubeVideoModel> generateVideoList(){ //화면에 띄울 비디오 정보 삽입하는 부분
        ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList = new ArrayList<>();

        String[] videoIDArray = getResources().getStringArray(R.array.video_id_array);
        String[] videoTitleArray =getResources().getStringArray(R.array.video_title_array);
        String[] videoViewNumArray = getResources().getStringArray(R.array.video_viewNum_array);
        String[] videoPostedTimeArray = getResources().getStringArray(R.array.video_postedTime_array);

        for(int i=0;i<videoIDArray.length;i++){
            YoutubeVideoModel model = new YoutubeVideoModel();
            model.setVideoID(videoIDArray[i]);
            model.setTitle(videoTitleArray[i]);
            model.setViewNum(videoViewNumArray[i]);
            model.setPostedTime(videoPostedTimeArray[i]);

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
