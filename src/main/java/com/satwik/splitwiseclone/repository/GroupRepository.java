package com.satwik.splitwiseclone.repository;

import com.satwik.splitwiseclone.persistence.dto.report.ReportDTO;
import com.satwik.splitwiseclone.persistence.dto.report.TempReport;
import com.satwik.splitwiseclone.persistence.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<Group, UUID> {
    List<Group> findByUserId (UUID userId);

    @Query(value = "SELECT * FROM group_table g WHERE g.default_group = TRUE AND g.user_id = ?1", nativeQuery = true)
    Optional<Group> findDefaultGroup(UUID userId);



//    @Query("SELECT NEW com.satwik.splitwiseclone.persistence.dto.report.ReportDTO(g.groupName, " +
//            "e.description, " +
//            "o.username, " +
//            "e.amount) " +
//            "FROM Group g " +
//            "INNER JOIN g.expenseList e " +
//            "INNER JOIN e.owner o " +
//            "WHERE g.id = ?1")

    @Query("SELECT NEW com.satwik.splitwiseclone.persistence.dto.report.TempReport("+
            "e.id, " +
            "g.groupName, " +
            "e.description, " +
            "e.owner.username, " +
            "e.amount) " +
            "FROM Expense e " +
            "JOIN e.group g " +
            "WHERE g.id = ?1 ")
    List<TempReport> generateReportById(UUID groupId);
}
