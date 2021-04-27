package sv.gob.corsatur.controller.ActivoFijo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

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

import sv.gob.corsatur.model.CodigoHacienda;
import sv.gob.corsatur.model.Inventario;
import sv.gob.corsatur.model.Tipo;
import sv.gob.corsatur.model.Usuario;
import sv.gob.corsatur.service.CodigoHaciendaService;
import sv.gob.corsatur.service.InventarioService;
import sv.gob.corsatur.service.TipoService;
import sv.gob.corsatur.service.UsuarioService;
import sv.gob.corsatur.utils.paginator.PageRender;

@Controller
@RequestMapping("/inventario")
public class InventarioController {

	@Autowired
	InventarioService inventarioService;

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	TipoService tipoService;

	@Autowired
	CodigoHaciendaService haciendaService;

	@GetMapping("/lista")
	public String listaActivo(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 10);

		Page<Inventario> inventario = inventarioService.obtenerActivos(pageRequest);

		PageRender<Inventario> pageRender = new PageRender<Inventario>("/inventario/lista/", inventario);

		model.addAttribute("list", inventario);
		model.addAttribute("page", pageRender);

		return "/inventario/lista";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("nuevo")
	public String nuevo(Model model) {
		List<Tipo> tipos = tipoService.obtenerActivos();
		List<CodigoHacienda> codigos = haciendaService.obtenerActivos();
		model.addAttribute("tipos", tipos);
		model.addAttribute("codigos", codigos);
		return "inventario/nuevo";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/guardar")
	public ModelAndView crear(@RequestParam Integer haciendaId, @RequestParam Integer tipoId,
			@RequestParam String marca, @RequestParam String modelo, @RequestParam String serie,
			@RequestParam float costo, @RequestParam String fechaAdquisicion, @RequestParam boolean depreciable,
			Model model) {
		ModelAndView mv = new ModelAndView();
		List<Tipo> tipos = tipoService.obtenerActivos();
		List<CodigoHacienda> codigos = haciendaService.obtenerActivos();
		if (StringUtils.isBlank(marca)) {
			mv.setViewName("inventario/nuevo");
			mv.addObject("error", "La Marca no puede estar vacia");
			model.addAttribute("tipos", tipos);
			model.addAttribute("codigos", codigos);
		}
		if (StringUtils.isBlank(modelo)) {
			mv.setViewName("inventario/nuevo");
			mv.addObject("error", "El Modelo no puede estar vacia");
			model.addAttribute("tipos", tipos);
			model.addAttribute("codigos", codigos);
		}
		if (StringUtils.isBlank(serie)) {
			mv.setViewName("inventario/nuevo");
			mv.addObject("error", "La Seria no puede estar vacia");
			model.addAttribute("tipos", tipos);
			model.addAttribute("codigos", codigos);
		}
		if (costo<0.0) {
			mv.setViewName("inventario/nuevo");
			mv.addObject("error", "El costo debe de ser mayor a 0.0");
			model.addAttribute("tipos", tipos);
			model.addAttribute("codigos", codigos);
		}

		/* Inicio Seccion de codigo que convierte la fecha del formulario a Date */
		SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
		Date fechaAd = null;
		Date fechaActual = new Date();
		try {
			fechaAd = formatoDelTexto.parse(fechaAdquisicion);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* Fin Seccion de codigo que convierte la fecha del formulario a Date */

		/* comienza la obtencionde datos para generar el codigo deinventario */
		/* Incicio XXXX */
		CodigoHacienda codigohacienda = haciendaService.getOne(haciendaId).get();
		String numeroHacienda = codigohacienda.getCodigo();

		Tipo tipo = tipoService.getOne(tipoId).get();
		String categoriaId = tipo.getCategoriaId().getCodigo();

		String tipoInv = tipo.getCodigo();

		int corr = 1;

		try {
			if (inventarioService.obtenerUltimo(tipoId, haciendaId) > 0) {
				corr = inventarioService.obtenerUltimo(tipoId, haciendaId) + 1;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();

		String correlativo = generarCorrelativo(corr);

		System.out.print(numeroHacienda + '-' + categoriaId + '-' + tipoInv + '-' + correlativo);

		String codigoIndividual = generarCodigoInventario(numeroHacienda, categoriaId, tipoInv, correlativo);

		Inventario inventario = new Inventario(tipo, codigohacienda, corr, codigoIndividual, marca, modelo, serie,
				fechaAd, costo, depreciable, 0, 0, 0, 0, 0, 0, new Date(),  usuario.getNombreUsuario(), "A");

		inventarioService.save(inventario);

		return new ModelAndView("redirect:/inventario/lista");
	}
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/borrar/{inventarioId}")
	public ModelAndView borrar(@PathVariable("inventarioId") int inventarioId) {
		if (inventarioService.existsById(inventarioId)) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			Usuario usuario = this.usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
			inventarioService.eliminar(inventarioId, new Date(), usuario.getNombreUsuario());
			return new ModelAndView("redirect:/inventario/lista");
		}
		return null;
	}


	public String generarCorrelativo(int corr) {

		String correlativo = "";

		if (corr < 10) {
			correlativo = "00" + corr;
		} else if (corr < 100) {
			correlativo = "0" + corr;
		} else {
			correlativo = correlativo + corr;
		}

		return correlativo;
	}

	public String generarCodigoInventario(String numeroHacienda, String categoria, String tipoInventario,
			String correlativo) {

		return numeroHacienda + "-" + categoria + "-" + tipoInventario + "-" + correlativo;
	}

}
