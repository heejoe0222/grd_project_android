package org.grd_p.grd_project;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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
                // * 서버에 정보 올리는 코드 들어가야 함 *
                showMainActivity(); //mainActivity로 이동
            }
        });
    }
    public void showMainActivity(){
        Intent intent = new Intent(getApplicationContext(),mainActivity.class);
        startActivity(intent);
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
