package lanchefacil.dalksoft.com.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import lanchefacil.dalksoft.com.R;
import lanchefacil.dalksoft.com.helper.Base64Custom;
import lanchefacil.dalksoft.com.helper.ConfigFireBase;
import lanchefacil.dalksoft.com.helper.Preferencias;
import lanchefacil.dalksoft.com.model.Usuarios;

public class CadastroActivity extends AppCompatActivity {

    private EditText editEmail, editComEmail, editSenha, editComSenha;
    private Button btCadastrar;
    private FirebaseAuth autenticacao;
    private Usuarios usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        inicializarComponentes();

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString();
                String confEmail = editComEmail.getText().toString();
                String senha = editSenha.getText().toString();
                String confSenha = editComSenha.getText().toString();

                if (!email.isEmpty() && email.equals(confEmail)) {
                    if (!senha.isEmpty() && senha.equals(confSenha)) {
                        usuarios = new Usuarios();
                        usuarios.setEmail(email);
                        usuarios.setSenha(senha);
                        criarUser ();

                    }else {
                        alerta("Senha invalida!");
                    }

                }else {
                    alerta("Email invalido!");
                }
            }
        });

    }

    private void criarUser() {
        autenticacao = ConfigFireBase.getFirebaseAuth();
        autenticacao.createUserWithEmailAndPassword(usuarios.getEmail(), usuarios.getSenha()).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String idUsuario = Base64Custom.codificarBase64(usuarios.getEmail());

                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    usuarios.setId(idUsuario);
                    usuarios.salvar();

                    Preferencias preferencias = new Preferencias(CadastroActivity.this);
                    preferencias.salvarPreferenciasUsuario(idUsuario, usuarios.getEmail());
                    alerta("Usuario cadastrado com sucesso! ");
                    abrirLoginUsuario();
                }
                else {
                    String erro = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e) {
                        erro = "Digite uma senha mais forte";
                    }
                    catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = "O email digitado não é valido";
                    }
                    catch (FirebaseAuthUserCollisionException e) {
                        erro = "Esse email já está cadastrado";
                    }
                    catch (Exception e) {
                        erro = "Erro ao realizar cadastro";
                        e.printStackTrace();
                    }
                    alerta("Erro: " + erro);
                }
            }
        });
    }

    public void abrirLoginUsuario () {
        Intent i = new Intent(CadastroActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void inicializarComponentes () {
        btCadastrar = findViewById(R.id.buttonCadCadratrar);
        editEmail = findViewById(R.id.editCadEmail1);
        editComEmail = findViewById(R.id.editCadConfEmail1);
        editSenha = findViewById(R.id.editCadSenha1);
        editComSenha = findViewById(R.id.editCadConfSenha1);
    }
    private void alerta (String texto) {
        Toast.makeText(CadastroActivity.this, texto, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        autenticacao = ConfigFireBase.getFirebaseAuth();
    }
}
