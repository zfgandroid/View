package com.zfg.largeimagedemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.io.IOException;
import java.io.InputStream;

public class LargeImageView extends View implements GestureDetector.OnGestureListener {

    private final String TAG = LargeImageView.class.getSimpleName();

    private BitmapRegionDecoder mDecoder;

    private BitmapFactory.Options mOptions;

    private Rect mRect = new Rect();

    //图片的宽度和高度
    private int mImageWidth;
    private int mImageHeight;

    private GestureDetector mGestureDetector;

    //分别记录上次滑动的坐标
    private int mLastX = 0;
    private int mLastY = 0;

    public LargeImageView(Context context) {
        super(context);

        init(context);
    }

    public LargeImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public LargeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {

        //初始化手势
        mGestureDetector = new GestureDetector(context, this);
    }

    /**
     * 从外面设置需要加载的图片
     *
     * @param in
     */
    public void setInputStream(InputStream in) {

        try {
            //获取图片的宽高，但不加载到内存中
            BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
            tmpOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, tmpOptions);
            mImageWidth = tmpOptions.outWidth;
            mImageHeight = tmpOptions.outHeight;
            Log.i(TAG, "mImageWidth = " + mImageWidth + ", mImageHeight = " + mImageHeight);

            //获取BitmapRegionDecoder，并设置相关参数
            mDecoder = BitmapRegionDecoder.newInstance(in, false);
            mOptions = new BitmapFactory.Options();
            mOptions.inPreferredConfig = Bitmap.Config.RGB_565;

            requestLayout();

            invalidate();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //把事件交给手势控制器处理
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {

        mLastX = (int) e.getRawX();
        mLastY = (int) e.getRawY();

        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        int x = (int) e2.getRawX();
        int y = (int) e2.getRawY();

        move(x, y);

        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

        mLastX = (int) e.getRawX();
        mLastY = (int) e.getRawY();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        int x = (int) e2.getRawX();
        int y = (int) e2.getRawY();

        move(x, y);

        return true;
    }

    /**
     * 移动的时候更新图片显示的区域
     *
     * @param x
     * @param y
     */
    private void move(int x, int y) {

        int deltaX = x - mLastX;
        int deltaY = y - mLastY;
        Log.i(TAG, "move, deltaX:" + deltaX + " deltaY:" + deltaY);

        //如果图片宽度大于屏幕宽度
        if (mImageWidth > getWidth()) {
            //移动rect区域
            mRect.offset(-deltaX, 0);
            //检查是否到达图片最右端
            if (mRect.right > mImageWidth) {
                mRect.right = mImageWidth;
                mRect.left = mImageWidth - getWidth();
            }

            //检查左端
            if (mRect.left < 0) {
                mRect.left = 0;
                mRect.right = getWidth();
            }

            invalidate();
        }

        //如果图片高度大于屏幕高度
        if (mImageHeight > getHeight()) {
            //移动rect区域
            mRect.offset(0, -deltaY);
            //是否到达最底部
            if (mRect.bottom > mImageHeight) {
                mRect.bottom = mImageHeight;
                mRect.top = mImageHeight - getHeight();
            }

            if (mRect.top < 0) {
                mRect.top = 0;
                mRect.bottom = getHeight();
            }
            //重绘
            invalidate();
        }

        mLastX = x;
        mLastY = y;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int imageWidth = mImageWidth;
        int imageHeight = mImageHeight;

        //默认显示图片的中心区域，可自行设置
        mRect.left = imageWidth / 2 - width / 2;
        mRect.top = imageHeight / 2 - height / 2;
        mRect.right = mRect.left + width;
        mRect.bottom = mRect.top + height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Bitmap bitmap = mDecoder.decodeRegion(mRect, mOptions);

        canvas.drawBitmap(bitmap, 0, 0, null);
    }
}
