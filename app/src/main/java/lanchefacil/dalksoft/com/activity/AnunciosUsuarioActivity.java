package lanchefacil.dalksoft.com.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import dmax.dialog.SpotsDialog;
import lanchefacil.dalksoft.com.R;
import lanchefacil.dalksoft.com.adapter.AdapterAnunciosUsuario;
import lanchefacil.dalksoft.com.helper.ConfigFireBase;
import lanchefacil.dalksoft.com.helper.RecyclerItemClickListener;
import lanchefacil.dalksoft.com.model.Anuncio;

public class AnunciosUsuarioActivity extends AppCompatActivity {

    private RecyclerView recyclerAnuncios;
    private List <Anuncio> anuncios = new ArrayList<>();
    private AdapterAnunciosUsuario adapterMeusAnuncios;
    private DatabaseReference usuarioRef;
    private SweetAlertDialog pDialog;
    private FloatingActionButton fab;
    private StorageReference storage;
    private Anuncio anuncio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncios_usuario);

                usuarioRef = ConfigFireBase.getFirebase()
                .child("meus_anuncios")
                .child(ConfigFireBase.getIdUsuario());

        anuncio = new Anuncio();
        storage = ConfigFireBase.getReferenciaStorage();


        inicializarComponentes ();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerAnuncios.setLayoutManager(linearLayoutManager);
        recyclerAnuncios.setHasFixedSize(true);
        adapterMeusAnuncios = new AdapterAnunciosUsuario(anuncios,this);
        recyclerAnuncios.setAdapter(adapterMeusAnuncios);

        recuperarAnuncios ();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Meus Anúncios");

        recyclerAnuncios.addOnItemTouchListener(new RecyclerItemClickListener(
                this,
                recyclerAnuncios,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Anuncio anuncioSelecionado = anuncios.get(position);
                        Intent i = new Intent(AnunciosUsuarioActivity.this, EditarAnuncioActivity.class);
                        i.putExtra("anuncioSelecionado", anuncioSelecionado);
                        startActivity(i);
                    }
                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    }
                }
        ));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AnunciosUsuarioActivity.this, CadastrarAnuncioActivity.class);
                startActivity(i);
            }
        });
    }


    private void recuperarAnuncios() {
        pDialog = new SweetAlertDialog(AnunciosUsuarioActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                anuncios.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    anuncios.add(ds.getValue(Anuncio.class));
                }
                Collections.reverse(anuncios);
                adapterMeusAnuncios.notifyDataSetChanged();
                pDialog.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void inicializarComponentes() {
        recyclerAnuncios = findViewById(R.id.recyclerMeusAnuncios2);
        fab = findViewById(R.id.floatingActionCadAnuncio);
    }
}
