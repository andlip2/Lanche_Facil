package lanchefacil.dalksoft.com.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import lanchefacil.dalksoft.com.R;

public class AjudaActivity extends AppCompatActivity {

    private TextView pergunta1, pergunta2, pergunta3, pergunta4, pergunta5, pergunta6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajuda);

        getSupportActionBar().setTitle("Ajuda");

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        pergunta1 = findViewById(R.id.textAjudaP1);
        pergunta2 = findViewById(R.id.textAjudaP2);
        pergunta3 = findViewById(R.id.textAjudaP3);
        pergunta4 = findViewById(R.id.textAjudaP4);
        pergunta5 = findViewById(R.id.textAjudaP5);
        pergunta6 = findViewById(R.id.textAjudaP6);
    }
}

