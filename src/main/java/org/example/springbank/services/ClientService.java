package org.example.springbank.services;

import org.example.springbank.exceptions.ClientNotFoundException;
import org.example.springbank.models.Client;
import org.example.springbank.repositories.ClientRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional
    public void addClient(Client client) {
        if (client == null || client.getName() == null) {
            throw new IllegalArgumentException("Client or client name cannot be null.");
        }

        clientRepository.save(client);
        System.out.println(
                "CLIENT IN ClientService" + clientRepository.findByPattern(client.getName(), null));
    }

    @Transactional(readOnly = true)
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Client> findAll(Pageable pageable) {
        return clientRepository.findAll(pageable).getContent();
    }

    @Transactional(readOnly = true)
    public Client getByName(String name) {
        return clientRepository
                .findByName(name)
                .orElseThrow(() -> new ClientNotFoundException(name));
    }

    @Transactional(readOnly = true)
    public List<Client> findByPattern(String pattern, Pageable pageable) {
        return clientRepository.findByPattern(pattern, pageable);
    }

    @Transactional(readOnly = true)
    public long count() {
        return clientRepository.count();
    }

    @Transactional
    public void deleteClient(long[] idList) {
        for (long id : idList) {
            Client client =
                    clientRepository
                            .findById(id)
                            .orElseThrow(() -> new ClientNotFoundException(id));
            clientRepository.delete(client);
        }
    }

    public void deleteAllClients() {
        clientRepository.deleteAll();
    }
}
