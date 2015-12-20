package com.pieter_jan.connector;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
{
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.website)
    public void goToWebsite()
    {
        askForUrl();
    }

    @OnClick(R.id.contacts)
    public void goToContacts()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        int requestCode = 1;
        startActivityForResult(intent, requestCode);
    }

    @OnClick(R.id.dial)
    public void dial()
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        startActivity(intent);
    }

    @OnClick(R.id.google)
    public void google()
    {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        startActivity(intent);
    }

    @OnClick(R.id.voice_command)
    public void goToVoiceCommand()
    {
        final int RECOGNIZER_REQ_CODE = 1234;
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        startActivityForResult(intent, RECOGNIZER_REQ_CODE);
    }

    private void askForUrl()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter a URL");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        //input.setText("http://");
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String url = input.getText().toString();
                if (!url.startsWith("http"))
                {
                    url = "http://".concat(url);
                }
                uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                try
                {
                    startActivity(intent);
                }
                catch (ActivityNotFoundException e)
                {
                    Toast.makeText(getApplicationContext(), "Invalid url", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        builder.show().setCancelable(false);
    }

}
