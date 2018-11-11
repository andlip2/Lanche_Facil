package lanchefacil.dalksoft.com.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.util.Date;

import lanchefacil.dalksoft.com.R;
import lanchefacil.dalksoft.com.helper.UsuarioFirebase;
import lanchefacil.dalksoft.com.model.Anuncio;
import lanchefacil.dalksoft.com.model.Usuarios;

public class FixaAnuncioActivity extends AppCompatActivity{

    private Button btEnviar;
    private RadioGroup radioGroup;
    private RadioButton radioA, radioB;
    private TextView txtValor;
    private StringBuilder msg;
    private Anuncio anuncio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixa_anuncio);


        btEnviar = findViewById(R.id.buttonFixaEnviar);
        radioGroup = findViewById(R.id.radioGroupFixaAnuncio);
        radioA = findViewById(R.id.radioButtonPlanoA);
        radioB = findViewById(R.id.radioButtonPlanoB);
        txtValor = findViewById(R.id.textFixaValor);
        final FirebaseUser user = UsuarioFirebase.getUsuarioAtual();
        anuncio = (Anuncio) getIntent().getSerializableExtra("anuncioSelecionado");

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                msg = new  StringBuilder();
                switch (checkedId) {
                    case R.id.radioButtonPlanoA:
                        msg.append("Prezados,");
                        msg.append('\n');
                        msg.append("Gostaria de fixa o meu anuncio ("+ anuncio.getTitulo()+") por meio do plano A");
                        msg.append("\n\nIdentificação do anuncio: "+ anuncio.getIdAnuncio());
                        msg.append("\n\n\n");
                        msg.append("Atenciosamente, " + user.getDisplayName());
                        txtValor.setText("Valor: R$ 20,00");
                        break;
                    case R.id.radioButtonPlanoB:
                        msg.append("Prezados,");
                        msg.append('\n');
                        msg.append("Gostaria de fixa o meu anuncio (\"+ anuncio.getTitulo()+\") por meio do plano B");
                        msg.append("\n\nIdentificação do anuncio: "+ anuncio.getIdAnuncio());
                        msg.append("\n\n\n");
                        msg.append("Atenciosamente, " + user.getDisplayName());
                        txtValor.setText("Valor: R$ 134,00");
                        break;
                }
            }
        });



        btEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = "dalksoft@gmail.com";

                Date date = new Date();


                if (radioA.isChecked() == true || radioB.isChecked() == true){

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{ email });
                i.putExtra(Intent.EXTRA_SUBJECT, "Fixa Anúncio");
                i.putExtra(Intent.EXTRA_TEXT   , (CharSequence) msg);
                try {
                    startActivity( Intent.createChooser(i, "Send mail..."));
                }
                catch (android.content.ActivityNotFoundException ex)
                {
                    Toast.makeText(FixaAnuncioActivity.this, "Não foi localizado software para o envio de email.", Toast.LENGTH_SHORT).show();
                }
            }else {
                    Toast.makeText(FixaAnuncioActivity.this, "Selecione um plano", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}
