package com.example.tanks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Enemy extends Tank {

    public Enemy(Context context, int screenX, int screenY) {
        super(context, screenX, screenY);
        this.setX(0);
        this.setY(0);
        this.setMovementState(Tank.RIGHT);
        this.setTankSpeed(150);
        this.heading = Tank.RIGHT;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemyright);

        // stretch the bitmap to a size appropriate for the screen resolution
        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);

        bitmapup = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemyup);
        bitmapup = Bitmap.createScaledBitmap(bitmapup, (int) (length), (int) (height),false);

        bitmapright = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemyright);
        bitmapright = Bitmap.createScaledBitmap(bitmapright, (int) (length), (int) (height),false);

        bitmapleft = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemyleft);
        bitmapleft = Bitmap.createScaledBitmap(bitmapleft, (int) (length), (int) (height),false);

        bitmapdown = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemydown);
        bitmapdown = Bitmap.createScaledBitmap(bitmapdown, (int) (length), (int) (height),false);

        currentBitmap = bitmap;




    }

}
