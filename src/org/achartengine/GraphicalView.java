/**
 * Copyright (C) 2009 - 2012 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.achartengine;

import org.achartengine.chart.AbstractChart;
import org.achartengine.chart.RoundChart;
import org.achartengine.chart.XYChart;
import org.achartengine.model.Point;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.tools.FitZoom;
import org.achartengine.tools.PanListener;
import org.achartengine.tools.Zoom;
import org.achartengine.tools.ZoomListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

/**
 * The view that encapsulates the graphical chart.
 * 封装了图形化图表 用于 显示在 GraphicalActivity
 */
public class GraphicalView extends View {
  /** The chart to be drawn. 需要绘制的图表 */
  private AbstractChart mChart;
  /** The chart renderer. 图表的渲染器(对于图标的设置一般都在这里) */
  private DefaultRenderer mRenderer;
  /** The view bounds. View的图形区域 */
  private Rect mRect = new Rect();
  /** The user interface thread handler. 用户接口线程的Handler。 */
  private Handler mHandler;
  /** The zoom buttons rectangle. 缩放按钮的矩形的区域。 */
  private RectF mZoomR = new RectF();
  /** The zoom in icon.放大图标。 */
  private Bitmap zoomInImage;
  /** The zoom out icon. 缩小图标。 */
  private Bitmap zoomOutImage;
  /** The fit zoom icon.适合1:1 图标。 */
  private Bitmap fitZoomImage;
  /** The zoom area size. 缩放区域大小。 */
  private int zoomSize = 50;
  /** The zoom buttons background color. 缩放按钮背景颜色。 */
  private static final int ZOOM_BUTTONS_COLOR = Color.argb(175, 150, 150, 150);
  /** The zoom in tool.放大工具 */
  private Zoom mZoomIn;
  /** The zoom out tool.缩小工具 */
  private Zoom mZoomOut;
  /** The fit zoom tool.适合1:1工具 */
  private FitZoom mFitZoom;
  /** The paint to be used when drawing the chart. 画图表用的画笔。 */
  private Paint mPaint = new Paint();
  /** The touch handler. 触摸事件的Handler */
  private ITouchHandler mTouchHandler;
  /** The old x coordinate. 老的X坐标? */
  private float oldX;
  /** The old y coordinate. 老的Y坐标? */
  private float oldY;
  /** If the graphical view is drawn. 如果图形视图 ?*/
  private boolean mDrawn;

  /**
   * Creates a new graphical view.
   * 创建一个新的视图View
   * @param context the context
   * @param chart the chart to be drawn
   */
  public GraphicalView(Context context, AbstractChart chart) {
    super(context);
    mChart = chart;
    mHandler = new Handler();
    if (mChart instanceof XYChart) {
      mRenderer = ((XYChart) mChart).getRenderer();
    } else {
      mRenderer = ((RoundChart) mChart).getRenderer();
    }
    if (mRenderer.isZoomButtonsVisible()) {//如果需要显示右下角的按钮这执行下面代码
      zoomInImage = BitmapFactory.decodeStream(GraphicalView.class
          .getResourceAsStream("image/zoom_in.png"));
      zoomOutImage = BitmapFactory.decodeStream(GraphicalView.class
          .getResourceAsStream("image/zoom_out.png"));
      fitZoomImage = BitmapFactory.decodeStream(GraphicalView.class
          .getResourceAsStream("image/zoom-1.png"));
    }

    if (mRenderer instanceof XYMultipleSeriesRenderer
        && ((XYMultipleSeriesRenderer) mRenderer).getMarginsColor() == XYMultipleSeriesRenderer.NO_COLOR) {
      ((XYMultipleSeriesRenderer) mRenderer).setMarginsColor(mPaint.getColor());
    }
    if (mRenderer.isZoomEnabled() && mRenderer.isZoomButtonsVisible()
        || mRenderer.isExternalZoomEnabled()) {
      mZoomIn = new Zoom(mChart, true, mRenderer.getZoomRate());
      mZoomOut = new Zoom(mChart, false, mRenderer.getZoomRate());
      mFitZoom = new FitZoom(mChart);
    }
    int version = 7;
    try {
      version = Integer.valueOf(Build.VERSION.SDK);
    } catch (Exception e) {
      // do nothing
    }
    if (version < 7) {
      mTouchHandler = new TouchHandlerOld(this, mChart);
    } else {
      mTouchHandler = new TouchHandler(this, mChart);
    }
  }

  /**
   * Returns the current series selection object.
   * 
   * @return the series selection
   */
  public SeriesSelection getCurrentSeriesAndPoint() {
    return mChart.getSeriesAndPointForScreenCoordinate(new Point(oldX, oldY));
  }

  /**
   * Transforms the currently selected screen point to a real point.
   * 
   * @param scale the scale
   * @return the currently selected real point
   */
  public double[] toRealPoint(int scale) {
    if (mChart instanceof XYChart) {
      XYChart chart = (XYChart) mChart;
      return chart.toRealPoint(oldX, oldY, scale);
    }
    return null;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvas.getClipBounds(mRect);
    int top = mRect.top;
    int left = mRect.left;
    int width = mRect.width();
    int height = mRect.height();
    if (mRenderer.isInScroll()) {
      top = 0;
      left = 0;
      width = getMeasuredWidth();
      height = getMeasuredHeight();
    }
    mChart.draw(canvas, left, top, width, height, mPaint);
    if (mRenderer != null && mRenderer.isZoomEnabled() && mRenderer.isZoomButtonsVisible()) {
      mPaint.setColor(ZOOM_BUTTONS_COLOR);
      zoomSize = Math.max(zoomSize, Math.min(width, height) / 7);
      mZoomR.set(left + width - zoomSize * 3, top + height - zoomSize * 0.775f, left + width, top
          + height);
      canvas.drawRoundRect(mZoomR, zoomSize / 3, zoomSize / 3, mPaint);
      float buttonY = top + height - zoomSize * 0.625f;
      canvas.drawBitmap(zoomInImage, left + width - zoomSize * 2.75f, buttonY, null);
      canvas.drawBitmap(zoomOutImage, left + width - zoomSize * 1.75f, buttonY, null);
      canvas.drawBitmap(fitZoomImage, left + width - zoomSize * 0.75f, buttonY, null);
    }
    mDrawn = true;
  }

  /**
   * Sets the zoom rate.
   * 
   * @param rate the zoom rate
   */
  public void setZoomRate(float rate) {
    if (mZoomIn != null && mZoomOut != null) {
      mZoomIn.setZoomRate(rate);
      mZoomOut.setZoomRate(rate);
    }
  }

  /**
   * Do a chart zoom in.
   */
  public void zoomIn() {
    if (mZoomIn != null) {
      mZoomIn.apply(Zoom.ZOOM_AXIS_XY);
      repaint();
    }
  }

  /**
   * Do a chart zoom out.
   */
  public void zoomOut() {
    if (mZoomOut != null) {
      mZoomOut.apply(Zoom.ZOOM_AXIS_XY);
      repaint();
    }
  }
  


  /**
   * Do a chart zoom reset / fit zoom.
   */
  public void zoomReset() {
    if (mFitZoom != null) {
      mFitZoom.apply();
      mZoomIn.notifyZoomResetListeners();
      repaint();
    }
  }

  /**
   * Adds a new zoom listener.
   * 
   * @param listener zoom listener
   */
  public void addZoomListener(ZoomListener listener, boolean onButtons, boolean onPinch) {
    if (onButtons) {
      if (mZoomIn != null) {
        mZoomIn.addZoomListener(listener);
        mZoomOut.addZoomListener(listener);
      }
      if (onPinch) {
        mTouchHandler.addZoomListener(listener);
      }
    }
  }

  /**
   * Removes a zoom listener.
   * 移除回调接口
   * @param listener zoom listener
   */
  public synchronized void removeZoomListener(ZoomListener listener) {
    if (mZoomIn != null) {
      mZoomIn.removeZoomListener(listener);
      mZoomOut.removeZoomListener(listener);
    }
    mTouchHandler.removeZoomListener(listener);
  }

  /**
   * Adds a new pan listener.
   * 
   * @param listener pan listener
   */
  public void addPanListener(PanListener listener) {
    mTouchHandler.addPanListener(listener);
  }

  /**
   * Removes a pan listener.
   * 
   * @param listener pan listener
   */
  public void removePanListener(PanListener listener) {
    mTouchHandler.removePanListener(listener);
  }

  /** 返回缩放按钮的矩形的区域。 */
  protected RectF getZoomRectangle() {
    return mZoomR;
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (event.getAction() == MotionEvent.ACTION_DOWN) {
      // save the x and y so they can be used in the click and long press
      // listeners
      oldX = event.getX();
      oldY = event.getY();
    }
    System.out.println("GraphicalView.onTouchEvent()  滑动");
    if (mRenderer != null && mDrawn && (mRenderer.isPanEnabled() || mRenderer.isZoomEnabled())) {
      if (mTouchHandler.handleTouch(event)) {
        return true;
      }
    }
    return super.onTouchEvent(event);
  }

  /**
   * Schedule a view content repaint.
   */
  public void repaint() {
    mHandler.post(new Runnable() {
      public void run() {
        invalidate();
      }
    });
  }

  /**
   * Schedule a view content repaint, in the specified rectangle area.
   * 
   * @param left the left position of the area to be repainted
   * @param top the top position of the area to be repainted
   * @param right the right position of the area to be repainted
   * @param bottom the bottom position of the area to be repainted
   */
  public void repaint(final int left, final int top, final int right, final int bottom) {
    mHandler.post(new Runnable() {
      public void run() {
        invalidate(left, top, right, bottom);
      }
    });
  }

  /**
   * Saves the content of the graphical view to a bitmap.
   * 
   * @return the bitmap
   */
  public Bitmap toBitmap() {
    setDrawingCacheEnabled(false);
    if (!isDrawingCacheEnabled()) {
      setDrawingCacheEnabled(true);
    }
    if (mRenderer.isApplyBackgroundColor()) {
      setDrawingCacheBackgroundColor(mRenderer.getBackgroundColor());
    }
    setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    return getDrawingCache(true);
  }

}