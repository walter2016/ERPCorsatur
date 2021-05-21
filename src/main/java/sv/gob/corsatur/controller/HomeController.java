package sv.gob.corsatur.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class HomeController {
	
	@GetMapping(value = {"/","/home"})
	public String index() {
		
		 return "home";
		
	}
	
	  @GetMapping("/login")
	    public String login(){
	        return "login";
	    }

	    @GetMapping("/forbidden")
	    public String forbidden(){
	        return "forbidden";
	    }

}
