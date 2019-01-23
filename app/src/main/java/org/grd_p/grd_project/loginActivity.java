package org.grd_p.grd_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class loginActivity extends AppCompatActivity {

    Button signupB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText pwInput = (EditText)findViewById(R.id.passwordInput);
        pwInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        signupB = (Button)findViewById(R.id.signupButton);
        signupB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignUpActivity();
            }
        });
    }

    public void showSignUpActivity(){
        Intent intent = new Intent(getApplicationContext(),signupActivity.class);
        startActivityForResult(intent,101);
    }
}
