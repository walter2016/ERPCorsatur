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

import sv.gob.corsatur.model.Motoristas;
import sv.gob.corsatur.model.Usuario;
import sv.gob.corsatur.service.MotoristaService;
import sv.gob.corsatur.service.UsuarioService;
import sv.gob.corsatur.utils.paginator.PageRender;

@Controller
@RequestMapping("/motoristas")
public class MotoristasController {
	
	@Autowired
	MotoristaService motoristaService;
	
	@Autowired
	UsuarioService usuarioService;

	

	@GetMapping("/lista")
	public String listaActivo(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 10);

		Page<Motoristas> motoristas = motoristaService.obtenerActivos(pageRequest);

		PageRender<Motoristas> pageRender = new PageRender<Motoristas>("/motoristas/lista/", motoristas);

		model.addAttribute("list", motoristas);
		model.addAttribute("page", pageRender);
		return "/motoristas/lista";
	}
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('TRAN')")
	@GetMapping("nuevo")
	public String nuevo() {
		return "motoristas/nuevo";
	}
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('TRAN')")
	@PostMapping("/guardar")
	public ModelAndView crear(@RequestParam String nombre) {
		ModelAndView mv = new ModelAndView();
		if (StringUtils.isBlank(nombre)) {
			mv.setViewName("motoristas/nuevo");
			mv.addObject("error", "El nombre del motorista no puede estar vacio");
			return mv;
		}

		if (motoristaService.existsByNombre(nombre)) {
			mv.setViewName("motoristas/nuevo");
			mv.addObject("error", "Ese Nombre ya Existe");
			return mv;
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
		Motoristas motorista = new Motoristas();

		motorista.setNombre(nombre);
		motorista.setEstado("A");
		motorista.setUserCreate( usuario.getNombreUsuario());
		motorista.setDisponible("S");
		motoristaService.save(motorista);
		mv.setViewName("redirect:/motoristas/lista");
		return mv;
	}
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('TRAN')")
	@GetMapping("/editar/{motoristaId}")
	public ModelAndView editar(@PathVariable("motoristaId") int id) {
		if (!motoristaService.existsById(id))
			return new ModelAndView("redirect:/motoristas/lista");
		Motoristas motorista = motoristaService.getOne(id).get();
		ModelAndView mv = new ModelAndView("/motoristas/editar");
		mv.addObject("motorista", motorista);
		return mv;
	}
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('TRAN')")
	@PostMapping("/actualizar")
	public ModelAndView actualizar(@RequestParam int motoristaId, @RequestParam String nombre) {
		if (!motoristaService.existsById(motoristaId))
			return new ModelAndView("redirect:/motoristas/lista");
		ModelAndView mv = new ModelAndView();
		Motoristas motorista = motoristaService.getOne(motoristaId).get();

		if (StringUtils.isBlank(nombre)) {
			mv.setViewName("motoristas/editar");
			mv.addObject("motorista", motorista);
			mv.addObject("error", "el nombre no puede estar vacío");
			return mv;
		}
		if (motoristaService.existsByNombre(nombre) && motoristaService.getByNombre(nombre).get().getMotoristaId() != motoristaId) {
			mv.setViewName("motoristas/editar");
			mv.addObject("error", "ese nombre ya existe");
			mv.addObject("motorista", motorista);
			return mv;
		}

		motorista.setNombre(nombre);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
		motorista.setUserUpdate(usuario.getNombreUsuario());
		motoristaService.save(motorista);
		return new ModelAndView("redirect:/motoristas/lista");
	}
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('TRAN')")
	@GetMapping("/borrar/{motoristaId}")
	public ModelAndView borrar(@PathVariable("motoristaId") int motoristaId) {
		if (motoristaService.existsById(motoristaId)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
			motoristaService.eliminar(motoristaId, new Date(), usuario.getNombreUsuario());
			return new ModelAndView("redirect:/motoristas/lista");
		}
		return null;
	}
	

}
