package org.grd_p.grd_project.mainFragment.fragment_report;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.grd_p.grd_project.NetworkStatus;
import org.grd_p.grd_project.PoschairDB.DataBaseAdapter;
import org.grd_p.grd_project.PoschairDB.DayChart;
import org.grd_p.grd_project.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class fragment_report_dayChart extends Fragment {
    RequestQueue requestQueue;
    PieChart sitting_time,sitting_degree,sitting_direction;
    BarChart sitting_time_part;
    Button dayBefore, dayAfter, info;
    TextView review,day;

    DataBaseAdapter dbAdapter;

    private String getDayChart_url = "http://101.101.163.32/dayChart/";
    String user_id="1";

    float right_ratio, wrong_ratio;
    float Left_ratio, Right_ratio, Straight_ratio,Distorted_ratio;
    float Turtle, Slouched, PelvisImbalance, Scoliosis, HipPain, KneePain, PoorCirculation;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); //DB용
    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd MMMM yyyy", Locale.US); //VIEW 용

    String getToday,currentDate; //currentDate는 DB용 형식



    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_report_daychart, container, false);
        requestQueue = Volley.newRequestQueue(getContext());
        user_id = getArguments().getString("user_id");

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        getToday = simpleDateFormat.format(date);
        currentDate = getToday;

        review = rootView.findViewById(R.id.review); //total time 표시하는 텍스트 뷰
        day = rootView.findViewById(R.id.day); //날짜 표시하는 텍스트 뷰
        day.setText(simpleDateFormat2.format(new Date(now))); //오늘 날짜 표시

        /*서버에서 받기 전 초기화 데이터*/
        right_ratio=40f;wrong_ratio=60f;
        Left_ratio=45f; Right_ratio=55f;
        Straight_ratio=30f; Distorted_ratio=70f;
        Turtle=10f; Slouched=25f; PelvisImbalance=3f; Scoliosis=12f; HipPain=30f; KneePain=5f; PoorCirculation=15f;


        info = rootView.findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });


        //DATE가 하루 간격으로 매일 있다는 가정하에 짠 코드 (DATE에 상관없으려면 INDEX attribute 추가 되어야)
        dayBefore = rootView.findViewById(R.id.dayBefore_button);
        dayBefore.setEnabled(false);
        dayBefore.setOnClickListener(new View.OnClickListener() { //이전 날짜 이동 버튼
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                try {
                cal.setTime(simpleDateFormat.parse(currentDate)); //string to date
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                cal.add(Calendar.DATE, -1);
                currentDate = simpleDateFormat.format(cal.getTime()); //currentDate 하루 전으로 update
                chartValue_Setting(currentDate);

                //gui 갱신
                dayAfter.setEnabled(true);
                day.setText(simpleDateFormat2.format(cal.getTime()));
                cal.add(Calendar.DATE, -1);
                isChartInfo(simpleDateFormat.format(cal.getTime()),-1); //이전날짜 정보 있는지 확인
            }
        });
        dayAfter = rootView.findViewById(R.id.dayAfter_button);
        dayAfter.setEnabled(false); //다음 날 데이터 선택 불가능
        dayAfter.setOnClickListener(new View.OnClickListener() { //이후 날짜 이동 버튼
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                try {
                    cal.setTime(simpleDateFormat.parse(currentDate)); //string to date
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                cal.add(Calendar.DATE, +1);
                currentDate = simpleDateFormat.format(cal.getTime()); //currentDate 하루 뒤로 update
                chartValue_Setting(currentDate);

                //gui 갱신
                dayBefore.setEnabled(true);
                day.setText(simpleDateFormat2.format(cal.getTime()));
                cal.add(Calendar.DATE, +1);
                isChartInfo(simpleDateFormat.format(cal.getTime()),1); //다음날짜 정보 있는지 확인
            }
        });

        sitting_time = rootView.findViewById(R.id.sitting_time);
        sitting_degree = rootView.findViewById(R.id.sitting_degree);
        sitting_direction = rootView.findViewById(R.id.sitting_direction);
        sitting_time_part = rootView.findViewById(R.id.sitting_time_part);

        setDayChartInfo();

        return rootView;
    }

    public void setDayChartInfo(){
        dbAdapter = new DataBaseAdapter(getContext());
        dbAdapter.open();

        int status = NetworkStatus.getConnectivityStatus(getContext());

        //서버에서 받아옴
        if(status==NetworkStatus.TYPE_MOBILE || status==NetworkStatus.TYPE_WIFI) {
            Log.d("DBGLOG","setDayChartInfo");
            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    getDayChart_url,
                    new Response.Listener<String>(){
                        @Override
                        //응답 성공적으로 받았을 때
                        public void onResponse(String response) {
                            Log.d("DBGLOG","success to send date");
                            if (response.equals("success"))
                                init_chartDataFromServer();
                        }
                        },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("DBGLOG","onErrorResponse in SetDayChart:"+error);
                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams() {
                    Map<String,String> params = new HashMap<>();
                    Log.d("DBGLOG","return params: "+user_id+","+getToday);
                    params.put("user_id",user_id);
                    params.put("sendDate",getToday);
                    return params;
                }
            };
            request.setShouldCache(false);
            requestQueue.add(request);

        }

    }

    public void init_chartDataFromServer(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                getDayChart_url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        // Process the JSON
                        try{
                            DayChart day = new DayChart();
                            Log.d("DBGLOG","response length: "+response.length());

                            if (response.length()==1) //오늘 데이터만 가져오는 경우
                                dayBefore.setEnabled(false);
                            else //오늘 이전 데이터도 가져오는 경우
                                dayBefore.setEnabled(true);

                            for(int i=0;i<response.length();i++){
                                JSONObject chartInfo = response.getJSONObject(i);

                                day.setDate(chartInfo.getString("DATE"));
                                day.setTotal_sitting_time(chartInfo.getInt("TOTAL_SITTING"));
                                day.setCorrect_sitting_time(chartInfo.getInt("CORRECT_SITTING"));
                                day.setK0(chartInfo.getInt("k0"));
                                day.setK1(chartInfo.getInt("k1"));
                                day.setK2(chartInfo.getInt("k2"));
                                day.setK3(chartInfo.getInt("k3"));
                                day.setK4(chartInfo.getInt("k4"));
                                day.setK5(chartInfo.getInt("k5"));
                                day.setK6(chartInfo.getInt("k6"));
                                day.setCorrect_pelvis(chartInfo.getInt("CORRECT_PELVIS"));
                                day.setLeft_pelvis(chartInfo.getInt("LEFT_PELVIS"));

                                Log.d("DBGLOG",day.toString());

                                dbAdapter.InsertTable3Data(day);

                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                        chartValue_Setting(getToday);
                        pieChart_Setting();

                        pieChart_dataSet();
                        pieChart2_dataSet();
                        pieChart3_dataSet();

                        barChart_dataSet();
                        dbAdapter.dbclose();

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d("DBGLOG","error in init_chartDataFromServer: "+error);
                    }
                }
        );
        jsonArrayRequest.setShouldCache(false);
        requestQueue.add(jsonArrayRequest);
    }

    public void getChartDataFromServer(){ //date:가져올 기준 날짜
        dbAdapter.dbopen();
        //서버에서 받아옴
        int status = NetworkStatus.getConnectivityStatus(getContext());
        if(status==NetworkStatus.TYPE_MOBILE || status==NetworkStatus.TYPE_WIFI) {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    getDayChart_url,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            if(response.length()!=0) { //서버에 이전날짜 정보 있는 경우
                                // Process the JSON

                                try {
                                    DayChart day = new DayChart();
                                    Log.d("DBGLOG","response length: "+response.length());

                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject chartInfo = response.getJSONObject(i);

                                        day.setDate(chartInfo.getString("DATE"));
                                        day.setTotal_sitting_time(chartInfo.getInt("TOTAL_SITTING"));
                                        day.setCorrect_sitting_time(chartInfo.getInt("CORRECT_SITTING"));
                                        day.setK0(chartInfo.getInt("k0"));
                                        day.setK1(chartInfo.getInt("k1"));
                                        day.setK2(chartInfo.getInt("k2"));
                                        day.setK3(chartInfo.getInt("k3"));
                                        day.setK4(chartInfo.getInt("k4"));
                                        day.setK5(chartInfo.getInt("k5"));
                                        day.setK6(chartInfo.getInt("k6"));
                                        day.setCorrect_pelvis(chartInfo.getInt("CORRECT_PELVIS"));
                                        day.setLeft_pelvis(chartInfo.getInt("LEFT_PELVIS"));

                                        //Log.d("DBGLOG",day.toString());

                                        dbAdapter.InsertTable3Data(day);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                dbAdapter.dbclose();
                                dayBefore.setEnabled(true);

                            }else{ //서버에도 이전날짜 정보 없는 경우
                                dayBefore.setEnabled(false);
                            }
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error){
                            Log.d("DBGLOG","error in getChartDataFromServer"+error);
                        }
                    }
            );
            jsonArrayRequest.setShouldCache(false);
            requestQueue.add(jsonArrayRequest);
        }
    }



    public void chartValue_Setting(String date){
        int total=0;
        if(dbAdapter==null)
            dbAdapter.open();
        else
            dbAdapter.dbopen();

        Cursor cursor = dbAdapter.fetchSomeTable3data(date); //해당 date에 해당하는 정보들 불러옴

        if(cursor!=null && cursor.moveToFirst()) {
            total = cursor.getInt(cursor.getColumnIndex(DayChart.TOTAL_SITTING));


            int hour = total / 60;
            int min = total % 60;
            review.setText("Total Sitting time: " + hour + " h " + min + " m");


            right_ratio = cursor.getInt(cursor.getColumnIndex(DayChart.CORRECT_SITTING));
            wrong_ratio = 100f - right_ratio;

            Straight_ratio = cursor.getInt(cursor.getColumnIndex(DayChart.CORRECT_PELVIS));
            Distorted_ratio = 100f - Straight_ratio;

            Left_ratio = cursor.getInt(cursor.getColumnIndex(DayChart.LEFT_PELVIS));
            Right_ratio = 100f - Left_ratio;

            Turtle = cursor.getInt(cursor.getColumnIndex(DayChart.KEYWORD0));
            Slouched = cursor.getInt(cursor.getColumnIndex(DayChart.KEYWORD1));
            PelvisImbalance = cursor.getInt(cursor.getColumnIndex(DayChart.KEYWORD2));
            Scoliosis = cursor.getInt(cursor.getColumnIndex(DayChart.KEYWORD3));
            HipPain = cursor.getInt(cursor.getColumnIndex(DayChart.KEYWORD4));
            KneePain = cursor.getInt(cursor.getColumnIndex(DayChart.KEYWORD5));
            PoorCirculation = cursor.getInt(cursor.getColumnIndex(DayChart.KEYWORD6));

            cursor.close();
            dbAdapter.dbclose();
        }
        pieChart_dataSet();
        pieChart2_dataSet();
        pieChart3_dataSet();

        barChart_dataSet();
    }

    public void isChartInfo(String date, int flag){

        if(dbAdapter==null)
            dbAdapter.open();
        else
            dbAdapter.dbopen();


        Log.d("DBGLOG","Date: "+date);
        Cursor cursor = dbAdapter.fetchSomeTable3data(date);

        //if (cursor != null)
        //    cursor.moveToFirst();
        final String sendDate = date;

        if(cursor!=null && cursor.moveToFirst()) { //안드로이드 db에 기록 있는 경우
            Log.d("DBGLOG","RECORD O");
            cursor.close();
            if(flag==1){ //다음 날짜일 경우
                dayAfter.setEnabled(true);
//                try {
//                    day.setText(simpleDateFormat2.format(simpleDateFormat.parse(date)));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
            }else if(flag==-1){ //이전 날짜일 경우
                dayBefore.setEnabled(true);
//                try {
//                    day.setText(simpleDateFormat2.format(simpleDateFormat.parse(date)));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
            }
        } else{ //기록 없는 경우
            Log.d("DBGLOG","RECORD X");
            cursor.close();
            if(flag==1){
                dayAfter.setEnabled(false);
            }else if(flag==-1){
                StringRequest request = new StringRequest(
                        Request.Method.POST,
                        getDayChart_url,
                        new Response.Listener<String>(){
                            @Override
                            //응답 성공적으로 받았을 때
                            public void onResponse(String response) {
                                Log.d("DBGLOG","success to send date");
                                if (response.equals("success"))
                                    getChartDataFromServer(); //이전날짜 정보 서버에서 받아옴
                            }
                        },
                        new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("DBGLOG","onErrorResponse in SetDayChart:"+error);
                            }
                        }
                ){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String,String> params = new HashMap<>();
                        params.put("user_id",user_id);
                        params.put("sendDate",sendDate);
                        Log.d("DBGLOG","return params: "+user_id+","+sendDate);
                        return params;
                    }
                };
                request.setShouldCache(false);
                requestQueue.add(request);
            }
        }
    }

    public void showDialog(){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(),R.style.AlertDialog);
        dialogBuilder.setTitle(" Label Info");
        dialogBuilder.setIcon(R.drawable.question_small);
        dialogBuilder.setMessage("\nT: Turtle\nS: Slouched\nP: PelvisImbalance\nS: Scoliosis\nH: HipPain\nK: KneePain\nPC: PoorCirculation");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                dlg.dismiss();
            }});
        AlertDialog alert = dialogBuilder.create();
        alert.show();
        alert.getWindow().setLayout(950,1100);
    }

    public void barChart_dataSet(){
        ArrayList<BarEntry> yValues = new ArrayList();
        yValues.add(new BarEntry(0,Turtle));
        yValues.add(new BarEntry(1,Slouched));
        yValues.add(new BarEntry(2,PelvisImbalance));
        yValues.add(new BarEntry(3,Scoliosis));
        yValues.add(new BarEntry(4,HipPain));
        yValues.add(new BarEntry(5,KneePain));
        yValues.add(new BarEntry(6,PoorCirculation));


        //MyBarDataSet dataset = new MyBarDataSet(yValues, "잘못 앉은 시간 (단위: 분)");
        dayBarDataSet dataset = new dayBarDataSet(yValues, "Wrong Sitting time (unit: %)");
        dataset.setValueTextSize(13);
        dataset.setColors(new int[] {R.color.theme1,R.color.theme2,R.color.theme3},getContext());
        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataset);

        //String[] labels={"목","어깨/등","골반","척추","고관절","무릎","혈액순환"} 키워드 7개
        final String[] labels={"T", "S", "P", "S", "H", "K", "PC"};

        XAxis xAxis = sitting_time_part.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(12);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels)); //x 축 레이블 수정해야!
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        Legend l = sitting_time_part.getLegend();
        l.setTextSize(13);

        sitting_time_part.getAxisRight().setDrawAxisLine(false);
        sitting_time_part.getAxisRight().setDrawLabels(false);

        BarData data = new BarData(dataset);
        data.setHighlightEnabled(false);
        data.setBarWidth(0.9f);
        sitting_time_part.setData(data);
        sitting_time_part.setFitBars(true);

        Description description = new Description();
        description.setText("");
        sitting_time_part.setDescription(description);
        sitting_time_part.invalidate();

    }

    public void pieChart_Setting(){
        sitting_time.setUsePercentValues(true);
        sitting_time.getDescription().setEnabled(false);
        //sitting_time.setExtraOffsets(5,10,5,5);
        //sitting_time.setDragDecelerationFrictionCoef(0.95f);

        sitting_time.setDrawHoleEnabled(false);
        sitting_time.setHoleColor(Color.WHITE);
        //sitting_time.setTransparentCircleRadius(61f);

        sitting_degree.setUsePercentValues(true);
        sitting_time.getDescription().setEnabled(false);
        sitting_degree.setDrawHoleEnabled(false);
        sitting_degree.setHoleColor(Color.WHITE);

        sitting_direction.setUsePercentValues(true);
        sitting_time.getDescription().setEnabled(false);
        sitting_direction.setDrawHoleEnabled(false);
        sitting_direction.setHoleColor(Color.WHITE);
    }
    public void pieChart_dataSet(){
        /*chart에 넣을 데이터 엔트리 생성*/
        ArrayList<PieEntry> yValues = new ArrayList();

//        yValues.add(new PieEntry(right_ratio,"바른 자세"));
//        yValues.add(new PieEntry(wrong_ratio,"잘못된 자세"));
        yValues.add(new PieEntry(right_ratio,"Correct Posture"));
        yValues.add(new PieEntry(wrong_ratio,"Wrong Posture"));

        Description description = new Description();
        //description.setText("총 앉은 시간"); //라벨
        description.setText("Sitting Ratio");
        description.setTextSize(18);
        sitting_time.setDescription(description);

        Legend l = sitting_time.getLegend();
        l.setTextSize(13);

        //sitting_time.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션

        /*chart dataSet 생성(이때 dataSet은 차트의 모양이나 환경설정을 지정한다)*/
        //PieDataSet dataSet = new PieDataSet(yValues,"(단위: %)");
        PieDataSet dataSet = new PieDataSet(yValues,"(unit: %)");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        /*data 객체에 dataSet 객체 넣어주기*/
        PieData data = new PieData((dataSet));
        data.setValueTextSize(20f);
        data.setValueTextColor(Color.BLACK);
        data.setValueFormatter(new PercentFormatter());

        /*chart에 data 객체 넣기*/
        sitting_time.setData(data);
        sitting_time.invalidate();
    }

    public void pieChart2_dataSet(){
        /*chart에 넣을 데이터 엔트리 생성*/
        ArrayList<PieEntry> yValues = new ArrayList();

//        yValues.add(new PieEntry(Straight_ratio,"바른 골반"));
//        yValues.add(new PieEntry(Distorted_ratio,"삐뚤어진 골반"));
        yValues.add(new PieEntry(Straight_ratio,"Correct pelvis"));
        yValues.add(new PieEntry(Distorted_ratio,"Crooked pelvis"));

        Description description = new Description();
        //description.setText("비율"); //라벨
        description.setText("Ratio"); //라벨
        description.setTextSize(18);
        sitting_degree.setDescription(description);

        Legend l = sitting_degree.getLegend();
        l.setTextSize(13);

        //sitting_time.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션

        /*chart dataSet 생성(이때 dataSet은 차트의 모양이나 환경설정을 지정한다)*/
        PieDataSet dataSet = new PieDataSet(yValues,"");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(new int[] {R.color.theme4,R.color.theme5},getContext());


        /*data 객체에 dataSet 객체 넣어주기*/
        PieData data = new PieData((dataSet));
        data.setValueTextSize(20f);
        data.setValueTextColor(Color.BLACK);
        data.setValueFormatter(new PercentFormatter());

        /*chart에 data 객체 넣기*/
        sitting_degree.setData(data);
        sitting_degree.setDrawEntryLabels(false);
        sitting_degree.invalidate();
    }

    public void pieChart3_dataSet(){
        /*chart에 넣을 데이터 엔트리 생성*/
        ArrayList<PieEntry> yValues = new ArrayList();

//        yValues.add(new PieEntry(Left_ratio,"왼쪽 골반"));
//        yValues.add(new PieEntry(Right_ratio,"오른쪽 골반"));
        yValues.add(new PieEntry(Left_ratio,"Left pelvis"));
        yValues.add(new PieEntry(Right_ratio,"Right pelvis"));

        Description description = new Description();
        //description.setText("방향"); //라벨
        description.setText("Direction"); //라벨
        description.setTextSize(18);
        sitting_direction.setDescription(description);

        Legend l = sitting_direction.getLegend();
        l.setTextSize(13);

        //sitting_time.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션

        /*chart dataSet 생성(이때 dataSet은 차트의 모양이나 환경설정을 지정한다)*/
        PieDataSet dataSet = new PieDataSet(yValues,"");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(new int[] {R.color.theme6,R.color.theme7},getContext());

        /*data 객체에 dataSet 객체 넣어주기*/
        PieData data = new PieData((dataSet));
        data.setValueTextSize(20f);
        data.setValueTextColor(Color.BLACK);
        data.setValueFormatter(new PercentFormatter());

        /*chart에 data 객체 넣기*/
        sitting_direction.setData(data);
        sitting_direction.setDrawEntryLabels(false);
        sitting_direction.invalidate();
    }
}

class dayBarDataSet extends BarDataSet {
    public dayBarDataSet(List<BarEntry> yVals, String label) {
        super(yVals, label);
    }

    @Override
    public int getColor(int index) {
        if (getEntryForIndex(index).getY() < 10) //기준 나중에 바꿔야
            return mColors.get(0);
        else if (getEntryForIndex(index).getY() < 20)
            return mColors.get(1);
        else
            return mColors.get(2);
    }
}

