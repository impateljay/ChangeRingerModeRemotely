package com.jay.changeringermoderemotely;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.x500.X500Principal;

public class CreatePinActivity extends AppCompatActivity {

    private static final String AndroidKeyStore = "AndroidKeyStore";
    private static final String AES_MODE = "AES/GCM/NoPadding";
    private static String TAG = "TAG_";
    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            SharedPreferences sharedPreferences = getSharedPreferences("PIN", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("InitialPin", pin);
            editor.apply();
            Intent intent = new Intent(CreatePinActivity.this, ConfirmPinActivity.class);
            startActivity(intent);
            finish();
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
        setContentView(R.layout.activity_create_pin);

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void keyStore() {
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance(AndroidKeyStore);
            keyStore.load(null);
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }

        try {
            if (!keyStore.containsAlias("KEY_ALIAS")) {
                KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, AndroidKeyStore);
                keyGenerator.init(
                        new KeyGenParameterSpec.Builder("KEY_ALIAS",
                                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                                .setBlockModes(KeyProperties.BLOCK_MODE_GCM).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                                .setRandomizedEncryptionRequired(false)
                                .build());
                keyGenerator.generateKey();
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

//    private java.security.Key getSecretKey(Context context) throws Exception {
//        return KeyStore.getInstance().getKey(XEALTH_KEY_ALIAS, null);
//    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void abc() {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            String alias = "key3";

            int nBefore = keyStore.size();

            // Create the keys if necessary
            if (!keyStore.containsAlias(alias)) {

                Calendar notBefore = Calendar.getInstance();
                Calendar notAfter = Calendar.getInstance();
                notAfter.add(Calendar.YEAR, 1);
                KeyPairGeneratorSpec spec = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    spec = new KeyPairGeneratorSpec.Builder(this)
                            .setAlias(alias)
                            .setKeyType(KeyProperties.KEY_ALGORITHM_RSA)
                            .setKeySize(2048)
                            .setSubject(new X500Principal("CN=test"))
                            .setSerialNumber(BigInteger.ONE)
                            .setStartDate(notBefore.getTime())
                            .setEndDate(notAfter.getTime())
                            .build();
                }
                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                generator.initialize(spec);

                KeyPair keyPair = generator.generateKeyPair();
            }
            int nAfter = keyStore.size();
            Log.v(TAG, "Before = " + nBefore + " After = " + nAfter);

            // Retrieve the keys
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
            RSAPrivateKey privateKey = (RSAPrivateKey) privateKeyEntry.getPrivateKey();
            RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();

            Log.v(TAG, "private key = " + privateKey.toString());
            Log.v(TAG, "public key = " + publicKey.toString());

            // Encrypt the text
            String plainText = "This text is supposed to be a secret!";
            String dataDirectory = getApplicationInfo().dataDir;
            String filesDirectory = getFilesDir().getAbsolutePath();
            String encryptedDataFilePath = filesDirectory + File.separator + "keep_yer_secrets_here";

            Log.v(TAG, "plainText = " + plainText);
            Log.v(TAG, "dataDirectory = " + dataDirectory);
            Log.v(TAG, "filesDirectory = " + filesDirectory);
            Log.v(TAG, "encryptedDataFilePath = " + encryptedDataFilePath);

            Cipher inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
            inCipher.init(Cipher.ENCRYPT_MODE, publicKey);

            Cipher outCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
            outCipher.init(Cipher.DECRYPT_MODE, privateKey);

            CipherOutputStream cipherOutputStream =
                    new CipherOutputStream(
                            new FileOutputStream(encryptedDataFilePath), inCipher);
            cipherOutputStream.write(plainText.getBytes("UTF-8"));
            cipherOutputStream.close();

            CipherInputStream cipherInputStream =
                    new CipherInputStream(new FileInputStream(encryptedDataFilePath),
                            outCipher);
            byte[] roundTrippedBytes = new byte[1000]; // TODO: dynamically resize as we get more data

            int index = 0;
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                roundTrippedBytes[index] = (byte) nextByte;
                index++;
            }
            String roundTrippedString = new String(roundTrippedBytes, 0, index, "UTF-8");
            Log.v(TAG, "round tripped string = " + roundTrippedString);

        } catch (NoSuchAlgorithmException | NoSuchProviderException | UnsupportedOperationException | InvalidKeyException | NoSuchPaddingException | UnrecoverableEntryException | IOException | CertificateException | KeyStoreException | InvalidAlgorithmParameterException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }
}
