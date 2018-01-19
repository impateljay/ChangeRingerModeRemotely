package com.jay.changeringermoderemotely;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;

public class PinLoginActivity extends AppCompatActivity {

    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            SharedPreferences sharedPreferences = getSharedPreferences("PIN", MODE_PRIVATE);
            if (pin.equals(sharedPreferences.getString("SecretCode", null))) {
                Intent intent = new Intent(PinLoginActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            } else {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(PinLoginActivity.this);
                builder.setTitle("CRMR")
                        .setMessage("Incorrect Pincode")
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(PinLoginActivity.this, PinLoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .show();
            }
        }

        @Override
        public void onEmpty() {
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pin_login);
        PinLockView mPinLockView = findViewById(R.id.pin_lock_view);
        IndicatorDots mIndicatorDots = findViewById(R.id.indicator_dots);
        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);
        mPinLockView.setPinLength(4);
        mPinLockView.setTextColor(ContextCompat.getColor(this, R.color.white));
        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_ANIMATION);
    }
}
