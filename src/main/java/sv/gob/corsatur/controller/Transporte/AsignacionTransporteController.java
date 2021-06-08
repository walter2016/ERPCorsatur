package sv.gob.corsatur.controller.Transporte;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

import sv.gob.corsatur.model.AsignacionTransporte;
import sv.gob.corsatur.model.ClaseVehiculo;
import sv.gob.corsatur.model.Gerencia;
import sv.gob.corsatur.model.InventarioVehiculo;
import sv.gob.corsatur.model.MarcaVehiculo;
import sv.gob.corsatur.model.Motoristas;
import sv.gob.corsatur.model.Usuario;
import sv.gob.corsatur.service.AsignacionTransporteService;
import sv.gob.corsatur.service.ClaseVehiculoService;
import sv.gob.corsatur.service.GerenciaService;
import sv.gob.corsatur.service.InventarioVehiculoService;
import sv.gob.corsatur.service.MotoristaService;
import sv.gob.corsatur.service.UsuarioService;
import sv.gob.corsatur.utils.paginator.PageRender;

@Controller
@RequestMapping("/solicitudvehiculo")
public class AsignacionTransporteController {

	@Autowired
	AsignacionTransporteService asignacionTransporteService;

	@Autowired
	GerenciaService gerenciaService;

	@Autowired
	ClaseVehiculoService claseVehiculoService;

	@Autowired
	InventarioVehiculoService inventarioVehiculoService;

	@Autowired
	MotoristaService motoristaService;

	@Autowired
	UsuarioService usuarioService;

	@GetMapping("/lista")
	public String listaActivo(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 10);

		Page<AsignacionTransporte> asignacionTransporte = asignacionTransporteService
				.obtenerAsignacionTransporte(pageRequest);

		PageRender<AsignacionTransporte> pageRender = new PageRender<AsignacionTransporte>("/solicitudvehiculo/lista/",
				asignacionTransporte);

		model.addAttribute("list", asignacionTransporte);
		model.addAttribute("page", pageRender);
		return "/solicitudvehiculo/lista";
	}

	//@PreAuthorize("hasAnyRole('ADMIN','USER')")
	@PreAuthorize("hasRole('ADMIN') or hasRole('TRAN')")
	@GetMapping("nuevo")
	public String nuevo(Model model) {
		List<Gerencia> gerencias = gerenciaService.obtenerActivos();
		List<ClaseVehiculo> claseVehiculos = claseVehiculoService.obtenerActivos();
		model.addAttribute("gerencias", gerencias);
		model.addAttribute("claseVehiculos", claseVehiculos);
		return "solicitudvehiculo/nuevo";
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('TRAN')")
	@PostMapping("/guardar")
	public ModelAndView crear(@RequestParam Integer gerenciaId, @RequestParam Integer claseVehiculoId,
			@RequestParam String fechaUtilizacion, @RequestParam String encargadaMision,
			@RequestParam String personaViajaran, @RequestParam String destino, @RequestParam String objetivoMision,
			@RequestParam String horaSalida, @RequestParam String horaRegreso, Model model) {
		ModelAndView mv = new ModelAndView();
		List<Gerencia> gerencias = gerenciaService.obtenerActivos();
		List<ClaseVehiculo> claseVehiculos = claseVehiculoService.obtenerActivos();
		if (StringUtils.isBlank(fechaUtilizacion)) {
			mv.setViewName("solicitudvehiculo/nuevo");
			mv.addObject("error", "La fecha de Solicitud no puede estar vacia");
			model.addAttribute("gerencias", gerencias);
			model.addAttribute("claseVehiculos", claseVehiculos);
			return mv;
		}
		if (StringUtils.isBlank(encargadaMision)) {
			mv.setViewName("solicitudvehiculo/nuevo");
			mv.addObject("error", "Los encargado de Mision no puede estar vacia");
			model.addAttribute("gerencias", gerencias);
			model.addAttribute("claseVehiculos", claseVehiculos);
			return mv;
		}
		if (StringUtils.isBlank(personaViajaran)) {
			mv.setViewName("solicitudvehiculo/nuevo");
			mv.addObject("error", "Las personasque viajaran no puede estarvacio");
			model.addAttribute("gerencias", gerencias);
			model.addAttribute("claseVehiculos", claseVehiculos);
			return mv;
		}
		if (StringUtils.isBlank(destino)) {
			mv.setViewName("solicitudvehiculo/nuevo");
			mv.addObject("error", "El destiono no puede estar vacio");
			model.addAttribute("gerencias", gerencias);
			model.addAttribute("claseVehiculos", claseVehiculos);
			return mv;
		}
		if (StringUtils.isBlank(objetivoMision)) {
			mv.setViewName("solicitudvehiculo/nuevo");
			mv.addObject("error", "El Objetivo de Mision no puede estar vacio");
			model.addAttribute("gerencias", gerencias);
			model.addAttribute("claseVehiculos", claseVehiculos);
			return mv;
		}
		if (StringUtils.isBlank(horaSalida)) {
			mv.setViewName("solicitudvehiculo/nuevo");
			mv.addObject("error", "La hora de Salida no puede estar vacio");
			model.addAttribute("gerencias", gerencias);
			model.addAttribute("claseVehiculos", claseVehiculos);
			return mv;
		}
		if (StringUtils.isBlank(horaRegreso)) {
			mv.setViewName("solicitudvehiculo/nuevo");
			mv.addObject("error", "La hora de regreso no puede estar vacio");
			model.addAttribute("gerencias", gerencias);
			model.addAttribute("claseVehiculos", claseVehiculos);
			return mv;
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();

		Gerencia gerencia = gerenciaService.getOne(gerenciaId).get();
		ClaseVehiculo claseVehiculo = claseVehiculoService.getOne(claseVehiculoId).get();

		AsignacionTransporte asignacionTransporte = new AsignacionTransporte();

		asignacionTransporte.setClaseVehiculoId(claseVehiculo);
		asignacionTransporte.setGerenciaId(gerencia);
		asignacionTransporte.setFechaUtilizacion(fechaUtilizacion);
		asignacionTransporte.setEncargadaMision(encargadaMision);
		asignacionTransporte.setPersonaViajaran(personaViajaran);
		asignacionTransporte.setDestino(destino);
		asignacionTransporte.setObjetivoMision(objetivoMision);
		asignacionTransporte.setHoraSalida(horaSalida);
		asignacionTransporte.setHoraRegreso(horaRegreso);

		asignacionTransporte.setCreateDate(new Date());
		asignacionTransporte.setEstado("A");
		asignacionTransporte.setEstadoSolicitud("SOLICITADA");
		asignacionTransporte.setUserCreate(usuario.getNombreUsuario());

		asignacionTransporteService.save(asignacionTransporte);

		mv.setViewName("redirect:/solicitudvehiculo/lista");
		return mv;
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('TRAN')")
	@GetMapping("/editar/{asignacionTransporteId}")
	public ModelAndView editar(@PathVariable("asignacionTransporteId") int id) {
		if (!asignacionTransporteService.existsById(id))
			return new ModelAndView("redirect:/clasevehiculo/lista");
		AsignacionTransporte asignacionTransporte = asignacionTransporteService.getOne(id).get();
		int claseVehiculoId = asignacionTransporte.getClaseVehiculoId().claseVehiculoId;

		List<Motoristas> motoristas = motoristaService.obtenerSinAsignacion();
		List<InventarioVehiculo> inventarioVehiculo = inventarioVehiculoService
				.obtenerAvehiculoParaAsignar(claseVehiculoId);

		ModelAndView mv = new ModelAndView("/solicitudvehiculo/editar");
		mv.addObject("motoristas", motoristas);
		mv.addObject("inventarioVehiculo", inventarioVehiculo);
		mv.addObject("asignacionTransporte", asignacionTransporte);
		return mv;
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('TRAN')")
	@PostMapping("/actualizar")
	public ModelAndView actualizar(@RequestParam int asignacionTransporteId, @RequestParam int motoristaId,
			@RequestParam int inventarioVehiculoId, @RequestParam String observacion) {
		if (!asignacionTransporteService.existsById(asignacionTransporteId))
			return new ModelAndView("redirect:/solicitudvehiculo/lista");
		ModelAndView mv = new ModelAndView();
		AsignacionTransporte asignacionTransporte = asignacionTransporteService.getOne(asignacionTransporteId).get();
		
		Motoristas motorista = new Motoristas();
		
		if (motoristaId!=0) {

			motorista = motoristaService.getOne(motoristaId).get();
		}
	
		InventarioVehiculo inventarioVehiculo = inventarioVehiculoService.getOne(inventarioVehiculoId).get();
		
		asignacionTransporte.setInventarioVehiculoId(inventarioVehiculo);
		if (motoristaId!=0) {

			asignacionTransporte.setMotoristaId(motorista);
		}
	
		
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
		asignacionTransporte.setUserUpdate(usuario.getNombreUsuario());
		asignacionTransporte.setEstadoSolicitud("ACEPTADA");
		asignacionTransporte.setObservacion(observacion);
		asignacionTransporteService.save(asignacionTransporte);
		
		if (motoristaId!=0) {
			motorista.setDisponible("N");
			motoristaService.save(motorista);
		}
	
		
		inventarioVehiculo.setAsignado("S");
		inventarioVehiculoService.save(inventarioVehiculo);
		return new ModelAndView("redirect:/solicitudvehiculo/lista");
	}
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('TRAN')")
	@GetMapping("/borrar/{asignacionTransporteId}")
	public ModelAndView borrar(@PathVariable("asignacionTransporteId") int asignacionTransporteId) {
		if (asignacionTransporteService.existsById(asignacionTransporteId)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
			asignacionTransporteService.denegar(asignacionTransporteId, new Date(), usuario.getNombreUsuario());
			return new ModelAndView("redirect:/solicitudvehiculo/lista");
		}
		return null;
	}
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('TRAN')")
	@GetMapping("/finalizar/{asignacionTransporteId}")
	public ModelAndView finalizar(@PathVariable("asignacionTransporteId") int asignacionTransporteId) {
		if (asignacionTransporteService.existsById(asignacionTransporteId)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
			asignacionTransporteService.finalizar(asignacionTransporteId, new Date(), usuario.getNombreUsuario());
			System.out.println("Aqui estoy");
			AsignacionTransporte asignacionTransporte = asignacionTransporteService.getOne(asignacionTransporteId).get();
			
			try {
				int motorista =asignacionTransporte.getMotoristaId().getMotoristaId();		
				if(Objects.nonNull(motorista)) {
					motoristaService.finalizarAsignacion(asignacionTransporte.getMotoristaId().getMotoristaId(), new Date(), usuario.getNombreUsuario());
				}
			}catch(NullPointerException e) {
				e.toString();
			}
			catch(Exception e) {
				e.toString();
			}
			inventarioVehiculoService.dejarDisponible(asignacionTransporte.getInventarioVehiculoId().getInventarioVehiculoId(), new Date(), usuario.getNombreUsuario());
			return new ModelAndView("redirect:/solicitudvehiculo/lista");
		}
		return null;
	}


}
