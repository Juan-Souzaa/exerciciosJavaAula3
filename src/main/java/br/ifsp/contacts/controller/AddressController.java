package br.ifsp.contacts.controller;

import br.ifsp.contacts.model.Address;
import br.ifsp.contacts.model.Contact;
import br.ifsp.contacts.repository.AddressRepository;
import br.ifsp.contacts.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {
    
    @Autowired
    private AddressRepository addressRepository;
    
    @Autowired
    private ContactRepository contactRepository;
    
    @GetMapping
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public Address getAddressById(@PathVariable Long id) {
        return addressRepository.findById(id).orElseThrow(() -> new RuntimeException("Endereço não encontrado: " + id));
    }
    
    @PostMapping
    public ResponseEntity<Address> createAddress(@RequestBody Address address) {
        if (address.getContact() == null || address.getContact().getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
        Contact contact = contactRepository.findById(address.getContact().getId()).orElse(null);
        if (contact == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
        address.setContact(contact);
        Address savedAddress = addressRepository.save(address);
        return ResponseEntity.status(HttpStatus.OK).body(savedAddress);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long id, @RequestBody Address updatedAddress) {
        Address address = addressRepository.findById(id).orElse(null);
        if (address == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
        address.setRua(updatedAddress.getRua());
        address.setCidade(updatedAddress.getCidade());
        address.setEstado(updatedAddress.getEstado());
        address.setCep(updatedAddress.getCep());
        
        if (updatedAddress.getContact() != null && updatedAddress.getContact().getId() != null) {
            Contact contact = contactRepository.findById(updatedAddress.getContact().getId()).orElse(null);
            if (contact == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            address.setContact(contact);
        }
        
        Address savedAddress = addressRepository.save(address);
        return ResponseEntity.status(HttpStatus.OK).body(savedAddress);
    }
    
    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable Long id) {
        addressRepository.deleteById(id);
    }
    
    @GetMapping("/search/cidade")
    public List<Address> getAddressesByCity(@RequestParam String cidade) {
        return addressRepository.findByCidadeContainingIgnoreCase(cidade);
    }
    
    @GetMapping("/search/estado")
    public List<Address> getAddressesByState(@RequestParam String estado) {
        return addressRepository.findByEstadoContainingIgnoreCase(estado);
    }
    
    @GetMapping("/search/cep")
    public List<Address> getAddressesByCep(@RequestParam String cep) {
        return addressRepository.findByCepContaining(cep);
    }
}
