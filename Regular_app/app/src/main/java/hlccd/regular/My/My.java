package hlccd.regular.My;
/**
 * 个人信息子界面碎片
 * 该界面包含个人头像和昵称显示
 * 个人月度报表、每日复盘、读书笔记和意见反馈的进入渠道
 * 同时包括登录登出选项
 *
 * @author hlccd 2020.6.10
 * @version 1.0
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import hlccd.regular.Blog.Blog;
import hlccd.regular.Homepage;
import hlccd.regular.R;
import hlccd.regular.User.User;
import hlccd.regular.User.User_login;

public class My extends Fragment {
    private View             view;
    private TextView         name;
    private TextView         login_logout;
    private ConstraintLayout message;
    private ConstraintLayout statement;
    private ConstraintLayout blog;
    private ConstraintLayout opinion;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.view    = view;
        name         = view.findViewById(R.id.my_name);
        login_logout = view.findViewById(R.id.my_login_logout);
        message      = view.findViewById(R.id.my_function_message_area);
        statement    = view.findViewById(R.id.my_function_statement_area);
        blog         = view.findViewById(R.id.my_function_blog_area);
        opinion      = view.findViewById(R.id.my_function_opinion_area);
        name.setText(User.getUserName());
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入个人信息活动
                startActivity(new Intent(getContext(), My_message.class));
            }
        });
        statement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入月度报表活动
                startActivity(new Intent(getContext(), My_statement.class));
            }
        });
        blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Blog.class));
            }
        });
        opinion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入意见返回活动
                startActivity(new Intent(getContext(), My_opinion.class));
            }
        });
        if (User.getUserId() > 0) {
            login_logout.setText("退出登录");
        } else {
            login_logout.setText("点击登录");
        }
        login_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), User_login.class));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my, container, false);
    }
}