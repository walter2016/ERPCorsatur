package sv.gob.corsatur.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.gob.corsatur.model.Area;
import sv.gob.corsatur.repository.IAreaRepository;

@Service
@Transactional
public class AreaService {

	@Autowired
	private IAreaRepository areaRepository;

	public List<Area> list() {
		return areaRepository.findAll();
	}

	public Optional<Area> getOne(int id) {
		return areaRepository.findById(id);
	}

	public Optional<Area> getByNombre(String nombre) {
		return areaRepository.findByNombre(nombre);
	}

	public void save(Area producto) {
		areaRepository.save(producto);
	}

	public void delete(int id) {
		areaRepository.deleteById(id);
	}

	public boolean existsById(int id) {
		return areaRepository.existsById(id);
	}

	public boolean existsByNombre(String nombre) {
		return areaRepository.existsByNombre(nombre);
	}
	
	public List<Area> obtenerActivos(){
		return areaRepository.obtenerActivos();
	}
	
	public void eliminar(int areaId, Date updateDate, String userUpdate) {
		areaRepository.eliminarArea(areaId, updateDate, userUpdate);
	}
	
	public Page<Area> obtenerActivos(Pageable pageable){
		return areaRepository.findEstado(pageable);
	}


}
