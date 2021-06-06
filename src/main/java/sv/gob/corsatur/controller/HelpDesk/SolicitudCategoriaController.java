package sv.gob.corsatur.controller.HelpDesk;

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

import sv.gob.corsatur.model.Categoria;
import sv.gob.corsatur.model.Gerencia;
import sv.gob.corsatur.model.SolicitudCategoria;
import sv.gob.corsatur.model.Usuario;
import sv.gob.corsatur.service.SolicitudCategoriaService;
import sv.gob.corsatur.service.UsuarioService;
import sv.gob.corsatur.utils.paginator.PageRender;

@Controller
@RequestMapping("/solicitudcategoria")
public class SolicitudCategoriaController {
	
	
	@Autowired
	SolicitudCategoriaService solicitudCategoriaService;
	
	
	@Autowired
	UsuarioService usuarioService;
	
	@GetMapping("/lista")
	public String listaActivo(@RequestParam(name="page", defaultValue="0") int page, Model model) {
		
		Pageable pageRequest = PageRequest.of(page, 10);

		Page<SolicitudCategoria> SolciitudCtegorias = solicitudCategoriaService.obtenerActivosPaginados(pageRequest);
		
		PageRender<SolicitudCategoria> pageRender = new PageRender<SolicitudCategoria>("/solicitudcategoria/lista/", SolciitudCtegorias);
		
		model.addAttribute("list", SolciitudCtegorias);
		model.addAttribute("page", pageRender);

		
		return "/solicitudcategoria/lista";
	}
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('HELP')")
	@GetMapping("nuevo")
	public String nuevo() {
		return "solicitudcategoria/nuevo";
	}
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('HELP')")
	@PostMapping("/guardar")
	public ModelAndView crear(@RequestParam String categoria) {
		ModelAndView mv = new ModelAndView();
		if (StringUtils.isBlank(categoria)) {
			mv.setViewName("solicitudcategoria/nuevo");
			mv.addObject("error", "El nocategoria no puede estar vacio");
			return mv;
		}

		if (solicitudCategoriaService.existsByCategoria(categoria)) {
			mv.setViewName("solicitudcategoria/nuevo");
			mv.addObject("error", "Ese Categoria ya Existe");
			return mv;
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
		SolicitudCategoria sociliCategoria = new SolicitudCategoria();
		sociliCategoria.setCategoria(categoria);
		sociliCategoria.setEstado("A");
		sociliCategoria.setUserCreate( usuario.getNombreUsuario());
		solicitudCategoriaService.save(sociliCategoria);
		mv.setViewName("redirect:/solicitudcategoria/lista");
		return mv;
	}
	
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('HELP')")
	@GetMapping("/editar/{solicCateId}")
	public ModelAndView editar(@PathVariable("solicCateId") int id) {
		if (!solicitudCategoriaService.existsById(id))
			return new ModelAndView("redirect:/solicitudcategoria/lista");
		SolicitudCategoria sociliCategoria = solicitudCategoriaService.getOne(id).get();
		ModelAndView mv = new ModelAndView("/solicitudcategoria/editar");
		mv.addObject("sociliCategoria", sociliCategoria);
		return mv;
	}
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('HELP')")
	@PostMapping("/actualizar")
	public ModelAndView actualizar(@RequestParam int solicCateId, @RequestParam String categoria) {
		if (!solicitudCategoriaService.existsById(solicCateId))
			return new ModelAndView("redirect:/solicitudcategoria/lista");
		ModelAndView mv = new ModelAndView();
		SolicitudCategoria sociliCategoria = solicitudCategoriaService.getOne(solicCateId).get();

		if (StringUtils.isBlank(categoria)) {
			mv.setViewName("solicitudcategoria/editar");
			mv.addObject("sociliCategoria", sociliCategoria);
			mv.addObject("error", "La categoria no puede estar vacío");
			return mv;
		}
		if (solicitudCategoriaService.existsByCategoria(categoria) && solicitudCategoriaService.getByCategoria(categoria).get().getSolicCateId() != solicCateId) {
			mv.setViewName("solicitudcategoria/editar");
			mv.addObject("error", "ese nombre ya existe");
			mv.addObject("sociliCategoria", sociliCategoria);
			return mv;
		}

		sociliCategoria.setCategoria(categoria);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
		sociliCategoria.setUserUpdate(usuario.getNombreUsuario());
		solicitudCategoriaService.save(sociliCategoria);
		return new ModelAndView("redirect:/solicitudcategoria/lista");
	}
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('HELP')")
	@GetMapping("/borrar/{solicCateId}")
	public ModelAndView borrar(@PathVariable("solicCateId") int solicCateId) {
		if (solicitudCategoriaService.existsById(solicCateId)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
			solicitudCategoriaService.eliminar(solicCateId, new Date(), usuario.getNombreUsuario());
			return new ModelAndView("redirect:/solicitudcategoria/lista");
		}
		return null;
	}

	

}
