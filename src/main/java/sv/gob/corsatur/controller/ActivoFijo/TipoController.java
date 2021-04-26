package sv.gob.corsatur.controller.ActivoFijo;

import java.util.Date;
import java.util.List;

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
import sv.gob.corsatur.model.Tipo;
import sv.gob.corsatur.model.Usuario;
import sv.gob.corsatur.service.CategoriaService;
import sv.gob.corsatur.service.TipoService;
import sv.gob.corsatur.service.UsuarioService;
import sv.gob.corsatur.utils.paginator.PageRender;

@Controller
@RequestMapping("/tipo")
public class TipoController {
	
	@Autowired
	TipoService tipoService;
	
	@Autowired
	CategoriaService categoriaService;
	
	@Autowired
	UsuarioService usuarioService;

	@GetMapping("/lista")
	public String listaActivo(@RequestParam(name="page", defaultValue="0") int page, Model model) {
		
		Pageable pageRequest = PageRequest.of(page, 10);

		Page<Tipo> tipo = tipoService.obtenerActivos(pageRequest);
		
		PageRender<Tipo> pageRender = new PageRender<Tipo>("/tipo/lista/", tipo);
		
		model.addAttribute("list", tipo);
		model.addAttribute("page", pageRender);

		
		return "/tipo/lista";
	}
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("nuevo")
	public String nuevo(Model model) {
		List<Categoria> categorias = categoriaService.obtenerActivos();
		model.addAttribute("categorias", categorias);
		return "tipo/nuevo";
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/guardar")
	public ModelAndView crear(@RequestParam String codigo,@RequestParam String nombre,@RequestParam Integer categoriaId, Model model) {
		ModelAndView mv = new ModelAndView();
		List<Categoria> categorias = categoriaService.obtenerActivos();
		if (StringUtils.isBlank(codigo)) {
			mv.setViewName("tipo/nuevo");
			mv.addObject("error", "El Codigo no puede estar vacio");
			
			model.addAttribute("categorias", categorias);
			return mv;
		}
		if (StringUtils.isBlank(nombre)) {
			mv.setViewName("tipo/nuevo");
			mv.addObject("error", "El Nombre no puede estar vacio");
			model.addAttribute("categorias", categorias);
			return mv;
		}

		if (tipoService.existsByCodigo(codigo)){
			mv.setViewName("area/nuevo");
			mv.addObject("error", "Ese Codigo ya Existe");
			model.addAttribute("categorias", categorias);
			return mv;
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
		
		Categoria categoria = categoriaService.getOne(categoriaId).get();
		
		Tipo tipo = new Tipo(codigo,nombre, usuario.getNombreUsuario(), "A",categoria);
		tipoService.save(tipo);
		mv.setViewName("redirect:/tipo/lista");
		return mv;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/editar/{tipoId}")
	public ModelAndView editar(@PathVariable("tipoId") int id) {
		if (!tipoService.existsById(id))
			return new ModelAndView("redirect:/tipo/lista");
		Tipo tipo = tipoService.getOne(id).get();
		List<Categoria> categorias = categoriaService.obtenerActivos();
		ModelAndView mv = new ModelAndView("/tipo/editar");
		mv.addObject("tipo", tipo);
		mv.addObject("categorias",categorias);
		return mv;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/actualizar")
	public ModelAndView actualizar(@RequestParam int tipoId, @RequestParam String codigo, @RequestParam String nombre, @RequestParam Integer categoriaId) {
		if (!tipoService.existsById(tipoId))
			return new ModelAndView("redirect:/tipo/lista");
		ModelAndView mv = new ModelAndView();
		
		Tipo tipo = tipoService.getOne(tipoId).get();
		List<Categoria> categorias = categoriaService.obtenerActivos();
		if (StringUtils.isBlank(codigo)) {
			mv.setViewName("tipo/editar");
			mv.addObject("tipo", tipo);
			mv.addObject("error", "El Codigo no puede estar vacío");
			mv.addObject("categorias",categorias);
			return mv;
		}
		if (StringUtils.isBlank(nombre)) {
			mv.setViewName("tipo/editar");
			mv.addObject("tipo", tipo);
			mv.addObject("error", "El Nombre no puede estar vacío");
			mv.addObject("categorias",categorias);
			return mv;
		}if (tipoService.existsByCodigo(codigo) && tipoService.getByCodigo(codigo).get().getTipoId() != tipoId) {
			mv.setViewName("tipo/editar");
			mv.addObject("error", "Es Tipo ya existe");
			mv.addObject("tipo", tipo);
			mv.addObject("categorias",categorias);
			return mv;
		}
		Categoria categoria = categoriaService.getOne(categoriaId).get();
		tipo.setCategoriaId(categoria);
		tipo.setCodigo(codigo);
		tipo.setNombre(nombre);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
		tipo.setUserUpdate(usuario.getNombreUsuario());
		tipoService.save(tipo);
		return new ModelAndView("redirect:/tipo/lista");
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/borrar/{tipoId}")
	public ModelAndView borrar(@PathVariable("tipoId") int tipoId) {
		if (tipoService.existsById(tipoId)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
			tipoService.eliminar(tipoId, new Date(), usuario.getNombreUsuario());
			return new ModelAndView("redirect:/tipo/lista");
		}
		return null;
	}


}
