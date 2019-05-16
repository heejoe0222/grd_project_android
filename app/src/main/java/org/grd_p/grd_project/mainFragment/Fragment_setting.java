package org.grd_p.grd_project.mainFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import org.grd_p.grd_project.R;

public class Fragment_setting extends Fragment{
    static final String[] bodypart={"Neck","Shoulder","Pelvis","Leg"}; //목, 어깨, 골반, 다리
    private RecyclerView listView;
    private settingAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_setting,container,false);
        //Log.d("DBGLOG FRAG","setting");

        String user_id = getArguments().getString("user_id");

        listView = rootView.findViewById(R.id.listView);
        adapter = new settingAdapter(getContext(),bodypart);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        listView.setLayoutManager(layoutManager);
        listView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        listView.setAdapter(adapter);

        return rootView;

    }
}

class settingAdapter extends RecyclerView.Adapter<settingAdapter.MyViewHolder> {

    private String[] settingName;
    Context context;


    public settingAdapter(Context context, String[] settingName) {
        this.settingName = settingName;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckedTextView CheckedTextView;

        public MyViewHolder(View view) {
            super(view);
            CheckedTextView = view.findViewById(R.id.CheckedTextView);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_items, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        holder.CheckedTextView.setText(settingName[position]);
        holder.CheckedTextView.setCheckMarkDrawable(R.drawable.alarm_on);

        // perform on Click Event Listener on CheckedTextView
        holder.CheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean value = holder.CheckedTextView.isChecked();
                if (value) {
                    // set check mark drawable and set checked property to false

                    holder.CheckedTextView.setCheckMarkDrawable(R.drawable.alarm_off);
                    holder.CheckedTextView.setChecked(false);
                } else {
                    // set check mark drawable and set checked property to true

                    holder.CheckedTextView.setCheckMarkDrawable(R.drawable.alarm_on);
                    holder.CheckedTextView.setChecked(true);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return settingName.length;
    }
}

