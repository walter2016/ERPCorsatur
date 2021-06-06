package sv.gob.corsatur.controller.ActivoFijo;

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

import sv.gob.corsatur.model.ClaseVehiculo;
import sv.gob.corsatur.model.Usuario;
import sv.gob.corsatur.service.ClaseVehiculoService;
import sv.gob.corsatur.service.UsuarioService;
import sv.gob.corsatur.utils.paginator.PageRender;

@Controller
@RequestMapping("/clasevehiculo")
public class ClaseVehiculoController {
	
	@Autowired
	ClaseVehiculoService claseVehiculoService;
	
	@Autowired
	UsuarioService usuarioService;
	
	@GetMapping("/lista")
	public String listaActivo(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 10);

		Page<ClaseVehiculo> claseVehiculo = claseVehiculoService.obtenerActivos(pageRequest);

		PageRender<ClaseVehiculo> pageRender = new PageRender<ClaseVehiculo>("/clasevehiculo/lista/", claseVehiculo);

		model.addAttribute("list", claseVehiculo);
		model.addAttribute("page", pageRender);
		return "/clasevehiculo/lista";
	}
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('ACTI')")
	@GetMapping("nuevo")
	public String nuevo() {
		return "clasevehiculo/nuevo";
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('ACTI')")
	@PostMapping("/guardar")
	public ModelAndView crear(@RequestParam String clase) {
		ModelAndView mv = new ModelAndView();
		if (StringUtils.isBlank(clase)) {
			mv.setViewName("clasevehiculo/nuevo");
			mv.addObject("error", "La Clase no puede estar vacia");
			return mv;
		}

		if (claseVehiculoService.existsByClase(clase)) {
			mv.setViewName("clasevehiculo/nuevo");
			mv.addObject("error", "Ese Nombre ya Existe");
			return mv;
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
		ClaseVehiculo clasevehiculo = new ClaseVehiculo();
		clasevehiculo.setCreateDate(new Date());
		clasevehiculo.setEstado("A");
		clasevehiculo.setClase(clase);
		clasevehiculo.setUserCreate(usuario.getNombreUsuario());
		claseVehiculoService.save(clasevehiculo);
		mv.setViewName("redirect:/clasevehiculo/lista");
		return mv;
	}
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('ACTI')")
	@GetMapping("/editar/{claseVehiculoId}")
	public ModelAndView editar(@PathVariable("claseVehiculoId") int id) {
		if (!claseVehiculoService.existsById(id))
			return new ModelAndView("redirect:/clasevehiculo/lista");
		ClaseVehiculo clasevehiculo = claseVehiculoService.getOne(id).get();

		ModelAndView mv = new ModelAndView("/clasevehiculo/editar");
		mv.addObject("claseVehiculo", clasevehiculo);
		return mv;
	}
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('ACTI')")
	@PostMapping("/actualizar")
	public ModelAndView actualizar(@RequestParam int claseVehiculoId, @RequestParam String clase) {
		if (!claseVehiculoService.existsById(claseVehiculoId))
			return new ModelAndView("redirect:/clasevehiculo/lista");
		ModelAndView mv = new ModelAndView();
		ClaseVehiculo clasevehiculo = claseVehiculoService.getOne(claseVehiculoId).get();

		if (StringUtils.isBlank(clase)) {
			mv.setViewName("clasevehiculo/editar");
			mv.addObject("clasevehiculo", clasevehiculo);
			mv.addObject("error", "la clase no puede estar vacío");
			return mv;
		}
		if (claseVehiculoService.existsByClase(clase) && claseVehiculoService.getByClase(clase).get().getClaseVehiculoId() != claseVehiculoId) {
			mv.setViewName("clasevehiculo/editar");
			mv.addObject("error", "ese nombre ya existe");
			mv.addObject("clasevehiculo", clasevehiculo);
			return mv;
		}

		clasevehiculo.setClase(clase);
	
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
		clasevehiculo.setUserUpdate(usuario.getNombreUsuario());
		claseVehiculoService.save(clasevehiculo);
		return new ModelAndView("redirect:/clasevehiculo/lista");
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('ACTI')")
	@GetMapping("/borrar/{claseVehiculoId}")
	public ModelAndView borrar(@PathVariable("claseVehiculoId") int claseVehiculoId) {
		if (claseVehiculoService.existsById(claseVehiculoId)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
			claseVehiculoService.eliminar(claseVehiculoId, new Date(), usuario.getNombreUsuario());
			return new ModelAndView("redirect:/clasevehiculo/lista");
		}
		return null;
	}
	
	
}
