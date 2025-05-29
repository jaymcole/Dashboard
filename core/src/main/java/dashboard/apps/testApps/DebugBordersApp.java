package dashboard.apps.testApps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dashboard.apps.BaseApp;
import dashboard.dataObjects.RenderInfo;
import dashboard.dataObjects.UpdateInfo;

public class DebugBordersApp extends BaseApp {


    public DebugBordersApp(Color DebugColor) {
        super();

    }

    @Override
    public void update(UpdateInfo updateInfo) {
    }

    @Override
    public void render(RenderInfo renderInfo) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(1,1, renderInfo.maxWidth-2, renderInfo.maxHeight-2);

        shapeRenderer.setColor(Color.SKY);
        shapeRenderer.line(1,1,renderInfo.maxWidth , renderInfo.maxHeight);
        shapeRenderer.line(1,renderInfo.maxHeight,renderInfo.maxWidth, 1);

        shapeRenderer.circle(0,0,50);
        shapeRenderer.circle(0,0,50);

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.line(1,1,100, 1);

        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.line(1,1, 1, 100);



        shapeRenderer.setColor(Color.RED);
        shapeRenderer.line(1,1, renderInfo.maxWidth, renderInfo.maxHeight);


        shapeRenderer.end();
    }


}
