package sv.gob.corsatur.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.gob.corsatur.model.Area;
import sv.gob.corsatur.model.Configuracion;
import sv.gob.corsatur.repository.IAreaRepository;
import sv.gob.corsatur.repository.IConfiguracionRepository;

@Service
@Transactional
public class ConfiguracionService {
	
	@Autowired
	private IConfiguracionRepository configuracionRepository;

	
	public Optional<Configuracion> getOne(int id) {
		return configuracionRepository.findById(id);
	}

	public Optional<Configuracion> getByCodigoClasificacion(String codigo) {
		return configuracionRepository.findByCodigoClasificacion(codigo);
	}

	public void save(Configuracion configuracion) {
		configuracionRepository.save(configuracion);
	}
	
	public boolean existsById(int id) {
		return configuracionRepository.existsById(id);
	}

	public boolean existsByNombre(String codigo) {
		return configuracionRepository.existsByCodigoClasificacion(codigo);
	}
	
	public Page<Configuracion> obtenerActivos(Pageable pageable){
		return configuracionRepository.findEstado(pageable);
	}
}
