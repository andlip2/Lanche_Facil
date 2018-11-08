package lanchefacil.dalksoft.com.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import dmax.dialog.SpotsDialog;
import lanchefacil.dalksoft.com.R;
import lanchefacil.dalksoft.com.adapter.AdapterFavoritos;
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
                    public void onItemClick(View view, int position) {
                        alerdDialogEscluirAnuncio(position);
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

    private void alerdDialogEscluirAnuncio (final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remover Favorito");
        builder.setMessage("Tem certeza que deseja remover esse anuncio dos seus favoritos? ");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Anuncio anuncioSelecionado = listaAnuncios.get(position);
                anuncioSelecionado.excluirFavorito();
                adapterFavoritos.notifyDataSetChanged();
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                builder.setCancelable(true);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
