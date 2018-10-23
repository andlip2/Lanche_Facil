package lanchefacil.dalksoft.com.activity;
import android.Manifest;
//import android.app.Activity;
import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
import android.location.Address;
//import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
//import android.net.Uri;
//import android.provider.MediaStore;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
//import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
//import com.bumptech.glide.Glide;
//import com.firebase.ui.storage.images.FirebaseImageLoader;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
import com.santalu.widget.MaskEditText;
import com.squareup.picasso.Picasso;

//import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//import dmax.dialog.SpotsDialog;
import lanchefacil.dalksoft.com.R;
import lanchefacil.dalksoft.com.helper.ConfigFireBase;
import lanchefacil.dalksoft.com.helper.Permissoes;
import lanchefacil.dalksoft.com.model.Anuncio;

public class EditarAnuncioActivity extends AppCompatActivity
//        implements View.OnClickListener
{


    private EditText editCidade, editCEP, editEndereco, editTitulo, editDescricao;
    private CurrencyEditText editValor;
    private MaskEditText editTelefone;
    private ImageView imagem1, imagem2, imagem3;
    private Button buttonGPS;
//    private LocationManager mLocalizacao;
//    protected Location localizacao;
//    private Address endereco;
    private Anuncio anuncio;
//    private StorageReference storage;
//    private AlertDialog dialog;
//    private String [] permissoes = new String[]{
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.CAMERA,
//    };
//    private List<String> listaImgRecuperadas = new ArrayList<>();
//    private List<String> listaURLFotos = new ArrayList<>();
//    private List<String> listaURLFotos2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_anuncio);

//        Permissoes.validarPermissoes(permissoes, this,1);
//
        iniciarComponentes();
//
//        storage = ConfigFireBase.getReferenciaStorage();
//
        anuncio = (Anuncio) getIntent().getSerializableExtra("anuncioSelecionado");
        if (anuncio != null) {
            editTitulo.setText(anuncio.getTitulo());
            editCEP.setText(anuncio.getCep());
            editCidade.setText(anuncio.getCidade());
            editDescricao.setText(anuncio.getDescricao());
            editEndereco.setText(anuncio.getEndereco());
            editTelefone.setText(anuncio.getTelefone());
            editValor.setText(anuncio.getValor());

            //Recuperar IMG
            recuperarFotos();
            }

        }

        public void recuperarFotos () {

        for (int cont = 0; cont <= anuncio.getFotos().size(); cont++){
            if (cont == 0) {
                String url = anuncio.getFotos().get(0);
                Picasso.get().load(url).into(imagem1);
            }
//            if (cont == 1) {
//                String url = anuncio.getFotos().get(1);
//                Picasso.get().load(url).into(imagem2);
//            }if (cont == 2){
//                String url = anuncio.getFotos().get(2);
//                Picasso.get().load(url).into(imagem3);
            }
        }
//        }
//        }

//        private void geolocalizacao() {
//            double latitude =0.0;
//            double longitude = 0.0;
//            if (ActivityCompat.checkSelfPermission(EditarAnuncioActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(EditarAnuncioActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            }else {
//                mLocalizacao = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//                localizacao = mLocalizacao.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            }
//
//            if (localizacao != null) {
//                longitude = localizacao.getLongitude();
//                latitude = localizacao.getLatitude();
//            }
//
//            try {
//                endereco = buscarEndereco(latitude, longitude);
//                editCidade.setText(endereco.getLocality());
//                editCEP.setText(endereco.getPostalCode());
//                editEndereco.setText(endereco.getAddressLine(0));
//            } catch (IOException e) {
//                alerta("Erro ao recuperar localização");
//            }
//        }
//
//        private Address buscarEndereco(double latitude, double longitude) throws IOException {
//            Geocoder geocoder;
//            Address address = null;
//            List<Address> addresses;
//
//            geocoder = new Geocoder(getApplicationContext());
//
//            addresses = geocoder.getFromLocation(latitude, longitude, 1);
//
//            if (addresses.size() >0) {
//                address = addresses.get(0);
//            }
//            return address;
//        }
//
//
//    public void validarDadosAnuncio (View view) {
//
//
//        String valor = String.valueOf(editValor.getRawValue());
//
//        String fone = "";
//        if (editTelefone.getRawText() != null) {
//            fone = editTelefone.getRawText().toString();
//        }
//        if (listaImgRecuperadas.size() != 0){
//            if (!anuncio.getTitulo().isEmpty()){
//                if (!anuncio.getCidade().isEmpty()){
//                    if (!anuncio.getCep().isEmpty()){
//                        if (!anuncio.getEndereco().isEmpty()){
//                            if (!valor.isEmpty() && !valor.equals("0")){
//                                if (!anuncio.getTelefone().isEmpty()){
//                                    if (fone.length() >=11) {
//                                        if (!anuncio.getDescricao().isEmpty()){
//                                            salvarAnuncio();
//                                        }else {
//                                            alerta("Defina a descrição do anúncio");
//                                        }
//                                    }else {
//                                        alerta("O numero digitado não é valido");
//                                    }
//                                }else {
//                                    alerta("Defina o valor do anúncio");
//                                }
//                            }else {
//                                alerta("Defina o valor do anúncio");
//                            }
//                        }else {
//                            alerta("Defina o endereço do anúncio");
//                        }
//                    }else {
//                        alerta("Defina o CEP do anúncio");
//                    }
//                }else {
//                    alerta("Defina a cidade do anúncio");
//                }
//            }else {
//                alerta("Defina o titulo do anúncio");
//            }
//
//        }else {
//            alerta("Você precisa adicionar ao menos uma foto!");
//        }
//
//    }
//
//    public void salvarAnuncio () {
//
//        dialog = new SpotsDialog.Builder()
//                .setContext(this)
//                .setMessage("Salvando Anúncio")
//                .setCancelable(false)
//                .build();
//        dialog.show();
//
//        //Salvar imagens
//        for (int i=0; i< listaImgRecuperadas.size(); i++) {
//            String urlIMG = listaImgRecuperadas.get(i);
//            int tamanho = listaImgRecuperadas.size();
//            salvarImagens (urlIMG, tamanho, i);
//        }
//
//    }
//
//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()) {
//            case R.id.imageEditarAnuncio1:
//                escolherImagem(1);
//                break;
//            case R.id.imageEditarAnuncio2:
//                escolherImagem(2);
//                break;
//            case R.id.imageEditarAnuncio3:
//                escolherImagem(3);
//                break;
//            case R.id.buttonEditarAnuncioGPS:
//                geolocalizacao ();
//                break;
//
//        }
//    }
//    private void escolherImagem(int requestCode) {
//        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(i, requestCode);
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == Activity.RESULT_OK) {
//            Uri imagemSelecionada = data.getData();
//            String caminhoImagem = imagemSelecionada.toString();
//
//            if (requestCode == 1) {
//                imagem1.setImageURI(imagemSelecionada);
//                listaImgRecuperadas.add(caminhoImagem);
//            }else if (requestCode == 2) {
//                imagem2.setImageURI(imagemSelecionada);
//            }else if ( requestCode == 3) {
//                imagem3.setImageURI(imagemSelecionada);
//            }
//
//            listaImgRecuperadas.add(caminhoImagem);
//
//        }
//    }
//    private void salvarImagens(String urlIMG, final int totalIMG, int contador) {
//        //criando nó no banco
//        StorageReference imgAnuncio = storage.child("imagens")
//                .child("anuncios")
//                .child(anuncio.getIdAnuncio())
//                .child("imagem"+contador);
//
//        //enviar imagem e anuncio
//        UploadTask uploadTask = imgAnuncio.putFile(Uri.parse(urlIMG));
//        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                Uri firebaseUrl = taskSnapshot.getDownloadUrl();
//                String urlConvertida = firebaseUrl.toString();
//                listaURLFotos.add(urlConvertida);
//
//                if (totalIMG == listaURLFotos.size()) {
//                    anuncio.setFotos(listaURLFotos);
//                    //Salvar anuncio
//                    anuncio.salvar();
//
//                    //finaliza carregamento
//                    dialog.dismiss();
//                    finish();
//                }else {
//
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                alerta("Falha ao fazer upload da imagem");
//                Log.i("INFO", "Falha ao fazer upload: " + e.getMessage());
//            }
//        });
//
//    }

    private void iniciarComponentes () {
        buttonGPS = findViewById(R.id.buttonEditarAnuncioGPS);
//        buttonGPS.setOnClickListener(this);
        editCidade = findViewById(R.id.editEditarAnuncioCidade);
        editCEP = findViewById(R.id.editEditarAnuncioCP);
        editCidade = findViewById(R.id.editEditarAnuncioCidade);
        editEndereco = findViewById(R.id.editEditarAnuncioRua);
        editTitulo = findViewById(R.id.editEditarAnuncioTitulo);
        editDescricao = findViewById(R.id.editEditarAnuncioDescricao);

        editValor = findViewById(R.id.editEditarAnuncioValor);
//        configurar localidade para pt -> portugues BR -> Brasil
//        Locale locale = new Locale ("pt", "BR");
//        editValor.setLocale(locale);
//
        editTelefone = findViewById(R.id.editEditarAnuncioTelefone);
        imagem1 = findViewById(R.id.imageEditarAnuncio1);
//        imagem1.setOnClickListener(this);
        imagem2 = findViewById(R.id.imageEditarAnuncio2);
//        imagem2.setOnClickListener(this);
        imagem3 = findViewById(R.id.imageEditarAnuncio3);
//        imagem3.setOnClickListener(this);
    }


//    //Verifica as permissões de galeria
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        for (int permissaoResult : grantResults) {
//            if (permissaoResult == PackageManager.PERMISSION_DENIED) {
//                alerdDialogPermissaoGaleria();
//            }
//        }
//    }
//
//    private void alerdDialogPermissaoGaleria () {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Permissões Negadas");
//        builder.setMessage("Para cadastrar um anúncio é necessário aceitar as permissões");
//        builder.setCancelable(false);
//        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                finish();
//            }
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
//    private void alerta (String texto) {
//        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
//    }

}
