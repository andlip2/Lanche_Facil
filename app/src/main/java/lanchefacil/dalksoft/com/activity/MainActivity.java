package lanchefacil.dalksoft.com.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Toast;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lanchefacil.dalksoft.com.R;
import lanchefacil.dalksoft.com.helper.AdapterMeusAnuncios;
import lanchefacil.dalksoft.com.helper.ConfigFireBase;
import lanchefacil.dalksoft.com.helper.RecyclerItemClickListener;
import lanchefacil.dalksoft.com.model.Anuncio;
import lanchefacil.dalksoft.com.model.Usuarios;
import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends AppCompatActivity {

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
    Usuarios usuario = new Usuarios();
    StorageReference storage;
    private boolean duploclique;
//    private String [] permissoes = new String[]{
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.CAMERA,
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        anunciosPublicosRef = ConfigFireBase.getFirebase().child("anuncios");
        storage = ConfigFireBase.getReferenciaStorage();
//        Permissoes.validarPermissoes(permissoes, this,1);

        //Solicitar permição ao GPS
//        callAccessLocation();

        inicializarComponentes();

        exibirAnuncios();
        pesquisar();

    }

    public void pesquisar () {
        pesquisa.setQueryHint("Buscar Anúncios");
        pesquisa.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String txtDigitado = newText.toUpperCase();
                pesquisarAnuncios (txtDigitado);
                return true;
            }
        });
        pesquisa.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                recuperarAnunciosPublicos();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (duploclique) {
            super.onBackPressed();
        }
        this.duploclique = true;
        Toast.makeText(this, "Aperte novamente para sair", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                duploclique = false;
            }
        }, 2000);
    }


    private void pesquisarAnuncios(String txtDigitado) {
        listaAnuncios.clear();

        if (txtDigitado.length() >=2) {
            Query query = anunciosPublicosRef.orderByChild("cidade_pesquisa")
                    .startAt(txtDigitado)
                    .endAt(txtDigitado + "\uf8ff");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
//                    listaAnuncios.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        listaAnuncios.add(ds.getValue(Anuncio.class));
                    }

                    adapterMeusAnuncios.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    alerta("não foi encontrado");
                }
            });
            Query query2 = anunciosPublicosRef.orderByChild("titulo_pesquisa")
                    .startAt(txtDigitado)
                    .endAt(txtDigitado + "\uf8ff");
            query2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
//                    listaAnuncios.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        listaAnuncios.add(ds.getValue(Anuncio.class));
                    }

                    adapterMeusAnuncios.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    alerta("não foi encontrado");
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Codigos q eu escrevi, Config_Menu_Logar
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (autenticacao.getCurrentUser() == null){
            menu.setGroupVisible(R.id.group_deslogado, true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cadastrar:
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
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
                        Anuncio anuncioSelecionado = listaAnuncios.get(position);
                        Intent i = new Intent(MainActivity.this, DetalhesAnuncioActivity.class);
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
        pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
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

//    //Verifica/solicita permissão ao GPS
//    public void callAccessLocation() {
//        Log.i (TAG,"callAccessLocation()");
//
//        if( ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ){
//
//            if( ActivityCompat.shouldShowRequestPermissionRationale( this, Manifest.permission.ACCESS_FINE_LOCATION ) ){
//                callDialog( "É preciso a permission ACCESS_FINE_LOCATION para apresentação dos eventos locais.", new String[]{Manifest.permission.ACCESS_FINE_LOCATION} );
//            }
//            else{
//                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_CODE );
//            }
//        }
//    }
//
//    //Configura a caixa para aceitar permissão
//    private void callDialog( String message, final String[] permissions ){
//        mMaterialDialog = new MaterialDialog(this)
//                .setTitle("Permission")
//                .setMessage( message )
//                .setPositiveButton("Ok", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        ActivityCompat.requestPermissions(MainActivity.this, permissions, REQUEST_PERMISSIONS_CODE);
//                        mMaterialDialog.dismiss();
//                    }
//                })
//                .setNegativeButton("Cancel", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mMaterialDialog.dismiss();
//                    }
//                });
//        mMaterialDialog.show();
//    }

    private void inicializarComponentes() {
        usuario = new Usuarios();
        recyclerAnunciosPublicos = findViewById(R.id.recyclerMainAcuncios);
        pesquisa = findViewById(R.id.searchMainPesquisa);

    }

    private void alerta (String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }
}
