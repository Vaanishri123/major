package com.example.hp.assistant;

import android.content.Context;
import android.hardware.camera2.CameraManager;

public class FlashActivity {

    Context context;
    String text;

    public FlashActivity(Context context, String text) {
        this.context = context;
        this.text = text;
    }

    public boolean openOrCloseFlash() {
        try {
            CameraManager camManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            String cameraId = camManager.getCameraIdList()[0]; // Usually front camera is at 0 position.
            if (FA.match("on", text) || FA.match("open", text)) {
                camManager.setTorchMode(cameraId, true);
            } else if (FA.match("off", text) || FA.match("close", text)) {
                camManager.setTorchMode(cameraId, false);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
