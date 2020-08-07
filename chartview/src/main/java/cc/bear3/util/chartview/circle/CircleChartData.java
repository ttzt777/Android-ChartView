package cc.bear3.util.chartview.circle;

import androidx.annotation.NonNull;

/**
 * 圆形图标数据封装
 *
 * @author TT
 * @since 2020-8-3
 */
public class CircleChartData {
    private double value;
    private int color;
    private @NonNull
    String valueString;
    private @NonNull
    String unit;
    private @NonNull
    String explain;
    private float angle;      // 用于后面参与计算，不用设置初值

    public CircleChartData(double value, int color, @NonNull String valueString, @NonNull String unit, @NonNull String explain) {
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

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
