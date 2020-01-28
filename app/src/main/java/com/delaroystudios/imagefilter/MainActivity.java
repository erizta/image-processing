package com.delaroystudios.imagefilter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;


public class MainActivity extends Activity {

    private ImageView imgMain ;
    private static final int SELECT_PHOTO = 100;
    private Bitmap src;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgMain = (ImageView) findViewById(R.id.effect_main);
        src = BitmapFactory.decodeResource(getResources(), R.drawable.image);
    }

    public void buttonClicked(View v){

        Toast.makeText(this,"Processing...",Toast.LENGTH_SHORT).show();
        ImageFilters imgFilter = new ImageFilters();
        if(v.getId() == R.id.btn_pick_img){
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        }
        else if(v.getId() == R.id.effect_brightness)
            saveBitmap(imgFilter.applyBrightnessEffect(src, 80),"effect_brightness");
        else if(v.getId() == R.id.effect_contrast)
            saveBitmap(imgFilter.applyContrastEffect(src, 70),"effect_contrast");
        else if(v.getId() == R.id.effect_grayscale)
            saveBitmap(imgFilter.applyGreyscaleEffect(src),"effect_grayscale");
        else if(v.getId() == R.id.effect_biner)
            saveBitmap(imgFilter.applyBinerEffect(src),"effect_biner");
        else if(v.getId() == R.id.effect_invert)
            saveBitmap(imgFilter.applyInvertEffect(src),"effect_invert");

    }

    private void saveBitmap(Bitmap bmp,String fileName){
        try {
            imgMain.setImageBitmap(bmp);
            File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName+".png");
            FileOutputStream fos = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG,90,fos);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    Bitmap bmp = decodeUri(selectedImage);
                    if(bmp !=null){
                        src = bmp;
                        imgMain.setImageBitmap(src);
                    }
                }
        }
    }

    private Bitmap decodeUri(Uri selectedImage)  {

        try {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 400;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
