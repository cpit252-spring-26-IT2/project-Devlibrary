package com.DevLibrary.repository;

import com.DevLibrary.Entity.ResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<ResourceEntity, String> {
    @Query("select r.id from ResourceEntity r where r.id like concat(:prefix, '-%')")
    List<String> findIdsByPrefix(@Param("prefix") String prefix);

}
