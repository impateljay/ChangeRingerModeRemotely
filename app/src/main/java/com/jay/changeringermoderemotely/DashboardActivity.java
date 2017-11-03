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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private static final int RESULT_PICK_CONTACT = 100;
    private RecyclerView recyclerView;
    private ContactsAdapter adapter;
    private List<Contact> albumList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        albumList = new ArrayList<>();
        adapter = new ContactsAdapter(this, albumList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareContacts();

        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        final FloatingActionButton enterMobileNumber = (FloatingActionButton) findViewById(R.id.enter_mobile_number);
        enterMobileNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
            }
        });
        final View pickFromContacts = findViewById(R.id.pick_from_contacts);
        pickFromContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
            }
        });
    }

    private void prepareContacts() {
        Contact a = new Contact("True Romance", "9876543210");
        albumList.add(a);

        a = new Contact("Loud", "9876543210");
        albumList.add(a);

        a = new Contact("Legend", "9876543210");
        albumList.add(a);

        List<Contact> contacts = Contact.getAllContacts();
        if (contacts != null && contacts.size() > 0) {
            albumList.addAll(contacts);
        } else {
            albumList = new ArrayList<>();
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
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }

    /**
     * Query the Uri and read contact details. Handle the picked contact data.
     *
     * @param data
     */
    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null;
            String name = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                // column index of the phone number
                int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                // column index of the contact name
                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                phoneNo = cursor.getString(phoneIndex);
                name = cursor.getString(nameIndex);
                Toast.makeText(DashboardActivity.this, "Name : " + name + "\nPhone No. : " + phoneNo, Toast.LENGTH_LONG).show();
                Contact contact = new Contact(name, phoneNo);
//                contact.save();
                albumList.add(new Contact(name, phoneNo));
                adapter.notifyDataSetChanged();
                // Set the value to the textviews
//            textView1.setText(name);
//            textView2.setText(phoneNo);
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
