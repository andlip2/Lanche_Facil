package lanchefacil.dalksoft.com.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import lanchefacil.dalksoft.com.R;
import lanchefacil.dalksoft.com.helper.ConfigFireBase;
import lanchefacil.dalksoft.com.model.Anuncio;
import lanchefacil.dalksoft.com.model.Usuarios;

public class FazerPedidoActivity extends AppCompatActivity {

    private Button btFazerPedido;
    private Usuarios usuario;
    private Anuncio anuncioSelecionado;
    private DatabaseReference usuarioRef;
    private EditText nome, lanche, qtd, endereco;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fazer_pedido);

//        usuarioRef = ConfigFireBase.getFirebase().child("meus_pedidos")
//                .child(ConfigFireBase.getIdUsuario());
//
//        btFazerPedido.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                anuncioSelecionado.salvarPedidos();
//                alerta("Pedido feito com sucesso!");
//            }
//        });
//
//
//    }


//    private void inicializarComponentes () {
//        btFazerPedido = findViewById(R.id.buttonFazerPedidoEnviar);
//        nome = findViewById(R.id.textFazerPedidoNome);
//        qtd = findViewById(R.id.textFazerPedidoQtd);
//        lanche = findViewById(R.id.textFazerPedidoLanche);
//        endereco = findViewById(R.id.textFazerPedidoEndereco);
//        usuario = new Usuarios();
//    }
//
////    private void criarNotificacao () {
////        String msg = "Você tem novos pedidos";
////        Intent intent =  new Intent(FazerPedidoActivity.this,  MeusPedidosActivity.class);
////        intent.putExtra("mensagem",  msg.toString());
////        PendingIntent pi=  PendingIntent.getActivity(getBaseContext(), usuario.getId().hashCode(),
////                intent,  PendingIntent.FLAG_UPDATE_CURRENT);
////        Notification notification=  new  Notification.Builder(getBaseContext())
////                .setContentTitle("Lanche Facil!"  )
////                .setContentText(msg.toString()).setSmallIcon(R.mipmap.ic_launcher)
////                .setContentIntent(pi).build();
////        NotificationManager notificationManager=  (NotificationManager)  getSystemService(NOTIFICATION_SERVICE);
////        notification.flags|=  Notification.FLAG_AUTO_CANCEL;
////        notificationManager.notify(usuario.getId().hashCode(),  notification);
////    }
//    private void alerta (String texto) {
//        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
//    }
//    private void alerdDialogPermissaoGaleria () {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Permissões Negadas");
//        builder.setMessage("Para cadastrar um anúncio é necessário aceitar as permissões");
//        builder.setCancelable(false);
//        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                finish();
//            }
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
    }

}
