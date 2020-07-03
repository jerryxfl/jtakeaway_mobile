package com.jerry.jtakeaway.ui.user.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.jerry.jtakeaway.R;
import com.jerry.jtakeaway.base.BaseActivity;
import com.jerry.jtakeaway.bean.responseBean.ResponseTransaction;
import com.jerry.jtakeaway.custom.AniImgButton;
import com.jerry.jtakeaway.utils.PixAndDpUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

@SuppressWarnings("all")
public class TableActivity extends BaseActivity {
    @BindView(R.id.top)
    View top;


    @BindView(R.id.return_aib)
    AniImgButton return_aib;

    @BindView(R.id.selectDate)
    TextView selectDate;

    @BindView(R.id.table)
    LineChartView table;


    private List<Line> lines = new ArrayList<>();


    private List<ResponseTransaction> responseTransactionList = new ArrayList<>();
    private TimePickerView pvTime;
    private Date nowDate = null;
    private int totalDays = 0;
    private float minY = 0f;//Y轴坐标最小值
    private float maxY = 100f;//Y轴坐标最大值

    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private List<AxisValue> mAxisYValues = new ArrayList<AxisValue>();

    private boolean hasAxesY = false; //是否需要Y坐标

    @Override
    public int getLayout() {
        return R.layout.activity_table;
    }

    @Override
    public void InitView() {
        ViewGroup.LayoutParams layoutParams = top.getLayoutParams();
        layoutParams.height = PixAndDpUtil.getStatusBarHeight(this);
        top.setLayoutParams(layoutParams);

        initTimePicker();
    }

    private void getAxisXYLables() {
        for (int i = 0; i < totalDays; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(nowDate.getMonth()+"-"+(i+1)));
        }
        for (float i = minY; i <= maxY; i+=10) {
            mAxisYValues.add(new AxisValue(i).setLabel(i+""));
        }
    }

    private void getAxisPoints() {
        for (int i = 0; i < responseTransactionList.size(); i++) {
            mPointValues.add(new PointValue(i, Float.parseFloat(responseTransactionList.get(i).getJtransaction().getPaymoney().toString())));
        }
    }



    @Override
    public void InitData() {
        Intent intent = getIntent();
        responseTransactionList = (List<ResponseTransaction>) intent.getSerializableExtra("TRANSACTION");
        nowDate = new Date();
        setTime(nowDate);

//        dealData.start();
    }

    private void InitLines() {
        Line line = new Line(mPointValues).setColor(Color.parseColor("#69c0ff"));  //折线的颜色
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状
        line.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line.setFilled(true);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴X
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.GRAY);  //设置字体颜色
        axisX.setMaxLabelChars(0);
        axisX.setValues(mAxisXValues);
        data.setAxisXBottom(axisX);//x 轴在底部

        //坐标轴Y
        if (hasAxesY) {
            Axis axisY = new Axis();
            axisY.setHasLines(false);
            axisY.setValues(mAxisYValues);
            data.setAxisYLeft(axisY);
        }

        //设置行为属性，支持缩放、滑动以及平移
        table.setInteractive(true);
        table.setZoomType(ZoomType.HORIZONTAL);
        table.setMaxZoom((float) 4);//最大方法比例
        table.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        table.setLineChartData(data);
        table.setVisibility(View.VISIBLE);

        Viewport v = new Viewport(table.getMaximumViewport());
        v.bottom = minY;
        v.top = maxY;
        //固定Y轴的范围,如果没有这个,Y轴的范围会根据数据的最大值和最小值决定,这不是我想要的
        table.setMaximumViewport(v);

        //这2个属性的设置一定要在lineChart.setMaximumViewport(v);这个方法之后,不然显示的坐标数据是不能左右滑动查看更多数据的
        v.left = totalDays - 7;
        v.right = totalDays - 1;
        table.setCurrentViewport(v);
    }


    //数据处理线程
    private Thread dealData = new Thread(new Runnable() {
        @Override
        public void run() {

        }
    });



    @Override
    public void InitListener() {
        return_aib.setOnClickListener(v -> finish());
        selectDate.setOnClickListener(v -> {
            pvTime.show();
        });
    }

    @Override
    public void destroy() {

    }
    private void initTimePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR,-2);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.YEAR,2);
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                nowDate = date;
                setTime(date);
            }
        })
                .setType(new boolean[]{true, true, false, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentSize(18)//滚轮文字大小
                .setTitleText("选择时间")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                .setCancelColor(Color.GRAY)//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setDate(Calendar.getInstance())// 如果不设置的话，默认是系统时间*/
                .setRangDate(calendar, calendar2)//起始终止年月日设定
                // //.setLabel("年","月","日","时","分","秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();
    }

    private void setTime(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
        selectDate.setText(format.format(date));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        totalDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println(totalDays+"天");
        getAxisXYLables();
        getAxisPoints();
        InitLines();
    }
}