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
import org.springframework.web.bind.annotation.PatchMapping;
import br.ifsp.contacts.model.Address;
import br.ifsp.contacts.repository.AddressRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {
    
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;

    @GetMapping    
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

  

    @GetMapping("{id}")
    public Contact getContactById(@PathVariable Long id) {
        return contactRepository.findById(id).orElseThrow(() -> new RuntimeException("Contato não encontrado: " + id));
    }

    // exercicio 1 Buscar por nome
    @GetMapping("/search")
    public List<Contact> searchContactsByName(@RequestParam String name) {
        return contactRepository.findByNomeContainingIgnoreCase(name);
    }
    
    @GetMapping("/{id}/addresses")
    public ResponseEntity<List<Address>> getContactAddresses(@PathVariable Long id) {
        if (!contactRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
        List<Address> addresses = addressRepository.findByContactId(id);
        return ResponseEntity.status(HttpStatus.OK).body(addresses);
    }

 

    
 
    @PostMapping
    public Contact createContact(@Valid @RequestBody Contact contact) {
        return contactRepository.save(contact);
    }
    
    @PutMapping("/{id}")
    public Contact updateContact(@PathVariable Long id, @Valid @RequestBody Contact updatedContact) {
        Contact existingContact = contactRepository.findById(id).orElseThrow(
            () -> new RuntimeException("Contato não encontrado: " + id));
        
        existingContact.setNome(updatedContact.getNome());
        existingContact.setEmail(updatedContact.getEmail());
        existingContact.setTelefone(updatedContact.getTelefone());

        return contactRepository.save(existingContact);
    }

     // exercicio 2 Atualização parcial (PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<Contact> partialUpdateContact(@PathVariable Long id, @RequestBody Contact updatedContact) {
        Contact existingContact = contactRepository.findById(id).orElse(null);
        
        // Se não encontrou, retorna 404
        if (existingContact == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
        // Atualiza apenas os campos que foram enviados (não nulos)
        if (updatedContact.getNome() != null) {
            existingContact.setNome(updatedContact.getNome());
        }
        if (updatedContact.getEmail() != null) {
            existingContact.setEmail(updatedContact.getEmail());
        }
        if (updatedContact.getTelefone() != null) {
            existingContact.setTelefone(updatedContact.getTelefone());
        }
        
        // Salva e retorna o contato atualizado
        Contact savedContact = contactRepository.save(existingContact);
        return ResponseEntity.ok(savedContact);
    }

    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable Long id) {
        contactRepository.deleteById(id);
    }

}
