package com.prokofeva.deal_api.repositories;

import com.prokofeva.deal_api.doman.Employment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmploymentRepo extends JpaRepository<Employment, UUID> {
}
