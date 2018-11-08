package lanchefacil.dalksoft.com.model;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lanchefacil.dalksoft.com.helper.ConfigFireBase;

public class Anuncio implements Serializable{
    private String idAnuncio;
    private String titulo;
    private String titulo_pesquisa;
    private String cidade_pesquisa;
    private String cidade;
    private String cep;
    private String endereco;
    private String valor;
    private String telefone;
    private String descricao;
    private String nome;
    private String lanche;
    private List <String> fotos;
    private int favoritos;
    private double latitude;
    private double longitude;
    private String status;


    public Anuncio()  {
        DatabaseReference anuncioRef = ConfigFireBase.getFirebase().child("meus_anuncios");
        setIdAnuncio(anuncioRef.push().getKey());
    }
    public void salvarFavoritos () {
        String idUsuario = ConfigFireBase.getIdUsuario();
        DatabaseReference anuncioRef = ConfigFireBase.getFirebase().child("favoritos");

        anuncioRef.child(idUsuario)
                .child(getIdAnuncio())
                .setValue(this);
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
        anuncioRef.child(getIdAnuncio())
                .setValue(this);
    }
    public void  atualizarFavoritos () {
        String idUsuario = ConfigFireBase.getIdUsuario();
        DatabaseReference anuncioRef = ConfigFireBase.getFirebase()
                .child("favoritos")
                .child(idUsuario)
                .child(getIdAnuncio());
        Map<String, Object> valoresUsuario = converterMap();
        anuncioRef.updateChildren(valoresUsuario);
    }
    public void atualizar () {
        String idUsuario = ConfigFireBase.getIdUsuario();
        DatabaseReference anuncioRef = ConfigFireBase.getFirebase()
                .child("meus_anuncios")
                .child(idUsuario)
                .child(getIdAnuncio());

        Map<String, Object> valoresUsuario = converterMap();
        anuncioRef.updateChildren(valoresUsuario);
        atualizarPublico();
    }
    public void atualizarPublico () {
        DatabaseReference anuncioRef = ConfigFireBase.getFirebase()
                .child("anuncios")
                .child(getIdAnuncio());
        Map<String, Object> valoresUsuario = converterMap();
        anuncioRef.updateChildren(valoresUsuario);
    }
    public void atualizarParcial () {
        String idUsuario = ConfigFireBase.getIdUsuario();
        DatabaseReference anuncioRef = ConfigFireBase.getFirebase()
                .child("meus_anuncios")
                .child(idUsuario)
                .child(getIdAnuncio());

        Map<String, Object> valoresUsuario = converterMapParcial();
        anuncioRef.updateChildren(valoresUsuario);
        atualizarPublicoParcial();
    }
    public void atualizarPublicoParcial () {
        DatabaseReference anuncioRef = ConfigFireBase.getFirebase()
                .child("anuncios")
                .child(getIdAnuncio());
        Map<String, Object> valoresUsuario = converterMapParcial();
        anuncioRef.updateChildren(valoresUsuario);
    }
    public void atualizarStatus () {
        String idUsuario = ConfigFireBase.getIdUsuario();
        DatabaseReference anuncioRef = ConfigFireBase.getFirebase()
                .child("meus_anuncios")
                .child(idUsuario)
                .child(getIdAnuncio());

        Map<String, Object> valoresUsuario = converterMapStatus();
        anuncioRef.updateChildren(valoresUsuario);
    }
    public Map<String, Object> converterMap () {
        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("titulo", getTitulo());
        usuarioMap.put("titulo_pesquisa", getTitulo_pesquisa());
        usuarioMap.put("id", getIdAnuncio());
        usuarioMap.put("cidade", getCidade());
        usuarioMap.put("cidade_pesquisa", getCidade_pesquisa());
        usuarioMap.put("endereco", getEndereco());
        usuarioMap.put("valor", getValor());
        usuarioMap.put("telefone",getTelefone());
        usuarioMap.put("descricao", getDescricao());
        usuarioMap.put("fotos" , getFotos());
        usuarioMap.put("status", getStatus());
        usuarioMap.put("latitude", getLatitude());
        usuarioMap.put("longitude", getLongitude());

        return usuarioMap;
    }
    public Map<String, Object> converterMapParcial () {
        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("titulo", getTitulo());
        usuarioMap.put("titulo_pesquisa", getTitulo().toUpperCase());
        usuarioMap.put("id", getIdAnuncio());
        usuarioMap.put("endereco", getEndereco());
        usuarioMap.put("valor", getValor());
        usuarioMap.put("telefone",getTelefone());
        usuarioMap.put("descricao", getDescricao());
        usuarioMap.put("status", getStatus());
        usuarioMap.put("cidade", getCidade());
        usuarioMap.put("cidade_pesquisa", getCidade_pesquisa());
        usuarioMap.put("latitude", getLatitude());
        usuarioMap.put("longitude", getLongitude());

        return usuarioMap;
    }
    public Map<String, Object> converterMapStatus () {
        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("status", getStatus());
        return usuarioMap;
    }


    public void excluirAnuncio () {
        String idUsuario = ConfigFireBase.getIdUsuario();
        DatabaseReference anuncioRef = ConfigFireBase.getFirebase()
                .child("meus_anuncios")
                .child(idUsuario)
                .child(getIdAnuncio());

        anuncioRef.removeValue();
        excluirAnuncioPublico();
        excluirFavorito();

    }
    public void excluirAnuncioPublico () {
        DatabaseReference anuncioRef = ConfigFireBase.getFirebase()
                .child("anuncios")
                .child(getIdAnuncio());

        anuncioRef.removeValue();

    }
    public void excluirFavorito () {
        String idUsuario = ConfigFireBase.getIdUsuario();
        DatabaseReference anuncioRef = ConfigFireBase.getFirebase()
                .child("favoritos")
                .child(idUsuario)
                .child(getIdAnuncio());

        anuncioRef.removeValue();

    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getFavoritos() {
        return favoritos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLanche() {
        return lanche;
    }

    public void setLanche(String lanche) {
        this.lanche = lanche;
    }

    public void setFavoritos(int favoritos) {

        this.favoritos = favoritos;
    }

    public String getCidade_pesquisa() {
        return cidade_pesquisa;
    }

    public void setCidade_pesquisa(String cidade_pesquisa) {
        this.cidade_pesquisa = cidade_pesquisa;
    }

    public String getTitulo_pesquisa() {
        return titulo_pesquisa;
    }

    public void setTitulo_pesquisa(String titulo_pesquisa) {
        this.titulo_pesquisa = titulo_pesquisa;
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
