package lanchefacil.dalksoft.com.activity;

import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import lanchefacil.dalksoft.com.Manifest;
import lanchefacil.dalksoft.com.R;

public class CadastrarAnuncioActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_anuncio);

        ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS);
    }


}
