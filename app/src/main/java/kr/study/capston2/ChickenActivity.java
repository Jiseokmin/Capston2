package kr.study.capston2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ParseException;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ChickenActivity extends AppCompatActivity {

    private static final String tag = "registerAlarm";
    private ListView listView;
    private Button btn_create;

    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> arr_roomList = new ArrayList<>();
    private DatabaseReference reference;    //chicken 이라는 공간 생성 -> 이 안에서 채팅방 생성
    private String name;

    private String userID;
    private String what;
    //private String str_name;
    private String str_room;
    // private EditText editSearch;        // 검색어를 입력할 Input 창

    Map<String, Object> map = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("채팅방");
        setContentView(R.layout.activity_chicken);

        //로그인화면에서 닉네임을 가져옵니다.
        Intent intent = getIntent();   //Login 에서 Main,, Main에서 Chicken 으로 userID를 받아옴
        userID  = intent.getStringExtra("userID");
        what  = intent.getStringExtra("what");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        reference = FirebaseDatabase.getInstance()
                .getReference().child(what).getRef();

        listView = (ListView) findViewById(R.id.list);
        btn_create = (Button) findViewById(R.id.btn_create);


        // 채팅방 리스트를 보여줍니다
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr_roomList);
        listView.setAdapter(arrayAdapter);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {      //뒤로 가기 버튼 클릭하면 MainActivity 로 이동
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent(ChickenActivity.this,MainActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("what", what);

                ChickenActivity.this.startActivity(intent);
                finish();
            }
        });

        // 다이얼로그에서 채팅방 이름을 적어서 채팅방을 생성합니다
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                final EditText et_inDialog = new EditText(ChickenActivity.this);

                final AlertDialog.Builder builder = new AlertDialog.Builder(ChickenActivity.this);
                builder.setTitle("채팅방 이름 입력");
                builder.setView(et_inDialog);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        str_room = et_inDialog.getText().toString();
                        map.put(str_room, "");
                        reference.updateChildren(map);
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });


        // 특정 경로의 전체 내용에 대한 변경 사항을 읽고 수신 대기함
        // onDataChange는 Database가 변경되었을때 호출되고
        // onCancelled는 취소됬을때 호출됩니다
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();

                while (i.hasNext()) {
                    set.add(((DataSnapshot) i.next()).getKey());
                }

                arr_roomList.clear();
                arr_roomList.addAll(set);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override public void onCancelled(DatabaseError databaseError) {

            }
        });

        // 리스트뷰의 채팅방을 클릭했을 때 반응
        // 채팅방의 이름과 입장하는 유저의 이름을 전달
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ChickenChatActivity.class);
                intent.putExtra("room_name", ((TextView) view).getText().toString());
                intent.putExtra("userID", userID);
                intent.putExtra("what",what);
                startActivity(intent);
            }
        });

    }

    //액션버튼 메뉴 액션바에 집어 넣기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


}