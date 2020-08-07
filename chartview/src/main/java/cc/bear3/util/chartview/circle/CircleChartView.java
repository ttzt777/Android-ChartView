package cc.bear3.util.chartview.circle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.List;

/**
 * 自定义环形统计图 - Path绘制
 */

public class CircleChartView extends View {
    private static final float mStartAngle = -90;           // 默认起点扇形角度

    // 圈环的宽度
    private float mCircleWidth = 0;
    private float mCircleRadius;
    // 圆点的半径
    private float mDotRadius = 6f;
    private float mDotOutRadius = 8f;
    private float mDotMargin = 2f;
    private float mDotOutAlpha = 0.25f;

    private float mValueMarginStart = 6f;
    private float mValueMarginBottom = 6f;
    private float mExplainMargin = 6f;
    private float mUnitMargin = 4f;

    private float mLineTurnMargin = 30f;
    private float mLineWidth = 2f;

    private int mValueColor = 0xFF333333;
    private float mValueSize = 24;

    private int mUnitColor = 0xFF666666;
    private float mUnitSize = 16;

    private int mExplainColor = 0xFF666666;
    private float mExplainSize = 16;
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
    private Paint mPaint;
    /**
     * 绘制文本的画笔
     */
    private Paint mTextPaint;

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
        init(context);
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

    private void init(Context context) {
        // 圆环画笔
        mPaint = new Paint();
        // 消除锯齿
        mPaint.setAntiAlias(true);
        // 设置填充
        mPaint.setStyle(Paint.Style.STROKE);
        // 绘制文本的画笔
        mTextPaint = new Paint();
        // 设置线和字体的宽度
        mTextPaint.setStrokeWidth(2);
        // 消除锯齿
        mTextPaint.setAntiAlias(true);
        // 设置字体大小
//        mTextPaint.setTextSize(mTextSize);
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

        mCircleRadius = centerY;

        mRectF.left = centerX - mCircleRadius;
        mRectF.top = centerY - mCircleRadius;
        mRectF.right = centerX + mCircleRadius;
        mRectF.bottom = centerY + mCircleRadius;

        mCircleWidth = mCircleRadius / 8f;
//        mCircleWidth = 1f;
        mPaint.setStrokeWidth(mCircleWidth);
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
                CircleChartData temp = dataList.get(index);
                // 画圆弧
                mPaint.setColor(temp.getColor());
                canvas.drawArc(mRectF, endAngle, temp.getAngle(), false, mPaint);

                endAngle += temp.getAngle();

                // 获取圆弧中心点
                float centerAngle = endAngle - temp.getAngle() / 2f;
                float x = (float) (centerX + mCircleRadius * Math.cos(Math.toRadians(centerAngle)));
                float y = (float) (centerY + mCircleRadius * Math.sin(Math.toRadians(centerAngle)));

                // 获取dot的中心点
                getOffsetPoint(x, y, mCircleWidth / 2f + mDotMargin + mDotOutRadius, tempPoint);
                // 画出小圆圈 - 外部
                mTextPaint.setStyle(Paint.Style.FILL);
                mTextPaint.setColor(temp.getColor());
                mTextPaint.setAlpha((int) (mDotOutAlpha * 255));
                canvas.drawCircle(tempPoint.x, tempPoint.y, mDotOutRadius, mTextPaint);
                //  - 内部
                mTextPaint.setAlpha(255);
                canvas.drawCircle(tempPoint.x, tempPoint.y, mDotRadius, mTextPaint);

                // 获取Dot外部坐标点
                getOffsetPoint(tempPoint.x, tempPoint.y, mDotRadius, tempPoint);

                // 测量value文字
                mTextPaint.setTextSize(mValueSize);
                mTextPaint.getTextBounds(temp.getValueString(), 0, temp.getValueString().length(), bounds);
                int valueW = bounds.right - bounds.left;
                int valueH = bounds.bottom - bounds.top;

                // 测量Unit文字
                mTextPaint.setTextSize(mUnitSize);
                mTextPaint.getTextBounds(temp.getUnit(), 0, temp.getUnit().length(), bounds);
                int unitW = bounds.right - bounds.left;
                int unitH = bounds.bottom - bounds.top;

                // 画出折线
                // 获取折线转折点
                mTextPaint.setStyle(Paint.Style.STROKE);
                mTextPaint.setColor(temp.getColor());
                mTextPaint.setStrokeWidth(mLineWidth);
                float startX = tempPoint.x;
                float startY = tempPoint.y;
                getOffsetPoint(tempPoint.x, tempPoint.y, mLineTurnMargin, tempPoint);
                // 第一段
                canvas.drawLine(startX, startY, tempPoint.x, tempPoint.y, mTextPaint);
                // 第二段
                startX = tempPoint.x;
                startY = tempPoint.y;
                getOffsetPoint(startX, startY, mValueMarginStart + valueW + mUnitMargin + unitW, tempPoint);
                canvas.drawLine(startX, startY, tempPoint.x, startY, mTextPaint);

                float endX = tempPoint.x;
                mTextPaint.setTextAlign(Paint.Align.CENTER);
                // value
                mTextPaint.setTextSize(mValueSize);
                mTextPaint.setColor(mValueColor);
                if (startX >= centerX) {
                    canvas.drawText(temp.getValueString(), startX + mValueMarginStart + valueW / 2f, startY - mValueMarginBottom - valueH / 2f, mTextPaint);
                } else {
                    canvas.drawText(temp.getValueString(), endX + valueW / 2f, startY - mValueMarginBottom - valueH / 2f, mTextPaint);
                }
                // unit
                mTextPaint.setTextSize(mUnitSize);
                mTextPaint.setColor(mUnitColor);
                if (startX >= centerX) {
                    canvas.drawText(temp.getUnit(), startX + mValueMarginStart + valueW + mUnitMargin + unitW / 2f, startY - mValueMarginBottom - unitH / 2f, mTextPaint);
                } else {
                    canvas.drawText(temp.getUnit(), endX + mUnitMargin + valueW + unitW / 2f, startY - mValueMarginBottom - unitH / 2f, mTextPaint);
                }
                // explain
                mTextPaint.setTextSize(mExplainSize);
                mTextPaint.setColor(mExplainColor);
                mTextPaint.getTextBounds(temp.getExplain(), 0, temp.getExplain().length(), bounds);
                int explainW = bounds.right - bounds.left;
                int explainH = bounds.bottom - bounds.top;
                if (startX >= centerX) {
                    canvas.drawText(temp.getExplain(), endX - explainW / 2f, startY + mExplainMargin + explainH / 2f, mTextPaint);
                } else {
                    canvas.drawText(temp.getExplain(), endX + explainW / 2f, startY + mExplainMargin + explainH / 2f, mTextPaint);
                }

                // 中心文字

//                drawText(canvas, temp, x, y);
            }
        }

        // 画出中心参数
        if (centerData != null) {

        }


//        // 计算外圆和内圆的半径
//        if (centerX < centerY) {
//            mOuterRadius = centerX / 2f;// 外圆半径
//        } else {
//            mOuterRadius = centerY / 2f;
//        }
//        if (mCircleWidth <= 0 || mCircleWidth > mOuterRadius) {
//            mInnerRadius = mOuterRadius / 2f;// 内圆半径
//        } else {
//            mInnerRadius = mOuterRadius - mCircleWidth;
//        }
//        // 设置外圆的颜色
//        mPaint.setColor(mRingColor);
//        mPath.addCircle(centerX, centerY, mOuterRadius, Path.Direction.CCW);
//        mPath.close();
//        // 绘制外圆
//        canvas.drawPath(mPath, mPaint);
//        mPath.reset();
//        // 绘制进度扇形区域
//        getSectorClip(canvas, mStartAngle);
//        // 设置内圆的颜色
//        mPaint.setColor(Color.WHITE);
//        mPath.addCircle(centerX, centerY, mInnerRadius, Path.Direction.CCW);
//        mPath.close();
//// 绘制内圆
//        canvas.drawPath(mPath, mPaint);
//        mPath.reset();
//// 获取进度圆弧的中心点
//        float textX1 = (float) (centerX + mOuterRadius * Math.cos((mSweepAngle / 2 - 90) * Math.PI / 180));
//        float textY1 = (float) (centerY + mOuterRadius * Math.sin((mSweepAngle / 2 - 90) * Math.PI / 180));
//// 设置画笔颜色
//        mTextPaint.setColor(mSectorColor);
//        drawText(canvas, progressText + getPer(mSweepAngle, 360), textX1, textY1, mTextPaint);
//// 获取进度圆弧的中心点
//        float textX2 = (float) (centerX + mOuterRadius * Math.cos(((mSweepAngle - 360) / 2 - 90) * Math.PI / 180));
//        float textY2 = (float) (centerY + mOuterRadius * Math.sin(((mSweepAngle - 360) / 2 - 90) * Math.PI / 180));
//// 设置画笔颜色
//        mTextPaint.setColor(mRingColor);
//        drawText(canvas, reminderText + getPer(360 - mSweepAngle, 360), textX2, textY2, mTextPaint);

        canvas.restore();
    }

    private float getOffsetX(float startX, float offset) {
        if (startX >= centerX) {
            return startX + offset;
        } else {
            return startX - offset;
        }
    }

//    /**
//     * 绘制一个扇形的裁剪区
//     *
//     * @param canvas     画布
//     * @param startAngle 扇形的起始角度
//     */
//    private void getSectorClip(Canvas canvas, float startAngle) {
//// 进度的颜色
//        mPaint.setColor(mSectorColor);
//        Path path = new Path();
//// 以下获得一个三角形的裁剪区
//// 圆心
//        path.moveTo(centerX, centerY);
//// 起始点角度在圆上对应的横坐标
//        float mStartAngleX = (float) (centerX + mOuterRadius * Math.cos(startAngle * Math.PI / 180));
//// 起始点角度在圆上对应的纵坐标
//        float mStartAngleY = (float) (centerY + mOuterRadius * Math.sin(startAngle * Math.PI / 180));
//        path.lineTo(mStartAngleX, mStartAngleY);
//// 终点角度在圆上对应的横坐标
//        float mEndAngleX = (float) (centerX + mOuterRadius * Math.cos(mEndAngle * Math.PI / 180));
//// 终点点角度在圆上对应的纵坐标
//        float mEndAngleY = (float) (centerY + mOuterRadius * Math.sin(mEndAngle * Math.PI / 180));
//        path.lineTo(mEndAngleX, mEndAngleY);
//// 回到初始点形成封闭的曲线
//        path.close();
//// 设置一个正方形，内切圆
//        RectF rectF = new RectF(centerX - mOuterRadius, centerY - mOuterRadius, centerX + mOuterRadius, centerY + mOuterRadius);
//// 获得弧形剪裁区的方法
//        path.addArc(rectF, startAngle, mEndAngle - startAngle);
//        canvas.drawPath(path, mPaint);
//    }
//
//    /**
//     * 计算百分比
//     *
//     * @return 返回比例
//     */
//    private String getPer(float now, float total) {
//// 创建一个数值格式化对象
//        NumberFormat numberFormat = NumberFormat.getInstance();
//// 设置精确到小数点后2位
//        numberFormat.setMaximumFractionDigits(2);
//        String result = numberFormat.format(now / total * 100);
//        return result + “%”;
//    }
//
//    /**
//     * 设置圆环宽度
//     *
//     * @param width 圆环宽度
//     */
//    public void setCircleWidth(int width) {
//        mCircleWidth = width;
//        invalidate();
//    }
//
//    /**
//     * 设置当前进度
//     */
//    public void setPercentage(float ring, float sector) {
//        mSweepAngle = (sector / (ring + sector)) * 360f;
//        mEndAngle = mStartAngle + mSweepAngle;
//        postInvalidate();
//    }

    private float mOffset1X = 20;
    private float mOffset1Y = 20;
    private float mOffset2X = 150;
    private float mOffset2Y = 20;
    private float mPointOffset = 10;

    /**
     * 绘制文字说明
     */
    private void drawText(Canvas canvas, CircleChartData data, float firstX, float firstY) {
        float endX1 = 0;
        float endY1 = 0;
        float endX2 = 0;
        float endY2 = 0;
        float textX = 0;
        float textY = 0;
        //初始点位于第四象限
        if (firstX <= centerX && firstY <= centerY) {
            firstX = firstX - mPointOffset;
            firstY = firstY - mPointOffset;
            endX1 = firstX - mOffset1X;
            endY1 = firstY - mOffset1Y;
            endX2 = firstX - mOffset2X;
            endY2 = firstY - mOffset2Y;
            textX = endX2;
            textY = endY2 - 10;
        }
        //初始点位于第三象限
        if (firstX < centerX && firstY > centerY) {
            firstX = firstX - mPointOffset;
            firstY = firstY + mPointOffset;
            endX1 = firstX - mOffset1X;
            endY1 = firstY + mOffset1Y;
            endX2 = firstX - mOffset2X;
            endY2 = firstY + mOffset2Y;
            textX = endX2;
            textY = endY2 + 30;
        }
        //初始点位于第一象限
        if (firstX > centerX && firstY < centerY) {
            firstX = firstX + mPointOffset;
            firstY = firstY - mPointOffset;
            endX1 = firstX + mOffset1X;
            endY1 = firstY - mOffset1Y;
            endX2 = firstX + mOffset2X;
            endY2 = firstY - mOffset2Y;
            textX = endX1;
            textY = endY2 - 10;
        }
        //初始点位于第二象限
        if (firstX >= centerX && firstY >= centerY) {
            firstX = firstX + mPointOffset;
            firstY = firstY + mPointOffset;
            endX1 = firstX + mOffset1X;
            endY1 = firstY + mOffset1Y;
            endX2 = firstX + mOffset2X;
            endY2 = firstY + mOffset2Y;
            textX = endX1;
            textY = endY2 + 30;
        }
        mTextPaint.setColor(data.getColor());

        canvas.drawCircle(firstX, firstY, mDotRadius, mTextPaint);
        canvas.drawLine(firstX, firstY, endX1, endY1, mTextPaint);
        canvas.drawLine(endX1, endY1, endX2, endY2, mTextPaint);
//        canvas.drawText(string, textX, textY, paint);
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
}