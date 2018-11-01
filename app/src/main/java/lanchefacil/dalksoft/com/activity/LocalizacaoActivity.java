package lanchefacil.dalksoft.com.activity;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import lanchefacil.dalksoft.com.R;
import lanchefacil.dalksoft.com.model.Anuncio;

public class LocalizacaoActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Anuncio anuncio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacao);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        anuncio = new Anuncio();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        anuncio = (Anuncio) getIntent().getSerializableExtra("anuncioSelecionado");

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng localizacao = new LatLng(anuncio.getLatitude(), anuncio.getLongitude());
        mMap.addMarker(new MarkerOptions()
                .position(localizacao)
                .title(anuncio.getTitulo())
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory
                                            .HUE_YELLOW)));
        mMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(localizacao, 10));
    }
}
