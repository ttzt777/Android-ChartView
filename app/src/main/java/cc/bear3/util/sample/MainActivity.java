package cc.bear3.util.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cc.bear3.util.chartview.circle.CircleChartData;
import cc.bear3.util.chartview.circle.CircleChartView;
import cc.bear3.util.chartview.doughnut.DoughnutData;
import cc.bear3.util.chartview.doughnut.DoughnutView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CircleChartView view = findViewById(R.id.view);

        List<CircleChartData> dataList = new ArrayList<>();
        CircleChartData data1 = new CircleChartData(1,
                0xFF5E8CFF,
                "2.35728",
                "BTC",
                "昨日电费支出");
        dataList.add(data1);
        CircleChartData data2 = new CircleChartData(1,
                0xFF0EC8B7,
                "2.45453",
                "BTC",
                "昨日管理费支出");
        dataList.add(data2);
        CircleChartData data3 = new CircleChartData(10,
                0xFFF4B945,
                "5.5455454",
                "BTC",
                "昨日总收益");
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

        CircleChartData center = new CircleChartData(5.4556165,
                Color.CYAN,
                "5.4556165",
                "BTC",
                "昨日总产出");

        view.updateData(center, dataList);

        showDoughnut();
    }

    private void showDoughnut() {
        DoughnutView view = findViewById(R.id.doughnut);
        List<DoughnutData> dataList  = new ArrayList<>();
        DoughnutData data1 = new DoughnutData(1,
                0xFF5E8CFF);
        dataList.add(data1);
        DoughnutData data2 = new DoughnutData(1,
                0xFF0EC8B7);
        dataList.add(data2);
        DoughnutData data3 = new DoughnutData(10,
                0xFFF4B945);
        dataList.add(data3);

        view.updateData(dataList);
    }
}