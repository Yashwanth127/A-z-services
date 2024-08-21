package com.example.a_zservices;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class userloginActivity extends AppCompatActivity {
    private static final String TAG ="userloginActivity";

    private EditText etUserPhoneNumber, etOtpCode;
    private Button btnSendOtp, btnVerifyOtp;
    private FirebaseAuth mAuth;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        etUserPhoneNumber = findViewById(R.id.etUserPhoneNumber);
        etOtpCode = findViewById(R.id.etOtpCode);
        btnSendOtp = findViewById(R.id.btnSendOtp);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);
        mAuth = FirebaseAuth.getInstance();

        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = etUserPhoneNumber.getText().toString().trim();

                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(userloginActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
                    return;
                }

                sendVerificationCode(phoneNumber);
            }
        });

        btnVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = etOtpCode.getText().toString().trim();

                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(userloginActivity.this, "Enter OTP", Toast.LENGTH_SHORT).show();
                    return;
                }

                verifyCode(code);
            }
        });
    }

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        String code = phoneAuthCredential.getSmsCode();
                        if (code != null) {
                            etOtpCode.setText(code);
                            verifyCode(code);
                        }
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(userloginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s;
                        etUserPhoneNumber.setVisibility(View.GONE);
                        btnSendOtp.setVisibility(View.GONE);
                        etOtpCode.setVisibility(View.VISIBLE);
                        btnVerifyOtp.setVisibility(View.VISIBLE);
                    }
                }
        );
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(userloginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(userloginActivity.this, UserProfileActivity.class);
                startActivity(intent);
                finish(); //
            } else {
                Toast.makeText(userloginActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
