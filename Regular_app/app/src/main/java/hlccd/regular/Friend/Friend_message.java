package hlccd.regular.Friend;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import hlccd.regular.API.IM;
import hlccd.regular.Homepage;
import hlccd.regular.R;
import hlccd.regular.User.User;


public class Friend_message extends Fragment {
    private View                        view;
    private SwipeRefreshLayout          SR;
    private RecyclerView                RV;
    private List<Friend_message_data>   messages = new ArrayList<>();
    private LinearLayoutManager         layoutManager;
    private Friend_message_data_adapter adapter;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        SR        = view.findViewById(R.id.SR);
        RV        = view.findViewById(R.id.friend_message_RV);
        SR.setColorSchemeResources(R.color.mainColor);
        SR.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable(){
                    public void run(){
                        initialize();
                        SR.setRefreshing(false);
                    }
                },1000);
            }
        });
        initialize();
        //监听服务器获取消息
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (IM.getReader() == null) {
                        continue;
                    }
                    String received = null;
                    try {
                        received = IM.getReader().readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (received == null) {
                        break;
                    } else {
                        try {
                            if (received.charAt(0) != '[') {
                                received = "[" + received + "]";
                            }
                            JSONArray array = new JSONArray(received);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject json      = array.getJSONObject(i);
                                String     mes       = json.optString("mes");
                                int        type      = json.optInt("type");
                                Long       id        = json.optLong("id");
                                Long       receiver  = json.optLong("receiver");
                                Long       group     = json.getLong("group");
                                Long       timestamp = json.getLong("timestamp");
                                success(mes, type, id, receiver, group, timestamp);
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    initialize();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    IM.getWriter().close();
                    IM.getReader().close();
                    IM.getSocket().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void success(String mes, int type, Long id, Long receiver, Long group, Long timestamp) {
        switch (type) {
            case 1:
                saveChatData(mes, id, receiver, group, timestamp);
                break;
            case 2:
                saveGroupData(mes, id, receiver, group, timestamp);
                break;
            case 3:
                saveInfo(3, id, group, receiver, timestamp);
                break;
            case 4:
                saveInfo(4, id, group, receiver, timestamp);
                break;
            case 5:
                saveInfo(5, id, group, receiver, timestamp);
                new Friend_friend_data().refresh();
                break;
            case 6:
                saveInfo(6, id, group, receiver, timestamp);
                new Friend_group_data().refresh();
                break;
            case 7:
                saveInfo(7, id, group, receiver, timestamp);
                break;
            case 8:
                saveInfo(8, id, group, receiver, timestamp);
                break;
        }
    }

    private void saveChatData(String mes, Long id, Long receiver, Long group, Long timestamp) {
        //保存为chat数据
        Friend_chat_data chat = new Friend_chat_data();
        chat.setPortrait(R.mipmap.my_falls_blue);
        chat.setSender(id);
        chat.setName(id + "");
        chat.setGroup(0L);
        chat.setMaster(receiver);
        chat.setBelong(id);
        chat.setMessage(mes);
        chat.setTimestamp(timestamp);
        chat.save();

        Boolean             b       = true;
        Friend_message_data message = null;
        for (int j = 0; j < messages.size(); j++) {
            if (messages.get(j).getSender().equals(id)) {
                message = messages.get(j);
                b       = false;
                break;
            }
        }
        if (b) {
            message = new Friend_message_data();
        }
        message.setMaster(receiver);
        message.setMes(mes);
        message.setName(id + "");
        message.setPortrait(R.mipmap.my_falls_blue);
        message.setSender(id);
        message.setGroup(0L);
        message.setTimestamp(timestamp);
        message.save();
        messages.add(message);
    }

    private void saveGroupData(String mes, Long id, Long receiver, Long group, Long timestamp) {
        //保存为chat数据
        Friend_chat_data chat = new Friend_chat_data();
        chat.setPortrait(R.mipmap.my_falls_blue);
        chat.setSender(id);
        chat.setName(id + "");
        chat.setGroup(group);
        chat.setMaster(receiver);
        chat.setBelong(group);
        chat.setMessage(mes);
        chat.setTimestamp(timestamp);
        chat.save();

        Boolean             b       = true;
        Friend_message_data message = null;
        for (int j = 0; j < messages.size(); j++) {
            if (messages.get(j).getGroup().equals(group)) {
                message = messages.get(j);
                b       = false;
                break;
            }
        }
        if (b) {
            message = new Friend_message_data();
        }
        message.setMaster(receiver);
        message.setMes(mes);
        message.setName(group + "");
        message.setPortrait(R.mipmap.my_falls_blue);
        message.setSender(0L);
        message.setGroup(group);
        message.setTimestamp(timestamp);
        message.save();
        messages.add(message);

    }

    private void saveInfo(int type, Long sender, Long group, Long master, Long timestamp) {
        Friend_info_data info = new Friend_info_data();
        info.setPortrait(R.mipmap.my_falls_blue);
        info.setType(type);
        info.setName(sender + "");
        info.setSender(sender);
        info.setGroup(group);
        info.setMaster(master);
        info.setTimestamp(timestamp);
        info.setDispose(false);
        info.save();
    }

    private void initialize() {
        messages = LitePal.where("master=?", User.getUserId() + "").find(Friend_message_data.class);
        Map<Long, Friend_message_data> userMap  = new HashMap<Long, Friend_message_data>();
        Map<Long, Friend_message_data> groupMap = new HashMap<Long, Friend_message_data>();
        boolean                        b        = false;
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getGroup() == 0) {
                if (userMap.get(messages.get(i).getSender()) == null) {
                    userMap.put(messages.get(i).getSender(), messages.get(i));
                } else {
                    messages.get(i).delete();
                    b = true;
                }
            } else {
                if (groupMap.get(messages.get(i).getGroup()) == null) {
                    groupMap.put(messages.get(i).getGroup(), messages.get(i));
                } else {
                    messages.get(i).delete();
                    b = true;
                }
            }
        }
        if (b) {
            messages = LitePal.where("master=?", User.getUserId() + "").find(Friend_message_data.class);
        }
        Collections.sort(messages);
        layoutManager = new LinearLayoutManager(getContext());
        RV.setLayoutManager(layoutManager);
        adapter = new Friend_message_data_adapter((Homepage) getActivity(), messages);
        RV.setAdapter(adapter);

        getActivity().sendBroadcast(new Intent("chat"));
        getActivity().sendBroadcast(new Intent("info"));
    }

    @Override
    public void onResume() {
        super.onResume();
        initialize();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.friend_message, container, false);
    }
}