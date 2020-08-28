package com.muffin.web.stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long>, IStockRepository {


    public Optional<Stock> findByStockName(String stockName);
}


