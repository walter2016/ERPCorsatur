package sv.gob.corsatur.controller.ActivoFijo;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import sv.gob.corsatur.utils.paginator.PageRender;

@Controller
@RequestMapping("/empleado")
public class EmpleadoController {

	@Autowired
	EmpleadoService empleadoService;

	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	RolService rolService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/lista2")
	public ModelAndView list() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/empleado/lista");
		List<Empleado> empleados = empleadoService.obtenerActivos();
		mv.addObject("empleados", empleados);
		return mv;
	}

	@GetMapping("/lista")
	public String listaActivo(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 10);

		Page<Empleado> empleados = empleadoService.obtenerActivos(pageRequest);

		PageRender<Empleado> pageRender = new PageRender<Empleado>("/empleado/lista/", empleados);

		model.addAttribute("empleados", empleados);
		model.addAttribute("page", pageRender);

		return "/empleado/lista";
	}

	//@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@PreAuthorize("hasRole('ADMIN') or hasRole('ACTI')")
	@GetMapping("nuevo")
	public String nuevo() {

		return "empleado/nuevo";
	}


	@PreAuthorize("hasRole('ADMIN') or hasRole('ACTI')")
	@PostMapping("/guardar")
	public ModelAndView crear(@RequestParam String usuario,@RequestParam String primerNombre, @RequestParam String segundoNombre,
			@RequestParam String primerApellido, @RequestParam String segundoApellido, @RequestParam String email) {
		ModelAndView mv = new ModelAndView();
		if (StringUtils.isBlank(primerNombre)) {
			mv.setViewName("empleado/nuevo");
			mv.addObject("error", "El Primer Nombre no puede estar vacio");

			return mv;
		}
		if (StringUtils.isBlank(usuario)) {
			mv.setViewName("empleado/nuevo");
			mv.addObject("error", "Usuario no puede estar vacio");

			return mv;
		}
		if (StringUtils.isBlank(primerApellido)) {
			mv.setViewName("empleado/nuevo");
			mv.addObject("error", "El Primer Apellido no puede estar vacio");

			return mv;
		}
		if (StringUtils.isBlank(email)) {
			mv.setViewName("empleado/nuevo");
			mv.addObject("error", "El email no puede estar vacio");

			return mv;
		}
		
		Usuario usuario1 = new Usuario();
		usuario1.setNombreUsuario(usuario);
		usuario1.setPassword(passwordEncoder.encode(usuario));
		
		Rol rolUser = rolService.getByRolNombre(RolNombre.ROLE_USER).get();
		Set<Rol> roles = new HashSet<>();
		roles.add(rolUser);
		usuario1.setRoles(roles);
		usuarioService.save(usuario1);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario user = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();

		Empleado empleado = new Empleado(primerNombre, segundoNombre, primerApellido, segundoApellido, email,
				new Date(), user.getNombreUsuario(), "A");
		
		empleado.setUsuario(usuario1);
		empleadoService.save(empleado);
		mv.setViewName("redirect:/empleado/lista");
		return mv;
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('ACTI')")
	@GetMapping("/editar/{empleadoId}")
	public ModelAndView editar(@PathVariable("empleadoId") int id) {
		if (!empleadoService.existsById(id))
			return new ModelAndView("redirect:/empleado/lista");
		Empleado empleado = empleadoService.getOne(id).get();
		ModelAndView mv = new ModelAndView("/empleado/editar");
		mv.addObject("empleado", empleado);

		return mv;
	}
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/rol/{empleadoId}")
	public ModelAndView editarRol(@PathVariable("empleadoId") int id) {
		if (!empleadoService.existsById(id))
			return new ModelAndView("redirect:/empleado/lista");
		Empleado empleado = empleadoService.getOne(id).get();
		
		System.out.println(empleado.getUsuario().getId());
		List<Rol> rolesUsuario = rolService.obtenerRoles(empleado.getUsuario().getId());
		List<Rol> roles = rolService.obtenerTodosRoles();
		
		ModelAndView mv = new ModelAndView("/empleado/rol");
		mv.addObject("empleado", empleado);
		mv.addObject("rolesUsuario", rolesUsuario);
		mv.addObject("roles", roles);

		return mv;
	}

	
	

	@PreAuthorize("hasRole('ADMIN') or hasRole('ACTI')")
	@PostMapping("/actualizar")
	public ModelAndView actualizar(@RequestParam String usuario,@RequestParam int empleadoId, @RequestParam String primerNombre,
			@RequestParam String segundoNombre, @RequestParam String primerApellido,
			@RequestParam String segundoApellido, @RequestParam String email) {
		if (!empleadoService.existsById(empleadoId))
			return new ModelAndView("redirect:/empelado/lista");
		ModelAndView mv = new ModelAndView();
		Empleado empleado = empleadoService.getOne(empleadoId).get();

		if (StringUtils.isBlank(primerNombre)) {
			mv.setViewName("empleado/editar");
			mv.addObject("empleado", empleado);
			mv.addObject("error", "el Primer Nombre no puede estar vacío");

			return mv;
		}
		if (StringUtils.isBlank(usuario)) {
			mv.setViewName("empleado/editar");
			mv.addObject("empleado", empleado);
			mv.addObject("error", "El Usuario no puede estar vacío");

			return mv;
		}
		if (StringUtils.isBlank(primerApellido)) {
			mv.setViewName("empleado/editar");
			mv.addObject("empleado", empleado);
			mv.addObject("error", "el Primer Apellido no puede estar vacío");

			return mv;
		}
		if (StringUtils.isBlank(email)) {
			mv.setViewName("empleado/editar");
			mv.addObject("empleado", empleado);
			mv.addObject("error", "el email no puede estar vacío");

			return mv;
		}

		empleado.setPrimerNombre(primerNombre);
		empleado.setSegundoNombre(segundoNombre);
		empleado.setPrimerApellido(primerApellido);
		empleado.setSegundoApellido(segundoApellido);
		empleado.setEmail(email);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario1 = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
		
		Usuario user = usuarioService.getById(empleado.getUsuario().getId()).get();
		
		user.setNombreUsuario(usuario);
		usuarioService.save(user);
		empleado.setUserUpdate(usuario1.getNombreUsuario());
		empleadoService.save(empleado);
		return new ModelAndView("redirect:/empleado/lista");
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('ACTI')")
	@GetMapping("/borrar/{empleadoId}")
	public ModelAndView borrar(@PathVariable("empleadoId") int empleadoId) {
		if (empleadoService.existsById(empleadoId)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
			empleadoService.eliminar(empleadoId, new Date(), usuario.getNombreUsuario());
			return new ModelAndView("redirect:/empleado/lista");
		}
		return null;
	}
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/reset/{empleadoId}")
	public ModelAndView resetear(@PathVariable("empleadoId") int empleadoId) {
		if (empleadoService.existsById(empleadoId)) {
			
			Empleado empleado = empleadoService.getOne(empleadoId).get();
			
			Usuario user = usuarioService.getByNombreUsuario(empleado.getUsuario().getNombreUsuario()).get();
			user.setPassword(passwordEncoder.encode(empleado.getUsuario().getNombreUsuario()));
			usuarioService.save(user);
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
			empleado.setUserUpdate(usuario.getNombreUsuario());
			empleadoService.save(empleado);
			return new ModelAndView("redirect:/empleado/lista");
		}
		return null;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/guardarrol")
	public ModelAndView actualizarRol(@RequestParam int rolId,@RequestParam int empleadoId) {
		if (!empleadoService.existsById(empleadoId))
			return new ModelAndView("redirect:/empleado/lista");
		ModelAndView mv = new ModelAndView();
		Empleado empleado = empleadoService.getOne(empleadoId).get();
		Usuario usaurio = usuarioService.getById(empleado.getUsuario().getId()).get();

		Rol rolUser = null;
		if(rolId==1) {
		 rolUser = rolService.getByRolNombre(RolNombre.ROLE_ADMIN).get();
		}
		if(rolId==2) {
			 rolUser = rolService.getByRolNombre(RolNombre.ROLE_USER).get();
			}
		if(rolId==3) {
			 rolUser = rolService.getByRolNombre(RolNombre.ROLE_HELP).get();
			}
		if(rolId==4) {
			 rolUser = rolService.getByRolNombre(RolNombre.ROLE_ACTI).get();
			}
		if(rolId==5) {
			 rolUser = rolService.getByRolNombre(RolNombre.ROLE_TRAN).get();
			}

		Set<Rol> roles = new HashSet<>();
		roles.add(rolUser);
		usaurio.setRoles(roles);
		usuarioService.save(usaurio);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario1 = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
		

		empleado.setUserUpdate(usuario1.getNombreUsuario());
		empleadoService.save(empleado);
		return new ModelAndView("redirect:/empleado/lista");
	}

}
