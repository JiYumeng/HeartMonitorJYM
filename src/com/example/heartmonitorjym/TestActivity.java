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
        //������main�����ϵĲ��֣�������ͼ���������������  
        LinearLayout layout = (LinearLayout)findViewById(R.id.test_graphic);          
        mDataset.removeSeries(series);
        series.clear();
       
        for(int i=0; i<100; i++){
        	
        	float x=i;
        	float y=(int)(Math.random() * 90);
        	series.add(x, y);
        	}
              
        mDataset.addSeries(series);  
          
        //���¶������ߵ���ʽ�����Եȵȵ����ã�renderer�൱��һ��������ͼ������Ⱦ�ľ��  
        int color = Color.RED;  
        PointStyle style = PointStyle.POINT;  
        renderer = buildRenderer(color, style, true);  
          
        //���ú�ͼ�����ʽ  
        setChartSettings(renderer, "X", "Y", 0, 31, 0, 100, Color.BLACK, Color.BLACK);  
          
        //����ͼ��  
        chart = ChartFactory.getLineChartView(context, mDataset, renderer);  
          
        //��ͼ����ӵ�������ȥ  
        layout.addView(chart, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));                  
	
	}
	
	 protected XYMultipleSeriesRenderer buildRenderer(int color, PointStyle style, boolean fill) {  
	        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();  
	          
	        //����ͼ�������߱������ʽ��������ɫ����Ĵ�С�Լ��ߵĴ�ϸ��  
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
	        //�йض�ͼ�����Ⱦ�ɲο�api�ĵ�  
	    	//renderer.setApplyBackgroundColor(false);
	       // renderer.setChartTitle("8�����ر仯����");  
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
	        //renderer.setXTitle("����"); 
	        //renderer.setYTitle("����");  
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
