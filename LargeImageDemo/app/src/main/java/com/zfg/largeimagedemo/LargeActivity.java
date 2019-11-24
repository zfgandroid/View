package com.zfg.largeimagedemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * 不压缩加载大图
 */
public class LargeActivity extends Activity {

    private LargeImageView image_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_large);

        image_view = findViewById(R.id.image_view);

        try {
            InputStream inputStream = getAssets().open("flower.jpg");

            image_view.setInputStream(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
