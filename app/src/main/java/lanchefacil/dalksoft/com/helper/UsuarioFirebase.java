package lanchefacil.dalksoft.com.helper;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import lanchefacil.dalksoft.com.model.Usuarios;

public class UsuarioFirebase {

    public static FirebaseUser getUsuarioAtual () {
        FirebaseAuth usuario = ConfigFireBase.getFirebaseAuth();
        return usuario.getCurrentUser();
    }

    public static void atualizarNomeUsuario (String nome) {
        try {

            FirebaseUser user = getUsuarioAtual();

            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest
                    .Builder()
                    .setDisplayName(nome)
                    .build();

            user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Log.d("Perfil", "Erro ao atualizar nome do perfil");
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Usuarios getDadosUsuarioLogado () {

        FirebaseUser firebaseUser = getUsuarioAtual();

        Usuarios usuario = new Usuarios();
        usuario.setNome(firebaseUser.getDisplayName());
        usuario.setEmail(firebaseUser.getEmail());
        usuario.setId(firebaseUser.getUid());

        if (firebaseUser.getPhotoUrl() == null) {
            usuario.setCaminhoFoto("");
        }else {
            usuario.setCaminhoFoto(firebaseUser.getPhotoUrl().toString());
        }

        return usuario;
    }

}
