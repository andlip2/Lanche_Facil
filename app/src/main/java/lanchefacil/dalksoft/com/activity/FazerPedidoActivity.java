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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fazer_pedido);

    }

}
