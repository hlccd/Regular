package hlccd.regular.Friend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import hlccd.regular.API.API;
import hlccd.regular.R;
import hlccd.regular.User.User;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Friend_search extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private List<Friend_search_data>   searchs = new ArrayList<>();
    private RadioGroup                 RG;
    private EditText                   search;
    private TextView                   send;
    private RecyclerView               RV;
    private LinearLayoutManager        layoutManager;
    private Friend_search_data_adapter adapter;
    private String                     target  = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_search);
        RG     = findViewById(R.id.RG);
        search = findViewById(R.id.search);
        send   = findViewById(R.id.send);
        RV     = findViewById(R.id.RV);
        initialize();
        RG.setOnCheckedChangeListener(this);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = search.getText().toString();
                if (target.equals("user")){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //组合url进行登录的get请求
                                String       url    = new API().UserSearchAPI() + "?search=" + s;
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .get()
                                        .header("Authorization", User.getUserToken())
                                        .url(url)
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
                                        array = new JSONArray(mes);
                                        searchs.clear();
                                        for (int i = 0; i < array.length(); i++) {
                                            json = array.getJSONObject(i);
                                            Long   id   = json.optLong("id");
                                            String name = json.getString("name");
                                            searchs.add(new Friend_search_data(R.drawable.main_my, id, name,"user"));
                                        }
                                        //将token,id,password传出,本次登录成功,进行结束操作
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                initialize();
                                            }
                                        });
                                    } else {
                                        //当登录失败时候进行失败操作
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //登录失败,进行广播
                                                Toast.makeText(Friend_search.this, "搜索用户失败", Toast.LENGTH_SHORT).show();
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
                }else if (target.equals("group")){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //组合url进行登录的get请求
                                String       url    = new API().GroupListAPI() ;
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .get()
                                        .header("Authorization", User.getUserToken())
                                        .url(url)
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
                                        array = new JSONArray(mes);
                                        searchs.clear();
                                        for (int i = 0; i < array.length(); i++) {
                                            json = array.getJSONObject(i);
                                            Long   group   = json.optLong("group");
                                            String name = json.getString("name");
                                            searchs.add(new Friend_search_data(R.drawable.main_my, group, name,"group"));
                                        }
                                        //将token,id,password传出,本次登录成功,进行结束操作
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                initialize();
                                            }
                                        });
                                    } else {
                                        //当登录失败时候进行失败操作
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //登录失败,进行广播
                                                Toast.makeText(Friend_search.this, "搜索用户失败", Toast.LENGTH_SHORT).show();
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
            }
        });
    }

    private void initialize() {
        layoutManager = new LinearLayoutManager(this);
        RV.setLayoutManager(layoutManager);
        adapter = new Friend_search_data_adapter(searchs);
        RV.setAdapter(adapter);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        /**
         * 将RadioGroup的各个按钮与ViewPager2中的碎片进行相互绑定
         * 记账字界面碎片绑定0号按钮
         * 规划记录界面碎片绑定1号按钮
         * 执行计划界面碎片绑定2号按钮
         * 好友群组界面碎片绑定3号按钮
         * 个人详情界面碎片绑定4号按钮
         * 点击RadioButton时切换当前展示Fragment
         */
        switch (checkedId) {
            case R.id.user:
                search.setHint("搜索用户");
                target = "user";
                searchs.clear();
                adapter = new Friend_search_data_adapter(searchs);
                RV.setAdapter(adapter);
                break;
            case R.id.group:
                search.setHint("搜索群组");
                target = "group";
                searchs.clear();
                adapter = new Friend_search_data_adapter(searchs);
                RV.setAdapter(adapter);
                break;
        }
    }
}