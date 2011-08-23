package aurelienribon.box2deditor.renderpanel.inputprocessors;

import aurelienribon.box2deditor.renderpanel.RenderPanel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Aurelien Ribon (aurelien.ribon@gmail.com)
 */
public class PanZoomInputProcessor extends InputAdapter {
	private final Vector2 lastTouch = new Vector2();

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		if (button != Buttons.RIGHT)
			return false;

		lastTouch.set(x, y);
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		if (!Gdx.input.isButtonPressed(Buttons.RIGHT))
			return false;

		OrthographicCamera camera = RenderPanel.instance().getCamera();
		Vector2 delta = new Vector2(x, y).sub(lastTouch).mul(camera.zoom);
		camera.translate(-delta.x, delta.y, 0);
		camera.update();
		lastTouch.set(x, y);
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		RenderPanel app = RenderPanel.instance();
		int[] zl = app.getZoomLevels();

		if (app.getZoom() == zl[0] && amount < 0) {
			app.setZoom(zl[1]);
		} else  if (app.getZoom() == zl[zl.length-1] && amount > 0) {
			app.setZoom(zl[zl.length-2]);
		} else {
			for (int i=1; i<zl.length-1; i++) {
				if (zl[i] == app.getZoom()) {
					app.setZoom(amount > 0 ? zl[i-1] : zl[i+1]);
					break;
				}
			}
		}

		OrthographicCamera camera = app.getCamera();
		app.getCamera().zoom = 100f / app.getZoom();
		app.getCamera().update();
		return false;
	}
}
