package sv.gob.corsatur.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.gob.corsatur.model.MarcaVehiculo;
import sv.gob.corsatur.repository.IMarcaVehiculoRepository;

@Service
@Transactional
public class MarcaVehiculoService {
	
	@Autowired
	private IMarcaVehiculoRepository marcaVehiculoRepository;
	
	public Optional<MarcaVehiculo> getOne(int id) {
		return marcaVehiculoRepository.findById(id);
	}
	
	public void save(MarcaVehiculo marcavehiculo) {
		marcaVehiculoRepository.save(marcavehiculo);
	}
	
	public boolean existsByMarca(String marca) {
		return marcaVehiculoRepository.existsByMarca(marca);
	}

	public Optional<MarcaVehiculo> getByMarca(String marca) {
		return marcaVehiculoRepository.findByMarca(marca);
	}
	
	public boolean existsById(int id) {
		return marcaVehiculoRepository.existsById(id);
	}
	
	public List<MarcaVehiculo> obtenerActivos(){
		return marcaVehiculoRepository.obtenerActivos();
	}
	
	public void eliminar(int marcaVehiculoId, Date updateDate, String userUpdate) {
		marcaVehiculoRepository.eliminarArea(marcaVehiculoId, updateDate, userUpdate);
	}
	
	public Page<MarcaVehiculo> obtenerActivos(Pageable pageable){
		return marcaVehiculoRepository.findMarcaVehiculo(pageable);
	}


}
