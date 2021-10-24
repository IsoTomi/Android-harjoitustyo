package com.example.harjoitustyo;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

public class Paddle {
    PointF centerPoint;
    PointF size;
    RectF hitBox;
    float velocity;
    int color;

    /**
     * Paddle-class constructor.
     *
     * @param givenPosition Position of the center point of the paddle on screen.
     * @param givenSize Size of the paddle.
     * @param givenVelocity Velocity of the paddle.
     * @param givenColor Paddle's color.
     */
    public Paddle( PointF givenPosition, PointF givenSize, float givenVelocity, int givenColor ) {
        centerPoint = givenPosition;
        size = givenSize;
        velocity = givenVelocity;
        color = givenColor;

        size.x /= 2;
        size.y /= 2;

        hitBox = new RectF(centerPoint.x - size.x,
                centerPoint.y - size.y,
                centerPoint.x + size.x,
                centerPoint.y + size.y);
    }

    /**
     * Getters and setters.
     */
    public PointF getPosition() { return centerPoint; }
    public PointF getSize() { return size; }
    public RectF getHitBox() { return hitBox; }
    public float getVelocity() { return velocity; }
    public int getColor() { return color; }

    public void setPosition( PointF newPosition) { centerPoint = newPosition; }
    public void setSize( PointF newSize) { size = newSize; size.x /= 2; size.y /= 2; }
    public void setHitBox( RectF newHitBox ) { hitBox = newHitBox; }
    public void setVelocity( float newVelocity ) { velocity = newVelocity; }
    public void setColor( int newColor ) { color = newColor; }

    /**
     * Moves the paddle on the screen.
     */
    public void move() {
        hitBox.offset(velocity, 0.0f);
        centerPoint.offset(velocity, 0.0f);
    }

    /**
     * Draws the paddle.
     *
     * @param canvas Canvas-object to draw into.
     */
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);

        canvas.drawRoundRect(centerPoint.x - size.x,
                centerPoint.y + size.y,
                centerPoint.x + size.x,
                centerPoint.y - size.y,
                20, 20, paint);
    }
}
