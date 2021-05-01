package sv.gob.corsatur.controller.ActivoFijo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import sv.gob.corsatur.model.Area;
import sv.gob.corsatur.model.Asignacion;
import sv.gob.corsatur.model.Empleado;
import sv.gob.corsatur.model.Inventario;
import sv.gob.corsatur.model.Usuario;
import sv.gob.corsatur.service.AreaService;
import sv.gob.corsatur.service.AsignacionService;
import sv.gob.corsatur.service.EmpleadoService;
import sv.gob.corsatur.service.InventarioService;
import sv.gob.corsatur.service.UsuarioService;
import sv.gob.corsatur.utils.paginator.PageRender;

@Controller
@RequestMapping("/asignacion")
public class AsignacionController {

	@Autowired
	private AsignacionService asignacionService;

	@Autowired
	private InventarioService inventarioService;

	@Autowired
	private EmpleadoService empleadoService;

	@Autowired
	private AreaService areaService;
	
	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/lista")
	public String listaActivo(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 10);

		Page<Asignacion> asignacuines = asignacionService.obtenerActivos(pageRequest);

		PageRender<Asignacion> pageRender = new PageRender<Asignacion>("/asignacion/lista/", asignacuines);

		model.addAttribute("list", asignacuines);
		model.addAttribute("page", pageRender);

		return "/asignacion/lista";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("nuevo")
	public String nuevo(Model model) {
		List<Inventario> inventarios = inventarioService.obtenerNoAsignados();
		List<Empleado> empleados = empleadoService.obtenerActivos();
		List<Area> areas = areaService.obtenerActivos();
		model.addAttribute("inventarios", inventarios);
		model.addAttribute("empleados", empleados);
		model.addAttribute("areas", areas);
		return "asignacion/nuevo";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/guardar")
	public ModelAndView crear(@RequestParam Integer inventarioId, @RequestParam Integer areaId,
			@RequestParam Integer empleadoId, @RequestParam String fechaAsignacion, Model model) {
		ModelAndView mv = new ModelAndView();
		List<Inventario> inventarios = inventarioService.obtenerNoAsignados();
		List<Empleado> empleados = empleadoService.obtenerActivos();
		List<Area> areas = areaService.obtenerActivos();
		if (StringUtils.isBlank(fechaAsignacion)) {
			mv.setViewName("asignacion/nuevo");
			mv.addObject("error", "La fecha de Asignacion no puede estar vacia");
			model.addAttribute("inventarios", inventarios);
			model.addAttribute("empleados", empleados);
			model.addAttribute("areas", areas);
			return mv;
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
		
		SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
		Date fechaAsigna = null;

		try {
			fechaAsigna = formatoDelTexto.parse(fechaAsignacion);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Inventario inventario = inventarioService.getOne(inventarioId).get();
		Empleado empleado= empleadoService.getOne(empleadoId).get();
		Area area = areaService.getOne(areaId).get();
		Asignacion  asignacion = new Asignacion();
		
		asignacion.setAreaId(area);
		asignacion.setInventarioId(inventario);
		asignacion.setEmpleadoId(empleado);
		asignacion.setCreateDate(new Date());
		asignacion.setEstado("A");
		asignacion.setTipoAsignacion("ASIGNACION");
		asignacion.setUserCreate(usuario.getNombreUsuario());
		asignacion.setFechaAsignacion(fechaAsigna);

		asignacionService.save(asignacion);
		
		inventarioService.actualizarAsignacion(inventario.getInventarioId(),new Date(),usuario.getNombreUsuario());
		mv.setViewName("redirect:/asignacion/lista");
		return mv;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/borrar/{asignacionId}")
	public ModelAndView borrar(@PathVariable("asignacionId") int asignacionId) {
		if (asignacionService.existsById(asignacionId)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
			
			Asignacion asignacion = asignacionService.getOne(asignacionId).get();
			
			Asignacion asigNew = new Asignacion();
			
			asigNew.setAreaId(asignacion.getAreaId());
			asigNew.setInventarioId(asignacion.getInventarioId());
			asigNew.setEmpleadoId(asignacion.getEmpleadoId());
			asigNew.setCreateDate(new Date());
			asigNew.setEstado("A");
			asigNew.setTipoAsignacion("DESCARGO");
			asigNew.setUserCreate(usuario.getNombreUsuario());
			asigNew.setFechaAsignacion(new Date());
			asignacionService.save(asigNew);
			Inventario inventario = inventarioService.getOne(asignacion.getInventarioId().getInventarioId()).get();
			inventarioService.actualizarDescargo(inventario.getInventarioId(),new Date(),usuario.getNombreUsuario());
			return new ModelAndView("redirect:/asignacion/lista");
		}
		return null;
	}

}
