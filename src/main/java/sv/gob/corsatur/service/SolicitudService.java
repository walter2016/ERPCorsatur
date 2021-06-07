package sv.gob.corsatur.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.gob.corsatur.model.Solicitud;
import sv.gob.corsatur.repository.ISolicitudRepository;

@Service
@Transactional
public class SolicitudService {
	
	@Autowired
	private ISolicitudRepository solicitudRepository;
	
	public Optional<Solicitud> getOne(int id) {
		return solicitudRepository.findById(id);
	}
	


	public void save(Solicitud solicitud) {
		solicitudRepository.save(solicitud);
	}
	
	public boolean existsById(int id) {
		return solicitudRepository.existsById(id);
	}


	
	public List<Solicitud> obtenerActivos(){
		return solicitudRepository.obtenerActivos();
	}
	
	public void eliminar(int solicitudId, Date updateDate, String userUpdate) {
		solicitudRepository.eliminarSolicutud(solicitudId, updateDate, userUpdate);
	}
	
	public Page<Solicitud> obtenerActivosPaginados(Pageable pageable){
		return solicitudRepository.findSolicitudes(pageable);
	}
	
	public Page<Solicitud> obtenerActivosUsuario(Pageable pageable, int empleadoId){
		return solicitudRepository.findSolicitudesPorUsuario(pageable,empleadoId);
	}
	
	public int obtenerNumeroSolicitud() {
		return solicitudRepository.buscarNumeroSolicitud();
	}

}
