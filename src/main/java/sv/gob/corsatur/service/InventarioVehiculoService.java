package sv.gob.corsatur.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.gob.corsatur.model.InventarioVehiculo;
import sv.gob.corsatur.repository.IInventarioVehiculoRepository;

@Service
@Transactional
public class InventarioVehiculoService {
	
	@Autowired
	private IInventarioVehiculoRepository inventarioVehiculoRepository;
	
	public Optional<InventarioVehiculo> getOne(int id) {
		return inventarioVehiculoRepository.findById(id);
	}
	
	public void save(InventarioVehiculo inventarioVehiculo) {
		inventarioVehiculoRepository.save(inventarioVehiculo);
	}
	
	public boolean existsByPlaca(String placa) {
		return inventarioVehiculoRepository.existsByPlaca(placa);
	}
	
	public boolean existsByNumeroMotor(String motor) {
		return inventarioVehiculoRepository.existsByNumeroMotor(motor);
	}
	
	public boolean existsByNumeroChasis(String chasis) {
		return inventarioVehiculoRepository.existsByNumeroChasis(chasis);
	}

	public Optional<InventarioVehiculo> getByPlaca(String placa) {
		return inventarioVehiculoRepository.findByPlaca(placa);
	}
	
	public boolean existsById(int id) {
		return inventarioVehiculoRepository.existsById(id);
	}
	
	public List<InventarioVehiculo> obtenerActivos(){
		return inventarioVehiculoRepository.obtenerActivos();
	}
	
	public void eliminar(int inventarioVehiculoId, Date updateDate, String userUpdate) {
		inventarioVehiculoRepository.eliminarInventarioVehiculo(inventarioVehiculoId, updateDate, userUpdate);
	}
	
	public Page<InventarioVehiculo> obtenerActivos(Pageable pageable){
		return inventarioVehiculoRepository.findInventarioVehiculo(pageable);
	}

}
