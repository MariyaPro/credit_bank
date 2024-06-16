package com.prokofeva.deal_api.repositories;

import com.prokofeva.deal_api.doman.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CreditRepo extends JpaRepository<Credit, UUID> {

}
