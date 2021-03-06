package ubicomp.ketdiary.utility.test.camera;

import android.os.Handler;
import android.os.Message;

/**
 * Handler of handling the camera
 * 
 * @author Stanley Wang
 */
public class CameraInitHandler extends Handler {

	private Tester tester;
	private CameraRecorder cameraRecorder;

	/**
	 * Constructor
	 * 
	 * @param tester
	 *            class support brac tests
	 * @param cameraRecorder
	 *            camera recorder
	 */
	public CameraInitHandler(Tester tester, CameraRecorder cameraRecorder) {
		this.tester = tester;
		this.cameraRecorder = cameraRecorder;
	}

	@Override
	public void handleMessage(Message msg) {
		cameraRecorder.initialize();
		tester.updateInitState(Tester._CAMERA);
	}
}
