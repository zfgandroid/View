package com.zfg.largeimagedemo;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

/**
 * 压缩显示大图
 */
public class CompressActivity extends Activity {

    private final static String TAG = CompressActivity.class.getSimpleName();

    private ImageView image_compress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_compress);

        image_compress = findViewById(R.id.image_compress);

        Bitmap bitmap = decodeSampledBitmapFromResource(getResources(), R.mipmap.flower,
                1080, 1920);

        image_compress.setImageBitmap(bitmap);
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        //inJustDecodeBounds设置为true，来获取图片大小，不会将图片加载到内存中
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        //计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        //inJustDecodeBounds设置为false，需要将图片加载到内存中
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        //原图片的宽高
        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            //计算出实际宽高和目标宽高的比率
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            //选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高一定都会大于等于目标的宽和高
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            Log.i(TAG, "widthRatio = " + widthRatio + ", heightRatio = " + heightRatio + ", inSampleSize = " + inSampleSize);
        }
        return inSampleSize;
    }

}
