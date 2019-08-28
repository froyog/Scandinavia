package com.nku.scandinavia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nku.scandinavia.helpers.Constants;

import org.opencv.android.OpenCVLoader;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Bitmap selectedImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_camera = findViewById(R.id.button_camera);
        Button button_gallery = findViewById(R.id.button_gallery);

        if (OpenCVLoader.initDebug()) {
            Toast.makeText(this, "openCv successfully loaded", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "openCv cannot be loaded", Toast.LENGTH_LONG).show();
        }

        button_camera.setOnClickListener(this);
        button_gallery.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_camera:
                goCamera();
                break;
            case R.id.button_gallery:
                goGallery();
                break;
        }
    }

    // 调用相机
    private void goCamera() {

    }

    // 调用相册
    private void goGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 222);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 111:
                break;
            case 222:
                if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(MainActivity.this, "取消从相册选择", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    Uri selectedImageUri = data.getData();
                    loadImageUriToBitmap(selectedImageUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void loadImageUriToBitmap(Uri selectedImageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
            selectedImageBitmap = BitmapFactory.decodeStream(inputStream);
            Constants.selectedImageBitmap = selectedImageBitmap;
            // jump to ImageDisplayActivity
            Intent intent = new Intent(getApplicationContext(), ImageDisplayActivity.class);
            startActivity(intent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
