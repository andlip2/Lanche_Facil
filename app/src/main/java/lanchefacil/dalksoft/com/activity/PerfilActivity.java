package lanchefacil.dalksoft.com.activity;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;
import lanchefacil.dalksoft.com.R;
import lanchefacil.dalksoft.com.helper.UsuarioFirebase;
import lanchefacil.dalksoft.com.model.Usuarios;

public class PerfilActivity extends AppCompatActivity {

    private CircleImageView imagePerfil;
    private TextView txtAlterarFoto;
    private TextInputEditText editNome, editEmail;
    private Button btSalvar;
    private Usuarios usuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        inicializarComponentes ();

        FirebaseUser user = UsuarioFirebase.getUsuarioAtual();
        editNome.setText(user.getDisplayName());
        editEmail.setText(user.getEmail());

        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String novoNome = editNome.getText().toString();

                UsuarioFirebase.atualizarNomeUsuario(novoNome);

                usuarioLogado.setNome(novoNome);
                usuarioLogado.atualizar();
            }
        });
    }

    private void inicializarComponentes() {
        imagePerfil = findViewById(R.id.imagePefilFoto);
        txtAlterarFoto = findViewById(R.id.textPerfilAlterarFoto);
        editEmail = findViewById(R.id.editPerfilEmail);
        editNome = findViewById(R.id.editPerfilNome);
        btSalvar = findViewById(R.id.buttonPerfilSalvarAlteracoes);
        editEmail.setFocusable(false);
    }
}
