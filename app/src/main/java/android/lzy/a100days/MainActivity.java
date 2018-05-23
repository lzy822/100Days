package android.lzy.a100days;

import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button timeBt;
    private int goal;
    private int current;
    private int current1;
    private TextView timeShow;

    private CountDownTimer timer = new CountDownTimer(90000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            timeShow.setText((millisUntilFinished / 1000) + "秒");
        }

        @Override
        public void onFinish() {
            timeShow.setVisibility(View.GONE);
            timeBt.setClickable(true);
            current1 = 12;
            timeBt.setText(Integer.toString(current1));
        }
    };

    private String getTime(){
        Calendar calendar = Calendar.getInstance();
        return Integer.toString(calendar.get(Calendar.YEAR)) + Integer.toString(calendar.MONTH) + Integer.toString(calendar.DAY_OF_MONTH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LitePal.getDatabase();
        timeShow = (TextView) findViewById(R.id.timeShow);
        goal = 48;
        current = 0;
        current1 = 12;
        timeBt = (Button) findViewById(R.id.time);
        timeBt.setText(Integer.toString(current1));
        timeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current1 > 1) {
                    current1--;
                    current++;
                    timeBt.setText(Integer.toString(current1));
                }else {
                    timeBt.setClickable(false);
                    timeBt.setText(Integer.toString(current1 - 1));
                    current++;
                    Log.w(TAG, "onClick: " + current + " ; " + goal);
                    if (current == goal){
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setTitle("提示");
                        dialog.setMessage("已经完成当日目标!");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("打卡!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                List<impulse> impulses = DataSupport.findAll(impulse.class);
                                String time = getTime();
                                if (impulses.size() != 0) {
                                    if (!impulses.get(0).getLastDay().equals(time)) {
                                        impulse impulse = new impulse();
                                        impulse.setLastDay(time);
                                        impulse.setTime(impulses.get(0).getTime() + 1);
                                        impulse.update(1);
                                    }else Toast.makeText(MainActivity.this, "今天已经打过卡了!", Toast.LENGTH_LONG).show();
                                }else {
                                    impulse impulse = new impulse();
                                    impulse.setLastDay(time);
                                    impulse.setTime(1);
                                    impulse.save();
                                }
                            }
                        });
                        dialog.setNegativeButton("就不打卡!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {List<impulse> impulses = DataSupport.findAll(impulse.class);
                                String time = getTime();
                                if (impulses.size() != 0) {
                                    if (!impulses.get(0).getLastDay().equals(time)) {
                                        impulse impulse = new impulse();
                                        impulse.setLastDay(time);
                                        impulse.setTime(impulses.get(0).getTime() + 1);
                                        impulse.update(1);
                                    }
                                }else {
                                    impulse impulse = new impulse();
                                    impulse.setLastDay(time);
                                    impulse.setTime(1);
                                    impulse.save();
                                }
                                Toast.makeText(MainActivity.this, "不打也得打!", Toast.LENGTH_LONG).show();
                            }
                        });
                        dialog.show();
                    }else {
                        timeShow.setVisibility(View.VISIBLE);
                        timeBt.setText(Integer.toString(0));
                        timeBt.setClickable(false);
                        timer.start();
                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("提示");
        final List<impulse> impulses = DataSupport.findAll(impulse.class);
        if (impulses.get(0).getTime() >= 100) dialog.setMessage("打卡皇帝!" + "\n" + "您一共打了: " + Integer.toString(impulses.get(0).getTime()) + "次卡!" + "\n" + "再接再厉!");
        else dialog.setMessage("你一共打了: " + Integer.toString(impulses.get(0).getTime()) + "次卡!" + "\n" + "再接再厉!");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.super.onBackPressed();
            }
        });
        dialog.show();
    }
}
