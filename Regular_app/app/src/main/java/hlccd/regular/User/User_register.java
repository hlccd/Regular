package hlccd.regular.User;

import androidx.appcompat.app.AppCompatActivity;

import hlccd.regular.API.API;
import hlccd.regular.API.IM;
import hlccd.regular.Friend.Friend_friend_data;
import hlccd.regular.Homepage;
import hlccd.regular.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.util.List;

public class User_register extends AppCompatActivity {
    private EditText name;
    private EditText password;
    private EditText password_again;
    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register);
        name           = findViewById(R.id.user_register_name);
        password       = findViewById(R.id.user_register_password);
        password_again = findViewById(R.id.user_register_password_again);
        register       = findViewById(R.id.user_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equals("")) {
                    Toast.makeText(User_register.this, "请输入昵称", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.getText().toString().equals("")) {
                    Toast.makeText(User_register.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.getText().toString().equals(password_again.getText().toString())) {
                    Toast.makeText(User_register.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                //新建一个线程进行网络请求
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String nS = name.getText().toString();
                            String pS = password.getText().toString();
                            //组合url进行登录的get请求
                            String       url    = new API().UserRegisterAPI() + "?password=" + pS + "&name=" + nS;
                            OkHttpClient client = new OkHttpClient();
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("password", pS)
                                    .add("name", nS)
                                    .build();
                            Request request = new Request.Builder()
                                    .post(requestBody)
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
                                    String name  = json.getString("data");
                                    //将token,id,password穿出,本次登录成功,进行结束操作
                                    success(token, uid, name);
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
    }

    private void success(String token, Long uid, String name) {
        //对线程结尾进行后续操作
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //获取数据库中已有的用户进行搜索,检测该用户是否在本地登陆过,如果登录则进行覆写
                boolean         flag = true;
                List<User_data> arr  = LitePal.findAll(User_data.class);
                for (User_data data : arr) {
                    data.setState(0);
                    data.save();
                }
                User_data user = new User_data();
                user.setU_id(uid);
                user.setP_num((long) 0);
                user.setNumber((long) 0);
                user.setDuration((long) 0);
                user.setB_num((long) 0);
                user.setName(name);
                user.setState(1);
                user.setToken(token);
                user.setPassword("");
                //覆写或新增该用户
                user.save();
                //跳转至主页
                startActivity(new Intent(User_register.this, Homepage.class));
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
                Toast.makeText(User_register.this, "注册失败,请稍后重试", Toast.LENGTH_SHORT).show();
            }
        });
    }
}