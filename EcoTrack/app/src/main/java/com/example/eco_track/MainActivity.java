package com.example.eco_track;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Button;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private Long userId;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des SharedPreferences
        sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        userId = sharedPref.getLong("user_id", -1);
        userEmail = sharedPref.getString("user_email", null);

        Button buttonScan = findViewById(R.id.buttonScan);
        buttonScan.setOnClickListener(v -> {
            // Configuration du scanner de code-barres
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("Scannez un code-barres");
            integrator.setCameraId(0);  // Utilise la caméra arrière
            integrator.setBeepEnabled(true);
            integrator.setBarcodeImageEnabled(true);
            integrator.initiateScan();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                // Scan annulé
            } else {
                String barcode = result.getContents();

                // Va vers ProductDetailsActivity avec le code-barres
                Intent intent = new Intent(MainActivity.this, ProductDetailsActivity.class);
                intent.putExtra("barcode", barcode);
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
