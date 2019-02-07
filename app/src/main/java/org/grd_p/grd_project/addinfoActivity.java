package org.grd_p.grd_project;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

public class addinfoActivity extends AppCompatActivity {
    Spinner sexInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addinfo);

        sexInput = (Spinner)findViewById(R.id.sexInput);
        sexInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // adapterView.getItemAtPosition(i)
                if(i==0){ //처음부터 뜸 -> 수정해야
                    Toast.makeText(addinfoActivity.this,"Male과 Female 중에 선택해주세요!", Toast.LENGTH_LONG).show();
                    return;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
}
