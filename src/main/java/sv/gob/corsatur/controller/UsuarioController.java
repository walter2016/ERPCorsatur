package sv.gob.corsatur.controller;

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

import sv.gob.corsatur.enums.RolNombre;
import sv.gob.corsatur.model.Empleado;
import sv.gob.corsatur.model.Rol;
import sv.gob.corsatur.model.Usuario;
import sv.gob.corsatur.service.EmpleadoService;
import sv.gob.corsatur.service.RolService;
import sv.gob.corsatur.service.UsuarioService;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	RolService rolService;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	EmpleadoService empleadoService;

	@GetMapping("/registro")
	public String registro() {
		return "registro";
	}

	@PostMapping("/registrar")
	public ModelAndView registrar(String nombreUsuario, String password, @RequestParam String primerNombre,
			@RequestParam String segundoNombre, @RequestParam String primerApellido,
			@RequestParam String segundoApellido, @RequestParam String email) {
		ModelAndView mv = new ModelAndView();
		if (StringUtils.isBlank(nombreUsuario)) {
			mv.setViewName("/registro");
			mv.addObject("error", "el nombre no puede estar vacío");
			return mv;
		}
		if (StringUtils.isBlank(password)) {
			mv.setViewName("/registro");
			mv.addObject("error", "la contraseña no puede estar vacía");
			return mv;
		}
		if (usuarioService.existsByNombreusuario(nombreUsuario)) {
			mv.setViewName("/registro");
			mv.addObject("error", "ese nombre de usuario ya existe");
			return mv;
		}
		if (StringUtils.isBlank(primerNombre)) {
			mv.setViewName("registro");
			mv.addObject("error", "El Primer Nombre no puede estar vacio");

			return mv;
		}
		if (StringUtils.isBlank(primerApellido)) {
			mv.setViewName("registro");
			mv.addObject("error", "El Primer Apellido no puede estar vacio");

			return mv;
		}
		if (StringUtils.isBlank(email)) {
			mv.setViewName("registro");
			mv.addObject("error", "El email no puede estar vacio");

			return mv;
		}
		Usuario usuario = new Usuario();
		usuario.setNombreUsuario(nombreUsuario);
		usuario.setPassword(passwordEncoder.encode(password));
		
		Rol rolUser = rolService.getByRolNombre(RolNombre.ROLE_USER).get();
		Set<Rol> roles = new HashSet<>();
		roles.add(rolUser);
		usuario.setRoles(roles);
		usuarioService.save(usuario);
		

		Empleado empleado = new Empleado(primerNombre, segundoNombre, primerApellido, segundoApellido, email,
				new Date(), nombreUsuario, "A");

		empleado.setUsuario(usuario);
		empleadoService.save(empleado);
		
		
		mv.setViewName("/login");
		mv.addObject("registroOK", "Cuenta creada, " + usuario.getNombreUsuario() + ", ya puedes iniciar sesión");
		return mv;
	}

}