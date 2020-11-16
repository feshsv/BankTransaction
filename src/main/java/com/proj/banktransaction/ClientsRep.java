package com.proj.banktransaction;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientsRep extends CrudRepository<Clients, Integer> {
}