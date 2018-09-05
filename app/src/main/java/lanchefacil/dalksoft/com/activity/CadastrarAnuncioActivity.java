package lanchefacil.dalksoft.com.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import lanchefacil.dalksoft.com.R;

public class CadastrarAnuncioActivity extends AppCompatActivity {


    private EditText editCidade, editCEP, editRua, editTitulo, editDescricao;
    private CurrencyEditText editValor;
    private Button buttonGPS;
    private LocationManager mLocalizacao;
    protected Location localizacao;
    private Address endereco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_anuncio);

        iniciarComponentes();

        buttonGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double latitude =0.0;
                double longitude = 0.0;

                if (ActivityCompat.checkSelfPermission(CadastrarAnuncioActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(CadastrarAnuncioActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                }else {
                    mLocalizacao = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                    localizacao = mLocalizacao.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }

                if (localizacao != null) {
                    longitude = localizacao.getLongitude();
                    latitude = localizacao.getLatitude();
                }

                try {
                    endereco = buscarEndereco(latitude, longitude);
                    editCidade.setText(endereco.getLocality());
                    editCEP.setText(endereco.getPostalCode());
                    editRua.setText(endereco.getAddressLine(0));
                } catch (IOException e) {
                    alerta("Erro ao recuperar localização");
                }
            }
        });




    }

    private Address buscarEndereco(double latitude, double longitude) throws IOException {
        Geocoder geocoder;
        Address address = null;
        List<Address> addresses;

        geocoder = new Geocoder(getApplicationContext());

        addresses = geocoder.getFromLocation(latitude, longitude, 1);

        if (addresses.size() >0) {
            address = addresses.get(0);
        }
        return address;
    }

    public void salvarAnuncio (View view) {
        String valor =editValor.getHintString();
        Log.d("Salvar", "salvarAnuncio" + valor);
    }

    private void alerta (String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

    private void iniciarComponentes () {
        buttonGPS = findViewById(R.id.buttonAnuncioGPS);
        editCidade = findViewById(R.id.editAnuncioCidade);
        editCEP = findViewById(R.id.editAnuncioCP);
        editCidade = findViewById(R.id.editAnuncioCidade);
        editRua = findViewById(R.id.editAnuncioRua);
        editTitulo = findViewById(R.id.editAnuncioTitulo);
        editDescricao = findViewById(R.id.editAnuncioDescricao);

        editValor = findViewById(R.id.editAnuncioValor);
        //configurar localidade para pt -> portugues BR -> Brasil
        Locale locale = new Locale ("pt", "BR");
        editValor.setLocale(locale);
    }

}
