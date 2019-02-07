package org.grd_p.grd_project;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class loginActivity extends AppCompatActivity {
    EditText emailInput;
    EditText pwInput;
    Button signupB;
    CheckBox remember_login; //나중에 코드 추가
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pwInput = (EditText)findViewById(R.id.passwordInput);
        //비밀번호 형식으로 뜨도록 설정
        pwInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        signupB = (Button)findViewById(R.id.signupButton);
        //회원가입 버튼 클릭
        signupB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignUpActivity();
            }
        }); //signUpActivity로 이동

        Button loginB = (Button)findViewById(R.id.loginButton);
        //로그인 버튼 클릭
        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                boolean check = inputCheck();
                if (!check) {
                    return;
                }
                // * 서버 통해 로그인 하는 코드 들어가야 됨 *
                */
                showAddinfoActivity(); //addInfoActivity로 이동
            }
        });
    }
    public void showAddinfoActivity(){
        Intent intent = new Intent(getApplicationContext(),addinfoActivity.class);
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
