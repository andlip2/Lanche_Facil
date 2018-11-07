package lanchefacil.dalksoft.com.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import lanchefacil.dalksoft.com.R;
import lanchefacil.dalksoft.com.model.Anuncio;

public class AdapterFavoritos extends RecyclerView.Adapter<AdapterFavoritos.MyViewHolder>{

    private List<Anuncio> anuncios;
    private Context context;

    public AdapterFavoritos(List<Anuncio> anuncios, Context context) {
        this.anuncios = anuncios;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterFavoritos.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_favoritos, parent, false);
        return new AdapterFavoritos.MyViewHolder(item);
    }


    @Override
    public void onBindViewHolder(@NonNull AdapterFavoritos.MyViewHolder holder, int position) {

        Anuncio anuncio = anuncios.get(position);
        holder.titulo.setText(anuncio.getTitulo());
        holder.valor.setText(anuncio.getValor());
        holder.descricao.setText(anuncio.getDescricao());

        List<String> urlIMG = anuncio.getFotos();
        String urlCapa = urlIMG.get(0);

        Picasso.get().load(urlCapa).into(holder.foto);



    }

    @Override
    public int getItemCount() {
        return anuncios.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titulo;
        TextView valor;
        TextView descricao;
        ImageView foto;

        public MyViewHolder(View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.textAdapterTitulo);
            valor = itemView.findViewById(R.id.textAdapterValor);
            foto = itemView.findViewById(R.id.imageAdapterAnuncio);
            descricao = itemView.findViewById(R.id.textAdapterDescricao);
        }
    }

}
