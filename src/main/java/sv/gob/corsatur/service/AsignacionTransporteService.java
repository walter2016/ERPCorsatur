package sv.gob.corsatur.service;

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

	public Page<AsignacionTransporte> obtenerAsignacionTransporte(Pageable pageable){
		return asignacionVehiculoRepository.buscarSolicitudes(pageable);
	}
	
	
}