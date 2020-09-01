package cc.bear3.util.chartview.doughnut;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.List;

import cc.bear3.util.chartview.R;

/**
 * 自定义环形统计图 - Path绘制
 */

public class DoughnutView extends View {
    private static final float mStartAngle = -90;           // 默认起点扇形角度

    // 圈环的宽度
    private float mCircleWidth = 0;
    // 圆环半径
    private float mCircleRadius;

    /**
     * 中心x坐标
     */
    private float centerX;
    /**
     * 中心y坐标
     */
    private float centerY;
    /**
     * 绘制圆环的画笔
     */
    private Paint mArcPaint;

    private RectF mRectF = new RectF();

    private List<DoughnutData> dataList;

    public DoughnutView(Context context) {
        this(context, null);
    }

    public DoughnutView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DoughnutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void updateData(List<DoughnutData> dataList) {
        if (dataList != null && dataList.size() > 0) {
            double total = 0;

            // 第一遍循环获取总数
            for (DoughnutData temp : dataList) {
                total += temp.getValue();
            }

            // 第二遍循环设置角度
            for (DoughnutData temp : dataList) {
                temp.setAngle((float) (temp.getValue() / total * 360));
            }
        }

        this.dataList = dataList;
        invalidate();
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        // 圆环画笔
        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setStyle(Paint.Style.STROKE);

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DoughnutView);

            mCircleWidth = array.getDimension(R.styleable.DoughnutView_dv_circle_width, mCircleWidth);

            array.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int cWidth = getWidth() - getPaddingStart() - getPaddingEnd();
        int cHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        // 获取圆心的x坐标
        centerX = cWidth / 2f;
        // 获取圆心的y坐
        centerY = cHeight / 2f;

        if (mCircleWidth == 0) {
            mCircleWidth = centerY / 4f;
        }
        mCircleRadius = centerY - mCircleWidth / 2f;

        mRectF.left = centerX - mCircleRadius;
        mRectF.top = centerY - mCircleRadius;
        mRectF.right = centerX + mCircleRadius;
        mRectF.bottom = centerY + mCircleRadius;

//        mCircleWidth = 1f;
        mArcPaint.setStrokeWidth(mCircleWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        // 移动画布
        canvas.translate(getPaddingStart(), getPaddingTop());

        // 画出每段圆环
        if (dataList != null && dataList.size() > 0) {
            int size = dataList.size();
            float endAngle = mStartAngle;
            for (int index = 0; index < size; index++) {
                DoughnutData temp = dataList.get(index);
                // 画圆弧
                mArcPaint.setColor(temp.getColor());
                canvas.drawArc(mRectF, endAngle, temp.getAngle(), false, mArcPaint);

                endAngle += temp.getAngle();
            }
        }

        canvas.restore();
    }
}