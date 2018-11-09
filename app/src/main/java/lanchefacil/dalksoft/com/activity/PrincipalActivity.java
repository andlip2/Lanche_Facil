package lanchefacil.dalksoft.com.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SearchView;
//import android.widget.TextView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.takusemba.spotlight.OnSpotlightStateChangedListener;
import com.takusemba.spotlight.OnTargetStateChangedListener;
import com.takusemba.spotlight.Spotlight;
import com.takusemba.spotlight.shape.Circle;
import com.takusemba.spotlight.target.SimpleTarget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import lanchefacil.dalksoft.com.R;
import lanchefacil.dalksoft.com.adapter.AdapterMeusAnuncios;
import lanchefacil.dalksoft.com.helper.ConfigFireBase;
import lanchefacil.dalksoft.com.helper.Permissoes;
import lanchefacil.dalksoft.com.helper.RecyclerItemClickListener;
import lanchefacil.dalksoft.com.helper.RoundRectangle;
import lanchefacil.dalksoft.com.helper.UsuarioFirebase;
import lanchefacil.dalksoft.com.model.Anuncio;
import lanchefacil.dalksoft.com.model.Usuarios;
import me.drakeet.materialdialog.MaterialDialog;

import static android.provider.CalendarContract.CalendarCache.URI;

public class PrincipalActivity extends AppCompatActivity


        implements NavigationView.OnNavigationItemSelectedListener{


    private FirebaseAuth autenticacao = ConfigFireBase.getFirebaseAuth();
    public static final String TAG = "LOG";
    public static final int REQUEST_PERMISSIONS_CODE = 128;
    private MaterialDialog mMaterialDialog;
    FloatingActionButton fab, fab2;
    private RecyclerView recyclerAnunciosPublicos;
    private SearchView pesquisa;
    private AdapterMeusAnuncios adapterMeusAnuncios;
    private DatabaseReference anunciosPublicosRef;
    private List<Anuncio> listaAnuncios = new ArrayList<>();
    private SweetAlertDialog pDialog;
    private TextView txtNome, txtEmail;
    private ImageView imgPerfil;
    private Spotlight spotlight;
//    private String filtroTitulo ="";
//    private Anuncio anuncio = new Anuncio();
    Usuarios usuario = new Usuarios();
//    private TextView menuEmail, menuNome;
    private CircleImageView menuIMGPerfil;
//    private String listaImgRecuperadas;
//    private List<String> listaURLFotos = new ArrayList<>();
    StorageReference storage;
    private boolean duploclique;
    private String [] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbarEditAnuncio);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Lanche Fácil");

        anunciosPublicosRef = ConfigFireBase.getFirebase().child("anuncios");
//        FirebaseUser user = UsuarioFirebase.getUsuarioAtual();
        storage = ConfigFireBase.getReferenciaStorage();
        Permissoes.validarPermissoes(permissoes, this,1);

        //Solicitar permição ao GPS
        alerdDialogPermissaoGPS();

        inicializarComponentes ();



        //iniciar tela para cadastrar anuncios
        fab = findViewById(R.id.fab);
        if (autenticacao.getCurrentUser() != null){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent i = new Intent(PrincipalActivity.this, CadastrarAnuncioActivity.class);
                    startActivity(i);


            }
        });}else {
            finish();
            Intent i = new Intent(PrincipalActivity.this, MainActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // adiciona a flag para a intent
            startActivity(i);
            fab.setVisibility(View.GONE);
        }

        //Verificar se o usuario está logado
        if (autenticacao.getCurrentUser() != null) {
            //Codigos gerados automaticos abaixo
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView =  findViewById(R.id.nav_view);
            View view = navigationView.getHeaderView(0);
            txtNome = view.findViewById(R.id.textPrincipalNome);
            txtEmail = view.findViewById(R.id.textPrincipalEmail);
            imgPerfil = view.findViewById(R.id.imagePrincipalPerfil);


            FirebaseUser user = UsuarioFirebase.getUsuarioAtual();
            Uri url = user.getPhotoUrl();
                Glide.with(PrincipalActivity.this)
                        .load(url)
                        .into(imgPerfil);
            txtEmail.setText(user.getEmail());
            txtNome.setText(user.getDisplayName());

            navigationView.setNavigationItemSelectedListener(this);
        }



        //Acho q o nome do metodo já diz tudo né
        exibirAnuncios();
        pesquisar();


    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instrucoes();
            }
        });
    }

    private void instrucoes() {

        final SimpleTarget simpleTarget = new SimpleTarget.Builder(PrincipalActivity.this)
                .setPoint(recyclerAnunciosPublicos)
                .setShape(new RoundRectangle(pesquisa.getLeft(), pesquisa.getTop(), pesquisa.getWidth(), pesquisa.getHeight()))
                .setTitle("teste")
                .setDescription("Descrição teste")
                .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
                    @Override
                    public void onStarted(SimpleTarget target) {

                    }

                    @Override
                    public void onEnded(SimpleTarget target) {

                    }
                }).build();

        spotlight = Spotlight.with(PrincipalActivity.this)
                .setOverlayColor(android.R.color.holo_red_light)
                .setDuration(200L)
                .setAnimation(new DecelerateInterpolator(2f))
                .setTargets(simpleTarget)
                .setClosedOnTouchedOutside(true)
                .setOnSpotlightStateListener(new OnSpotlightStateChangedListener() {
                    @Override
                    public void onStarted() {

                    }

                    @Override
                    public void onEnded() {

                    }
                });
        spotlight.start();

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
        pDialog = new SweetAlertDialog(PrincipalActivity.this, SweetAlertDialog.PROGRESS_TYPE);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (autenticacao.getCurrentUser() != null) {
            int id = item.getItemId();
            if (id == R.id.menu_perfil) {
                Intent i = new Intent(PrincipalActivity.this, PerfilActivity.class);
                startActivity(i);
            } else if (id == R.id.menu_anuncios) {
                Intent i = new Intent(PrincipalActivity.this, AnunciosUsuarioActivity.class);
                startActivity(i);
            }
//        else if (id == R.id.menu_pedidos) {
//            Intent i = new Intent(PrincipalActivity.this, MeusPedidosActivity.class);
//            startActivity(i);
//
//        }
            else if (id == R.id.menu_favoritos) {
                Intent i = new Intent(PrincipalActivity.this, FavoritosActivity.class);
                startActivity(i);
            } else if (id == R.id.menu_ajuda) {
                Intent i = new Intent(PrincipalActivity.this, AjudaActivity.class);
                startActivity(i);
            } else if (id == R.id.menu_sair) {
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Desconectar conta")
                        .setContentText("Você realmente deseja sair da sua conta?")
                        .setCancelText("Não")
                        .setConfirmText("Sim")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .showConfirmButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                                FirebaseAuth.getInstance().signOut();
                                LoginManager.getInstance().logOut();
                                finish();
                                Intent i = new Intent(PrincipalActivity.this, MainActivity.class);
                                startActivity(i);
                                alerta("Usuario desconectado!");
                            }
                        })
                        .show();
            }

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }else {
            item.setVisible(false);
            alerta("Faça login para acessar");
        }
        return true;
    }

    //Verifica/solicita permissão ao GPS
    public void alerdDialogPermissaoGPS() {
        Log.i (TAG,"callAccessLocation()");

        if( ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ){

            if( ActivityCompat.shouldShowRequestPermissionRationale( this, Manifest.permission.ACCESS_FINE_LOCATION ) ){
                callDialog( "É preciso que seja altorizada a permissão ao GPS para poder realizar cadastro de anuncios.", new String[]{Manifest.permission.ACCESS_FINE_LOCATION} );
            }
            else{
                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_CODE );
            }
        }
    }

    //Configura a caixa para aceitar permissão
    private void callDialog( String message, final String[] permissions ){
        new SweetAlertDialog(PrincipalActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Permission")
                .setContentText(message)
                .setConfirmText("ACEITAR")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        ActivityCompat.requestPermissions(PrincipalActivity.this, permissions, REQUEST_PERMISSIONS_CODE);

                        sDialog.cancel();
                    }
                })
                .show();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && null != data) {
//
//            Uri selectedImage = data.getData();
//            String[] filePathColumn = { MediaStore.Images.Media.DATA };
//            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
//            cursor.close();
//
//            menuIMGPerfil.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//        }
//    }


    private void inicializarComponentes() {
        usuario = new Usuarios();
        recyclerAnunciosPublicos = findViewById(R.id.recyclerPricipalAcuncios);
        pesquisa = findViewById(R.id.searchPrincipalPesquisa);
        menuIMGPerfil = findViewById(R.id.imagePefilFoto);
        fab2 = findViewById(R.id.floatingPrincipalAjuda);

    }

    private void alerta (String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }
}