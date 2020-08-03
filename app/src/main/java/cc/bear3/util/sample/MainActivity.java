package cc.bear3.util.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cc.bear3.util.chartview.circle.CircleChartData;
import cc.bear3.util.chartview.circle.CircleChartView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CircleChartView view = findViewById(R.id.view);

        List<CircleChartData> dataList = new ArrayList<>();
        CircleChartData data1 = new CircleChartData(100.0,
                Color.RED,
                new CircleChartData.TextData("100.0", 0xEEEEEE, 13, true),
                new CircleChartData.TextData("unit", 0xEEEEEE, 13, true),
                new CircleChartData.TextData("explain", 0xEEEEEE, 13, true));
        CircleChartData data2 = new CircleChartData(200.0,
                Color.BLUE,
                new CircleChartData.TextData("200.0", 0xEEEEEE, 13, true),
                new CircleChartData.TextData("unit", 0xEEEEEE, 13, true),
                new CircleChartData.TextData("explain", 0xEEEEEE, 13, true));
        CircleChartData data3 = new CircleChartData(100.0,
                Color.BLACK,
                new CircleChartData.TextData("300.0", 0xEEEEEE, 13, true),
                new CircleChartData.TextData("unit", 0xEEEEEE, 13, true),
                new CircleChartData.TextData("explain", 0xEEEEEE, 13, true));
        dataList.add(data1);
        dataList.add(data2);
        dataList.add(data3);

        view.updateData(null, dataList);
    }
}