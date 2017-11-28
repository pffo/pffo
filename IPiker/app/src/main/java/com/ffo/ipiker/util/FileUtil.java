package com.ffo.ipiker.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class FileUtil {

    private static final String TAG = "FileUtil";
    private static final File parentPath = Environment
            .getExternalStorageDirectory();
    private static String storagePath = "";
    private static final String DST_FOLDER_NAME = "IPIKER";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static String IMGPATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/" + "Ipiker" + "/" + "Photo";

    private static FileUtil fileUtil;

    public static synchronized FileUtil getInstance() {
        if (fileUtil == null) {
            fileUtil = new FileUtil();
        }
        return fileUtil;
    }

    /**
     * 初始化保存路径
     *
     * @return
     */
    private static String initPath() {

        if (storagePath.equals("")) {
            storagePath = parentPath.getAbsolutePath() + "/" + DST_FOLDER_NAME
                    + "photographs";
            File f = new File(storagePath);
            if (!f.exists()) {
                f.mkdirs();
            }
        }
        return storagePath;
    }

    /** Create a File for saving an image or video */
    public static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        //
        // File mediaStorageDir = null;
        // try
        // {
        // // This location works best if you want the created images to be
        // // shared
        // // between applications and persist after your app has been
        // // uninstalled.
        // mediaStorageDir = new File(
        // Environment
        // .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
        // "MyCameraApp");
        //
        //
        // }
        // catch (Exception e)
        // {
        // e.printStackTrace();
        // }
        //
        // // Create the storage directory if it does not exist
        // if (!mediaStorageDir.exists())
        // {
        // if (!mediaStorageDir.mkdirs())
        // {
        // // 在SD卡上创建文件夹需要权限：
        // // <uses-permission
        // // android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
        // return null;
        // }
        // }

        String path = initPath();

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(path + File.separator + "IMG_" + timeStamp
                    + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(path + File.separator + "VID_" + timeStamp
                    + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    /**
     * 保存dyte[]到sdcard
     *
     * @param data
     */
    public static void saveByte(byte[] data) {
        Bitmap bm0 = BitmapFactory.decodeByteArray(data, 0, data.length);

        // 图片旋转90度后，叠加文字信息
//        Bitmap bitmap = BitmapOrientation.getInstance().Orientation(bm0);
//        // Bitmap res =
//        // TextToBitmap.getInstance().AddInformationToBitmap(bitmap,
//        // "Lic:" + Recognition_information.getInstance().getLic());
//        Bitmap resTime = TextToBitmap.getInstance().AddTimeToBitmap(bitmap);
//        // 将识别的图片保存在Recognition_information里面
//        // Recognition_information.getInstance().setPicture(resTime);
//
//        Recognition_Result.getInstance().setListBitmap(resTime);

        // saveBitmap(res);
//        saveBitmap(resTime);

        // 直接将图片保存 图片式横着的
        // File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        // try {
        // FileOutputStream fos = new FileOutputStream(pictureFile);
        // fos.write(data);
        // fos.close();
        // } catch (FileNotFoundException e) {
        // } catch (IOException e) {
        // }
    }

    /**
     * 保存Bitmap到sdcard
     *
     * @param b
     */
    public static void saveBitmap(Bitmap b) {

        String path = initPath();
        long dataTake = System.currentTimeMillis();
        String jpegName = path + "/" + dataTake + ".jpg";
        Log.i(TAG, "saveBitmap:jpegName = " + jpegName);
        try {
            FileOutputStream fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 20, bos);
            bos.flush();
            bos.close();
            Log.i(TAG, "saveBitmap成功");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.i(TAG, "saveBitmap:失败");
            e.printStackTrace();
        }

    }

    public static String save(byte[] data) {
        String path = initPath();
        long dataTake = System.currentTimeMillis();
        String jpegName = path + "/" + dataTake + ".jpg";
        try {
            FileOutputStream fout = new FileOutputStream(jpegName);
            fout.write(data);
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jpegName;

    }

    public static String save(byte[] data, String imgPath) {
        // String path = initPath();
        // long dataTake = System.currentTimeMillis();
        // String jpegName = imgPath + "/" + dataTake + ".jpg";

        File f = new File(IMGPATH);
        if (!f.exists()) {
            f.mkdirs();
        }

        // File img = new File(imgPath);
        // if (!img.exists()) {
        // try {
        // img.createNewFile();
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // }1476863926852
        try {
            FileOutputStream fout = new FileOutputStream(imgPath);
            fout.write(data);
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return imgPath;

    }


}
