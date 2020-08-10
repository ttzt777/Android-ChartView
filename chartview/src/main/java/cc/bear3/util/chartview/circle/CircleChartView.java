package cc.bear3.util.chartview.circle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.List;

import cc.bear3.util.chartview.R;

/**
 * 自定义环形统计图 - Path绘制
 */

public class CircleChartView extends View {
    private static final float mStartAngle = -90;           // 默认起点扇形角度

    // 圈环的宽度
    private float mCircleWidth = 0;
    // 圆环半径
    private float mCircleRadius;
    // 圆点内圈的半径
    private float mDotRadius = dp2px(3f);
    // 圆点外圈透明度的半径
    private float mDotOutRadius = dp2px(5f);
    // 圆点到圆弧的距离
    private float mDotMargin = dp2px(2f);
    // 圆点外圈的透明度值
    private float mDotOutAlpha = 0.25f;

    // 文字（数值）距离左边的留白
    private float mValueMarginStart = dp2px(8f);
    // 文字（数值）距离底部折线的留白
    private float mValueMarginBottom = dp2px(1f);
    // 文字（说明）距离顶部折线的留白
    private float mExplainMargin = dp2px(2f);
    // 文字（单位）距离数值左边的留白
    private float mUnitMargin = dp2px(4f);

    // 折线拐点到圆点边缘的距离
    private float mLineTurnMargin = dp2px(11f);
    // 折线宽度
    private float mLineWidth = dp2px(0.5f);

    // 文字（数值）的颜色
    private int mValueColor = 0xFF242332;
    // 文字（数值）的大小
    private float mValueSize = sp2px(14f);

    // 文字（单位）的颜色
    private int mUnitColor = 0xFF242332;
    // 文字（单位）的大小
    private float mUnitSize = sp2px(12f);

    // 文字（说明）的颜色
    private int mExplainColor = 0xFF808080;
    // 文字（说明）的大小
    private float mExplainSize = sp2px(11f);
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
    /**
     * 绘制文本的画笔
     */
    private Paint mTextPaint;
    private Paint mLinePaint;

    private RectF mRectF = new RectF();

    private PointF tempPoint = new PointF();
    private Rect bounds = new Rect();

    private CircleChartData centerData;
    private List<CircleChartData> dataList;

    public CircleChartView(Context context) {
        this(context, null);
    }

    public CircleChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void updateData(CircleChartData centerData, List<CircleChartData> dataList) {
        this.centerData = centerData;

        if (dataList != null && dataList.size() > 0) {
            double total = 0;

            // 第一遍循环获取总数
            for (CircleChartData temp : dataList) {
                total += temp.getValue();
            }

            // 第二遍循环设置角度
            for (CircleChartData temp : dataList) {
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
        // 绘制文本的画笔
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleChartView);

            mCircleWidth = array.getDimension(R.styleable.CircleChartView_ccv_circle_width, mCircleWidth);

            mDotMargin = array.getDimension(R.styleable.CircleChartView_ccv_dot_margin, mDotMargin);
            mDotRadius = array.getDimension(R.styleable.CircleChartView_ccv_dot_radius_in, mDotRadius);
            mDotOutRadius = array.getDimension(R.styleable.CircleChartView_ccv_dot_radius_out, mDotOutRadius);
            mDotOutAlpha = array.getFloat(R.styleable.CircleChartView_ccv_dot_out_alpha, mDotOutAlpha);

            mLineTurnMargin = array.getDimension(R.styleable.CircleChartView_ccv_line_turn_margin, mLineTurnMargin);
            mLineWidth = array.getDimension(R.styleable.CircleChartView_ccv_line_width, mLineWidth);

            mValueSize = array.getDimension(R.styleable.CircleChartView_ccv_value_size, mValueSize);
            mValueColor = array.getColor(R.styleable.CircleChartView_ccv_value_color, mValueColor);
            mValueMarginStart = array.getDimension(R.styleable.CircleChartView_ccv_value_margin_start, mValueMarginStart);
            mValueMarginBottom = array.getDimension(R.styleable.CircleChartView_ccv_value_margin_bottom, mValueMarginBottom);

            mUnitSize = array.getDimension(R.styleable.CircleChartView_ccv_unit_size, mUnitSize);
            mUnitColor = array.getColor(R.styleable.CircleChartView_ccv_unit_color, mUnitColor);
            mUnitMargin = array.getDimension(R.styleable.CircleChartView_ccv_unit_margin, mUnitMargin);

            mExplainSize = array.getDimension(R.styleable.CircleChartView_ccv_explain_size, mExplainSize);
            mExplainColor = array.getColor(R.styleable.CircleChartView_ccv_explain_color, mExplainColor);
            mExplainMargin = array.getDimension(R.styleable.CircleChartView_ccv_explain_margin, mExplainMargin);

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
        Paint.FontMetrics fontMetrics;

        canvas.save();

        // 移动画布
        canvas.translate(getPaddingStart(), getPaddingTop());

        // 画出每段圆环
        if (dataList != null && dataList.size() > 0) {
            int size = dataList.size();
            float endAngle = mStartAngle;
            for (int index = 0; index < size; index++) {
                CircleChartData temp = dataList.get(index);
                // 画圆弧
                mArcPaint.setColor(temp.getColor());
                canvas.drawArc(mRectF, endAngle, temp.getAngle(), false, mArcPaint);

                endAngle += temp.getAngle();

                // 获取圆弧中心点
                float centerAngle = endAngle - temp.getAngle() / 2f;
                float x = (float) (centerX + mCircleRadius * Math.cos(Math.toRadians(centerAngle)));
                float y = (float) (centerY + mCircleRadius * Math.sin(Math.toRadians(centerAngle)));

                // 获取dot的中心点
                getOffsetPointWithAngle(x, y, mCircleWidth / 2f + mDotMargin + mDotOutRadius, centerAngle, tempPoint);
                // 画出小圆圈 - 外部
                mLinePaint.setStyle(Paint.Style.FILL);
                mLinePaint.setColor(temp.getColor());
                mLinePaint.setAlpha((int) (mDotOutAlpha * 255));
                canvas.drawCircle(tempPoint.x, tempPoint.y, mDotOutRadius, mLinePaint);
                //  - 内部
                mLinePaint.setAlpha(255);
                canvas.drawCircle(tempPoint.x, tempPoint.y, mDotRadius, mLinePaint);

                // 获取Dot外部坐标点
                getOffsetPoint(tempPoint.x, tempPoint.y, mDotRadius, tempPoint);

                // 测量value文字
                mTextPaint.setTextSize(mValueSize);
                mTextPaint.getTextBounds(temp.getValueString(), 0, temp.getValueString().length(), bounds);
                int valueW = bounds.right - bounds.left;
//                int valueH = bounds.bottom - bounds.top;

                // 测量Unit文字
                mTextPaint.setTextSize(mUnitSize);
                mTextPaint.getTextBounds(temp.getUnit(), 0, temp.getUnit().length(), bounds);
                int unitW = bounds.right - bounds.left;
//                int unitH = bounds.bottom - bounds.top;

                // 画出折线
                // 获取折线转折点
                mLinePaint.setStyle(Paint.Style.STROKE);
                mLinePaint.setColor(temp.getColor());
                mLinePaint.setStrokeWidth(mLineWidth);
                float startX = tempPoint.x;
                float startY = tempPoint.y;
                getOffsetPoint(tempPoint.x, tempPoint.y, mLineTurnMargin, tempPoint);
                // 第一段
                canvas.drawLine(startX, startY, tempPoint.x, tempPoint.y, mLinePaint);
                // 第二段
                startX = tempPoint.x;
                startY = tempPoint.y;
                getOffsetPoint(startX, startY, mValueMarginStart + valueW + mUnitMargin + unitW, tempPoint);
                canvas.drawLine(startX, startY, tempPoint.x, startY, mLinePaint);

                float endX = tempPoint.x;
                mTextPaint.setTextAlign(Paint.Align.CENTER);
                // value
                mTextPaint.setTextSize(mValueSize);
                mTextPaint.setColor(mValueColor);
                fontMetrics = mTextPaint.getFontMetrics();
                if (startX >= centerX) {
                    canvas.drawText(temp.getValueString(), startX + mValueMarginStart + valueW / 2f, startY - mValueMarginBottom - fontMetrics.bottom, mTextPaint);
                } else {
                    canvas.drawText(temp.getValueString(), endX + valueW / 2f, startY - mValueMarginBottom - fontMetrics.bottom, mTextPaint);
                }
                // unit
                mTextPaint.setTextSize(mUnitSize);
                mTextPaint.setColor(mUnitColor);
//                fontMetrics = mTextPaint.getFontMetrics();
                if (startX >= centerX) {
                    canvas.drawText(temp.getUnit(), startX + mValueMarginStart + valueW + mUnitMargin + unitW / 2f, startY - mValueMarginBottom - fontMetrics.bottom, mTextPaint);
                } else {
                    canvas.drawText(temp.getUnit(), endX + mUnitMargin + valueW + unitW / 2f, startY - mValueMarginBottom - fontMetrics.bottom, mTextPaint);
                }
                // explain
                mTextPaint.setTextSize(mExplainSize);
                mTextPaint.setColor(mExplainColor);
                mTextPaint.getTextBounds(temp.getExplain(), 0, temp.getExplain().length(), bounds);
                fontMetrics = mTextPaint.getFontMetrics();
                int explainW = bounds.right - bounds.left;
                int explainH = bounds.bottom - bounds.top;
                if (startX >= centerX) {
                    canvas.drawText(temp.getExplain(), endX - explainW / 2f, startY + mExplainMargin - fontMetrics.top, mTextPaint);
                } else {
                    canvas.drawText(temp.getExplain(), endX + explainW / 2f, startY + mExplainMargin - fontMetrics.top, mTextPaint);
                }
            }
        }

        // 画出中心参数
        if (centerData != null) {
//            mTextPaint.setTextSize(mValueSize);
//            mTextPaint.getTextBounds(centerData.getValueString(), 0, centerData.getValueString().length(), bounds);
//            int valueW = bounds.right - bounds.left;
//            int valueH = bounds.bottom - bounds.top;

//            mTextPaint.setTextSize(mUnitSize);
//            mTextPaint.getTextBounds(centerData.getUnit(), 0, centerData.getUnit().length(), bounds);
//            int unitW = bounds.right - bounds.left;
//            int unitH = bounds.bottom - bounds.top;

//            mTextPaint.setTextSize(mUnitSize);
//            mTextPaint.getTextBounds(centerData.getExplain(), 0, centerData.getExplain().length(), bounds);
//            int explainW = bounds.right - bounds.left;
//            int explainH = bounds.bottom - bounds.top;

            mTextPaint.setTextSize(mValueSize);
            mTextPaint.setColor(mValueColor);
            fontMetrics = mTextPaint.getFontMetrics();
            canvas.drawText(centerData.getValueString(), centerX, centerY - fontMetrics.bottom, mTextPaint);

            mTextPaint.setTextSize(mUnitSize);
            mTextPaint.setColor(mUnitColor);
            fontMetrics = mTextPaint.getFontMetrics();
            canvas.drawText(centerData.getUnit(), centerX, centerY - fontMetrics.top, mTextPaint);

            float unitH = fontMetrics.descent - fontMetrics.ascent;

            mTextPaint.setTextSize(mExplainSize);
            mTextPaint.setColor(mExplainColor);
            fontMetrics = mTextPaint.getFontMetrics();
            canvas.drawText(centerData.getExplain(), centerX, centerY + unitH + mExplainMargin - fontMetrics.top, mTextPaint);
        }

        canvas.restore();
    }

    /**
     * 获取偏移后的点的坐标
     *
     * @param oX     起始坐标x
     * @param oY     起始坐标y
     * @param offset 便宜量
     */
    private void getOffsetPoint(float oX, float oY, float offset, PointF target) {
        if ((oX - centerX) >= 0) {
            if ((oY - centerY) >= 0) {
                // 第一象限
                target.x = oX + offset;
                target.y = oY + offset;
            } else {
                // 第二象限
                target.x = oX + offset;
                target.y = oY - offset;
            }
        } else {
            if ((oY - centerY) < 0) {
                // 第三象限
                target.x = oX - offset;
                target.y = oY - offset;
            } else {
                // 第四象限
                target.x = oX - offset;
                target.y = oY + offset;
            }
        }
    }

    private void getOffsetPointWithAngle(float oX, float oY, float offset, float angle, PointF target) {
        float radians = (float) Math.toRadians(angle + 90);
        float tX = (float) Math.abs(offset * Math.sin(radians));
        float ty = (float) Math.abs(offset * Math.cos(radians));

        if ((oX - centerX) >= 0) {
            if ((oY - centerY) >= 0) {
                // 第一象限
                target.x = oX + tX;
                target.y = oY + ty;
            } else {
                // 第二象限
                target.x = oX + tX;
                target.y = oY - ty;
            }
        } else {
            if ((oY - centerY) < 0) {
                // 第三象限
                target.x = oX - tX;
                target.y = oY - ty;
            } else {
                // 第四象限
                target.x = oX - tX;
                target.y = oY + ty;
            }
        }
    }

    private float sp2px(Float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, getContext().getResources().getDisplayMetrics());
    }

    private float dp2px(Float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getContext().getResources().getDisplayMetrics());
    }

    //    private float getOffsetX(float startX, float offset) {
//        if (startX >= centerX) {
//            return startX + offset;
//        } else {
//            return startX - offset;
//        }
//    }
}