package com.proj.banktransaction.service;

import com.proj.banktransaction.entityes.Clients;
import com.proj.banktransaction.repository.ClientsRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Класс для проведения транзакции.
 * Изменённые значения сохраняет в базе (только в оперативной памяти).
 */

@Service
public class TransactionService {
    private final ClientsRep clientsRep;
    private static final String ERROR = "Суммы на счёте не дастаточно для совершения операции.";

    @Autowired
    public TransactionService(ClientsRep clientsRep) {
        this.clientsRep = clientsRep;
    }

    /**
     * Принимает три параметра, проверяет хватит ли денег у отправителя, записывает в БД, генерирует и возвращает сообщение.
     * @param sender    id отправителя
     * @param host      id получателя
     * @param rightSum сумму перевода
     * @return          Сообщение о переводе или ошибке
     * @implSpec
     * Проверяет достаточно денег у отправителя или нет и если да,
     * то отнимает у отправителя и прибавляет получателю, сохраняя изменения в базу данных.
     * Если денег не достаточно, то возвращает строку "ERROR + " Доступные средства " + senderClient.get().getMoney()"
     */
    public String operation(Integer sender, Integer host, BigDecimal rightSum) {
        Optional<Clients> senderClient;
        Optional<Clients> hostClient;
        String message;

        try {
            senderClient = clientsRep.findById(sender);
            hostClient = clientsRep.findById(host);
            message = "Начальный балланс:\nОтправитель " + senderClient.get().getName() + " балланс " + senderClient.get().getMoney() + "\n"
                    + "Получатель " + hostClient.get().getName() + " балланс " + hostClient.get().getMoney();

            if(senderClient.get().getMoney().compareTo(rightSum) > 0) {
                senderClient.get().setMoney(senderClient.get().getMoney().subtract(rightSum));
                hostClient.get().setMoney(hostClient.get().getMoney().add(rightSum));
                clientsRep.save(senderClient.get());
                clientsRep.save(hostClient.get());
            } else {
                return ERROR + " Доступные средства " + senderClient.get().getMoney();
            }
        } catch (Exception e) {
            return e.getMessage();
        }
        message += "\nКонечный балланс:\nОтправитель " + senderClient.get().getName() + " балланс " + senderClient.get().getMoney() + "\n"
                + "Получатель " + hostClient.get().getName() + " балланс " + hostClient.get().getMoney();
        return message;
    }
}
