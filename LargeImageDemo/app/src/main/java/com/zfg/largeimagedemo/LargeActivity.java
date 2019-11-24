package com.zfg.largeimagedemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * 不压缩加载大图
 */
public class LargeActivity extends Activity {

    private ImageView image_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_large);

        image_view = findViewById(R.id.image_view);

        try {
            InputStream inputStream = getAssets().open("flower.jpg");

            //获取图片的宽高，但不加载到内存中
            BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
            tmpOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, tmpOptions);
            int mImageWidth = tmpOptions.outWidth;
            int mImageHeight = tmpOptions.outHeight;

            //获取BitmapRegionDecoder，并设置相关参数
            BitmapRegionDecoder mDecoder = BitmapRegionDecoder.newInstance(inputStream, false);
            BitmapFactory.Options mOptions = new BitmapFactory.Options();
            mOptions.inPreferredConfig = Bitmap.Config.RGB_565;

            //显示图片
            Rect rect = new Rect(mImageWidth / 2 - 400, mImageHeight / 2 - 400,
                    mImageWidth / 2 + 400, mImageHeight / 2 + 400);
            Bitmap newBitmap = mDecoder.decodeRegion(rect, mOptions);
            image_view.setImageBitmap(newBitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
