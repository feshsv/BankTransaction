package com.proj.banktransaction.repository;

import com.proj.banktransaction.entityes.Clients;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientsRep extends CrudRepository<Clients, Integer> {
}