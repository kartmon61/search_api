package com.test.search.externalapi.kakao.validator;

import static org.assertj.core.api.Assertions.*;

import com.test.search.externalapi.common.exception.ExternalApiErrorCode;
import com.test.search.externalapi.common.exception.ExternalApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class kakaoBlogSearchValidatorTest {
    private KakaoBlogSearchValidator validator;

    @BeforeEach
    void setUp() {
        validator = new KakaoBlogSearchValidator();
    }


    @Test
    @DisplayName("sort 관련 validation 테스트")
    void testValidateSort() {
        assertThat(validator.validateSort("accuracy")).isEqualTo("accuracy");
        assertThat(validator.validateSort("recency")).isEqualTo("recency");
        assertThat(validator.validateSort(null)).isEqualTo(null);
        assertThatThrownBy(() -> validator.validateSort("invalidSort"))
            .isInstanceOf(ExternalApiException.class)
            .hasMessageContaining(ExternalApiErrorCode.SORT_VALUE_INVALID.getMessage());
    }

    @Test
    @DisplayName("page 관련 validation 테스트")
    void testValidatePage() {
        assertThat(validator.validatePage(1)).isEqualTo(1);
        assertThat(validator.validatePage(10)).isEqualTo(10);
        assertThat(validator.validatePage(null)).isEqualTo(null);
        assertThatThrownBy(() -> validator.validatePage(-1))
            .isInstanceOf(ExternalApiException.class)
            .hasMessageContaining(ExternalApiErrorCode.PAGE_NUMBER_INVALID.getMessage());
        assertThatThrownBy(() -> validator.validatePage(0))
            .isInstanceOf(ExternalApiException.class)
            .hasMessageContaining(ExternalApiErrorCode.PAGE_NUMBER_INVALID.getMessage());
    }

    @Test
    @DisplayName("size 관련 validation 테스트")
    void testValidateSize() {
        assertThat(validator.validateSize(1)).isEqualTo(1);
        assertThat(validator.validateSize(10)).isEqualTo(10);
        assertThat(validator.validateSize(50)).isEqualTo(50);
        assertThat(validator.validateSize(null)).isEqualTo(null);
        assertThatThrownBy(() -> validator.validateSize(-1))
            .isInstanceOf(ExternalApiException.class)
            .hasMessageContaining(ExternalApiErrorCode.PAGE_SIZE_INVALID.getMessage());
        assertThatThrownBy(() -> validator.validateSize(0))
            .isInstanceOf(ExternalApiException.class)
            .hasMessageContaining(ExternalApiErrorCode.PAGE_SIZE_INVALID.getMessage());
        assertThatThrownBy(() -> validator.validateSize(51))
            .isInstanceOf(ExternalApiException.class)
            .hasMessageContaining(ExternalApiErrorCode.PAGE_SIZE_INVALID.getMessage());
    }
}