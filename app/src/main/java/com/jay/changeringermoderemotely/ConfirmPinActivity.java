package com.jay.changeringermoderemotely;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;

public class ConfirmPinActivity extends AppCompatActivity {

    private static String TAG = "TAG_";
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(ConfirmPinActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(ConfirmPinActivity.this);
                }
                builder.setTitle("Pincode does not match")
//                        .setMessage("Pincode does not match")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                editor.putString("InitialPin", null);
                                editor.apply();
                                Intent intent = new Intent(ConfirmPinActivity.this, CreatePinActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
//                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // do nothing
//                            }
//                        })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }

        @Override
        public void onEmpty() {
            Log.d(TAG, "Pin empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            Log.d(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_confirm_pin);

        PinLockView mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
        IndicatorDots mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);

        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);
        //mPinLockView.setCustomKeySet(new int[]{2, 3, 1, 5, 9, 6, 7, 0, 8, 4});
        //mPinLockView.enableLayoutShuffling();

        mPinLockView.setPinLength(4);
        mPinLockView.setTextColor(ContextCompat.getColor(this, R.color.white));

        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_ANIMATION);
    }
}
