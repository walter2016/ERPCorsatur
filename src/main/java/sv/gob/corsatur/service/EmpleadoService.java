package sv.gob.corsatur.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.gob.corsatur.model.Empleado;
import sv.gob.corsatur.repository.IEmpleadoRepository;

@Service
@Transactional
public class EmpleadoService {
	
	@Autowired
	IEmpleadoRepository empleadoRepository;
	
	
	public List<Empleado> list() {
		return empleadoRepository.findAll();
	}

	public Optional<Empleado> getOne(int id) {
		return empleadoRepository.findById(id);
	}

	
	public void save(Empleado empleado) {
		empleadoRepository.save(empleado);
	}

	public void delete(int id) {
		empleadoRepository.deleteById(id);
	}

	public boolean existsById(int id) {
		return empleadoRepository.existsById(id);
	}

	
	public List<Empleado> obtenerActivos(){
		return empleadoRepository.obtenerActivos();
	}
	
	public void eliminar(int empleadoId, Date updateDate, String userUpdate) {
		empleadoRepository.eliminarArea(empleadoId, updateDate, userUpdate);
	}
	
	public Page<Empleado> obtenerActivos(Pageable pageable){
		return empleadoRepository.findEstado(pageable);
	}


}
