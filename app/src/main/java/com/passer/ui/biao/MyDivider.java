package com.passer.ui.biao;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

public class MyDivider extends RecyclerView.ItemDecoration {
    private int mScreenWidth;
    private Paint mPaint;

    public MyDivider(Context context) {
        super();
        DisplayMetrics metric = new DisplayMetrics();
        ((AppCompatActivity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        drawDivider(c, parent);
    }


    private void drawDivider(Canvas c, RecyclerView parent) {

        final int childCount = parent.getChildCount();

        //遍历所有item view，为它们的右边方绘制分割线，就是计算出上下左右四个值画一个矩形
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int top = child.getTop();
            int bottom = child.getBottom();
            int left = mScreenWidth / 20;
            int right = mScreenWidth / 20 + 1;

            int centerX = (left + right) / 2;
            int centerY = (top + bottom) / 2;
            int radius = (bottom - top) / 20;

            mPaint.setColor(Color.GRAY);

            // 处理第一个和最后一个原点的半径和线段的长度
            if (i == 0) {
                mPaint.setColor(Color.GREEN);
                radius = (bottom - top) / 10;
                top += (bottom - top) / 2;
                centerY = top;
            } else if (i == childCount - 1) {
                radius = (bottom - top) / 10;
                bottom -= (bottom - top) / 2;
                centerY = bottom;
                mPaint.setColor(Color.RED);
            }
            c.drawCircle(centerX, centerY, radius, mPaint);

            mPaint.setColor(Color.GRAY);
            c.drawLine(left, top, right, bottom, mPaint);

        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(mScreenWidth / 10 + 1, 0, 0, 0);
    }
}
