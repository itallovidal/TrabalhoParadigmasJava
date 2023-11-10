package paradigmas.paradigmasCadastramento.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import paradigmas.paradigmasCadastramento.database.Database;
import paradigmas.paradigmasCadastramento.model.User;

import java.util.Objects;


@Controller
public class formController {

    @Autowired
    Database db;
    @GetMapping("/")
    public ModelAndView listagem() {
            ModelAndView mv = new ModelAndView("index");
            mv.addObject("cadastros", db.getCadastros());
            return mv;
        }

    @PostMapping("/")
    public ModelAndView postUser(User user) {
        System.out.println(user);
        if(Objects.equals(user.name(), "") || Objects.equals(user.hobbie(), "") || Objects.equals(user.password(), "")){
            ModelAndView mv = new ModelAndView("indexEmpty");
            mv.addObject("cadastros", db.getCadastros());
            return mv;
        }

        try{
            String documentID = db.logUser(user.name(), user.password());
            // fazer a atualização
            db.updateUser(documentID, user.hobbie());
            ModelAndView mv = new ModelAndView("indexUpdated");
            mv.addObject("cadastros", db.getCadastros());
            return mv;
        }catch (Exception e){
            // verifica se o nome é valido ou se já existe algum com esse nome
            boolean checkingUser = db.checkNameInUse(user.name());
            if(checkingUser){
                ModelAndView mv = new ModelAndView("indexError");
                mv.addObject("cadastros", db.getCadastros());
                return mv;
            }

            // caso o nome seja valido, eu posso fazer o upload desta pessoa.
            db.uploadUser(user);
            ModelAndView mv = new ModelAndView("indexSuccess");
            mv.addObject("cadastros", db.getCadastros());
            return mv;
        }
    }
}
