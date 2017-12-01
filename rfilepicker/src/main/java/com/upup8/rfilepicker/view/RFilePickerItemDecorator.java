package com.upup8.rfilepicker.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * RFilePickerItemDecorator
 * Created by renwoxing on 2017/12/1.
 */
public class RFilePickerItemDecorator extends ItemDecoration {

    Context mContext;
    private Drawable mDivider;
    private int mOrientation;

    private RFIlePickerDecorationCallback callback;

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    //我们通过获取系统属性中的listDivider来添加，在系统中的AppTheme中设置
    public static final int[] ATRRS = new int[]{
            android.R.attr.listDivider
    };

    public RFilePickerItemDecorator(Context mContext, int orientation, RFIlePickerDecorationCallback callback) {
        this.mContext = mContext;
        final TypedArray ta = mContext.obtainStyledAttributes(ATRRS);
        this.mDivider = ta.getDrawable(0);
        ta.recycle();
        setOrientation(orientation);
        this.callback = callback;
    }

    //设置屏幕的方向
    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    /**
     * 在Item绘制前绘制（会画在Item图层下面）
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == HORIZONTAL_LIST) {
            //drawVerticalLine(c, parent, state);
        } else {
            drawHorizontalLine(c, parent, state);
        }

//        final int left = parent.getPaddingLeft();
//        final int right = parent.getWidth() - parent.getPaddingRight();
//        final int childCount = parent.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            final View child = parent.getChildAt(i);
//            int position = parent.getChildAdapterPosition(child);
//            //long groupId = callback.getGroupId(position);
//            //if (groupId < 0) return;
//            //String textLine = callback.getGroupFirstLine(position).toUpperCase();
//            if (position == 0 || isFirstInGroup(position)) {
//                float top = child.getTop() - topGap;
//                float bottom = child.getTop();
//                c.drawRect(left, top, right, bottom, paint);//绘制红色矩形
//                c.drawText(textLine, left, bottom, textPaint);//绘制文本
//            }
//        }
//
//        c.restore();

    }

    /**
     * 画横线, 这里的parent其实是显示在屏幕显示的这部分
     *
     * @param c
     * @param parent
     * @param state
     */
    public void drawHorizontalLine(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        final int leftWithMargin = (int) convertDpToPixel(mContext, 70);
        final int right = parent.getWidth();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int adapterPosition = parent.getChildAdapterPosition(child);
            int left = (adapterPosition == 0 || isFirstInGroup(adapterPosition)) ? 0 : leftWithMargin;
            //parent.getDecoratedBoundsWithMargins(child, mBounds);
            //final int bottom = mBounds.bottom + Math.round(ViewCompat.getTranslationY(child));
            //final int top = child.getBottom() + params.bottomMargin;
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
        c.restore();
    }

    /**
     * 在Item绘制后绘制（会画在Item图层上面）
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    /**
     * 间距,会在测量Item的时候调用
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //Rect对象可以给每个Item共用，不需要继续去new，相对高效
        super.getItemOffsets(outRect, view, parent, state);
    }

    public float convertDpToPixel(Context context, float dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public int convertPxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public int convertDpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }


    private boolean isFirstInGroup(int pos) {
        if (pos == 0) {
            return true;
        } else {
            return callback.getIsGroupHeadByPos(pos);
        }
    }


    public interface RFIlePickerDecorationCallback {
        boolean getIsGroupHeadByPos(int position);
    }
}
