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

import sv.gob.corsatur.model.EstadoVehiculo;
import sv.gob.corsatur.model.Usuario;
import sv.gob.corsatur.service.EstadoVehiculoService;
import sv.gob.corsatur.service.UsuarioService;
import sv.gob.corsatur.utils.paginator.PageRender;

@Controller
@RequestMapping("/estadovehiculo")
public class EstadoVehiculoController {
	
	@Autowired
	EstadoVehiculoService estadoVehiculoService;
	
	@Autowired
	UsuarioService usuarioService;
	
	@GetMapping("/lista")
	public String listaActivo(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 10);

		Page<EstadoVehiculo> estadoVehiculo = estadoVehiculoService.obtenerActivos(pageRequest);

		PageRender<EstadoVehiculo> pageRender = new PageRender<EstadoVehiculo>("/estadovehiculo/lista/", estadoVehiculo);

		model.addAttribute("list", estadoVehiculo);
		model.addAttribute("page", pageRender);
		return "/estadovehiculo/lista";
	}
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('ACTI')")
	@GetMapping("nuevo")
	public String nuevo() {
		return "estadovehiculo/nuevo";
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('ACTI')")
	@PostMapping("/guardar")
	public ModelAndView crear(@RequestParam String estVehiculo) {
		ModelAndView mv = new ModelAndView();
		if (StringUtils.isBlank(estVehiculo)) {
			mv.setViewName("estadovehiculo/nuevo");
			mv.addObject("error", "El Estado no puede estar vacio");
			return mv;
		}

		if (estadoVehiculoService.existsByEstVehiculo(estVehiculo)) {
			mv.setViewName("estadovehiculo/nuevo");
			mv.addObject("error", "Ese Estado ya Existe");
			return mv;
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
		EstadoVehiculo estadoVehiculo = new EstadoVehiculo();
		estadoVehiculo.setCreateDate(new Date());
		estadoVehiculo.setEstado("A");
		estadoVehiculo.setEstVehiculo(estVehiculo);
		estadoVehiculo.setUserCreate(usuario.getNombreUsuario());
		estadoVehiculoService.save(estadoVehiculo);
		mv.setViewName("redirect:/estadovehiculo/lista");
		return mv;
	}
	

	@PreAuthorize("hasRole('ADMIN') or hasRole('ACTI')")
	@GetMapping("/editar/{estadoVehiculoId}")
	public ModelAndView editar(@PathVariable("estadoVehiculoId") int id) {
		if (!estadoVehiculoService.existsById(id))
			return new ModelAndView("redirect:/estadovehiculo/lista");
		EstadoVehiculo estadoVehiculo = estadoVehiculoService.getOne(id).get();

		ModelAndView mv = new ModelAndView("/estadovehiculo/editar");
		mv.addObject("estadoVehiculo", estadoVehiculo);
		return mv;
	}

	
	@PreAuthorize("hasRole('ADMIN') or hasRole('ACTI')")
	@PostMapping("/actualizar")
	public ModelAndView actualizar(@RequestParam int estadoVehiculoId, @RequestParam String estVehiculo) {
		if (!estadoVehiculoService.existsById(estadoVehiculoId))
			return new ModelAndView("redirect:/estadovehiculo/lista");
		ModelAndView mv = new ModelAndView();
		EstadoVehiculo estadoVehiculo = estadoVehiculoService.getOne(estadoVehiculoId).get();

		if (StringUtils.isBlank(estVehiculo)) {
			mv.setViewName("estadovehiculo/editar");
			mv.addObject("estadoVehiculo", estadoVehiculo);
			mv.addObject("error", "El Estado no puede estar vacío");
			return mv;
		}
		if (estadoVehiculoService.existsByEstVehiculo(estVehiculo) && estadoVehiculoService.getByEstVehiculo(estVehiculo).get().getEstadoVehiculoId() != estadoVehiculoId) {
			mv.setViewName("estadovehiculo/editar");
			mv.addObject("error", "ese nombre ya existe");
			mv.addObject("estadoVehiculo", estadoVehiculo);
			return mv;
		}

		estadoVehiculo.setEstVehiculo(estVehiculo);
	
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
		estadoVehiculo.setUserUpdate(usuario.getNombreUsuario());
		estadoVehiculoService.save(estadoVehiculo);
		return new ModelAndView("redirect:/estadovehiculo/lista");
	}
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('ACTI')")
	@GetMapping("/borrar/{estadoVehiculoId}")
	public ModelAndView borrar(@PathVariable("estadoVehiculoId") int estadoVehiculoId) {
		if (estadoVehiculoService.existsById(estadoVehiculoId)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
			estadoVehiculoService.eliminar(estadoVehiculoId, new Date(), usuario.getNombreUsuario());
			return new ModelAndView("redirect:/estadovehiculo/lista");
		}
		return null;
	}
	
}
