package com.jay.changeringermoderemotely;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class EnterContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_contact);

        final TextInputLayout nameTextInputLayout = findViewById(R.id.input_layout_name);
        final TextInputLayout phoneTextInputLayout = findViewById(R.id.input_layout_phone);
        final EditText name = findViewById(R.id.editText_name);
        final EditText phone = findViewById(R.id.editText_phone);

        findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        findViewById(R.id.button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(name.getText())) {
                    nameTextInputLayout.setError("Please enter name of the person");
                } else {
                    nameTextInputLayout.setErrorEnabled(false);
                }
                if (TextUtils.isEmpty(phone.getText())) {
                    phoneTextInputLayout.setError("Please enter mobile number of the person");
                } else {
                    phoneTextInputLayout.setErrorEnabled(false);
                }
                if (!TextUtils.isEmpty(name.getText()) && !TextUtils.isEmpty(phone.getText())) {
                    Contact contact = new Contact(name.getText().toString().trim(), phone.getText().toString().trim());
                    contact.save();
                    finish();
                }
            }
        });
    }
}
