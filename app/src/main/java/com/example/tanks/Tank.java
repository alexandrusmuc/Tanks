package com.example.tanks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

public class Tank {

    RectF rect;
    protected Bitmap bitmap;
    protected Bitmap bitmapup;
    protected Bitmap bitmapleft;
    protected Bitmap bitmapright;
    protected Bitmap bitmapdown;
    public Bitmap currentBitmap;
    protected float height;
    protected float length;
    protected float x;
    protected float y;

    private float tankSpeed;
    public final static int STOPPED = 0;
    public final static int LEFT = 1;
    public final static int RIGHT = 2;
    public final static int UP = 3;
    public final static int DOWN = 4;

    ///maybe more movement than this
    protected int tankMoving = STOPPED;
    protected int heading = UP;
    //private int tankSpeed;


    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Tank(Context context, int screenX, int screenY){

        rect = new RectF();

        length = screenY/10;
        height = screenY/8;

        x = screenX / 2;
        y = screenY -height;

        tankSpeed = 350;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.tankup);

        // stretch the bitmap to a size appropriate for the screen resolution
        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);

          bitmapup = BitmapFactory.decodeResource(context.getResources(), R.drawable.tankup);
          bitmapup = Bitmap.createScaledBitmap(bitmapup, (int) (length), (int) (height),false);

        bitmapright = BitmapFactory.decodeResource(context.getResources(), R.drawable.tankright);
        bitmapright = Bitmap.createScaledBitmap(bitmapright, (int) (length), (int) (height),false);

        bitmapleft = BitmapFactory.decodeResource(context.getResources(), R.drawable.tankleft);
        bitmapleft = Bitmap.createScaledBitmap(bitmapleft, (int) (length), (int) (height),false);

        bitmapdown = BitmapFactory.decodeResource(context.getResources(), R.drawable.tankdown);
        bitmapdown = Bitmap.createScaledBitmap(bitmapdown, (int) (length), (int) (height),false);

         currentBitmap = bitmap;
    }

    public void setMovementState(int state){
        tankMoving = state;
    }


    public void update(long fps){
          if(tankMoving == LEFT){
              x = x - tankSpeed / fps;
               bitmap = bitmapleft;
               heading = LEFT;
           }
            if(tankMoving == RIGHT){
                x = x + tankSpeed / fps;
                bitmap = bitmapright;
                heading = RIGHT;
            }
            if(tankMoving == UP){
                y = y - tankSpeed / fps;
                bitmap = bitmapup;
                heading = UP;

            }

            if(tankMoving == DOWN){
                y=y + tankSpeed / fps;
                bitmap = bitmapdown;
                heading = DOWN;

           }

        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;

    }


    public RectF getRect(){
        return rect;
    }

    public Bitmap getBitmap(){

        return bitmap;
    }

    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }


    public float getLength(){
        return length;
    }

    public float getTankSpeed() {
        return tankSpeed;
    }

    public void setTankSpeed(float tankSpeed) {
        this.tankSpeed = tankSpeed;
    }

    public float getHeight() {
    return this.height;
    }

    public int getDirection() {
        return this.heading;
    }

    public int getTankMoving() {
        return tankMoving;
    }
}

