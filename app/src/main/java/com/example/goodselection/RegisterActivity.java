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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText registerId;
    private EditText registerPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabaseRef= FirebaseDatabase.getInstance().getReference("UserInformation");

        registerId=findViewById(R.id.et_email);
        registerPassword=findViewById(R.id.et_pwd);
        Button Btn_RegisterOk =findViewById(R.id.btn_register);

        Btn_RegisterOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 회원가입 처리 시작
                String strEmail = registerId.getText().toString();
                String strPwd=registerPassword.getText().toString();

                // 계정 등록
                accountRegistration(mFirebaseAuth, mDatabaseRef, strEmail, strPwd);
            }
        });

    }

    public void accountRegistration(FirebaseAuth mFirebaseAuth, DatabaseReference mDatabaseRef,String email, String pw){
        mFirebaseAuth.createUserWithEmailAndPassword(email,pw).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                    UserAccount user=new UserAccount();
                    user.setIdToken(firebaseUser.getUid());
                    user.setEmailId(firebaseUser.getEmail());
                    user.setPassword(pw);
                    user.setPoint(0);
                    user.setGoods();

                    // firebase에 계정 추가 후 업데이트
                    mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(user);

                    makeRegisterSuccessMsg();

                    showLoginActivity();
                }
                else
                    makeRegisterErrorMsg();
            }
        });
    }

    private void makeRegisterErrorMsg(){
        Toast.makeText(RegisterActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
    }

    private void makeRegisterSuccessMsg(){
        Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
    }

    private void showLoginActivity(){
        Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
