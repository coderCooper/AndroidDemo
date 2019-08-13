package com.lollipop.white.util;

import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.lollipop.demo.R;
import com.lollipop.white.view.CircleImageTransformation;
import com.lollipop.white.view.RoundRectImageTransformation;

public class AppUtils {

	/**
	 * 获取或创建Cache目录 默认放在SD卡下的textiles文件夹， 所有缓存的文件，
	 *
	 * @param bucket
	 *            临时文件目录，bucket = "/cache/" ，则放在"sdcard/linked-joyrun/cache"; 如果bucket=""或null,则放在"sdcard/addoil/"
	 */
	public static String getMyDefaultCacheDir(String bucket) {
		String dir;

		// 保证目录名称正确
		if (bucket != null) {
			if (!bucket.equals("")) {
				if (!bucket.endsWith("/")) {
					bucket = bucket + "/";
				}
			}
		}

		String joyrun_default = "/textiles/";

		if (isSDCardExist()) {
			dir = Environment.getExternalStorageDirectory().toString() + joyrun_default + bucket;
		} else {
			dir = Environment.getDownloadCacheDirectory().toString() + joyrun_default + bucket;
		}

		File f = new File(dir);
		if (!f.exists()) {
			f.mkdirs();
		}
		return dir;
	}

	public static boolean isSDCardExist() {
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			return false;
		return true;
	}

	public static int getVersionCode(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			int version = info.versionCode;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static String getVersionName(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			String version = info.versionName;
			return version+"";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String formatTime(int ms) {

		int mi = 60;
		int hh = mi * 60;

		int hour = ms / hh;
		int minute = (ms - hour * hh) / mi;
		int second = ms - hour * hh - minute * mi;

		String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
		String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
		String strSecond = second < 10 ? "0" + second : "" + second;//秒

		return strHour + ":" + strMinute + ":" + strSecond;
	}

	public static String formatTimeSF(int ms) {

		int mi = 60;
		int hh = mi * 60;

		int hour = ms / hh;
		int minute = (ms - hour * hh) / mi;
//		int second = ms - hour * hh - minute * mi;

		String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
		String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟

		return strHour + "小时" + strMinute + "分";
	}

	public static String formatTimeDSF(int ms) {
		int mi = 60;
		int hh = mi * 60;
		int dd = hh * 24;

		long day = ms / dd;
		long hour = (ms - day * dd) / hh;
		long minute = (ms - day * dd - hour * hh) / mi;
//		long second = (ms - day * dd - hour * hh - minute * mi) / ss;
//		long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

		String strDay = day < 10 ? "0" + day : "" + day; //天
		String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
		String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
//		String strSecond = second < 10 ? "0" + second : "" + second;//秒
//		String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//毫秒
//		strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;

		return strDay + "天" + strHour + "小时" + strMinute + "分";
	}

	public static String formatDateTime(long time) {
		return formatDateTime("yyyy-MM-dd HH:mm:ss",time);
	}

	public static String formatDateTime(String pattern, long time) {

		SimpleDateFormat format = new SimpleDateFormat(pattern);

		Date date = new Date(time);

		return format.format(date);
	}

	public static String formatDate(long time) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		Date date = new Date(time);

		return format.format(date);
	}

	public static void imageShow(Context mCon, String url, ImageView imageView, boolean showPlaceholder){
		if (showPlaceholder){
			imageShow(mCon, url, R.mipmap.placeholder, imageView);
		} else {
			Picasso.with(mCon).load(url).config(Bitmap.Config.RGB_565).into(imageView);
		}
	}

	public static void imageShow(Context mCon, String url, ImageView imageView){
		imageShow(mCon, url, R.mipmap.placeholder, imageView);
	}

	public static void imageShow(Context mCon, String url, int placeholderResId, ImageView imageView){
		if (TextUtils.isEmpty(url)){
			imageView.setImageResource(R.mipmap.placeholder);
			return;
		}
		Picasso.with(mCon).load(url).placeholder(placeholderResId).config(Bitmap.Config.RGB_565).into(imageView);
	}

	public static void imageShowFragment(Context mCon, String url, ImageView imageView){
		imageView.setImageResource(R.mipmap.placeholder);
		if (TextUtils.isEmpty(url)){
			return;
		}
		Picasso.with(mCon).load(url).placeholder(imageView.getDrawable()).config(Bitmap.Config.RGB_565).noFade().into(imageView);
	}

	public static void imageShowRound(Context mCon, String url, int placeholderResId, ImageView imageView){
		if (TextUtils.isEmpty(url)){
			return;
		}
		Picasso.with(mCon).load(url).placeholder(placeholderResId).config(Bitmap.Config.ALPHA_8).transform(new CircleImageTransformation()).into(imageView);
	}

	public static void imageShowRoundRect(Context mCon, String url, int placeholderResId, ImageView imageView){
		if (TextUtils.isEmpty(url)){
			return;
		}
		Picasso.with(mCon).load(url).placeholder(placeholderResId).config(Bitmap.Config.ALPHA_8).transform(new RoundRectImageTransformation()).into(imageView);
	}

	public static void imageShowLocal(Context mCon, String localPath, ImageView imageView){
		int sizeW = (int) dp2px(mCon,100);
		Picasso.with(mCon).load("file://" + localPath).resize(10, 10).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).config(Bitmap.Config.RGB_565).centerCrop().into(imageView);
	}


	public static int orderStatusNum(String status){
		if ("unpaid".equals(status)){
			return 1;
		} else if ("paid".equals(status)) {
			return 2;
		} else if ("auditing".equals(status)) {
			return 8;
		} else if ("refunding".equals(status)) {
			return 6;
		} else if ("refund".equals(status)) {
			return 7;
		} else if ("pending".equals(status)) {
			return 2;
		} else if ("shipped".equals(status)) {
			return 3;
		} else if ("received".equals(status)) {
			return 5;
		} else if ("paying".equals(status)) {
			return 9;
		} else if ("canceled".equals(status)) {
			return 4;
		} else if ("overtime_unpaid".equals(status)) {
			return 4;
		} else if ("overtime_non".equals(status)) {
			return 4;
		} else if ("overtime".equals(status)) {
			return 4;
		} else if ("timeout".equals(status)) {
			return 5;
		}
		return 0;
	}

	public static String payStatus(String status){
		if ("unpaid".equals(status)){
			return "待支付";
		} else if ("overtime_unpaid".equals(status)) {
			return "超时未支付";
		} else if ("paying".equals(status)) {
			return "支付中";
		} else if ("paid".equals(status)) {
			return "已支付";
		} else if ("auditing".equals(status)) {
			return "审核中";
		} else if ("canceled".equals(status)) {
			return "已取消";
		} else if ("refunding".equals(status)) {
			return "退款中";
		} else if ("refund".equals(status)) {
			return "退款完成";
		}

		return status;
	}

	public static String expressStatus(String status){
		if ("non".equals(status)){
			return "等待付款";
		} else if ("overtime".equals(status)) {
			return "超时不发货";
		} else if ("pending".equals(status)) {
			return "待发货";
		} else if ("shipped".equals(status)) {
			return "待收货";
		} else if ("received".equals(status)) {
			return "已收货";
		} else if ("timeout".equals(status)) {
			return "超时自动收货";
		} else if ("canceled".equals(status)) {
			return "已取消";
		} else if ("refunding".equals(status)) {
			return "申请退货";
		}else if ("refund".equals(status)) {
			return "退货完成";
		}

		return status;
	}


	/**
	 * 按尺寸压缩图片
	 *
	 * @param srcPath  图片路径
	 * @param desWidth 压缩的图片宽度
	 * @return Bitmap 对象
	 */

	public static Bitmap compressImageFromFile(String srcPath, float desWidth) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;//只读边,不读内容x
		BitmapFactory.decodeFile(srcPath, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float desHeight = desWidth * h / w;
		int be = 1;
		if (w > h && w > desWidth) {
			be = (int) (newOpts.outWidth / desWidth);
		} else if (w < h && h > desHeight) {
			be = (int) (newOpts.outHeight / desHeight);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置采样率

//        newOpts.inPreferredConfig = Config.ARGB_8888;//该模式是默认的,可不设
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收

		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return bitmap;
	}

	/**
	 * 压缩图片（质量压缩）
	 *
	 * @param image
	 */

	public static File compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;

		while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();//重置baos即清空baos
			options -= 10;//每次都减少10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			long length = baos.toByteArray().length;
		}
//        long length = baos.toByteArray().length;
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片


		File file = new File(Environment.getExternalStorageDirectory() + "/temp.png");
		try {
			FileOutputStream fos = new FileOutputStream(file);
			try {
				fos.write(baos.toByteArray());
				fos.flush();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return file;
	}

	/**
	 * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
	 */
	@SuppressWarnings("NewApi")
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
									   String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	public static boolean checkPass(String value){

		String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";

		return value.matches(regex);
	}

	// 本地xml保存的文件名称
	public static String getLocalSpName(){
		return "AppLocalSpName";
	}

	public static String formatMoney(int payMoney) {
		return String.format("￥%.2f",payMoney / 100.0);
	}

	public static float dp2px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return dp * scale + 0.5f;
	}

	public static float sp2px(Context context, float sp) {
		final float scale = context.getResources().getDisplayMetrics().scaledDensity;
		return sp * scale;
	}
}
