package cc.bear3.util.chartview.circle;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * 圆形图标数据封装
 *
 * @author TT
 * @since 2020-8-3
 */
public class CircleChartData {
    private double value;
    private int color;
    private TextData valueString;
    private TextData unit;
    private TextData explain;
    private float angle;      // 用于后面参与计算，不用设置初值

    public CircleChartData(double value, int color, TextData valueString, TextData unit, TextData explain) {
        this.value = value;
        this.color = color;
        this.valueString = valueString;
        this.unit = unit;
        this.explain = explain;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public TextData getValueString() {
        return valueString;
    }

    public void setValueString(TextData valueString) {
        this.valueString = valueString;
    }

    public TextData getUnit() {
        return unit;
    }

    public void setUnit(TextData unit) {
        this.unit = unit;
    }

    public TextData getExplain() {
        return explain;
    }

    public void setExplain(TextData explain) {
        this.explain = explain;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public static class TextData {
        private String text;
        private int color;
        private int size;

        public TextData(String text, int color, int size) {
            this(text, color, size, false);
        }

        public TextData(String text, int color, int size, boolean isSizeDp) {
            this.text = text;
            this.color = color;
            if (isSizeDp) {
                this.size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, Resources.getSystem().getDisplayMetrics());
            } else {
                this.size = size;
            }
        }

        public String getText() {
            return text;
        }

        public int getColor() {
            return color;
        }

        public int getSize() {
            return size;
        }
    }
}
