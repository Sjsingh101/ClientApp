package accurate.gaw.livestreamingapp.dialogpack;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import accurate.gaw.livestreamingapp.AfterLoginActivity;
import accurate.gaw.livestreamingapp.BaseCameraActivity;
import accurate.gaw.livestreamingapp.R;

public class DialogBoxCls {

    public ProgressDialog pDialog;

    protected void initDialog() {


    }

    public void showpDialog(Context context) {
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        if (!pDialog.isShowing()) pDialog.show();
    }

    public void hidepDialog() {

        //if (pDialog != null && pDialog.isShowing())
                pDialog.dismiss();
    }


    public void videoUploadDialog(Context context, BaseCameraActivity cameraActivity) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.videouploaddialog);

           /* TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
            text.setText(msg);*/

        Button yesBtn = (Button) dialog.findViewById(R.id.yesbtn);
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent_upload = new Intent();
                intent_upload.setType("video/*");
                intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                cameraActivity.startActivityForResult(intent_upload, 1);


            }
        });

        Button noBtn = (Button) dialog.findViewById(R.id.nobtn);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(cameraActivity, AfterLoginActivity.class));
                cameraActivity.finish();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

}
