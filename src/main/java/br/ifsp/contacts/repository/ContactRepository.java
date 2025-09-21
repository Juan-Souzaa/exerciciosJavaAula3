package br.ifsp.contacts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import br.ifsp.contacts.model.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByNomeContainingIgnoreCase(String nome);
}
