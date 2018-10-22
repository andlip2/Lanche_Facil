package lanchefacil.dalksoft.com.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import lanchefacil.dalksoft.com.R;
import lanchefacil.dalksoft.com.adapter.AdapterMeusAnuncios;
import lanchefacil.dalksoft.com.helper.ConfigFireBase;
import lanchefacil.dalksoft.com.helper.Permissoes;
import lanchefacil.dalksoft.com.helper.RecyclerItemClickListener;
import lanchefacil.dalksoft.com.helper.UsuarioFirebase;
import lanchefacil.dalksoft.com.model.Anuncio;
import lanchefacil.dalksoft.com.model.Usuarios;
import me.drakeet.materialdialog.MaterialDialog;

public class PrincipalActivity extends AppCompatActivity


        implements NavigationView.OnNavigationItemSelectedListener{


    private FirebaseAuth autenticacao = ConfigFireBase.getFirebaseAuth();
    public static final String TAG = "LOG";
    public static final int REQUEST_PERMISSIONS_CODE = 128;
    private MaterialDialog mMaterialDialog;
    private FloatingActionButton fab;
    private RecyclerView recyclerAnunciosPublicos;
    private SearchView pesquisa;
    private AdapterMeusAnuncios adapterMeusAnuncios;
    private DatabaseReference anunciosPublicosRef;
    private List<Anuncio> listaAnuncios = new ArrayList<>();
    private AlertDialog dialog;
    private String filtroTitulo ="";
    private Anuncio anuncio = new Anuncio();
    private Usuarios usuario = new Usuarios();
    private TextView menuEmail, menuNome;
    private CircleImageView menuIMGPerfil;
    private String listaImgRecuperadas;
    private List<String> listaURLFotos = new ArrayList<>();
    private StorageReference storage;
    private String [] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        anunciosPublicosRef = ConfigFireBase.getFirebase().child("anuncios");
        FirebaseUser user = UsuarioFirebase.getUsuarioAtual();
        storage = ConfigFireBase.getReferenciaStorage();
        Permissoes.validarPermissoes(permissoes, this,1);

        //Solicitar permição ao GPS
        callAccessLocation();

        inicializarComponentes ();

        //iniciar tela para cadastrar anuncios
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        if (autenticacao.getCurrentUser() != null){
                    Intent i = new Intent(PrincipalActivity.this, CadastrarAnuncioActivity.class);
                    startActivity(i);

        }else {
            alerta("Você precisa está logado para cadastrar um anúncio!");
        }
            }
        });

        //Verificar se o usuario está logado
        if (autenticacao.getCurrentUser() != null) {
            //Codigos gerados automaticos abaixo
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView =  findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        }

        //Acho q o nome do metodo já diz tudo né
        exibirAnuncios();
        pesquisar();

//        Uri url = user.getPhotoUrl();
//        if (url != null) {
//            Glide.with(PrincipalActivity.this)
//                    .load(url)
//                    .into(menuIMGPerfil);
//        }else {
//            menuIMGPerfil.setImageResource(R.drawable.padrao);
//
//
//        }

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

    private void pesquisarAnuncios(String txtDigitado) {
        listaAnuncios.clear();

        if (txtDigitado.length() >=2) {
            Query query = anunciosPublicosRef.orderByChild("titulo_pesquisa")
                    .startAt(txtDigitado)
                    .endAt(txtDigitado + "\uf8ff");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    listaAnuncios.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        listaAnuncios.add(ds.getValue(Anuncio.class));
                    }

                    adapterMeusAnuncios.notifyDataSetChanged();
//                    int total = listaAnuncios.size();
//                    Log.i("totalAnuncios","Total: " + total);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
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
                        Intent i = new Intent(PrincipalActivity.this, DetalhesAnuncioActivity.class);
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


    //Codigos q eu escrevi, Config_Menu_Logar
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
        }else {

        }

        return super.onPrepareOptionsMenu(menu);
    }

        @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cadastrar:
                    Intent i = new Intent(PrincipalActivity.this, LoginActivity.class);
                    startActivity(i);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

            int id = item.getItemId();
        if (id == R.id.menu_perfil) {
            Intent i = new Intent(PrincipalActivity.this, PerfilActivity.class);
            startActivity(i);
        }
     else if (id == R.id.menu_anuncios) {
        Intent i = new Intent(PrincipalActivity.this, AnunciosUsuarioActivity.class);
        startActivity(i);
        }
        else if (id == R.id.menu_pedidos) {
            Intent i = new Intent(PrincipalActivity.this, MeusPedidosActivity.class);
            startActivity(i);

        }
         else if (id == R.id.menu_favoritos) {
            Intent i = new Intent(PrincipalActivity.this, FavoritosActivity.class);
            startActivity(i);}
//        } else if (id == R.id.menu_config) {
//            Intent i = new Intent(PrincipalActivity.this, ConfigiracaoActivity.class);
//            startActivity(i);
//        }
        else if (id == R.id.menu_ajuda) {
            Intent i = new Intent(PrincipalActivity.this, AjudaActivity.class);
            startActivity(i);
        }
           else if (id == R.id.menu_sair) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                finish();
                Intent i = new Intent(PrincipalActivity.this, PrincipalActivity.class);
                startActivity(i);
                alerta("Usuario desconectado!");
            }

            DrawerLayout drawer =  findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    //Verifica/solicita permissão ao GPS
    public void callAccessLocation() {
        Log.i (TAG,"callAccessLocation()");

        if( ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ){

            if( ActivityCompat.shouldShowRequestPermissionRationale( this, Manifest.permission.ACCESS_FINE_LOCATION ) ){
                callDialog( "É preciso a permission ACCESS_FINE_LOCATION para apresentação dos eventos locais.", new String[]{Manifest.permission.ACCESS_FINE_LOCATION} );
            }
            else{
                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_CODE );
            }
        }
        else{

        }
    }

    //Configura a caixa para aceitar permissão
    private void callDialog( String message, final String[] permissions ){
        mMaterialDialog = new MaterialDialog(this)
                .setTitle("Permission")
                .setMessage( message )
                .setPositiveButton("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ActivityCompat.requestPermissions(PrincipalActivity.this, permissions, REQUEST_PERMISSIONS_CODE);
                        mMaterialDialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
        mMaterialDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            menuIMGPerfil.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }


    private void inicializarComponentes() {
        recyclerAnunciosPublicos = findViewById(R.id.recyclerPricipalAcuncios);
        pesquisa = findViewById(R.id.searchPrincipalPesquisa);
        menuEmail = findViewById(R.id.textMenuPrincipalEmail);
//        menuEmail.setText(autenticacao.getCurrentUser().toString());
        menuNome = findViewById(R.id.textMenuPrincipalNome);
        menuIMGPerfil = findViewById(R.id.imagePefilFoto);

    }

    private void alerta (String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }
}