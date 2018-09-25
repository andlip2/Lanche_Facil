package lanchefacil.dalksoft.com.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dmax.dialog.SpotsDialog;
import lanchefacil.dalksoft.com.R;
import lanchefacil.dalksoft.com.adapter.AdapterMeusAnuncios;
import lanchefacil.dalksoft.com.helper.ConfigFireBase;
import lanchefacil.dalksoft.com.helper.RecyclerItemClickListener;
import lanchefacil.dalksoft.com.model.Anuncio;

public class FavoritosActivity extends AppCompatActivity {

    private RecyclerView recyclerFavoritos;
    private AdapterMeusAnuncios adapterMeusAnuncios;
    private DatabaseReference anunciosPublicosRef;
    private List<Anuncio> listaAnuncios = new ArrayList<>();
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        anunciosPublicosRef = ConfigFireBase.getFirebase().child("favoritos");
        recyclerFavoritos = findViewById(R.id.recyclerFavoritos);

        exibirAnuncios();
    }
    public void exibirAnuncios () {
        recyclerFavoritos.setLayoutManager(new LinearLayoutManager(this));
        recyclerFavoritos.setHasFixedSize(true);
        adapterMeusAnuncios = new AdapterMeusAnuncios(listaAnuncios,this);
        recyclerFavoritos.setAdapter(adapterMeusAnuncios);
        recuperarAnunciosPublicos();

        recyclerFavoritos.addOnItemTouchListener(new RecyclerItemClickListener(
                this,
                recyclerFavoritos,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Anuncio anuncioSelecionado = listaAnuncios.get(position);
                        Intent i = new Intent(FavoritosActivity.this, DetalhesAnuncioActivity.class);
                        i.putExtra("anuncioSelecionado", anuncioSelecionado);
                        startActivity(i);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));
    }
    public void recuperarAnunciosPublicos () {
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Carregando Anúncios")
                .setCancelable(false)
                .build();
        dialog.show();
        anunciosPublicosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaAnuncios.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    listaAnuncios.add(ds.getValue(Anuncio.class));
                }
                Collections.reverse(listaAnuncios);
                adapterMeusAnuncios.notifyDataSetChanged();
                dialog.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
