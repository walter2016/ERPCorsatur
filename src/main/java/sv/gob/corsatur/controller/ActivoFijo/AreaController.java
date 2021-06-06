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

import sv.gob.corsatur.model.Area;
import sv.gob.corsatur.model.Usuario;
import sv.gob.corsatur.service.AreaService;
import sv.gob.corsatur.service.UsuarioService;
import sv.gob.corsatur.utils.paginator.PageRender;

@Controller
@RequestMapping("/area")
public class AreaController {

	@Autowired
	AreaService areaService;

	@Autowired
	UsuarioService usuarioService;

	@GetMapping("/lista")
	public String listaActivo(@RequestParam(name="page", defaultValue="0") int page, Model model) {
		
		Pageable pageRequest = PageRequest.of(page, 10);

		Page<Area> areas = areaService.obtenerActivos(pageRequest);
		
		PageRender<Area> pageRender = new PageRender<Area>("/area/lista/", areas);
		
		model.addAttribute("list", areas);
		model.addAttribute("page", pageRender);

		
		return "/area/lista";
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('ACTI')")
	@GetMapping("nuevo")
	public String nuevo() {
		return "area/nuevo";
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('ACTI')")
	@PostMapping("/guardar")
	public ModelAndView crear(@RequestParam String nombre) {
		ModelAndView mv = new ModelAndView();
		if (StringUtils.isBlank(nombre)) {
			mv.setViewName("area/nuevo");
			mv.addObject("error", "El Area no puede estar vacia");
			return mv;
		}

		if (areaService.existsByNombre(nombre)) {
			mv.setViewName("area/nuevo");
			mv.addObject("error", "Ese Nombre ya Existe");
			return mv;
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
		Area area = new Area(nombre, usuario.getNombreUsuario(), "A");
		areaService.save(area);
		mv.setViewName("redirect:/area/lista");
		return mv;
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('ACTI')")
	@GetMapping("/editar/{areaId}")
	public ModelAndView editar(@PathVariable("areaId") int id) {
		if (!areaService.existsById(id))
			return new ModelAndView("redirect:/area/lista");
		Area area = areaService.getOne(id).get();

		ModelAndView mv = new ModelAndView("/area/editar");
		mv.addObject("area", area);
		return mv;
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('ACTI')")
	@PostMapping("/actualizar")
	public ModelAndView actualizar(@RequestParam int areaId, @RequestParam String nombre) {
		if (!areaService.existsById(areaId))
			return new ModelAndView("redirect:/area/lista");
		ModelAndView mv = new ModelAndView();
		Area area = areaService.getOne(areaId).get();
		if (StringUtils.isBlank(nombre)) {
			mv.setViewName("area/editar");
			mv.addObject("area", area);
			mv.addObject("error", "el nombre no puede estar vacío");
			return mv;
		}
		if (areaService.existsByNombre(nombre) && areaService.getByNombre(nombre).get().getAreaId() != areaId) {
			mv.setViewName("area/editar");
			mv.addObject("error", "ese nombre ya existe");
			mv.addObject("area", area);
			return mv;
		}

		area.setNombre(nombre);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
		area.setUserUpdate(usuario.getNombreUsuario());
		areaService.save(area);
		return new ModelAndView("redirect:/area/lista");
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('ACTI')")
	@GetMapping("/borrar/{areaId}")
	public ModelAndView borrar(@PathVariable("areaId") int areaId) {
		if (areaService.existsById(areaId)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
			areaService.eliminar(areaId, new Date(), usuario.getNombreUsuario());
			return new ModelAndView("redirect:/area/lista");
		}
		return null;
	}

}
