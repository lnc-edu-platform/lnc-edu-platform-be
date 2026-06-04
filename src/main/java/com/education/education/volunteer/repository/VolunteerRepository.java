package com.education.education.volunteer.repository;

import com.education.education.preference.domain.Subject;
import com.education.education.preference.domain.TargetAudience;
import com.education.education.volunteer.domain.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {

    @Query("SELECT DISTINCT v FROM Volunteer v JOIN v.targetAudiences ta WHERE ta IN :audiences")
    List<Volunteer> findByTargetAudiencesIn(@Param("audiences") List<TargetAudience> audiences);

    @Query("SELECT DISTINCT v FROM Volunteer v JOIN v.subjects s WHERE s IN :subjects")
    List<Volunteer> findBySubjectsIn(@Param("subjects") List<Subject> subjects);
}
