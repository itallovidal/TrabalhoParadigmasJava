package paradigmas.paradigmasCadastramento.controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import paradigmas.paradigmasCadastramento.model.*;
import paradigmas.paradigmasCadastramento.ParadigmasCadastramentoApplication.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;



@Controller
public class formController {

    ArrayList<User> cadastros = new ArrayList<User>(){
        {
            add(new User("itallo", "12345", "jogar"));
            add(new User("thaissa", "12345", "futebol"));
            add(new User("manu", "12345", "desenhar"));
            add(new User("thayna", "12345", "ler"));
        }
    };

//    @GetMapping("/")
//    public ModelAndView home(){
//        ModelAndView mv = new ModelAndView("index");
//        mv.addObject("cadastros", cadastros);
//        return mv;
//    }

    @PostMapping("/")
    public ModelAndView listagem(User user){
        System.out.println(user);
        cadastros.add(user);
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("cadastros", cadastros);


        return mv;
    }

    @GetMapping("/")
    public void teste() throws ExecutionException, InterruptedException {

        Firestore db = FirestoreClient.getFirestore();

        db.collection("users");
        ApiFuture<QuerySnapshot> query = db.collection("users").get();

        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            System.out.println("User: " + document.getId());
        }
    }



}
