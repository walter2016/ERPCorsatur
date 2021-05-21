package sv.gob.corsatur.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.gob.corsatur.model.SolicitudCategoria;
import sv.gob.corsatur.repository.ISolicitudCategoriaRepository;

@Service
@Transactional
public class SolicitudCategoriaService {
	
	@Autowired
	private ISolicitudCategoriaRepository solicitudCategoriaRepository;
	
	public Optional<SolicitudCategoria> getOne(int id) {
		return solicitudCategoriaRepository.findById(id);
	}
	
	public Optional<SolicitudCategoria> getByCategoria(String categoria) {
		return solicitudCategoriaRepository.findByCategoria(categoria);
	}

	public void save(SolicitudCategoria solicitudCategoria) {
		solicitudCategoriaRepository.save(solicitudCategoria);
	}
	
	public boolean existsById(int id) {
		return solicitudCategoriaRepository.existsById(id);
	}

	public boolean existsByCategoria(String codigo) {
		return solicitudCategoriaRepository.existsByCategoria(codigo);
	}
	
	public List<SolicitudCategoria> obtenerActivos(){
		return solicitudCategoriaRepository.obtenerActivos();
	}
	
	public void eliminar(int solicitudCategoriaId, Date updateDate, String userUpdate) {
		solicitudCategoriaRepository.eliminarSolicutudCategoria(solicitudCategoriaId, updateDate, userUpdate);
	}
	
	public Page<SolicitudCategoria> obtenerActivosPaginados(Pageable pageable){
		return solicitudCategoriaRepository.findEstado(pageable);
	}

}
