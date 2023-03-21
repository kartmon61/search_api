package com.test.search.searchapi.repository;

import com.test.search.searchapi.entity.SearchKeyword;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchKeywordRepository extends JpaRepository<SearchKeyword, Long> {

    Optional<SearchKeyword> findByKeyword(String keyword);

    List<SearchKeyword> findTop10ByOrderByCountDesc();
}
