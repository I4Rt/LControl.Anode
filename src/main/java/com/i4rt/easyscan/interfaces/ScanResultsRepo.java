package com.i4rt.easyscan.interfaces;

import com.i4rt.easyscan.model.ScanResults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScanResultsRepo extends JpaRepository<ScanResults, Long> {
    @Query("FROM ScanResults s ORDER BY s.id DESC")
    public List<ScanResults> findAllOrderById();
}
