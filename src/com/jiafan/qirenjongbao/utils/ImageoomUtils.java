package com.jiafan.qirenjongbao.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.view.Display;
import android.widget.ImageView;

public class ImageoomUtils {

	private static ImageView iv;

	public static Bitmap imageOomutils(Activity context,Context mcontext,int id){
		Options option = new Options();
		option.inJustDecodeBounds = true;
		iv = new ImageView(context);
		Bitmap bitmap = null;
		BitmapFactory.decodeResource(context.getResources(),
				id, option);
		//拿到图片的宽高
		int imagewith=option.outWidth;
		int imageheight=option.outHeight;
		
		Display dp=context.getWindowManager().getDefaultDisplay();
		int screenwith=dp.getWidth();
		int screenheight=dp.getHeight();
		int scale=1;
		int scalex=imagewith/screenwith;
		int scaley=imageheight/screenheight;
		if(scalex>=scaley&&scalex>1){
			scale=scalex;
		}else if(scaley>scalex && scaley>1){
			scale=scaley;
		}
		option.inSampleSize=scale;
		option.inJustDecodeBounds = false;
		Bitmap bm=BitmapFactory.decodeResource(mcontext.getResources(), id, option);
		return bm;
	}

}
