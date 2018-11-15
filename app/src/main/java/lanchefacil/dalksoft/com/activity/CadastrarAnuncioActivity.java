package lanchefacil.dalksoft.com.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.blackcat.currencyedittext.CurrencyEditText;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.santalu.widget.MaskEditText;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import lanchefacil.dalksoft.com.R;
import lanchefacil.dalksoft.com.helper.ConfigFireBase;
import lanchefacil.dalksoft.com.helper.Permissoes;
import lanchefacil.dalksoft.com.helper.UsuarioFirebase;
import lanchefacil.dalksoft.com.model.Anuncio;

public class CadastrarAnuncioActivity extends AppCompatActivity
        implements View.OnClickListener{


    private EditText editCEP, editEndereco, editTitulo, editDescricao;
    private CurrencyEditText editValor;
    private MaskEditText editTelefone;
    private ImageView imagem1, imagem2, imagem3;
    private Button buttonGPS, cancelarCadastro;
    public static final int REQUEST_PERMISSIONS_CODE = 128;
    private LocationManager mLocalizacao;
    protected Location localizacao;
    private Address endereco;
    private Anuncio anuncio;
    private StorageReference storage;
    private SweetAlertDialog pDialog;
    private String [] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };
    private List<String> listaImgRecuperadas = new ArrayList<>();
    private List<String> listaURLFotos = new ArrayList<>();
    private List<String> image01 = new ArrayList<>();
    private List<String> image02 = new ArrayList<>();
    private List<String> image03 = new ArrayList<>();
    private double latitude =0.0;
    private double longitude = 0.0;
    private String cidade;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_anuncio);

        Permissoes.validarPermissoes(permissoes, this,1);

        mLocalizacao = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                latitude = location.getLatitude();
                longitude = location.getLongitude();

                try {

                    List<Address> listaEndereco = geocoder.getFromLocation(latitude, longitude, 1);
                    if (listaEndereco !=null && listaEndereco.size() >0) {
                        Address endereco = listaEndereco.get(0);
                        editCEP.setText(endereco.getPostalCode());
                        editEndereco.setText(endereco.getAddressLine(0));
                        cidade = endereco.getLocality();
                        pDialog.dismiss();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    alerta("Erro ao recuperar localização");
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        iniciarComponentes();

        storage = ConfigFireBase.getReferenciaStorage();

        getSupportActionBar().setTitle("Cadastrar Anúncio");

        cancelarCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        anuncio = new Anuncio();

        alerdDialogPermissaoGPS();

    }

    private void geolocalizacao() {
        if (ActivityCompat.checkSelfPermission(CadastrarAnuncioActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            pDialog = new SweetAlertDialog(CadastrarAnuncioActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Checando Localização");
            pDialog.setCancelable(false);
            pDialog.show();
            mLocalizacao.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    10000,
                    5,
                    locationListener);
        }else {


       }

    }


    private Anuncio configurarAnuncio () {
        String titulo = editTitulo.getText().toString();
        String cep = editCEP.getText().toString();
        String endereco = editEndereco.getText().toString();
        String valor = editValor.getText().toString();
        String telefone = editTelefone.getText().toString();
        String descricao = editDescricao.getText().toString();
        FirebaseUser user = UsuarioFirebase.getUsuarioAtual();

        Anuncio anuncio = new Anuncio();
        anuncio.setTitulo(titulo);
        anuncio.setTitulo_pesquisa(titulo.toUpperCase());
        anuncio.setCep(cep);
        anuncio.setEndereco(endereco);
        anuncio.setValor(valor);
        anuncio.setTelefone(telefone);
        anuncio.setDescricao(descricao);
        anuncio.setLatitude(latitude);
        anuncio.setLongitude(longitude);
        anuncio.setCidade(cidade);
        anuncio.setIdUsuario(user.getUid());

        return anuncio;
    }

    public void validarDadosAnuncio (View view) {


        String valor = String.valueOf(editValor.getRawValue());

        String fone = "";
        if (editTelefone.getRawText() != null) {
            fone = editTelefone.getRawText().toString();
        }
        anuncio = configurarAnuncio();
        add(image01,image02,image03);
        if (listaImgRecuperadas.size() != 0){
            if (!anuncio.getCep().isEmpty()){
                if (!anuncio.getEndereco().isEmpty()){
                    if (!anuncio.getTitulo().isEmpty()){
                        if (!valor.isEmpty() && !valor.equals("0")){
                            if (!anuncio.getTelefone().isEmpty()){
                                if (fone.length() >=11) {
                                    if (!anuncio.getDescricao().isEmpty()){
                                        salvarAnuncio();
                                    }else {
                                        alerta("Defina a descrição do anúncio");
                                    }
                                }else {
                                    alerta("O numero digitado não é valido");
                                }
                            }else {
                                alerta("Defina o telefone do anúncio");
                            }
                        }else {
                            alerta("Defina o valor anúncio");
                        }
                    }else {
                        alerta("Defina o titulo do anúncio");
                    }
                }else {
                    alerta("Defina o CEP do anúncio");
                }
            }else {
                alerta("Defina o endereço do anúncio");
            }
        }else {
            alerta("Você precisa adicionar ao menos uma foto!");
        }

    }

    public void salvarAnuncio () {

        pDialog = new SweetAlertDialog(CadastrarAnuncioActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Salvando");
        pDialog.setCancelable(false);
        pDialog.show();

        //Salvar imagens
        for (int i=0; i< listaImgRecuperadas.size(); i++) {
            String urlIMG = listaImgRecuperadas.get(i);
            int tamanho = listaImgRecuperadas.size();
            salvarImagens (urlIMG, tamanho, i);
        }

    }





    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imageCadAnuncio1:
                escolherImagem(1);
                break;
            case R.id.imageEditarAnuncio2:
                escolherImagem(2);
                break;
            case R.id.imageEditarAnuncio3:
                escolherImagem(3);
                break;
            case R.id.buttonEditarAnuncioGPS:
                geolocalizacao ();
                break;

        }
    }

    private void escolherImagem(int requestCode) {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, requestCode);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Uri imagemSelecionada = data.getData();
            String caminhoImagem = imagemSelecionada.toString();

            int i =0;

            if (requestCode == 1) {
                imagem1.setImageURI(imagemSelecionada);
                image01.clear();
                image01.add(caminhoImagem);
                i++;
            }else if (requestCode == 2) {
                imagem2.setImageURI(imagemSelecionada);
                image02.clear();
                image02.add(caminhoImagem);
            }else if ( requestCode == 3) {
                imagem3.setImageURI(imagemSelecionada);
                image03.clear();
                image03.add(caminhoImagem);
            }
        }


    }

    private void add (List<String> image01, List<String> image02, List<String> image03) {
        if (!image01.isEmpty()) {
            listaImgRecuperadas.add(0,"");
            listaImgRecuperadas.remove(0);
            listaImgRecuperadas.add(0, image01.get(0));

        }
        if (!image02.isEmpty()){
            listaImgRecuperadas.add(1, image02.get(0));
        }
        if (!image03.isEmpty()){
            listaImgRecuperadas.add(2, image03.get(0));
        }
    }

    private void salvarImagens(String urlIMG, final int totalIMG, int contador) {
        //criando nó no banco
        StorageReference imgAnuncio = storage.child("imagens")
                .child("anuncios")
                .child(anuncio.getIdAnuncio())
                .child("imagem"+contador+".jpeg");

        //enviar imagem e anuncio
        UploadTask uploadTask = imgAnuncio.putFile(Uri.parse(urlIMG));
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Uri firebaseUrl = taskSnapshot.getDownloadUrl();
                String urlConvertida = firebaseUrl.toString();
                listaURLFotos.add(urlConvertida);

                if (totalIMG == listaURLFotos.size()) {
                    anuncio.setFotos(listaURLFotos);
                    anuncio.setStatus("Status: Ativo");
                    //Salvar anuncio
                    anuncio.salvar();

                    //finaliza carregamento
                    pDialog.dismiss();
                    finish();
                }else {

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                alerta("Falha ao fazer upload da imagem");
                Log.i("INFO", "Falha ao fazer upload: " + e.getMessage());
            }
        });

    }

    private void iniciarComponentes () {
        buttonGPS = findViewById(R.id.buttonEditarAnuncioGPS);
        buttonGPS.setOnClickListener(this);
        editCEP = findViewById(R.id.editAnuncioCP);
        editCEP.setFocusable(false);
        editEndereco = findViewById(R.id.editAnuncioRua);
        editEndereco.setFocusable(false);
        editTitulo = findViewById(R.id.editAnuncioTitulo);
        editDescricao = findViewById(R.id.editAnuncioDescricao);
        cancelarCadastro = findViewById(R.id.buttonCadAnuncioCancelar);

        editValor = findViewById(R.id.editEditarAnuncioValor);
        //configurar localidade para pt -> portugues BR -> Brasil
        Locale locale = new Locale ("pt", "BR");
        editValor.setLocale(locale);

        editTelefone = findViewById(R.id.editEditarAnuncioTelefone);
        imagem1 = findViewById(R.id.imageCadAnuncio1);
        imagem1.setOnClickListener(this);
        imagem2 = findViewById(R.id.imageEditarAnuncio2);
        imagem2.setOnClickListener(this);
        imagem3 = findViewById(R.id.imageEditarAnuncio3);
        imagem3.setOnClickListener(this);





    }


    //Verifica as permissões de galeria
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int permissaoResult : grantResults) {
            if (permissaoResult == PackageManager.PERMISSION_DENIED) {
                alerdDialogPermissaoGaleria();
            }
        }
    }

    private void alerdDialogPermissaoGaleria () {
        new SweetAlertDialog(CadastrarAnuncioActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Permissões Negadas")
                .setContentText("Para cadastrar um anúncio é necessário aceitar as permissões")
                .setCancelText(null)
                .setCancelClickListener(null)
                .setConfirmText("ACEITAR")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        finish();

                        sDialog.cancel();
                    }
                })
                .show();
    }

    public void alerdDialogPermissaoGPS() {
        Log.i ("LOG","callAccessLocation()");

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
        new SweetAlertDialog(CadastrarAnuncioActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Permission")
                .setContentText(message)
                .setConfirmText("ACEITAR")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        ActivityCompat.requestPermissions(CadastrarAnuncioActivity.this, permissions, REQUEST_PERMISSIONS_CODE);

                        sDialog.cancel();
                    }
                })
                .show();
    }
    private void alerta (String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }
}