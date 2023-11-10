package paradigmas.paradigmasCadastramento.database;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;
import paradigmas.paradigmasCadastramento.ParadigmasCadastramentoApplication;
import paradigmas.paradigmasCadastramento.model.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class Database {

    // retorna conexão com banco de dados
    private Firestore FirestoreDatabaseConfig() throws IOException {
        ClassLoader classLoader = ParadigmasCadastramentoApplication.class.getClassLoader();
        InputStream serviceAccount = classLoader.getResourceAsStream("config/serviceAccountKey.json");

        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();

        FirebaseApp app;

        if (FirebaseApp.getApps().isEmpty()) {
            app = FirebaseApp.initializeApp(options, "paradigmasdb");
        } else {
            app = FirebaseApp.getApps().get(0);
        }

        return  FirestoreClient.getFirestore(app);
    }

    // get all users
    public ArrayList<User> getCadastros(){
        ArrayList<User> cadastros = new ArrayList<>();

        try{
            Firestore db = this.FirestoreDatabaseConfig();

            ApiFuture<QuerySnapshot> query = db.collection("users").get();
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

            for (QueryDocumentSnapshot document : documents) {
                cadastros.add(
                        new User(   document.getData().get("name").toString(),
                                document.getData().get("password").toString(),
                                document.getData().get("hobbie").toString(),
                                document.getData().get("documentID").toString())
                );
            }

            System.out.println(cadastros);
            return cadastros;
        }catch (Exception e){
            System.out.println("erro ao pegar os cadastros");
            return cadastros;
        }
    }

    // upload de usuário
    public boolean uploadUser(User user){
        String uuid = UUID.randomUUID().toString();

        try{
            // criando uma ref ao documento criado
            DocumentReference docRef = this.FirestoreDatabaseConfig().collection("users").document(uuid);

            // criando um hasmap com os dados a serem inseridos
            Map<String, String> data = new HashMap<>();
            data.put("name", user.name());
            data.put("hobbie", user.hobbie());
            data.put("password", user.password());
            data.put("documentID", uuid);

            // de fato escrevendo no banco
            ApiFuture<WriteResult> result = docRef.set(data);

            // result.get() blocks on response
//            System.out.println("Update time : " + result.get());

            // retornando true caso tudo dê certo
            return true;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean updateUser(String documentID, String hobbie){
        try{
            DocumentReference docRef = this.FirestoreDatabaseConfig().collection("users").document(documentID);
            docRef.update("hobbie", hobbie);

//            System.out.println(result);
            // retornando true caso tudo dê certo
            return true;

        }catch (Exception e){
//            System.out.println(e.getMessage());
            System.out.println("erro no update");
            return false;
        }
    }


    public boolean checkNameInUse(String name){
        try{
            Firestore db = this.FirestoreDatabaseConfig();

            // pegando na coleção usuário onde o nome é igual a X
            ApiFuture<QuerySnapshot> query = db.collection("users").whereEqualTo("name", name).get();
            // capturando um snapshot com a query montada
            QuerySnapshot querySnapshot = query.get();
            // extraindo o documento desta query
            QueryDocumentSnapshot document = querySnapshot.getDocuments().get(0);

            // mostrando para saber só
//            System.out.println(document);

            // caso exista a gente retorna true
            return true;
        }catch (Exception e){
            System.out.println("Usuário não existe!");
            return false;
        }
    }

    public String logUser(String name, String password) throws IOException, ExecutionException, InterruptedException {
        Firestore db = this.FirestoreDatabaseConfig();

        // pegando na coleção usuário onde o nome é igual a X
        ApiFuture<QuerySnapshot> query = db.collection("users").whereEqualTo("name", name).whereEqualTo("password", password).get();
        // capturando um snapshot com a query montada
        QuerySnapshot querySnapshot = query.get();
        // extraindo o documento desta query
        QueryDocumentSnapshot document = querySnapshot.getDocuments().get(0);
        return document.getData().get("documentID").toString();
    }




}
