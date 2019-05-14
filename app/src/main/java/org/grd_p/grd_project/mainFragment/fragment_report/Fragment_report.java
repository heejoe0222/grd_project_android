package org.grd_p.grd_project.mainFragment.fragment_report;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import org.grd_p.grd_project.R;

public class Fragment_report extends Fragment{
    Button dayButton,weekButton,monthButton;
    FrameLayout chartContainer;

    fragment_report_dayChart dayFragment;
    fragment_report_weekChart weekFragment;
    fragment_report_monthChart monthFragment;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_report,container,false);
        Log.d("DBGLOG FRAG","report");
        String user_id = getArguments().getString("user_id");

        chartContainer = rootView.findViewById(R.id.chart_container);
        //처음에 바로 보이는 화면은 주별
        /*
        weekFragment = new fragment_report_weekChart();
        getFragmentManager().beginTransaction().replace(chartContainer.getId(),weekFragment).commit();
        */

        dayFragment = new fragment_report_dayChart();
        getFragmentManager().beginTransaction().replace(chartContainer.getId(),dayFragment).commit();

        dayButton = rootView.findViewById(R.id.day_button);
        dayButton.setSelected(true); //지울 부분
        dayButton.setTextColor(getResources().getColor(R.color.color_white));//지울 부분
        dayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPressed_day();
                if(dayFragment==null){
                    dayFragment = new fragment_report_dayChart();
                    getFragmentManager().beginTransaction().add(chartContainer.getId(),dayFragment).commit();
                }
                if(dayFragment!=null)
                    getFragmentManager().beginTransaction().show(dayFragment).commit();
                if(weekFragment!=null)
                    getFragmentManager().beginTransaction().hide(weekFragment).commit();
                if(monthFragment!=null)
                    getFragmentManager().beginTransaction().hide(monthFragment).commit();

            }
        });

        weekButton = rootView.findViewById(R.id.week_button);
        //weekButton.setSelected(true);
        //weekButton.setTextColor(getResources().getColor(R.color.color_white));
        weekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPressed_week();
                if(weekFragment==null){
                    weekFragment = new fragment_report_weekChart();
                    getFragmentManager().beginTransaction().add(chartContainer.getId(),weekFragment).commit();
                }
                if(dayFragment!=null)
                    getFragmentManager().beginTransaction().hide(dayFragment).commit();
                if(weekFragment!=null)
                    getFragmentManager().beginTransaction().show(weekFragment).commit();
                if(monthFragment!=null)
                    getFragmentManager().beginTransaction().hide(monthFragment).commit();
            }
        });

        monthButton = rootView.findViewById(R.id.month_button);
        monthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPressed_month();
                if(monthFragment==null){
                    monthFragment = new fragment_report_monthChart();
                    getFragmentManager().beginTransaction().add(chartContainer.getId(),monthFragment).commit();
                }
                if(dayFragment!=null)
                    getFragmentManager().beginTransaction().hide(dayFragment).commit();
                if(weekFragment!=null)
                    getFragmentManager().beginTransaction().hide(weekFragment).commit();
                if(monthFragment!=null)
                    getFragmentManager().beginTransaction().show(monthFragment).commit();
            }
        });

        return rootView;
    }

    public void setPressed_day(){
        dayButton.setSelected(true);
        dayButton.setTextColor(getResources().getColor(R.color.color_white));

        weekButton.setSelected(false);
        weekButton.setTextColor(getResources().getColor(R.color.font_color));

        monthButton.setSelected(false);
        monthButton.setTextColor(getResources().getColor(R.color.font_color));
    }
    public void setPressed_week(){
        dayButton.setSelected(false);
        dayButton.setTextColor(getResources().getColor(R.color.font_color));

        weekButton.setSelected(true);
        weekButton.setTextColor(getResources().getColor(R.color.color_white));

        monthButton.setSelected(false);
        monthButton.setTextColor(getResources().getColor(R.color.font_color));
    }
    public void setPressed_month(){
        dayButton.setSelected(false);
        dayButton.setTextColor(getResources().getColor(R.color.font_color));

        weekButton.setSelected(false);
        weekButton.setTextColor(getResources().getColor(R.color.font_color));

        monthButton.setSelected(true);
        monthButton.setTextColor(getResources().getColor(R.color.color_white));
    }

}
