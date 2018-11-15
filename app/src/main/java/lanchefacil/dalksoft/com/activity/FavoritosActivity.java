package lanchefacil.dalksoft.com.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lanchefacil.dalksoft.com.R;
import lanchefacil.dalksoft.com.helper.AdapterFavoritos;
import lanchefacil.dalksoft.com.helper.ConfigFireBase;
import lanchefacil.dalksoft.com.helper.RecyclerItemClickListener;
import lanchefacil.dalksoft.com.model.Anuncio;

public class FavoritosActivity extends AppCompatActivity {

    private RecyclerView recyclerFavoritos;
    private AdapterFavoritos adapterFavoritos;
    private DatabaseReference usuarioRef;
    private List<Anuncio> listaAnuncios = new ArrayList<>();
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        usuarioRef = ConfigFireBase.getFirebase().child("favoritos")
                .child(ConfigFireBase.getIdUsuario());
        recyclerFavoritos = findViewById(R.id.recyclerFavoritos);

        exibirAnuncios();
        getSupportActionBar().setTitle("Favoritos");
    }
    public void exibirAnuncios () {
        recyclerFavoritos.setLayoutManager(new LinearLayoutManager(this));
        recyclerFavoritos.setHasFixedSize(true);
        adapterFavoritos = new AdapterFavoritos(listaAnuncios,this);
        recyclerFavoritos.setAdapter(adapterFavoritos);
        recuperarFavoritos();
        recyclerFavoritos.addOnItemTouchListener(new RecyclerItemClickListener(
                this,
                recyclerFavoritos,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(final View view, final int position) {
                        new SweetAlertDialog(FavoritosActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Remover Favorito")
                                .setContentText("Tem certeza que deseja remover esse anúncio de seus favoritos?")
                                .setCancelText("NÃO")
                                .setCancelClickListener(null)
                                .setConfirmText("SIM")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        Anuncio anuncioSelecionado = listaAnuncios.get(position);
                                        anuncioSelecionado.excluirFavorito();
                                        adapterFavoritos.notifyDataSetChanged();

                                        feito();
                                        sDialog.cancel();
                                    }
                                })
                                .show();
                    }
                    @Override
                    public void onLongItemClick(View view, int position) {
                        Anuncio anuncioSelecionado = listaAnuncios.get(position);
                        Intent i = new Intent(FavoritosActivity.this, DetalhesAnuncioActivity.class);
                        i.putExtra("anuncioSelecionado", anuncioSelecionado);
                        startActivity(i);
                    }
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    }
                }
        ));


    }

    public void feito () {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Removido")
                .setContentText("O anúncio foi removido de seus favoritos")
                .setConfirmText("OK")
                .show();
    }

    private void recuperarFavoritos() {
        pDialog = new SweetAlertDialog(FavoritosActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaAnuncios.clear();
                for (DataSnapshot id: dataSnapshot.getChildren()){
                    listaAnuncios.add(id.getValue(Anuncio.class));
                }
                Collections.reverse(listaAnuncios);
                adapterFavoritos.notifyDataSetChanged();
                pDialog.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


}
