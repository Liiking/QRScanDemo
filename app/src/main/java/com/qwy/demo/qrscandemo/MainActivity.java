package com.qwy.demo.qrscandemo;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.qwy.demo.qrscandemo.constant.Constant;
import com.qwy.demo.qrscandemo.entity.IDCardEntity;
import com.qwy.demo.qrscandemo.entity.IdCardInfo;
import com.qwy.demo.qrscandemo.network.HttpCallback;
import com.qwy.demo.qrscandemo.network.HttpManager;
import com.qwy.demo.qrscandemo.sign.YoutuSign;
import com.qwy.demo.qrscandemo.util.BitMapUtils;
import com.qwy.demo.qrscandemo.util.Utility;
import com.qwy.demo.qrscandemo.view.ShadowTextView;
import java.io.File;
import java.util.ArrayList;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_result, tv_scan, tv_scan_idcard;
    private final static int REQUEST_CODE_SCAN_QRCODE = 100, REQUEST_CODE_SCAN_IDCARD = 101;
    private final static int REQUEST_CODE_CAMERA = 1000;
    private int type = -1;
    private ImageView imageView, iv_src;
    private AlertDialog dialogInfo;// 识别身份证信息弹窗
    private ShadowTextView stv;
    private int scanBy = 1;// 提供两种API识别身份证， 0 - 优图，1 - face++

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stv = (ShadowTextView) findViewById(R.id.stv);
        iv_src = (ImageView) findViewById(R.id.iv_src);
        iv_src.setOnClickListener(this);
        TextView tv_anim = (TextView) findViewById(R.id.tv_anim);
        tv_anim.setOnClickListener(this);
        TextView tv_check_idcard = (TextView) findViewById(R.id.tv_check_idcard);
        tv_check_idcard.setOnClickListener(this);
        TextView tv_share = (TextView) findViewById(R.id.tv_share);
        tv_share.setOnClickListener(this);
        TextView tv_share1 = (TextView) findViewById(R.id.tv_share1);
        tv_share1.setOnClickListener(this);
        TextView tv_share2 = (TextView) findViewById(R.id.tv_share2);
        tv_share2.setOnClickListener(this);
        TextView tv_draw_layout = (TextView) findViewById(R.id.tv_draw_layout);
        tv_draw_layout.setOnClickListener(this);
        TextView tv_list = (TextView) findViewById(R.id.tv_list);
        tv_list.setOnClickListener(this);
        imageView = (ImageView) findViewById(R.id.imageView);
        tv_result = (TextView) findViewById(R.id.tv_result);
        tv_scan = (TextView) findViewById(R.id.tv_scan);
        tv_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scan();
            }
        });
        tv_scan_idcard = (TextView) findViewById(R.id.tv_scan_idcard);
        tv_scan_idcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanIDCard();
//                scanIdCard();
            }
        });
    }

    /**
     * 调用face++ API识别本地身份证图片
     */
    private void scanIdCard() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.idcard);
        uploadIdCardByFacePP(BitMapUtils.bitmapToBase64(bitmap));
    }

    /**
     * 扫描身份证
     */
    private void scanIDCard() {
        type = 0;
        try {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // 获取相机权限
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
            } else {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, IDCardScanActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, REQUEST_CODE_SCAN_IDCARD);
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "获取相机权限失败", Toast.LENGTH_SHORT).show();
        }
    }

    public void next(View v) {
        startActivity(new Intent(this, TestShadowActivity.class));
    }

    /**
     * 扫描二维码
     */
    private void scan() {
        type = 1;
        try {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // 获取相机权限
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
            } else {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, QRScanActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, REQUEST_CODE_SCAN_QRCODE);
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "获取相机权限失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                if (type == 0) {
                    intent.setClass(MainActivity.this, IDCardScanActivity.class);
                } else {
                    intent.setClass(MainActivity.this, QRScanActivity.class);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, REQUEST_CODE_SCAN_QRCODE);
            } else {
                // Permission Denied
                Toast.makeText(MainActivity.this, "获取相机权限失败", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SCAN_QRCODE:// 扫描二维码成功
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String result = bundle.getString("result");
                    tv_result.setText("扫描结果: " + result);
                    if (!TextUtils.isEmpty(result) && result.startsWith("http://")) {
                        // 扫描到地址，在浏览器打开
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(result));
                        startActivity(intent);
                    }
                }
                break;
            case REQUEST_CODE_SCAN_IDCARD:// 扫描身份证成功
                if (resultCode == RESULT_OK && data != null) {
//                    Bitmap bitmap = data.getParcelableExtra("bitmap");
                    if (MyApplication.globalBitmap != null) {
                        imageView.setImageBitmap(MyApplication.globalBitmap);
                        if (scanBy == 0) {
                            uploadIdCardByYouTu(BitMapUtils.bitmapToBase64(MyApplication.globalBitmap));
                        } else {
                            uploadIdCardByFacePP(BitMapUtils.bitmapToBase64(MyApplication.globalBitmap));
                        }
                    }
                }
                break;
        }
    }

    /**
     * 查询身份证信息（face++ API）
     *
     * @param bitmap
     */
    public void uploadIdCardByFacePP(String bitmap) {
        RequestParams params = new RequestParams();
        params.addFormDataPart("image_base64", bitmap);
        params.addFormDataPart("api_key", Constant.FACEPLUS_ID);
        params.addFormDataPart("api_secret", Constant.FACEPLUS_SECRET);
        HttpManager.getInstance().post1(MainActivity.this, Constant.FACEPLUS_API, params, new HttpCallback<String>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onResponse(int code, Headers headers, String response) {
                if (!TextUtils.isEmpty(response) && response.startsWith("{")) {
                    IdCardInfo info = new Gson().fromJson(response, IdCardInfo.class);
                    if (info != null) {
                        Utility.shortToast(MainActivity.this, "成功 ");
                        if (info.getCards() != null && info.getCards().size() > 0) {
                            showDialogInfo(info.getCards().get(0));
                        }
                    }
                }
            }

            @Override
            public void onFinish() {

            }
        });
    }

    /**
     * 查询身份证信息（腾讯优图API） 貌似已过期
     *
     * @param bitmap
     */
    public void uploadIdCardByYouTu(String bitmap) {
        StringBuffer mySign = new StringBuffer("");
        YoutuSign.appSign(Constant.AppID, Constant.SecretID, Constant.SecretKey,
                System.currentTimeMillis() / 1000 + Constant.EXPIRED_SECONDS,
                Constant.QQNumber, mySign);
        String url = "http://api.youtu.qq.com/youtu/ocrapi/idcardocr";
        RequestParams params = new RequestParams();
        params.applicationJson();
        params.addHeader("accept", "*/*");
        params.addHeader("Host", "api.youtu.qq.com");
        params.addHeader("user-agent", "youtu-java-sdk");
        params.addHeader("Authorization", mySign.toString());
        params.addHeader("Content-Type", "text/json");
        params.addFormDataPart("card_type", 0);// 身份证图片类型，0-正面，1-反面
        params.addFormDataPart("app_id", Constant.AppID);
        params.addFormDataPart("image", bitmap);
        HttpManager.getInstance().post(MainActivity.this, url, params, new HttpCallback<String>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onResponse(int code, Headers headers, String response) {
                if (!TextUtils.isEmpty(response) && response.startsWith("{")) {
                    IDCardEntity info = new Gson().fromJson(response, IDCardEntity.class);
                    if (info != null) {
                        Utility.shortToast(MainActivity.this, "成功 " + info.getId());
                        showDialogInfo(info);
                    }
                }
            }

            @Override
            public void onFinish() {

            }
        });
    }

    /**
     * 显示对话框
     *
     * @param info
     */
    private void showDialogInfo(final IdCardInfo.CardsInfo info) {
        StringBuilder sb = new StringBuilder();
        sb.append("姓名：" + info.getName() + "\n");
        sb.append("性别：" + info.getGender() + "\t" + "民族：" + info.getRace() + "\n");
        sb.append("出生：" + info.getBirthday() + "\n");
        sb.append("住址：" + info.getAddress() + "\n" + "\n");
        sb.append("公民身份证号码：" + info.getId_card_number() + "\n");
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        dialogInfo = builder.setTitle("识别成功")
                .setMessage(sb.toString())
                .setPositiveButton("复制号码", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("text", info.getId_card_number());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(MainActivity.this, "身份证号已复制到粘贴板", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", null)
                .create();
        dialogInfo.show();
    }

    /**
     * 显示对话框
     *
     * @param info
     */
    private void showDialogInfo(final IDCardEntity info) {
        StringBuilder sb = new StringBuilder();
        sb.append("姓名：" + info.getName() + "\n");
        sb.append("性别：" + info.getSex() + "\t" + "民族：" + info.getNation() + "\n");
        sb.append("出生：" + info.getBirth() + "\n");
        sb.append("住址：" + info.getAddress() + "\n" + "\n");
        sb.append("公民身份证号码：" + info.getId() + "\n");
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        dialogInfo = builder.setTitle("识别成功")
                .setMessage(sb.toString())
                .setPositiveButton("复制号码", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("text", info.getId());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(MainActivity.this, "身份证号已复制到粘贴板", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", null)
                .create();
        dialogInfo.show();
    }

    // 分享文字
    public void shareText() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my Share text.");
        shareIntent.setType("text/plain");

        //设置分享列表的标题，并且每次都显示分享列表
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    // 分享单张图片
    public void shareSingleImage() {
        String imagePath = Environment.getExternalStorageDirectory() + File.separator + "test.jpg";
        //由文件得到uri
        Uri imageUri = Uri.fromFile(new File(imagePath));
        Log.d("share", "uri:" + imageUri);  //输出：file:///storage/emulated/0/test.jpg

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    // 分享多张图片
    public void shareMultipleImage() {
        ArrayList uriList = new ArrayList<>();

        String path = Environment.getExternalStorageDirectory() + File.separator;
        uriList.add(Uri.fromFile(new File(path + "australia_1.jpg")));
        uriList.add(Uri.fromFile(new File(path + "australia_2.jpg")));
        uriList.add(Uri.fromFile(new File(path + "australia_3.jpg")));

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    /**
     * 共享元素
     */
    private void transition() {
        Intent intent = new Intent(MainActivity.this, DrawLayoutActivity.class);
        if (android.os.Build.VERSION.SDK_INT > 20) {
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    MainActivity.this, new Pair<View, String>(iv_src, "trans"));
            startActivity(intent, activityOptions.toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_anim:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (iv_src.getVisibility() == View.VISIBLE) {
                        hideImageCircular(iv_src);
                    } else {
                        revealImageCircular(iv_src);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "当前手机不支持", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_check_idcard:
                scanIdCard();
                break;
            case R.id.tv_share:
                shareText();
                break;
            case R.id.iv_src:
                transition();
                break;
            case R.id.tv_share1:
                shareSingleImage();
                break;
            case R.id.tv_share2:
                shareMultipleImage();
                break;
            case R.id.tv_draw_layout:
                startActivity(new Intent(getApplicationContext(), DrawLayoutActivity.class));
                break;
            case R.id.tv_list:
                startActivity(new Intent(getApplicationContext(), ListOprateActivity.class));
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void hideImageCircular(final View view) {
        int x = getX(view);
        int y = getY(view);
        int radius = getRadius(view);

        Animator anim = ViewAnimationUtils.createCircularReveal(view, x, y, radius, 0);

        anim.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });

        anim.start();
    }

    private int getY(final View view) {
        return (view.getTop() + view.getBottom()) / 2;
    }

    private int getX(final View view) {
        return (view.getRight() + view.getLeft()) / 2;
    }

    private int getRadius(final View view) {
        return (int) (view.getWidth() * Math.sqrt(2) / 2);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void revealImageCircular(final View view) {
        int x = getX(view);
        int y = getY(view);
        int radius = getRadius(view);

        Animator anim = ViewAnimationUtils.createCircularReveal(view, x, y, 0, radius);

        anim.setDuration(1000);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                view.setVisibility(View.VISIBLE);
            }
        });

        anim.start();
    }

}
