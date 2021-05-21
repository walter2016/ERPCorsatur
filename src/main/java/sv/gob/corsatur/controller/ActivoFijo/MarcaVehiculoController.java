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

import sv.gob.corsatur.model.MarcaVehiculo;
import sv.gob.corsatur.model.Usuario;
import sv.gob.corsatur.service.MarcaVehiculoService;
import sv.gob.corsatur.service.UsuarioService;
import sv.gob.corsatur.utils.paginator.PageRender;

@Controller
@RequestMapping("/marcavehiculo")
public class MarcaVehiculoController {
	
	@Autowired
	MarcaVehiculoService marcaVehiculoService;
	
	@Autowired
	UsuarioService usuarioService;
	
	@GetMapping("/lista")
	public String listaActivo(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 10);

		Page<MarcaVehiculo> marcaVehiculo = marcaVehiculoService.obtenerActivos(pageRequest);

		PageRender<MarcaVehiculo> pageRender = new PageRender<MarcaVehiculo>("/clasevehiculo/lista/", marcaVehiculo);

		model.addAttribute("list", marcaVehiculo);
		model.addAttribute("page", pageRender);
		return "/marcavehiculo/lista";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("nuevo")
	public String nuevo() {
		return "marcavehiculo/nuevo";
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/guardar")
	public ModelAndView crear(@RequestParam String marca) {
		ModelAndView mv = new ModelAndView();
		if (StringUtils.isBlank(marca)) {
			mv.setViewName("marcavehiculo/nuevo");
			mv.addObject("error", "La Marca no puede estar vacia");
			return mv;
		}

		if (marcaVehiculoService.existsByMarca(marca)) {
			mv.setViewName("marcavehiculo/nuevo");
			mv.addObject("error", "Ese Nombre ya Existe");
			return mv;
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
		MarcaVehiculo marcaVehiculo=new MarcaVehiculo();

		marcaVehiculo.setCreateDate(new Date());
		marcaVehiculo.setEstado("A");
		marcaVehiculo.setMarca(marca);
		marcaVehiculo.setUserCreate(usuario.getNombreUsuario());
		marcaVehiculoService.save(marcaVehiculo);
		mv.setViewName("redirect:/marcavehiculo/lista");
		return mv;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/editar/{marcaVehiculoId}")
	public ModelAndView editar(@PathVariable("marcaVehiculoId") int id) {
		if (!marcaVehiculoService.existsById(id))
			return new ModelAndView("redirect:/marcavehiculo/lista");
		MarcaVehiculo marcaVehiculo = marcaVehiculoService.getOne(id).get();

		ModelAndView mv = new ModelAndView("/marcavehiculo/editar");
		mv.addObject("marcaVehiculo", marcaVehiculo);
		return mv;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/actualizar")
	public ModelAndView actualizar(@RequestParam int marcaVehiculoId, @RequestParam String marca) {
		if (!marcaVehiculoService.existsById(marcaVehiculoId))
			return new ModelAndView("redirect:/marcavehiculo/lista");
		ModelAndView mv = new ModelAndView();
		MarcaVehiculo marcaVehiculo = marcaVehiculoService.getOne(marcaVehiculoId).get();

		if (StringUtils.isBlank(marca)) {
			mv.setViewName("marcavehiculo/editar");
			mv.addObject("marcaVehiculo", marcaVehiculo);
			mv.addObject("error", "La Marca no puede estar vacío");
			return mv;
		}
		if (marcaVehiculoService.existsByMarca(marca) && marcaVehiculoService.getByMarca(marca).get().getMarcaVehiculoId() != marcaVehiculoId) {
			mv.setViewName("marcavehiculo/editar");
			mv.addObject("error", "ese nombre ya existe");
			mv.addObject("marcaVehiculo", marcaVehiculo);
			return mv;
		}

		marcaVehiculo.setMarca(marca);
	
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
		marcaVehiculo.setUserUpdate(usuario.getNombreUsuario());
		marcaVehiculoService.save(marcaVehiculo);
		return new ModelAndView("redirect:/marcavehiculo/lista");
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/borrar/{marcaVehiculoId}")
	public ModelAndView borrar(@PathVariable("marcaVehiculoId") int marcaVehiculoId) {
		if (marcaVehiculoService.existsById(marcaVehiculoId)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
			marcaVehiculoService.eliminar(marcaVehiculoId, new Date(), usuario.getNombreUsuario());
			return new ModelAndView("redirect:/marcavehiculo/lista");
		}
		return null;
	}
	

}
