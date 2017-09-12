package br.com.thiagohora.categoria.repository;

import br.com.thiagohora.categoria.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}