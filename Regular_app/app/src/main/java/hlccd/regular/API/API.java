package hlccd.regular.API;

public class API {
    String ip    = "hlccd.top";
    String point = "2975";
    public final String UserLogin="http://" + ip + ":" + point + "/user/login";

    public String UserLoginAPI() {
        return "http://" + ip + ":" + point + "/user/login";
    }

    public String UserRegisterAPI() {
        return "http://" + ip + ":" + point + "/user/register";
    }

    public String UserSearchAPI() {
        return "http://" + ip + ":" + point + "/user/search";
    }

    public String FriendListAPI() {
        return "http://" + ip + ":" + point + "/user/friend";
    }

    public String MyGroupListAPI() {
        return "http://" + ip + ":" + point + "/group/my";
    }

    public String GroupListAPI() {
        return "http://" + ip + ":" + point + "/group/find/groups";
    }

    public String GroupEstablishAPI() {
        return "http://" + ip + ":" + point + "/group/establish";
    }
    public String MusicUploadMd5API() {
        return "http://" + ip + ":" + point + "/music/md5";
    }
    public String MusicUploadFileAPI() {
        return "http://" + ip + ":" + point + "/music/file";
    }
    public String MusicListAPI() {
        return "http://" + ip + ":" + point + "/music/list";
    }
    public String MusicListenAPI() {
        return "http://" + ip + ":" + point + "/music/listen";
    }
    public String MusicHotAPI() {
        return "http://" + ip + ":" + point + "/music/hot";
    }
}
