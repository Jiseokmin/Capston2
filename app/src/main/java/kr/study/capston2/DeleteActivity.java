package kr.study.capston2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DeleteActivity extends AppCompatActivity {
    String userID;
    private DatabaseReference reference_user;
    private static final String TAG = "DeleteActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("회원탈퇴");
        setContentView(R.layout.activity_delete);

        reference_user = FirebaseDatabase.getInstance()
                .getReference();

        final Button deleteButton = (Button) findViewById(R.id.btn_delete);


        Intent intent = getIntent();   //Login 에서 Main,, Main에서 Chicken 으로 userID를 받아옴
        userID  = intent.getStringExtra("userID");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                         try {
                             JSONObject jsonResponse = new JSONObject(response);
                             boolean success = jsonResponse.getBoolean("success");
                             if(success) {
                                 Intent intent = new Intent(DeleteActivity.this, LoginActivity.class);
                                 intent.putExtra("deleteUserID", userID);
                                 Toast.makeText(getApplicationContext(), "회원 탈퇴가 완료 되었습니다.", Toast.LENGTH_LONG).show();
                                 SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                                 SharedPreferences.Editor editor = auto.edit();
                                 //editor.clear()는 auto에 들어있는 모든 정보를 기기에서 지웁니다.
                                 editor.clear();
                                 editor.commit();

                                 final DatabaseReference reference_delete = FirebaseDatabase.getInstance().getReference().child("users").child(userID).getRef();

                                 reference_delete.removeValue();


                                 startActivity(intent);
                                 finish();
                             }
                         }
                         catch (Exception e) {
                             e.printStackTrace();
                         }
                    }
                };
                DeleteRequest deleteRequest = new DeleteRequest(userID,responseListener);
                RequestQueue queue = Volley.newRequestQueue(DeleteActivity.this);
                queue.add(deleteRequest);

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {      //뒤로 가기 버튼 클릭하면 MainActivity 로 이동
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent(DeleteActivity.this,MainActivity.class);
                intent.putExtra("userID", userID);

                DeleteActivity.this.startActivity(intent);
                finish();
            }
        });
    }

}
