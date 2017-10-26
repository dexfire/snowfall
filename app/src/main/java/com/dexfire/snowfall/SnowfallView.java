package com.dexfire.snowfall;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Dexfire on 2017/10/26 0026.
 *
 */
public class SnowfallView extends SurfaceView implements SurfaceHolder.Callback {

    DrawingThread dt;
    Context context;
    boolean isDrawable = false;

    public SnowfallView (Context context){
        super(context);
        this.context=context;
        getHolder().addCallback(this);
        dt=new DrawingThread(getHolder(),context);
    }

    public SnowfallView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        this.context=context;
        getHolder().addCallback(this);
        dt=new DrawingThread(getHolder(),context);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isDrawable= true;
        dt.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDrawable=true;
        try {
            dt.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class DrawingThread extends Thread{

        SurfaceHolder holder;
        Context context;

        private DrawingThread(SurfaceHolder holder,Context context){
            this.context = context;
            this.holder=holder;
        }

        @Override
        public void run() {
            float radius = 10f;
            while(true){
                if(isDrawable){
                    synchronized(holder){
                        Canvas canvas = holder.lockCanvas();
                        if(canvas==null)continue;
                        canvas.drawColor(Color.CYAN);
                        Paint paint = new Paint();
                        paint.setColor(Color.RED);
                        paint.setStyle(Paint.Style.FILL_AND_STROKE);
                        canvas.drawCircle(50,50,radius++,paint);
                        paint.setARGB(0xff,genRandomInteger(0,0xff),genRandomInteger(0,0xff),genRandomInteger(0,0xff));
                        canvas.drawCircle(genRandomInteger(0,500),genRandomInteger(0,500),genRandomInteger(20,100),paint);
                        if(radius>100){
                            radius=10f;
                        }
                        holder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }

        private  int genRandomInteger(int min,int max){
            if(min > max){
                throw new RuntimeException("error in genRandomInteger(II):min > max");
            }
            return (int) (Math.random()*(max-min))+min;
        }
    }
}
