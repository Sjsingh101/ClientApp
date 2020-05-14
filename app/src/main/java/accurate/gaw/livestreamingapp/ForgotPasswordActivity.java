package accurate.gaw.livestreamingapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {

    Button btnForgotPassword;
    EditText emailTextView;
    private static final String FORGOTURL = "http://test-demo.co.in/shwe_wala/api/forgot.php";
    private static final String SENDOTP = "http://test-demo.co.in/shwe_wala/api/verify.php";
    private static final String RESETPASS = "http://test-demo.co.in/shwe_wala/api/reset_password.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        emailTextView = findViewById(R.id.emailTextView);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest request = new StringRequest(Request.Method.POST,FORGOTURL,
                        new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("CODE", response);
                        Gson gson = new Gson();
                        //Map<String,String> mp = gson.fromJson(response,Map.class);
                        List<Map<String, String>> ls = gson.fromJson(response, List.class);
                        Map<String, String> mp = ls.get(0);

                        String val = mp.get("message");
                        if(val.equals("success")){

                            OtpDialoag dialoag = new OtpDialoag();
                            dialoag.showDialog(ForgotPasswordActivity.this,val);

                        }

                        Log.d("Json", val + "");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ForgotPasswordActivity.this, "some thingh is wrong", Toast.LENGTH_SHORT).show();

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("action", "forgot");
                        params.put("email", emailTextView.getText().toString());


                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(ForgotPasswordActivity.this);
                queue.add(request);
            }
        });

    }

    public class OtpDialoag {

        public void showDialog(Activity activity, String msg){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.enter_opt_dialog);

           /* TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
            text.setText(msg);*/

            Button dialogButton = (Button) dialog.findViewById(R.id.btnSendOtp);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    EditText otpEditText = dialog.findViewById(R.id.otpEditText);

                    StringRequest request = new StringRequest(Request.Method.POST,SENDOTP,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    //Log.d("CODE", response);
                                    Gson gson = new Gson();
                                    //Map<String,String> mp = gson.fromJson(response,Map.class);
                                    List<Map<String, String>> ls = gson.fromJson(response, List.class);
                                    Map<String, String> mp = ls.get(0);

                                    String val = mp.get("message");
                                    if(val.equals("success")){

                                       /* OtpDialoag dialoag = new OtpDialoag();
                                        dialoag.showDialog(ForgotPasswordActivity.this,val);*/

                                        resetDialog(ForgotPasswordActivity.this,"success");

                                    }

                                    Log.d("Json", val + "");
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ForgotPasswordActivity.this, "some thingh is wrong", Toast.LENGTH_SHORT).show();

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("action", "verify_otp");
                            params.put("otp", otpEditText.getText().toString());


                            return params;
                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(ForgotPasswordActivity.this);
                    queue.add(request);


                    dialog.dismiss();
                }
            });

            dialog.show();


            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            // The absolute width of the available display size in pixels.
            int displayWidth = displayMetrics.widthPixels;
            // The absolute height of the available display size in pixels.
            int displayHeight = displayMetrics.heightPixels;

            // Initialize a new window manager layout parameters
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

            // Copy the alert dialog window attributes to new layout parameter instance
            layoutParams.copyFrom(dialog.getWindow().getAttributes());

            // Set alert dialog width equal to screen width 70%
            int dialogWindowWidth = (int) (displayWidth * 0.9f);
            // Set alert dialog height equal to screen height 70%
            int dialogWindowHeight = (int) (displayHeight * 0.5f);

            // Set the width and height for the layout parameters
            // This will bet the width and height of alert dialog
            layoutParams.width = dialogWindowWidth;
            layoutParams.height = dialogWindowHeight;

            // Apply the newly created layout parameters to the alert dialog window
            dialog.getWindow().setAttributes(layoutParams);

        }
    }


    public void resetDialog(Activity activity, String msg){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.reset_password);

           /* TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
            text.setText(msg);*/

        Button dialogButton = dialog.findViewById(R.id.btnResetPass);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText resetPassEditText = dialog.findViewById(R.id.resetpassword);
                EditText resetConfirmEditText = dialog.findViewById(R.id.resetconfirmpass);
                EditText resetemailEditText = dialog.findViewById(R.id.resetEmail);

                StringRequest request = new StringRequest(Request.Method.POST,RESETPASS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Log.d("CODE", response);
                                Gson gson = new Gson();
                                //Map<String,String> mp = gson.fromJson(response,Map.class);
                                List<Map<String, String>> ls = gson.fromJson(response, List.class);
                                Map<String, String> mp = ls.get(0);

                                String val = mp.get("message");
                                if(val.equals("success")){

                                       /* OtpDialoag dialoag = new OtpDialoag();
                                        dialoag.showDialog(ForgotPasswordActivity.this,val);*/
                                    Toast.makeText(activity, "Password changed Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ForgotPasswordActivity.this, ManualLoginActivity.class));
                                }

                                Log.d("Json", val + "");
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ForgotPasswordActivity.this, "some thingh is wrong", Toast.LENGTH_SHORT).show();

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("action", "reset_password");
                        params.put("password", resetPassEditText.getText().toString());

                        params.put("confirm_password", resetConfirmEditText.getText().toString());
                        params.put("email",resetemailEditText.getText().toString());
                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(ForgotPasswordActivity.this);
                queue.add(request);


                dialog.dismiss();
            }
        });

        dialog.show();


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // The absolute width of the available display size in pixels.
        int displayWidth = displayMetrics.widthPixels;
        // The absolute height of the available display size in pixels.
        int displayHeight = displayMetrics.heightPixels;

        // Initialize a new window manager layout parameters
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        // Copy the alert dialog window attributes to new layout parameter instance
        layoutParams.copyFrom(dialog.getWindow().getAttributes());

        // Set alert dialog width equal to screen width 70%
        int dialogWindowWidth = (int) (displayWidth * 0.9f);
        // Set alert dialog height equal to screen height 70%
        int dialogWindowHeight = (int) (displayHeight * 1f);

        // Set the width and height for the layout parameters
        // This will bet the width and height of alert dialog
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;

        // Apply the newly created layout parameters to the alert dialog window
        dialog.getWindow().setAttributes(layoutParams);

    }

}
