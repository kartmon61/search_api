package com.test.search.externalapi.common.validator;

public interface BlogSearchValidator {
    String validateSort(String sort);

    Integer validatePage(Integer page);

    Integer validateSize(Integer size);
}
