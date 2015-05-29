package com.example.takayama.android_theme_pat3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

/**
 * Created by admin on 2015/04/25.
 */
public class OverLayView extends View {
    private Bitmap icon[];
    int width;
    int height;

    public int assign_menu;

    //Constructor
    public OverLayView(Context context) {
        super(context);

        setDrawingCacheEnabled(true); //キャッシュを取得する設定にする

        //画像宣言
        icon = new Bitmap[4];
        icon[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.hashimoto_kannna1 );
        icon[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.hashimoto_kannna2 );
        icon[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.hashimoto_kannna3 );
        icon[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.hashimoto_kannna4 );

        //変数初期化
        assign_menu = 0;

        setFocusable(true);
    }

    //Viewサイズが変更されたときに呼び出されるメソッド
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        //Viewのサイズを取得
        width= w;
        height= h;
    }

    @Override
    protected void onDraw( Canvas canvas ) {
        super.onDraw( canvas );
        //透過部分は透過で
        canvas.drawColor( Color.TRANSPARENT );


        //Viewのサイズから画像サイズを引くことで画面の右端にいい感じに表示
        canvas.drawBitmap( icon[ assign_menu ], width - icon[ assign_menu ].getWidth(), 0, null );
    }
}
