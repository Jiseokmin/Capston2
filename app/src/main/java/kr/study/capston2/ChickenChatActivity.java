package kr.study.capston2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ChickenChatActivity extends AppCompatActivity {
    private  String numToStr;
    private static final String TAG = "ChickenChatActivity";

    private ListView userlist;  //현재 대화방에 있는 user들 저장하기 위한 listview
    private ListView lv_chating;
    private EditText et_send;
    private Button btn_send;

    Map<String, Object> map = new HashMap<String, Object>();

    private ArrayAdapter<String> adapter;
    private ChatAdapter arrayAdapter;
    //private ArrayList<String> arr_room = new ArrayList<>();
    private ArrayList<String> user_list = new ArrayList<>();    //채팅방에 들어가는 usrID

    private String str_room_name;
    private String str_user_name;

    private DatabaseReference reference;
    private DatabaseReference reference_user;
    private DatabaseReference reference_warn;
    private DatabaseReference mDatabase;

    private String key;
    private String chat_user;
    private String chat_message;
    private String what;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chicken_chat);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout) ;

        userlist=(ListView)findViewById(R.id.drawer);
        final View header = getLayoutInflater().inflate(R.layout.listview_header, null, false) ;
        userlist.addHeaderView(header) ;


        Intent intent = getIntent();
        what  = intent.getStringExtra("what");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        et_send = (EditText) findViewById(R.id.et_send);
        lv_chating = (ListView) findViewById(R.id.lv_chating);
        btn_send = (Button) findViewById(R.id.btn_send);

        str_room_name = getIntent().getExtras().get("room_name").toString();
        str_user_name = getIntent().getExtras().get("user_name").toString();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        reference = FirebaseDatabase.getInstance().getReference().child(str_room_name);
        reference_user =  FirebaseDatabase.getInstance()
                .getReference().child(str_room_name+"user").getRef();

        setTitle(str_room_name);

        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,user_list);
        userlist.setAdapter(adapter);

        arrayAdapter = new ChatAdapter(this, 0);
        lv_chating.setAdapter(arrayAdapter);

        // 리스트뷰가 갱신될때 하단으로 자동 스크롤
        lv_chating.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        map.put(str_user_name, "");
        reference_user.updateChildren(map);

        reference_warn = FirebaseDatabase.getInstance().getReference();


        arrayAdapter.setName(str_user_name);    //자기 인지 확인해서 채팅방에 적용
        arrayAdapter.notifyDataSetChanged();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {      //뒤로 가기 버튼 클릭하면 ChickenActivity 로 이동
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent(ChickenChatActivity.this,ChickenActivity.class);
                intent.putExtra("userID", str_user_name);
                intent.putExtra("what", what);

                ChickenChatActivity.this.startActivity(intent);
                finish();
            }
        });

        userlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView parent, View v, final int position, long id) {
                final String selected_item = (String)parent.getItemAtPosition(position);
                new AlertDialog.Builder(ChickenChatActivity.this)

                        .setTitle("당신과의 약속장소에 나오지 않았나요??")
                        .setMessage("'신고하기'를 클릭할 시 신고가 접수 됩니다.\n허위 신고일 경우 제재가 있을 수 있습니다.")

                        .setNeutralButton("신고하기", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dlg, int sumthin) {



                                final DatabaseReference reference_warnRef = reference_warn.child("users").child(selected_item).child("point");
                                reference_warnRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Map<String, Object> taskMap = new HashMap<String, Object>();
                                        String value = dataSnapshot.getValue(String.class); //firebase 에서 DB 가져옴
                                        int numInt = Integer.parseInt(value) - 5;           //포인트 5점 까지
                                        numToStr = String.valueOf(numInt);            //다시 문자열로 변환
                                        //    taskMap.put("point", numToStr);

                                        writeNewUser(selected_item,numToStr);

                                        Log.d(TAG, "Value is: " + numToStr);

                                        Toast.makeText(getApplicationContext(), "신고가 접수되었습니다", Toast.LENGTH_LONG).show();

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                        // Failed to read value
                                        Log.w(TAG, "Failed to read value.", error.toException());
                                    }
                                });


                            }
                        })

                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 다이얼로그를 취소한다
                                dialog.cancel();
                            }
                        })

                        .show(); // 팝업창 보여줌


            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {

                // map을 사용해 name과 메시지를 가져오고, key에 값 요청
                Map<String, Object> map = new HashMap<String, Object>();
                key = reference.push().getKey();
                reference.updateChildren(map);

                DatabaseReference root = reference.child(key);

                // updateChildren를 호출하여 database 최종 업데이트
                Map<String, Object> objectMap = new HashMap<String, Object>();
                objectMap.put("name", str_user_name);
                objectMap.put("message", et_send.getText().toString());

                root.updateChildren(objectMap);

                et_send.setText("");
            }
        });

        reference.addChildEventListener(new ChildEventListener() {
            @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                chatConversation(dataSnapshot);
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                chatConversation(dataSnapshot);
            }

            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override public void onCancelled(DatabaseError databaseError) {

            }
        });

        reference_user.addValueEventListener(new ValueEventListener() { //대화상대에 user들 추가
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();

                while (i.hasNext()) {

                    set.add(((DataSnapshot) i.next()).getKey());
                }

                user_list.clear();
                user_list.addAll(set);

                adapter.notifyDataSetChanged();
            }

            @Override public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //액션버튼 메뉴 액션바에 집어 넣기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout) ;
        //각각의 버튼을 클릭할때의 수행할것을 정의해 준다.
        switch (item.getItemId()){
            case R.id.navigation_drawer:    //drawer 오른쪽으로 펼치기

                if (!drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.openDrawer(Gravity.RIGHT) ;
                }

        }

        return true;
    }

    // addChildEventListener를 통해 실제 데이터베이스에 변경된 값이 있으면,
    // 화면에 보여지고 있는 Listview의 값을 갱신함
    private void chatConversation(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();
        ChatData chatData = dataSnapshot.getValue(ChatData.class);
        chatData.firebaseKey = dataSnapshot.getKey();


        while (i.hasNext()) {
            chat_message = (String) ((DataSnapshot) i.next()).getValue();
            chat_user = (String) ((DataSnapshot) i.next()).getValue();
            chatData.userName = chat_user;
            chatData.message = chat_message;

            arrayAdapter.add(chatData);
        }

        arrayAdapter.notifyDataSetChanged();
    }


    private void writeNewUser(String userId, String point) {
        User user = new User(point);

        mDatabase.child("users").child(userId).setValue(user);
    }

}
