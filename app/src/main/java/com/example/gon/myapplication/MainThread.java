package com.example.gon.myapplication;

import android.graphics.Canvas;
import android.provider.Settings;
import android.view.SurfaceHolder;

/**
 * Created by Gon on 2016-12-29.
 */

public class MainThread extends Thread {
    public static final int MAX_FPS = 30;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private Field field;
    private boolean running;
    public static Canvas canvas;

    public void setRunning(boolean running){
        this.running = running;
    }

    public MainThread(SurfaceHolder surfaceHolder, Field field){
        super();
        this.surfaceHolder = surfaceHolder;
        this.field = field;
    }

    @Override
    public void run(){
        long startTime;
        long timeMillis = 1000/MAX_FPS;
        long waitTime;
        int frameCount =0;
        long totalTime = 0;
        long targetTime = 1000/MAX_FPS;
        while(running){
            startTime = System.nanoTime();
            canvas = null;

            try{
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    this.field.play();
                    this.field.update();
                    this.field.draw(canvas);


                }
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e){e.printStackTrace();}
                }
            }
            timeMillis = (System.nanoTime() - startTime)/1000000;
            waitTime = targetTime - timeMillis;
            try{
                if(waitTime>0){
                    this.sleep(waitTime);
                }
            } catch (Exception e){e.printStackTrace();}

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount ==  MAX_FPS){
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println(averageFPS);
            }
        }
    }



}
