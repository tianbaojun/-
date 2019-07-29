package com.tabjin.pullrefresh;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Looper;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by xuxiao on 2018/5/21.
 * 图片加载工具
 */

public class GlideUtil {

    /**
     * glide最简洁的使用方式,网络加载,
     */
    public static void setImageView(final Context context, final String url, final ImageView view) {
//        Kits.L.e("GlideUtil",url);
        if(!checkContext(context)) return;
        try {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.img_placeholder)
                    .error(R.mipmap.img_placeholder)
                    .fallback(R.mipmap.img_placeholder);
            DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(100).setCrossFadeEnabled(true).build();
            Glide.with(context).load(url).transition(DrawableTransitionOptions.with(drawableCrossFadeFactory)).apply(options).into(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void setImageView(final Context context, final String url, final ImageView view,int width, int height ) {
        if(!checkContext(context)) return;
        try {
            Glide.with(context).load(url).transition(DrawableTransitionOptions.withCrossFade()).apply(new RequestOptions().override(width, height)).into(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * glide最简洁的使用方式,网络加载,
     */
    public static void setImageView(final Context context, final int id, final ImageView view) {
        if(!checkContext(context)) return;
        try {
            Glide.with(context).load(id).transition(DrawableTransitionOptions.withCrossFade()).into(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * glide最简洁的使用方式,网络加载,
     */
   /* public static void setNetImageView(final Context context, final String url, final ImageView view) {
        try {
            if (url.contains("http://")) {
                Glide.with(context).load(url).transition(DrawableTransitionOptions.withCrossFade()).apply(goodsRequestOptions).into(view);
            } else {
                Glide.with(context).load("http://" + url).transition(DrawableTransitionOptions.withCrossFade()).apply(goodsRequestOptions).into(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /**
     * glide最简洁的使用方式,网络加载,
     */
    public static void setImageView(final Context context, final Uri url, final ImageView view) {
        if(!checkContext(context)) return;
        try {
            Glide.with(context).load(url).transition(DrawableTransitionOptions.withCrossFade()).into(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * glide最简洁的使用方式,网络加载,底图加载
     */
    public static void setImageView(final Context context, final String url, int place, final ImageView view, RequestOptions requestOptions) {
        if(!checkContext(context)) return;
        try {
            Glide.with(context).load(url).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).into(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * glide最简洁的使用方式,网络加载,
     */
    public static void setDialogImageView(final Context context, final int id, final ImageView view) {
        if(!checkContext(context)) return;
        try {
            Glide.with(context).load(id).transition(DrawableTransitionOptions.withCrossFade()).into(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * glide最简洁的使用方式,网络加载
     */
    public static void setImageView(final Context context, final String url, final ImageView view, RequestOptions requestOptions) {
        if(!checkContext(context)) return;
        try {
            Glide.with(context).load(url).transition(DrawableTransitionOptions.withCrossFade()).apply(requestOptions).into(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片磁盘缓存
     */
    public static void clearImageDiskCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取Glide造成的缓存大小
     *
     * @return CacheSize
     */
    public static String getCacheSize(Context context) {
        try {
            return getFormatSize(getFolderSize(new File(context.getCacheDir() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0 KB";
    }

    /**
     * 删除指定目录下的文件，这里用于缓存的删除
     *
     * @param filePath       filePath
     * @param deleteThisPath deleteThisPath
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (File file1 : files) {
                        deleteFolderFile(file1.getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {
                        file.delete();
                    } else {
                        if (file.listFiles().length == 0) {
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }


    /**
     * 格式化单位
     *
     * @param size size
     * @return size
     */
    public static String getFormatSize(double size) {

        double kiloByte = size / 1024;
//        if (kiloByte < 1) {
//            return size + "Byte";
//        }
        if (kiloByte < 1) {
            return "0 KB";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }


    /**
     * 加载头像
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadAvter(final Context context, String url, final ImageView imageView){
//        loadRoundImage(context,Kits.Dimens.dpToPx(context,5),url,R.mipmap.icon_header_conner,imageView);
    }

    /**
     * 加载圆形图片
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadRoundImage(final Context context,String url,final ImageView imageView){
        if(!checkContext(context)) return;
        if(!TextUtils.isEmpty(url)&&url.startsWith("http")) {
            try {
                Glide.with(context).asBitmap().load(url).into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }/**
     * 加载圆形图片
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadRoundImage(final Context context,int url,final ImageView imageView){
        if(!checkContext(context)) return;
            try {
                Glide.with(context).asBitmap().load(url).into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    /**
     * 加载圆弧角度
     * @param context
     * @param cornerRadius
     * @param url
     * @param resId
     * @param imageView
     */
    public static void loadRoundImage(final Context context, final int cornerRadius, String url,int resId,final ImageView imageView){
        RequestOptions options = new RequestOptions()
                .placeholder(resId)
                .error(resId)
                .fallback(resId).diskCacheStrategy(DiskCacheStrategy.ALL);
        //如果页面已经销毁，不请求
        if(context instanceof Activity){
            if(((Activity) context).isFinishing()){
                return;
            }
        }
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(options)
                .into(new BitmapImageViewTarget(imageView){
                    @Override
                    protected void setResource(Bitmap resource) {
                        super.setResource(resource);
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCornerRadius(cornerRadius); //设置圆角弧度
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    private static boolean checkContext(Context context){
        if(context  == null){
            return false;
        }else if(context instanceof Activity&&((Activity) context).isFinishing()){
            return false;
        }else{
            return true;
        }
    }

    public static void loadGif(Context context,String url,ImageView imageView){
        RequestOptions options = new RequestOptions();
        options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(context).asGif().load(url).apply(options).into(imageView);
    }
    public static void loadGif(Context context,int resouceId,ImageView imageView){
        RequestOptions options = new RequestOptions();
        options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(context).asGif().load(resouceId).apply(options).into(imageView);
    }


}
