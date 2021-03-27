package com.example.tanks;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;

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
    private Tank enemy;
    private Joystick joystick;
    private Button fireButton;
    private Bullet playerBullet;
    private int joystickPointerID = 0;


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


        joystick = new Joystick(screenX/8, screenY -screenX/8,screenX/10,screenX/20);

        playerBullet =  new Bullet(context,screenX,screenY);





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
        if (playerBullet.isActive()){
            playerBullet.update(fps);
        }
        checkColisions();
        joystick.update();


    }
    private void checkColisions(){

        //tank off screen
        if(tank.getX() > screenX - tank.getLength()) tank.setX(screenX-tank.getLength());
        if(tank.getX()<0 ) tank.setX(0);
        if(tank.getY()> screenY-tank.getLength())
            tank.setY(screenY-tank.getLength());
        if(tank.getY()< 0 ) tank.setY(0);

        //bullet off screen
        if(playerBullet.getX() > screenX - playerBullet.getLength() ||
        (playerBullet.getX()<0 ) ||
        (playerBullet.getY()> screenY-playerBullet.getLength())||

        (playerBullet.getY()< 0 )) playerBullet.setInactive();

    }





    private void draw(){

        if (ourHolder.getSurface().isValid()) {

            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.argb(255, 26, 128, 182));
            paint.setColor(Color.argb(255,  255, 255, 255));
            paint.setColor(Color.argb(255,  249, 129, 0));
            paint.setTextSize(40);
            //paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("Score: " + score + "   Lives: " + lives, 10,50, paint);
            canvas.drawBitmap(tank.getBitmap(),tank.getX(),tank.getY(),paint);

            joystick.draw(canvas);
            if(playerBullet.isActive()){
                //canvas.drawRect(playerBullet.getRect(),paint);
                canvas.drawCircle(playerBullet.getX(),playerBullet.getY(),playerBullet.getLength()/2,paint);
                //canvas.drawBitmap(playerBullet.getBitmap(),playerBullet.getX(),playerBullet.getY(),paint);
            }

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


//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction() & event.getActionMasked()) {
//            case MotionEvent.ACTION_DOWN:
//                paused = false;
//                if (event.getY() > screenY - screenY / 4) {
//                    if (event.getX() > screenX - 3 * screenX / 4 && event.getX() < screenX - screenX / 4)
//                        tank.setMovementState(tank.DOWN);
//
//                }
//                if (event.getY() < screenY - 3 * screenY / 4) {
//                    if (event.getX() > screenX - 3 * screenX / 4 && event.getX() < screenX - screenX / 4)
//                        tank.setMovementState(tank.UP);
//                }
//
//
//                if (event.getX() > screenX - screenX / 4) {
//                    if (event.getY() > screenX - 3 * screenY / 4 && event.getX() < screenY - screenY / 4)
//                        tank.setMovementState(tank.RIGHT);
//
//                }
//                if (event.getX() < screenX - 3 * screenX / 4) {
//                    if (event.getY() > screenY - 3 * screenY / 4 && event.getX() < screenY - screenY / 4)
//                        tank.setMovementState(tank.LEFT);
//                }
//
//
//                break;
//            case MotionEvent.ACTION_UP:
//                //paused = true;
//                tank.setMovementState(tank.STOPPED);
//                break;
//        }
//        return true;
//
//
//
//}
    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if(joystick.isPressed()){
                    //joystick was already pressed
                    playerBullet.shoot(tank);
                }else if(joystick.isPressed((double)event.getX(), (double)event.getY())){
                    paused = false;
                    joystickPointerID = event.getPointerId(event.getActionIndex());
                    joystick.setIsPressed(true);
                }else{
                    playerBullet.shoot(tank);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if(joystick.isPressed()){
                    joystick.setActuator((double)event.getX(), (double)event.getY());
                    if(joystick.getActuatorY()< 0.0){
                        if((Math.abs(joystick.getActuatorY())> (Math.abs(joystick.getActuatorX())))){
                            tank.setMovementState(tank.UP);
                            //playerBullet.shoot(tank);

                        }else{
                            if(joystick.getActuatorX()>0.0){
                                tank.setMovementState(tank.RIGHT);
                            }else {
                                tank.setMovementState(tank.LEFT);
                            }

                    }

                }else{
                        if((Math.abs(joystick.getActuatorY())> (Math.abs(joystick.getActuatorX())))){
                            tank.setMovementState(tank.DOWN);

                        }else{
                            if(joystick.getActuatorX()>0.0){
                                tank.setMovementState(tank.RIGHT);
                            }else {
                                tank.setMovementState(tank.LEFT);
                            }

                        }
                    }

                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if(joystickPointerID == event.getPointerId(event.getActionIndex())){
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                    tank.setMovementState(tank.STOPPED);
                }


                return true;

//            case MotionEvent.ACTION_POINTER_DOWN:
//                System.out.println("FIRE!!!");
//                return  true;

        }
        return super.onTouchEvent(event);
    }







} // end class
