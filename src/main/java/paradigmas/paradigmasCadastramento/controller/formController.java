package paradigmas.paradigmasCadastramento.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import paradigmas.paradigmasCadastramento.model.*;

import java.util.ArrayList;


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

    @GetMapping("/")
    public ModelAndView home(){
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("cadastros", cadastros);
        return mv;
    }

    @PostMapping("/")
    public ModelAndView listagem(User user){
        System.out.println(user);
        cadastros.add(user);
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("cadastros", cadastros);


        return mv;
    }

}
