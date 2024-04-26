package com.pbl.tasktoolintegration.monday.repository;

import com.pbl.tasktoolintegration.monday.entity.MondayConfigurations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MondayConfigurationsRepository extends
    JpaRepository<MondayConfigurations, Long> {

}
