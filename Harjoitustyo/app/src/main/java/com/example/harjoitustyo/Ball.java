package com.example.harjoitustyo;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

public class Ball {
    PointF centerPoint;
    PointF direction;
    RectF hitBox;
    float radius;
    float velocity;
    float currentRotation;
    float angularVelocity;
    int primaryColor;
    int secondaryColor;

    /**
     * Ball-class constructor.
     *
     * @param givenPosition Position of the center point of the ball on screen.
     * @param givenDirection Ball's direction unit vector.
     * @param givenRadius Ball's radius.
     * @param givenVelocity Velocity of the ball.
     * @param givenPriColor Ball's primary color.
     * @param givenSecColor Ball's secondary color.
     */
    public Ball( PointF givenPosition,
                 PointF givenDirection,
                 float givenRadius,
                 float givenVelocity,
                 float givenAngVelocity,
                 int givenPriColor,
                 int givenSecColor ) {
        centerPoint = givenPosition;
        direction = givenDirection;
        radius = givenRadius;
        velocity = givenVelocity;
        angularVelocity = givenAngVelocity;
        primaryColor = givenPriColor;
        secondaryColor = givenSecColor;
        currentRotation = 0;
        hitBox = new RectF(centerPoint.x - radius,
                centerPoint.y - radius,
                centerPoint.x + radius,
                centerPoint.y + radius);
    }

    /**
     * Getters and setters.
     */
    public PointF getPosition() { return centerPoint; };
    public PointF getDirection() { return direction; }
    public RectF getHitBox() { return hitBox; }
    public float getRadius() { return radius; }
    public float getVelocity() { return velocity; }
    public float getAngularVelocity() { return angularVelocity; }
    public int getPrimaryColor() { return primaryColor; }
    public int getSecondaryColor() { return secondaryColor; }

    public void setPosition( PointF newPosition ) { centerPoint = newPosition; }
    public void setDirection( PointF newDirection ) { direction = newDirection; }
    public void setHitBox( RectF newHitBox ) { hitBox = newHitBox; }
    public void setRadius( float newRadius ) { radius = newRadius; }
    public void setVelocity( float newVelocity ) { velocity = newVelocity; }
    public void setAngularVelocity( float newAngVelocity ) { angularVelocity = newAngVelocity; }
    public void setPrimaryColor( int newColor ) { primaryColor = newColor; }
    public void setSecondaryColor( int newColor ) { secondaryColor = newColor; }

    /**
     * Animates the ball's rotation.
     */
    public void animate() {
        currentRotation += angularVelocity;

        if (currentRotation >= 360.0F)
            currentRotation = 0.0F;
    }

    /**
     * Moves the ball on the screen.
     */
    public void move() {
        hitBox.offset(direction.x * velocity, direction.y * velocity);
        centerPoint.offset(direction.x * velocity, direction.y * velocity);
        angularVelocity = direction.x * velocity;
    }

    /**
     * Draws the ball.
     *
     * @param canvas Canvas-object to draw into.
     */
    public void draw( Canvas canvas ) {
        // Draw the background circle first with primary color.
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(primaryColor);
        canvas.drawCircle( centerPoint.x, centerPoint.y, radius, paint );

        // Draw the arcs with secondary color.
        canvas.save();
        canvas.translate(centerPoint.x, centerPoint.y);
        canvas.rotate(currentRotation);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(secondaryColor);
        canvas.drawArc( new RectF( -radius, -radius,
                        radius,  radius ),
                0, 90, true, paint );

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(secondaryColor);
        canvas.drawArc( new RectF( -radius, -radius,
                        radius,  radius ),
                180, 90, true, paint );

        canvas.restore();
    }
}
