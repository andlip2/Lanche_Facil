package lanchefacil.dalksoft.com.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import lanchefacil.dalksoft.com.R;
import lanchefacil.dalksoft.com.helper.ConfigFireBase;
import lanchefacil.dalksoft.com.model.Anuncio;
import lanchefacil.dalksoft.com.model.Usuarios;

public class FazerPedidoActivity extends AppCompatActivity {

    private Button btFazerPedido;
    private Usuarios usuario;
    private Anuncio anuncioSelecionado;
    private DatabaseReference usuarioRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fazer_pedido);

        usuarioRef = ConfigFireBase.getFirebase().child("meus_pedidos")
                .child(ConfigFireBase.getIdUsuario());

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                btFazerPedido.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        anuncioSelecionado.salvarPedidos(dataSnapshot.getKey());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void fazerPedido () {
        anuncioSelecionado.salvarPedidos("ss");
        alerta("Pedido feito com sucesso!");
    }

    private void inicializarComponentes () {
        btFazerPedido = findViewById(R.id.buttonFazerPedidoEnviar);
        usuario = new Usuarios();
    }

    private void criarNotificacao () {
        String msg = "Você tem novos pedidos";
        Intent intent =  new Intent(FazerPedidoActivity.this,  MeusPedidosActivity.class);
        intent.putExtra("mensagem",  msg.toString());
        PendingIntent pi=  PendingIntent.getActivity(getBaseContext(), usuario.getId().hashCode(),
                intent,  PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification=  new  Notification.Builder(getBaseContext())
                .setContentTitle("Lanche Facil!"  )
                .setContentText(msg.toString()).setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pi).build();
        NotificationManager notificationManager=  (NotificationManager)  getSystemService(NOTIFICATION_SERVICE);
        notification.flags|=  Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(usuario.getId().hashCode(),  notification);
    }
    private void alerta (String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

}
