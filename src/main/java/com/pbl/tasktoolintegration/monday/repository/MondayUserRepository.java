package com.pbl.tasktoolintegration.monday.repository;

import com.pbl.tasktoolintegration.monday.entity.MondayUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MondayUserRepository extends JpaRepository<MondayUser, String> {

}
