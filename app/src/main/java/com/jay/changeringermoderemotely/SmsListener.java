package com.jay.changeringermoderemotely;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by jaypatel on 16/01/18
 */

public class SmsListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null && intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs;
            String msg_from;
            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    if (pdus != null) {
                        msgs = new SmsMessage[pdus.length];
                        for (int i = 0; i < msgs.length; i++) {
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            msg_from = msgs[i].getOriginatingAddress();
                            String msgBody = msgs[i].getMessageBody();
                            if (Contact.checkPhoneNumberExists(msg_from)) {
                                String[] splittedMessage = msgBody.split("__");
                                String mode = splittedMessage[splittedMessage.length - 1].trim();
                                Toast.makeText(context, "Mode: " + mode, Toast.LENGTH_LONG).show();
                                SharedPreferences pref = context.getSharedPreferences("Preferences", Context.MODE_PRIVATE);
                                if (pref.getBoolean("canChangeProfile", false)) {
                                    AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                                    if (am != null) {
                                        //For Normal mode
                                        if (mode.equalsIgnoreCase("normal")) {
                                            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                                            Toast.makeText(context, "RINGER_MODE_NORMAL", Toast.LENGTH_LONG).show();
                                        }
                                        //For Silent mode
                                        else if (mode.equalsIgnoreCase("silent")) {
                                            am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                                            Toast.makeText(context, "RINGER_MODE_SILENT", Toast.LENGTH_LONG).show();
                                        }
                                        //For Vibrate mode
                                        else if (mode.equalsIgnoreCase("vibrate")) {
                                            am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                                            Toast.makeText(context, "RINGER_MODE_VIBRATE", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(context, msg_from + " Not in Contacts", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}