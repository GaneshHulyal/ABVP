package com.magicstar.abvp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class AfterReg extends AppCompatActivity  implements View.OnClickListener{

    private static final int SELECT_PICTURE = 0;
    private int STORAGE_PERMISSION_CODE = 1;
    private ImageView imageView;
    FrameLayout rootContent;
    private Button customPageScreenshot;
    TextView t1;
    private TextView hiddenText;
    enum ScreenshotType{
        CUSTOM
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_reg);

        rootContent = (FrameLayout) findViewById(R.id.root);

        Intent intent = getIntent();
        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");
        ImageView img=findViewById(R.id.icon);
        img.setImageBitmap(bitmap);
        Bundle extras=getIntent().getExtras();

        if(extras != null){
            String name=extras.getString("name");
            String city=extras.getString("city");
            String blood=extras.getString("blood");
            int id=extras.getInt("id");
           TextView t1=(TextView)findViewById(R.id.textView1);
            TextView t2=(TextView)findViewById(R.id.textView2);
            TextView t3=(TextView)findViewById(R.id.textView3);
            TextView t4=(TextView)findViewById(R.id.textView4);
            t1.setText(name);
            t2.setText(""+id);
            t3.setText(city);
            t4.setText(blood);


        }
        findViews();
        implementClickEvents();
        selectImage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bitmap bitmap = getPath(data.getData());
            imageView=findViewById(R.id.icon);
            imageView.setImageBitmap(bitmap);

        }
    }

    private Bitmap getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(column_index);
        // cursor.close();
        // Convert file path into bitmap image using below line.
       Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        return bitmap;
    }

    private void selectImage() {
        if (ContextCompat.checkSelfPermission(AfterReg.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(AfterReg.this, "Select Your Photo",
                    Toast.LENGTH_SHORT).show();
        } else {
            requestStoragePermission();
        }

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(AfterReg.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void findViews() {
      //  fullPageScreenshot = (Button) findViewById(R.id.full_page_screenshot);
        customPageScreenshot = (Button) findViewById(R.id.custom_page_screenshot);

        t1=(TextView) findViewById(R.id.text5);

     //   rootContent = (LinearLayout) findViewById(R.id.root_content);

     //   imageView = (ImageView) findViewById(R.id.image_view);

        hiddenText = (TextView) findViewById(R.id.hidden_text);
    }

    /*  Implement Click events over Buttons */
    private void implementClickEvents() {
       // fullPageScreenshot.setOnClickListener(this);
        customPageScreenshot.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.custom_page_screenshot:
                takeScreenshot(ScreenshotType.CUSTOM);
                break;
        }
    }

    /*  Method which will take screenshot on Basis of Screenshot Type ENUM  */
    private void takeScreenshot(ScreenshotType screenshotType) {
        Bitmap b = null;
        switch (screenshotType) {
            case CUSTOM:
                //If Screenshot type is CUSTOM
                t1.setVisibility(View.INVISIBLE);
                hiddenText.setVisibility(View.INVISIBLE);//set the visibility to INVISIBLE of hidden text
                customPageScreenshot.setVisibility(View.INVISIBLE);
                //fullPageScreenshot.setVisibility(View.INVISIBLE);//set the visibility to INVISIBLE of first button
                //hiddenText.setVisibility(View.VISIBLE);//set the visibility to VISIBLE of hidden text

                b = ScreenshotUtils.getScreenShot(rootContent);

                //After taking screenshot reset the button and view again
                //fullPageScreenshot.setVisibility(View.VISIBLE);//set the visibility to VISIBLE of first button again

                //NOTE:  You need to use visibility INVISIBLE instead of GONE to remove the view from frame else it wont consider the view in frame and you will not get screenshot as you required.
                break;
        }

        //If bitmap is not null
        if (b != null) {
            showScreenShotImage(b);//show bitmap over imageview

            File saveFile = ScreenshotUtils.getMainDirectoryName(this);//get the path to save screenshot
            File file = ScreenshotUtils.store(b, "screenshot" + screenshotType + ".jpg", saveFile);//save the screenshot to selected path
            shareScreenshot(file);//finally share screenshot
        } //else
            //If bitmap is null show toast message
            //Toast.makeText(this, R.string.screenshot_take_failed, Toast.LENGTH_SHORT).show();

    }

    /*  Show screenshot Bitmap */
    private void showScreenShotImage(Bitmap b) {
       // imageView.setImageBitmap(b);
    }

    /*  Share Screenshot  */
    private void shareScreenshot(File file) {
        Uri uri = Uri.fromFile(file);//Convert file path into Uri for sharing
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.sharing_text));
        intent.putExtra(Intent.EXTRA_STREAM, uri);//pass uri here
        startActivity(Intent.createChooser(intent, getString(R.string.share_title)));
    }

}

