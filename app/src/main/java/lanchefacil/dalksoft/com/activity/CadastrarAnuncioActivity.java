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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import lanchefacil.dalksoft.com.R;

public class CadastrarAnuncioActivity extends AppCompatActivity {


    private TextView tv;
    private LocationManager mLocalizacao;
    protected Location localizacao;
    private Address endereco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_anuncio);


        tv = findViewById(R.id.tvtvtv);
        double latitude =0.0;
        double longitude = 0.0;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

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
            tv.setText("Cidade: " + endereco.getLocality());
     } catch (IOException e) {
         e.printStackTrace();
     }


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
    private void alerta (String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

}
