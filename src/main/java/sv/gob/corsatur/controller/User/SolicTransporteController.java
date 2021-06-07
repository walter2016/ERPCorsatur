package sv.gob.corsatur.controller.User;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import sv.gob.corsatur.model.AsignacionTransporte;
import sv.gob.corsatur.model.ClaseVehiculo;
import sv.gob.corsatur.model.Gerencia;
import sv.gob.corsatur.model.Usuario;
import sv.gob.corsatur.service.AsignacionTransporteService;
import sv.gob.corsatur.service.ClaseVehiculoService;
import sv.gob.corsatur.service.GerenciaService;
import sv.gob.corsatur.service.InventarioVehiculoService;
import sv.gob.corsatur.service.MotoristaService;
import sv.gob.corsatur.service.UsuarioService;
import sv.gob.corsatur.utils.paginator.PageRender;

@Controller
@RequestMapping("/user")
public class SolicTransporteController {
	
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

	@GetMapping("/listasolicvehiculo")
	public String listaActivo(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 10);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();

		Page<AsignacionTransporte> asignacionTransporte = asignacionTransporteService
				.obtenerAsignacionTransporteUsuario(pageRequest,usuario.getNombreUsuario());

		PageRender<AsignacionTransporte> pageRender = new PageRender<AsignacionTransporte>("/user/listasolicvehiculo/",
				asignacionTransporte);

		model.addAttribute("list", asignacionTransporte);
		model.addAttribute("page", pageRender);
		return "/user/listasolicvehiculo";
	}
	

		@GetMapping("nuevoveh")
		public String nuevo(Model model) {
			List<Gerencia> gerencias = gerenciaService.obtenerActivos();
			List<ClaseVehiculo> claseVehiculos = claseVehiculoService.obtenerActivos();
			model.addAttribute("gerencias", gerencias);
			model.addAttribute("claseVehiculos", claseVehiculos);
			return "user/nuevosolicveh";
		}
		
		

		@PostMapping("/guardarsoli")
		public ModelAndView crear(@RequestParam Integer gerenciaId, @RequestParam Integer claseVehiculoId,
				@RequestParam String fechaUtilizacion, @RequestParam String encargadaMision,
				@RequestParam String personaViajaran, @RequestParam String destino, @RequestParam String objetivoMision,
				@RequestParam String horaSalida, @RequestParam String horaRegreso, Model model) {
			ModelAndView mv = new ModelAndView();
			List<Gerencia> gerencias = gerenciaService.obtenerActivos();
			List<ClaseVehiculo> claseVehiculos = claseVehiculoService.obtenerActivos();
			if (StringUtils.isBlank(fechaUtilizacion)) {
				mv.setViewName("user/nuevosolicveh");
				mv.addObject("error", "La fecha de Solicitud no puede estar vacia");
				model.addAttribute("gerencias", gerencias);
				model.addAttribute("claseVehiculos", claseVehiculos);
				return mv;
			}
			if (StringUtils.isBlank(encargadaMision)) {
				mv.setViewName("user/nuevosolicveh");
				mv.addObject("error", "Los encargado de Mision no puede estar vacia");
				model.addAttribute("gerencias", gerencias);
				model.addAttribute("claseVehiculos", claseVehiculos);
				return mv;
			}
			if (StringUtils.isBlank(personaViajaran)) {
				mv.setViewName("user/nuevosolicveh");
				mv.addObject("error", "Las personasque viajaran no puede estarvacio");
				model.addAttribute("gerencias", gerencias);
				model.addAttribute("claseVehiculos", claseVehiculos);
				return mv;
			}
			if (StringUtils.isBlank(destino)) {
				mv.setViewName("user/nuevosolicveh");
				mv.addObject("error", "El destiono no puede estar vacio");
				model.addAttribute("gerencias", gerencias);
				model.addAttribute("claseVehiculos", claseVehiculos);
				return mv;
			}
			if (StringUtils.isBlank(objetivoMision)) {
				mv.setViewName("user/nuevosolicveh");
				mv.addObject("error", "El Objetivo de Mision no puede estar vacio");
				model.addAttribute("gerencias", gerencias);
				model.addAttribute("claseVehiculos", claseVehiculos);
				return mv;
			}
			if (StringUtils.isBlank(horaSalida)) {
				mv.setViewName("user/nuevosolicveh");
				mv.addObject("error", "La hora de Salida no puede estar vacio");
				model.addAttribute("gerencias", gerencias);
				model.addAttribute("claseVehiculos", claseVehiculos);
				return mv;
			}
			if (StringUtils.isBlank(horaRegreso)) {
				mv.setViewName("user/nuevosolicveh");
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

			mv.setViewName("redirect:/user/listasolicvehiculo");
			return mv;
		}


}
