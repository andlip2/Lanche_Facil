package lanchefacil.dalksoft.com;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private TextView btCadastrar, btRecuSenha;
    private Button btentrar;
    private TextInputEditText editEmail, editSenha;

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

        btentrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.toString();
            }
        });
    }
    private void inicializarComponentes () {
        btCadastrar = findViewById(R.id.buttonLogCadastrar);
        editEmail = findViewById(R.id.editLogEmail);
        editSenha = findViewById(R.id.editLogSenha);
    }
}
