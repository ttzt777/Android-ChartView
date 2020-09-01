package cc.bear3.util.chartview.doughnut;

/**
 * 圆形图标数据封装
 *
 * @author TT
 * @since 2020-8-3
 */
public class DoughnutData {
    private double value;
    private int color;
    private float angle;      // 用于后面参与计算，不用设置初值

    public DoughnutData(double value, int color) {
        this.value = value;
        this.color = color;
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

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
