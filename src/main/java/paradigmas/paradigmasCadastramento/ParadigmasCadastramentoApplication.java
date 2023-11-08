package paradigmas.paradigmasCadastramento;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.google.auth.oauth2.GoogleCredentials;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
public class ParadigmasCadastramentoApplication {

	public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
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

		Firestore db = FirestoreClient.getFirestore(app);

		db.collection("users");
		ApiFuture<QuerySnapshot> query = db.collection("users").get();

		QuerySnapshot querySnapshot = query.get();
		List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
		for (QueryDocumentSnapshot document : documents) {
			System.out.println("User: " + document.getData().get("nome"));
		}

		SpringApplication.run(ParadigmasCadastramentoApplication.class, args);

	}

}
