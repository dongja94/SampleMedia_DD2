package com.begentgroup.samplemedia;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class CameraActivity extends AppCompatActivity implements
        SurfaceHolder.Callback {

    Camera mCamera;
    SurfaceHolder mHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        SurfaceView displayView = (SurfaceView)findViewById(R.id.surface_preview);
        displayView.getHolder().addCallback(this);

        Button btn = (Button)findViewById(R.id.btn_change);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeCamera();
            }
        });
    }

    private void changeCamera() {
        if (Camera.getNumberOfCameras() > 1) {
            cameraId = (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) ?
                    Camera.CameraInfo.CAMERA_FACING_FRONT :
                    Camera.CameraInfo.CAMERA_FACING_BACK;
            stopPreview();
            openCamera();
            startPreview();
        }
    }
    private static final int[] ORIENTATION = { 90, 0, 270 , 180};
    int cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private void openCamera() {
        releaseCamera();
        mCamera = Camera.open(cameraId);
        Display display = getWindowManager().getDefaultDisplay();
        int rotation = display.getRotation();
        int orientation = ORIENTATION[rotation];
        mCamera.setDisplayOrientation(orientation);
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private void startPreview() {
        if (mHolder != null) {
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopPreview() {
        try {
            mCamera.stopPreview();
        } catch(Exception e) {

        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        openCamera();
        mHolder = surfaceHolder;
        startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        stopPreview();
        mHolder = surfaceHolder;
        startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        stopPreview();
        mHolder = null;
        releaseCamera();
    }
}
