package lanchefacil.dalksoft.com.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dmax.dialog.SpotsDialog;
import lanchefacil.dalksoft.com.R;
import lanchefacil.dalksoft.com.adapter.AdapterMeusAnuncios;
import lanchefacil.dalksoft.com.helper.ConfigFireBase;
import lanchefacil.dalksoft.com.helper.RecyclerItemClickListener;
import lanchefacil.dalksoft.com.model.Anuncio;

public class MeusPedidosActivity extends AppCompatActivity {

    private RecyclerView recyclerPedidos;
    private AdapterMeusAnuncios adapterMeusAnuncios;
    private DatabaseReference usuarioRef;
    private List<Anuncio> listaAnuncios = new ArrayList<>();
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_pedidos);

//        usuarioRef = ConfigFireBase.getFirebase().child("meus_pedidos")
//                .child(ConfigFireBase.getIdUsuario());
//        recyclerPedidos = findViewById(R.id.recyclerMeusPedido);
//
//
//        exibirAnuncios();
//    }
//    public void exibirAnuncios () {
//        recyclerPedidos.setLayoutManager(new LinearLayoutManager(this));
//        recyclerPedidos.setHasFixedSize(true);
//        adapterMeusAnuncios = new AdapterMeusAnuncios(listaAnuncios,this);
//        recyclerPedidos.setAdapter(adapterMeusAnuncios);
//        recuperarFavoritos();
//
//        recyclerPedidos.addOnItemTouchListener(new RecyclerItemClickListener(
//                this,
//                recyclerPedidos,
//                new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        Anuncio anuncioSelecionado = listaAnuncios.get(position);
//                        Intent i = new Intent(MeusPedidosActivity.this, DetalhesAnuncioActivity.class);
//                        i.putExtra("anuncioSelecionado", anuncioSelecionado);
//                        startActivity(i);
//                    }
//
//                    @Override
//                    public void onLongItemClick(View view, int position) {
//                        alerdDialogEscluirAnuncio(position);
//                    }
//
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                    }
//                }
//        ));
//    }
//
//    private void recuperarFavoritos() {
//        dialog = new SpotsDialog.Builder()
//                .setContext(this)
//                .setMessage("Carregando Seus Pedidos")
//                .setCancelable(false)
//                .build();
//        dialog.show();
//        usuarioRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                listaAnuncios.clear();
//                for (DataSnapshot id: dataSnapshot.getChildren()){
//                    listaAnuncios.add(id.getValue(Anuncio.class));
//                }
//                Collections.reverse(listaAnuncios);
//                adapterMeusAnuncios.notifyDataSetChanged();
//
//                dialog.dismiss();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    private void alerdDialogEscluirAnuncio (final int position) {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Comfirmar entrega");
//        builder.setMessage("Tem certeza que deseja confirmar a entrega desse anuncio?");
//        builder.setCancelable(false);
//        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Anuncio anuncioSelecionado = listaAnuncios.get(position);
//                anuncioSelecionado.excluirFavorito();
//
//                adapterMeusAnuncios.notifyDataSetChanged();
//            }
//        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                builder.setCancelable(true);
//            }
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
    }

}
