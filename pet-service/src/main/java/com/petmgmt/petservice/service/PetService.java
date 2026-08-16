package com.petmgmt.petservice.service;

import com.petmgmt.petservice.client.OwnerClient;
import com.petmgmt.petservice.exception.BadRequestException;
import com.petmgmt.petservice.exception.ResourceNotFoundException;
import com.petmgmt.petservice.model.Pet;
import com.petmgmt.petservice.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final OwnerClient ownerClient;

    public PetService(PetRepository petRepository, OwnerClient ownerClient) {
        this.petRepository = petRepository;
        this.ownerClient = ownerClient;
    }

    public Pet create(Pet pet) {
        if (!ownerClient.ownerExists(pet.getOwnerId())) {
            throw new BadRequestException("No owner found with id: " + pet.getOwnerId());
        }
        return petRepository.save(pet);
    }

    public Pet getById(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found with id: " + id));
    }

    public List<Pet> getByOwnerId(Long ownerId) {
        return petRepository.findByOwnerId(ownerId);
    }

    public List<Pet> getAll() {
        return petRepository.findAll();
    }

    public Pet update(Long id, Pet updates) {
        Pet existing = getById(id);
        existing.setName(updates.getName());
        existing.setSpecies(updates.getSpecies());
        existing.setBreed(updates.getBreed());
        existing.setAge(updates.getAge());
        return petRepository.save(existing);
    }

    public void delete(Long id) {
        Pet existing = getById(id);
        petRepository.delete(existing);
    }
}
