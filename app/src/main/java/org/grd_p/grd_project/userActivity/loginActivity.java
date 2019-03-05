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

import org.grd_p.grd_project.R;
import org.grd_p.grd_project.mainActivity;
import org.grd_p.grd_project.userConnection.LoginConnection;

public class loginActivity extends AppCompatActivity {
    EditText emailInput;
    EditText pwInput;
    Button signupB;
    CheckBox remember_login; //나중에 코드 추가
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = (EditText)findViewById(R.id.emailInput);

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
                boolean check = inputCheck();
                if (!check) {
                    return;
                }
                String reply = OnLogin();
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
                }else if(reply.equals("non_email")){
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
        });
    }
    public String OnLogin(){
        String result="fail";
        String type="login";
        String email = emailInput.getText().toString();
        String pw = pwInput.getText().toString();
        LoginConnection loginConnection = new LoginConnection(this);
        try {
            result = loginConnection.execute(type, email, pw).get();
            return result;
        }catch (Exception e){
            Log.d("DBGLOG","Exception in LoginConnection.execute");
            e.printStackTrace();
        }
        return result;
    }

    public void showMainActivity(){
        Intent intent = new Intent(getApplicationContext(), mainActivity.class);
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
