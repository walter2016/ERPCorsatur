package sv.gob.corsatur.controller.Soporte;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import sv.gob.corsatur.model.Configuracion;
import sv.gob.corsatur.model.Usuario;
import sv.gob.corsatur.service.ConfiguracionService;
import sv.gob.corsatur.service.UsuarioService;
import sv.gob.corsatur.utils.paginator.PageRender;

@Controller
@RequestMapping("/configuracion")
public class ConfiguracionController {

	@Autowired
	ConfiguracionService configuracionService;

	@Autowired
	UsuarioService usuarioService;

	@GetMapping("/lista")
	public String listaActivo(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 10);

		Page<Configuracion> configuraciones = configuracionService.obtenerActivos(pageRequest);

		PageRender<Configuracion> pageRender = new PageRender<Configuracion>("/configuracion/lista/", configuraciones);

		model.addAttribute("list", configuraciones);
		model.addAttribute("page", pageRender);

		return "/configuracion/lista";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("nuevo")
	public String nuevo() {
		return "configuracion/nuevo";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/guardar")
	public ModelAndView crear(@RequestParam String codigoClasificacion, @RequestParam String valor,
			@RequestParam String descripcion) {
		ModelAndView mv = new ModelAndView();
		if (StringUtils.isBlank(codigoClasificacion)) {
			mv.setViewName("configuracion/nuevo");
			mv.addObject("error", "El codigo de clasificaion no puede estar vacio");
			return mv;
		}
		if (StringUtils.isBlank(valor)) {
			mv.setViewName("configuracion/nuevo");
			mv.addObject("error", "El valor de clasificaion no puede estar vacio");
			return mv;
		}
		if (StringUtils.isBlank(descripcion)) {
			mv.setViewName("configuracion/nuevo");
			mv.addObject("error", "La descripcion de clasificaion no puede estar vacio");
			return mv;
		}

		if (configuracionService.existsByNombre(codigoClasificacion)) {
			mv.setViewName("configuracion/nuevo");
			mv.addObject("error", "Ese Codigo ya Existe");
			return mv;
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
		Configuracion configuracion = new Configuracion();
		configuracion.setCodigoClasificacion(codigoClasificacion);
		configuracion.setValor(valor);
		configuracion.setDescripcion(descripcion);
		configuracion.setUserCreate(usuario.getNombreUsuario());
		configuracion.setCreateDate(new Date());
		configuracion.setEstado("A");

		configuracionService.save(configuracion);
		mv.setViewName("redirect:/configuracion/lista");
		return mv;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/editar/{configuracionId}")
	public ModelAndView editar(@PathVariable("configuracionId") int id) {
		if (!configuracionService.existsById(id))
			return new ModelAndView("redirect:/configuracion/lista");
		Configuracion configuracion = configuracionService.getOne(id).get();

		ModelAndView mv = new ModelAndView("/configuracion/editar");
		mv.addObject("configuracion", configuracion);
		return mv;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/actualizar")
	public ModelAndView actualizar(@RequestParam int configuracionId, @RequestParam String codigoClasificacion,
			@RequestParam String valor, @RequestParam String descripcion) {
		if (!configuracionService.existsById(configuracionId))
			return new ModelAndView("redirect:/configuracion/lista");
		ModelAndView mv = new ModelAndView();
		Configuracion configuracion = configuracionService.getOne(configuracionId).get();
		if (StringUtils.isBlank(codigoClasificacion)) {
			mv.setViewName("configuracion/nuevo");
			mv.addObject("configuracion", configuracion);
			mv.addObject("error", "El codigo de clasificaion no puede estar vacio");
			return mv;
		}
		if (StringUtils.isBlank(valor)) {
			mv.setViewName("configuracion/nuevo");
			mv.addObject("configuracion", configuracion);
			mv.addObject("error", "El valor de clasificaion no puede estar vacio");
			return mv;
		}
		if (StringUtils.isBlank(descripcion)) {
			mv.setViewName("configuracion/nuevo");
			mv.addObject("configuracion", configuracion);
			mv.addObject("error", "La descripcion de clasificaion no puede estar vacio");
			return mv;
		}

		if (configuracionService.existsByNombre(codigoClasificacion) && configuracionService.getByCodigoClasificacion(codigoClasificacion).get().getConfiguracionId() != configuracionId) {
			mv.setViewName("configuracion/nuevo");
			mv.addObject("configuracion", configuracion);
			mv.addObject("error", "Ese Codigo ya Existe");
			return mv;
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();

		configuracion.setCodigoClasificacion(codigoClasificacion);
		configuracion.setValor(valor);
		configuracion.setDescripcion(descripcion);
		configuracion.setUserUpdate(usuario.getNombreUsuario());
		

		configuracionService.save(configuracion);
		return new ModelAndView("redirect:/configuracion/lista");
	}

}
