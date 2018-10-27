package kr.study.capston2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView idText = (TextView)findViewById(R.id.idText);
        //TextView passwordText = (TextView)findViewById(R.id.passwordText);
        TextView welcomMessage =(TextView) findViewById(R.id.welcomeMessage);

        Intent intent = getIntent();
       // String userID = intent.getStringExtra("userID");
      //  String userPassword = intent.getStringExtra("userPassword");
        String message = "환영합니다";

     //   idText.setText(userID);
      //  passwordText.setText(userPassword);
        welcomMessage.setText(message);
    }
}
///////////////////해야 될거
////////////////// 자동 로그인 기능