package com.jiafan.qirenjongbao.utils;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;

import org.apache.commons.codec.binary.Base64;

import com.jiafan.qirenjongbao.application.Myaplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SPUtils {

	private static Myaplication walkingaplication;
	private static Context mcontext;
	private static SharedPreferences sp;
	private static Editor ed;
	public static SharedPreferences getSP(){
		walkingaplication = new Myaplication();
		mcontext = walkingaplication.getApplication();
		SharedPreferences sp=mcontext.getSharedPreferences("common",mcontext.MODE_PRIVATE );
		return sp;
	}
	
	public static boolean putSPstring(String key,String value){
		sp=getSP();
		ed=sp.edit();
		ed.putString(key, value);
		boolean flag=ed.commit();
		return flag;
	}
	
	public static boolean putSPint(String key,int value){
		sp=getSP();
		ed=sp.edit();
		ed.putInt(key, value);
		boolean flag=ed.commit();
		return flag;
	}
	
	public static int getSPint(String key,int defValue){
		sp=getSP();
		int k=sp.getInt(key, defValue);
		return k;
	}
	
	public static String getSPstring(String key,String defValue){
		sp=getSP();
		String k=sp.getString(key, defValue);
		return k;
	}
	
	public static <T> Boolean putObject(String key,T object){
		ByteArrayOutputStream baos = new ByteArrayOutputStream(3000);
        ObjectOutputStream oos=null;
        try {
         oos = new ObjectOutputStream(baos);
         oos.writeObject(object);
        } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
        }
        // 将Product对象放到OutputStream中
        // 将Product对象转换成byte数组，并将其进行base64编码
        String newWord = new String(Base64.encodeBase64(baos.toByteArray()));
      
        // 将编码后的字符串写到base64.xml文件中
        sp=getSP();
		ed=sp.edit();
		ed.putString(key, newWord);
		boolean flag=ed.commit();
		return flag;
	}
	
	public static <T> T getObject(String key){
		sp=getSP();
		String k=sp.getString(key, null);
		 try {
		// 对Base64格式的字符串进行解码
		       byte[] base64Bytes = Base64.decodeBase64(k.getBytes());
		         ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
		      ObjectInputStream ois = new ObjectInputStream(bais);
		  // 从ObjectInputStream中读取Product对象
				T addWord= (T) ois.readObject();
				return addWord;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return null;
			}
		 
	}
	
	
}
