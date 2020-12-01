package com.proj.banktransaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Класс проверяет возможно ли произвести транзакцию от одного клиента к другому исходя из поступивших данных.
 */

@Component
public class InputCheck {
    private static final String ERROR = "Для перевода необходимо ввести правильные значения:  (номер отправителя (целое число), " +
            "номер получателя (целое число), сумма перевода (пример: 2134.22))";
    private static final String ERROR_WITH_ZERO = "Заданное число меньше ноля. Укажите положительную сумму перевода.";
    private static final String ERROR_SAME_ID = "Перевод самому себе невозможен. Укажите id получателя отличный от id отправителя";
    private static final String ERROR_CLIENT_NOT_FOUND = "Не найден клиент с id ";

    private final ClientsRep clientsRep;

    @Autowired
    public InputCheck(ClientsRep clientsRep) {
        this.clientsRep = clientsRep;
    }

    /**
     * Валидирует запросы от контроллера {@link HttpController}
     * @param sendFromId
     * @param sendToId
     * @param money
     * @see HttpController
     * @implSpec
     * Все если тут
     */
    public CheckResponse checkValid(String sendFromId, String sendToId, String money) {
        String message = null;
        Integer sender = null;
        Integer host = null;
        BigDecimal rightSumm = null;
        try {
            // Если пришедшие данные невозможно привести к типам необходимым для транзакции, вернёт ERROR
            sender = Integer.valueOf(sendFromId);
            host  = Integer.valueOf(sendToId);
            rightSumm = new BigDecimal(money);
            rightSumm = rightSumm.setScale(2, RoundingMode.HALF_UP);

            // Если сумма перевода отрицательная перезапишет переменную message сообщением ERROR_WITH_ZERO
            if (BigDecimal.ZERO.compareTo(rightSumm) > 0) {
                message = ERROR_WITH_ZERO;
            }

            // Если id отправителя равен id получателя перезапишет переменную message сообщением ERROR_SAME_ID
            if (sender.equals(host)) {
                message = ERROR_SAME_ID;
            }

            // Если клиент с первым указанным id отсутствует в базе данных перезапишет переменную message сообщением
            // ERROR_CLIENT_NOT_FOUND и указанный id
            if (!clientsRep.existsById(sender)) {
                message = ERROR_CLIENT_NOT_FOUND + sender.toString();
            }

            // Если клиент со вторым указанным id отсутствует в базе данных перезапишет переменную message сообщением
            // ERROR_CLIENT_NOT_FOUND и указанный id.
            // Если ранее было определено, что первый указанный id не найден, то переменную message не перезапишет,
            // а дополнит сообщением ERROR_CLIENT_NOT_FOUND и указанный id.
            if (!clientsRep.existsById(host)) {
                if (message !=  null && message.contains("Не найден клиент с id")) {
                    message += "\n" + ERROR_CLIENT_NOT_FOUND + host.toString();
                } else {
                    message = ERROR_CLIENT_NOT_FOUND + host.toString();
                }
            }
        } catch (NumberFormatException | NullPointerException e) {
            message = ERROR;
        } catch (Exception e) {
            message = e.toString();
        }

        // Если переменная message менялась, значит была ошибка и метод вернёт обьект CheckResponse со статусом false
        // и сообщением об ошибке. В противном случае вернёт обьект CheckResponse со статусом true и данными для транзакции.
        if (message != null) {
            return CheckResponse.of(message);
        } else {
            return CheckResponse.of(sender, host, rightSumm);
        }
    }
}