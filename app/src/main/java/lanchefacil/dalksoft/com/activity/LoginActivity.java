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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import lanchefacil.dalksoft.com.R;
import lanchefacil.dalksoft.com.helper.ConfigFireBase;
import lanchefacil.dalksoft.com.model.Usuarios;

public class LoginActivity extends AppCompatActivity {

    private TextView btCadastrar, btRecuSenha;
    private Button btEntrar;
    private EditText editEmail, editSenha;
    private Usuarios usuarios;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inicializarComponentes();

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

    private void inicializarComponentes () {
        btEntrar = findViewById(R.id.buttonLogEntrar);
        btCadastrar = findViewById(R.id.buttonLogCadastrar);
        editEmail = findViewById(R.id.editLogEmail1);
        editSenha = findViewById(R.id.editLogSenha1);
        btRecuSenha = findViewById(R.id.buttonLogRecuSenha);
    }

    private void alerta (String texto) {
        Toast.makeText(LoginActivity.this, texto, Toast.LENGTH_SHORT).show();
    }
}
