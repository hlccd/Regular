package hlccd.regular.Friend;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.List;

import hlccd.regular.API.API;
import hlccd.regular.R;
import hlccd.regular.User.User;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Friend_group_data extends LitePalSupport implements Comparable<Friend_group_data> {
    /**
     * 该群组头像的图片地址
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private int    portrait;
    /**
     * 该群组的ID
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long  group;
    /**
     * 该群组昵称
     */
    @Column (nullable = true, unique = false, defaultValue = "", ignore = false)
    private String name;
    /**
     * 该群组所属人的ID
     */
    @Column (nullable = true, unique = false, defaultValue = "0", ignore = false)
    private Long   master;

    Friend_group_data() {

    }

    public void refresh(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Friend_group_data> groups = LitePal.where("master=?", User.getUserId() + "").find(Friend_group_data.class);
                for (int i=0;i<groups.size();i++){
                    groups.get(i).delete();
                }
                try {
                    //组合url进行登录的get请求
                    String       url    = new API().MyGroupListAPI();
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
                            for (int i = 0; i < array.length(); i++) {
                                json = array.getJSONObject(i);
                                Long   group   = json.optLong("group");
                                String name = json.getString("name");
                                new Friend_group_data(R.drawable.main_my, group, name,User.getUserId()).save();
                            }
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

    Friend_group_data(int portrait, Long group, String name, Long master) {
        this.portrait = portrait;
        this.group   = group;
        this.name     = name;
        this.master   = master;
    }

    public int getPortrait() {
        return portrait;
    }

    public void setPortrait(int portrait) {
        this.portrait = portrait;
    }

    public Long getGroup() {
        return group;
    }

    public void setGroup(Long group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMaster() {
        return master;
    }

    public void setMaster(Long master) {
        this.master = master;
    }

    /**
     * 按照好友ID进行降序
     */
    @Override
    public int compareTo(Friend_group_data o) {
        if (this.group > o.group) {
            return 1;
        } else if (this.group < o.group) {
            return -1;
        }
        return 0;
    }
}
