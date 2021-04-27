package sv.gob.corsatur.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.gob.corsatur.model.Inventario;
import sv.gob.corsatur.repository.IInventarioRepository;

@Service
@Transactional
public class InventarioService {
	
	@Autowired
	private IInventarioRepository inventarioRepository;

	public List<Inventario> list() {
		return inventarioRepository.findAll();
	}

	public Optional<Inventario> getOne(int id) {
		return inventarioRepository.findById(id);
	}

	public Optional<Inventario> getByCodigo(String codigo) {
		return inventarioRepository.findByCodigoIndividual(codigo);
	}

	public void save(Inventario inventario) {
		inventarioRepository.save(inventario);
	}

	public void delete(int id) {
		inventarioRepository.deleteById(id);
	}

	public boolean existsById(int id) {
		return inventarioRepository.existsById(id);
	}

	public boolean existsByNombre(String nombre) {
		return inventarioRepository.existsByCodigoIndividual(nombre);
	}
	
	public List<Inventario> obtenerActivos(){
		return inventarioRepository.obtenerActivos();
	}
	
	public void eliminar(int inventarioId, Date updateDate, String userUpdate) {
		inventarioRepository.eliminarArea(inventarioId, updateDate, userUpdate);
	}
	
	public Page<Inventario> obtenerActivos(Pageable pageable){
		return inventarioRepository.findEstado(pageable);
	}
	
	public int obtenerUltimo(int tipoId,int haciendaid) {
		return inventarioRepository.buscarCorrelativo(tipoId, haciendaid);
	}

}
