package com.jay.changeringermoderemotely;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;

public class DashboardActivity extends AppCompatActivity {

    private static final int RESULT_PICK_CONTACT = 100;
    private ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        adapter = new ContactsAdapter(this);
        adapter.albumList = new ArrayList<>();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareContacts();

        final FloatingActionsMenu menuMultipleActions = findViewById(R.id.multiple_actions);
        final FloatingActionButton enterMobileNumber = findViewById(R.id.enter_mobile_number);
        enterMobileNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, EnterContactActivity.class);
                startActivity(intent);
                menuMultipleActions.collapse();
            }
        });
        final View pickFromContacts = findViewById(R.id.pick_from_contacts);
        pickFromContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
                menuMultipleActions.collapse();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareContacts();
    }

    //MultiContactPicker
    //https://github.com/broakenmedia/MultiContactPicker?utm_source=android-arsenal.com&utm_medium=referral&utm_campaign=6083
    private void prepareContacts() {
        adapter.albumList.clear();
        List<Contact> contacts = Contact.getAllContacts();
        if (contacts != null && contacts.size() > 0) {
            adapter.albumList.addAll(contacts);
        } else {
            adapter.albumList = new ArrayList<>();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        }
    }

    /**
     * Query the Uri and read contact details. Handle the picked contact data.
     */
    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo;
            String name;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            if (uri != null) {
                cursor = getContentResolver().query(uri, null, null, null, null);
            }
            if (cursor != null) {
                cursor.moveToFirst();
                // column index of the phone number
                int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                // column index of the contact name
                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                phoneNo = cursor.getString(phoneIndex);

                if (phoneNo.contains("+")) {
                    PhoneNumberUtil phoneUtil = PhoneNumberUtil.createInstance(this);
                    try {
                        // phone must begin with '+'
                        Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phoneNo, "");
                        int countryCode = numberProto.getCountryCode();
                        long phoneNumber = numberProto.getNationalNumber();
                        Toast.makeText(this, countryCode + "", Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, phoneNumber + "", Toast.LENGTH_SHORT).show();
                    } catch (NumberParseException e) {
                        e.printStackTrace();
                    }
                } else if (phoneNo.trim().startsWith("0")) {
                    String phoneNumber = phoneNo.substring(1);
                    Toast.makeText(this, phoneNumber, Toast.LENGTH_SHORT).show();
                }

                name = cursor.getString(nameIndex);
                Contact contact = new Contact(name, phoneNo);
                contact.save();
                adapter.albumList.add(new Contact(name, phoneNo));
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(DashboardActivity.this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
