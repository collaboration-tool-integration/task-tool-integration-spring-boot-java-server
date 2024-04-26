package com.pbl.tasktoolintegration.monday.repository;

import com.pbl.tasktoolintegration.monday.entity.MondayUsers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MondayUsersRepository extends JpaRepository<MondayUsers, String> {

}
