package com.qwy.demo.qrscandemo.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import cn.finalteam.okhttpfinal.RequestParams;

public class Utility {
    public static boolean debug = true;// 是否是Debug模式
    private static int versionCode;
    private static String versionName;

    /**
     * SD卡的路径*
     */
    private static String SDPATH = Environment.getExternalStorageDirectory().getPath();

    /**
     *获取sd卡主文件路径
     */
    public static String getMainPath() {
        File f = new File(SDPATH + "/demo/");
        if (!f.exists()) {
            f.mkdir();
        }
        return f.getAbsolutePath();
    }

    public static String getPostImgPath() {
        File f = new File(getMainPath() + "/demo_post_img/");
        if (!f.exists()) {
            f.mkdir();
        }
        return f.getAbsolutePath();
    }

    public static String getImgCachePath() {
        File f = new File(getMainPath() + "/demo_cache_img/");
        if (!f.exists()) {
            f.mkdir();
        }
        return f.getAbsolutePath();
    }

    public static void log(String msg){
        if(debug){
            Log.i("log", msg);
        }
    }

    public static void shortToast(Context context, String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    public static void shortToast(Context context,int msgid){
        Toast.makeText(context, msgid, Toast.LENGTH_SHORT).show();
    }

    public static RequestParams getRequestParams() {
        RequestParams params = new RequestParams();
        params.addHeader("domain_type", 4);
        params.addHeader("app_version", "1.1");
        return params;
    }

    public static int dp2px(Context context,float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static long getAvailMemory(Context context)
    {
        // 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存

        //return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
        return mi.availMem / (1024 * 1024);
    }
    public static void clearMemory(Context context) {
        ActivityManager activityManger = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = activityManger.getRunningAppProcesses();
        if (list != null)
            for (int i = 0; i < list.size(); i++) {
                ActivityManager.RunningAppProcessInfo apinfo = list.get(i);
//
//                System.out.println("pid            " + apinfo.pid);
//                System.out.println("processName              " + apinfo.processName);
//                System.out.println("importance            " + apinfo.importance);
                String[] pkgList = apinfo.pkgList;

                if (apinfo.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE) {
                    // Process.killProcess(apinfo.pid);
                    for (int j = 0; j < pkgList.length; j++) {
                        //2.2以上是过时的,请用killBackgroundProcesses代替
                        activityManger.killBackgroundProcesses(pkgList[j]);
                    }
                }
            }
    }

    /**
     * 获取系统版本名称
     * @return
     */
    public static  String getSystemVersionName(){
        return android.os.Build.VERSION.RELEASE;
    }
    
    /**
     * 获取系统版本号
     * @return
     */
    public static int getSystemVersionCode(){
        
        return android.os.Build.VERSION.SDK_INT;
    }
    
    /**
     * 获取应用版本号
     * @return
     */
    public static int getVersionCode() {
        return versionCode;
    }

    public static void loadAppVersion(Context context) {
        if(versionName == null || versionCode <= 0){
            //获取软件版本号
             PackageInfo info;
            try {
                info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                versionCode = info.versionCode;
                versionName = info.versionName;
                info = null;
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取应用版本名
     * @return
     */
    public static String getVersionName() {
        return versionName;
    }

    /**
     * 获取状态栏的高度
     * @param context
     * @return
     */
    private static int getStatusHeight(Context context){
        int statusHeight = 0;
        Rect localRect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight){
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    /**
     * 是否联网
     * @param context
     * @return
     */
    public static boolean hasNetwork(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo == null) {
            return false;
        } else {
            return true;
        }
    }
    /**
     * 是否已安装指定APP功能
     * @param context
     * @param packageName
     * @return
     */
    public static boolean hasApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        List<PackageInfo> pkgList = manager.getInstalledPackages(0);
        for (int i = 0; i < pkgList.size(); i++) {
            PackageInfo pI = pkgList.get(i);
            if (pI.packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }
    
    /**
     * 获取Application中的<meta-data>元素值
     * @param context
     * @param name
     * @return
     */
    public static String getMetaData(Context context,String name){
        ApplicationInfo appInfo;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA);
            return appInfo.metaData.get(name).toString();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取服务提供商名字
     * @param context
     * @return
     */
    public static String getSPN(Context context){
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimOperatorName(); // Returns the Service Provider Name (SPN).服务提供商名称
//        return tm.getSimOperator(); // Returns the Service Provider code (SPN).代号
    }

    /**  
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)  
     */   
    public static int dip2px(Context context, float dpValue) {   
        final float scale = context.getResources().getDisplayMetrics().density;   
        return (int) (dpValue * scale + 0.5f);   
    }  

    // 关闭软键盘
 	public static void closeKeyboard(Activity context, EditText editText) {
 		try {
 			InputMethodManager imm = (InputMethodManager) context
 					.getSystemService(context.INPUT_METHOD_SERVICE);
 			// imm.hideSoftInputFromWindow(context.getCurrentFocus().getApplicationWindowToken(),
 			// InputMethodManager.HIDE_NOT_ALWAYS);
 			if (editText != null) {
 				if (imm.hideSoftInputFromWindow(editText.getWindowToken(), 0)) {
 					imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
 				}
 			}
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 	}

 	public static void openKeybord(Activity context, EditText editText) {
 		InputMethodManager imm = (InputMethodManager) context
 				.getSystemService(Context.INPUT_METHOD_SERVICE);
 		// 得到InputMethodManager的实例
 		if (imm.isActive()) {
 			imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
 		}

 	}
 	
 	
 	public static void installApp(Context context, String path) {
		File file = new File(path);
		if(file.exists()){
			// 调用系统打开apk文件.
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.fromFile(file), android.webkit.MimeTypeMap.getSingleton()
			        .getMimeTypeFromExtension(android.webkit.MimeTypeMap.getFileExtensionFromUrl(file.getPath())));
			context.startActivity(intent);
		}else{
			Toast.makeText(context, "安装失败，文件已被移除", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 打开指定应用（微信）
	 */
	public static boolean openWX(Context activity){
		try {
			Intent intent = new Intent();
			ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
			intent.setAction(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setComponent(cmp);
			// 跳转到微信
			activity.startActivity(intent);
			return true;
		} catch (Exception e) {
			// 没有安装微信客户端
			return false;
		}
	}
	
    /**
     * 获取SDCARD剩余存储空间
     * 
     * @return
     */
    public static long getAvailableExternalMemorySize() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return -1;
        }
    }
    
    /**
     * 获取手机内部剩余存储空间
     * 
     * @return
     */
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

	
	// 根据经纬度计算两点间距离
	private static final  double EARTH_RADIUS = 6378137;//赤道半径(单位m)
		
    /**
     * 转化为弧度(rad)
     * */
    private static double rad(double d)
    {
       return d * Math.PI / 180.0;
    }
    /**
     * 基于googleMap中的算法得到两经纬度之间的距离,计算精度与谷歌地图的距离精度差不多，相差范围在0.2米以下
     * @param lon1 第一点的精度
     * @param lat1 第一点的纬度
     * @param lon2 第二点的精度
     * @param lat2 第二点的纬度
     * @return 返回的距离，单位km
     * */
    public static double GetDistance(double lon1,double lat1,double lon2, double lat2) {
       double radLat1 = rad(lat1);
       double radLat2 = rad(lat2);
       double a = radLat1 - radLat2;
       double b = rad(lon1) - rad(lon2);
       double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2)+Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
       s = s * EARTH_RADIUS;
       s = Math.round(s * 10000) / 10000;
       return s;
    }


    /**
     * 这是一个压缩图片大小的方法
     * 传入参数为图片路径
     */
    public static Bitmap compressImageSize(String imagepath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 设置只获取图片
        options.inJustDecodeBounds = true;
        // 得到图片
        BitmapFactory.decodeFile(imagepath, options);
        // 得到合理的比率
        double ratio = Math.max(options.outWidth * 1.0d / 1024f,
                options.outHeight * 1.0d / 1024f);
        // 把比率设置给options
        options.inSampleSize = (int) Math.ceil(ratio);
        // 设置加载图片
        options.inJustDecodeBounds = false;
        // 内存不足时可被回收
        options.inPurgeable = true;
        // 得到了图片的位图
        Bitmap  imagebitmap = BitmapFactory.decodeFile(imagepath, options);
        return imagebitmap;
    }

    /*
     * 方法名称：getMD5 功 能：字符串MD5加密 参 数：待转换字符串 返 回 值：加密之后字符串
     */
    public static String getMD5(String sourceStr)  {
        String resultStr = "";
        try {
            byte[] temp = sourceStr.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(temp);
            // resultStr = new String(md5.digest());
            byte[] b = md5.digest();
            for (int i = 0; i < b.length; i++) {
                char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                        '9', 'A', 'B', 'C', 'D', 'E', 'F' };
                char[] ob = new char[2];
                ob[0] = digit[(b[i] >>> 4) & 0X0F];
                ob[1] = digit[b[i] & 0X0F];
                resultStr += new String(ob);
            }
            return resultStr;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
	 * md5加密
	 * */
//    public static String md5(String string) {
//        if(TextUtils.isEmpty(string)){
//            return "";
//        }
//        byte[] hash;
//        try {
//            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("Huh, MD5 should be supported?", e);
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
//        }
//
//        StringBuilder hex = new StringBuilder(hash.length * 2);
//        for (byte b : hash) {
//            if ((b & 0xFF) < 0x10) hex.append("0");
//            hex.append(Integer.toHexString(b & 0xFF));
//        }
//        return hex.toString();
//    }

	/**
	 * 是否是电话号码
	 * */
    public static boolean isPhoneNumber(String number) {
        String str = "^1[34578]\\d{9}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(number);

        return m.matches();
    }

    /**
     * 显示分享面板
     * @param activity
     * @param shareData
     * @param listener
     */
 /*   public static void showShare(Activity activity, ShareData shareData, UMShareListener listener){
        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
                {
                        SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA
//                        , SHARE_MEDIA.QQ
                };
        UMImage image = null;
        if(!TextUtils.isEmpty(shareData.getImage_url())) {
            image = new UMImage(activity, shareData.getImage_url());
        }else {
            image = new UMImage(activity, FileUtil.getInputStreamFromDrawable(activity, R.mipmap.icon));
        }
        if(TextUtils.isEmpty(shareData.getTitle())){
            shareData.setTitle(activity.getResources().getString(R.string.item_text_share_title));
        }
        if(TextUtils.isEmpty(shareData.getContent())){
            shareData.setContent(activity.getResources().getString(R.string.item_text_share_content));
        }
        if(TextUtils.isEmpty(shareData.getLink_url())){
            shareData.setLink_url(activity.getResources().getString(R.string.item_text_share_link));
        }
        new ShareAction(activity).setDisplayList(displaylist)
                .withText(shareData.getContent())
                .withTitle(shareData.getTitle())
                .withTargetUrl(shareData.getLink_url())
                .withMedia(image)
                .setListenerList(listener)
                .open();
    }*/

    /**
     * 根据时间转换成特定格式,用于首页卡片
     * @param time 秒
     * @return
     */
    public static String getSmartTime(long time){
        long delt = System.currentTimeMillis() / 1000 - time;
        if(delt < 0){
            return "刚刚";
        }else if(delt < 5){
            return "刚刚";
        }else if(delt < 60){
            return delt + "秒前";
        }else if(delt < 1 * 60 * 60){
            return (delt / 60) + "分钟前";
        }else if(delt < 24 * 60 * 60){
            return (delt / 3600) + "小时前";
        }else if(delt <= 24 * 60 * 60 * 7){
            return (delt / (3600 * 24)) + "天前";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(new Date(time * 1000));
    }

    /**
     * 根据秒数格式化成指定类型的日期
     * @param time 秒
     * @return 返回指定时间戳对应年月日
     */
    public static String getDate(long time, String format){
        if(TextUtils.isEmpty(format)){
            format = "yyyy/MM/dd";
        }
        if(time < 0){
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time * 1000));
    }

    /**
     * 根据秒数格式化成指定类型的年
     * @param time 秒
     * @return 返回指定时间戳对应年
     */
    public static String getYear(long time){
        String format = "yyyy";
        if(time < 0){
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time * 1000));
    }

    /**
     * 根据秒数格式化成指定类型的月
     * @param time 秒
     * @return 返回指定时间戳对应月份
     */
    public static String getMonth(long time){
        String format = "MM";
        if(time < 0){
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time * 1000));
    }

    /**
     * 根据秒数格式化成指定类型的月
     * @param time 秒
     * @return 返回指定时间戳对应月份,个位数不补0
     */
    public static String getSingleMonth(long time){
        String format = "M";
        if(time < 0){
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time * 1000));
    }

    /**
     * 根据秒数格式化成指定类型的日
     * @param time 秒
     * @return 返回指定时间戳对应日
     */
    public static String getDay(long time){
        String format = "dd";
        if(time < 0){
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time * 1000));
    }

    /**
     * 根据秒数格式化成指定类型的日
     * @param time 秒
     * @return 返回指定时间戳对应日,个位数不补0
     */
    public static String getSingleDay(long time){
        String format = "d";
        if(time < 0){
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time * 1000));
    }

    /**
     * 根据秒数格式化成时间
     * @param time 秒
     * @return 返回指定时间戳对应时分
     */
    public static String getTime(long time){
        if(time < 0){
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(new Date(time * 1000));
    }

    /**
     * 获取安装的应用商店
     */
    public static ArrayList<String> getInstalledMarketPkgs(Context context) {
        ArrayList<String> pkgs = new ArrayList<String>();
        if (context == null)
            return pkgs;
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.APP_MARKET");
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        if (infos == null || infos.size() == 0) {
            return pkgs;
        }
        int size = infos.size();
        for (int i = 0; i < size; i++) {
            String pkgName = "";
            try {
                ActivityInfo activityInfo = infos.get(i).activityInfo;
                pkgName = activityInfo.packageName;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(pkgName)) {
                pkgs.add(pkgName);
            }
        }
        return pkgs;
    }

    /**
     * 启动到app详情界面
     *
     * @param appPkg
     *            App的包名
     * @param marketPkg
     *            应用商店包名 ,如果为""则由系统弹出应用商店列表供用户选择,否则调转到目标市场的应用详情界面，某些应用商店可能会失败
     */
    public static void launchAppDetail(Context context, String appPkg, String marketPkg) {
        try {
            if (TextUtils.isEmpty(appPkg))
                return;
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg))
                intent.setPackage(marketPkg);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开默认浏览器到指定地址
     * @param context
     * @param url
     */
    public static void openExplore(Context context, String url){
        if(TextUtils.isEmpty(url)){
            return ;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(intent);
    }

    private static ExecutorService singleExecutor = null;

    /**
     * 执行单线程列队执行
     */
    public static void runOnQueue(Runnable runnable) {
        if (singleExecutor == null) {
            singleExecutor = Executors.newSingleThreadExecutor();
        }
        singleExecutor.submit(runnable);
    }

    private static NotificationManager mNotificationManager = null;
    private static Notification mNotification;
    private static int lastResult = 0;

    /**
     * 安装应用
     * @param file
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        // 执行动作
        intent.setAction(Intent.ACTION_VIEW);
        // 执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 将String转化为 资源 ID
     */
    public static int getResource(Context context, String imageName){
        Context ctx = context;
        int resId = ctx.getResources().getIdentifier(imageName, "drawable", ctx.getPackageName());
        // 如果没有在"mipmap"下找到imageName,将会返回0
        return resId;
    }
}
