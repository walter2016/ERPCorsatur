package sv.gob.corsatur.controller.HelpDesk;

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

import sv.gob.corsatur.model.Configuracion;
import sv.gob.corsatur.model.Empleado;
import sv.gob.corsatur.model.Solicitud;
import sv.gob.corsatur.model.SolicitudCategoria;
import sv.gob.corsatur.model.Usuario;
import sv.gob.corsatur.service.ConfiguracionService;
import sv.gob.corsatur.service.EmailService;
import sv.gob.corsatur.service.EmpleadoService;
import sv.gob.corsatur.service.SolicitudCategoriaService;
import sv.gob.corsatur.service.SolicitudService;
import sv.gob.corsatur.service.UsuarioService;
import sv.gob.corsatur.utils.paginator.PageRender;

@Controller
@RequestMapping("/solicitud")
public class SolicitudController {

	@Autowired
	SolicitudService solicitudService;

	@Autowired
	ConfiguracionService confiService;
	
	@Autowired
	UsuarioService usuarioService;

	@Autowired
	EmpleadoService empleadoService;

	@Autowired
	SolicitudCategoriaService soliCateService;

	@Autowired
	EmailService emailService;

	@GetMapping("/lista")
	public String listaActivo(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 10);

		Page<Solicitud> solicitud = solicitudService.obtenerActivosPaginados(pageRequest);

		PageRender<Solicitud> pageRender = new PageRender<Solicitud>("/solicitud/lista/", solicitud);

		model.addAttribute("list", solicitud);
		model.addAttribute("page", pageRender);
		return "/solicitud/lista";
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('HELP')")
	@GetMapping("nuevo")
	public String nuevo(Model model) {
		List<Empleado> empleado = empleadoService.obtenerActivos();
		List<SolicitudCategoria> soliCategorias = soliCateService.obtenerActivos();
		model.addAttribute("empelados", empleado);
		model.addAttribute("solicitudCategorias", soliCategorias);
		return "solicitud/nuevo";
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('HELP')")
	@PostMapping("/guardar")
	public ModelAndView crear(@RequestParam String descripcion, @RequestParam Integer solicCateId, Model model) {
		ModelAndView mv = new ModelAndView();

		List<SolicitudCategoria> soliCategorias = soliCateService.obtenerActivos();
		if (StringUtils.isBlank(descripcion)) {
			mv.setViewName("solicitud/nuevo");
			mv.addObject("error", "La descripcion no puede estar vacia");
			model.addAttribute("solicitudCategorias", soliCategorias);
			return mv;
		}
		if (!soliCateService.existsById(solicCateId)) {
			mv.setViewName("solicitud/nuevo");
			mv.addObject("error", "Ese categoria no Existe");
			model.addAttribute("solicitudCategorias", soliCategorias);
			return mv;
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();

		SolicitudCategoria solcicategoria = soliCateService.getOne(solicCateId).get();

		Empleado empleado = empleadoService.obtenerEmpeladoPorUsuario(usuario);

		int corr = 1;

		try {
			if (solicitudService.obtenerNumeroSolicitud() > 0) {
				corr = solicitudService.obtenerNumeroSolicitud() + 1;
			}
		} catch (Exception e) {

		}

		Solicitud solicitud = new Solicitud();
		solicitud.setCreateDate(new Date());
		solicitud.setDescripcion(descripcion);
		solicitud.setEstado("A");
		solicitud.setRespuestaSolicitud("");
		solicitud.setEstadoSolicitud("NUEVO");
		solicitud.setUserCreate(usuario.getNombreUsuario());
		solicitud.setSolicCateId(solcicategoria);
		solicitud.setEmpleadoId(empleado);
		solicitud.setNumeroSolicitud(corr);
		solicitudService.save(solicitud);

		// Envio de correo
		// cuerpo del correo

		String cuerpo = "Se creado un Nuevo Ticket de parte de: " + empleado.getPrimerNombre() + " "
				+ empleado.getPrimerApellido() + " con la siguiente Descripcion: " + descripcion;

		// subjet del correo

		String subjet = "Nuevo Ticket " + " Con Numero " + corr + " de la categoria " + solcicategoria.getCategoria();
	
		Configuracion configCorreo = confiService.getByCodigoClasificacion("CORREO").get();

		emailService.sendEmail(configCorreo.getValor(), subjet, cuerpo, empleado.getEmail());

		mv.setViewName("redirect:/solicitud/lista");
		return mv;

	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('HELP')")
	@GetMapping("/editar/{solicitudId}")
	public ModelAndView editar(@PathVariable("solicitudId") int id) {
		if (!solicitudService.existsById(id))
			return new ModelAndView("redirect:/solicitud/lista");

		Solicitud solicitud = solicitudService.getOne(id).get();

		ModelAndView mv = new ModelAndView("/solicitud/editar");
		mv.addObject("solicitud", solicitud);
		return mv;
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('HELP')")
	@PostMapping("/actualizar")
	public ModelAndView actualizar(@RequestParam int solicitudId, @RequestParam String respuestaSolicitud) {
		if (!solicitudService.existsById(solicitudId))
			return new ModelAndView("redirect:/solicitud/lista");
		ModelAndView mv = new ModelAndView();
		Solicitud solicitud = solicitudService.getOne(solicitudId).get();
		if (StringUtils.isBlank(respuestaSolicitud)) {
			mv.setViewName("solicitud/editar");
			mv.addObject("solicitud", solicitud);
			mv.addObject("error", "El Comentario es requerido");
			return mv;
		}

		solicitud.setRespuestaSolicitud(respuestaSolicitud);
		solicitud.setEstadoSolicitud("RESUELTO");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
		solicitud.setUserUpdate(usuario.getNombreUsuario());
		solicitudService.save(solicitud);

		String cuerpo = "Se Solvento el Ticket numero : " + solicitud.getNumeroSolicitud()
				+ " con el siguiente comentario  " + respuestaSolicitud;

		// subjet del correo

		String subjet = "Se solvento el Ticket Con Numero " + solicitud.getNumeroSolicitud();
		System.out.print(solicitud.getEmpleadoId().getEmail());
		emailService.sendEmail(solicitud.getEmpleadoId().getEmail(), subjet, cuerpo, "");

		return new ModelAndView("redirect:/solicitud/lista");
	}

}
