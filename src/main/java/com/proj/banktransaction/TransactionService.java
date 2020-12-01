package com.proj.banktransaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Класс проверяет достаточно ли суммы на счёте отправителя и в случае успешной проверки переводит деньги на счёт получателя.
 * Изменённые значения сохраняет в базе (только в оперативной памяти). Возвращает сообщение о начальных и конечных значениях транзакции.
 *
 * Если продолжение невозможно, то возвращает сообщение о причине.
 */

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
        String message;

        try {
            senderClient = clientsRep.findById(sender);
            hostClient = clientsRep.findById(host);
            message = "Начальный балланс:\nОтправитель " + senderClient.get().getName() + " балланс " + senderClient.get().getMoney() + "\n"
                    + "Получатель " + hostClient.get().getName() + " балланс " + hostClient.get().getMoney();

            // Тут проверяет достаточно денег у отправителя или нет и если да,
            // то отнимает у отправителя и прибавляет получателю, сохраняя изменения в базу данных.
            // Если денег не достаточно, то возвращает строку "ERROR + " Доступные средства " + senderClient.get().getMoney()"
            if(senderClient.get().getMoney().compareTo(rightSumm) > 0) {
                senderClient.get().setMoney(senderClient.get().getMoney().subtract(rightSumm));
                hostClient.get().setMoney(hostClient.get().getMoney().add(rightSumm));
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
