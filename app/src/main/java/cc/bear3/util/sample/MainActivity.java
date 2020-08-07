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
                "100.0",
                "unit",
                "explain");
        dataList.add(data1);
        CircleChartData data2 = new CircleChartData(100.0,
                Color.BLUE,
                "200.0",
                "unit",
                "explain");
        dataList.add(data2);
        CircleChartData data3 = new CircleChartData(100.0,
                Color.BLACK,
                "300.0",
                "unit",
                "explain");
        dataList.add(data3);
//        CircleChartData data4 = new CircleChartData(100.0,
//                Color.DKGRAY,
//                "300.0",
//                "unit",
//                "explain");
//        dataList.add(data4);
//        CircleChartData data5 = new CircleChartData(100.0,
//                Color.GRAY,
//                "300.0",
//                "unit",
//                "explain");
//        dataList.add(data5);
//        CircleChartData data6 = new CircleChartData(100.0,
//                Color.LTGRAY,
//                "300.0",
//                "unit",
//                "explain");
//        dataList.add(data6);
//        CircleChartData data7 = new CircleChartData(100.0,
//                Color.YELLOW,
//                "300.0",
//                "unit",
//                "explain");
//        dataList.add(data7);
//        CircleChartData data8 = new CircleChartData(100.0,
//                Color.CYAN,
//                "300.0",
//                "unit",
//                "explain");
//        dataList.add(data8);

        view.updateData(null, dataList);
    }
}