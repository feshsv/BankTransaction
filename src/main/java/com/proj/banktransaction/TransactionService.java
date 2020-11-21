package com.proj.banktransaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransactionService {
    private final ClientsRep clientsRep;
    private static final String ERROR = "Суммы на счёте не дастаточно для совершения операции.";

    @Autowired
    public TransactionService(ClientsRep clientsRep) {
        this.clientsRep = clientsRep;
    }

    public String operation(Integer sender, Integer host, BigDecimal rightSumm) {
        Optional<Clients> senderClient;
        Optional<Clients> hostClient;
        String message = "";

        try {
            senderClient = clientsRep.findById(sender);
            hostClient = clientsRep.findById(host);
            message = "Начальный балланс:\nОтправитель " + senderClient.get().getName() + " балланс " + senderClient.get().getMoney() + "\n"
                    + "Получатель " + hostClient.get().getName() + " балланс " + hostClient.get().getMoney();
            if(senderClient.get().getMoney().compareTo(rightSumm) > 0) {
                senderClient.get().setMoney(senderClient.get().getMoney().subtract(rightSumm));
                hostClient.get().setMoney(hostClient.get().getMoney().add(rightSumm));
                clientsRep.save(senderClient.get());
                clientsRep.save(hostClient.get());
            } else {
                return ERROR + " Доступные средства " + senderClient.get().getMoney();
            }
        } catch (Exception e) {
            return e.toString();
        }
        message += "\nКонечный балланс:\nОтправитель " + senderClient.get().getName() + " балланс " + senderClient.get().getMoney() + "\n"
                + "Получатель " + hostClient.get().getName() + " балланс " + hostClient.get().getMoney();
        return message;
    }
}
