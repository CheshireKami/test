package com.kami.learneverything.com.kami.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by kami on 16/5/29.
 */
public class ScrollPanel extends LinearLayout {

    private Paint linePaint;
    private int mHeight;
    private int mWidth;

    public ScrollPanel(Context context) {
        this(context, null);
    }

    public ScrollPanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    private void init(final Context context) {
        Log.i("init", "init was call");
        //默认ViewGroup不会调用OnDraw方法，取消这个Flag
        setWillNotDraw(false);
        linePaint = new Paint();
        linePaint.setColor(Color.RED);
        linePaint.setAntiAlias(true);

        ScrollView scrollView = new ScrollView(context);
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        scrollView.setVerticalScrollBarEnabled(false);
        this.addView(scrollView);

        final LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(linearLayout);


        //监听视图绘制，完成后获取高度并添加组件
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mHeight = getHeight();
                mWidth = getWidth();
                TextView tv;
                for (int i = 2000; i<2020; i++){
                    tv = new TextView(context);
                    tv.setText(i + "-" + (i + 1));
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    tv.setGravity(Gravity.CENTER);
                    tv.setHeight(mHeight/3);
                    tv.setWidth(mWidth);
                    linearLayout.addView(tv);
                }
                Log.i("onGlobalLayout","onGlobalLayout finish");
                //因为onGlobalLayout可能在视图改变时被调用多次，所以在任务完成后就将它注销.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(0, (float) (mHeight / 3), mWidth, (float) (mHeight / 3), linePaint);
        canvas.drawLine(0, (float) (mHeight / 3 * 2), mWidth, (float) (mHeight / 3 * 2), linePaint);
        canvas.drawRect(0, 0, 100, 100, linePaint);

    }
}
