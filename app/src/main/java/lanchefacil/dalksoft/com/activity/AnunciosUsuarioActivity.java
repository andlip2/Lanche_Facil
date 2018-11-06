package lanchefacil.dalksoft.com.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
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
import lanchefacil.dalksoft.com.adapter.AdapterAnunciosUsuario;
import lanchefacil.dalksoft.com.helper.ConfigFireBase;
import lanchefacil.dalksoft.com.helper.RecyclerItemClickListener;
import lanchefacil.dalksoft.com.model.Anuncio;

public class AnunciosUsuarioActivity extends AppCompatActivity {

    private RecyclerView recyclerAnuncios;
    private List <Anuncio> anuncios = new ArrayList<>();
    private AdapterAnunciosUsuario adapterMeusAnuncios;
    private DatabaseReference usuarioRef;
    private AlertDialog dialog;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncios_usuario);

                usuarioRef = ConfigFireBase.getFirebase()
                .child("meus_anuncios")
                .child(ConfigFireBase.getIdUsuario());
        inicializarComponentes ();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerAnuncios.setLayoutManager(linearLayoutManager);
        recyclerAnuncios.setHasFixedSize(true);
        adapterMeusAnuncios = new AdapterAnunciosUsuario(anuncios,this);
        recyclerAnuncios.setAdapter(adapterMeusAnuncios);

        recuperarAnuncios ();
        getSupportActionBar().setTitle("Meus Anúncios");

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


                        alerdDialogEscluirAnuncio(position);
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
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Carregando Anúncios")
                .setCancelable(false)
                .build();
        dialog.show();
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                anuncios.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    anuncios.add(ds.getValue(Anuncio.class));
                }
                Collections.reverse(anuncios);
                adapterMeusAnuncios.notifyDataSetChanged();

                dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void alerdDialogEscluirAnuncio (final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Excluir anúncio");
        builder.setMessage("Tem certeza que deseja excluir esse anuncio? ");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Anuncio anuncioSelecionado = anuncios.get(position);
                anuncioSelecionado.excluirAnuncio();

                adapterMeusAnuncios.notifyDataSetChanged();
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

    private void inicializarComponentes() {
        recyclerAnuncios = findViewById(R.id.recyclerMeusAnuncios2);
        fab = findViewById(R.id.floatingActionCadAnuncio);
    }
}
