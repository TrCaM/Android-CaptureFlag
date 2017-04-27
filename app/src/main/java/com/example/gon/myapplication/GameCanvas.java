package com.example.gon.myapplication;

/**
 * Created by Gon on 2016-12-28.
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;


public class GameCanvas extends Canvas {

    //the bitmap that this view is in
    Bitmap blankBitmap;

    //the field that the game is playing on
    Field field;
    Paint paint;
    /** extremal coordinates of the field itself */
    protected int minX, minY, maxX, maxY;

    /**
     * Construct our game and set it running.
     */
    public GameCanvas(Field field, Bitmap blankBitmap, int minX, int minY, int maxX, int maxY) {
        super(blankBitmap);
        this.field = field;
        // set the field corner points
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;

        this.paint = new Paint();








    }

    public void update(){
        this.drawColor(Color.GREEN);
        paint.setColor(Color.BLACK);

        // Draw a line
        this.drawLine((maxX-minX)/2,minY,(maxX-minX)/2,maxY,paint);
    }

    public void draw(Activity activity,ArrayList<Entity> team1, ArrayList<Entity> team2, ArrayList<Entity> things) {
        for (Entity player : team1) {
            Bitmap image = BitmapFactory.decodeResource(activity.getResources(), player.getSprite());
            Bitmap scale = Bitmap.createScaledBitmap(image, image.getWidth()*2/3, image.getHeight()*2/3, true);
            this.drawBitmap(scale, (int) player.getX(), (int) player.getY(), paint);
        }

        for (Entity player : team2) {
            Bitmap image = BitmapFactory.decodeResource(activity.getResources(), player.getSprite());
            Bitmap scale = Bitmap.createScaledBitmap(image, image.getWidth()*2/3, image.getHeight()*2/3, true);
            this.drawBitmap(scale, (int) player.getX(), (int) player.getY(), paint);
        }

        for (Entity thing : things) {
            Bitmap image = BitmapFactory.decodeResource(activity.getResources(), thing.getSprite());
            Bitmap scale = Bitmap.createScaledBitmap(image, image.getWidth()*2/3, image.getHeight()*2/3, true);
            this.drawBitmap(scale, (int) thing.getX(), (int) thing.getY(), paint);
        }
    }
}
