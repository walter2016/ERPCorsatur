package sv.gob.corsatur.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.gob.corsatur.model.EstadoVehiculo;
import sv.gob.corsatur.repository.IEstadoVehiculoRepository;

@Service
@Transactional
public class EstadoVehiculoService {
	
	@Autowired
	private IEstadoVehiculoRepository estadoVehiculoRepository;
	
	public Optional<EstadoVehiculo> getOne(int id) {
		return estadoVehiculoRepository.findById(id);
	}
	
	public void save(EstadoVehiculo estadovehiculo) {
		estadoVehiculoRepository.save(estadovehiculo);
	}
	
	public boolean existsByEstVehiculo(String estadovehiculo) {
		return estadoVehiculoRepository.existsByEstVehiculo(estadovehiculo);
	}

	public Optional<EstadoVehiculo> getByEstVehiculo(String estadovehiculo) {
		return estadoVehiculoRepository.findByEstVehiculo(estadovehiculo);
	}
	
	public boolean existsById(int id) {
		return estadoVehiculoRepository.existsById(id);
	}
	
	public List<EstadoVehiculo> obtenerActivos(){
		return estadoVehiculoRepository.obtenerActivos();
	}
	
	public void eliminar(int estadoVehiculoId, Date updateDate, String userUpdate) {
		estadoVehiculoRepository.eliminarEstadoVehiculo(estadoVehiculoId, updateDate, userUpdate);
	}
	
	public Page<EstadoVehiculo> obtenerActivos(Pageable pageable){
		return estadoVehiculoRepository.findEstadoVehiculo(pageable);
	}


}
