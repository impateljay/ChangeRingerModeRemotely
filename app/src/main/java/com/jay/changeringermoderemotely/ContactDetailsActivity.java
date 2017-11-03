package com.jay.changeringermoderemotely;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ContactDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        long id = getIntent().getLongExtra("contact_id", -1);
        Contact contact = Contact.load(Contact.class, id);

        ((TextView) findViewById(R.id.textview_contact_name)).setText(contact.getName());
        ((TextView) findViewById(R.id.textview_contact_number)).setText(contact.getPhoneNumber());
    }
}
