package com.example.myapplication.global;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;

import com.example.myapplication.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

public class CaptureImage {
    private Context context;
    private String imagePath = "";

    public CaptureImage(Context context) {
        this.context = context;
    }

    public Uri setImageUri() {
        Uri imgUri;
        String state = Environment.getExternalStorageState();
        File file;

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            file = new File(Environment.getExternalStorageDirectory() + "/DCIM/",
                    context.getString(R.string.app_name) + Calendar.getInstance().getTimeInMillis() + ".png");
        } else {
            file = new File(context.getFilesDir(),
                    context.getString(R.string.app_name) + Calendar.getInstance().getTimeInMillis() + ".png");
        }

        imgUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
        imagePath = file.getAbsolutePath();

        return imgUri;
    }

    public String getCurrentImagePath() {
        return imagePath;
    }

    public String getRightAngleImage(String photoPath) {
        try {
            ExifInterface ei = new ExifInterface(photoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int degree = getRotationDegree(orientation);
            return rotateImage(degree, photoPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return photoPath;
    }

    private int getRotationDegree(int orientation) {
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
            default:
                return 0;
        }
    }

    private String rotateImage(int degree, String imagePath) {
        if (degree <= 0) return imagePath;

        try {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            Matrix matrix = new Matrix();
            matrix.setRotate(degree);

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            FileOutputStream out = new FileOutputStream(imagePath);

            String imageType = imagePath.substring(imagePath.lastIndexOf(".") + 1);
            if ("png".equalsIgnoreCase(imageType)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            }

            out.flush();
            out.close();
            bitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imagePath;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, String.valueOf(System.currentTimeMillis()), null);
            return Uri.parse(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getRealPathFromURI(Uri uri, Context mContext) {
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
        String path = "";

        if (cursor != null) {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            path = cursor.getString(idx);
            cursor.close();
        }
        return path;
    }

    public Bitmap decodeFile(String path) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);

            final int REQUIRED_SIZE = 1024;
            int scale = 1;
            while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPath(final Uri uri, final Context context) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
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
                final String[] selectionArgs = {split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }
}
