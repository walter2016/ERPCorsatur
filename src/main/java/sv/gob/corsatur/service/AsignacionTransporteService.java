package sv.gob.corsatur.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.gob.corsatur.model.AsignacionTransporte;
import sv.gob.corsatur.model.Motoristas;
import sv.gob.corsatur.repository.IAsignacionVehiculorepository;

@Service
@Transactional
public class AsignacionTransporteService {
	
	@Autowired
	private IAsignacionVehiculorepository asignacionVehiculoRepository;
	
	public Optional<AsignacionTransporte> getOne(int id) {
		return asignacionVehiculoRepository.findById(id);
	}
	
	public void save(AsignacionTransporte asignacionTransporte) {
		asignacionVehiculoRepository.save(asignacionTransporte);
	}
	
	public boolean existsById(int id) {
		return asignacionVehiculoRepository.existsById(id);
	}
	
	public void denegar(int asignacionTransporteId, Date updateDate, String userUpdate) {
		asignacionVehiculoRepository.denegar(asignacionTransporteId, updateDate, userUpdate);
	}
	public void finalizar(int asignacionTransporteId, Date updateDate, String userUpdate) {
		asignacionVehiculoRepository.finalizar(asignacionTransporteId, updateDate, userUpdate);
	}

	public Page<AsignacionTransporte> obtenerAsignacionTransporte(Pageable pageable){
		return asignacionVehiculoRepository.buscarSolicitudes(pageable);
	}
	
	public Page<AsignacionTransporte> obtenerAsignacionTransporteUsuario(Pageable pageable, String userCreate){
		return asignacionVehiculoRepository.buscarSolicitudesUsuario(pageable,userCreate);
	}
	
	
}
