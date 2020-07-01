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

    @BindView(R.id.table)
    LineChartView table;


    @BindView(R.id.return_aib)
    AniImgButton return_aib;

    @BindView(R.id.selectDate)
    TextView selectDate;


    List<String> dates = new ArrayList<String>();//X轴的标注
    List<Integer> score = new ArrayList<Integer>();//图表的数据点
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();

    private List<ResponseTransaction> responseTransactionList = new ArrayList<>();
    private TimePickerView pvTime;
    private Date nowDate = null;
    private int totalDays = 0;

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



    /**
     * 设置X 轴的显示
     */
    private void getAxisXLables() {
        for (int i = 0; i < totalDays; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(nowDate.getMonth()+"-"+totalDays));
        }
    }

    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints() {
        for (int i = 0; i < score.size(); i++) {
            mPointValues.add(new PointValue(i, score.get(i)));
        }
    }

    private void initLineChart() {
        Line line = new Line(mPointValues).setColor(Color.parseColor("#bae7ff"));  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.GRAY);  //设置字体颜色
        axisX.setName("日期");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("支出");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边


        //设置行为属性，支持缩放、滑动以及平移
        table.setInteractive(true);
        table.setZoomType(ZoomType.HORIZONTAL);
        table.setMaxZoom((float) 2);//最大方法比例
        table.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        table.setLineChartData(data);
        table.setVisibility(View.VISIBLE);
        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        Viewport v = new Viewport(table.getMaximumViewport());
        v.left = 0;
        v.right = totalDays;
        table.setCurrentViewport(v);
    }

    @Override
    public void InitData() {
        Intent intent = getIntent();
        responseTransactionList = (List<ResponseTransaction>) intent.getSerializableExtra("TRANSACTION");

        nowDate = new Date();
        setTime(nowDate);
        getAxisXLables();//获取x轴的标注
        getAxisPoints();//获取坐标点
        initLineChart();//初始化
//        dealData.start();
    }


    //数据处理线程
    private Thread dealData = new Thread(new Runnable() {

        @Override
        public void run() {
            for (ResponseTransaction response : responseTransactionList) {
                Date date = response.getJtransaction().getPaytime();
                boolean add = true;
                for (String s : dates) {
                    System.out.println("已有:" + s);
                    if (s.equals(date.getMonth() + "月")) {
                        add = false;
                        break;
                    }
                }
                if (add) dates.add(date.getMonth() + "月");

            }
            getAxisXLables();//获取x轴的标注
            getAxisPoints();//获取坐标点
            initLineChart();//初始化
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
    }
}