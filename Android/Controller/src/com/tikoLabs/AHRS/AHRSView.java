package com.tikoLabs.AHRS;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;

public class AHRSView extends View {
	private static float roll=25.0f,pitch=0.0f,yaw=0.0f;
	
	private final float ang_rad = 3.14159f/180.0f;
	private Paint horizontalLine;
	private Paint ahrsLine;
	private Paint downPoly;
	private Paint upPoly;
	private float centerX = 0; // Center view x position
	private float centerY = 0; // Center view y position
	private float shortLen = 50;
	private float longLen = 120;
	private float horPara=16.0f;
	private float pitchInterval = 20.0f;
	Path path;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		centerX = (getWidth()) / 2;
		centerY = (getHeight()) / 2;
		
		
		path.reset();
		path.moveTo(0, 0);
		path.lineTo(centerX*2, 0);
		path.lineTo(centerX*2, (float)(centerY-pitch*2-centerX*Math.tan(roll*ang_rad)));
		path.lineTo(0, (float)(centerY-pitch*2+centerX*Math.tan(roll*ang_rad)));
		path.lineTo(0, 0);

		canvas.drawPath(path, upPoly);
		
		path.reset();
		path.moveTo(0, centerY*2);
		path.lineTo(centerX*2, centerY*2);
		path.lineTo(centerX*2, centerY-pitch*2-centerX*(float)Math.tan(roll*ang_rad));
		path.lineTo(0, centerY-pitch*2+centerX*(float)Math.tan(roll*ang_rad));
		path.lineTo(0, centerY*2);

		canvas.drawPath(path, downPoly);
		
		// draw ahrs line
		for(int i=(int) (-pitch*2/pitchInterval)-(int) (centerY*2*2/pitchInterval); i<(int) (-pitch*2/pitchInterval)+centerY*2*2/pitchInterval; ++i)
		{
			drawAHRSLine(canvas, centerY-pitch*2 + pitchInterval*i, (i%2==0));
		}
		// draw center horizontal line 
		canvas.drawLine(centerX, centerY, centerX-horPara, centerY+horPara, horizontalLine);
		canvas.drawLine(centerX, centerY, centerX+horPara, centerY+horPara, horizontalLine);
		canvas.drawLine(centerX-horPara, centerY+horPara, centerX-horPara*2, centerY, horizontalLine);
		canvas.drawLine(centerX+horPara, centerY+horPara, centerX+horPara*2, centerY, horizontalLine);
		canvas.drawLine(centerX-horPara*2, centerY, centerX-horPara*4, centerY, horizontalLine);
		canvas.drawLine(centerX+horPara*2, centerY, centerX+horPara*4, centerY, horizontalLine);
		

	}
	private void drawAHRSLine(Canvas canvas, float yPos, boolean bLong)
	{
		float leng = shortLen;
		if(bLong) leng = longLen;
		leng /= 2.0f;
		float x1,x2,y1,y2;
		float delta = (float)((yPos-centerY)*Math.sin(roll*ang_rad));
		float deltaX = (float)(delta*Math.cos(roll*ang_rad));
		float deltaY = (float)(delta*Math.sin(roll*ang_rad));
		x1 = (float)(centerX+deltaX+leng*Math.cos(roll*ang_rad));
		x2 = (float)(centerX+deltaX-leng*Math.cos(roll*ang_rad));
		y1 = (float)(yPos+deltaY-leng*Math.sin(roll*ang_rad));
		y2 = (float)(yPos+deltaY+leng*Math.sin(roll*ang_rad));
		canvas.drawLine(x1, y1, x2, y2, ahrsLine);
		
	}
	

	public AHRSView(Context context) {
		super(context);
	}

	public AHRSView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAHRSView();
	}

	public AHRSView(Context context, AttributeSet attrs, int defaultStyle) {
		super(context, attrs, defaultStyle);
		initAHRSView();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// setting the measured values to resize the view to a certain width and
		// height

		setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));

		// before measure, get the center of view
		centerX = (int) getWidth() / 2;
		centerY = (int) getWidth() / 2;
	}

	private int measure(int measureSpec) {
		int result = 0;

		// Decode the measurement specifications.
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.UNSPECIFIED) {
			// Return a default size of 200 if no bounds are specified.
			result = 200;
		} else {
			// As you want to fill the available space
			// always return the full available bounds.
			result = specSize;
		}
		return result;
	}

	protected void initAHRSView() {
		horizontalLine = new Paint(Paint.ANTI_ALIAS_FLAG);
		horizontalLine.setStrokeWidth(3);
		horizontalLine.setColor(Color.YELLOW);
		
		ahrsLine = new Paint(Paint.ANTI_ALIAS_FLAG);
		ahrsLine.setStrokeWidth(2);
		ahrsLine.setColor(Color.GREEN);
		
		upPoly = new Paint(Paint.ANTI_ALIAS_FLAG);
		upPoly.setColor(Color.LTGRAY);
		upPoly.setStyle(Style.FILL);
		
		downPoly = new Paint(Paint.ANTI_ALIAS_FLAG);
		downPoly.setColor(Color.GRAY);
		downPoly.setStyle(Style.FILL);
		
		path = new Path();
	}
	
	public static void setRoll(float _roll){roll=_roll;}
	public static void setPitch(float _pitch){pitch=_pitch;}
	public static void setYaw(float _yaw){roll=_yaw;}

}
