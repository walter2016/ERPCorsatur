package sv.gob.corsatur.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.gob.corsatur.model.Condicion;
import sv.gob.corsatur.repository.ICondicionRepository;

@Service
@Transactional
public class CondicionService {
	
	@Autowired
	private ICondicionRepository condicionRepository;
	
	public List<Condicion> obtenerActivos(){
		return condicionRepository.obtenerActivos();
	}

}
