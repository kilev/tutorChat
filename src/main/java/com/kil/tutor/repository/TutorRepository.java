package com.kil.tutor.repository;

import com.kil.tutor.entity.user.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, String> {
}
