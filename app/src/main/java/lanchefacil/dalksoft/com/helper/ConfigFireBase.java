package lanchefacil.dalksoft.com.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import lanchefacil.dalksoft.com.model.Anuncio;

public class ConfigFireBase {
    private static DatabaseReference referenciaDatabase;
    private static FirebaseAuth referenciaAuth;
    private static StorageReference referenciaStorage;


    public static String getIdUsuario () {
        FirebaseAuth auth = getFirebaseAuth();

        return auth.getCurrentUser().getUid();
    }

    public static DatabaseReference getFirebase () {
        if (referenciaDatabase == null) {
            referenciaDatabase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaDatabase;
    }

    public static FirebaseAuth getFirebaseAuth () {
        if (referenciaAuth == null) {
            referenciaAuth = FirebaseAuth.getInstance();
        }
        return referenciaAuth;
    }

    public static StorageReference getReferenciaStorage () {
        if (referenciaStorage == null) {
            referenciaStorage = FirebaseStorage.getInstance().getReference();
        }
        return referenciaStorage;
    }



}
