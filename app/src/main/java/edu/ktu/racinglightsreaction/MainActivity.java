package edu.ktu.racinglightsreaction;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ImageView lights;
    private TextView reactionTime,resultText;
    private Button tryAgain;
    private boolean ready=false,go=false,falseStart=false,played=false;

    Handler customHandler = new Handler();

    long startTime=0L, timeInMillisecond=0L, timeSwapBuff=0L, updateTime=0L;

    int secs, mins,milliseconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lights=(ImageView) findViewById(R.id.lights);

        reactionTime=(TextView) findViewById(R.id.reactionTime);
        resultText=(TextView) findViewById(R.id.resultText);

        tryAgain=(Button) findViewById(R.id.tryAgain);
        tryAgain.setVisibility(View.INVISIBLE);
    }

    Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMillisecond=SystemClock.uptimeMillis()-startTime;
            updateTime = timeSwapBuff+timeInMillisecond;
            secs=(int) updateTime/1000;
            mins=(secs/60);
            secs%=60;
            milliseconds=(int) updateTime %1000;
            //reactionTime.setText(String.format("%2d",secs)+"."+String.format("%3d",milliseconds));
            customHandler.postDelayed(this,0);
        }
    };

    public void screenTapped(View v){
        if(ready && !go && !played){
            played=true;
            timeSwapBuff=timeInMillisecond;
            customHandler.removeCallbacks(updateTimerThread);
            resultText.setText("FALSE START");
            tryAgain.setVisibility(View.VISIBLE);

        }
        if(ready && go && !played){
            played=true;
            ready=false;
            go=false;
            reactionTime.setText(String.format("%2d",secs)+"."+String.format("%3d",milliseconds));
            timeSwapBuff=timeInMillisecond;
            customHandler.removeCallbacks(updateTimerThread);
            tryAgain.setVisibility(View.VISIBLE);


        }
        if(!ready && !go && !played){
            ready=true;

            Random r = new Random();
            final int additionalTime=r.nextInt(5000-2000) + 2000;

           // long totalTime= TimeUnit.SECONDS.toMillis(6) + TimeUnit.MILLISECONDS.toSeconds(additionalTime);
            long totalTime= TimeUnit.SECONDS.toMillis(6);

            new CountDownTimer(totalTime, 1000) {

                public void onTick(long millisUntilFinished) {
                    long seconds = millisUntilFinished / 1000;
                    long minutes = seconds / 60;
                    long hours = minutes / 60;

                    if(!falseStart){
                        if(seconds==5)
                            lights.setImageResource(R.drawable.light_1);
                        else if(seconds==4)
                            lights.setImageResource(R.drawable.light_2);
                        else if(seconds==3)
                            lights.setImageResource(R.drawable.light_3);
                        else if(seconds==2)
                            lights.setImageResource(R.drawable.light_4);
                        else if(seconds==1)
                            lights.setImageResource(R.drawable.light_5);
                    }
                }

                public void onFinish() {
                    go=true;
                    lights.setImageResource(R.drawable.lights_off);
                    startTime=SystemClock.uptimeMillis();
                    customHandler.postDelayed(updateTimerThread,0);
                }}.start();

        }

    }

    public void tryAgain(View v){
        finish();
        overridePendingTransition( 0, 0);
        startActivity(getIntent());
        overridePendingTransition( 0, 0);
    }
}


