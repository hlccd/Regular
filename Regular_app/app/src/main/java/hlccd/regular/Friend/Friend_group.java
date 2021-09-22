package hlccd.regular.Friend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import hlccd.regular.API.API;
import hlccd.regular.R;
import hlccd.regular.User.User;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Friend_group extends Fragment {
    private View                      view;
    private SwipeRefreshLayout        SR;
    private ImageView                 establish;
    private RecyclerView              RV;
    private List<Friend_group_data>   groups = new ArrayList<>();
    private LinearLayoutManager       layoutManager;
    private Friend_group_data_adapter adapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        establish = view.findViewById(R.id.establish);
        SR        = view.findViewById(R.id.SR);
        RV        = view.findViewById(R.id.RV);
        SR.setColorSchemeResources(R.color.mainColor);
        SR.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Friend_group_data().refresh();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        initialize();
                        SR.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        initialize();
        establish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("请输入群昵称:");
                final EditText edit = new EditText(getContext());
                dialog.setView(edit);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = edit.getText().toString();
                        if (name.equals("")) {
                            Toast.makeText(getContext(), "未输入群名,建群取消", Toast.LENGTH_SHORT).show();
                        } else {
                            establishGroup(name);
                        }
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "建群已取消", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
            }
        });
    }

    private void initialize() {
        groups = LitePal.where("master=?", User.getUserId() + "").find(Friend_group_data.class);
        Collections.sort(groups);
        layoutManager = new LinearLayoutManager(getContext());
        RV.setLayoutManager(layoutManager);
        adapter = new Friend_group_data_adapter(groups);
        RV.setAdapter(adapter);
    }

    private void establishGroup(String name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String       url    = new API().GroupEstablishAPI() + "?name=" + name;
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("name", name)
                            .build();
                    Request request = new Request.Builder()
                            .post(requestBody)
                            .url(url)
                            .header("Authorization", User.getUserToken())
                            .build();
                    //获取响应的body内容
                    Response response = client.newCall(request).execute();
                    String   data     = response.body().string();
                    //对响应数据进行json解析,获取响应code和mes
                    try {
                        JSONArray  array = new JSONArray("[" + data + "]");
                        JSONObject json  = array.getJSONObject(0);
                        String     mes   = json.optString("message");
                        int        code  = json.optInt("code");
                        //当且仅当code为200时说明登录成功,继续对mes进行json解析获取token
                        if (code == 200) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new Friend_group_data(R.drawable.main_my, Long.valueOf(mes), name, User.getUserId()).save();
                                    new Friend_message_data(R.drawable.main_my, 0L, Long.valueOf(mes), name, "群组创建成功", User.getUserId(), System.currentTimeMillis()).save();
                                    initialize();
                                }
                            });
                        } else {
                            //当登录失败时候进行失败操作
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "建群失败,请稍后重试", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        initialize();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.friend_group, container, false);
    }
}