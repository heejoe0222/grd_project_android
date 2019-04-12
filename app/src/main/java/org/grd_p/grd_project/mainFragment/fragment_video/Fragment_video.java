package org.grd_p.grd_project.mainFragment.fragment_video;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.grd_p.grd_project.NetworkStatus;
import org.grd_p.grd_project.R;
import org.grd_p.grd_project.sharedPreference;

import java.util.ArrayList;
import java.util.Collections;

public class Fragment_video extends Fragment {
    private RecyclerView buttonView;
    private HorizontalAdapter adapter;
    private ArrayList<String> tagArray;
    private LinearLayoutManager layoutManager;

    private RecyclerView videoView;
    private YoutubeVideoAdapter videoAdapter;
    //private ArrayList<YoutubeVideoModel> video_array 형태로 받기
    private ArrayList<String> video_id_array = new ArrayList<>();
    private ArrayList<String> video_title_array = new ArrayList<>();
    private ArrayList<String> video_viewNum_array = new ArrayList<>();
    private ArrayList<String> video_postedTime_array = new ArrayList<>();
    private ArrayList<Integer> video_liked_array = new ArrayList<>();

    private ArrayList<String> likeVideo_id_array = new ArrayList<>();
    private ArrayList<String> likeVideo_title_array = new ArrayList<>();
    private ArrayList<String> likeVideo_viewNum_array = new ArrayList<>();
    private ArrayList<String> likeVideo_postedTime_array = new ArrayList<>();
    private ArrayList<Integer> likeVideo_liked_array = new ArrayList<>();

    private RecyclerView videoLikeView;
    private YoutubeLikedVideoAdapter videoLikeAdapter;
    private ArrayList<YoutubeVideoModel> likeVideo_array = new ArrayList<>();

    SQLiteDatabase db;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_video,container,false);

        String user_id = getArguments().getString("user_id");

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
        setVideoInfo(); //장치나 서버에서 데이터 받아와  video_array 변수에 저장
        setUpRecyclerView(rootView);
        populateRecyclerView();

        //좋아요한 비디오 리스트 부분
        populateRecyclerView2();
        return rootView;
    }

    private void setVideoInfo(){
        DatabaseHelper helper = new DatabaseHelper(getContext(),"user_video",null,1);
        db = helper.getWritableDatabase();

        int status = NetworkStatus.getConnectivityStatus(getContext());

        //서버에서 받아옴
        if(status==NetworkStatus.TYPE_MOBILE || status==NetworkStatus.TYPE_WIFI){

        }else{ //기기장치에서 받아옴 (내부 DB)

            if(db!=null){
                //videoID, title, viewNum, postedTime, videoLike
                String sql = "select videoID, title, viewNum, postedTime, videoLike from video";
                Cursor cursor = db.rawQuery(sql,null);

                for(int i=0;i<cursor.getCount();i++){
                    cursor.moveToNext();
                    video_id_array.add(cursor.getString(0));
                    video_title_array.add(cursor.getString(1));
                    video_viewNum_array.add(cursor.getString(2));
                    video_postedTime_array.add(cursor.getString(3));
                    video_liked_array.add(cursor.getInt(4));
                }

                sql = "select videoID, title, viewNum, postedTime, videoLike from likeVideo";
                cursor = db.rawQuery(sql,null);

                for(int i=0;i<cursor.getCount();i++){
                    cursor.moveToNext();
                    likeVideo_id_array.add(cursor.getString(0));
                    likeVideo_title_array.add(cursor.getString(1));
                    likeVideo_viewNum_array.add(cursor.getString(2));
                    likeVideo_postedTime_array.add(cursor.getString(3));
                    likeVideo_liked_array.add(cursor.getInt(4));
                }

                cursor.close();
            }else{
                Log.d("DBGLOG","not exist db");
            }

        }

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

    class DatabaseHelper extends SQLiteOpenHelper{
        public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        //schema: (videoID text PRIMARY KEY, title text, viewNum text, postedTime text, videoLike integer)
        @Override
        public void onCreate(SQLiteDatabase db) {
            String name = "video";
            String sql = "create table if not exists "+name+" (videoID text PRIMARY KEY, title text, viewNum text, postedTime text, videoLike integer)";
            db.execSQL(sql);

            name = "likeVideo";
            sql = "create table if not exists "+name+" (videoID text PRIMARY KEY, title text, viewNum text, postedTime text, videoLike integer)";
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int old_v, int new_v) {
            if(new_v>1){
                String tableName ="video";
                db.execSQL("drop table if exists "+tableName);

                String sql = "create table if not exists "+tableName+" (videoID text PRIMARY KEY, title text, viewNum text, postedTime text, videoLike integer)";
                db.execSQL(sql);

                tableName ="likeVideo";
                db.execSQL("drop table if exists "+tableName);

                sql = "create table if not exists "+tableName+" (videoID text PRIMARY KEY, title text, viewNum text, postedTime text, videoLike integer)";
                db.execSQL(sql);
            }
        }
    }

    private void setUpRecyclerView(View rootView){
        videoView = rootView.findViewById(R.id.recycler_view_video);
        videoView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        videoView.setLayoutManager(linearLayoutManager);

        videoLikeView = rootView.findViewById(R.id.recycler_view_video_like);
        videoLikeView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity());
        videoLikeView.setLayoutManager(linearLayoutManager2);
    }


    private void populateRecyclerView() {
        final ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList = generateVideoList();
        videoAdapter = new YoutubeVideoAdapter(getActivity(), youtubeVideoModelArrayList);
        if (videoAdapter.getItemCount() > 2) { //리사이클러뷰에서 비디오 4개까지 보이도록
            ViewGroup.LayoutParams params = videoView.getLayoutParams();
            params.height = 800;
            videoView.setLayoutParams(params);
        }
        videoView.setAdapter(videoAdapter);
        videoAdapter.setOnClickListener(new YoutubeVideoAdapter.OnClickListener() {
            @Override
            public void onLikeButtonListener(View v, int position) {
                updateLikeVideoList(youtubeVideoModelArrayList, position);
            }
        });
    }

    private void updateLikeVideoList(ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList, int i){
        YoutubeVideoModel youtubeVideoModel = youtubeVideoModelArrayList.get(i);
        Button likeButton =  videoView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.video_likeButton);
        if(youtubeVideoModel.isLiked()==1){
            likeButton.setBackgroundResource(R.drawable.heart);
            //좋아요한 비디오 영상 리스트에서 해당 비디오 삭제되도록
            Log.d("DBGLOG","adapter_remove: "+youtubeVideoModel.getVideoID());
            videoLikeAdapter.removeVideo(youtubeVideoModel.getVideoID());
            videoLikeAdapter.notifyDataSetChanged();
            youtubeVideoModel.setLiked(0);
            // 비디오 db에서 videoLike 0 으로 값 update
            // 좋아요한 db에서 해당 비디오 delete
        }else{
            //좋아요한 비디오 영상 리스트에 해당 비디오 추가되도록
            likeButton.setBackgroundResource(R.drawable.selected_heart);
            Log.d("DBGLOG","adapter_add: "+youtubeVideoModel.getVideoID()+", "+youtubeVideoModel.getTitle());
            videoLikeAdapter.addVideo(youtubeVideoModel);
            videoLikeAdapter.notifyDataSetChanged();
            youtubeVideoModel.setLiked(1);
            //비디오 db에서 videoLike 1 으로 값 update
            //좋아요한 db에 해당 비디오 insert
        }
    }

    private void populateRecyclerView2(){
        likeVideo_array = generateLikeVideoList();
        videoLikeAdapter = new YoutubeLikedVideoAdapter(getActivity(), likeVideo_array);
        if(videoLikeAdapter.getItemCount()>4){ //리사이클러뷰에서 비디오 4개까지 보이도록
            ViewGroup.LayoutParams params = videoLikeView.getLayoutParams();
            params.height = 800;
            videoLikeView.setLayoutParams(params);
        }
        videoLikeView.setAdapter(videoLikeAdapter);
        videoLikeAdapter.setOnClickListener(new YoutubeLikedVideoAdapter.likedOnClickListener() {
            @Override
            public void onLikeButtonListener(View v, int i) {
                YoutubeVideoModel youtubeVideoModel = likeVideo_array.get(i);
                int index = videoAdapter.findIndex(youtubeVideoModel.getVideoID());
                if(index!=0) { //비디오 영상리스트에 좋아요 취소한 영상 있다면
                    //추천 비디오 영상 리스트에서 해당 비디오 좋아요(모양) 취소되도록
                    Button likeButton = videoView.findViewHolderForAdapterPosition(index).itemView.findViewById(R.id.video_likeButton);
                    likeButton.setBackgroundResource(R.drawable.heart);
                    videoAdapter.setUnliked(index);
                }
                //좋아요한 비디오 영상 리스트에서 해당 비디오 삭제되도록
                Log.d("DBGLOG","adapter_remove: "+youtubeVideoModel.getVideoID());
                videoLikeAdapter.removeVideo(youtubeVideoModel.getVideoID());
                videoLikeAdapter.notifyDataSetChanged();

                // 좋아요한 db에서 해당 비디오 delete
            }
        });

    }

    private ArrayList<YoutubeVideoModel> generateVideoList(){ //화면에 띄울 비디오 정보 삽입하는 부분
        ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList = new ArrayList<>();
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

    private ArrayList<YoutubeVideoModel> generateLikeVideoList(){ //화면에 띄울 좋아요한 비디오 정보 삽입하는 부분
        ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList = new ArrayList<>();
        for(int i=0;i<likeVideo_id_array.size();i++){
            YoutubeVideoModel model = new YoutubeVideoModel();
            model.setVideoID(likeVideo_id_array.get(i));
            model.setTitle(likeVideo_title_array.get(i));
            model.setViewNum(likeVideo_viewNum_array.get(i));
            model.setPostedTime(likeVideo_postedTime_array.get(i));
            model.setLiked(likeVideo_liked_array.get(i));

            youtubeVideoModelArrayList.add(model);
        }

        return youtubeVideoModelArrayList;
    }

    private void generateTagList(){
        tagArray = new ArrayList<>();

        //String[] tagStrList = {"목 꺾임","눕는 자세","기대는 자세","굽은 어깨"}; //화면에 띄울 태그 리스트
        String[] tagStrList = {"Rounded shoulders","Forward head posture", "Crooked pelvis"}; //화면에 띄울 태그 리스트

        for(int i=0;i<tagStrList.length;i++){
            tagStrList[i] = " # ".concat(tagStrList[i]+" ");
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
