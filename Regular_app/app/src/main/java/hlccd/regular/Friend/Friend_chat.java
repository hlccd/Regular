package hlccd.regular.Friend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import hlccd.regular.API.IM;
import hlccd.regular.R;
import hlccd.regular.User.User;

public class Friend_chat extends AppCompatActivity {
    private List<Friend_chat_data>   chats = new ArrayList<>();
    private LinearLayoutManager      layoutManager;
    private Friend_chat_data_adapter adapter;
    private String                   user;
    private String                   group;

    private TextView     title;
    private RecyclerView RV;
    private EditText     edit;
    private TextView     send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_chat);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("chat");
        registerReceiver(new broadcastReceiver(), intentFilter);
        Intent intent = getIntent();
        user  = ((Intent) intent).getStringExtra("Friend_user_chat");
        group = ((Intent) intent).getStringExtra("Friend_group_chat");
        title = findViewById(R.id.friend_chat_title);
        RV    = findViewById(R.id.friend_chat_RV);
        edit  = findViewById(R.id.friend_chat_edit);
        send  = findViewById(R.id.friend_chat_send);
        initialize();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mes = edit.getText().toString();
                if (mes.equals("")) {
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject json = new JSONObject();
                        try {
                            json.put("mes", mes);
                            json.put("id", User.getUserId());
                            if (Long.parseLong(group) == 0) {
                                json.put("type", 1);
                                json.put("receiver", Long.valueOf(user));
                                json.put("group", 0);
                            } else {
                                json.put("type", 2);
                                json.put("receiver", 0);
                                json.put("group", Long.valueOf(group));
                            }
                            json.put("timestamp", System.currentTimeMillis());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (IM.getSocket().isClosed()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Friend_chat.this, "发送失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            IM.getWriter().println(json);
                            IM.getWriter().flush();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Friend_chat_data chat = new Friend_chat_data();
                                    chat.setPortrait(R.mipmap.my_falls_blue);
                                    chat.setSender(User.getUserId());
                                    chat.setName(User.getUserName());
                                    chat.setGroup(Long.valueOf(group));
                                    chat.setMaster(User.getUserId());
                                    chat.setBelong(Long.valueOf(user));
                                    chat.setMessage(mes);
                                    chat.setTimestamp(System.currentTimeMillis());
                                    chat.save();
                                    initialize();
                                    edit.setText("");

                                    List<Friend_message_data> messages = LitePal.where("master=?", User.getUserId() + "").find(Friend_message_data.class);
                                    boolean                   b        = true;
                                    Friend_message_data       message  = null;
                                    for (int j = 0; j < messages.size(); j++) {
                                        if (Objects.equals(messages.get(j).getSender(), Long.valueOf(user))) {
                                            message = messages.get(j);
                                            b       = false;
                                            break;
                                        } else if (Objects.equals(messages.get(j).getGroup(), Long.valueOf(group))) {
                                            message = messages.get(j);
                                            b       = false;
                                            break;
                                        }
                                    }
                                    if (b) {
                                        message = new Friend_message_data();
                                    }
                                    message.setMaster(User.getUserId());
                                    message.setMes(mes);
                                    if (Long.parseLong(group) == 0) {
                                        message.setName(user);
                                    } else {
                                        message.setName(group);
                                    }
                                    message.setPortrait(R.mipmap.my_falls_blue);
                                    message.setSender(Long.valueOf(user));
                                    message.setGroup(Long.valueOf(group));
                                    message.setTimestamp(System.currentTimeMillis());
                                    message.save();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class broadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            initialize();
        }
    }

    private void initialize() {
        if (Long.parseLong(group) == 0) {
            title.setText(user);
            chats = LitePal.where("belong=?", user).find(Friend_chat_data.class);
        } else {
            title.setText(group);
            chats = LitePal.where("group=?", group).find(Friend_chat_data.class);
        }
        Collections.sort(chats);
        layoutManager = new LinearLayoutManager(this);
        RV.setLayoutManager(layoutManager);
        adapter = new Friend_chat_data_adapter(chats);
        RV.setAdapter(adapter);
        RV.scrollToPosition(chats.size() - 1);
    }
}