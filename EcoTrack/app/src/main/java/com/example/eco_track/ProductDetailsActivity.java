package com.example.eco_track;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eco_track.api.ApiService;
import com.example.eco_track.api.RetrofitClient;
import com.example.eco_track.model.Product;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView textViewName, textViewNutri, textViewEco, textViewOrigin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // Initialisation des TextViews
        textViewName = findViewById(R.id.textViewProductName);
        textViewNutri = findViewById(R.id.textViewNutriscore);
        textViewEco = findViewById(R.id.textViewEcoscore);
        textViewOrigin = findViewById(R.id.textViewOrigin);

        // Récupération du code-barres depuis l'Intent
        String barcode = getIntent().getStringExtra("barcode");

        if (barcode != null && !barcode.isEmpty()) {
            fetchProductDetails(barcode);
        } else {
            textViewName.setText("Code-barres invalide");
        }
    }

    private void fetchProductDetails(String barcode) {
        ApiService api = RetrofitClient.getApiService();
        Call<Product> call = api.getProductByCode(barcode);

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Product product = response.body();
                    textViewName.setText(product.getNom());
                    textViewNutri.setText("Nutriscore : " + product.getNutriscore());
                    textViewEco.setText("Ecoscore : " + product.getEcoscore());
                    textViewOrigin.setText("Origine : " + product.getOrigine());
                } else {
                    textViewName.setText("Produit non trouvé");
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                textViewName.setText("Erreur de connexion");
            }
        });
    }
}
