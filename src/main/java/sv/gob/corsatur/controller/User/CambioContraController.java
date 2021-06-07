package sv.gob.corsatur.controller.User;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import sv.gob.corsatur.model.Usuario;
import sv.gob.corsatur.service.UsuarioService;

@Controller
@RequestMapping("/user")
public class CambioContraController {

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@GetMapping("cambio")
	public String nuevo() {
		return "user/cambiocontra";
	}

	@PostMapping("/actualizar")
	public ModelAndView actualizarContra( @RequestParam String newContra,
			@RequestParam String newContraRep) {
		ModelAndView mv = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
		if (StringUtils.isBlank(newContra)) {
			mv.setViewName("user/cambiocontra");
			mv.addObject("error", "Porfavor digite la nueva contraseña");
			return mv;
		}
		if (StringUtils.isBlank(newContraRep)) {
			mv.setViewName("user/cambiocontra");
			mv.addObject("error", "Porfavor repita la contraseña");
			return mv;
		}
		if (!newContra.equals(newContraRep)) {
			mv.setViewName("user/cambiocontra");
			mv.addObject("error", "La contraseña no coinciden");
			return mv;
		}
		
		
		usuario.setPassword(passwordEncoder.encode(newContra));
		
		usuarioService.save(usuario);

		mv.setViewName("redirect:/logout");
		return mv;
	}

}
