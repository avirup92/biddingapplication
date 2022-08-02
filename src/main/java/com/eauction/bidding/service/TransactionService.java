package com.eauction.bidding.service;

import com.eauction.bidding.entity.Transaction;
import com.eauction.bidding.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    public Transaction create(Transaction req) {

        UUID trxId = UUID.randomUUID();
        req.setTransactionId(String.valueOf(trxId));
        try {
            return transactionRepository.save(req);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return new Transaction();
    }

    public Transaction update(String productId, String emailld, double newBidAmount) {

        Transaction transaction = transactionRepository.findByProductIdAndEmail(productId,emailld);
        transaction.setBidAmount(newBidAmount);
        transactionRepository.deleteByProductIdAndEmail(productId,emailld);
        return transactionRepository.save(transaction);


    }

    public List<Transaction> findByProductID(String productID) {
        List<Transaction> transactions = transactionRepository.findByProductIdAndFirstNameIsNotNull(productID);
        return transactions;
    }

    public Transaction getTransaction(String transactionId) {
        Optional<Transaction> transaction = transactionRepository.findByTransactionId(transactionId);
        if (transaction.isPresent())
         return transaction.get();
        else
          return new Transaction();
    }

    public ResponseEntity<?> getBidDetails(String productID) {
        List<Transaction> transactions = transactionRepository.findByProductIdAndFirstNameIsNotNull(productID);
        if (transactions.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        else
            return new ResponseEntity<>(null,HttpStatus.OK);
    }
}
