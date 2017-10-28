package my.wf.demo.cloud.author.controller;

import my.wf.demo.cloud.author.config.ApiAuthorProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController("/api/v1/check")
public class CheckController {

    @Autowired
    private ApiAuthorProperties properties;

    @Value("${message:default}")
    private String message;


    @PostConstruct
    public void postConstruct(){
        System.out.println("----------");
        System.out.println("message: " + properties.getMessage());
        System.out.println("message prop: " + message);
        System.out.println("----------");
    }

    @GetMapping
    public String getMessage(){
        System.out.println(properties.getMessage());
        return "[" + properties.getMessage() + " " + message + "]";
    }


}
