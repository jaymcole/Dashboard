package dashboard.apps.bouncingBalls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import dashboard.apps.BaseApp;
import dashboard.miscDataObjects.RenderInfo;
import dashboard.miscDataObjects.UpdateInfo;
import dashboard.rendering.BoundingBox;

import java.util.ArrayList;
import java.util.HashMap;
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

        public void update (float delta, float gravity) {
            x += directionX * delta;
            y += directionY * delta;
            directionY -= gravity;


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
                directionY *= 0.9f;
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
        Color.GREEN,
        Color.CHARTREUSE,
        Color.MAGENTA,
        Color.ROYAL
    };

    private final int MINIMUM_BALL_SIZE = 3; // in pixel radius
    private final int MAX_BALL_SIZE = 20;

    private Random random;
    private List<Ball> balls;


    private float gravity = 9.8f;

    public BouncingBallsApp(BoundingBox appBounds) {
        super(appBounds);
        random = new Random();
        balls = new ArrayList<>();
    }

    @Override
    public void update(UpdateInfo updateInfo) {
//        if (random.nextInt(1000) < 10) {
//            random = null;
//        }
        for(Ball ball : balls) {
            ball.update(updateInfo.delta, gravity);
        }
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
    public void resizeApp() {
        if (balls != null) {
            for (Ball ball : balls) {
                ball.bounds = appBounds;
            }
        }
    }

    // test
    private static final String BallCountSettingKey = "BallsCount";

    @Override
    public void loadSettings(HashMap<String, String> savedSettings) {
        if (savedSettings.containsKey(BallCountSettingKey)) {
            int newBallCount = Integer.parseInt(savedSettings.get(BallCountSettingKey));
            adjustBallCount(newBallCount);
        }
    }

    @Override
    public HashMap<String, String> saveSettings() {
        HashMap<String, String> settings = new HashMap<>();
        settings.put(BallCountSettingKey, String.valueOf(balls.size()));
        return settings;
    }

    @Override
    public List<Actor> getSettingsUiActors() {
        List<Actor> settingsButtons = new ArrayList<>();
        Skin skin = new Skin((Gdx.files.internal("skins/metalui/metal-ui.json")));

        Table ballCountTable = new Table();
        TextButton ballCountLabel = new TextButton("Balls: " + balls.size(), skin);

        Slider ballsSlider = new Slider(0, 100, 1, false, skin);
        ballsSlider.setValue(balls.size());
        ballsSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                if (actor instanceof Slider actorSlider) {
                    adjustBallCount((int)actorSlider.getValue());
                    ballCountLabel.setText("Balls: " + balls.size());
                    System.out.println("Setting balls to: " + (int)actorSlider.getValue());
                }
            }
        });
        ballCountTable.add(ballCountLabel);
        ballCountTable.add(ballsSlider);
        settingsButtons.add(ballCountTable);


        Table gravityTable = new Table();
        TextButton sliderLabel = new TextButton("Gravity: " + gravity, skin);
        sliderLabel.setDisabled(true);

        Slider gravitySlider = new Slider(-5, 20, 0.01f, false, skin);
        gravitySlider.setValue(gravity);
        gravitySlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                if (actor instanceof Slider actorSlider) {
                    gravity = actorSlider.getValue();
                    sliderLabel.setText(String.format("Gravity %.2f", gravity));
                }
            }
        });
        TextButton gravityResetButton = new TextButton("9.8", skin);
        gravityResetButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                gravity = 9.8f;
                sliderLabel.setText(String.format("Gravity %.2f", gravity));
                gravitySlider.setValue(gravity);
            }
        });

        gravityTable.add(sliderLabel);
        gravityTable.add(gravitySlider);
        gravityTable.add(gravityResetButton);

        settingsButtons.add(gravityTable);
        return settingsButtons;
    }

    public void adjustBallCount(int newCount) {
        while (newCount < balls.size()) {
            balls.remove(random.nextInt(balls.size()));
        }
        while (newCount > balls.size()) {
            balls.add(constructBall());
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
