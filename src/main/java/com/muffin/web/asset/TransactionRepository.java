package com.muffin.web.asset;

import com.muffin.web.util.Box;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, ITransactionRepository {
    List<Transaction> findByUserId(Long userId);
}
