package org.jeremyworkspace.reviewsmanager.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Value("${app.version}")
    private String appVersion;

    // List of front end urls available for which we need to serve the content.
    @GetMapping({
            "/",
            "/inscription",
            "/connexion",
            "/mentionslegales",
            "/mesclassements",
            "/gestionclassements",
            "/editerclassement/*",
            "/consulterclassement/*",
            "/deconnexion"
    })
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView mav=new ModelAndView("index");
        return mav;
    }


    @GetMapping("/version")
    public ResponseEntity<String> version() {
        return new ResponseEntity<String>(appVersion, HttpStatus.OK);
    }

}