package hlccd.regular.API;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import hlccd.regular.User.User;

public class IM {
    private static Socket         socket;          /*定义socket连接*/
    private static BufferedReader reader;          /*从字符输入流读取文本*/
    private static PrintWriter    writer;          /*打印到文本输出流*/

    static {
        try {
            socket = new Socket("47.108.217.244", 2997);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));/*标准输入输出流*/
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject login = new JSONObject();
        try {
            login.put("mes", "login");
            login.put("id", User.getUserId());
            login.put("receiver", 0);
            login.put("type", 0);
            login.put("group", 0);
            login.put("timestamp", System.nanoTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (socket != null && !socket.isClosed()) {
            writer.println(login);
            writer.flush();
        }
    }

    public static void Change() {
        try {
            socket = new Socket("47.108.217.244", 2997);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));/*标准输入输出流*/
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject login = new JSONObject();
        try {
            login.put("mes", "login");
            login.put("id", User.getUserId());
            login.put("receiver", 0);
            login.put("type", 0);
            login.put("group", 0);
            login.put("timestamp", System.nanoTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (socket != null && !socket.isClosed()) {
            writer.println(login);
            writer.flush();
        }
    }

    public static Socket getSocket() {
        return socket;
    }

    public static BufferedReader getReader() {
        if (socket == null || socket.isClosed()) {
            return null;
        }
        return reader;
    }

    public static PrintWriter getWriter() {
        return writer;
    }
}
