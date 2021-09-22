package hlccd.regular.Music;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hlccd.regular.API.API;
import hlccd.regular.R;
import hlccd.regular.User.User;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Music_hot extends Fragment {
    private SwipeRefreshLayout  SR;
    private RecyclerView        RV;
    private List<Song>          songs = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private SongAdapter         adapter;
    private String[]            ids   = new String[]{"19723756", "3779629", "2884035", "3778678", "5453912201"
            , "991319590", "71384707", "1978921795", "71385702", "745956260", "10520166", "180106", "60198"
            , "3812895", "21845217", "60131", "2809513713", "2809577409", "27135204", "3001835560", "3001795926"
            , "3001890046", "3112516681", "5059644681", "5059633707", "5059642708", "5338990334", "5059661515"
            , "6688069460", "6723173524", "6732051320", "6732014811", "6886768100"};
    private int                 idx   = 0;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        SR = view.findViewById(R.id.SR);
        RV = view.findViewById(R.id.RV);
        SR.setColorSchemeResources(R.color.mainColor);
        SR.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initialize();
                SR.setRefreshing(false);
            }
        });
        initialize();
    }

    @Override
    public void onResume() {
        super.onResume();
        initialize();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music_hot, container, false);
        return view;
    }

    private void initialize() {
        songs.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //组合url进行登录的get请求
                    String       url    = new API().MusicHotAPI() + "?id=" + ids[idx];
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .header("Authorization", User.getUserToken())
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
                            if (mes.charAt(0) != '[') {
                                mes = "[" + mes + "]";
                            }
                            array = new JSONArray(mes);
                            for (int i = 0; i < array.length(); i++) {
                                json = array.getJSONObject(i);
                                String name = json.getString("name");
                                String idS  = json.getString("id");
                                songs.add(new Song(name, idS));
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    layoutManager = new LinearLayoutManager(getContext());
                                    RV.setLayoutManager(layoutManager);
                                    adapter = new SongAdapter(getActivity(), songs);
                                    RV.setAdapter(adapter);
                                }
                            });
                        } else {
                            //当登录失败时候进行失败操作
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "获取失败,请检查网络", Toast.LENGTH_SHORT).show();
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
        idx++;
        if (idx >= ids.length) {
            idx = 0;
        }
    }
}
