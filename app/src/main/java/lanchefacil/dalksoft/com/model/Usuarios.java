package lanchefacil.dalksoft.com.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import lanchefacil.dalksoft.com.helper.ConfigFireBase;


public class Usuarios {

    private String id;
    private String email;
    private String senha;

    public Usuarios() {
    }

    public void salvar () {
        DatabaseReference referenciaFirebase = ConfigFireBase.getFirebase();
        referenciaFirebase.child("usuarios").child(String.valueOf(getId())).setValue(this);

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> hashMapUsuario = new HashMap<>();
        hashMapUsuario.put("id", getId());
        hashMapUsuario.put("email", getEmail());
        hashMapUsuario.put("senha", getSenha());

        return hashMapUsuario;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
