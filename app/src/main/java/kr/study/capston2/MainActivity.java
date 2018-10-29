package kr.study.capston2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton imagebtn_chicken = (ImageButton) findViewById(R.id.chicken);


        Intent intent = getIntent();
        userID  = intent.getStringExtra("userID");

        imagebtn_chicken.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent  = new Intent(MainActivity.this,ChickenActivity.class);


                intent.putExtra("userID",userID);

                MainActivity.this.startActivity(intent);
                finish();
            }
        });
    }


    }

