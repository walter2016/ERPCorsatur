package sv.gob.corsatur.controller.Transporte;

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

import sv.gob.corsatur.model.Gerencia;
import sv.gob.corsatur.model.Usuario;
import sv.gob.corsatur.service.GerenciaService;
import sv.gob.corsatur.service.UsuarioService;
import sv.gob.corsatur.utils.paginator.PageRender;

@Controller
@RequestMapping("/gerencia")
public class GerenciaController {
	
	@Autowired
	GerenciaService gerenciaservice;
	
	@Autowired
	UsuarioService usuarioService;

	

	@GetMapping("/lista")
	public String listaActivo(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 10);

		Page<Gerencia> gerencias = gerenciaservice.obtenerActivos(pageRequest);

		PageRender<Gerencia> pageRender = new PageRender<Gerencia>("/gerencia/lista/", gerencias);

		model.addAttribute("list", gerencias);
		model.addAttribute("page", pageRender);
		return "/gerencia/lista";
	}
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("nuevo")
	public String nuevo() {
		return "gerencia/nuevo";
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/guardar")
	public ModelAndView crear(@RequestParam String nombre) {
		ModelAndView mv = new ModelAndView();
		if (StringUtils.isBlank(nombre)) {
			mv.setViewName("gerencia/nuevo");
			mv.addObject("error", "El nombre de la gerencia no puede estar vacio");
			return mv;
		}

		if (gerenciaservice.existsByNombre(nombre)) {
			mv.setViewName("gerencia/nuevo");
			mv.addObject("error", "Ese Nombre ya Existe");
			return mv;
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
		Gerencia gerencia = new Gerencia();
		gerencia.setNombre(nombre);
		gerencia.setEstado("A");
		gerencia.setUserCreate( usuario.getNombreUsuario());
		gerenciaservice.save(gerencia);
		mv.setViewName("redirect:/gerencia/lista");
		return mv;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/editar/{gerenciaId}")
	public ModelAndView editar(@PathVariable("gerenciaId") int id) {
		if (!gerenciaservice.existsById(id))
			return new ModelAndView("redirect:/gerencia/lista");
		Gerencia gerencia = gerenciaservice.getOne(id).get();
		ModelAndView mv = new ModelAndView("/gerencia/editar");
		mv.addObject("gerencia", gerencia);
		return mv;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/actualizar")
	public ModelAndView actualizar(@RequestParam int gerenciaId, @RequestParam String nombre) {
		if (!gerenciaservice.existsById(gerenciaId))
			return new ModelAndView("redirect:/gerencia/lista");
		ModelAndView mv = new ModelAndView();
		Gerencia gerencia = gerenciaservice.getOne(gerenciaId).get();
		if (StringUtils.isBlank(nombre)) {
			mv.setViewName("gerencia/editar");
			mv.addObject("gerencia", gerencia);
			mv.addObject("error", "el nombre no puede estar vacío");
			return mv;
		}
		if (gerenciaservice.existsByNombre(nombre) && gerenciaservice.getByNombre(nombre).get().getGerenciaId() != gerenciaId) {
			mv.setViewName("gerencia/editar");
			mv.addObject("error", "ese nombre ya existe");
			mv.addObject("gerencia", gerencia);
			return mv;
		}

		gerencia.setNombre(nombre);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
		gerencia.setUserUpdate(usuario.getNombreUsuario());
		gerenciaservice.save(gerencia);
		return new ModelAndView("redirect:/gerencia/lista");
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/borrar/{gerenciaId}")
	public ModelAndView borrar(@PathVariable("gerenciaId") int gerenciaId) {
		if (gerenciaservice.existsById(gerenciaId)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
			gerenciaservice.eliminar(gerenciaId, new Date(), usuario.getNombreUsuario());
			return new ModelAndView("redirect:/gerencia/lista");
		}
		return null;
	}

	
	
}
