package com.ffo.ipiker.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ffo.ipiker.R;
import com.ffo.ipiker.util.LogUtil;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Author: huchunhua
 * Time: 2017/8/31 18:47
 * Package: com.ffo.ipiker.activity
 * Project: IPiker
 * Mail: 742295818@qq.com
 * Describe: 一句话描述
 */

public class EvidenceActiviry extends BaseActivity implements SurfaceHolder.Callback {

    private static final String TAG = EvidenceActiviry.class.getSimpleName();
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;

    private CameraManager mCameraManager;
    private CameraDevice mCameraDevice;
    private CaptureRequest.Builder mCaptureRequestBuilder;
    private CameraCaptureSession mCameraCaptureSession;
    /**
     * 摄像头ID（通常0代表后置摄像头，1代表前置摄像头）
     * 默认为0
     */
    private String cameraID = "0";
    private HandlerThread mHandlerThread;
    private Handler mBgHandler;
    private ImageReader mImageReader;

    private final int MY_PERMISSION_CAMERA = 0x0001;

    /**
     * 预览尺寸
     */
    private Size previewSize;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_layout);

        initHandlerThread();

        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        initView();
        chenkPrimission();
        LogUtil.v(TAG, "EvidenceActivity onCreate() Finished!!");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        setContentView(R.layout.clear_null);
    }


    private void initHandlerThread() {
        mHandlerThread = new HandlerThread("camera");
        mHandlerThread.start();
        mBgHandler = new Handler(mHandlerThread.getLooper());
    }

    /**
     * 运行时权限，检测Camera权限
     */
    private void chenkPrimission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager
                .PERMISSION_DENIED) {
            // 已有权限
            mSurfaceHolder.addCallback(this);
        } else {
            //  未有权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSION_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager
                        .PERMISSION_GRANTED) {
                    mSurfaceHolder.addCallback(this);
                } else {
                    //权限被拒绝，返回上一个activity
                    this.finish();
                }
                break;
            default:
                break;
        }
    }

    /**
     * initViewS
     */
    private void initView() {
        findLayout();
        findView();
    }

    /**
     * findView
     */
    private void findLayout() {
    }

    /**
     * findView
     */
    private void findView() {
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();
//        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setUpCameraOutputs(1080, 1920);
        initImageReader(1080, 1920);
        initCameraAndPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    private void setUpCameraOutputs(int width, int height) {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                try {
                    cameraID = mCameraManager.getCameraIdList()[0];
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                    LogUtil.v(TAG, "getCameraIdList()[0] Fiald!!");
                }
            }

            // 获取指定摄像头的特性
            CameraCharacteristics characteristics
                    = manager.getCameraCharacteristics(cameraID);
            // 获取摄像头支持的配置属性
            StreamConfigurationMap map = characteristics.get(
                    CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            // 获取摄像头支持的最大尺寸
            Size largest = Collections.max(
                    Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                    new CompareSizesByArea());
            // 获取最佳的预览尺寸
            previewSize = chooseOptimalSize(map.getOutputSizes(
                    SurfaceView.class), width, height, largest);
            // 根据选中的预览尺寸来调整预览组件（TextureView）的长宽比
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mSurfaceView.setMinimumWidth(previewSize.getWidth());
                mSurfaceView.setMinimumHeight(previewSize.getHeight());
            } else {
                mSurfaceView.setMinimumWidth(previewSize.getHeight());
                mSurfaceView.setMinimumHeight(previewSize.getWidth());
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            LogUtil.i(TAG, "setUpCameraOutputs ：" + e);
        }
    }

    /**
     * 初始化Camera
     */
    private void initCameraAndPreview() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED) {
                mCameraManager.openCamera(cameraID, cameraDeviceCallback, mBgHandler);
            } else {
                this.finish();
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    private CameraDevice.StateCallback cameraDeviceCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mCameraDevice = camera;
            startPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {

        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {

        }
    };

    /**
     * 开始预览
     */
    private void startPreview() {
        try {
            mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice
                    .TEMPLATE_PREVIEW);
            mCaptureRequestBuilder.addTarget(mSurfaceHolder.getSurface());
            mCameraDevice.createCaptureSession(Arrays.asList(mSurfaceHolder.getSurface(),
                    mImageReader.getSurface()), cameraCaptureSessionCallback, mBgHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    CameraCaptureSession.StateCallback cameraCaptureSessionCallback = new CameraCaptureSession
            .StateCallback() {


        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            mCameraCaptureSession = session;
            mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            mCaptureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            try {
                mCameraCaptureSession.setRepeatingRequest(mCaptureRequestBuilder.build(), listener, mBgHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {

        }
    };

    CameraCaptureSession.CaptureCallback listener = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request,
                                       @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
        }
    };

    /**
     * initImageReader
     */
    private void initImageReader(int with, int height) {
//        mImageReader = ImageReader.newInstance(with, height, ImageFormat.YV12, 1);
        mImageReader = ImageReader.newInstance(mSurfaceView.getWidth(), mSurfaceView.getHeight(), ImageFormat.YV12, 1);
        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                // 获取捕获的照片数据
//                Image image = reader.acquireNextImage();
//                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
//                byte[] bytes = new byte[buffer.remaining()];
//                buffer.get(bytes);
//                image.close();
            }
        }, mBgHandler);
    }

    private static Size chooseOptimalSize(Size[] choices
            , int width, int height, Size aspectRatio) {
        // 收集摄像头支持的大过预览Surface的分辨率
        List<Size> bigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getHeight() == option.getWidth() * h / w &&
                    option.getWidth() >= width && option.getHeight() >= height) {
                bigEnough.add(option);
            }
        }
        // 如果找到多个预览尺寸，获取其中面积最小的
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else {
            System.out.println("找不到合适的预览尺寸！！！");
            return choices[0];
        }
    }
}

// 为Size定义一个比较器Comparator
class CompareSizesByArea implements Comparator<Size> {
    @Override
    public int compare(Size lhs, Size rhs) {
        // 强转为long保证不会发生溢出
        return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                (long) rhs.getWidth() * rhs.getHeight());
    }
}
