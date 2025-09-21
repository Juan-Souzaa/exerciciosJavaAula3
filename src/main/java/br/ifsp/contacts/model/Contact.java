package br.ifsp.contacts.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;

import java.util.List;
import java.util.ArrayList;

@Entity
public class Contact {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    
    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Address> addresses = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    public List<Address> getAddresses() {
        return addresses;
    }
    
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
    
    public void addAddress(Address address) {
        addresses.add(address);
        address.setContact(this);
    }
    
    public void removeAddress(Address address) {
        addresses.remove(address);
        address.setContact(null);
    }

    public Contact() {
    }

    public Contact(String nome, String email, String telefone) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

}
