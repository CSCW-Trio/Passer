package com.passer.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.passer.R;


/**
 * 标题栏
 */
public class TitleBar extends FrameLayout {
	private TextView tvLeft;
	private TextView tvCenter;
	private TextView tvRight;
	private FrameLayout view;
	private ImageView remind;

	public TitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.include_titlebar, this);
		view = (FrameLayout) findViewById(R.id.bg_include_titlebar);
		tvLeft = (TextView) findViewById(R.id.tv_left_titlebar);
		tvCenter = (TextView) findViewById(R.id.tv_title_titlebar);
		tvRight = (TextView) findViewById(R.id.tv_right_titlebar);

		TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.TitleBar);
		tvLeft.setText(array.getString(R.styleable.TitleBar_left_text));
		tvRight.setText(array.getString(R.styleable.TitleBar_right_text));
		tvCenter.setText(array.getString(R.styleable.TitleBar_center_text));
		view.setBackgroundColor(array.getColor(R.styleable.TitleBar_android_background,
				context.getResources().getColor(R.color.colorPrimary)));
//        Drawable d = array.getDrawable(R.styleable.TitleBar_right_img);
//        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
//        setRightImage(new BitmapDrawable(getResources(),getBitmap(bitmap,150,150)));
		array.recycle();
	}

    /**
     * 返回一张指定宽高的图片
     * @param bitmap 原图
     * @param newWidth 新的宽度
     * @param newHeight 新的高度
     * @return 新的图片
     */
    public Bitmap getBitmap(Bitmap bitmap,int newWidth,int newHeight){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = (float) newWidth / width;
        float scaleHeight = (float) newHeight / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);
        return Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
    }

	public void setBackgroundColor(String colorString) {
		view.setBackgroundColor(Color.parseColor(colorString));
	}

	public void setTitleText(String text) {
		tvCenter.setText(text);
	}

	public void setLeftText(String text) {
		tvLeft.setText(text);
	}

	public void setLeftButtonClickListener(OnClickListener listener) {
		tvLeft.setOnClickListener(listener);
	}

	public void setLeftImage(Drawable d) {
		tvLeft.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
	}

	public void setLeftImage(int id) {
		tvLeft.setCompoundDrawablesWithIntrinsicBounds(id, 0, 0, 0);
	}

	public void setRightText(String text) {
		tvRight.setText(text);
	}

	public void setRightButtonClickListener(OnClickListener listener) {
		tvRight.setOnClickListener(listener);
	}

	public void setRightImage(Drawable d) {

		tvRight.setCompoundDrawablesWithIntrinsicBounds(null, null, d, null);
	}

	public void setRightImage(int id){
		tvRight.setCompoundDrawablesWithIntrinsicBounds(0, 0, id, 0);

	}

	public void setLeftBottomImage(int id) {
		tvLeft.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, id);
	}

	public void setCenterButtonClickListener(OnClickListener listener) {
		tvCenter.setOnClickListener(listener);
	}

	public void setRightTopImage(int id) {
		tvRight.setCompoundDrawablesWithIntrinsicBounds(0, id, 0, 0);
	}

}
