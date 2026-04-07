package com.education.education.reflection.repository;

import com.education.education.reflection.domain.Reflection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReflectionRepository extends JpaRepository<Reflection, Long> {
}
