package com.example.tanks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

public class Tank {

    RectF rect;
    private Bitmap bitmap;
    private Bitmap bitmapup;
    private Bitmap bitmapleft;
    private Bitmap bitmapright;
    private Bitmap bitmapdown;
    public Bitmap currentBitmap;
    private float height;
    private float length;
    private float x;
    private float y;

    private float tankSpeed;
    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;
    public final int UP = 3;
    public final int DOWN = 4;

    ///maybe more movement than this
    private int tankMoving = STOPPED;
    //private int tankSpeed;


    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Tank(Context context, int screenX, int screenY){

        rect = new RectF();

        length = screenX/10;
        height = screenX/10;

        x = screenX / 2;
        y = screenY / 2;

        tankSpeed = 350;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.tankright);

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
           }
            if(tankMoving == RIGHT){
                x = x + tankSpeed / fps;
                bitmap = bitmapright;
            }
            if(tankMoving == UP){
                y = y - tankSpeed / fps;
                bitmap = bitmapup;

            }

            if(tankMoving == DOWN){
                y=y + tankSpeed / fps;
                bitmap = bitmapdown;

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




}

