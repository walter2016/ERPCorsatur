package sv.gob.corsatur.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.gob.corsatur.model.Categoria;
import sv.gob.corsatur.repository.ICategoriaRepository;

@Service
@Transactional
public class CategoriaService {
	
	@Autowired
	private ICategoriaRepository categoriaRepository;
	
	public List<Categoria> list() {
		return categoriaRepository.findAll();
	}

	public Optional<Categoria> getOne(int id) {
		return categoriaRepository.findById(id);
	}

	public Optional<Categoria> getByCodigo(String codigo) {
		return categoriaRepository.findByCodigo(codigo);
	}

	public void save(Categoria categoria) {
		categoriaRepository.save(categoria);
	}

	public void delete(int id) {
		categoriaRepository.deleteById(id);
	}

	public boolean existsById(int id) {
		return categoriaRepository.existsById(id);
	}

	public boolean existsByCodigo(String codigo) {
		return categoriaRepository.existsByCodigo(codigo);
	}
	
	public List<Categoria> obtenerActivos(){
		return categoriaRepository.obtenerActivos();
	}
	
	public void eliminar(int categoriaId, Date updateDate, String userUpdate) {
		categoriaRepository.eliminarCategoria(categoriaId, updateDate, userUpdate);
	}
	
	public Page<Categoria> obtenerActivos(Pageable pageable){
		return categoriaRepository.findEstado(pageable);
	}
	

}
