package my.wf.demo.cloud.author.controller;

import my.wf.demo.cloud.author.config.CheckProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController("/api-author/v1/check")
public class CheckController {

    @Autowired
    private CheckProperties properties;

    @PostConstruct
    public void postConstruct(){
        System.out.println("----------");
        System.out.println("message: " + properties.getMessage());
        System.out.println("----------");
    }

    @GetMapping
    public String getMessage(){
        System.out.println(properties.getMessage());
        return "[" + properties.getMessage() + "]";
    }


}
