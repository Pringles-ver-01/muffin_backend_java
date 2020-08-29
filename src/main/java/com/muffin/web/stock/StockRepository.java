package com.muffin.web.stock;

import com.muffin.web.util.Box;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long>, IStockRepository {

<<<<<<< HEAD
    public Optional<Stock> findByStockName(String stockName);

    boolean existsBySymbol(String symbol);
}



=======

    public Optional<Stock> findByStockName(String stockName);
}


>>>>>>> master
