package hlccd.regular.User;
/**
 * 用户登录
 * 在该界面实现用户登录,跳转至注册或找回密码
 * 对于spinner的子item监听存在问题
 *
 * @author hlccd 2020.6.7
 * @version 1.0
 */

import androidx.appcompat.app.AppCompatActivity;

import hlccd.regular.API.API;
import hlccd.regular.API.IM;
import hlccd.regular.Friend.Friend_friend_data;
import hlccd.regular.Homepage;
import hlccd.regular.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User_login extends AppCompatActivity {
    private Spinner      select;
    private EditText     idEdit;
    private EditText     passwordEdit;
    private TextView     remember;
    private boolean      isRemember;
    private TextView     login;
    private TextView     register;
    private List<String> arr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);
        select       = findViewById(R.id.user_login_select);
        idEdit       = findViewById(R.id.user_login_id);
        passwordEdit = findViewById(R.id.user_login_password);
        remember     = findViewById(R.id.user_login_remember);
        login        = findViewById(R.id.user_login);
        register     = findViewById(R.id.user_login_register);
        //获取数据库中登陆过的user的id和密码
        List<User_data> users = LitePal.findAll(User_data.class);
        //对所有获取的用户进行排序,将已登录的用户排在最前面
        Collections.sort(users);
        //将所有用户的id加入数组中以备后续spinner选择
        for (User_data data : users) {
            arr.add(data.getU_id() + "");
        }
        //spinner的适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, arr.toArray(new String[arr.size()]));
        select.setAdapter(adapter);
        //对spinner进行监听,监听其中的每一个item,存在问题
        select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idEdit.setText(users.get(position).getU_id()+"");
                passwordEdit.setText(users.get(position).getPassword());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //初始默认记住密码
        remember.setText("☑记住密码");
        isRemember = true;
        remember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击修改是否记住密码
                if (isRemember) {
                    remember.setText("▢记住密码");
                    isRemember = false;
                } else {
                    remember.setText("☑记住密码");
                    isRemember = true;
                }
            }
        });
        //点击进行登录
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取编辑框中数据,并检测是否进行的输入,若未输入则退回
                String id       = idEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                if (id.length() == 0 && password.length() == 0) {
                    Toast.makeText(User_login.this, "请输入账号密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                //新建一个线程进行网络请求
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //组合url进行登录的get请求
                            String       url    = new API().UserLoginAPI() + "?password=" + password + "&uid=" + id;
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .get()
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
                                    array = new JSONArray("[" + mes + "]");
                                    json  = array.getJSONObject(0);
                                    Long   uid   = json.optLong("uid");
                                    String token = json.getString("token");
                                    String name = json.getString("data");
                                    //将token,id,password传出,本次登录成功,进行结束操作
                                    success(token, uid, password,name);
                                } else {
                                    //当登录失败时候进行失败操作
                                    failure();
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
        });
        //跳转到注册界面
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_login.this, User_register.class));
            }
        });
    }

    private void success(String token, Long uid, String password,String name) {
        //对线程结尾进行后续操作
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //获取数据库中已有的用户进行搜索,检测该用户是否在本地登陆过,如果登录则进行覆写
                boolean         flag = true;
                List<User_data> arr  = LitePal.findAll(User_data.class);
                User_data       user = new User_data();
                for (User_data data : arr) {
                    if (data.getU_id().equals(uid)) {
                        user = data;
                        flag = false;
                    }
                    data.setState(0);
                    data.save();
                }
                if (flag) {
                    user.setU_id(uid);
                    user.setP_num((long) 0);
                    user.setNumber((long) 0);
                    user.setDuration((long) 0);
                    user.setB_num((long) 0);
                }
                user.setName(name);
                user.setState(1);
                user.setToken(token);
                //对是否记住密码进行确定
                if (isRemember) {
                    user.setPassword(password);
                } else {
                    user.setPassword("");
                }
                //覆写或新增该用户
                user.save();
                //跳转至主页
                startActivity(new Intent(User_login.this, Homepage.class));
                IM.Change();
                new Friend_friend_data().refresh();
            }
        });
    }

    private void failure() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //登录失败,进行广播
                Toast.makeText(User_login.this, "登录失败,请检查账号密码", Toast.LENGTH_SHORT).show();
            }
        });
    }
}