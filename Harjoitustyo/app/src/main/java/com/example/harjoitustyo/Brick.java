package com.example.harjoitustyo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

public class Brick {
    PointF centerPoint;
    PointF size;
    RectF hitBox;
    int color;
    int row;
    boolean destroyed;

    /**
     * Brick-class constructor.
     *
     * @param givenPosition Position of the center point of the brick on screen.
     * @param givenSize Size of the brick.
     * @param givenColor Brick's color.
     */
    public Brick( PointF givenPosition, PointF givenSize, int givenColor ) {
        centerPoint = givenPosition;
        size = givenSize;
        color = givenColor;

        size.x /= 2;
        size.y /= 2;

        hitBox = new RectF(centerPoint.x - size.x,
                centerPoint.y - size.y,
                centerPoint.x + size.x,
                centerPoint.y + size.y);

        destroyed = false;
    }

    /**
     * Getters and setters.
     */
    public PointF getPosition() { return centerPoint; }
    public PointF getSize() { return size; }
    public RectF getHitBox() { return hitBox; }
    public int getColor() { return color; }
    public boolean getDestroyed() { return destroyed; }
    public int getRow() { return row; }

    public void setPosition( PointF newPosition ) { centerPoint = newPosition; }
    public void setSize( PointF newSize ) { size = newSize; size.x /= 2; size.y /= 2; }
    public void setHitBox( RectF newHitBox ) { hitBox = newHitBox; }
    public void setColor( int newColor ) { color = newColor; }
    public void setDestroyed( boolean isDestroyed ) { destroyed = isDestroyed; }
    public void setRow( int newRow ) { row = newRow; }

    /**
     * Draws a brick.
     *
     * @param canvas Canvas-object to draw into.
     */
    public void draw(Canvas canvas) {

        if (destroyed == false) {

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(color);

            canvas.drawRect(centerPoint.x - size.x,
                    centerPoint.y - size.y,
                    centerPoint.x + size.x,
                    centerPoint.y + size.y,
                    paint);
        }
    }
}
