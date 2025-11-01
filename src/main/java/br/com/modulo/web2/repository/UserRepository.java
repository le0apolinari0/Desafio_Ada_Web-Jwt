package br.com.modulo.web2.repository;

import br.com.modulo.web2.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByCpf(String cpf);
    Optional<Usuario> findByTelefone(String telefone);
    Optional<Usuario> findByVerificationToken(String token);

    Boolean existsByEmail(String email);
    Boolean existsByCpf(String cpf);
    Boolean existsByTelefone(String telefone);

    @Query("SELECT u FROM User u WHERE u.active = true AND (u.email = :login OR u.cpf = :login OR u.telefone = :login)")
    Optional<Usuario> findByEmailOrCpfOrTelefone(@Param("login") String login);

    Page<Usuario> findByActiveTrue(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.active = true")
    Optional<Usuario> findActiveByEmail(@Param("email") String email);
}
