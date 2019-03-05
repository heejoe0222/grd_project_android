package org.grd_p.grd_project.userActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.grd_p.grd_project.R;
import org.grd_p.grd_project.mainActivity;
import org.grd_p.grd_project.userConnection.UpdateConnection;

//첫 로그인 시만 실행되는 액티비티
public class addinfoActivity extends AppCompatActivity {
    private EditText ageInput;
    private EditText heightInput;
    private EditText weightInput;
    private RadioButton male,female;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addinfo);

        ageInput = (EditText)findViewById(R.id.ageInput);
        heightInput = (EditText)findViewById(R.id.heightInput);
        weightInput = (EditText)findViewById(R.id.weightInput);
        male = (RadioButton)findViewById(R.id.male_button);
        female = (RadioButton)findViewById(R.id.female_button);

        Button registerButton = (Button)findViewById(R.id.registerButton);
        //등록 버튼 눌렀을 때
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = inputCheck(); //inputcheck 함수 호출
                if (!check) {
                    return;
                }

                String reply = OnUpdateInfo();
                if(reply.equals("success")){
                    showMainActivity(); //mainActivity()로 이동
                }
            }
        });
    }
    public void showMainActivity(){
        Intent intent = new Intent(getApplicationContext(), mainActivity.class);
        startActivity(intent);
    }
    public String OnUpdateInfo(){
        String result="fail";
        String type="updateInfo";
        String age = ageInput.getText().toString();
        String height = heightInput.getText().toString();
        String weight = weightInput.getText().toString();
        String sex="female";
        if (male.isChecked())
            sex="male";
        UpdateConnection updateConnection = new UpdateConnection(this);
        try {
            result = updateConnection.execute(type, age, sex, height, weight).get();
            return result;
        }catch (Exception e){
            Log.d("DBGLOG","Exception in UpdateConnection.execute");
            e.printStackTrace();
        }
        return result;
    }

    //inputCheck 함수 : 입력되어야 하는 내용이 다 입력됐는지 검사
    public boolean inputCheck(){
        if(ageInput.getText().toString().length()==0){
            Toast.makeText(addinfoActivity.this,"나이를 입력하세요!", Toast.LENGTH_LONG).show();
            ageInput.requestFocus();
            return false;
        }
        if(!male.isChecked()&&!female.isChecked()){
            Toast.makeText(addinfoActivity.this,"성별을 선택하세요!", Toast.LENGTH_LONG).show();
            return false;
        }

        if(heightInput.getText().toString().length()==0){
            Toast.makeText(addinfoActivity.this,"키를 입력하세요!", Toast.LENGTH_LONG).show();
            heightInput.requestFocus();
            return false;
        }
        if(weightInput.getText().toString().length()==0){
            Toast.makeText(addinfoActivity.this,"몸무게를 입력하세요!", Toast.LENGTH_LONG).show();
            weightInput.requestFocus();
            return false;
        }
        return true;
    }

}
