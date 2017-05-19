package pl.kostro.expensesystem.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pl.kostro.expensesystem.model.UserLimit;
import pl.kostro.expensesystem.model.UserSummary;

public interface UserLimitRepository extends JpaRepository<UserLimit, Long> {

  @Query("select us from UserLimit ul join ul.userSummaryList us where ul = :userLimit")
  List<UserSummary> findUserSummaryList(@Param("userLimit") UserLimit userLimit);

}
