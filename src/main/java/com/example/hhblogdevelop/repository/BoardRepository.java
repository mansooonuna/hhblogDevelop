package com.example.hhblogdevelop.repository;

import com.example.hhblogdevelop.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface BoardRepository extends JpaRepository<BoardFile, Long> {

    static final String SELECT_FILE_ID = "SELECT ID FROM board_file " +
            "WHERE BOARD_ID = :boardId AND DELETE_YN != 'Y'";

    static final String UPDATE_DELETE_YN = "UPDATE board_file " +
            "SET DELETE_YN = 'Y' " +
            "WHERE ID IN (:deleteIdList)";

    static final String DELETE_BOARD_FILE_YN = "UPDATE board_file " +
            "SET DELETE_YN = 'Y' " +
            "WHERE BOARD_ID IN (:boardIdList)";

    @Query(value = SELECT_FILE_ID, nativeQuery = true)
    public List<Long> findByBoardId(@Param("boardId") Long boardId);

    @Transactional
    @Modifying
    @Query(value = UPDATE_DELETE_YN, nativeQuery = true)
    public int updateDeleteYn(@Param("deleteIdList") Long[] deleteIdList);

    @Transactional
    @Modifying
    @Query(value = DELETE_BOARD_FILE_YN, nativeQuery = true)
    public int deleteBoardFileYn(@Param("boardIdList") Long[] boardIdList);
}