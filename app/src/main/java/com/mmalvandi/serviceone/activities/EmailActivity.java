package com.mmalvandi.serviceone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.mmalvandi.serviceone.R;

//TODO: Change the whole email... Don't show edittexts. Use a button to send email to you.

public class EmailActivity extends AppCompatActivity {
    EditText nameEdit, subjectEdit, bodyEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        getInit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.email_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.send) {
            if (nameEdit.getText().toString().isEmpty()
                    || subjectEdit.getText().toString().isEmpty()
                    || bodyEdit.getText().toString().isEmpty())
                Toast.makeText(EmailActivity.this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            else
                sendEmail("skillson28@gmail.com", subjectEdit.getText().toString(), "Eye Shield User.\n" + bodyEdit.getText().toString());
        }
        return super.onOptionsItemSelected(item);
    }

    private void getInit() {
        nameEdit = (EditText) findViewById(R.id.edit_name);
        subjectEdit = (EditText) findViewById(R.id.edit_subject);
        bodyEdit = (EditText) findViewById(R.id.edit_body);
        setTitle(R.string.contact_us);
    }

    private void sendEmail(String objective, String subject, String body) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{objective});
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT, body);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(EmailActivity.this, R.string.no_email_client_found, Toast.LENGTH_LONG).show();
        }
    }
}
