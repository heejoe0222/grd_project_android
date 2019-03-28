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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.grd_p.grd_project.R;
import org.grd_p.grd_project.mainActivity;

import java.util.HashMap;
import java.util.Map;

//첫 로그인 시만 실행되는 액티비티
public class addinfoActivity extends AppCompatActivity {
    private EditText ageInput;
    private EditText heightInput;
    private EditText weightInput;
    private RadioButton male,female;

    RequestQueue requestQueue;
    String reply="fail";
    String sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addinfo);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        ageInput = findViewById(R.id.ageInput);
        heightInput = findViewById(R.id.heightInput);
        weightInput = findViewById(R.id.weightInput);
        male = findViewById(R.id.male_button);
        female = findViewById(R.id.female_button);

        Button registerButton = findViewById(R.id.registerButton);
        //등록 버튼 눌렀을 때
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = inputCheck(); //inputcheck 함수 호출
                if (!check) {
                    return;
                }
                OnUpdateInfo();
            }
        });
    }
    public void showMainActivity(){
        finish();
        startActivity(new Intent(getApplicationContext(), mainActivity.class));
    }
    public void OnUpdateInfo(){
        //String url = "http://ec2-52-79-250-100.ap-northeast-2.compute.amazonaws.com/addInfo/";
        String url = "http://101.101.163.32/addInfo/";
        sex="female";
        if (male.isChecked())
            sex="male";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>(){
                    @Override
                    //응답 성공적으로 받았을 때
                    public void onResponse(String response) {
                        //Log.d("DBGLOG","onResponse");
                        reply =  response; //응답 받은 string
                        if(reply.equals("success")){
                            showMainActivity(); //mainActivity() 대신 초기 자세 설정으로 바꿔야
                        }
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
                Map<String,String> params = new HashMap<>();
                params.put("age",ageInput.getText().toString());
                params.put("sex",sex);
                params.put("height",heightInput.getText().toString());
                params.put("weight",weightInput.getText().toString());
                return params;
            }
        };
        request.setShouldCache(false);
        requestQueue.add(request);

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
