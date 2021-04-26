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

import sv.gob.corsatur.model.Categoria;
import sv.gob.corsatur.model.Usuario;
import sv.gob.corsatur.service.CategoriaService;
import sv.gob.corsatur.service.UsuarioService;
import sv.gob.corsatur.utils.paginator.PageRender;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {
	
	@Autowired
	CategoriaService categoriaService;
	
	
	@Autowired
	UsuarioService usuarioService;

	@GetMapping("/lista")
	public String listaActivo(@RequestParam(name="page", defaultValue="0") int page, Model model) {
		
		Pageable pageRequest = PageRequest.of(page, 10);

		Page<Categoria> categorias = categoriaService.obtenerActivos(pageRequest);
		
		PageRender<Categoria> pageRender = new PageRender<Categoria>("/categoria/lista/", categorias);
		
		model.addAttribute("list", categorias);
		model.addAttribute("page", pageRender);

		
		return "/categoria/lista";
	}
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("nuevo")
	public String nuevo() {
		return "categoria/nuevo";
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/guardar")
	public ModelAndView crear(@RequestParam String codigo,@RequestParam String nombre) {
		ModelAndView mv = new ModelAndView();
		if (StringUtils.isBlank(codigo)) {
			mv.setViewName("categoria/nuevo");
			mv.addObject("error", "El Codigo no puede estar vacio");
			return mv;
		}
		if (StringUtils.isBlank(nombre)) {
			mv.setViewName("categoria/nuevo");
			mv.addObject("error", "El Nombre no puede estar vacio");
			return mv;
		}

		if (categoriaService.existsByCodigo(codigo)){
			mv.setViewName("area/nuevo");
			mv.addObject("error", "Ese Codigo ya Existe");
			return mv;
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
		
		Categoria categoria = new Categoria(codigo,nombre, usuario.getNombreUsuario(), "A");
		categoriaService.save(categoria);
		mv.setViewName("redirect:/categoria/lista");
		return mv;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/editar/{categoriaId}")
	public ModelAndView editar(@PathVariable("categoriaId") int id) {
		if (!categoriaService.existsById(id))
			return new ModelAndView("redirect:/categoria/lista");
		Categoria categoria = categoriaService.getOne(id).get();

		ModelAndView mv = new ModelAndView("/categoria/editar");
		mv.addObject("categoria", categoria);
		return mv;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/actualizar")
	public ModelAndView actualizar(@RequestParam int categoriaId, @RequestParam String codigo, @RequestParam String nombre) {
		if (!categoriaService.existsById(categoriaId))
			return new ModelAndView("redirect:/categoria/lista");
		ModelAndView mv = new ModelAndView();
		Categoria categoria = categoriaService.getOne(categoriaId).get();
		if (StringUtils.isBlank(codigo)) {
			mv.setViewName("categoria/editar");
			mv.addObject("categoria", categoria);
			mv.addObject("error", "El Codigo no puede estar vacío");
			return mv;
		}
		if (StringUtils.isBlank(nombre)) {
			mv.setViewName("categoria/editar");
			mv.addObject("categoria", categoria);
			mv.addObject("error", "El Nombre no puede estar vacío");
			return mv;
		}if (categoriaService.existsByCodigo(codigo) && categoriaService.getByCodigo(codigo).get().getCategoriaId() != categoriaId) {
			mv.setViewName("categoria/editar");
			mv.addObject("error", "Esa categoria ya existe");
			mv.addObject("categoria", categoria);
			return mv;
		}
		
		categoria.setCodigo(codigo);
		categoria.setNombre(nombre);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
		categoria.setUserUpdate(usuario.getNombreUsuario());
		categoriaService.save(categoria);
		return new ModelAndView("redirect:/categoria/lista");
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/borrar/{categoriaId}")
	public ModelAndView borrar(@PathVariable("categoriaId") int categoriaId) {
		if (categoriaService.existsById(categoriaId)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
			categoriaService.eliminar(categoriaId, new Date(), usuario.getNombreUsuario());
			return new ModelAndView("redirect:/categoria/lista");
		}
		return null;
	}


}
