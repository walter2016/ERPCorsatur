package sv.gob.corsatur.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.gob.corsatur.model.Gerencia;
import sv.gob.corsatur.repository.IGerenciaRepository;

@Service
@Transactional
public class GerenciaService {
	
	@Autowired
	private IGerenciaRepository gerenciaRepository;
	
	
	public Optional<Gerencia> getOne(int id) {
		return gerenciaRepository.findById(id);
	}
	
	public void save(Gerencia gerencia) {
		gerenciaRepository.save(gerencia);
	}
	
	public boolean existsByNombre(String gerencia) {
		return gerenciaRepository.existsByNombre(gerencia);
	}

	public Optional<Gerencia> getByNombre(String nombre) {
		return gerenciaRepository.findByNombre(nombre);
	}
	
	public boolean existsById(int id) {
		return gerenciaRepository.existsById(id);
	}
	
	public List<Gerencia> obtenerActivos(){
		return gerenciaRepository.obtenerActivos();
	}
	
	public void eliminar(int gerenciaId, Date updateDate, String userUpdate) {
		gerenciaRepository.eliminarGerencia(gerenciaId, updateDate, userUpdate);
	}
	
	public Page<Gerencia> obtenerActivos(Pageable pageable){
		return gerenciaRepository.findGerencia(pageable);
	}

}
