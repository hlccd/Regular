package hlccd.regular.Blog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import hlccd.regular.R;

public class Blog_publish extends AppCompatActivity {
    private TextView     publish;
    private EditText     message;
    private ImageView    choose;
    private RecyclerView img;
    private RecyclerView circles;

    private List<File>                   files  = new ArrayList<>();
    private List<String>                 ss     = new Blog_data().getCircleList();
    private LinearLayoutManager          LM1;
    private LinearLayoutManager          LM2;
    private Blog_publish_img_adapter     ImgAdapter;
    private Blog_publish_circles_adapter CircleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog_publish);
        publish = findViewById(R.id.publish);
        message = findViewById(R.id.message);
        choose  = findViewById(R.id.choose);
        img     = findViewById(R.id.img);
        circles = findViewById(R.id.circles);

        LM1 = new LinearLayoutManager(Blog_publish.this);
        LM1.setOrientation(LinearLayoutManager.HORIZONTAL);
        img.setLayoutManager(LM1);
        ImgAdapter = new Blog_publish_img_adapter(this, files);
        img.setAdapter(ImgAdapter);
        LM2 = new LinearLayoutManager(Blog_publish.this);
        circles.setLayoutManager(LM2);
        CircleAdapter = new Blog_publish_circles_adapter(Blog_publish.this, ss);
        circles.setAdapter(CircleAdapter);

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Blog_publish.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Blog_publish.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("image/*");
                    startActivityForResult(intent, 2);
                }
            }
        });
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String       s  = "";
                List<String> ss = CircleAdapter.getSelect();
                for (int i = 0; i < ss.size(); i++) {
                    s += ss.get(i);
                }
                Toast.makeText(Blog_publish.this, s + "发布" + message.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2: {
                if (resultCode == RESULT_OK) {
                    handleImage(data);
                }
            }
        }
    }

    @TargetApi (19)
    private void handleImage(Intent data) {
        String path = null;
        Uri    uri  = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id        = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                path = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                path = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            path = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            path = uri.getPath();
        }
        display(path);
    }

    private String getImagePath(Uri uri, String selection) {
        String path   = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void display(String path) {
        if (path != null) {
            files.add(new File(path));
            LM1 = new LinearLayoutManager(Blog_publish.this);
            LM1.setOrientation(LinearLayoutManager.HORIZONTAL);
            img.setLayoutManager(LM1);
            ImgAdapter = new Blog_publish_img_adapter(this, files);
            img.setAdapter(ImgAdapter);
            img.scrollToPosition(ImgAdapter.getItemCount() - 1);
        } else {
            Toast.makeText(this, "没找到", Toast.LENGTH_SHORT).show();
        }
    }
}