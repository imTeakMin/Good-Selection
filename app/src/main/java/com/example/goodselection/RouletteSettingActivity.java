package com.example.goodselection;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RouletteSettingActivity extends AppCompatActivity {
    private EditText inputEditText;
    private Button checkButton;
    private LinearLayout middleLayout;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roulettesetting);

        inputEditText=findViewById(R.id.text_rouletteCount);
        checkButton=findViewById(R.id.btn_checkRouletteCount);
        middleLayout=findViewById(R.id.middleLayout);

        checkButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String input=inputEditText.getText().toString();
                if(!input.isEmpty()){
                    int numColumns=Integer.parseInt(input);
                    if(numColumns>8){
                        Toast.makeText(RouletteSettingActivity.this, "최대 8개 가능", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        addEditTexts(numColumns);
                    }
                }
            }
        });
    }

    private void addEditTexts(int numColumns){
        middleLayout.removeAllViews();

        for(int i=0;i<numColumns;i++){
            EditText middleEditText=new EditText(this);
            middleEditText.setHint("입력 "+(i+1));
            middleLayout.addView(middleEditText);
        }

        Button startButton = new Button(this);
        startButton.setText("게임 시작");
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 모든 EditText가 입력되었는지 확인
                boolean allFilled = checkIfAllEditTextsFilled();

                String[] inputTexts = new String[numColumns];

                for (int i = 0; i < numColumns; i++) {
                    EditText middleEditText = (EditText) middleLayout.getChildAt(i);
                    inputTexts[i] = middleEditText.getText().toString();
                }

                if (allFilled) {
                    showRouletteActivity(inputTexts);
                } else {
                    makeRouletteSettingErrorMsg();
                }
            }
        });

        // "게임 시작" 버튼을 하단에 추가
        middleLayout.addView(startButton);
    }

    private boolean checkIfAllEditTextsFilled() {
        // 상단과 하단의 EditText들이 모두 입력되었는지 확인
        for (int i = 0; i < middleLayout.getChildCount()-1; i++) {
            EditText middleEditText = (EditText) middleLayout.getChildAt(i);
            if (middleEditText.getText().toString().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void makeRouletteSettingErrorMsg(){
        Toast.makeText(RouletteSettingActivity.this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
    }

    public void showRouletteActivity(String[] inputTexts){
        Intent intent = new Intent(RouletteSettingActivity.this, RouletteActivity.class);
        intent.putExtra("middleTexts", inputTexts);
        startActivity(intent);
    }

}