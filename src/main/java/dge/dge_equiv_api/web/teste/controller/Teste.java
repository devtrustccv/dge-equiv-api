package dge.dge_equiv_api.web.teste.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class Teste {
    @GetMapping
    public String index(){
        return "hello word";
    }
}
