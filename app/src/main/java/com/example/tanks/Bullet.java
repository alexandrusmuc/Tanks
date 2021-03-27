package com.example.tanks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;

public class Bullet {
    private final int screenX;
    private final int screenY;
    private boolean isActive;
    private float x;
    private float y;
    private RectF rect;
    private Bitmap bitmap;

    public final int LEFT = 1;
    public final int RIGHT = 2;
    public final int UP = 3;
    public final int DOWN = 4;
    private int heading;
    private int height;
    private int length;
    private int bulletSpeed;

    public Bullet(Context context, int screenX, int screenY) {
        this.isActive = false;
        this.screenX = screenX;
        this.screenY = screenY;
        this.rect  = new RectF();
        this.bulletSpeed = 1000;







    }
    public boolean shoot(Tank tank){
        if(!isActive){
            if(tank.getDirection() == tank.UP  ){
                x = (tank.getX() + tank.getLength()/2);
                y = (tank.getY());
            }else if(tank.getDirection() == tank.DOWN ){
                x = (tank.getX() + tank.getLength()/2);
                y = (tank.getY() + tank.getHeight());
            }else if(tank.getDirection() == tank.LEFT){
                x = tank.getX();
                y = (tank.getY() + tank.getHeight()/2);

            }else if(tank.getDirection() == tank.RIGHT){
                x = tank.getX() + tank.getLength();
                y = tank.getY() + tank.getHeight()/2;

            }



            this.heading = tank.getDirection();
            isActive = true;
            this.height = screenY/80;
            this.length = screenY/80;
            return true;
        }
        return false;

    }
    public void update(long fps){
        if(heading == UP){
            y = y- bulletSpeed/fps;
        }
        if(heading == DOWN){
            y = y+ bulletSpeed/fps;
        }
        if(heading == LEFT){
            x = x- bulletSpeed/fps;
        }
        if(heading == RIGHT){
            x = x + bulletSpeed/fps;
        }

        rect.left = x;
        rect.right = x + length;
        rect.top = y;
        rect.bottom = y+height;


    }

    public RectF getRect(){
        return rect;
    }

    public boolean isActive(){
        return isActive;
    }

    public void setInactive(){
        this.isActive = false;
    }

    public float getX() {
        return this.x;
    }
    public float getY(){
        return this.y;
    }

    public int getLength() {
        return this.length;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }



}
