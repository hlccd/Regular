package hlccd.regular.User;
/**
 * 用户操作
 * 查看当然登录用户ID,如无登录者则返回0
 *
 * @author hlccd 2020.6.7
 * @version 1.0
 */

import org.litepal.LitePal;

import java.util.List;

public class User {
    /**
     * getUserId
     * 查看当然已登录的用户ID
     * 如果没有登录者,则用户ID为0
     *
     * @return String 已登录的用户ID
     */
    public static Long getUserId() {
        Long            u_id  = 0L;
        List<User_data> users = LitePal.findAll(User_data.class);
        for (User_data user : users) {
            if (user.getState() == 1) {
                u_id = user.getU_id();
                break;
            }
        }
        return u_id;
    }

    public static String getUserToken() {
        String          token = "";
        List<User_data> users = LitePal.findAll(User_data.class);
        for (User_data user : users) {
            if (user.getState() == 1) {
                token = user.getToken();
                break;
            }
        }
        return token;
    }

    /**
     * getUserName
     * 查看当然已登录的用户名字
     * 如果没有登录者,则返回"未登录"
     *
     * @return String 已登录的用户名字
     */
    public static String getUserName() {
        String          name  = "未登录";
        List<User_data> users = LitePal.findAll(User_data.class);
        for (User_data user : users) {
            if (user.getState() == 1) {
                name = user.getName();
                break;
            }
        }
        return name;
    }
}
