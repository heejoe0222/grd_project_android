package org.grd_p.grd_project.mainFragment.fragment_video;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.grd_p.grd_project.NetworkStatus;
import org.grd_p.grd_project.PoschairDB.DataBaseAdapter;
import org.grd_p.grd_project.R;
import org.grd_p.grd_project.PoschairDB.DatabaseHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Fragment_video extends Fragment {
    RequestQueue requestQueue;

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
    DataBaseAdapter dbAdapter;
    DatabaseHelper helper;

    private TextView updateTime, isFavoriteVideo;
    String user_id;
    private String getVideo_url = "http://101.101.163.32/video/";
    private String getLikeVideo_url = "http://101.101.163.32/likeVideo/";
    private String changeVideoLike_url = "http://101.101.163.32/changeVideoLike/";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd HH:mm");

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_video,container,false);
        Log.d("DBGLOG FRAG","video");

        user_id = getArguments().getString("user_id");
        requestQueue = Volley.newRequestQueue(getContext());

        updateTime = rootView.findViewById(R.id.updateTime);
        isFavoriteVideo = rootView.findViewById(R.id.isFavoriteVideo);



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

        return rootView;
    }

    private void setVideoInfo(){
        helper = new DatabaseHelper(getContext());
        dbAdapter = new DataBaseAdapter(getContext());
        dbAdapter.open();
        db = helper.getWritableDatabase();

        int status = NetworkStatus.getConnectivityStatus(getContext());

        //서버에서 받아옴
        if(status==NetworkStatus.TYPE_MOBILE || status==NetworkStatus.TYPE_WIFI){
            //서버에 user_id 전달하면서 비디오 db 요청
            //내부 db에 저장 및 video_array 시리즈에 값 넣기

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    getVideo_url,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            // Process the JSON
                            try{
                                for(int i=0;i<response.length();i++){
                                    JSONObject video = response.getJSONObject(i);

                                    // Get the current student (json object) data
                                    String videoID = video.getString("vidID");
                                    String title = video.getString("vidTitle");
                                    int viewNum = video.getInt("view");
                                    String str_viewNum = viewNum+" views";
                                    String postedTime = video.getString("uploadDate");
                                    int videoLike = video.getInt("liked");
                                    Log.d("DBGLOG","vidID: "+videoID+", title: "+title+", str_viewNum: "+str_viewNum);
                                    Log.d("DBGLOG","postedTime: "+postedTime+", videoLike: "+videoLike);

                                    video_id_array.add(videoID);
                                    video_title_array.add(title);
                                    video_viewNum_array.add(str_viewNum);
                                    video_postedTime_array.add(postedTime);
                                    video_liked_array.add(videoLike);

                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }

                            if(db!=null) {
                                dbAdapter.deleteTable("video");
                                for (int i = 0; i < video_id_array.size(); i++) {
                                    int viewNum = Integer.parseInt(video_viewNum_array.get(i).split(" ")[0]);
                                    Object[] params = {video_id_array.get(i), video_title_array.get(i), viewNum, video_postedTime_array.get(i), video_liked_array.get(i)};
                                    dbAdapter.InsertTable1Data(params);

                                }
                            }else{
                                Log.d("DBGLOG","not exist db");
                            }

                            setVideoInitialize();

                            Log.d("DBGLOG","finish conneciton");

                            populateRecyclerView();
                            setUpdateTimefromServer();


                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error){
                            Log.d("DBGLOG","error to get videoJsonArray");
                        }
                    }
            );
            requestQueue.add(jsonArrayRequest);

            JsonArrayRequest jsonArrayRequest2 = new JsonArrayRequest(
                    Request.Method.GET,
                    getLikeVideo_url,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d("DBGLOG","likevideo response: "+response);
                            if (response!=null) {
                                // Process the JSON
                                try{
                                    for(int i=0;i<response.length();i++){
                                        JSONObject video = response.getJSONObject(i);

                                        // Get the current student (json object) data
                                        String videoID = video.getString("vidID");
                                        String title = video.getString("vidTitle");
                                        int viewNum = video.getInt("view");
                                        String str_viewNum = viewNum+" views";
                                        String postedTime = video.getString("uploadDate");
                                        int videoLike = video.getInt("liked");

                                        likeVideo_id_array.add(videoID);
                                        likeVideo_title_array.add(title);
                                        likeVideo_viewNum_array.add(str_viewNum);
                                        likeVideo_postedTime_array.add(postedTime);
                                        likeVideo_liked_array.add(videoLike);

                                    }
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }else{
                                Log.d("DBGLOG","likevideo response null");
                            }
                            //서버에서 받아온 내용 내부 DB에 업데이트
                            if(db!=null){
                                dbAdapter.deleteTable("likeVideo");
                                for (int i=0;i<likeVideo_id_array.size();i++){
                                    int viewNum=Integer.parseInt(likeVideo_viewNum_array.get(i).split(" ")[0]);
                                    Object[] params = {likeVideo_id_array.get(i),likeVideo_title_array.get(i),viewNum,likeVideo_postedTime_array.get(i),likeVideo_liked_array.get(i)};
                                    dbAdapter.InsertTable2Data(params);
                                }
                            }else{
                                Log.d("DBGLOG","not exist db");
                            }

                            //좋아요한 비디오 리스트 부분
                            populateRecyclerView2();
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error){
                            Log.d("DBGLOG","error to get videoJsonArray");
                        }
                    }
            );
            requestQueue.add(jsonArrayRequest2);


        }else{ //기기장치에서 받아옴 (내부 DB)

            Log.d("DBGLOG","not connected with internet");

            if(db!=null){
                //videoID, title, viewNum, postedTime, videoLike
                String sql = "select videoID, title, viewNum, postedTime, videoLike from video order by viewNum desc";
                Cursor cursor = db.rawQuery(sql,null);

                for(int i=0;i<cursor.getCount();i++){
                    cursor.moveToNext();
                    video_id_array.add(cursor.getString(0));
                    video_title_array.add(cursor.getString(1));
                    String s_viewNum=cursor.getInt(2)+" views";
                    video_viewNum_array.add(s_viewNum);
                    video_postedTime_array.add(cursor.getString(3));
                    video_liked_array.add(cursor.getInt(4));
                }

                sql ="select count(*) from likeVideo";
                cursor = db.rawQuery(sql,null);
                cursor.moveToFirst();
                int count = cursor.getInt(0);
                Log.d("DBGLOG","inner db num of likeVideo count: "+Integer.toString(count));

                sql = "select videoID, title, viewNum, postedTime, videoLike from likeVideo order by viewNum desc";
                cursor = db.rawQuery(sql,null);

                for(int i=0;i<cursor.getCount();i++){
                    cursor.moveToNext();
                    likeVideo_id_array.add(cursor.getString(0));
                    likeVideo_title_array.add(cursor.getString(1));
                    String s_viewNum=cursor.getInt(2)+" views";
                    likeVideo_viewNum_array.add(s_viewNum);
                    likeVideo_postedTime_array.add(cursor.getString(3));
                    likeVideo_liked_array.add(cursor.getInt(4));
                }

                cursor.close();
                db.close();
            }else{
                Log.d("DBGLOG","not exist db");
            }

        }

    }

    public void setVideoInitialize(){
        //기존에 디바이스에 저장된 정보 없을 경우 string.xml에 미리 세팅된 값 가져와서 띄움
        if((video_id_array.isEmpty()) && (video_liked_array.isEmpty()) && (video_title_array.isEmpty())
                && (video_viewNum_array.isEmpty()) && (video_postedTime_array.isEmpty())){

            Log.d("DBGLOG","not exist innerdb");

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

            if(db!=null){
                for (int i=0;i<video_id_array.size();i++){
                    int viewNum=Integer.parseInt(video_viewNum_array.get(i).split(" ")[0]);
                    Object[] params = {video_id_array.get(i),video_title_array.get(i),viewNum,video_postedTime_array.get(i),video_liked_array.get(i)};
                    dbAdapter.InsertTable1Data(params);
                }
                String sql ="select count(*) from video";
                Cursor cursor = db.rawQuery(sql,null);
                cursor.moveToFirst();
                int count = cursor.getInt(0);
                Log.d("DBGLOG","inner db insert success! count: "+count);
                db.close();
            }
        }
    }

    public void setUpdateTimefromServer(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        String getTime = simpleDateFormat.format(date);

        updateTime.setText("Last update time: "+getTime);
    }

    //서버에 user의 좋아요/좋아요 취소 여부를 전송하는 함수
    public void sendVideoLikeToServer(String videoID, String videoLike){
        final String video= videoID;
        final String videolike = videoLike;
        StringRequest request = new StringRequest(
                Request.Method.POST,
                changeVideoLike_url,
                new Response.Listener<String>(){
                    @Override
                    //응답 성공적으로 받았을 때
                    public void onResponse(String response) {
                        Log.d("DBGLOG","success to update server data");
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("DBGLOG","onErrorResponse "+error);
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<String,String>();
                params.put("user_id",user_id);
                params.put("videoID",video);
                params.put("isLike",videolike);
                return params;
            }
        };
        request.setShouldCache(false);
        requestQueue.add(request);

    }



    //schema: (videoID text PRIMARY KEY, title text, viewNum text, postedTime text, videoLike integer)
    public void insertTolikeVideo(String videoID, String title, int viewNum, String postedTime){ //videoDB 업데이트(1) 하고 likeVideoDB에 추가
        db = helper.getWritableDatabase();
        if(db!=null){
            Object[] params = {videoID,title,viewNum,postedTime,1};
            dbAdapter.InsertTable2Data(params);
            Log.d("DBGLOG","success to insert favorite video data");

            db.execSQL("UPDATE video SET videoLike=1 WHERE videoID='"+videoID+"'");
            db.close();
        }else{
            Log.d("DBGLOG","not exist db");
        }
    }

    public void deleteFromlikeVideo(String videoID){ //videoDB 업데이트(0) 하고 likeVideoDB에서 삭제
        db = helper.getWritableDatabase();
        if(db!=null){

            db.execSQL("delete from likeVideo where videoID='"+videoID+"'");
            db.execSQL("UPDATE video SET videoLike=0 WHERE videoID='"+videoID+"'");

            db.close();
        }else{
            Log.d("DBGLOG","not exist db");
        }
    }

    public void deleteFromlikeVideo2(String videoID){ //likeVideo에만 존재했던 영상, likeVideoDB에서 삭제
        db = helper.getWritableDatabase();
        if(db!=null){
            db.execSQL("delete from likeVideo where videoID='"+videoID+"'");
            db.close();
        }else{
            Log.d("DBGLOG","not exist db");
        }
    }

    public void checkIsFavoriteVideo(){
        if (videoLikeAdapter.getItemCount()==0){
            isFavoriteVideo.setText("No FAVORITE video yet!");
        }else{
            isFavoriteVideo.setText("");
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
            checkIsFavoriteVideo(); //비디오 존재하는지 체크 -> 없으면 textvivew 띄움
            youtubeVideoModel.setLiked(0);
            // 비디오 db에서 videoLike 0 으로 값 update
            // 좋아요한 db에서 해당 비디오 delete
            deleteFromlikeVideo(youtubeVideoModel.getVideoID());
            sendVideoLikeToServer(youtubeVideoModel.getVideoID(),"unlike");

        }else{
            //좋아요한 비디오 영상 리스트에 해당 비디오 추가되도록
            likeButton.setBackgroundResource(R.drawable.selected_heart);
            Log.d("DBGLOG","adapter_add: "+youtubeVideoModel.getVideoID()+", "+youtubeVideoModel.getTitle());
            videoLikeAdapter.addVideo(youtubeVideoModel);
            videoLikeAdapter.notifyDataSetChanged();
            checkIsFavoriteVideo(); //비디오 존재하는지 체크 -> 없으면 textvivew 띄움
            youtubeVideoModel.setLiked(1);
            //비디오 db에서 videoLike 1 으로 값 update
            //좋아요한 db에 해당 비디오 insert
            int viewNum = Integer.parseInt(youtubeVideoModel.getViewNum().split(" ")[0]);
            insertTolikeVideo(youtubeVideoModel.getVideoID(),youtubeVideoModel.getTitle(),viewNum ,youtubeVideoModel.getPostedTime());
            sendVideoLikeToServer(youtubeVideoModel.getVideoID(),"like");
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
        checkIsFavoriteVideo(); //비디오 존재하는지 체크 -> 없으면 textvivew 띄움
        videoLikeAdapter.setOnClickListener(new YoutubeLikedVideoAdapter.likedOnClickListener() {
            @Override
            public void onLikeButtonListener(View v, int i) {
                YoutubeVideoModel youtubeVideoModel = likeVideo_array.get(i);
                int index = videoAdapter.findIndex(youtubeVideoModel.getVideoID());

                if(index>=0) { //비디오 영상리스트에 좋아요 취소한 영상 있다면
                    //추천 비디오 영상 리스트에서 해당 비디오 좋아요(모양) 취소되도록
                    Log.d("DBGLOG","videoAdapterIndex: "+index);
                    Button likeButton = videoView.findViewHolderForAdapterPosition(index).itemView.findViewById(R.id.video_likeButton);
                    likeButton.setBackgroundResource(R.drawable.heart);
                    videoAdapter.setUnliked(index);

                    //좋아요한 비디오 영상 리스트에서 해당 비디오 삭제되도록
                    Log.d("DBGLOG","adapter_remove1: "+youtubeVideoModel.getVideoID());
                    videoLikeAdapter.removeVideo(youtubeVideoModel.getVideoID());
                    videoLikeAdapter.notifyDataSetChanged();
                    checkIsFavoriteVideo(); //비디오 존재하는지 체크 -> 없으면 textvivew 띄움

                    // 좋아요한 db에서 해당 비디오 delete
                    deleteFromlikeVideo(youtubeVideoModel.getVideoID());
                    sendVideoLikeToServer(youtubeVideoModel.getVideoID(),"unlike");

                }else{
                    //좋아요한 비디오 영상 리스트에서 해당 비디오 삭제되도록
                    Log.d("DBGLOG","adapter_remove2: "+youtubeVideoModel.getVideoID());
                    videoLikeAdapter.removeVideo(youtubeVideoModel.getVideoID());
                    videoLikeAdapter.notifyDataSetChanged();
                    checkIsFavoriteVideo(); //비디오 존재하는지 체크 -> 없으면 textvivew 띄움

                    // 좋아요한 db에서 해당 비디오 delete
                    deleteFromlikeVideo2(youtubeVideoModel.getVideoID());
                    sendVideoLikeToServer(youtubeVideoModel.getVideoID(),"unlike");
                }

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

            button = itemView.findViewById(R.id.tag_button);
        }
    }


}
