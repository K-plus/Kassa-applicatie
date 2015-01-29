package com.studiovazer.kassaapplicatie;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements NfcAdapter.CreateNdefMessageCallback
{
    private int ontvangenTotaalBedrag = 0;
    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupNFCAdapter();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()))
        {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage message = (NdefMessage) rawMessages[0]; // only one message transferred

            //retrieved the nfc message
            ontvangenTotaalBedrag = Integer.valueOf(new String(message.getRecords()[0].getPayload()));
            nfcAdapter.setNdefPushMessageCallback(this, this);
        }

    }

    private void setupNFCAdapter()
    {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (!nfcAdapter.isEnabled())
        {
            Toast.makeText(this, "Please enable NFC via Settings.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent)
    {
        String message = "Het bedrag: " + ontvangenTotaalBedrag + " is verrekent! Tot de volgende keer Kappa!";
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", message.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
    }
}
