package lanchefacil.dalksoft.com.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import lanchefacil.dalksoft.com.R;

public class MeusPedidosActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_pedidos);

        getSupportActionBar().setTitle("Meus Pedidos" );


    }

}
