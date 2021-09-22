package hlccd.regular.Regular;

import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import hlccd.regular.Friend.Friend_chat;
import hlccd.regular.Homepage;
import hlccd.regular.Music.Music;
import hlccd.regular.Project.Project_backlog_data;
import hlccd.regular.Project.Project_backlog_data_adapter;
import hlccd.regular.Project.Project_habit_data;
import hlccd.regular.Project.Project_task_data;
import hlccd.regular.R;
import hlccd.regular.User.User;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.QMUIProgressBar;

import org.litepal.LitePal;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Regular extends Fragment implements SensorEventListener {
    public static String      name      = "music";
    public static MediaPlayer mp        = new MediaPlayer();
    public static Boolean     control   = false;
    public static String      timestamp = "0";
    public static String      type      = "regular";

    private Project_habit_data   habit;
    private Project_task_data    task;
    private Project_backlog_data backlog;
    private Regular_data         regular;
    private ImageButton          music_icon;
    private TextView             music_name;
    private ImageButton          music_control;
    private ImageButton          music_catalogue;
    private TextView             type_name;
    private QMUIProgressBar      progress;
    private Chronometer          time;
    private ImageButton          stop;
    private ImageButton          pause;
    private ImageButton          go;
    private Long                 duration = 0L;
    private boolean              canStart = true;

    private SlidingDrawer            SD;
    private ImageButton              left;
    private ImageButton              right;
    private TextView                 data;
    private TextView                 sum;
    private TextView                 average;
    private TextView                 number;
    private RecyclerView             RV;
    private List<List<Regular_data>> regularss = new ArrayList<>();
    private List<Regular_data>       regulars  = new ArrayList<>();
    private LinearLayoutManager      layoutManager;
    private Regular_data_adapter     adapter;
    private Long                     now       = System.currentTimeMillis();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        music_icon      = view.findViewById(R.id.regular_music_icon);
        music_name      = view.findViewById(R.id.regular_music_name);
        music_control   = view.findViewById(R.id.regular_music_control);
        music_catalogue = view.findViewById(R.id.regular_music_catalogue);

        type_name = view.findViewById(R.id.regular_type_name);
        progress  = view.findViewById(R.id.regular_progress);
        time      = view.findViewById(R.id.regular_begin_time);
        stop      = view.findViewById(R.id.regular_begin_stop);
        pause     = view.findViewById(R.id.regular_begin_pause);
        go        = view.findViewById(R.id.regular_begin_go);

        SD      = view.findViewById(R.id.SD);
        left    = view.findViewById(R.id.left);
        right   = view.findViewById(R.id.right);
        data    = view.findViewById(R.id.data);
        sum     = view.findViewById(R.id.sum);
        average = view.findViewById(R.id.average);
        number  = view.findViewById(R.id.number);
        RV      = view.findViewById(R.id.RV);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("music");
        getActivity().registerReceiver(new broadcastReceiver(), intentFilter);

        SensorManager sManager           = (SensorManager) getActivity().getSystemService(getActivity().SENSOR_SERVICE);
        Sensor        mSensorOrientation = sManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sManager.registerListener(this, mSensorOrientation, SensorManager.SENSOR_DELAY_UI);

        get();
        implement_init();
        music_init();
        regular_init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.regular, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        get();
        implement_init();
        regular_init();
        music_name.setText(name);
    }


    /**
     * initialize
     * 初始化
     * 开始计时
     * 隐藏继续和停止按钮
     * 展示暂停按钮
     */
    private void implement_init() {
        pause.setVisibility(View.INVISIBLE);
        go.setVisibility(View.VISIBLE);
        stop.setVisibility(View.VISIBLE);
        if (type.equals("task")) {
            type_name.setText("日常任务:" + task.getName());
        } else if (type.equals("habit_positive")) {
            type_name.setText("积极习惯:" + habit.getName());
        } else if (type.equals("habit_negative")) {
            type_name.setText("消极习惯:" + habit.getName());
        } else if (type.equals("backlog")) {
            type_name.setText("待办事项:" + backlog.getName());
        } else {
            type_name.setText("regular");
        }
        duration = 0L;
        time.stop();
        time.setBase(SystemClock.elapsedRealtime());

        //暂停计时，同时显示继续和停止按钮
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duration = SystemClock.elapsedRealtime() - time.getBase();
                time.stop();
                pause.setVisibility(View.INVISIBLE);
                go.setVisibility(View.VISIBLE);
                stop.setVisibility(View.VISIBLE);
            }
        });
        //继续计时，展示暂停按钮
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time.setBase(SystemClock.elapsedRealtime() - duration);
                time.start();
                pause.setVisibility(View.VISIBLE);
                go.setVisibility(View.GONE);
                stop.setVisibility(View.GONE);
            }
        });
        //停止计时，记录超过1min的记录并结束该活动
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duration = SystemClock.elapsedRealtime() - time.getBase();
                if (duration < 10 * 1000) {
                    Toast.makeText(getActivity(), "时间少于一分钟,不予记录", Toast.LENGTH_SHORT).show();
                } else {
                    save();
                }
                duration = 0L;
                time.stop();
                time.setBase(SystemClock.elapsedRealtime());
            }
        });
        //计时上限50min,到上限时会震动
        time.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                duration = SystemClock.elapsedRealtime() - time.getBase();
                if (duration >= 50 * 60 * 1000) {
                    duration = 50 * 60 * 1000L;
                    save();
                    duration = 0L;
                    time.stop();
                    time.setBase(SystemClock.elapsedRealtime());
                    pause.setVisibility(View.INVISIBLE);
                    go.setVisibility(View.VISIBLE);
                    stop.setVisibility(View.VISIBLE);
                    Vibrator vibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
                    long[]   patter   = {0, 1000, 1000, 1000, 1000, 1000, 1000, 1000};
                    vibrator.vibrate(patter, -1);
                }
                if (duration <= 50 * 60 * 1000) {
                    progress.setProgress((int) (duration / (30 * 1000)));
                }
            }
        });
    }

    /**
     * get
     * 获取对象
     * 根据传输过来的识别符获取特定对象
     */
    private void get() {
        if (timestamp == null) {
            this.timestamp = "0";
        }
        if (type == null) {
            this.type = "regular";
        }
        if (type.equals("task")) {
            task = LitePal.where("timestamp=?", timestamp).find(Project_task_data.class).get(0);
        } else if (type.equals("habit_positive")) {
            habit = LitePal.where("timestamp=?", timestamp).find(Project_habit_data.class).get(0);
        } else if (type.equals("habit_negative")) {
            habit = LitePal.where("timestamp=?", timestamp).find(Project_habit_data.class).get(0);
        } else if (type.equals("backlog")) {
            backlog = LitePal.where("timestamp=?", timestamp).find(Project_backlog_data.class).get(0);
        } else if (type.equals("regular")) {
            regular = new Regular_data();
        }
    }

    /**
     * save
     * 保存对象
     * 根据传输过来的识别符保存对应对象
     */
    private void save() {
        if (type.equals("task")) {
            task.setNumber(task.getNumber() + 1);
            task.setDuration(task.getDuration() + duration);
            task.save();
        } else if (type.equals("habit_positive")) {
            habit.setPositive(habit.getPositive() + duration);
            habit.save();
        } else if (type.equals("habit_negative")) {
            habit.setNegative(habit.getNegative() + duration);
            habit.save();
        } else if (type.equals("backlog")) {
            backlog.setDuration(duration);
            backlog.save();
        }
        regular.setU_id(User.getUserId());
        if (type.equals("task")) {
            regular.setName(task.getName());
            regular.setDifficulty(task.getDifficulty());
        } else if (type.equals("habit_positive")) {
            regular.setName(habit.getName());
            regular.setDifficulty(habit.getDifficulty());
        } else if (type.equals("habit_negative")) {
            regular.setName(habit.getName());
            regular.setDifficulty(habit.getDifficulty());
        } else if (type.equals("backlog")) {
            regular.setName(backlog.getName());
            regular.setDifficulty(backlog.getDifficulty());
        } else if (type.equals("regular")) {
            regular.setName("regular");
            regular.setDifficulty(1);
        }
        regular.setBegin(System.currentTimeMillis() - duration);
        regular.setDuration(duration);
        regular.save();
    }

    private void music_init() {
        music_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (control) {
                    mp.setLooping(true);
                    mp.start();
                    control = false;
                    music_control.setImageResource(R.drawable.small_pause);
                } else {
                    mp.pause();
                    control = true;
                    music_control.setImageResource(R.drawable.small_begin);
                }
            }
        });
        music_catalogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (control) {
                    control = false;
                } else {
                    control = true;
                }
                startActivity(new Intent(getContext(), Music.class));
            }
        });
    }

    private void regular_init() {
        regularss.clear();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long             next   = now + 2 * 60 * 60 * 1000;
        String           nowS   = (new SimpleDateFormat("yyyy-MM-dd").format(now)) + " 00:00:00";
        try {
            now  = format.parse(nowS).getTime();
            next = now + 8 * 60 * 60 * 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<Regular_data> all = LitePal.findAll(Regular_data.class);
        Collections.sort(all);
        for (int t = 6; t <= 22; t += 2) {
            regulars = new ArrayList<>();
            for (int x = 0; x < all.size(); x++) {
                if (all.get(x).getU_id().equals(User.getUserId())){
                    if (all.get(x).getBegin() >= now && all.get(x).getBegin() <= next) {
                        regulars.add(all.get(x));
                    }
                }
            }
            Collections.sort(regulars);
            regularss.add(regulars);
            now = next;
            next += 2 * 60 * 60 * 1000;
        }
        try {
            now = format.parse(nowS).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String[] ss = {"00-08", "08-10", "10-12", "12-14", "14-16", "16-18", "18-20", "20-22", "22-24"};
        layoutManager = new LinearLayoutManager(getContext());
        RV.setLayoutManager(layoutManager);
        adapter = new Regular_data_adapter(ss, regularss);
        RV.setAdapter(adapter);

        SD.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                regular_init();
            }
        });
        Long sumL = 0L, numberL = 0L;
        for (int j = 0; j < regularss.size(); j++) {
            for (int i = 0; i < regularss.get(j).size(); i++) {
                sumL += regularss.get(j).get(i).getDuration();
                numberL++;
            }
        }

        data.setText(nowS.split(" ")[0]);
        if (numberL != 0) {
            number.setText(numberL + "次");
            sum.setText(sumL / (60 * 1000) + "min");
            average.setText((sumL / (60 * 1000 * numberL)) + "min");
        } else {
            number.setText("0次");
            sum.setText("0min");
            average.setText("0min");
        }
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                now = System.currentTimeMillis();
                regular_init();
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                now = now - 24 * 60 * 60 * 1000;
                regular_init();
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                now = now + 24 * 60 * 60 * 1000;
                regular_init();
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (Math.round(event.values[1] * 100) / 100 < -120 || Math.round(event.values[1] * 100) / 100 > 120) {
            if (canStart) {
                time.setBase(SystemClock.elapsedRealtime() - duration);
                time.start();
                pause.setVisibility(View.VISIBLE);
                go.setVisibility(View.GONE);
                stop.setVisibility(View.GONE);
                canStart = false;
            }
        } else if (!canStart) {
            duration = SystemClock.elapsedRealtime() - time.getBase();
            time.stop();
            pause.setVisibility(View.INVISIBLE);
            go.setVisibility(View.VISIBLE);
            stop.setVisibility(View.VISIBLE);
            canStart = true;
            Vibrator vibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
            long[]   patter   = {0, 1000, 1000, 1000};
            vibrator.vibrate(patter, -1);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private class broadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mp.setLooping(true);
            mp.start();
            control = false;
            music_control.setImageResource(R.drawable.small_pause);
        }
    }
}