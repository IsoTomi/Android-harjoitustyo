package com.example.harjoitustyo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class GameView extends View implements Runnable, GestureDetector.OnGestureListener {
    boolean executeThread;
    Thread animationThread;
    GestureDetector gestureDetector;
    Context appContext;

    // Game related
    int score;
    int lives;
    int bricksLeft;
    float startVelocity;

    // Objects on screen
    RectF bouncingArea;
    Ball ball;
    Paddle paddle;
    Vector<Brick> bricks;

    // Ball related temporal variables
    PointF ballPosition;
    PointF ballDirection;
    float ballRadius;
    float ballVelocity;

    // Paddle related temporal variables
    PointF paddlePosition;
    PointF paddleSize;
    float paddleVelocity;

    // Bricks related
    Brick tempBrick;
    int horNumOfBricks;
    int verNumOfBricks;
    PointF brickSize;

    /**
     * Ball-class constructor.
     *
     * @param context View's context.
     */
    public GameView( Context context ) {
        super(context);
        gestureDetector = new GestureDetector(context, this);
        appContext = context;

        ballPosition = new PointF();
        ballDirection = new PointF();
        paddlePosition = new PointF();
        paddleSize = new PointF();
        brickSize = new PointF();

        score = 0;
        lives = 3;
    }

    /**
     * Screen size changed.
     *
     * @param w New screen width.
     * @param h New screen height.
     * @param oldW Old screen width.
     * @param oldH Old screen height.
     */
    @Override
    protected void onSizeChanged( int w, int h, int oldW, int oldH ) {
        // Create the bouncing area.
        bouncingArea = new RectF(0, h * 0.1f, w, h);

        // Bricks number and size
        horNumOfBricks = 10;
        verNumOfBricks = 5;
        bricksLeft = horNumOfBricks * verNumOfBricks;
        brickSize.x = bouncingArea.right / (horNumOfBricks + 2);
        brickSize.y = bouncingArea.bottom * 0.03f;

        // Create bricks
        bricks = new Vector<Brick>();

        for (int i = 0; i < horNumOfBricks; ++i) {
            for (int j = 0; j < verNumOfBricks; ++j) {
                int color;

                switch (j) {
                    case 0:
                        color = Color.RED;
                        break;

                    case 1:
                        color = Color.MAGENTA;
                        break;

                    case 2:
                        color = Color.YELLOW;
                        break;

                    case 3:
                        color = Color.GREEN;
                        break;

                    case 4:
                        color = Color.BLUE;
                        break;

                    default:
                        color = Color.BLACK;
                }

                tempBrick = new Brick(
                        new PointF(bouncingArea.left + brickSize.x * 1.5f + brickSize.x * i,
                                bouncingArea.top + brickSize.y * 3.0f + brickSize.y * j),
                        new PointF(brickSize.x, brickSize.y),
                        color);
                tempBrick.setRow(verNumOfBricks - j - 1);
                bricks.addElement(tempBrick);
            }
        }

        // Start a new game.
        newGame();
    }

    /**
     * Create a new game.
     */
    private void newGame() {
        lives = 3;
        score = 0;
        bricksLeft = horNumOfBricks * verNumOfBricks;
        startVelocity = bouncingArea.bottom * 0.005f;
        resetPaddleAndBall();
        resetBricks();
    }

    /**
     * Reset a paddle and a ball
     */
    private void resetPaddleAndBall() {
        // Set the paddle's position, size and velocity.
        paddlePosition.x = bouncingArea.right / 2;
        paddlePosition.y = bouncingArea.bottom * 0.85f;
        paddleSize.x = bouncingArea.right * 0.3f;
        paddleSize.y = 2 * bouncingArea.bottom * 0.02f;
        paddleVelocity = 0.0f;

        // Set the ball's position, direction and radius.
        ballRadius = bouncingArea.bottom * 0.01f;
        ballPosition.x = bouncingArea.right / 2;
        ballPosition.y = paddlePosition.y - paddleSize.y / 2 - ballRadius;
        ballDirection.x = (float) Math.random();
        ballDirection.y = (float) (Math.sqrt(1 - ballDirection.x * ballDirection.x));
        ballDirection.y = ballDirection.y < 0 ? ballDirection.y : -ballDirection.y;
        ballVelocity = startVelocity;

        // Create a ball.
        ball = new Ball( ballPosition,
                ballDirection,
                ballRadius,
                ballVelocity,
                ballVelocity,
                Color.GREEN,
                0xFF007F00);

        // Create a paddle.
        paddle = new Paddle(paddlePosition, paddleSize, paddleVelocity, Color.BLUE);
    }

    /**
     * Reset the bricks
     */
    private void resetBricks() {
        bricksLeft = horNumOfBricks * verNumOfBricks;
        for (Brick brick : bricks) {
            brick.setDestroyed(false);
        }
    }

    /**
     * Draws the objects on screen.
     */
    @Override
    protected void onDraw( Canvas canvas ) {
        if (ball != null && paddle != null && bricks!= null && bouncingArea != null) {
            ball.draw(canvas);
            paddle.draw(canvas);

            for (Brick brick : bricks) {
                brick.draw(canvas);
            }

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(3);
            canvas.drawRect((int) bouncingArea.left, (int) bouncingArea.top,
                    (int) bouncingArea.right, (int) bouncingArea.bottom, paint);

            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(50);
            canvas.drawText( "Lives: "   +  lives  + " Score: "  +  score,
                    50, 50, paint ) ;
        }
    }


    /**
     * Has the ball or the paddle hit the bouncing area walls?
     */
    private void wallCollisionDetection() {
        RectF ballHitBox = ball.getHitBox();
        RectF paddleHitBox = paddle.getHitBox();

        if (ballHitBox.left <= bouncingArea.left || ballHitBox.right >= bouncingArea.right) {
            ballDirection.x *= -1;
        }

        if (ballHitBox.top <= bouncingArea.top) {
            ballDirection.y *= -1;
        }

        if (paddleHitBox.left <= bouncingArea.left) {
            paddleHitBox.offsetTo(bouncingArea.left, paddleHitBox.top);
            paddlePosition.x = paddleHitBox.centerX();
            if (paddleVelocity < 0.0f) {
                paddleVelocity = 0.0f;
            }
        }

        if (paddleHitBox.right >= bouncingArea.right) {
            paddleHitBox.offsetTo(bouncingArea.right - paddleHitBox.width(), paddleHitBox.top);
            paddlePosition.x = paddleHitBox.centerX();
            if (paddleVelocity > 0.0f) {
                paddleVelocity = 0.0f;
            }
        }

        if (ballHitBox.bottom >= bouncingArea.bottom) {
            lives--;
            if (lives == 0) {
                newGame();
            } else {
                resetPaddleAndBall();
            }
        }
    }

    /**
     * Has the ball hit the paddle?
     */
    private void paddleBallCollisionDetection() {
        RectF paddleHitBox = paddle.getHitBox();
        RectF ballHitBox = ball.getHitBox();

        if(ballHitBox.top + ballDirection.y * ballVelocity <= paddleHitBox.bottom &&
                ballHitBox.bottom + ballDirection.y * ballVelocity >= paddleHitBox.top &&
                ballHitBox.left >= paddleHitBox.left &&
                ballHitBox.right <= paddleHitBox.right)
        {
            PointF normal = new PointF();
            float ballX = ballHitBox.centerX();
            float paddleX = paddleHitBox.centerX();
            float paddleWidth = paddleHitBox.width() / 2;

            normal.x = -(paddleX - ballX) / paddleWidth;
            normal.y = (float) -Math.sqrt(1 - normal.x * normal.x);

            ballDirection = normal;
        }
    }

    /**
     * Has the ball hit a brick?
     */
    private void brickBallCollisionDetection(Brick brick) {
        RectF ballHitBox = ball.getHitBox();
        RectF brickHitBox = brick.getHitBox();

        if (RectF.intersects(ballHitBox, brickHitBox)) {
            if (ballHitBox.centerX() > brickHitBox.left
                    && ballHitBox.centerX() < brickHitBox.right) {
                ballDirection.y *= -1;
            }
            if (ballHitBox.centerY() > brickHitBox.top
                    && ballHitBox.centerY() < brickHitBox.bottom) {
                ballDirection.x *= -1;
            }

            brick.setDestroyed(true);
            ballVelocity = startVelocity + bouncingArea.bottom * 0.001f * brick.getRow();
            score += brick.getRow() + 1;
            bricksLeft--;
        }
    }

    /**
     * Game loop
     */
    public void run() {
        while (executeThread == true) {
            if (ball != null && paddle != null && bricks != null) {
                ballPosition = ball.getPosition();
                paddlePosition = paddle.getPosition();

                wallCollisionDetection();
                paddleBallCollisionDetection();

                for (int i = 0; i < horNumOfBricks * verNumOfBricks; ++i) {
                    Brick brick = bricks.elementAt(i);
                    if (brick.getDestroyed() == false) {
                        brickBallCollisionDetection(brick);
                    }
                }

                ball.setDirection(ballDirection);
                ball.setVelocity(ballVelocity);
                paddle.setPosition(paddlePosition);
                paddle.setVelocity(paddleVelocity);

                if (bricksLeft == 0) {
                    startVelocity += 0.01f;
                    resetPaddleAndBall();
                    resetBricks();
                }

                ball.move();
                ball.animate();
                paddle.move();
            }

            postInvalidate();

            try {
                Thread.sleep(10);
            } catch (InterruptedException caught_exception) {
                // No actions to handle the exception.
            }
        }
    }

    /**
     * Starts the thread
     */
    public void startAnimationThread() {
        if (animationThread == null) {
            executeThread = true;
            animationThread = new Thread(this);
            animationThread.start();
        }
    }

    /**
     * Stops the thread
     */
    public void stopAnimationThread() {
        if (animationThread != null) {
            executeThread = false;
            animationThread.interrupt();
            animationThread = null;
        }
    }

    public boolean onTouchEvent ( MotionEvent motion_event ) {
        gestureDetector.onTouchEvent(motion_event);
        return true;
    }
    public boolean onScroll( MotionEvent first_down_motion,
                             MotionEvent last_move_motion,
                             float distanceX, float distanceY ) {
        paddleVelocity = -distanceX;
        paddle.setVelocity(paddleVelocity);

        return true;
    }

    public boolean onDown( MotionEvent motionEvent ) { return true; }
    public void onShowPress( MotionEvent down_motion ) { }
    public boolean onSingleTapUp( MotionEvent up_motion ) { return true; }
    public void onLongPress( MotionEvent first_down_motion ) { }
    public boolean onFling( MotionEvent f, MotionEvent l, float vX, float vY ) { return true ; }
}
