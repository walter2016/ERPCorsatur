package sv.gob.corsatur.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.gob.corsatur.model.Tipo;
import sv.gob.corsatur.repository.ITipoRepository;

@Service
@Transactional
public class TipoService {
	
	@Autowired
	private ITipoRepository tipoRepository;
	
	public List<Tipo> list() {
		return tipoRepository.findAll();
	}

	public Optional<Tipo> getOne(int id) {
		return tipoRepository.findById(id);
	}

	public Optional<Tipo> getByCodigo(String codigo) {
		return tipoRepository.findByCodigo(codigo);
	}

	public void save(Tipo tipo) {
		tipoRepository.save(tipo);
	}

	public void delete(int id) {
		tipoRepository.deleteById(id);
	}

	public boolean existsById(int id) {
		return tipoRepository.existsById(id);
	}

	public boolean existsByCodigo(String codigo) {
		return tipoRepository.existsByCodigo(codigo);
	}
	
	public List<Tipo> obtenerActivos(){
		return tipoRepository.obtenerActivos();
	}
	
	public void eliminar(int tipoId, Date updateDate, String userUpdate) {
		tipoRepository.eliminarTipo(tipoId, updateDate, userUpdate);
	}
	
	public Page<Tipo> obtenerActivos(Pageable pageable){
		return tipoRepository.findEstado(pageable);
	}
	

}
