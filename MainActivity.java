package com.example.takayama.android_theme_pat3;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;


public class MainActivity extends ActionBarActivity {
    private Menu menu;
    private ProgressDialog dlg = null;
    //instantiate用
    CameraPreview cameraPreview;
    OverLayView overlay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Progress Dialog settings
        dlg = new ProgressDialog(this);
        dlg_settings();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN );
        cameraPreview = new CameraPreview( this );
        this.setContentView( cameraPreview, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        //オーバーレイする. Layoutのパラメータについて：http://tande.jp/lab/2013/01/1962
        overlay = new OverLayView(this);
        addContentView( overlay, new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT )
        );

    }


    public android.hardware.Camera.PictureCallback jpegListener = new android.hardware.Camera.PictureCallback() {
        //撮影後にコールバックされる（takePictureの後に呼ばれる）
        @Override
        public void onPictureTaken(byte[] data, android.hardware.Camera c) {
            //撮影された画像データをバイト配列として受け取る。　配列を画像化してSDに保存。
            //カメラのイメージ
            Bitmap cameraMap = BitmapFactory.decodeByteArray(data, 0, data.length, null);
            //オーバーレイイメージ。Viewから画像を取得.(描画キャッシュを用いる手法)
            overlay.destroyDrawingCache(); // 既存のキャッシュをクリアする
            Bitmap overlayMap = overlay.getDrawingCache(); //キャッシュを作成して取得

            //カメラと同じサイズの空のイメージを作成
            Bitmap offBitmap = Bitmap.createBitmap( cameraMap.getWidth(), cameraMap.getHeight(), Bitmap.Config.ARGB_8888 );
            Canvas offScreen = new Canvas(offBitmap);
            //空のCanvasにカメライメージとオーバーレイイメージをのっける
            offScreen.drawBitmap(
                    cameraMap,
                    null,
                    new Rect(0, 0, cameraMap.getWidth(), cameraMap.getHeight()), null);
            offScreen.drawBitmap(
                    overlayMap,
                    null,
                    new Rect(0, 0, cameraMap.getWidth(), cameraMap.getHeight()), null);

            //保存。第三引数（""のところ）が名前。
            MediaStore.Images.Media.insertImage(getContentResolver(), offBitmap, "", null);

            //Preview再開（これを行わないとPreviewが再開されない）
            cameraPreview.mCam.startPreview();
            //Progress Dialogを閉じる
            dlg.dismiss();
        }
    };


    //タッチイベントで撮影
    @Override
    public boolean onTouchEvent( MotionEvent event ) {
        if( event.getAction() == MotionEvent.ACTION_DOWN ) {
            // オートフォーカスを行う
            cameraPreview.mCam.autoFocus(mAutoFocusListener);
        }
        //Progress Dialogの表示
        dlg.show();

        return super.onTouchEvent( event );
    }


    /**
     * オートフォーカス完了のコールバック
     */
    private android.hardware.Camera.AutoFocusCallback mAutoFocusListener = new android.hardware.Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, android.hardware.Camera camera) {
            //撮影そのものはtakePictureで実行される.画面タッチで撮影される
            cameraPreview.mCam.takePicture( null, null, jpegListener ); //thisはonPictureTakenを指す
        }
    };



    //メニューが押されたときに一度呼び出されるメソッド
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //Progress Dialog 設定用自作メソッド
    public void dlg_settings() {
        // Progress Dialog タイトル, 本文を設定
        dlg.setTitle("画像を保存中");
        dlg.setMessage("しばらくお待ちください.");
        // Progress Dialog スタイルを設定
        dlg.setProgressStyle( ProgressDialog.STYLE_SPINNER );
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch( id ) {
            case R.id.menu_kanna1 :
                overlay.assign_menu = 0;
                overlay.postInvalidate(); //再描画。別スレッドからのinvalidate()メソッド呼び出しには、postInvalidate()を使う
                break;

            case R.id.menu_kanna2 :
                overlay.assign_menu = 1;
                overlay.postInvalidate(); //再描画。
                break;

            case R.id.menu_kanna3 :
                overlay.assign_menu = 2;
                overlay.postInvalidate(); //再描画。
                break;
            case R.id.menu_kanna4 :
                overlay.assign_menu = 3;
                overlay.postInvalidate();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
