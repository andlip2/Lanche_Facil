package lanchefacil.dalksoft.com.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferencias {

    private Context contexto;
    private SharedPreferences preferencias;
    private String NOME_ARQUIVO = "LancheFacil.preferencias";
    private int MODE = 0;
    private SharedPreferences.Editor editor;
    private final String CHAVE_IDENTIFICADOR = "identificarUsuarioLogado";
    private final String CHAVE_NOME = "nomeUsuarioLogado";

    public Preferencias (Context context){
        this.contexto = context;
        preferencias = context.getSharedPreferences(NOME_ARQUIVO, MODE);

        editor = preferencias.edit();
    }

    public void salvarPreferenciasUsuario (String identificadorUsuario, String nomeUsuario) {
        editor.putString(CHAVE_IDENTIFICADOR, identificadorUsuario);
        editor.putString(CHAVE_NOME, nomeUsuario);
        editor.commit();
    }

    public String getIdentificador () {
        return preferencias.getString(CHAVE_IDENTIFICADOR, null);
    }

    public String getNome () {
        return preferencias.getString(CHAVE_NOME, null);
    }
}
