package com.proj.banktransaction;


import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Clients {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private BigDecimal account;

    public Clients() {
    }

    public Clients(Integer id, String name, BigDecimal money) {
        this.id = id;
        this.name = name;
        this.account = money;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getMoney() {
        return account;
    }

    public void setMoney(BigDecimal money) {
        this.account = money;
    }

    @Override
    public String toString() {
        return "Clients{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", money=" + account +
                '}';
    }
}
