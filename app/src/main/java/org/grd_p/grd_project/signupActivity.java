package org.grd_p.grd_project;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class signupActivity extends AppCompatActivity {
    private EditText nameInput;
    private EditText pwInput;
    private EditText cfPwInput;
    private EditText serialnumber;
    private EditText emailInput;

    private Activity activity;
    AlertDialog failAlert; //회원가입 실패 시 뜰 알림창


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        activity = this;

        nameInput = (EditText)findViewById(R.id.nameInput);
        pwInput = (EditText)findViewById(R.id.pwInput);
        //비밀번호 입력 창 -> 비밀번호 형식으로 문자 뜨도록
        pwInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        cfPwInput = (EditText)findViewById(R.id.cfPwInput);
        //비밀번호 확인 창 -> 비밀번호 형식으로 문자 뜨도록
        cfPwInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        serialnumber = (EditText)findViewById(R.id.serialnumber);
        emailInput = (EditText)findViewById(R.id.emailInput);

        Button signupButton = (Button)findViewById(R.id.signupButton);
        //회원가입 버튼 눌렀을 때
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = inputCheck(); //inputcheck 함수 호출
                if (!check) {
                    return;
                }

                // 다이얼로그 바디
                final AlertDialog.Builder alertdialog = new AlertDialog.Builder(activity);

                // 메인 다이얼로그 생성
                final AlertDialog alert = alertdialog.create();
                // 다이얼로그 메세지
                alertdialog.setMessage("Name: "+nameInput.getText().toString()+
                        "\nE-mail: "+emailInput.getText().toString());

                // 팝업의 확인버튼
                alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // SignUp(); //실패 시 알림 창 띄움
                        // 이메일 중복확인 내용은 나중에 추가
                        if(!failAlert.isShowing()) {
                            new AlertDialog.Builder(signupActivity.this)
                                    .setMessage("회원가입이 완료되었습니다!")
                                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dlg, int sumthin) {
                                            finish(); //로그인 액티비티로 돌아가기
                                        }
                                    }).show(); // 팝업창 보여줌
                        }else{
                            alert.dismiss();
                        }

                    }
                });

                // 팝업의 취소버튼
                alertdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                // 타이틀
                alert.setTitle("입력하신 내용이 맞습니까?");
                // 다이얼로그 보기
                alert.show();

            }
        });
        //비밀번호 일치 검사
        pwInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    pwInput.setTextColor(Color.rgb(158,158,158));
                    cfPwInput.setTextColor(Color.rgb(158,158,158)); //기본색 표시
                }else{
                    cfPwInput.getText().clear(); //pw창 수정되면 pw확인창 초기화시킴
                }
            }
        });

        //비밀번호 일치 검사
        cfPwInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if(pwInput.getText().toString().equals(cfPwInput.getText().toString())){
                    pwInput.setTextColor(Color.rgb(113,224,174));
                    cfPwInput.setTextColor(Color.rgb(113,224,174)); //초록색 표시
                }else{
                    if(cfPwInput.getText().toString().length()==0){
                        pwInput.setTextColor(Color.rgb(158,158,158));
                        cfPwInput.setTextColor(Color.rgb(158,158,158)); //기본색 표시
                    }else {
                        pwInput.setTextColor(Color.rgb(147, 59, 45));
                        cfPwInput.setTextColor(Color.rgb(147, 59, 45)); //빨간색 표시
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                }
            });

        TextView back_to_login = (TextView)findViewById(R.id.login_click);
        //log in text 눌렀을 때
        back_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }); //로그인 액티비티로 돌아가기
    }

    public void SignUp(){
        String type="signUp";
        String name = nameInput.getText().toString();
        String pw = pwInput.getText().toString();
        String serialNum = serialnumber.getText().toString();
        String email = emailInput.getText().toString();

        SignUpConnection signUpConnection = new SignUpConnection(this,failAlert);
        signUpConnection.execute(type,name,pw,serialNum,email);

    }

    //inputCheck 함수 : 입력되어야 하는 내용이 다 입력됐는지 검사
    public boolean inputCheck(){
        if(nameInput.getText().toString().length()==0){
            Toast.makeText(signupActivity.this,"이름을 입력하세요!", Toast.LENGTH_LONG).show();
            nameInput.requestFocus();
            return false;
        }

        if(pwInput.getText().toString().length()==0){
            Toast.makeText(signupActivity.this,"비밀번호를 입력하세요!", Toast.LENGTH_LONG).show();
            pwInput.requestFocus();
            return false;
        }
        if(cfPwInput.getText().toString().length()==0){
            Toast.makeText(signupActivity.this,"비밀번호를 다시 한 번 입력하세요!", Toast.LENGTH_LONG).show();
            cfPwInput.requestFocus();
            return false;
        }
        if(serialnumber.getText().toString().length()==0){
            Toast.makeText(signupActivity.this,"시리얼번호를 입력하세요!", Toast.LENGTH_LONG).show();
            serialnumber.requestFocus();
            return false;
        }

        if(emailInput.getText().toString().length()==0){
            Toast.makeText(signupActivity.this,"메일 주소를 입력하세요!", Toast.LENGTH_LONG).show();
            emailInput.requestFocus();
            return false;
        }
        if(!cfPwInput.getText().toString().equals(pwInput.getText().toString())){
            Toast.makeText(signupActivity.this,"비밀번호가 일치하지 않습니다!", Toast.LENGTH_LONG).show();
            cfPwInput.requestFocus();
            return false;
        }
        return true;
    }


}
