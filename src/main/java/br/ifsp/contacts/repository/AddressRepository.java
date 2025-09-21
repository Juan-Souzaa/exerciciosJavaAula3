package br.ifsp.contacts.repository;

import br.ifsp.contacts.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    
    @Query("SELECT a FROM Address a WHERE a.contact.id = :contactId")
    List<Address> findByContactId(@Param("contactId") Long contactId);
    
    List<Address> findByCidadeContainingIgnoreCase(String cidade);
    List<Address> findByEstadoContainingIgnoreCase(String estado);
    List<Address> findByCepContaining(String cep);
}
