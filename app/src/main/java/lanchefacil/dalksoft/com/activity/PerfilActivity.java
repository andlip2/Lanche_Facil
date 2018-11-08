package lanchefacil.dalksoft.com.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.Tag;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import lanchefacil.dalksoft.com.R;
import lanchefacil.dalksoft.com.helper.ConfigFireBase;
import lanchefacil.dalksoft.com.helper.UsuarioFirebase;
import lanchefacil.dalksoft.com.model.Usuarios;

public class PerfilActivity extends AppCompatActivity {

    private CircleImageView imagePerfil;
    private TextView txtAlterarFoto;
    private TextInputEditText editNome, editEmail;
    private Button btSalvar, btExcluir, btAlterarSenha;
    private Usuarios usuarioLogado;
    private static final int SELECAO_GALERIA = 200;
    private String identificadorUsuario;
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();
        identificadorUsuario = UsuarioFirebase.getUsuarioAtual().getUid();

        inicializarComponentes ();
        getSupportActionBar().setTitle("Perfil");

        FirebaseUser user = UsuarioFirebase.getUsuarioAtual();
        editNome.setText(user.getDisplayName());
        editEmail.setText(user.getEmail());
        Uri url = user.getPhotoUrl();
        if (url != null) {
            Glide.with(PerfilActivity.this)
            .load(url)
            .into(imagePerfil);
        }else {
            imagePerfil.setImageResource(R.drawable.padrao);
        }
        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new SweetAlertDialog(PerfilActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Carregando");
                pDialog.setCancelable(false);
                pDialog.show();
                String novoNome = editNome.getText().toString();
                UsuarioFirebase.atualizarNomeUsuario(novoNome);
                usuarioLogado.setNome(novoNome);
                usuarioLogado.atualizar();
                pDialog.dismiss();
                alerta("Alteração feita com sucesso");
            }
        });
        txtAlterarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(i, SELECAO_GALERIA);
                }
            }
        });
        btExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excluirUsuario();
            }
        });

        btAlterarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PerfilActivity.this, RecuperarSenhaActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;
            try {
                switch (requestCode) {
                    case SELECAO_GALERIA:
                        Uri imagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), imagemSelecionada);
                        break;
                }
                if (imagem !=null) {
                    imagePerfil.setImageBitmap(imagem);
                    //recupera
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
                    byte[] dadosImagem = outputStream.toByteArray();
                    pDialog = new SweetAlertDialog(PerfilActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Carregando");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    StorageReference imagemRef = ConfigFireBase.getReferenciaStorage()
                            .child("imagens")
                            .child("perfil")
                            .child(identificadorUsuario+".jpeg");
                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            alerta("Erro ao fazer upload da imagem");
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri url = taskSnapshot.getDownloadUrl();
                            atualizarFoto(url);
                            pDialog.dismiss();
                            alerta("Sucesso ao fazer upload da imagem");
                        }
                    });
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void excluirUsuario () {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("sucesso", "Conta excluida.");
                    finish();
                    Intent i = new Intent(PerfilActivity.this, PrincipalActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    private void atualizarFoto(Uri url) {
        UsuarioFirebase.atualizarFotoUsuario(url);
        usuarioLogado.setCaminhoFoto(url.toString());
        usuarioLogado.atualizar();
        alerta("Foto atualizada com sucesso!");
    }

    private void inicializarComponentes() {
        imagePerfil = findViewById(R.id.imagePefilFoto);
        txtAlterarFoto = findViewById(R.id.textPerfilAlterarFoto);
        editEmail = findViewById(R.id.editPerfilEmail);
        editNome = findViewById(R.id.editPerfilNome);
        btSalvar = findViewById(R.id.buttonPerfilSalvarAlteracoes);
        editEmail.setFocusable(false);
        btExcluir = findViewById(R.id.buttonPerfilExcluirConta);
        btAlterarSenha = findViewById(R.id.buttonPerfilAlterarSenha);
    }

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
