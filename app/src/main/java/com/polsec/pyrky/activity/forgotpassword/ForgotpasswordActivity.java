package com.polsec.pyrky.activity.forgotpassword;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.polsec.pyrky.R;
import com.polsec.pyrky.activity.signin.SignInActivity;
import com.polsec.pyrky.utils.Utils;

public class ForgotpasswordActivity extends AppCompatActivity {
    ImageView backBtnclose;
    RelativeLayout cancelbtnrel;
    EditText mEmail;
    Button resetBtn,resetbutton1;
    RelativeLayout LinLay;
    TextView txt_error;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        mEmail =(EditText)findViewById(R.id.email_edit);
        cancelbtnrel=(RelativeLayout)findViewById(R.id.btncancel);
        resetBtn=(Button)findViewById(R.id.sinin_edt);
//        txt_error = (TextView)findViewById(R.id.txt_error);
        LinLay=(RelativeLayout)findViewById(R.id.const_lay);
        backBtnclose=(ImageView)findViewById(R.id.close_img);
//
        mEmail.addTextChangedListener(mTextWatcher);
        mEmail.setInputType(mEmail.getInputType()
                | EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                | EditorInfo.TYPE_TEXT_VARIATION_FILTER);

        LinLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(ForgotpasswordActivity.this);
            }
        });

        cancelbtnrel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotpasswordActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
//                onBackPressed();


            }
        });

        backBtnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotpasswordActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
//           onBackPressed();


            }
        });
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(ForgotpasswordActivity.this);
                String emailAddress = mEmail.getText().toString();
                if (validateForm()) {
                    if (emailAddress.isEmpty() || !emailAddress.contains("@") || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {

//                    showerror("invalid email address");
                        Toast.makeText(ForgotpasswordActivity.this, "inavalid email", Toast.LENGTH_SHORT).show();

                    } else {
                        showProgressDialog();
                        FirebaseAuth auth = FirebaseAuth.getInstance();

                        auth.sendPasswordResetEmail(emailAddress)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            Popup();

//                                            Toast.makeText(getContext(), "Reset Successsfully", Toast.LENGTH_SHORT).show();
//                                            Log.d(TAG, "Email sent.");
                                        } else {
//                                        showerror("Reset password failed.");
                                            Toast.makeText(ForgotpasswordActivity.this, "Reset password failed.", Toast.LENGTH_SHORT).show();
                                            hideProgressDialog();

                                        }
                                    }
                                });
                    }
                }
            }

        });

    }

    public void showProgressDialog() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        //View view = getLayoutInflater().inflate(R.layout.progress);
        alertDialog.setView(R.layout.progress);
        dialog = alertDialog.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    public void hideProgressDialog(){
        if(dialog!=null)
            dialog.dismiss();
    }
    private void Popup() {


        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.alert_lay, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(deleteDialogView);
        Button ok = (Button)deleteDialogView.findViewById(R.id.ok_button);

        final AlertDialog alertDialog1 = alertDialog.create();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotpasswordActivity.this,SignInActivity.class);
                startActivity(intent);
                finish();
                hideProgressDialog();
                alertDialog1.dismiss();
            }
        });


        alertDialog1.setCanceledOnTouchOutside(false);
        try {
            alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        alertDialog1.show();
//        alertDialog1.getWindow().setLayout((int) Utils.convertDpToPixel(228,getActivity()),(int)Utils.convertDpToPixel(220,getActivity()));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog1.getWindow().getAttributes());
//        lp.height=200dp;
//        lp.width=228;
        lp.gravity = Gravity.CENTER;
//        lp.windowAnimations = R.style.DialogAnimation;
        alertDialog1.getWindow().setAttributes(lp);
    }


    private boolean validateForm() {
        boolean valid = true;

        String email = mEmail.getText().toString().trim();
        if (!TextUtils.isEmpty(email)) {
            valid = true;
        }else{
            if (TextUtils.isEmpty(email)){
                Toast.makeText(this, "Enter e-mail address", Toast.LENGTH_SHORT).show();
                valid = false;
            }
            if ((!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())){
                Toast.makeText(this, "Enter valid e-mail address", Toast.LENGTH_SHORT).show();
                valid = false;
            }
        }

        return valid;
    }
        private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // check Fields For Empty Values

        }
    };

}

