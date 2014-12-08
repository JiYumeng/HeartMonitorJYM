package com.example.heartmonitorjym;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class TestActivity extends Activity {

	private Timer timer = new Timer();  
    private TimerTask task;  
    private Handler handler;  
    private String title = "Signal Strength";  
    private XYSeries series;  
    private XYMultipleSeriesDataset mDataset;  
    private GraphicalView chart;  
    private XYMultipleSeriesRenderer renderer;  
    private Context context;  
    private int addX = -1, addY;  
    
      
    int[] xv = new int[10];  
    int[] yv = new int[10];  
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        series = new XYSeries(title); 
        mDataset = new XYMultipleSeriesDataset();
    }
	
	
	@Override
    protected void onResume(){
    	super.onResume();
    	//Log.d("onResume", "onResume Method is executed");
    	initGraph();
    }


	private void initGraph() {
		// TODO Auto-generated method stub
		
    	context = getApplicationContext();          
        //这里获得main界面上的布局，下面会把图表画在这个布局里面  
        LinearLayout layout = (LinearLayout)findViewById(R.id.test_graphic);          
        mDataset.removeSeries(series);
        series.clear();
       
        for(int i=0; i<100; i++){
        	
        	float x=i;
        	float y=(int)(Math.random() * 90);
        	series.add(x, y);
        	}
              
        mDataset.addSeries(series);  
          
        //以下都是曲线的样式和属性等等的设置，renderer相当于一个用来给图表做渲染的句柄  
        int color = Color.RED;  
        PointStyle style = PointStyle.POINT;  
        renderer = buildRenderer(color, style, true);  
          
        //设置好图表的样式  
        setChartSettings(renderer, "X", "Y", 0, 31, 0, 100, Color.BLACK, Color.BLACK);  
          
        //生成图表  
        chart = ChartFactory.getLineChartView(context, mDataset, renderer);  
          
        //将图表添加到布局中去  
        layout.addView(chart, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));                  
	
	}
	
	 protected XYMultipleSeriesRenderer buildRenderer(int color, PointStyle style, boolean fill) {  
	        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();  
	          
	        //设置图表中曲线本身的样式，包括颜色、点的大小以及线的粗细等  
	        XYSeriesRenderer r = new XYSeriesRenderer();  
	        r.setColor(color);  
	        r.setPointStyle(style);  
	        r.setFillPoints(fill);  
	        r.setLineWidth(3);  
	        renderer.addSeriesRenderer(r);  
	          
	        return renderer;  
	    }  
	      
	    protected void setChartSettings(XYMultipleSeriesRenderer renderer, String xTitle, String yTitle,  
	                                    double xMin, double xMax, double yMin, double yMax, int axesColor, int labelsColor) {  
	        //有关对图表的渲染可参看api文档  
	    	//renderer.setApplyBackgroundColor(false);
	       // renderer.setChartTitle("8月体重变化趋势");  
	       // renderer.setXTitle(xTitle);  
	       // renderer.setYTitle(yTitle);  
	        renderer.setXAxisMin(xMin);  
	        renderer.setXAxisMax(xMax);  
	        renderer.setYAxisMin(yMin);  
	        renderer.setYAxisMax(yMax);  
	        renderer.setAxesColor(axesColor);  
	        //render.setLabelsColor(Color.BLACK);
	        renderer.setLabelsColor(labelsColor);  
	        renderer.setLabelsColor(Color.BLACK);
	        renderer.setShowGrid(true);  
	        renderer.setGridColor(Color.GRAY);  
	        renderer.setXLabels(20);  
	        renderer.setYLabels(10);  
	        //renderer.setXTitle("日期"); 
	        //renderer.setYTitle("体重");  
	        renderer.setMarginsColor(Color.argb(0, 0xff, 0, 0));        
	        renderer.setBackgroundColor(Color.WHITE);
	        //renderer.setMarginsColor(Color.GRAY);
	        //renderer.setLabelsColor(Color.WHITE);
	        renderer.setMargins(new int[] {50, 40, 40,20});
	        renderer.setChartTitleTextSize((float) 40);
	        renderer.setAxisTitleTextSize((float) 20);
	        renderer.setLabelsTextSize((float) 30);
	        renderer.setYLabelsAlign(Align.RIGHT);  
	        renderer.setPointSize((float) 8);  
	        renderer.setShowLegend(false);  
	        renderer.setPanLimits(new double[]{0,31,0,200});
	        renderer.setZoomLimits(new double[]{0,31,0,200});
	        
	    }  
	

}
