package com.example.takayama.android_theme_pat3;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.Camera;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by admin on 2015/04/25.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder mSurfaceHolder;
    Camera mCam;

    //サーフェスホルダーの取得とコールバックの通知先の設定
    public CameraPreview( Context context ) {
        super( context );

//        mCam = mCamera;
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback( this );
    }


    //SurfaceView生成。カメラを取得し、Holderにセットする
    @Override
    public void surfaceCreated( SurfaceHolder holder ) {
        try {
            //CAMERA_FACING_BACK : 背面
            int openCameraType = Camera.CameraInfo.CAMERA_FACING_BACK;
            if( openCameraType <= Camera.getNumberOfCameras()) {
                mCam = Camera.open( openCameraType ); // カメラインスタンスを取得
                mCam.setPreviewDisplay(holder); //カメラインスタンスに画像表示先を設定
            }
            else {
                Log.d("CameraSample", "cannnot bind camera.");
            }
        } catch( Exception e ) {
            e.printStackTrace();
        }
    }

    //SurfaceViewが破棄されたときに呼ばれる。カメラのプレビューの停止とリリースを行う。
    @Override
    public void surfaceDestroyed( SurfaceHolder holder ) {
        mCam.stopPreview();
        mCam.release();
        mCam = null;
    }

    //SurfaceViewの形状が変化したときに呼ばれる。surfaceCreatedの後にも一度呼ばれる。カメラのパラメータによりPreviewサイズの変更を行う。
    @Override
    public void surfaceChanged( SurfaceHolder holder, int format, int width, int height ) {
        setPreviewSize( width, height );
        mCam.startPreview();
    }


    //サイズの設定
    protected void setPreviewSize( int width, int height ) {
        Camera.Parameters params = mCam.getParameters();
        List<Camera.Size> supported = params.getSupportedPreviewSizes();
        if( supported != null ) {
            for( Camera.Size size : supported ) {
                if( size.width <= width && size.height <= height ) {
                    params.setPreviewSize( size.width, size.height );
                    mCam.setParameters( params );
                    break;
                }
            }
        }
    }



}
