package cn.com.changan.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyViewGroup extends ViewGroup {
    //存放传过来的子View数据
    private List<Bitmap> mData;         //用于存储所有子View的内容
    private List<List<View>> mLines;    //集合内的每个元素代表每一行。

    private int leftMargin = 5;   //每个子View间的左边间距
    private int toptMargin = 5;   //每个子View间的与上面子View的间距
    private OnTextClickListener textListener;


    public MyViewGroup(Context context) {
        super(context);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mData = new ArrayList<Bitmap>();
        mLines = new ArrayList<>();
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return p;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }


    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }

    //添加子View
    public void setView(List<Bitmap> list) {
        mData = new ArrayList<Bitmap>();
//        mData.clear();
        mData.addAll(list);
        //先清空原来的内容
//        removeAllViews();
        for (Bitmap data : list) {
            ImageView imageView=new ImageView(getContext());
            imageView.setImageBitmap(data);
            addView(imageView);
        }
    }

    /**
     * 第二步： 重写onMeasure方法测量子控件和自身宽高；
     * 在onMeasure方法里进行子控件测量及ViewGroup自身的测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //得到父类提供的大小
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

        //获得子View个数
        int childCount = getChildCount();
        if (childCount == 0) {
            return;
        }
        //先清空
        mLines = new ArrayList<>();
        mLines.clear();
        //添加默认行
        List<View> newLine = new ArrayList<>();
        mLines.add(newLine);

        int width = MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.AT_MOST);
        int height = MeasureSpec.makeMeasureSpec(parentHeight, MeasureSpec.AT_MOST);


        for (int i = 0; i < childCount; i++) {
            //获得子View个体
            View childAt = getChildAt(i);

            //如果这个View在new时设置是不可见的，则直接跳到下一个循环
            if (childAt.getVisibility() != VISIBLE) {
                continue;
            }
            measureChild(childAt, width, height);
            if (newLine.size() == 0) {
                //可以添加
                newLine.add(childAt);
            } else {
                //判断是否可以添加进该行（计算组合宽度是否超出限定宽度，若超出则需要换行）
                boolean isCanBeAdd = checkChildCanBeAdd(newLine, childAt, parentWidth);
                if (!isCanBeAdd) {
                    newLine = new ArrayList<>();
                    mLines.add(newLine);
                }
//                newLine.add(childAt);
            }
        }
        //根据尺寸j计算所有行高
        View child = getChildAt(0);
        int childHeight = child.getMeasuredHeight();
        int totalChildHeight = childHeight * mLines.size() + toptMargin * (mLines.size() + 1) + getPaddingBottom() + getPaddingTop();
        setMeasuredDimension(parentWidth, totalChildHeight);
    }

    //判断是否可以添加进该行
    private boolean checkChildCanBeAdd(List<View> newLine, View childAt, int parentWidth) {

        int totalWidth = leftMargin + getPaddingLeft();

        for (View view : newLine) {
            totalWidth += view.getMeasuredWidth() + leftMargin;
        }
        totalWidth += childAt.getMeasuredWidth() + leftMargin + getPaddingRight();

        //如果超出限制宽度，则不可以再添加（换行）
        //否则可以添加
        return totalWidth <= parentWidth;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View fristChild = getChildAt(0);
        //用于标记子View的位置
        int currentLeft = leftMargin + getPaddingLeft();
        int currentRight = leftMargin + getPaddingLeft();
        int currentTop = toptMargin + getPaddingTop();
        int currentBottom = fristChild.getMeasuredHeight() + toptMargin + getPaddingTop();

        //循环每一行
        for (List<View> mLine : mLines) {

            //对一行内的View进行布局
            for (View child : mLine) {
                int width = child.getMeasuredWidth();

                currentRight += width;

                if (currentRight > (getMeasuredWidth() - leftMargin - getPaddingRight())) {
                    Log.e("TAG-------------", "asdasd");
                    currentRight = getMeasuredWidth() - leftMargin - getPaddingRight();
                }
                //为子View排放位置
                child.layout(currentLeft, currentTop, currentRight, currentBottom);

                currentLeft = currentRight + leftMargin;
                currentRight += leftMargin;
            }

            currentLeft = leftMargin + getPaddingLeft();
            currentRight = leftMargin + getPaddingLeft();
            currentTop += getChildAt(0).getMeasuredHeight() + toptMargin;
            currentBottom += fristChild.getMeasuredHeight() + toptMargin;

        }
    }

    //-------设置监听-----
    public void setOnTextListener(OnTextClickListener textListener) {
        this.textListener = textListener;
    }

    ;

    //-------监听接口-----
    public interface OnTextClickListener {
        void getItemTextContext(View view, String text);
    }
}
