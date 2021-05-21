package sv.gob.corsatur.controller.HelpDesk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sv.gob.corsatur.model.Categoria;
import sv.gob.corsatur.model.SolicitudCategoria;
import sv.gob.corsatur.service.SolicitudCategoriaService;
import sv.gob.corsatur.service.UsuarioService;
import sv.gob.corsatur.utils.paginator.PageRender;

@Controller
@RequestMapping("/solicitudcategoria")
public class SolicitudCategoriaController {
	
	
	@Autowired
	SolicitudCategoriaService solicitudCategoriaService;
	
	
	@Autowired
	UsuarioService usuarioService;
	
	@GetMapping("/lista")
	public String listaActivo(@RequestParam(name="page", defaultValue="0") int page, Model model) {
		
		Pageable pageRequest = PageRequest.of(page, 10);

		Page<SolicitudCategoria> SolciitudCtegorias = solicitudCategoriaService.obtenerActivosPaginados(pageRequest);
		
		PageRender<SolicitudCategoria> pageRender = new PageRender<SolicitudCategoria>("/solicitudcategoria/lista/", SolciitudCtegorias);
		
		model.addAttribute("list", SolciitudCtegorias);
		model.addAttribute("page", pageRender);

		
		return "/solicitudcategoria/lista";
	}

}
