package hlccd.regular.Music;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import hlccd.regular.R;
import hlccd.regular.util.VP_adapter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Music extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private static final int        RESULT_CODE_STARTAUDIO = 1;
    private              ViewPager2 VP;
    private              RadioGroup RG;

    @RequiresApi (api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music);
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.
                checkSelfPermission(Music.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
        } else {
            //提示用户开户获取文件的权限
            String[] perms = {"android.permission.READ_EXTERNAL_STORAGE"};
            ActivityCompat.requestPermissions(Music.this, perms, RESULT_CODE_STARTAUDIO);
            Toast.makeText(Music.this, "无权限", Toast.LENGTH_SHORT).show();
        }
        VP = findViewById(R.id.VP);
        RG = findViewById(R.id.RG);
        VP_adapter adapter = new VP_adapter(Music.this);
        adapter.addFragment(new Music_localhost());
        adapter.addFragment(new Music_like());
        adapter.addFragment(new Music_hot());
        VP.setAdapter(adapter);
        VP.setCurrentItem(0);
        RG.setOnCheckedChangeListener(this);
        VP.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        ((RadioButton) findViewById(R.id.localhost)).setChecked(true);
                        break;
                    case 1:
                        ((RadioButton) findViewById(R.id.like)).setChecked(true);
                        break;
                    case 2:
                        ((RadioButton) findViewById(R.id.hot)).setChecked(true);
                        break;
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.localhost:
                VP.setCurrentItem(0);
                break;
            case R.id.like:
                VP.setCurrentItem(1);
                break;
            case R.id.hot:
                VP.setCurrentItem(2);
                break;
        }
    }
}