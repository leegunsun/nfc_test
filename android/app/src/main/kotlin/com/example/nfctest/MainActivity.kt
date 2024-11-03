package com.example.nfctest

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity() {

    private val CHANNEL = "com.example.nfctest/nfc"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TestNFC", "onCreate $intent")

        // Check if the activity was started with an NFC intent
        handleNfcIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("TestNFC", "onNewIntent $intent")
        handleNfcIntent(intent)
    }

    private fun handleNfcIntent(intent: Intent) {
        Log.d("TestNFC", "handleNfcIntent called with action: ${intent.action}")

        if (intent.action == NfcAdapter.ACTION_TAG_DISCOVERED) {
            Log.d("TestNFC", "NFC tag detected")

            val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            val nfcData = if (rawMsgs != null) {
                // Process NFC data here if available
                "NFC tag detected with data: ${rawMsgs.contentToString()}"
            } else {
                "NFC tag detected, but no data found."
            }

            Log.d("TestNFC", "NFC Data: $nfcData")

            // Send the NFC data to Flutter via a MethodChannel
            flutterEngine?.dartExecutor?.binaryMessenger?.let {
                MethodChannel(it, CHANNEL).invokeMethod("onNfcData", nfcData)
            }
        }
    }

}
