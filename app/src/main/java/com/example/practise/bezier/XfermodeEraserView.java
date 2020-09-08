package com.example.practise.bezier;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.example.practise.R;

// 刮刮卡功能
public class XfermodeEraserView extends View {
    private Paint mPaint;
    private Bitmap mDstBmp, mSrcBmp, mTxtBmp;
    private Path mPath;

    public XfermodeEraserView(Context context) {
        this(context, null);
    }

    public XfermodeEraserView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XfermodeEraserView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //初始化画笔
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(80);

        //禁用硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        //初始化图片对象
        mTxtBmp = BitmapFactory.decodeResource(getResources(), R.mipmap.result);
        mSrcBmp = BitmapFactory.decodeResource(getResources(), R.mipmap.eraser);
        mDstBmp = Bitmap.createBitmap(mSrcBmp.getWidth(), mSrcBmp.getHeight(), Bitmap.Config.ARGB_8888);

        //路径（贝塞尔曲线）
        mPath = new android.graphics.Path();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制刮奖结果
        canvas.drawBitmap(mTxtBmp, 0, 0, mPaint);

        //使用离屏绘制
        int layerID = canvas.saveLayer(0, 0, getWidth(), getHeight(), mPaint, Canvas.ALL_SAVE_FLAG);

        //先将路径绘制到 bitmap上
        Canvas dstCanvas = new Canvas(mDstBmp);
        dstCanvas.drawPath(mPath, mPaint);

        //绘制 目标图像
        canvas.drawBitmap(mDstBmp, 0, 0, mPaint);
        //设置 模式 为 SRC_OUT, 擦橡皮区域为交集区域需要清掉像素
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        //绘制源图像
        canvas.drawBitmap(mSrcBmp, 0, 0, mPaint);

        mPaint.setXfermode(null);

        canvas.restoreToCount(layerID);
    }

    private float mEventX, mEventY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mEventX = event.getX();
                mEventY = event.getY();
                mPath.moveTo(mEventX, mEventY);
                postDelayed(mTask, 1000);
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = (event.getX() - mEventX) / 2 + mEventX;
                float endY = (event.getY() - mEventY) / 2 + mEventY;
                //画二阶贝塞尔曲线
                mPath.quadTo(mEventX, mEventY, endX, endY);
                mEventX = event.getX();
                mEventY = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                removeCallbacks(mTask);
                break;
        }
        return true; //消费事件
    }

    // 计算擦出的区域, 应该在子线程执行
    private Runnable mTask = new Runnable() {
        @Override
        public void run() {
            int width = getWidth();
            int height = getHeight();

            int totalArea = width * height;
            int wipedArea = 0;

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int pixel = mDstBmp.getPixel(x, y); // mDstBmp.getPixels(pixels[], 0, width, 0, 0, width, height);可以先一次性将pixel数据都读出来
                    if (pixel != 0) {
                        wipedArea++;
                    }
                }
            }

            postDelayed(mTask, 1000);
            Log.e("TEST", "WipedArea is " + wipedArea * 100.0 / totalArea + "%");
        }
    };
}
