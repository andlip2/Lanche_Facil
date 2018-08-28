package lanchefacil.dalksoft.com.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Array;

import lanchefacil.dalksoft.com.R;
import lanchefacil.dalksoft.com.helper.ConfigFireBase;
import lanchefacil.dalksoft.com.model.Usuarios;

public class LoginActivity extends AppCompatActivity {

    private TextView btCadastrar, btRecuSenha;
    private Button btEntrar, btGoogle;
    private EditText editEmail, editSenha;
    private Usuarios usuarios;
    private CallbackManager callbackManager;
    private LoginButton btFacobook;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inicializarComponentes();
        inicializarFirebaseCallback();
        loginFace();

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(i);
            }
        });

        btRecuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RecuperarSenhaActivity.class);
                startActivity(i);
            }
        });

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString();
                String senha = editSenha.getText().toString();
                if (!email.equals(null) && !senha.equals(null)) {

                    usuarios = new Usuarios();
                    usuarios.setEmail(email);
                    usuarios.setSenha(senha);
                    validarLogin();

                }else {
                    alerta("Preencha todos os campos");
                }
            }
        });



    }

    private void validarLogin () {
        autenticacao = ConfigFireBase.getFirebaseAuth();
        autenticacao.signInWithEmailAndPassword(usuarios.getEmail(), usuarios.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    alerta("Login efetuado com sucesso");
                    Intent i = new Intent(LoginActivity.this, PrincipalActivity.class);
                    startActivity(i);
                }else {
                    alerta("Erro ao realizar login");
                }
            }
        });
    }

    //login com facebook
    private void inicializarFirebaseCallback () {
        autenticacao = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
    }
    private void loginFace () {
        btFacobook.setReadPermissions();
        btFacobook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                firebaseLogin(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                alerta("Operação cancelada!");
            }

            @Override
            public void onError(FacebookException error) {
                alerta("Erro ao realizar login!");
            }
        });
    }


    private void firebaseLogin(AccessToken accessToken) {
        AuthCredential credencial = FacebookAuthProvider.getCredential(accessToken.getToken());

        autenticacao.signInWithCredential(credencial).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent i = new Intent(LoginActivity.this, PrincipalActivity.class);
                    startActivity(i);
                }else {
                    alerta("Erro de autenticação!");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        autenticacao.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        autenticacao.removeAuthStateListener(firebaseAuthListener);
    }

    private void inicializarComponentes () {
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null){
                    Intent i = new Intent(LoginActivity.this, PrincipalActivity.class);
                    startActivity(i);
                }

            }
        };
        btFacobook = findViewById(R.id.buttonLogFacebook);
        btFacobook.setReadPermissions("email", "public_profile");
        btEntrar = findViewById(R.id.buttonLogEntrar);
        btCadastrar = findViewById(R.id.buttonLogCadastrar);
        editEmail = findViewById(R.id.editLogEmail1);
        editSenha = findViewById(R.id.editLogSenha1);
        btRecuSenha = findViewById(R.id.buttonLogRecuSenha);
    }

    private void alerta (String texto) {
        Toast.makeText(LoginActivity.this, texto, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

