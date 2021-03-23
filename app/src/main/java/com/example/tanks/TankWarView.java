package com.example.tanks;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class TankWarView extends SurfaceView implements Runnable{

    private Context context;
    private Thread gameThread = null;
    private SurfaceHolder ourHolder;
    private volatile boolean playing;
    private boolean paused = true;
    private Canvas canvas;
    private Paint paint;
    private long fps;
    private long timeThisFrame;
    private int screenX;
    private int screenY;
    private int score = 0;
    private int lives = 5;
    private Tank tank;


    public TankWarView(Context context, int x, int y) {

        super(context);
        this.context = context;

        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        prepareLevel();
    }



    private void prepareLevel(){
        tank = new Tank(context,screenX,screenY);

    }


    @Override
    public void run() {
        while (playing) {

            long startFrameTime = System.currentTimeMillis();

            if(!paused){
                update();
            }


            draw();

            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }

        }

    }



    private void update(){
        tank.update(fps);
        checkColisions();


    }
    private void checkColisions(){
        if(tank.getX() > screenX - tank.getLength()) tank.setX(screenX-tank.getLength());
        if(tank.getX()<0 ) tank.setX(0);
        if(tank.getY()> screenY-tank.getLength())
            tank.setY(screenY-tank.getLength());
        if(tank.getY()< 0 ) tank.setY(0);
    }





    private void draw(){

        if (ourHolder.getSurface().isValid()) {

            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.argb(255, 26, 128, 182));
            paint.setColor(Color.argb(255,  255, 255, 255));
            paint.setColor(Color.argb(255,  249, 129, 0));
            paint.setTextSize(40);
            canvas.drawText("Score: " + score + "   Lives: " + lives, 10,50, paint);
            canvas.drawBitmap(tank.getBitmap(),tank.getX(),tank.getY(),paint);
            ourHolder.unlockCanvasAndPost(canvas);
        }

    }




    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }



    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction() & event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                paused = false;
                if(event.getY()>screenY-screenY/4){
                    if(event.getX()>screenX-3*screenX/4 && event.getX()<screenX - screenX/4)
                        tank.setMovementState(tank.DOWN);

                }
                if(event.getY()<screenY-3*screenY/4){
                    if(event.getX()>screenX-3*screenX/4 && event.getX()<screenX - screenX/4)
                    tank.setMovementState(tank.UP);
                }



                if(event.getX()>screenX-screenX/4){
                    if(event.getY()>screenX-3*screenY/4 && event.getX()<screenY - screenY/4)
                        tank.setMovementState(tank.RIGHT);

                }
                if(event.getX()<screenX-3*screenX/4){
                    if(event.getY()>screenY-3*screenY/4 && event.getX()<screenY - screenY/4)
                        tank.setMovementState(tank.LEFT);
                }


                break;
            case MotionEvent.ACTION_UP:
                //paused = true;
                tank.setMovementState(tank.STOPPED);
                break;
        }



        return true;
    }
}  // end class
