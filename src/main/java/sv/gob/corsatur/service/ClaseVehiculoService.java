package sv.gob.corsatur.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.gob.corsatur.model.ClaseVehiculo;
import sv.gob.corsatur.repository.IClaseVehiculoRepository;

@Service
@Transactional
public class ClaseVehiculoService {
	
	@Autowired
	private IClaseVehiculoRepository claseVehiculoRepository;
	
	public Optional<ClaseVehiculo> getOne(int id) {
		return claseVehiculoRepository.findById(id);
	}
	
	public void save(ClaseVehiculo clasevehiculo) {
		claseVehiculoRepository.save(clasevehiculo);
	}
	
	public boolean existsByClase(String clase) {
		return claseVehiculoRepository.existsByClase(clase);
	}

	public Optional<ClaseVehiculo> getByClase(String clase) {
		return claseVehiculoRepository.findByClase(clase);
	}
	
	public boolean existsById(int id) {
		return claseVehiculoRepository.existsById(id);
	}
	
	public List<ClaseVehiculo> obtenerActivos(){
		return claseVehiculoRepository.obtenerActivos();
	}
	
	public void eliminar(int claseVehiculoId, Date updateDate, String userUpdate) {
		claseVehiculoRepository.eliminarArea(claseVehiculoId, updateDate, userUpdate);
	}
	
	public Page<ClaseVehiculo> obtenerActivos(Pageable pageable){
		return claseVehiculoRepository.findEstado(pageable);
	}

}
