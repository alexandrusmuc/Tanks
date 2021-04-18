package com.example.tanks;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Date;
import java.util.Random;

public class TankWarView extends SurfaceView implements Runnable {

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
    private int playerScore = 0;
    private int enemyScore = 0;
    private Tank tank;
    private Enemy enemy;
    private Joystick joystick;

    private Bullet playerBullet;
    private Bullet enemyBullet;
    private Date date = new Date();

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


    private void prepareLevel() {
        tank = new Tank(context, screenX, screenY);
        enemy = new Enemy(context, screenX, screenY);


        joystick = new Joystick(screenX / 6, screenY - screenX / 6, screenX / 8, screenX / 16);

        playerBullet = new Bullet(context, screenX, screenY);

        enemyBullet = new Bullet(context, screenX, screenY);
        enemyBullet.setBulletSpeed(600);

    }


    @Override
    public void run() {
        while (playing) {
            long startFrameTime = System.currentTimeMillis();
            if (!paused) {
                update();
                }

            Date date = new Date();

            if (date.getTime()%10 == 0 && enemy.getTankMoving() != Tank.STOPPED){
                enemyBullet.shoot(enemy);
            }

            draw();

            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }


    private void update() {
        tank.update(fps);
        enemy.update(fps);
        if (playerBullet.isActive()) {
            playerBullet.update(fps);
        }
        if(enemyBullet.isActive()){
            enemyBullet.update(fps);
        }
        checkCollisions();
        joystick.update();
    }

    private void checkCollisions() {

        //tank off screen
        if (tank.getX() > screenX - tank.getLength()) tank.setX(screenX - tank.getLength());
        if (tank.getX() < 0) tank.setX(0);
        if (tank.getY() > screenY - tank.getLength())
            tank.setY(screenY - tank.getLength());
        if (tank.getY() < 0) tank.setY(0);


        //enemy off screen



        date= new Date();
        if(date.getTime()%20 == 0 && enemy.getTankMoving()!= Tank.STOPPED){
            System.out.println(date.getTime());
            Random random = new Random();
            int randomDirection = random.nextInt(5)+1;
            enemy.setMovementState(randomDirection);
        }
        if(enemy.getX()>screenX - tank.getLength()){
            enemy.setMovementState(Tank.LEFT);
        }
        if(enemy.getY()>screenY - enemy.getHeight()){
            enemy.setMovementState(Tank.UP);
        }
        if(enemy.getX()<0 ){
            enemy.setMovementState(Tank.RIGHT);
        }

        if (enemy.getY()<0){
            enemy.setMovementState(Tank.DOWN);
        }
        //enemy bullet offscreen
        if (enemyBullet.getX() > screenX - enemyBullet.getLength() ||
                (enemyBullet.getX() < 0) ||
                (enemyBullet.getY() > screenY - enemyBullet.getLength()) ||

                (enemyBullet.getY() < 0)) enemyBullet.setInactive();

        //bullet off screen
        if (playerBullet.getX() > screenX - playerBullet.getLength() ||
                (playerBullet.getX() < 0) ||
                (playerBullet.getY() > screenY - playerBullet.getLength()) ||

                (playerBullet.getY() < 0)) playerBullet.setInactive();

        //bullet intersects enemy
        if (playerBullet.getRect().intersect(enemy.getRect()) && playerBullet.isActive()){
            playerScore++;
            playerBullet.setInactive();
            enemy = new Enemy(context, screenX, screenY);
        }

        // enemyBullet intersects player
        if (enemyBullet.getRect().intersect(tank.getRect()) && enemyBullet.isActive()){
            enemyScore++;
            enemyBullet.setInactive();
            tank = new Tank(context, screenX, screenY);
        }



        //player collide with enemy
        if(tank.getTankMoving() == Tank.DOWN){
            if(tank.getY() + tank.height > enemy.getY() &&
            tank.getY()+ tank.height < enemy.getY() + enemy.height &&
            tank.getX() + tank.length > enemy.getX() && tank.getX() < enemy.getX() + enemy.length){
                tank.setY(enemy.getY() - tank.height);
            }
        }


        if(tank.getTankMoving() == Tank.UP){
            if(tank.getY()  < enemy.getY()+ enemy.height &&
                    tank.getY() > enemy.getY()  &&
                    tank.getX() + tank.length > enemy.getX() && tank.getX() < enemy.getX() + enemy.length){
                tank.setY(enemy.getY() + tank.height);
            }
        }


        if(tank.getTankMoving() == Tank.LEFT){
            if(tank.getX()  < enemy.getX()+ enemy.length &&
                    tank.getX() > enemy.getX()  &&
                    tank.getY() + tank.height > enemy.getY() && tank.getY() < enemy.getY() + enemy.height){
                tank.setX(enemy.getX() + tank.height);
            }
        }

        if(tank.getTankMoving() == Tank.RIGHT){
            if(tank.getX() + tank.length > enemy.getX() &&
                    tank.getX()+ tank.length < enemy.getX() + enemy.length &&
                    tank.getY() + tank.height > enemy.getY() && tank.getY() < enemy.getY() + enemy.height){
                tank.setX(enemy.getX() - tank.length);
            }
        }


        //enemy collide with player
        if(enemy.getTankMoving() == Tank.DOWN){
            if(enemy.getY() + enemy.height > tank.getY() &&
                    enemy.getY()+ enemy.height < tank.getY() + tank.height &&
                    enemy.getX() + enemy.length > tank.getX() && enemy.getX() < tank.getX() + tank.length){
                enemy.setY(tank.getY() - tank.height);
            }
        }


        if(enemy.getTankMoving() == Tank.UP){
            if(enemy.getY()  < tank.getY()+ tank.height &&
                    enemy.getY() > tank.getY()  &&
                    enemy.getX() + enemy.length > tank.getX() && enemy.getX() < tank.getX() + tank.length){
                enemy.setY(tank.getY() + enemy.height);
            }
        }


        if(enemy.getTankMoving() == Tank.LEFT){
            if(enemy.getX()  < tank.getX()+ tank.length &&
                    enemy.getX() > tank.getX()  &&
                    enemy.getY() + enemy.height > tank.getY() && enemy.getY() < tank.getY() + tank.height){
                enemy.setX(tank.getX() + enemy.height);
            }
        }

        if(enemy.getTankMoving() == Tank.RIGHT){
            if(enemy.getX() + enemy.length > tank.getX() &&
                    enemy.getX()+ enemy.length < tank.getX() + tank.length &&
                    enemy.getY() + enemy.height > tank.getY() && enemy.getY() < tank.getY() + tank.height){
                enemy.setX(tank.getX() - enemy.length);
            }
        }








    }


    private void draw() {

        if (ourHolder.getSurface().isValid()) {

            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.argb(255, 26, 128, 182));
            paint.setColor(Color.argb(255, 255, 255, 255));
            paint.setColor(Color.argb(255, 249, 129, 0));
            paint.setTextSize(100);
            //paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText( "" + enemyScore ,screenX/2, screenY/4, paint);
            canvas.drawText("" + playerScore, screenX/2 , (screenY/4)*3, paint);
            canvas.drawBitmap(tank.getBitmap(), tank.getX(), tank.getY(), paint);
            canvas.drawBitmap(enemy.getBitmap(), enemy.getX(), enemy.getY(), paint);
            joystick.draw(canvas);
            if (playerBullet.isActive()) {
                //canvas.drawRect(playerBullet.getRect(),paint);
                canvas.drawCircle(playerBullet.getX(), playerBullet.getY(), playerBullet.getLength() / 2, paint);
                //canvas.drawBitmap(playerBullet.getBitmap(),playerBullet.getX(),playerBullet.getY(),paint);
            }

            if(enemyBullet.isActive()){
                canvas.drawCircle(enemyBullet.getX(), enemyBullet.getY(), enemyBullet.getLength() / 2, paint);
            }
            if (enemyScore == 5){
                canvas.drawText("Game over",40, screenY/2-20 , paint);
                enemy.setMovementState(Tank.STOPPED);
                playing = false;

            }
            if(playerScore == 5){
                    canvas.drawText("You won!!!",40, screenY/2-20 , paint);
                enemy.setMovementState(Tank.STOPPED);
                playing = false;
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



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (joystick.isPressed()) {
                    //joystick was already pressed
                    playerBullet.shoot(tank);
                } else if (joystick.isPressed((double) event.getX(), (double) event.getY())) {
                    paused = false;
                    joystickPointerID = event.getPointerId(event.getActionIndex());
                    joystick.setIsPressed(true);
                } else {
                    playerBullet.shoot(tank);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (joystick.isPressed()) {
                    joystick.setActuator((double) event.getX(), (double) event.getY());
                    if (joystick.getActuatorY() < 0.0) {
                        if ((Math.abs(joystick.getActuatorY()) > (Math.abs(joystick.getActuatorX())))) {
                            tank.setMovementState(tank.UP);
                            //playerBullet.shoot(tank);

                        } else {
                            if (joystick.getActuatorX() > 0.0) {
                                tank.setMovementState(tank.RIGHT);
                            } else {
                                tank.setMovementState(tank.LEFT);
                            }

                        }

                    } else {
                        if ((Math.abs(joystick.getActuatorY()) > (Math.abs(joystick.getActuatorX())))) {
                            tank.setMovementState(tank.DOWN);

                        } else {
                            if (joystick.getActuatorX() > 0.0) {
                                tank.setMovementState(tank.RIGHT);
                            } else {
                                tank.setMovementState(tank.LEFT);
                            }

                        }
                    }

                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (joystickPointerID == event.getPointerId(event.getActionIndex())) {
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                    tank.setMovementState(tank.STOPPED);
                }

                return true;


        }
        return super.onTouchEvent(event);
    }


} // end class
