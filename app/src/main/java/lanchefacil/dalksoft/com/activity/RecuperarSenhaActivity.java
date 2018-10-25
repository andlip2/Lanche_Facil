package lanchefacil.dalksoft.com.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import lanchefacil.dalksoft.com.R;
import lanchefacil.dalksoft.com.model.Usuarios;

public class RecuperarSenhaActivity extends AppCompatActivity {

    private Button btEnviar;
    private EditText editEmail;
    private FirebaseAuth autenticacao;
    private Usuarios usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        inicializarComponentes();
        getSupportActionBar().setTitle("Recuperar Senha");

        btEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString();
                usuarios.setEmail(email);
                autenticacao.sendPasswordResetEmail(usuarios.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            alerta("Código de verificação enviado com sucesso!");
                            finish();
                        }else {
                            alerta("Erro ao enviar o código!");
                        }
                    }
                });
            }
        });


    }

    private void inicializarComponentes () {
        usuarios = new Usuarios();
        autenticacao = FirebaseAuth.getInstance();
        btEnviar = findViewById(R.id.buttonRecuSenhaEnvia);
        editEmail = findViewById(R.id.editRecuSenhaEmail);
    }
    private void alerta (String texto) {
        Toast.makeText(RecuperarSenhaActivity.this, texto, Toast.LENGTH_SHORT).show();
    }
}
