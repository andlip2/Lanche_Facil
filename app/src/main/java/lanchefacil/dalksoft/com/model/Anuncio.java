package lanchefacil.dalksoft.com.model;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

import lanchefacil.dalksoft.com.helper.ConfigFireBase;

public class Anuncio {
    private String idAnuncio;
    private String titulo;
    private String cidade;
    private String cep;
    private String endereco;
    private String valor;
    private String telefone;
    private String descricao;
    private List <String> fotos;

    public Anuncio() {
        DatabaseReference anuncioRef = ConfigFireBase.getFirebase().child("meus_anuncios");
        setIdAnuncio(anuncioRef.push().getKey());
    }

    public void salvar () {
        String idUsuario = ConfigFireBase.getIdUsuario();
        DatabaseReference anuncioRef = ConfigFireBase.getFirebase().child("meus_anuncios");
        setIdAnuncio(anuncioRef.push().getKey());

        anuncioRef.child(idUsuario)
                .child(getIdAnuncio())
                .setValue(this);
        salvarPublico();
    }

    public void salvarPublico () {
        DatabaseReference anuncioRef = ConfigFireBase.getFirebase()
                .child("anuncios");
        setIdAnuncio(anuncioRef.push().getKey());

        anuncioRef.child(getTitulo())
                .child(getCep())
                .child(getIdAnuncio())
                .setValue(this);
    }

    public void excluirAnuncio () {
        String idUsuario = ConfigFireBase.getIdUsuario();
        DatabaseReference anuncioRef = ConfigFireBase.getFirebase()
                .child("meus_anuncios")
                .child(idUsuario)
                .child(getIdAnuncio());

        anuncioRef.removeValue();
        excluirAnuncioPublico();

    }
    public void excluirAnuncioPublico () {
        String idUsuario = ConfigFireBase.getIdUsuario();
        DatabaseReference anuncioRef = ConfigFireBase.getFirebase()
                .child("anuncios")
                .child(getTitulo())
                .child(getCep())
                .child(getIdAnuncio());

        anuncioRef.removeValue();

    }

    public String getIdAnuncio() {
        return idAnuncio;
    }

    public void setIdAnuncio(String idAnuncio) {
        this.idAnuncio = idAnuncio;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<String> getFotos() {
        return fotos;
    }

    public void setFotos(List<String> fotos) {
        this.fotos = fotos;
    }
}
