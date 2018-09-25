package lanchefacil.dalksoft.com.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import lanchefacil.dalksoft.com.R;
import lanchefacil.dalksoft.com.model.Anuncio;

public class DetalhesAnuncioActivity extends AppCompatActivity {
    private CarouselView carouselView;
    private TextView titulo, descricao, cidade, valor;
    private Anuncio anuncioSelecionado;
    private Button fazerPedido, favoritos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_anuncio);

        inicializarComponentes();

        //Titulo da pagina
        getSupportActionBar().setTitle("Detelhes do Produto");


        //recuperar dados
        anuncioSelecionado = (Anuncio) getIntent().getSerializableExtra("anuncioSelecionado");

        if (anuncioSelecionado != null){
            titulo.setText(anuncioSelecionado.getTitulo());
            descricao.setText(anuncioSelecionado.getDescricao());
            cidade.setText(anuncioSelecionado.getCidade());
            valor.setText(anuncioSelecionado.getValor());

            //exibindo slide
            ImageListener imageListener = new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    String url = anuncioSelecionado.getFotos().get(position);
                    Picasso.get().load(url).into(imageView);
                }
            };
            carouselView.setPageCount(anuncioSelecionado.getFotos().size());
            carouselView.setImageListener(imageListener);
        }
    }

    public void abrirTelefone (View view) {
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", anuncioSelecionado.getTelefone(),null));
        startActivity(i);

    }

    public void adicionarFavoritos (View view) {
        anuncioSelecionado.salvarFavoritos();
    }

    private void inicializarComponentes() {
        carouselView = findViewById(R.id.carouselViewDetalheAnuncio);
        titulo = findViewById(R.id.textDetalheAnuncioTitulo);
        descricao = findViewById(R.id.textDetalheAnuncioDescricao);
        cidade = findViewById(R.id.textDetalheAnuncioCidade);
        valor = findViewById(R.id.textDetelhesAnuncioValor);
        fazerPedido = findViewById(R.id.buttonDetalheAnuncioFazerPedido);
        favoritos = findViewById(R.id.buttonDetalheAnuncioFavoritos);

    }
}
