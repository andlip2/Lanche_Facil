package lanchefacil.dalksoft.com.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

import lanchefacil.dalksoft.com.R;
import lanchefacil.dalksoft.com.model.Anuncio;

public class FixaAnuncioActivity extends AppCompatActivity{

    private Button btEnviar;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixa_anuncio);


        btEnviar = findViewById(R.id.buttonFixaEnviar);

        btEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = "dalksoft@gmail.com";

                Date date = new Date();
                StringBuilder msg = new  StringBuilder();

                msg.append("Prezados,");
                msg.append('\n');
                msg.append("Gostaria de fixa o meu anuncio por meio do plano A");
                msg.append("\n\n");
                msg.append("Atenciosamente,");


//        File sd = getActivity().getBaseContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS + "/backups" );
//        String backupDBPath = "BKP_" + dbname;
//        File backupDB = new File(sd, backupDBPath);

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
//        i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(backupDB));
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
            }
        });



    }
}
