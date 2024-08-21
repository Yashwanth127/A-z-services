package com.example.a_zservices;

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

public class adminloginActivity extends AppCompatActivity {

    private EditText etAdminPhoneNumber, etAdminOtp;
    private Button btnSendOtp, btnVerifyOtp;
    private FirebaseAuth mAuth;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        etAdminPhoneNumber = findViewById(R.id.etAdminPhoneNumber);
        etAdminOtp = findViewById(R.id.etAdminOtp);
        btnSendOtp = findViewById(R.id.btnSendOtp);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);
        mAuth = FirebaseAuth.getInstance();

        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = etAdminPhoneNumber.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(adminloginActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Validate phone number format
                phoneNumber = formatPhoneNumber(phoneNumber);
                if (phoneNumber != null) {
                    sendVerificationCode(phoneNumber);
                } else {
                    Toast.makeText(adminloginActivity.this, "Invalid Phone Number Format", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = etAdminOtp.getText().toString().trim();
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(adminloginActivity.this, "Enter OTP", Toast.LENGTH_SHORT).show();
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
                            etAdminOtp.setText(code);
                            verifyCode(code);
                        }
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(adminloginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s;
                        etAdminPhoneNumber.setVisibility(View.GONE);
                        btnSendOtp.setVisibility(View.GONE);
                        etAdminOtp.setVisibility(View.VISIBLE);
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
                Toast.makeText(adminloginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                // Redirect to admin dashboard or home screen
            } else {
                Toast.makeText(adminloginActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String formatPhoneNumber(String phoneNumber) {

        phoneNumber = phoneNumber.replaceAll("[\\s\\-()]", "");


        if (!phoneNumber.startsWith("+")) {

            phoneNumber = "+91" +phoneNumber;
        }


        if (phoneNumber.length() >= 10 && phoneNumber.length() <= 15) {
            return phoneNumber;
        } else {
            return null;
        }
    }
}
