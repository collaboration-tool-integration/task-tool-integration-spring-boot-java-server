package com.pbl.tasktoolintegration.monday.legacy.repository;

import com.pbl.tasktoolintegration.monday.legacy.entity.MondayUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MondayUserRepository extends JpaRepository<MondayUser, String> {

}
