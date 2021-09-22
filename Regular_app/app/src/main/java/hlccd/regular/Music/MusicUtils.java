package hlccd.regular.Music;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class MusicUtils {
    /**
     * 扫描系统里面的音频文件，返回一个list集合
     */
    @RequiresApi (api = Build.VERSION_CODES.R)
    public static List<Song> getMusicData(Context context) {
        List<Song>      list             = new ArrayList<>();
        ContentResolver mContentResolver = context.getContentResolver();
        Cursor          c                = null;
        try {
            c = mContentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            while (c.moveToNext()) {
                Song song = new Song();
                song.setName(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                song.setSinger(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                song.setPath(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                song.setDuration(c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                song.setFile(new File(song.getPath()));
                if (song.getDuration() > 0) {
                    song.setMd5(getFileMD5(song.getFile()));
                    boolean b = true;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getMd5().equals(song.getMd5())) {
                            b = false;
                            break;
                        }
                    }
                    if (b) {
                        list.add(song);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return list;
    }

    /**
     * 定义一个方法用来格式化获取到的时间
     */
    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            return time / 1000 / 60 + ":0" + time / 1000 % 60;

        } else {
            return time / 1000 / 60 + ":" + time / 1000 % 60;
        }

    }

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest   digest   = null;
        FileInputStream in       = null;
        byte            buffer[] = new byte[1024];
        int             len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in     = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }
}
