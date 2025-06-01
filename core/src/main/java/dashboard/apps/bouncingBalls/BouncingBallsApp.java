package dashboard.apps.bouncingBalls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dashboard.apps.BaseApp;
import dashboard.miscDataObjects.RenderInfo;
import dashboard.miscDataObjects.UpdateInfo;
import dashboard.rendering.BoundingBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BouncingBallsApp extends BaseApp {

    private class Ball {
        public float x, y, directionX, directionY, size;
        public Color color;
        private BoundingBox bounds;

        public Ball(float x, float y, float directionX, float directionY, float size, BoundingBox bounds, Color color) {
            this.x = x;
            this.y = y;
            this.directionX = directionX;
            this.directionY = directionY;
            this.size = size;
            this.bounds = bounds;
            this.color = color;
        }

        public void update (float delta) {
            x += directionX * delta;
            y += directionY * delta;
            directionY -= 9.8f;


            if (x < bounds.getX() + size) {
                directionX *= -1;
                x = bounds.getX() + size;
                directionX *= 1.2f;

            } else if (x > bounds.getX() + bounds.getWidth() - size) {
                directionX *= -1;
                x = bounds.getX() + bounds.getWidth() - size;
                directionX *= 1.2f;

            }

            if (directionX > 1000) {
                directionX *= 0.1f;
            }

            if (y < bounds.getY() + size) {
                directionY *= -1;
                directionY *= 0.95f;
                y = bounds.getY() + 1 + size;
            }
        }

        public void render(ShapeRenderer shapeRenderer) {
            shapeRenderer.setColor(color);
            shapeRenderer.circle(x, y, size);
        }
    }

    private final Color[] colorOptions = new Color[] {
        Color.RED,
        Color.YELLOW,
        Color.BLUE,
        Color.GREEN
    };

    private final int MINIMUM_BALL_SIZE = 3; // in pixel radius
    private final int MAX_BALL_SIZE = 20;

    private Random random;
    private List<Ball> balls;


    public BouncingBallsApp() {
        super();
        random = new Random();
        balls = new ArrayList<>();
    }

    @Override
    public void update(UpdateInfo updateInfo) {
        for(Ball ball : balls) {
            ball.update(updateInfo.delta);
        }
        // Randomly break app
//        if (random.nextInt(1000) < 3) {
//            random = null;
//        }
    }

    @Override
    public void render(RenderInfo renderInfo) {
        matrix.setToOrtho2D(0, 0, appBounds.getWidth(), appBounds.getHeight());
        shapeRenderer.setProjectionMatrix(matrix);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(appBounds.getX(), appBounds.getY(), appBounds.getWidth(), appBounds.getHeight());

        for(Ball ball : balls) {
            ball.render(shapeRenderer);
        }
        shapeRenderer.end();
    }

    @Override
    public void resize() {
        if (balls.isEmpty()) {
            for(int i = 0; i < 15; i++ ) {
                balls.add(constructBall());
            }
        } else {
            for (Ball ball : balls) {
                ball.bounds = appBounds;
            }
        }
    }

    private Ball constructBall() {
        return new Ball(
            random.nextInt((int)appBounds.getWidth()),
            random.nextInt((int)appBounds.getHeight()),
            random.nextInt(30) + 5,
            random.nextInt(30) - 15,
            random.nextInt(MAX_BALL_SIZE - MINIMUM_BALL_SIZE) + MINIMUM_BALL_SIZE,
            appBounds,
            colorOptions[random.nextInt(colorOptions.length)]
        );
    }
}
