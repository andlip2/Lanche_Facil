package lanchefacil.dalksoft.com.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import lanchefacil.dalksoft.com.R;
import lanchefacil.dalksoft.com.model.Anuncio;

public class AdapterMeusAnuncios extends RecyclerView.Adapter<AdapterMeusAnuncios.MyViewHolder>{

    private List<Anuncio> anuncios;
    private Context context;

    public AdapterMeusAnuncios(List<Anuncio> anuncios, Context context) {
        this.anuncios = anuncios;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_meus_anuncios, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Anuncio anuncio = anuncios.get(position);
        holder.titulo.setText(anuncio.getTitulo());
        holder.valor.setText(anuncio.getValor());

        List<String> urlIMG = anuncio.getFotos();
        String urlCapa = urlIMG.get(0);

        Picasso.get().load(urlCapa).into((Target) holder.foto);


    }

    @Override
    public int getItemCount() {
        return anuncios.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titulo;
        TextView titulo2;
        TextView valor;
        TextView valor2;
        TextView foto;
        TextView foto2;

        public MyViewHolder(View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.textAdapterTitulo1);
            titulo2 = itemView.findViewById(R.id.textAdapterTitulo2);
            valor = itemView.findViewById(R.id.textAdapterValor1);
            valor2 = itemView.findViewById(R.id.textAdapterValor2);
            foto = itemView.findViewById(R.id.imageAdapterAnuncio1);
            foto2 = itemView.findViewById(R.id.imageAdapterAnuncio2);
        }
    }
}
