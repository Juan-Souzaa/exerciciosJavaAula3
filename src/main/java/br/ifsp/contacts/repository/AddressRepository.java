package br.ifsp.contacts.repository;

import br.ifsp.contacts.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AddressRepository extends JpaRepository<Address, Long> {
    
    @Query("SELECT a FROM Address a WHERE a.contact.id = :contactId")
    Page<Address> findByContactId(@Param("contactId") Long contactId, Pageable pageable);
    
    Page<Address> findByCidadeContainingIgnoreCase(String cidade, Pageable pageable);
    Page<Address> findByEstadoContainingIgnoreCase(String estado, Pageable pageable);
    Page<Address> findByCepContaining(String cep, Pageable pageable);
}
