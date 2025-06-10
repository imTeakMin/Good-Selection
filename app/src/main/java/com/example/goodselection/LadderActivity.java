package com.example.goodselection;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class LadderActivity extends AppCompatActivity {

    private String[] departureTexts;
    private String[] arrivalTexts;
    private int numColumns;
    private int MAX_HORIZONTAL_LINES = 14;
    private ArrayList<TextView> animateTextViews = new ArrayList<>();
    private ArrayList<Horizon> horizon = new ArrayList<>();
    private ArrayList<Horizon> resultForHorizon = new ArrayList<>();
    private FrameLayout frameLayout;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        departureTexts = intent.getStringArrayExtra("departureTexts");
        arrivalTexts = intent.getStringArrayExtra("arrivalTexts");
        numColumns = departureTexts.length;
        setContentView(R.layout.activity_ladder);
        frameLayout = findViewById(R.id.frameLayout);

        switch(numColumns){
            case 2:
                MAX_HORIZONTAL_LINES=4;
                break;
            case 3:
                MAX_HORIZONTAL_LINES=6;
                break;
            case 4:
                MAX_HORIZONTAL_LINES=8;
                break;
            case 5:
                MAX_HORIZONTAL_LINES=10;
                break;
            case 6:
                MAX_HORIZONTAL_LINES=12;
                break;
            case 7:
                MAX_HORIZONTAL_LINES=14;
        }

        LadderView ladderView = new LadderView(this);
        // ladderView onDraw 실행
        frameLayout.addView(ladderView);


        frameLayout.post(() -> {
            int width = frameLayout.getWidth();
            int height= frameLayout.getHeight();
            int colSpacing = width / (numColumns + 1);
            int rowSpacing = height / 12;
            for (int i = 0; i < numColumns; i++) {
                TextView tv = new TextView(this);
                tv.setText(departureTexts[i]);
                tv.setTextSize(20);
                tv.setX((i + 1) * colSpacing-10);
                tv.setY(rowSpacing-80);
                tv.setLayoutParams(new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                frameLayout.addView(tv);
                animateTextViews.add(tv);
            }
            ladderView.showAnimation();

            Button resultButton = new Button(this);
            resultButton.setText("결과 보기");

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            params.bottomMargin = 20;

            resultButton.setLayoutParams(params);

            resultButton.setOnClickListener(v -> {
                showLadderResultActivity();
            });

            // 애니메이션 후 버튼 추가
            frameLayout.postDelayed(() -> frameLayout.addView(resultButton), 1000);
        });


    }

    public void showLadderResultActivity(){
        Intent resultIntent=new Intent(LadderActivity.this, LadderResultActivity.class);
        resultIntent.putExtra("resultForHorizon",resultForHorizon);
        resultIntent.putExtra("departureTexts", departureTexts);
        resultIntent.putExtra("arrivalTexts", arrivalTexts);
        startActivity(resultIntent);
    }

    private class LadderView extends View {
        private Paint paint;
        private ArrayList<Integer> horizontalLines;
        public LadderView(LadderActivity context) {
            super(context);
            paint = new Paint();
            horizontalLines = new ArrayList<>();
            generateRandomLines();
        }

        private void generateRandomLines() {
            Random random=new Random();

            for (int i = 0; i < MAX_HORIZONTAL_LINES; i++) {
                int col = random.nextInt(numColumns - 1); // 이웃한 세로줄 사이에서만 생성
                horizontalLines.add(col);
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);

            Random random=new Random();

            int width = getWidth();
            int height = getHeight();
            int colSpacing = width / (numColumns + 1);
            int rowSpacing = height / 12;

            // 세로줄 그리기
            for (int i = 0; i < numColumns; i++) {
                int x = colSpacing * (i + 1);
                canvas.drawLine(x, rowSpacing, x, height - rowSpacing, paint);

                // 도착 지점 TextView 화면에 그리기
                paint.setTextSize(40);
                canvas.drawText(arrivalTexts[i], x-10, height - 150, paint);
            }

            int y=0;
            // 가로줄 그리기
            for (int line : horizontalLines) {
                int x1 = colSpacing * (line + 1);
                int x2 = colSpacing * (line + 2);
                boolean flag=false;
                while(!flag){
                    y = random.nextInt(height);
                    if(y>2*rowSpacing && y<height-2*rowSpacing){
                        if(horizon.size()!=0){
                            for(int i=0;i<horizon.size();i++){
                                if(Math.abs(horizon.get(i).getyPosition()-y)<2 &&
                                        (horizon.get(i).getFirstColumn()==x1 || horizon.get(i).getSecondColumn()==x1)){
                                    flag=false;
                                    break;
                                }
                                else
                                    flag=true;
                            }
                        }
                        else {
                            flag=true;
                            break;
                        }
                    }
                }
                resultForHorizon.add(new Horizon(line+1,line+2,y));
                horizon.add(new Horizon(x1,x2,y));
                canvas.drawLine(x1, y, x2, y, paint);
            }
            Collections.sort(resultForHorizon);
            Collections.sort(horizon);

        }
        public void showAnimation() {
            for (int i = 0; i < animateTextViews.size(); i++) {
                TextView tv = animateTextViews.get(i);
                moveTextView(tv, i);
            }
        }
        private void moveTextView(TextView textView, int colIndex) {

            int width = frameLayout.getWidth();
            int colSpacing = width / (numColumns + 1);

            int startX = (colIndex + 1) * colSpacing;
            int currentX = startX;
            int currentY = 50;
            int height = frameLayout.getHeight();

            ArrayList<Animator> animators = new ArrayList<>();
            int prevY = currentY;

            // 애니메이션 추가
            for (Horizon h : horizon) {
                if (h.yPosition <= prevY) continue;

                animators.add(ObjectAnimator.ofFloat(textView, "y", prevY, h.yPosition));
                prevY = h.yPosition;

                if (Math.abs(currentX - h.firstColumn) < 10) {
                    currentX = h.secondColumn;
                    animators.add(ObjectAnimator.ofFloat(textView, "x", h.firstColumn, h.secondColumn));
                } else if (Math.abs(currentX - h.secondColumn) < 10) {
                    currentX = h.firstColumn;
                    animators.add(ObjectAnimator.ofFloat(textView, "x", h.secondColumn, h.firstColumn));
                }
            }

            animators.add(ObjectAnimator.ofFloat(textView, "y", prevY, height - 200));

            AnimatorSet set = new AnimatorSet();
            set.playSequentially(animators);
            set.setDuration(300);
            set.start();

        }


    }

}
