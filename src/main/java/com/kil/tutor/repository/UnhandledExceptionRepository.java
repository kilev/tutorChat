package com.kil.tutor.repository;

import com.kil.tutor.entity.UnhandledException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnhandledExceptionRepository extends JpaRepository<UnhandledException, Long> {
}
