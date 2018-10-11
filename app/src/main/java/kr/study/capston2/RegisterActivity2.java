package kr.study.capston2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("회원 가입");
        setContentView(R.layout.activity_register2);

        TextView textView1 = (TextView) findViewById(R.id.tv_email) ;
        textView1.setText("@cau.ac.kr") ;

        TextView textView2 = (TextView) findViewById(R.id.tv_announc) ;
        textView2.setText(" 인증 버튼을 누르시면 본인 메일에 인증 전용 메일에서 링크를 클릭 해주세요!") ;


        Button btn2 = (Button) findViewById(R.id.btn_confirm);
        btn2.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {



            }

        });


        Button btn = (Button) findViewById(R.id.btn_next);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                startActivity(new Intent(RegisterActivity2.this,RegisterActivity.class));
                finish();


            }

        });
    }
}
