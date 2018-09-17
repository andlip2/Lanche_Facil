package lanchefacil.dalksoft.com.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Adapter;

import java.util.ArrayList;
import java.util.List;

import lanchefacil.dalksoft.com.R;
import lanchefacil.dalksoft.com.adapter.AdapterMeusAnuncios;
import lanchefacil.dalksoft.com.model.Anuncio;

public class MeusAnunciosActivity extends AppCompatActivity {

    private RecyclerView recyclerAnuncios;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private List <Anuncio> anuncios = new ArrayList<>();
    private AdapterMeusAnuncios adapterMeusAnuncios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_anuncios);

        inicializarComponentes ();
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerAnuncios.setLayoutManager(new LinearLayoutManager(this));
        recyclerAnuncios.setHasFixedSize(true);

//        recyclerAnuncios.setAdapter();
    }

    private void inicializarComponentes() {
        recyclerAnuncios = findViewById(R.id.recyclerMeusAnuncios);
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fabMeusAnuncios);
    }

}
