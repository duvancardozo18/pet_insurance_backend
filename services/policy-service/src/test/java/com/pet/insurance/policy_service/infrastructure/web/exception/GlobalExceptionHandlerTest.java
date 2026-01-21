package com.pet.insurance.policy_service.infrastructure.web.exception;

import com.pet.insurance.policy_service.domain.exception.QuotationExpiredException;
import com.pet.insurance.policy_service.domain.exception.QuotationNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    @DisplayName("should handle QuotationNotFoundException correctly")
    void shouldHandleQuotationNotFoundException() {
        // Arrange
        String quotationId = "test-quotation-123";
        QuotationNotFoundException exception = new QuotationNotFoundException(quotationId);

        // Act & Assert
        StepVerifier.create(globalExceptionHandler.handleQuotationNotFoundException(exception))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

                    Map<String, Object> body = response.getBody();
                    assertNotNull(body);
                    assertNotNull(body.get("timestamp"));
                    assertEquals(HttpStatus.NOT_FOUND.value(), body.get("status"));
                    assertEquals("Not Found", body.get("error"));
                    assertTrue(body.get("message").toString().contains(quotationId));
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("should handle QuotationExpiredException correctly")
    void shouldHandleQuotationExpiredException() {
        // Arrange
        String quotationId = "expired-quotation-456";
        QuotationExpiredException exception = new QuotationExpiredException(quotationId);

        // Act & Assert
        StepVerifier.create(globalExceptionHandler.handleQuotationExpiredException(exception))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

                    Map<String, Object> body = response.getBody();
                    assertNotNull(body);
                    assertNotNull(body.get("timestamp"));
                    assertEquals(HttpStatus.BAD_REQUEST.value(), body.get("status"));
                    assertEquals("Bad Request", body.get("error"));
                    assertNotNull(body.get("message"));
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("should handle IllegalArgumentException correctly")
    void shouldHandleIllegalArgumentException() {
        // Arrange
        String errorMessage = "Invalid argument provided";
        IllegalArgumentException exception = new IllegalArgumentException(errorMessage);

        // Act & Assert
        StepVerifier.create(globalExceptionHandler.handleIllegalArgumentException(exception))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

                    Map<String, Object> body = response.getBody();
                    assertNotNull(body);
                    assertNotNull(body.get("timestamp"));
                    assertEquals(HttpStatus.BAD_REQUEST.value(), body.get("status"));
                    assertEquals("Bad Request", body.get("error"));
                    assertEquals(errorMessage, body.get("message"));
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("should handle generic Exception correctly")
    void shouldHandleGenericException() {
        // Arrange
        String errorMessage = "Unexpected error occurred";
        Exception exception = new Exception(errorMessage);

        // Act & Assert
        StepVerifier.create(globalExceptionHandler.handleGenericException(exception))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

                    Map<String, Object> body = response.getBody();
                    assertNotNull(body);
                    assertNotNull(body.get("timestamp"));
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), body.get("status"));
                    assertEquals("Internal Server Error", body.get("error"));
                    assertTrue(body.get("message").toString().contains("An unexpected error occurred"));
                    assertTrue(body.get("message").toString().contains(errorMessage));
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("should include timestamp in all error responses")
    void shouldIncludeTimestampInAllErrorResponses() {
        // Arrange
        QuotationNotFoundException notFoundEx = new QuotationNotFoundException("test-id");
        QuotationExpiredException expiredEx = new QuotationExpiredException("test-id");
        IllegalArgumentException illegalArgEx = new IllegalArgumentException("Invalid");
        Exception genericEx = new Exception("Generic error");

        // Act & Assert - QuotationNotFoundException
        StepVerifier.create(globalExceptionHandler.handleQuotationNotFoundException(notFoundEx))
                .assertNext(response -> {
                    Map<String, Object> body = response.getBody();
                    assertNotNull(body);
                    assertInstanceOf(LocalDateTime.class, body.get("timestamp"));
                })
                .verifyComplete();

        // QuotationExpiredException
        StepVerifier.create(globalExceptionHandler.handleQuotationExpiredException(expiredEx))
                .assertNext(response -> {
                    Map<String, Object> body = response.getBody();
                    assertNotNull(body);
                    assertInstanceOf(LocalDateTime.class, body.get("timestamp"));
                })
                .verifyComplete();

        // IllegalArgumentException
        StepVerifier.create(globalExceptionHandler.handleIllegalArgumentException(illegalArgEx))
                .assertNext(response -> {
                    Map<String, Object> body = response.getBody();
                    assertNotNull(body);
                    assertInstanceOf(LocalDateTime.class, body.get("timestamp"));
                })
                .verifyComplete();

        // Generic Exception
        StepVerifier.create(globalExceptionHandler.handleGenericException(genericEx))
                .assertNext(response -> {
                    Map<String, Object> body = response.getBody();
                    assertNotNull(body);
                    assertInstanceOf(LocalDateTime.class, body.get("timestamp"));
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("should return correct HTTP status for each exception type")
    void shouldReturnCorrectHttpStatusForEachExceptionType() {
        // Arrange
        QuotationNotFoundException notFoundEx = new QuotationNotFoundException("test-id");
        QuotationExpiredException expiredEx = new QuotationExpiredException("test-id");
        IllegalArgumentException illegalArgEx = new IllegalArgumentException("Invalid");
        Exception genericEx = new Exception("Generic error");

        // Act & Assert
        StepVerifier.create(globalExceptionHandler.handleQuotationNotFoundException(notFoundEx))
                .assertNext(response -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()))
                .verifyComplete();

        StepVerifier.create(globalExceptionHandler.handleQuotationExpiredException(expiredEx))
                .assertNext(response -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()))
                .verifyComplete();

        StepVerifier.create(globalExceptionHandler.handleIllegalArgumentException(illegalArgEx))
                .assertNext(response -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()))
                .verifyComplete();

        StepVerifier.create(globalExceptionHandler.handleGenericException(genericEx))
                .assertNext(response -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode()))
                .verifyComplete();
    }

    @Test
    @DisplayName("should return proper error body structure")
    void shouldReturnProperErrorBodyStructure() {
        // Arrange
        QuotationNotFoundException exception = new QuotationNotFoundException("test-id");

        // Act & Assert
        StepVerifier.create(globalExceptionHandler.handleQuotationNotFoundException(exception))
                .assertNext(response -> {
                    Map<String, Object> body = response.getBody();
                    assertNotNull(body);
                    assertTrue(body.containsKey("timestamp"));
                    assertTrue(body.containsKey("status"));
                    assertTrue(body.containsKey("error"));
                    assertTrue(body.containsKey("message"));
                    assertEquals(4, body.size());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("should return Mono of ResponseEntity")
    void shouldReturnMonoOfResponseEntity() {
        // Arrange
        QuotationNotFoundException exception = new QuotationNotFoundException("test-id");

        // Act
        var result = globalExceptionHandler.handleQuotationNotFoundException(exception);

        // Assert
        assertNotNull(result);
        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();
    }
}
