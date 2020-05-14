package accurate.gaw.livestreamingapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.opengl.GLException;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daasuu.camerarecorder.CameraRecordListener;
import com.daasuu.camerarecorder.CameraRecorder;
import com.daasuu.camerarecorder.CameraRecorderBuilder;
import com.daasuu.camerarecorder.LensFacing;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;

import accurate.gaw.livestreamingapp.adapter.FilterAdapter;
import accurate.gaw.livestreamingapp.adapter.Filter_Adapter;
import accurate.gaw.livestreamingapp.dialogpack.DialogBoxCls;
import accurate.gaw.livestreamingapp.retrofitpack.RetrofitService;
import accurate.gaw.livestreamingapp.widget.Filters;
import accurate.gaw.livestreamingapp.widget.SampleGLView;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

/**
 * Created by sudamasayuki2 on 2018/07/02.
 */


public class BaseCameraActivity extends AppCompatActivity {

    private static Uri uniuri;
    private SampleGLView sampleGLView;
    protected CameraRecorder cameraRecorder;
    protected Camera camera;
    AppCompatActivity appCompatActivity = this;
    Dialog dialog1;
    public ProgressDialog pDialog;
    private String filepath;
    private ImageButton recordBtn;
    protected LensFacing lensFacing = LensFacing.BACK;
    protected int cameraWidth = 1280;
    protected int cameraHeight = 720;
    protected int videoWidth = 720;
    protected int videoHeight = 720;
    private AlertDialog filterDialog;
    private boolean toggleClick = false;
    public static int flag = 0;
    private SharedPreferences settings;
    String filePath;

    Handler timerUpdateHandler;
    boolean timelapseRunning = false;
    int currentTime = 0;
    final int SECONDS_BETWEEN_PHOTOS = 100;
    ProgressBar progressBar;

    int DIALOG_FLAG = 0;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(BaseCameraActivity.this,AfterLoginActivity.class));
        finish();
    }

    protected void initDialog() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {

                //the selected audio.
                Uri uri = data.getData();
                filePath = getPath(uri);
                RetrofitService retrofitService = new RetrofitService();
                retrofitService.uploadVideo(filePath, BaseCameraActivity.this);
                //startActivity(new Intent(BaseCameraActivity.this,AfterLoginActivity.class));
                //finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }


    private Runnable timerUpdateTask = new Runnable() {
        public void run() {
            if (currentTime < SECONDS_BETWEEN_PHOTOS) {
                currentTime++;
               // Log.d("tiemr", currentTime + "");
                progressBar.setProgress(currentTime);
                timerUpdateHandler.postDelayed(timerUpdateTask, 200);
            } else {
                //progressBar.setVisibility();
                currentTime = 0;

                cameraRecorder.stop();
                cameraRecorder.release();
                flag = 0;
                recordBtn.setImageResource(R.drawable.record2);

                if(DIALOG_FLAG == 0) {
                    /*DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    Intent intent_upload = new Intent();
                                    intent_upload.setType("video/*");
                                    intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(intent_upload, 1);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    finish();
                                    break;
                            }
                        }
                    };7101215038

                    AlertDialog.Builder builder = new AlertDialog.Builder(BaseCameraActivity.this);
                    builder.setMessage("Want to upload Video?").setPositiveButton("Yesssssss", dialogClickListener)
                            .setNegativeButton("Noooooo", dialogClickListener).show().setCancelable(false);*/

                    Log.d("flag0",DIALOG_FLAG+"");
                    //new DialogBoxCls().videoUploadDialog(BaseCameraActivity.this,BaseCameraActivity.this);

                   // DIALOG_FLAG = 1;
                }
                //Log.d("record", "finishedddddddddddd");

            }


            //countdownTextView.setText("" + currentTime);
        }
    };



    protected void onCreateActivity() {
        getSupportActionBar().hide();

        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, 1);
        setUpCamera();
        initDialog();


        timerUpdateHandler = new Handler();
        progressBar = findViewById(R.id.progressBar);
        recordBtn = findViewById(R.id.btn_record);
        recordBtn.setOnClickListener(v -> {

            if (flag == 0) {
                filepath = getVideoFilePath();
                // Log.d("filepath",filepath);

                // recordBtn.setText("Stop");

                cameraRecorder.start(filepath);
                recordBtn.setImageResource(R.drawable.record);
                timerUpdateHandler.post(timerUpdateTask);
                flag = 1;
            } else {
                currentTime = 100;
                progressBar.setProgress(0);
                cameraRecorder.stop();
                cameraRecorder.release();
                //Log.e("END",filepath);
                //uploadFile(filepath);
                // recordBtn.setText(getString(R.string.app_record));
                flag = 0;
                recordBtn.setImageResource(R.drawable.record2);
                new DialogBoxCls().videoUploadDialog(BaseCameraActivity.this,BaseCameraActivity.this);


            }

        });

        findViewById(R.id.vdoUploadBtn).setOnClickListener(v -> {

            Intent intent_upload = new Intent();
            intent_upload.setType("video/*");
            intent_upload.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent_upload, 1);


        });

        findViewById(R.id.btn_flash).setOnClickListener(v -> {
            if (cameraRecorder != null && cameraRecorder.isFlashSupport()) {
                cameraRecorder.switchFlashMode();
                cameraRecorder.changeAutoFocus();
            }
        });

        findViewById(R.id.btn_switch_camera).setOnClickListener(v -> {
            releaseCamera();
            if (lensFacing == LensFacing.BACK) {
                lensFacing = LensFacing.FRONT;
            } else {
                lensFacing = LensFacing.BACK;
            }
            toggleClick = true;
        });

        findViewById(R.id.btn_filter).setOnClickListener(v -> {

            filterDialog();
            //  Filters[] filters = Filters.values();
            /*BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(R.layout.camerafilterdialog);
            bottomSheetDialog.show();
            Filters[] filters = Filters.values();

            ListView filterList = bottomSheetDialog.findViewById(R.id.filterList);

            List<String> list = new ArrayList<>();


                String[] charList = new String[filters.length];
                for (int i = 0, n = filters.length; i < n; i++) {
                    list.add(filters[i].name());
                    charList[i] = filters[i].name();

                }

            *//*ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(BaseCameraActivity.this,
                    android.R.layout.simple_list_item_1,list);*//*

            FilterAdapter arrayAdapter = new FilterAdapter(BaseCameraActivity.this,charList);

                filterList.setAdapter(arrayAdapter);


                filterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        changeFilter(filters[position]);
                    }
                });*/

        });

        findViewById(R.id.btn_cross).setOnClickListener(v -> {
            startActivity(new Intent(BaseCameraActivity.this,AfterLoginActivity.class));
            finish();
        });


        findViewById(R.id.btn_back).setOnClickListener(v -> {
            startActivity(new Intent(BaseCameraActivity.this,AfterLoginActivity.class));
            finish();
        });

        findViewById(R.id.btn_image_capture).setOnClickListener(v -> {
            captureBitmap(bitmap -> {
                new Handler().post(() -> {
                    String imagePath = getImageFilePath();
                    saveAsPngImage(bitmap, imagePath);
                    exportPngToGallery(getApplicationContext(), imagePath);
                });
            });
        });


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }


    void filterDialog() {
        dialog1 = new Dialog(appCompatActivity);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog_filter1);
        RecyclerView recy_filter = (RecyclerView) dialog1.findViewById(R.id.recy_filter);
        final List<Filters> filterTypes = Filters.createFilterList();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(appCompatActivity);
        //recyclerview_servies.setLayoutManager(new GridLayoutManager(appCompatActivity, 2));
        recy_filter.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recy_filter.setVisibility(View.VISIBLE);
        Filter_Adapter dateAdapter = new Filter_Adapter(appCompatActivity, filterTypes, BaseCameraActivity.this);
        recy_filter.setAdapter(dateAdapter);


        Window window = dialog1.getWindow();
        assert window != null;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams wmlp = dialog1.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        wmlp.x = -500; //x position
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.setCancelable(true);
        dialog1.show();
    }

    public void filterData(int position) {
        final List<Filters> filterTypes = Filters.createFilterList();
        try {
            //cameraRecorder.setFilter(Filters.getFilterInstance(filterTypes.get(position), getApplicationContext()));
            //dialog1.dismiss();

            cameraRecorder.setFilter(Filters.getFilterInstance(filterTypes.get(position), getApplicationContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseCamera();
    }

    private void releaseCamera() {
        if (sampleGLView != null) {
            sampleGLView.onPause();
        }

        if (cameraRecorder != null) {
            cameraRecorder.stop();
            cameraRecorder.release();
            cameraRecorder = null;
        }

        if (sampleGLView != null) {
            ((FrameLayout) findViewById(R.id.wrap_view)).removeView(sampleGLView);
            sampleGLView = null;
        }
    }


    private void setUpCameraView() {
        runOnUiThread(() -> {
            FrameLayout frameLayout = findViewById(R.id.wrap_view);
            frameLayout.removeAllViews();
            sampleGLView = null;
            sampleGLView = new SampleGLView(getApplicationContext());
            sampleGLView.setTouchListener((event, width, height) -> {
                if (cameraRecorder == null) return;
                cameraRecorder.changeManualFocusPoint(event.getX(), event.getY(), width, height);
            });
            frameLayout.addView(sampleGLView);
        });
    }


    private void setUpCamera() {
        setUpCameraView();

        cameraRecorder = new CameraRecorderBuilder(this, sampleGLView)
                //.recordNoFilter(true)
                .cameraRecordListener(new CameraRecordListener() {
                    @Override
                    public void onGetFlashSupport(boolean flashSupport) {
                        runOnUiThread(() -> {
                            findViewById(R.id.btn_flash).setEnabled(flashSupport);
                        });
                    }

                    @Override
                    public void onRecordComplete() {
                        exportMp4ToGallery(getApplicationContext(), filepath);
                    }

                    @Override
                    public void onRecordStart() {

                    }

                    @Override
                    public void onError(Exception exception) {
                        Log.e("CameraRecorder", exception.toString());
                    }

                    @Override
                    public void onCameraThreadFinish() {
                        if (toggleClick) {
                            runOnUiThread(() -> {
                                setUpCamera();
                            });
                        }
                        toggleClick = false;
                    }
                })
                .videoSize(videoWidth, videoHeight)
                .cameraSize(cameraWidth, cameraHeight)
                .lensFacing(lensFacing)
                .build();


    }

    private void changeFilter(Filters filters) {
        cameraRecorder.setFilter(Filters.getFilterInstance(filters, getApplicationContext()));
    }


    private interface BitmapReadyCallbacks {
        void onBitmapReady(Bitmap bitmap);
    }

    private void captureBitmap(final BitmapReadyCallbacks bitmapReadyCallbacks) {
        sampleGLView.queueEvent(() -> {
            EGL10 egl = (EGL10) EGLContext.getEGL();
            GL10 gl = (GL10) egl.eglGetCurrentContext().getGL();
            Bitmap snapshotBitmap = createBitmapFromGLSurface(sampleGLView.getMeasuredWidth(), sampleGLView.getMeasuredHeight(), gl);

            runOnUiThread(() -> {
                bitmapReadyCallbacks.onBitmapReady(snapshotBitmap);
            });
        });
    }

    private Bitmap createBitmapFromGLSurface(int w, int h, GL10 gl) {

        int bitmapBuffer[] = new int[w * h];
        int bitmapSource[] = new int[w * h];
        IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
        intBuffer.position(0);

        try {
            gl.glReadPixels(0, 0, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer);
            int offset1, offset2, texturePixel, blue, red, pixel;
            for (int i = 0; i < h; i++) {
                offset1 = i * w;
                offset2 = (h - i - 1) * w;
                for (int j = 0; j < w; j++) {
                    texturePixel = bitmapBuffer[offset1 + j];
                    blue = (texturePixel >> 16) & 0xff;
                    red = (texturePixel << 16) & 0x00ff0000;
                    pixel = (texturePixel & 0xff00ff00) | red | blue;
                    bitmapSource[offset2 + j] = pixel;
                }
            }
        } catch (GLException e) {
            Log.e("CreateBitmap", "createBitmapFromGLSurface: " + e.getMessage(), e);
            return null;
        }

        return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888);
    }

    public void saveAsPngImage(Bitmap bitmap, String filePath) {
        try {
            File file = new File(filePath);
            FileOutputStream outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void exportMp4ToGallery(Context context, String filePath) {
        final ContentValues values = new ContentValues(2);
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Video.Media.DATA, filePath);
        context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                values);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + filePath)));

        uniuri = Uri.parse("file://" + filePath);
    }

    public static String getVideoFilePath() {
        String path = getAndroidMoviesFolder().getAbsolutePath() + "/" + new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + "cameraRecorder.mp4";
        return path;
    }

    public static File getAndroidMoviesFolder() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
    }

    private static void exportPngToGallery(Context context, String filePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(filePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public static String getImageFilePath() {
        return getAndroidImageFolder().getAbsolutePath() + "/" + new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + "cameraRecorder.png";
    }

    public static File getAndroidImageFolder() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }

}
