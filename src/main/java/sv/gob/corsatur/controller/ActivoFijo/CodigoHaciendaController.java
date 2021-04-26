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

import sv.gob.corsatur.model.CodigoHacienda;
import sv.gob.corsatur.model.Usuario;
import sv.gob.corsatur.service.CodigoHaciendaService;
import sv.gob.corsatur.service.UsuarioService;
import sv.gob.corsatur.utils.paginator.PageRender;

@Controller
@RequestMapping("/hacienda")
public class CodigoHaciendaController {

	@Autowired
	CodigoHaciendaService codigoHaciendaService;
	
	@Autowired
	UsuarioService usuarioService;

	@GetMapping("/lista")
	public String listaActivo(@RequestParam(name="page", defaultValue="0") int page, Model model) {
		
		Pageable pageRequest = PageRequest.of(page, 10);

		 
		Page<CodigoHacienda> hacienda = codigoHaciendaService.obtenerActivos(pageRequest);
		
		PageRender<CodigoHacienda> pageRender = new PageRender<CodigoHacienda>("/hacienda/lista/", hacienda);
		
		model.addAttribute("list", hacienda);
		model.addAttribute("page", pageRender);

		
		return "/hacienda/lista";
	}
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("nuevo")
	public String nuevo() {
		return "hacienda/nuevo";
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/guardar")
	public ModelAndView crear(@RequestParam String codigo,@RequestParam String nombre) {
		ModelAndView mv = new ModelAndView();
		if (StringUtils.isBlank(codigo)) {
			mv.setViewName("hacienda/nuevo");
			mv.addObject("error", "El Codigo no puede estar vacio");
			return mv;
		}
		if (StringUtils.isBlank(nombre)) {
			mv.setViewName("hacienda/nuevo");
			mv.addObject("error", "El Nombre no puede estar vacio");
			return mv;
		}

		if (codigoHaciendaService.existsByCodigo(codigo)){
			mv.setViewName("hacienda/nuevo");
			mv.addObject("error", "Ese Codigo ya Existe");
			return mv;
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
		

		CodigoHacienda hacienda = new CodigoHacienda(codigo,nombre, usuario.getNombreUsuario(), "A");
		codigoHaciendaService.save(hacienda);
		mv.setViewName("redirect:/hacienda/lista");
		return mv;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/editar/{haciendaId}")
	public ModelAndView editar(@PathVariable("haciendaId") int id) {
		if (!codigoHaciendaService.existsById(id))
			return new ModelAndView("redirect:/hacienda/lista");
		CodigoHacienda hacienda = codigoHaciendaService.getOne(id).get();

		ModelAndView mv = new ModelAndView("/hacienda/editar");
		mv.addObject("hacienda", hacienda);
		return mv;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/actualizar")
	public ModelAndView actualizar(@RequestParam int haciendaId, @RequestParam String codigo, @RequestParam String nombre) {
		if (!codigoHaciendaService.existsById(haciendaId))
			return new ModelAndView("redirect:/hacienda/lista");
		ModelAndView mv = new ModelAndView();
		
		CodigoHacienda hacienda = codigoHaciendaService.getOne(haciendaId).get();
		if (StringUtils.isBlank(codigo)) {
			mv.setViewName("hacienda/editar");
			mv.addObject("hacienda", hacienda);
			mv.addObject("error", "El Codigo no puede estar vacío");
			return mv;
		}
		if (StringUtils.isBlank(nombre)) {
			mv.setViewName("hacienda/editar");
			mv.addObject("hacienda", hacienda);
			mv.addObject("error", "El Nombre no puede estar vacío");
			return mv;
		}
		if (codigoHaciendaService.existsByCodigo(codigo) && codigoHaciendaService.getByCodigo(codigo).get().getHaciendaId() != haciendaId) {
			mv.setViewName("hacienda/editar");
			mv.addObject("error", "Es Codigo ya existe");
			mv.addObject("hacienda", hacienda);
			return mv;
		}
		
		hacienda.setCodigo(codigo);
		hacienda.setNombre(nombre);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
		hacienda.setUserUpdate(usuario.getNombreUsuario());
		codigoHaciendaService.save(hacienda);
		return new ModelAndView("redirect:/hacienda/lista");
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/borrar/{haciendaId}")
	public ModelAndView borrar(@PathVariable("haciendaId") int haciendaId) {
		if (codigoHaciendaService.existsById(haciendaId)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
			codigoHaciendaService.eliminar(haciendaId, new Date(), usuario.getNombreUsuario());
			return new ModelAndView("redirect:/hacienda/lista");
		}
		return null;
	}


}
