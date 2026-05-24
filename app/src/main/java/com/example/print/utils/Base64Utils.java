package com.example.print.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import java.io.ByteArrayOutputStream;

public class Base64Utils {

    /**
     * Resizes and compresses the bitmap, then encodes it to a Base64 string.
     * Resizing is crucial for Realtime Database performance and storage limits.
     */
    public static String encode(Bitmap bitmap) {
        if (bitmap == null) return "";

        // Senior practice: Resize image if it's too large
        int maxSize = 800;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // Compress to JPEG with 70% quality
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
        byte[] byteArray = outputStream.toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap decode(String base64String) {
        if (base64String == null || base64String.isEmpty()) return null;
        try {
            byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            return null;
        }
    }
}
