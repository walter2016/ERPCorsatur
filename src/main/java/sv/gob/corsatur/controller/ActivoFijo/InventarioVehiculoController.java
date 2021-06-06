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

import sv.gob.corsatur.model.ClaseVehiculo;
import sv.gob.corsatur.model.EstadoVehiculo;
import sv.gob.corsatur.model.InventarioVehiculo;
import sv.gob.corsatur.model.MarcaVehiculo;
import sv.gob.corsatur.model.Usuario;
import sv.gob.corsatur.service.ClaseVehiculoService;
import sv.gob.corsatur.service.EstadoVehiculoService;
import sv.gob.corsatur.service.InventarioVehiculoService;
import sv.gob.corsatur.service.MarcaVehiculoService;
import sv.gob.corsatur.service.UsuarioService;
import sv.gob.corsatur.utils.paginator.PageRender;

@Controller
@RequestMapping("/inventariovehiculo")
public class InventarioVehiculoController {

	@Autowired
	InventarioVehiculoService inventarioVehiculoService;

	@Autowired
	ClaseVehiculoService claseVehiculoService;

	@Autowired
	MarcaVehiculoService marcaVehiculoService;

	@Autowired
	EstadoVehiculoService estadoVehiculoService;

	@Autowired
	UsuarioService usuarioService;

	@GetMapping("/lista")
	public String listaActivo(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 10);

		Page<InventarioVehiculo> inventarioVehiculo = inventarioVehiculoService.obtenerActivos(pageRequest);

		PageRender<InventarioVehiculo> pageRender = new PageRender<InventarioVehiculo>("/inventariovehiculo/lista/",
				inventarioVehiculo);

		model.addAttribute("list", inventarioVehiculo);
		model.addAttribute("page", pageRender);
		return "/inventariovehiculo/lista";
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('ACTI')")
	@GetMapping("/detalle/{id}")
	public ModelAndView detalle(@PathVariable("id") int id) {
		if (!inventarioVehiculoService.existsById(id))
			return new ModelAndView("redirect:/inventariovehiculo/lista");
		InventarioVehiculo inventarioVehiculo = inventarioVehiculoService.getOne(id).get();
		ModelAndView mv = new ModelAndView("/inventariovehiculo/detalle");
		mv.addObject("inventarioVehiculo", inventarioVehiculo);
		return mv;
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('ACTI')")
	@GetMapping("nuevo")
	public String nuevo(Model model) {
		List<MarcaVehiculo> marcaVehiculo = marcaVehiculoService.obtenerActivos();
		List<EstadoVehiculo> estadoVehiculo = estadoVehiculoService.obtenerActivos();
		List<ClaseVehiculo> claseVehiculo = claseVehiculoService.obtenerActivos();
		model.addAttribute("marcaVehiculo", marcaVehiculo);
		model.addAttribute("estadoVehiculo", estadoVehiculo);
		model.addAttribute("claseVehiculo", claseVehiculo);

		return "inventariovehiculo/nuevo";
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('ACTI')")
	@PostMapping("/guardar")
	public ModelAndView crear(@RequestParam String placa, @RequestParam String asignacion, @RequestParam String modelo,
			@RequestParam String anho, @RequestParam String numeroMotor, @RequestParam String numeroChasis,
			@RequestParam String tipoAsientos, @RequestParam String color, @RequestParam String descripcion,
			@RequestParam String numeroVin, @RequestParam String capacidad, @RequestParam String combustible,
			@RequestParam String traccion, @RequestParam String utilizadoPor, @RequestParam int marcaVehiculoId,
			@RequestParam int claseVehiculoId, @RequestParam int estadoVehiculoId, Model model) {
		ModelAndView mv = new ModelAndView();
		List<MarcaVehiculo> marcaVehiculo = marcaVehiculoService.obtenerActivos();
		List<EstadoVehiculo> estadoVehiculo = estadoVehiculoService.obtenerActivos();
		List<ClaseVehiculo> claseVehiculo = claseVehiculoService.obtenerActivos();

		if (StringUtils.isBlank(placa)) {
			mv.setViewName("inventariovehiculo/nuevo");
			mv.addObject("error", "La Placa no puede estar vacia");
			model.addAttribute("marcaVehiculo", marcaVehiculo);
			model.addAttribute("estadoVehiculo", estadoVehiculo);
			model.addAttribute("claseVehiculo", claseVehiculo);
			return mv;
		}
		if (StringUtils.isBlank(asignacion)) {
			mv.setViewName("inventariovehiculo/nuevo");
			mv.addObject("error", "La Asignacion no puede estar vacia");
			model.addAttribute("marcaVehiculo", marcaVehiculo);
			model.addAttribute("estadoVehiculo", estadoVehiculo);
			model.addAttribute("claseVehiculo", claseVehiculo);
			return mv;
		}
		if (StringUtils.isBlank(modelo)) {
			mv.setViewName("inventariovehiculo/nuevo");
			mv.addObject("error", "El modelo no puede estar vacio");
			model.addAttribute("marcaVehiculo", marcaVehiculo);
			model.addAttribute("estadoVehiculo", estadoVehiculo);
			model.addAttribute("claseVehiculo", claseVehiculo);
			return mv;
		}
		if (StringUtils.isBlank(anho)) {
			mv.setViewName("inventariovehiculo/nuevo");
			mv.addObject("error", "El Año no puede estar vacio");
			model.addAttribute("marcaVehiculo", marcaVehiculo);
			model.addAttribute("estadoVehiculo", estadoVehiculo);
			model.addAttribute("claseVehiculo", claseVehiculo);
			return mv;
		}
		if (StringUtils.isBlank(numeroMotor)) {
			mv.setViewName("inventariovehiculo/nuevo");
			mv.addObject("error", "El Numero de Motor no puede estar vacio");
			model.addAttribute("marcaVehiculo", marcaVehiculo);
			model.addAttribute("estadoVehiculo", estadoVehiculo);
			model.addAttribute("claseVehiculo", claseVehiculo);
			return mv;
		}
		if (StringUtils.isBlank(numeroChasis)) {
			mv.setViewName("inventariovehiculo/nuevo");
			mv.addObject("error", "El Numero de Chasis no puede estar vacio");
			model.addAttribute("marcaVehiculo", marcaVehiculo);
			model.addAttribute("estadoVehiculo", estadoVehiculo);
			model.addAttribute("claseVehiculo", claseVehiculo);
			return mv;
		}
		if (StringUtils.isBlank(numeroVin)) {
			mv.setViewName("inventariovehiculo/nuevo");
			mv.addObject("error", "El Numero de Vin no puede estar vacio en su defecto coloque S/N");
			model.addAttribute("marcaVehiculo", marcaVehiculo);
			model.addAttribute("estadoVehiculo", estadoVehiculo);
			model.addAttribute("claseVehiculo", claseVehiculo);
			return mv;
		}
		if (StringUtils.isBlank(tipoAsientos)) {
			mv.setViewName("inventariovehiculo/nuevo");
			mv.addObject("error", "El tipo de Asientos no puede estar vacio");
			model.addAttribute("marcaVehiculo", marcaVehiculo);
			model.addAttribute("estadoVehiculo", estadoVehiculo);
			model.addAttribute("claseVehiculo", claseVehiculo);
			return mv;
		}
		if (StringUtils.isBlank(descripcion)) {
			mv.setViewName("inventariovehiculo/nuevo");
			mv.addObject("error", "La descripcion no puede estar vacia");
			model.addAttribute("marcaVehiculo", marcaVehiculo);
			model.addAttribute("estadoVehiculo", estadoVehiculo);
			model.addAttribute("claseVehiculo", claseVehiculo);
			return mv;
		}
		if (StringUtils.isBlank(color)) {
			mv.setViewName("inventariovehiculo/nuevo");
			mv.addObject("error", "El color no puede estar vacio");
			model.addAttribute("marcaVehiculo", marcaVehiculo);
			model.addAttribute("estadoVehiculo", estadoVehiculo);
			model.addAttribute("claseVehiculo", claseVehiculo);
			return mv;
		}
		if (StringUtils.isBlank(capacidad)) {
			mv.setViewName("inventariovehiculo/nuevo");
			mv.addObject("error", "La capacidad no puede estar vacia");
			model.addAttribute("marcaVehiculo", marcaVehiculo);
			model.addAttribute("estadoVehiculo", estadoVehiculo);
			model.addAttribute("claseVehiculo", claseVehiculo);
			return mv;
		}
		if (StringUtils.isBlank(combustible)) {
			mv.setViewName("inventariovehiculo/nuevo");
			mv.addObject("error", "el Combustible no puede estar vacio");
			model.addAttribute("marcaVehiculo", marcaVehiculo);
			model.addAttribute("estadoVehiculo", estadoVehiculo);
			model.addAttribute("claseVehiculo", claseVehiculo);
			return mv;
		}
		if (inventarioVehiculoService.existsByPlaca(placa)) {
			mv.setViewName("inventariovehiculo/nuevo");
			mv.addObject("error", "Esa Placa ya Existe");
			model.addAttribute("marcaVehiculo", marcaVehiculo);
			model.addAttribute("estadoVehiculo", estadoVehiculo);
			model.addAttribute("claseVehiculo", claseVehiculo);
			return mv;
		}
		if (inventarioVehiculoService.existsByNumeroMotor(numeroMotor)) {
			mv.setViewName("inventariovehiculo/nuevo");
			mv.addObject("error", "Ese numero de motor ya Existe");
			model.addAttribute("marcaVehiculo", marcaVehiculo);
			model.addAttribute("estadoVehiculo", estadoVehiculo);
			model.addAttribute("claseVehiculo", claseVehiculo);
			return mv;
		}
		if (inventarioVehiculoService.existsByNumeroChasis(numeroChasis)) {
			mv.setViewName("inventariovehiculo/nuevo");
			mv.addObject("error", "Ese numero de chasis ya Existe");
			model.addAttribute("marcaVehiculo", marcaVehiculo);
			model.addAttribute("estadoVehiculo", estadoVehiculo);
			model.addAttribute("claseVehiculo", claseVehiculo);
			return mv;
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();

		MarcaVehiculo marVehiculo = marcaVehiculoService.getOne(marcaVehiculoId).get();
		EstadoVehiculo estVehiculo = estadoVehiculoService.getOne(estadoVehiculoId).get();
		ClaseVehiculo claVehiculo = claseVehiculoService.getOne(claseVehiculoId).get();

		InventarioVehiculo inventarioVehiculo = new InventarioVehiculo();

		inventarioVehiculo.setPlaca(placa);
		inventarioVehiculo.setAsignacion(asignacion);
		inventarioVehiculo.setMarcaVehiculoId(marVehiculo);
		inventarioVehiculo.setModelo(modelo);
		inventarioVehiculo.setAnho(anho);
		inventarioVehiculo.setClaseVehiculoId(claVehiculo);
		inventarioVehiculo.setNumeroMotor(numeroMotor);
		inventarioVehiculo.setNumeroChasis(numeroChasis);
		inventarioVehiculo.setNumeroVin(numeroVin);
		inventarioVehiculo.setTipoAsientos(tipoAsientos);
		inventarioVehiculo.setColor(color);
		inventarioVehiculo.setDescripcion(descripcion);
		inventarioVehiculo.setTraccion(traccion);
		inventarioVehiculo.setCapacidad(capacidad);
		inventarioVehiculo.setCombustible(combustible);
		inventarioVehiculo.setEstadoVehiculoId(estVehiculo);
		inventarioVehiculo.setUtilizadoPor(utilizadoPor);
		inventarioVehiculo.setUserCreate(usuario.getNombreUsuario());
		inventarioVehiculo.setCreateDate(new Date());
		inventarioVehiculo.setEstado("A");
		inventarioVehiculo.setAplica(true);
		inventarioVehiculo.setAsignado("N");
		
		inventarioVehiculoService.save(inventarioVehiculo);
		mv.setViewName("redirect:/inventariovehiculo/lista");

		return mv;
	}
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('ACTI')")
	@GetMapping("/borrar/{inventarioVehiculoId}")
	public ModelAndView borrar(@PathVariable("inventarioVehiculoId") int inventarioVehiculoId) {
		if (inventarioVehiculoService.existsById(inventarioVehiculoId)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
			inventarioVehiculoService.eliminar(inventarioVehiculoId, new Date(), usuario.getNombreUsuario());
			return new ModelAndView("redirect:/inventariovehiculo/lista");
		}
		return null;
	}
	
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('ACTI')")
	@GetMapping("/editar/{inventarioVehiculoId}")
	public ModelAndView editar(@PathVariable("inventarioVehiculoId") int id) {
		if (!marcaVehiculoService.existsById(id))
			return new ModelAndView("redirect:/inventariovehiculo/lista");
		InventarioVehiculo inventarioVehiculo = inventarioVehiculoService.getOne(id).get();
		List<MarcaVehiculo> marcaVehiculo = marcaVehiculoService.obtenerActivos();
		List<EstadoVehiculo> estadoVehiculo = estadoVehiculoService.obtenerActivos();
		List<ClaseVehiculo> claseVehiculo = claseVehiculoService.obtenerActivos();
		ModelAndView mv = new ModelAndView("/inventariovehiculo/editar");
		mv.addObject("inventarioVehiculo", inventarioVehiculo);
		mv.addObject("marcaVehiculo",marcaVehiculo);
		mv.addObject("estadoVehiculo",estadoVehiculo);
		mv.addObject("claseVehiculo",claseVehiculo);
		return mv;
	}
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('ACTI')")
	@PostMapping("/actualizar")
	public ModelAndView actualizar(@RequestParam int inventarioVehiculoId,@RequestParam String placa, @RequestParam String asignacion, @RequestParam String modelo,
			@RequestParam String anho, @RequestParam String numeroMotor, @RequestParam String numeroChasis,
			@RequestParam String tipoAsientos, @RequestParam String color, @RequestParam String descripcion,
			@RequestParam String numeroVin, @RequestParam String capacidad, @RequestParam String combustible,
			@RequestParam String traccion, @RequestParam String utilizadoPor, @RequestParam int marcaVehiculoId,
			@RequestParam int claseVehiculoId, @RequestParam int estadoVehiculoId, Model model) {
		if (!inventarioVehiculoService.existsById(inventarioVehiculoId))
			return new ModelAndView("redirect:/inventariovehiculo/lista");
		ModelAndView mv = new ModelAndView();
		InventarioVehiculo inventarioVehiculo = inventarioVehiculoService.getOne(inventarioVehiculoId).get();

		List<MarcaVehiculo> marcaVehiculo = marcaVehiculoService.obtenerActivos();
		List<EstadoVehiculo> estadoVehiculo = estadoVehiculoService.obtenerActivos();
		List<ClaseVehiculo> claseVehiculo = claseVehiculoService.obtenerActivos();

		if (StringUtils.isBlank(placa)) {
			mv.setViewName("inventariovehiculo/editar");
			mv.addObject("error", "La Placa no puede estar vacia");
			model.addAttribute("marcaVehiculo", marcaVehiculo);
			model.addAttribute("estadoVehiculo", estadoVehiculo);
			model.addAttribute("claseVehiculo", claseVehiculo);
			model.addAttribute("inventarioVehiculo", inventarioVehiculo);
			return mv;
		}
		if (StringUtils.isBlank(asignacion)) {
			mv.setViewName("inventariovehiculo/editar");
			mv.addObject("error", "La Asignacion no puede estar vacia");
			model.addAttribute("marcaVehiculo", marcaVehiculo);
			model.addAttribute("estadoVehiculo", estadoVehiculo);
			model.addAttribute("claseVehiculo", claseVehiculo);
			model.addAttribute("inventarioVehiculo", inventarioVehiculo);
			return mv;
		}
		if (StringUtils.isBlank(modelo)) {
			mv.setViewName("inventariovehiculo/editar");
			mv.addObject("error", "El modelo no puede estar vacio");
			model.addAttribute("marcaVehiculo", marcaVehiculo);
			model.addAttribute("estadoVehiculo", estadoVehiculo);
			model.addAttribute("claseVehiculo", claseVehiculo);
			model.addAttribute("inventarioVehiculo", inventarioVehiculo);
			return mv;
		}
		if (StringUtils.isBlank(anho)) {
			mv.setViewName("inventariovehiculo/editar");
			mv.addObject("error", "El Año no puede estar vacio");
			model.addAttribute("marcaVehiculo", marcaVehiculo);
			model.addAttribute("estadoVehiculo", estadoVehiculo);
			model.addAttribute("claseVehiculo", claseVehiculo);
			model.addAttribute("inventarioVehiculo", inventarioVehiculo);
			return mv;
		}
		if (StringUtils.isBlank(numeroMotor)) {
			mv.setViewName("inventariovehiculo/editar");
			mv.addObject("error", "El Numero de Motor no puede estar vacio");
			model.addAttribute("marcaVehiculo", marcaVehiculo);
			model.addAttribute("estadoVehiculo", estadoVehiculo);
			model.addAttribute("claseVehiculo", claseVehiculo);
			model.addAttribute("inventarioVehiculo", inventarioVehiculo);
			return mv;
		}
		if (StringUtils.isBlank(numeroChasis)) {
			mv.setViewName("inventariovehiculo/editar");
			mv.addObject("error", "El Numero de Chasis no puede estar vacio");
			model.addAttribute("marcaVehiculo", marcaVehiculo);
			model.addAttribute("estadoVehiculo", estadoVehiculo);
			model.addAttribute("claseVehiculo", claseVehiculo);
			model.addAttribute("inventarioVehiculo", inventarioVehiculo);
			return mv;
		}
		if (StringUtils.isBlank(numeroVin)) {
			mv.setViewName("inventariovehiculo/editar");
			mv.addObject("error", "El Numero de Vin no puede estar vacio en su defecto coloque S/N");
			model.addAttribute("marcaVehiculo", marcaVehiculo);
			model.addAttribute("estadoVehiculo", estadoVehiculo);
			model.addAttribute("claseVehiculo", claseVehiculo);
			model.addAttribute("inventarioVehiculo", inventarioVehiculo);
			return mv;
		}
		if (StringUtils.isBlank(tipoAsientos)) {
			mv.setViewName("inventariovehiculo/editar");
			mv.addObject("error", "El tipo de Asientos no puede estar vacio");
			model.addAttribute("marcaVehiculo", marcaVehiculo);
			model.addAttribute("estadoVehiculo", estadoVehiculo);
			model.addAttribute("claseVehiculo", claseVehiculo);
			model.addAttribute("inventarioVehiculo", inventarioVehiculo);
			return mv;
		}
		if (StringUtils.isBlank(descripcion)) {
			mv.setViewName("inventariovehiculo/editar");
			mv.addObject("error", "La descripcion no puede estar vacia");
			model.addAttribute("marcaVehiculo", marcaVehiculo);
			model.addAttribute("estadoVehiculo", estadoVehiculo);
			model.addAttribute("claseVehiculo", claseVehiculo);
			model.addAttribute("inventarioVehiculo", inventarioVehiculo);
			return mv;
		}
		if (StringUtils.isBlank(color)) {
			mv.setViewName("inventariovehiculo/editar");
			mv.addObject("error", "El color no puede estar vacio");
			model.addAttribute("marcaVehiculo", marcaVehiculo);
			model.addAttribute("estadoVehiculo", estadoVehiculo);
			model.addAttribute("claseVehiculo", claseVehiculo);
			model.addAttribute("inventarioVehiculo", inventarioVehiculo);
			return mv;
		}
		if (StringUtils.isBlank(capacidad)) {
			mv.setViewName("inventariovehiculo/editar");
			mv.addObject("error", "La capacidad no puede estar vacia");
			model.addAttribute("marcaVehiculo", marcaVehiculo);
			model.addAttribute("estadoVehiculo", estadoVehiculo);
			model.addAttribute("claseVehiculo", claseVehiculo);
			model.addAttribute("inventarioVehiculo", inventarioVehiculo);
			return mv;
		}
		if (StringUtils.isBlank(combustible)) {
			mv.setViewName("inventariovehiculo/editar");
			mv.addObject("error", "el Combustible no puede estar vacio");
			model.addAttribute("marcaVehiculo", marcaVehiculo);
			model.addAttribute("estadoVehiculo", estadoVehiculo);
			model.addAttribute("claseVehiculo", claseVehiculo);
			model.addAttribute("inventarioVehiculo", inventarioVehiculo);
			return mv;
		}
		if (inventarioVehiculoService.existsByPlaca(placa) && inventarioVehiculoService.getByPlaca(placa).get().getInventarioVehiculoId() != inventarioVehiculoId) {
			mv.setViewName("inventariovehiculo/editar");
			mv.addObject("error", "Esa Placa ya Existe");
			model.addAttribute("marcaVehiculo", marcaVehiculo);
			model.addAttribute("estadoVehiculo", estadoVehiculo);
			model.addAttribute("claseVehiculo", claseVehiculo);
			model.addAttribute("inventarioVehiculo", inventarioVehiculo);
			return mv;
		}

		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();

		MarcaVehiculo marVehiculo = marcaVehiculoService.getOne(marcaVehiculoId).get();
		EstadoVehiculo estVehiculo = estadoVehiculoService.getOne(estadoVehiculoId).get();
		ClaseVehiculo claVehiculo = claseVehiculoService.getOne(claseVehiculoId).get();

		inventarioVehiculo.setPlaca(placa);
		inventarioVehiculo.setAsignacion(asignacion);
		inventarioVehiculo.setMarcaVehiculoId(marVehiculo);
		inventarioVehiculo.setModelo(modelo);
		inventarioVehiculo.setAnho(anho);
		inventarioVehiculo.setClaseVehiculoId(claVehiculo);
		inventarioVehiculo.setNumeroMotor(numeroMotor);
		inventarioVehiculo.setNumeroChasis(numeroChasis);
		inventarioVehiculo.setNumeroVin(numeroVin);
		inventarioVehiculo.setTipoAsientos(tipoAsientos);
		inventarioVehiculo.setColor(color);
		inventarioVehiculo.setDescripcion(descripcion);
		inventarioVehiculo.setTraccion(traccion);
		inventarioVehiculo.setCapacidad(capacidad);
		inventarioVehiculo.setCombustible(combustible);
		inventarioVehiculo.setEstadoVehiculoId(estVehiculo);
		inventarioVehiculo.setUtilizadoPor(utilizadoPor);

		inventarioVehiculo.setUserUpdate(usuario.getNombreUsuario());
		inventarioVehiculoService.save(inventarioVehiculo);
		return new ModelAndView("redirect:/inventariovehiculo/lista");
	}
	

}
