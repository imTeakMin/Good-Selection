package com.example.goodselection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText id, password; // 로그인 입력필드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseAuth mFirebaseAuth=FirebaseAuth.getInstance();

        id=findViewById(R.id.et_email);
        password =findViewById(R.id.et_pwd);

        Button btn_login=findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그인 요청
                String strEmail = id.getText().toString();
                String strPwd= password.getText().toString();

                checkUserAccount(mFirebaseAuth,strEmail,strPwd);

            }
        });

        showRegisterActivity();

    }

    public void showRegisterActivity(){
        Button btn_register=findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 회원가입 화면으로 이동
                Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void checkUserAccount(FirebaseAuth mFirebaseAuth,String email,String pw){
        mFirebaseAuth.signInWithEmailAndPassword(email,pw).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // 로그인 성공
                    showMainActivity();
                }
                else
                    // 로그인 실패
                    makeLoginErrorMsg();

            }
        });
    }

    private void makeLoginErrorMsg(){
        Toast.makeText(LoginActivity.this,"로그인 실패", Toast.LENGTH_SHORT).show();
    }

    private void showMainActivity(){
        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}