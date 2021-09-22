package hlccd.regular.Music;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.util.List;

import hlccd.regular.API.API;
import hlccd.regular.API.IM;
import hlccd.regular.Friend.Friend_friend_data;
import hlccd.regular.Homepage;
import hlccd.regular.R;
import hlccd.regular.Regular.Regular;
import hlccd.regular.User.User;
import hlccd.regular.User.User_data;
import hlccd.regular.User.User_register;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private Context    context;
    private List<Song> songs;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        TextView         name;//歌曲名
        TextView         singer;//歌手
        TextView         duration;//时长
        TextView         position;//序号
        ImageView        sign;

        public ViewHolder(View view) {
            super(view);
            layout   = (ConstraintLayout) view.findViewById(R.id.layout);
            position = (TextView) view.findViewById(R.id.position);
            name     = (TextView) view.findViewById(R.id.name);
            singer   = (TextView) view.findViewById(R.id.singer);
            duration = (TextView) view.findViewById(R.id.duration);
            sign     = (ImageView) view.findViewById(R.id.sign);
        }
    }

    public SongAdapter(Context context, List<Song> list) {
        this.context = context;
        this.songs   = list;

    }

    @Override
    public SongAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_song_data, parent, false);
        return new SongAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SongAdapter.ViewHolder holder, int position) {
        //获取其中一条待办事项
        Song song = songs.get(position);
        //给控件赋值
        holder.name.setText(song.getName());
        holder.singer.setText(song.getSinger());
        //时间需要转换一下
        int duration = song.getDuration();
        if (duration > 0) {
            String time = MusicUtils.formatTime(duration);
            holder.duration.setText(time);
        } else {
            holder.duration.setText("");
        }
        holder.position.setText(position + 1 + "");
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Regular.mp.stop();
                Regular.mp = new MediaPlayer();
                try {
                    if (song.getFile() != null) {
                        Regular.mp.setDataSource(String.valueOf(song.getFile()));
                    } else {
                        Regular.mp.setDataSource(song.getPath());
                    }
                    Regular.mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Regular.name = song.getName();
                ((Activity) context).sendBroadcast(new Intent("music"));
                ((Activity) context).finish();
            }
        });
        if (song.isShow()) {
            holder.sign.setVisibility(View.VISIBLE);
            holder.sign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.sign.setVisibility(View.GONE);
                    upload(holder, song);
                }
            });
        } else {
            holder.sign.setVisibility(View.GONE);
        }

    }

    private void upload(SongAdapter.ViewHolder holder, Song song) {
        String name     = song.getName();
        String singer   = song.getSinger();
        String duration = song.getDuration() + "";
        String md5      = song.getMd5();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //组合url进行登录的get请求
                    String url = new API().MusicUploadMd5API()
                            + "?name=" + name
                            + "&singer=" + singer
                            + "&duration=" + duration
                            + "&md5=" + md5;
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .build();
                    Request request = new Request.Builder()
                            .post(requestBody)
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
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (code == 500) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    holder.sign.setVisibility(View.VISIBLE);
                                    Toast.makeText(context, "上传失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (code == 300) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    uploadMusic(holder, song);
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

    private void uploadMusic(SongAdapter.ViewHolder holder, Song song) {
        String name     = song.getName();
        String singer   = song.getSinger();
        String duration = song.getDuration() + "";
        String md5      = song.getMd5();
        File   file     = song.getFile();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = new API().MusicUploadFileAPI()
                            + "?name=" + name
                            + "&singer=" + singer
                            + "&duration=" + duration
                            + "&md5=" + md5;
                    MediaType   mediaType   = MediaType.parse("application/octet-stream");
                    RequestBody requestBody = RequestBody.create(mediaType, file);
                    RequestBody multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                            .addFormDataPart("music", name, requestBody)
                            .build();
                    Request request = new Request.Builder()
                            .header("Authorization", User.getUserToken())
                            .url(url)
                            .post(multipartBody)
                            .build();

                    OkHttpClient client   = new OkHttpClient();
                    Response     response = client.newCall(request).execute();
                    String       data     = response.body().string();
                    //对响应数据进行json解析,获取响应code和mes
                    try {
                        JSONArray  array = new JSONArray("[" + data + "]");
                        JSONObject json  = array.getJSONObject(0);
                        String     mes   = json.optString("message");
                        int        code  = json.optInt("code");
                        //当且仅当code为200时说明登录成功,继续对mes进行json解析获取token
                        if (code == 200) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    holder.sign.setVisibility(View.VISIBLE);
                                    Toast.makeText(context, "上传失败", Toast.LENGTH_SHORT).show();
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
    public int getItemCount() {
        return songs.size();
    }
}
