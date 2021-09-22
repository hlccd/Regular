package hlccd.regular.Music;

import java.io.File;

import hlccd.regular.API.API;

public class Song {
    private String  name;
    private String  singer;
    private int     duration;
    private String  md5;
    private String  path;
    private File    file;
    private boolean show;

    public Song() {
        this.show = true;
    }

    public Song(String name, String id) {
        this.name     = name;
        this.singer   = "";
        this.duration = 0;
        this.md5      = "";
        this.path     = "http://music.163.com/song/media/outer/url?id=" + id + ".mp3";
        this.file     = null;
        this.show     = false;
    }

    public Song(String name, String singer, int duration, String md5) {
        this.name     = name;
        this.singer   = singer;
        this.duration = duration;
        this.md5      = md5;
        this.path     = new API().MusicListenAPI() + "?md5=" + md5;
        this.file     = null;
        this.show     = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}