package com.qtrade.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "stocks")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String ticker;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double currentPrice;

    public Stock() {}

    public Stock(String ticker, String name, double currentPrice) {
        this.ticker = ticker;
        this.name = name;
        this.currentPrice = currentPrice;
    }

    public Long getId() { return id; }
    public String getTicker() { return ticker; }
    public void setTicker(String ticker) { this.ticker = ticker; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(double currentPrice) { this.currentPrice = currentPrice; }
}