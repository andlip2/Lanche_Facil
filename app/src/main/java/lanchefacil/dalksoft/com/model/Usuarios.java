package lanchefacil.dalksoft.com.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lanchefacil.dalksoft.com.helper.ConfigFireBase;


public class Usuarios {

    private String id;
    private String email;
    private String nome = "teste";
    private String senha;
    private String caminhoFoto;



    public Usuarios() {
    }

    public void salvar () {
        DatabaseReference referenciaFirebase = ConfigFireBase.getFirebase();
        DatabaseReference usuariosRef = referenciaFirebase
                .child("usuarios")
                .child(String.valueOf(getId()));
        usuariosRef.setValue(this);

    }

    public void excluir () {
        String idUsuario = ConfigFireBase.getIdUsuario();
        DatabaseReference anuncioRef = ConfigFireBase.getFirebase()
                .child("usuarios")
                .child(String.valueOf(getId()));

        anuncioRef.removeValue();
    }

    public void atualizar () {
        DatabaseReference reference = ConfigFireBase.getFirebase();
        DatabaseReference usuarioRef = reference
                .child("usuarios")
                .child(getId());
        Map<String, Object> valoresUsuario = converterMap();
        usuarioRef.updateChildren(valoresUsuario);
    }

    public Map<String, Object> converterMap () {
        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("email", getEmail());
        usuarioMap.put("nome", getNome());
        usuarioMap.put("id", getId());
        usuarioMap.put("caminhoFoto", getCaminhoFoto());

        return usuarioMap;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> hashMapUsuario = new HashMap<>();
        hashMapUsuario.put("id", getId());
        hashMapUsuario.put("email", getEmail());
        hashMapUsuario.put("senha", getSenha());

        return hashMapUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
