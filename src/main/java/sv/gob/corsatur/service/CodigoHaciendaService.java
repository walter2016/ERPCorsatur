package sv.gob.corsatur.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.gob.corsatur.model.CodigoHacienda;
import sv.gob.corsatur.repository.ICodigoHaciendaRepository;

@Service
@Transactional
public class CodigoHaciendaService {
	
	@Autowired
	private ICodigoHaciendaRepository codigoHaciendaRepository;
	
	public List<CodigoHacienda> list() {
		return codigoHaciendaRepository.findAll();
	}

	public Optional<CodigoHacienda> getOne(int id) {
		return codigoHaciendaRepository.findById(id);
	}

	public Optional<CodigoHacienda> getByCodigo(String codigo) {
		return codigoHaciendaRepository.findByCodigo(codigo);
	}

	public void save(CodigoHacienda codigoHacienda) {
		codigoHaciendaRepository.save(codigoHacienda);
	}

	public void delete(int id) {
		codigoHaciendaRepository.deleteById(id);
	}

	public boolean existsById(int id) {
		return codigoHaciendaRepository.existsById(id);
	}

	public boolean existsByCodigo(String codigo) {
		return codigoHaciendaRepository.existsByCodigo(codigo);
	}
	
	public List<CodigoHacienda> obtenerActivos(){
		return codigoHaciendaRepository.obtenerActivos();
	}
	
	public void eliminar(int haciendaId, Date updateDate, String userUpdate) {
		codigoHaciendaRepository.eliminarCodigoHacienda(haciendaId, updateDate, userUpdate);
	}
	
	public Page<CodigoHacienda> obtenerActivosPaginados(Pageable pageable){
		return codigoHaciendaRepository.findEstado(pageable);
	}
	

}
