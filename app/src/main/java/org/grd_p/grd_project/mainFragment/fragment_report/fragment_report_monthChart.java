package org.grd_p.grd_project.mainFragment.fragment_report;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

import org.grd_p.grd_project.R;

import java.util.ArrayList;
import java.util.List;

public class fragment_report_monthChart extends Fragment {
    PieChart sitting_time,sitting_degree,sitting_direction;
    BarChart sitting_time_part;
    Button monthBefore, monthAfter, info;

    float right_ratio, wrong_ratio;
    float Left_ratio, Right_ratio, Straight_ratio, Distorted_ratio;
    float Turtle, Slouched, PelvisImbalance, Scoliosis, HipPain, KneePain, PoorCirculation;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_report_monthchart, container, false);

        //DB에서 기존 데이터 불러오는 작업
        right_ratio=51f;wrong_ratio=49f;
        Left_ratio=35f; Right_ratio=65f;
        Straight_ratio=30f; Distorted_ratio=70f;

        Turtle=15f; Slouched=20f; PelvisImbalance=5f; Scoliosis=10f; HipPain=30f; KneePain=5f; PoorCirculation=15f;

        info = rootView.findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        //기존 데이터 있으면 dayBefore.setEnabled(true)
        monthBefore = rootView.findViewById(R.id.monthBefore_button);
        monthBefore.setEnabled(false);
        monthBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //전날 데이터 가져오는 작업 (함수)
            }
        });
        monthAfter = rootView.findViewById(R.id.monthAfter_button);
        monthAfter.setEnabled(false); //다음 날 데이터 선택 불가능
        monthAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //다음 날 데이터 가져오는 작업 (함수)
            }
        });

        sitting_time = rootView.findViewById(R.id.sitting_time);
        sitting_degree = rootView.findViewById(R.id.sitting_degree);
        sitting_direction = rootView.findViewById(R.id.sitting_direction);
        pieChart_Setting();

        pieChart_dataSet();
        pieChart2_dataSet();
        pieChart3_dataSet();

        sitting_time_part = rootView.findViewById(R.id.sitting_time_part);
        barChart_dataSet();

        return rootView;
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
        alert.getWindow().setLayout(750,800);
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

        //monthBarDataSet dataset = new monthBarDataSet(yValues, "잘못 앉은 시간 (단위: 시간)");
        monthBarDataSet dataset = new monthBarDataSet(yValues, "Wrong Sitting time (unit: %)");
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
    }
}

class monthBarDataSet extends BarDataSet {
    public monthBarDataSet(List<BarEntry> yVals, String label) {
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
