package rumstajn.parfem.parfem.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageFileUtils {
    @SuppressLint("SimpleDateFormat")
    public static File createTempFile(MainActivity mainActivity) throws IOException {
        File imagesDir = mainActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        return File.createTempFile(timeStamp, ".jpg", imagesDir);
    }

    public static void addImageToGallery(String imagePath, MainActivity mainActivity) {
        if (imagePath == null){
            return;
        }
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(imagePath);
        if (file.exists()){
            Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            mainActivity.sendBroadcast(mediaScanIntent);
        }
    }

    public static boolean isNonEmptyImageFile(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        return options.outWidth != -1 && options.outHeight != -1;
    }
}
