package com.app.videonewsmaker.Utility;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResourcesUtil {
    private static final String TAG = "FFMPEG_LOG";

    public static void resourceToFile(Resources resources, final int resourceId, final File file) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId);

        if (file.exists()) {
            file.delete();
        }

        FileOutputStream outputStream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        outputStream.flush();
        outputStream.close();
    }

    public static void rawResourceToFile(Resources resources, final int resourceId, final File file) throws IOException {
        final InputStream inputStream = resources.openRawResource(resourceId);
        if (file.exists()) {
            file.delete();
        }
        final FileOutputStream outputStream = new FileOutputStream(file);

        try {
            final byte[] buffer = new byte[1024];
            int readSize;

            while ((readSize = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, readSize);
            }
        } catch (final IOException e) {
            Log.e(TAG, String.format("Saving raw resource failed.%s",e));
        } finally {
            inputStream.close();
            outputStream.flush();
            outputStream.close();
        }
    }
}
