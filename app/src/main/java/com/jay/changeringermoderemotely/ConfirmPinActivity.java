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

public class ConfirmPinActivity extends AppCompatActivity {

    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            SharedPreferences sharedPreferences = getSharedPreferences("PIN", MODE_PRIVATE);
            String sharedPreferencesPin = sharedPreferences.getString("InitialPin", null);
            final SharedPreferences.Editor editor = sharedPreferences.edit();
            if (pin.equals(sharedPreferencesPin)) {
                editor.putString("SecretCode", pin);
                editor.apply();
                Intent intent = new Intent(ConfirmPinActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            } else {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(ConfirmPinActivity.this);
                builder.setTitle("CRMR")
                        .setMessage("Pincode does not match")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                editor.putString("InitialPin", null);
                                editor.apply();
                                Intent intent = new Intent(ConfirmPinActivity.this, CreatePinActivity.class);
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
        setContentView(R.layout.activity_confirm_pin);
        PinLockView mPinLockView = findViewById(R.id.pin_lock_view);
        IndicatorDots mIndicatorDots = findViewById(R.id.indicator_dots);
        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);
        mPinLockView.setPinLength(4);
        mPinLockView.setTextColor(ContextCompat.getColor(this, R.color.white));
        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_ANIMATION);
    }
}
