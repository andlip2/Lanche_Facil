package lanchefacil.dalksoft.com.activity;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.santalu.widget.MaskEditText;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import lanchefacil.dalksoft.com.R;
import lanchefacil.dalksoft.com.adapter.AdapterAnunciosUsuario;
import lanchefacil.dalksoft.com.helper.ConfigFireBase;
import lanchefacil.dalksoft.com.helper.Permissoes;
import lanchefacil.dalksoft.com.model.Anuncio;

public class EditarAnuncioActivity extends AppCompatActivity
        implements View.OnClickListener
{


    private EditText editCEP, editEndereco, editTitulo, editDescricao;
    private CurrencyEditText editValor;
    private MaskEditText editTelefone;
    private static final int SELECAO_GALERIA = 200;
    private ImageView imagem1, imagem2, imagem3;
    private Button buttonGPS, buttonAtualizar, buttonOcutar;
    private LocationManager mLocalizacao;
    protected Location localizacao;
    private Address endereco;
    private Anuncio anuncio;
    private StorageReference storage;
    private AlertDialog dialog;
    private String [] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };
    private List<String> listaImgRecuperadas = new ArrayList<>();
    private List<String> listaURLFotos  = new ArrayList<>();
    private List<String> image01 = new ArrayList<>();
    private List<String> image02 = new ArrayList<>();
    private List<String> image03 = new ArrayList<>();
    int teste =0;
    int teste2 =0;
    private double latitude =0.0;
    private double longitude = 0.0;
    private String cidade;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_anuncio);
//        Toolbar toolbar = findViewById(R.id.toolbarEditAnuncio);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Editar Anúncio");

        Permissoes.validarPermissoes(permissoes, this,1);
//
        iniciarComponentes();
//
        storage = ConfigFireBase.getReferenciaStorage();

//      Recuperando e enviando dados
        anuncio = (Anuncio) getIntent().getSerializableExtra("anuncioSelecionado");
        if (anuncio != null) {
            editTitulo.setText(anuncio.getTitulo());
            editCEP.setText(anuncio.getCep());
            editDescricao.setText(anuncio.getDescricao());
            editEndereco.setText(anuncio.getEndereco());
            editTelefone.setText(anuncio.getTelefone());
            editValor.setText(anuncio.getValor());
            cidade = anuncio.getCidade();

            if (anuncio.getStatus().equals("Status: Inativo")) {
                buttonAtualizar.setText("ATIVAR ANÚNCIO");
            }

            //Recuperar IMG
            recuperarFotos();
            }
        }

        public void recuperarFotos () {

            List<String> listaURLFotosSalvas;

            listaURLFotosSalvas = anuncio.getFotos();

        for (int cont = 0; cont <= listaURLFotosSalvas.size()-1; cont++){
            if (cont == 0 ) {
                String url = listaURLFotosSalvas.get(0);
                Picasso.get().load(url).into(imagem1);

            }
            else if (cont == 1) {
                String url1 = listaURLFotosSalvas.get(1);
                Picasso.get().load(url1).into(imagem2);

            }
            else if (cont == 2){
                String url2 = listaURLFotosSalvas.get(2);
                Picasso.get().load(url2).into(imagem3);
            }
            listaImgRecuperadas = listaURLFotosSalvas;

        }
        }

        private void geolocalizacao() {
            if (ActivityCompat.checkSelfPermission(EditarAnuncioActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(EditarAnuncioActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }else {
                mLocalizacao = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                localizacao = mLocalizacao.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            if (localizacao != null) {
                longitude = localizacao.getLongitude();
                latitude = localizacao.getLatitude();
            }

            try {
                endereco = buscarEndereco(latitude, longitude);
                editCEP.setText(endereco.getPostalCode());
                editEndereco.setText(endereco.getAddressLine(0));
                cidade = endereco.getLocality();
            } catch (IOException e) {
                alerta("Erro ao recuperar localização");
            }
        }

        private Address buscarEndereco(double latitude, double longitude) throws IOException {
            Geocoder geocoder;
            Address address = null;
            List<Address> addresses;

            geocoder = new Geocoder(getApplicationContext());

            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses.size() >0) {
                address = addresses.get(0);
            }
            return address;
        }


    public void validarDados(View view) {

        String valor = String.valueOf(editValor.getRawValue());

        String fone = "";
        if (editTelefone.getRawText() != null) {
            fone = editTelefone.getRawText().toString();
        }
            if (!anuncio.getTitulo().isEmpty()){
                    if (!anuncio.getCep().isEmpty()){
                        if (!anuncio.getEndereco().isEmpty()){
                            if (!valor.isEmpty() && !valor.equals("0")){
                                if (!anuncio.getTelefone().isEmpty()){
                                    if (fone.length() >=11) {
                                        if (!anuncio.getDescricao().isEmpty()){

                                            add(image01,image02,image03);

                                            atualizarAnuncio();
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
                                alerta("Defina o valor do anúncio");
                            }
                        }else {
                            alerta("Defina o endereço do anúncio");
                        }
                    }else {
                        alerta("Defina o CEP do anúncio");
                    }
            }else {
                alerta("Defina o titulo do anúncio");
            }

        }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imageEditAnuncio1:
                escolherImagem(1);
                break;
            case R.id.imageEditAnuncio2:
                escolherImagem(2);
                break;
            case R.id.imageEditAnuncio3:
                escolherImagem(3);
                break;
            case R.id.buttonEditAnuncioGPS:
                geolocalizacao ();
                break;
            case R.id.buttonEditAnuncioOcutar:
                alerdDialogOcutarAnuncio();
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
            if (requestCode == 1) {
                teste++;
                imagem1.setImageURI(imagemSelecionada);
                image01.clear();
                image01.add(caminhoImagem);
            }else if (requestCode == 2) {
                teste++;
                imagem2.setImageURI(imagemSelecionada);
                image02.clear();
                image02.add(caminhoImagem);
            }else if ( requestCode == 3) {
                teste++;
                imagem3.setImageURI(imagemSelecionada);
                image03.clear();
                image03.add(caminhoImagem);
            }
        }


    }

    private void add (List<String> image01, List<String> image02, List<String> image03) {
        listaImgRecuperadas.clear();
        if (!image01.isEmpty()) {
            listaImgRecuperadas.add(0,"");
            listaImgRecuperadas.remove(0);
            listaImgRecuperadas.add(0, image01.get(0));

        }
        if (!image02.isEmpty()){
            listaImgRecuperadas.add(1,"");
            listaImgRecuperadas.remove(1);
            listaImgRecuperadas.add(1, image02.get(0));
        }
        if (!image03.isEmpty()){
            listaImgRecuperadas.add(2,"");
            listaImgRecuperadas.remove(2);
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
                    anuncio.setTitulo(editTitulo.getText().toString());
                    anuncio.setTitulo_pesquisa(editTitulo.getText().toString().toUpperCase());
                    anuncio.setCep(editCEP.getText().toString());
                    anuncio.setDescricao(editDescricao.getText().toString());
                    anuncio.setEndereco(editEndereco.getText().toString());
                    anuncio.setTelefone(editTelefone.getText().toString());
                    anuncio.setValor(editValor.getText().toString());
                    anuncio.setFotos(listaURLFotos);
                    anuncio.setLatitude(latitude);
                    anuncio.setLongitude(longitude);
                    anuncio.setCidade(cidade);
                    anuncio.setCidade_pesquisa(cidade.toUpperCase());

                    anuncio.atualizar();

                    //finaliza carregamento
                    dialog.dismiss();
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

    public void atualizarAnuncio() {
        anuncio.setStatus("Status: Ativo");
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Atualizando Anúncio")
                .setCancelable(false)
                .build();
        dialog.show();
        //Salvar imagens
        if (teste >0){
            for (int i=0; i< listaImgRecuperadas.size(); i++) {
                String urlIMG = listaImgRecuperadas.get(i);
                int tamanho = listaImgRecuperadas.size();
                salvarImagens (urlIMG, tamanho, i);

            }}else {
            anuncio.setTitulo(editTitulo.getText().toString());
            anuncio.setTitulo_pesquisa(editTitulo.getText().toString().toUpperCase());
            anuncio.setCep(editCEP.getText().toString());
            anuncio.setDescricao(editDescricao.getText().toString());
            anuncio.setEndereco(editEndereco.getText().toString());
            anuncio.setTelefone(editTelefone.getText().toString());
            anuncio.setValor(editValor.getText().toString());
            anuncio.setLatitude(latitude);
            anuncio.setLongitude(longitude);
            anuncio.setCidade(cidade);
            anuncio.setCidade_pesquisa(cidade.toUpperCase());
            anuncio.atualizarParcial();
            dialog.dismiss();
            finish();
        }

    }

    private void iniciarComponentes () {
        buttonGPS = findViewById(R.id.buttonEditAnuncioGPS);
        buttonGPS.setOnClickListener(this);
        editCEP = findViewById(R.id.editEditAnuncioCP);
        editCEP.setFocusable(false);
        editEndereco = findViewById(R.id.editEditAnuncioRua);
        editEndereco.setFocusable(false);
        editTitulo = findViewById(R.id.editEditAnuncioTitulo);
        editDescricao = findViewById(R.id.editEditAnuncioDescricao);
        buttonOcutar = findViewById(R.id.buttonEditAnuncioOcutar);
        buttonOcutar.setOnClickListener(this);
        buttonAtualizar = findViewById(R.id.buttonEditAnuncioAtualizarAnuncio);
        editValor = findViewById(R.id.editEditAnuncioValor);
//        configurar localidade para pt -> portugues BR -> Brasil
        Locale locale = new Locale ("pt", "BR");
        editValor.setLocale(locale);
//
        editTelefone = findViewById(R.id.editEditAnuncioTelefone);
        imagem1 = findViewById(R.id.imageEditAnuncio1);
        imagem1.setOnClickListener(this);
        imagem2 = findViewById(R.id.imageEditAnuncio2);
        imagem2.setOnClickListener(this);
        imagem3 = findViewById(R.id.imageEditAnuncio3);
        imagem3.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excluir, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Codigos q eu escrevi, Config_Menu_Logar
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
            menu.setGroupVisible(R.id.group_excluir, true);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_excluir:
                alerdDialogEscluirAnuncio();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

//    //Verifica as permissões de galeria
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int permissaoResult : grantResults) {
            if (permissaoResult == PackageManager.PERMISSION_DENIED) {
                alerdDialogPermissaoGaleria();
            }
        }
    }

    private void alerdDialogEscluirAnuncio () {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Excluir anúncio");
        builder.setMessage("Tem certeza que deseja excluir esse anuncio? ");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                anuncio.excluirAnuncio();
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

    private void alerdDialogOcutarAnuncio () {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ocutar anúncio");
        builder.setMessage("Tem certeza que deseja ocutar esse anuncio para o publico? ");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                anuncio.excluirAnuncioPublico();
                anuncio.setStatus("Status: Inativo");
                anuncio.atualizarStatus();

                finish();

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

    private void alerdDialogPermissaoGaleria () {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para cadastrar um anúncio é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void alerta (String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

}
