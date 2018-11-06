package lanchefacil.dalksoft.com.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import lanchefacil.dalksoft.com.R;
import lanchefacil.dalksoft.com.model.Anuncio;

public class AdapterAnunciosUsuario  extends RecyclerView.Adapter<AdapterAnunciosUsuario.MyViewHolder> {
    private List<Anuncio> anuncios;
    private Context context;

    public AdapterAnunciosUsuario(List<Anuncio> anuncios, Context context) {
        this.anuncios = anuncios;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_anuncios_usuario, parent, false);

        MyViewHolder holder = new MyViewHolder(item);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if ( (anuncios != null) && (anuncios.size() >0) ){

        Anuncio anuncio = anuncios.get(position);
        holder.titulo.setText(anuncio.getTitulo());
        holder.valor.setText(anuncio.getValor());
        holder.descricao.setText(anuncio.getDescricao());
        holder.status.setText(anuncio.getStatus());


        List<String> urlIMG = anuncio.getFotos();
        String urlCapa = urlIMG.get(0);

        Picasso.get().load(urlCapa).into(holder.foto);
        }
    }

    @Override
    public int getItemCount() {
        return anuncios.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titulo;
        TextView valor;
        TextView descricao;
        TextView status;
        ImageView foto;

        public MyViewHolder(View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.textAdapterUsuarioTitulo);
            valor = itemView.findViewById(R.id.textAdapterUsuarioValor);
            foto = itemView.findViewById(R.id.imageAdapterUsuarioAnuncio);
            descricao = itemView.findViewById(R.id.textAdapterUsuarioDescricao);
            status = itemView.findViewById(R.id.textAdapterUsuarioStatus);

        }
    }

}
