package br.ifsp.contacts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifsp.contacts.model.Contact;
import br.ifsp.contacts.repository.ContactRepository;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {
    
    @Autowired
    private ContactRepository contactRepository;

    @GetMapping    
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

  

    @GetMapping("{id}")
    public Contact getContactById(@PathVariable Long id) {
        return contactRepository.findById(id).orElseThrow(() -> new RuntimeException("Contato não encontrado: " + id));
    }

    // exercicio 1 Buscar por nome

 

    
 
    @PostMapping
    public Contact createContact(@RequestBody Contact contact) {
        return contactRepository.save(contact);
    }
    
    @PutMapping("/{id}")
    public Contact updateContact(@PathVariable Long id, @RequestBody Contact updatedContact) {
        Contact existingContact = contactRepository.findById(id).orElseThrow(
            () -> new RuntimeException("Contato não encontrado: " + id));
        
        existingContact.setNome(updatedContact.getNome());
        existingContact.setEmail(updatedContact.getEmail());
        existingContact.setTelefone(updatedContact.getTelefone());

        return contactRepository.save(existingContact);
    }

    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable Long id) {
        contactRepository.deleteById(id);
    }

}
