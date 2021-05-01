package sv.gob.corsatur.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.gob.corsatur.model.Asignacion;
import sv.gob.corsatur.repository.IAsignacionRepository;

@Service
@Transactional
public class AsignacionService {
	
	@Autowired
	private IAsignacionRepository asignacionRepository;
	
	public List<Asignacion> list() {
		return asignacionRepository.findAll();
	}

	public Optional<Asignacion> getOne(int id) {
		return asignacionRepository.findById(id);
	}

	

	public void save(Asignacion asignacion) {
		asignacionRepository.save(asignacion);
	}

	public void delete(int id) {
		asignacionRepository.deleteById(id);
	}

	public boolean existsById(int id) {
		return asignacionRepository.existsById(id);
	}

	
	public List<Asignacion> obtenerActivos(){
		return asignacionRepository.obtenerActivos();
	}
	
	public void eliminar(int asignacionId, Date updateDate, String userUpdate) {
		asignacionRepository.eliminarAsignacion(asignacionId, updateDate, userUpdate);
	}
	
	public Page<Asignacion> obtenerActivos(Pageable pageable){
		return asignacionRepository.obtenerAsignacionPaginados(pageable);
	}

}
