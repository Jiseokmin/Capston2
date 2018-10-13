package kr.study.capston2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AuthActivity extends AppCompatActivity {

    String ran_num;
    String mailid;

    private int count = 180;
    private static final int MILLISINFUTURE = 180 * 1000;
    private static final int COUNT_DOWN_INTERVAL = 1000;
    private CountDownTimer countDownTimer;
    private TextView countTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Intent intent = new Intent(this.getIntent());
        ran_num = intent.getStringExtra("ran_num");
        mailid = intent.getStringExtra("mail");

        countTxt = (TextView) findViewById(R.id.timeCount);
        countDownTimer();
        countDownTimer.start();


        Button btn = (Button) findViewById(R.id.bt_ok);
        final EditText editText = (EditText) findViewById(R.id.et_rannum);


        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String num = editText.getText().toString();      //현재 엑티비티에서 입력한 문자

                if (num.equals(ran_num)) {

                    Intent intent = new Intent(AuthActivity.this, RegisterActivity.class);
                    //intent.putExtra("mail",mailid);

                    startActivity(intent);
                    finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AuthActivity.this);
                    builder.setMessage("문자가 일치하지 않습니다!")
                            .setNegativeButton("다시 시도", null)
                            .create()
                            .show();
                }

            }
        });
    }

    public void countDownTimer() {

        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                long emailAuthCount = millisUntilFinished / 1000;
                Log.d("Alex", emailAuthCount + "");

                if ((emailAuthCount - ((emailAuthCount / 60) * 60)) >= 10) { //초가 10보다 크면 그냥 출력
                    countTxt.setText((emailAuthCount / 60) + " : " + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                } else { //초가 10보다 작으면 앞에 '0' 붙여서 같이 출력. ex) 02,03,04...
                    countTxt.setText((emailAuthCount / 60) + " : 0" + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                }
            }

            public void onFinish() {            //시간이 다되면 현재 activity를 종료한다.
                countTxt.setText(String.valueOf("이메일 인증 실패"));
                finish();
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            countDownTimer.cancel();
        } catch (Exception e) {}
        countDownTimer=null;
    }

}
////////////////////해야될거
////////////////////회원가입 할 때 아이디 중복 등 예외처리