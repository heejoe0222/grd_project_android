package org.grd_p.grd_project.userActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class loginActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    String reply="fail";
    EditText emailInput;
    EditText pwInput;
    Button signupB;
    CheckBox remember_login; //나중에 코드 추가 //sharedPreference 이용해서 자동로그인 구현
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        emailInput = findViewById(R.id.emailInput);

        pwInput = findViewById(R.id.passwordInput);
        //비밀번호 형식으로 뜨도록 설정
        pwInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        signupB = findViewById(R.id.signupButton);
        //회원가입 버튼 클릭
        signupB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignUpActivity();
            }
        }); //signUpActivity로 이동

        Button loginB = findViewById(R.id.loginButton);
        //로그인 버튼 클릭
        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = inputCheck();
                if (!check) {
                    return;
                }
                //테스트 위해 지움
                OnLogin();
                //showMainActivity(); //mainActivity()로 이동
            }
        });
    }
    public void OnLogin(){
        //String url = "http://ec2-52-79-250-100.ap-northeast-2.compute.amazonaws.com/login/";
        String url = "http://101.101.163.32/login/";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>(){
                    @Override
                    //응답 성공적으로 받았을 때
                    public void onResponse(String response) {
                        //Log.d("DBGLOG","onResponse");
                        reply =  response; //응답 받은 string
                        Log.d("DBGLOG","responese: "+reply);
                        if(reply.equals("success")){
                            showMainActivity(); //mainActivity()로 이동
                        }else if(reply.equals("wrong_pw")){
                            new AlertDialog.Builder(loginActivity.this)
                                    .setTitle("Fail to log in")
                                    .setMessage("비밀번호가 올바르지 않습니다!\n다시 입력해주세요.")
                                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dlg, int sumthin) {
                                            pwInput.getText().clear();
                                            return;
                                        }
                                    }).show(); // 팝업창 보여줌
                        }else if(reply.equals("non_email")) {
                            new AlertDialog.Builder(loginActivity.this)
                                    .setTitle("Fail to log in")
                                    .setMessage("존재하지 않는 이메일입니다!\n새로 가입하시거나 다시 입력해주세요.")
                                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dlg, int sumthin) {
                                            emailInput.getText().clear();
                                            pwInput.getText().clear();
                                            return;
                                        }
                                    }).show(); // 팝업창 보여줌
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
                params.put("email",emailInput.getText().toString());
                params.put("pwd",pwInput.getText().toString());
                Log.d("DBGLOG","return params: "+emailInput.getText().toString());
                return params;
            }
        };
        request.setShouldCache(false);
        requestQueue.add(request);
    }

    public void showMainActivity(){
        finish();
        Intent intent = new Intent(getApplicationContext(), mainActivity.class);
        intent.putExtra("user_id",emailInput.getText().toString());
        startActivity(intent);
    }

    public void showSignUpActivity(){
        Intent intent = new Intent(getApplicationContext(),signupActivity.class);
        startActivityForResult(intent,101);
    }

    //inputCheck 함수 : 입력되어야 하는 내용이 다 입력됐는지 검사
    public boolean inputCheck(){
        if(emailInput.getText().toString().length()==0){
            Toast.makeText(loginActivity.this,"메일 주소를 입력하세요!", Toast.LENGTH_LONG).show();
            emailInput.requestFocus();
            return false;
        }

        if(pwInput.getText().toString().length()==0){
            Toast.makeText(loginActivity.this,"비밀번호를 입력하세요!", Toast.LENGTH_LONG).show();
            pwInput.requestFocus();
            return false;
        }

        return true;
    }
}
