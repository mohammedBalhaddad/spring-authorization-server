package sample.authentication;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String show(){
		return "login";
	}
}
