package lanchefacil.dalksoft.com.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Toast;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lanchefacil.dalksoft.com.R;
import lanchefacil.dalksoft.com.adapter.AdapterMeusAnuncios;
import lanchefacil.dalksoft.com.helper.ConfigFireBase;
import lanchefacil.dalksoft.com.helper.RecyclerItemClickListener;
import lanchefacil.dalksoft.com.model.Anuncio;
import lanchefacil.dalksoft.com.model.Usuarios;
import me.drakeet.materialdialog.MaterialDialog;

public class AdminActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao = ConfigFireBase.getFirebaseAuth();
    public static final String TAG = "LOG";
    public static final int REQUEST_PERMISSIONS_CODE = 128;
    private MaterialDialog mMaterialDialog;
    private RecyclerView recyclerAnunciosPublicos;
    private SearchView pesquisa;
    private AdapterMeusAnuncios adapterMeusAnuncios;
    private DatabaseReference anunciosPublicosRef;
    private List<Anuncio> listaAnuncios = new ArrayList<>();
    private SweetAlertDialog pDialog;
    private Anuncio anuncio;
    Usuarios usuario = new Usuarios();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        anunciosPublicosRef = ConfigFireBase.getFirebase().child("anuncios");
        anuncio = new Anuncio();
        inicializarComponentes();

        exibirAnuncios();
    }

    public void exibirAnuncios () {
        recyclerAnunciosPublicos.setHasFixedSize(true);
        recyclerAnunciosPublicos.setLayoutManager(new LinearLayoutManager(this));
        adapterMeusAnuncios = new AdapterMeusAnuncios(listaAnuncios,this);
        recyclerAnunciosPublicos.setAdapter(adapterMeusAnuncios);
        recuperarAnunciosPublicos();

        recyclerAnunciosPublicos.addOnItemTouchListener(new RecyclerItemClickListener(
                this,
                recyclerAnunciosPublicos,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
//                        Anuncio anuncioSelecionado = listaAnuncios.get(position);
//                        Intent i = new Intent(AdminActivity.this, DetalhesAnuncioActivity.class);
//                        i.putExtra("anuncioSelecionado", anuncioSelecionado);
//                        startActivity(i);
                    }

                    @Override
                    public void onLongItemClick(View view, final int position) {
                        new SweetAlertDialog(AdminActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Excluir Anúncio")
                                .setContentText("Tem certeza que deseja excluir este anúncio?")
                                .setConfirmText("SIM")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                      Anuncio anuncioSelecionado = listaAnuncios.get(position);
                                      String id = anuncioSelecionado.getIdUsuario();
                                      anuncioSelecionado.setIdUsuario(id);
                                      anuncioSelecionado.excluirAnuncio();

                                      anuncioSelecionado.setStatus("Status: Excluido por moderador");
                                      anuncioSelecionado.atualizarStatus();
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .setCancelText("NÃO")
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.cancel();
                                    }
                                })
                                .show();
                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));
    }

    public void recuperarAnunciosPublicos () {
        pDialog = new SweetAlertDialog(AdminActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Carregando");
        pDialog.setCancelable(false);
        pDialog.show();
        anunciosPublicosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaAnuncios.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    listaAnuncios.add(ds.getValue(Anuncio.class));
                }
                Collections.reverse(listaAnuncios);
                adapterMeusAnuncios.notifyDataSetChanged();
                pDialog.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void inicializarComponentes() {
        usuario = new Usuarios();
        recyclerAnunciosPublicos = findViewById(R.id.recyclerAdminAcuncios);

    }

    private void alerta (String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }
}

