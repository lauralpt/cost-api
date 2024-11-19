package com.etraveli.cardcostapi;

import com.etraveli.cardcostapi.controller.ClearingCostController;
import com.etraveli.cardcostapi.entity.ClearingCost;
import com.etraveli.cardcostapi.service.IClearingCostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link ClearingCostController}.
 * This class uses JUnit and Mockito to test the functionality of {@link ClearingCostController}.
 * The {@link IClearingCostService} is mocked to verify the interactions and ensure that the controller
 * methods behave as expected. Each test method focuses on a specific endpoint of the controller.
 *
 * @see org.mockito.Mock
 * @see org.mockito.InjectMocks
 * @see org.mockito.MockitoAnnotations
 * @see org.junit.jupiter.api.Test
 */

public class ClearingCostControllerTest {

    @Mock
    private IClearingCostService clearingCostService;

    @InjectMocks
    private ClearingCostController clearingCostController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the creation of a clearing cost.
     * Verifies that the {@code createClearingCost} method returns an HTTP status of {@code CREATED}
     * and that the response body contains the expected {@link ClearingCost}.
     */

    @Test
    public void testCreateClearingCost() {
        ClearingCost clearingCost = new ClearingCost(1L, "US", new BigDecimal("5.00"));
        when(clearingCostService.saveClearingCost(any(ClearingCost.class))).thenReturn(clearingCost);

        ResponseEntity<ClearingCost> response = clearingCostController.createClearingCost(clearingCost);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(clearingCost, response.getBody());
    }

    /**
     * Tests retrieving all clearing costs.
     * Verifies that the {@code getAllClearingCosts} method returns an HTTP status of {@code 200 OK}
     * and that the response body contains the correct number of clearing cost records.
     */

    @Test
    public void testGetAllClearingCosts() {
        ClearingCost cost1 = new ClearingCost(1L, "US", new BigDecimal("5.00"));
        ClearingCost cost2 = new ClearingCost(2L, "GR", new BigDecimal("15.00"));
        when(clearingCostService.findAll()).thenReturn(Arrays.asList(cost1, cost2));

        ResponseEntity<List<ClearingCost>> response = clearingCostController.getAllClearingCosts();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
    }

    /**
     * Tests retrieving a clearing cost by country code.
     * Verifies that the {@code getClearingCostByCountry} method returns an HTTP status of {@code 200 OK}
     * and that the response body contains the expected {@link ClearingCost}.
     */

    @Test
    public void testGetClearingCostByCountry() {
        ClearingCost clearingCost = new ClearingCost(1L, "US", new BigDecimal("5.00"));
        when(clearingCostService.findByCountryCode("US")).thenReturn(clearingCost);

        ResponseEntity<ClearingCost> response = clearingCostController.getClearingCostByCountry(clearingCost.getCountryCode());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clearingCost, response.getBody());
    }

    /**
     * Tests updating an existing clearing cost.
     * Verifies that the {@code updateClearingCost} method returns an HTTP status of {@code 200 OK}
     * and that the response body contains the updated {@link ClearingCost}.
     */

    @Test
    public void testUpdateClearingCost() {
        ClearingCost updatedClearingCost = new ClearingCost(1L, "US", new BigDecimal("10.00"));
        when(clearingCostService.updateClearingCost(anyLong(), any(ClearingCost.class))).thenReturn(updatedClearingCost);

        ResponseEntity<ClearingCost> response = clearingCostController.updateClearingCost(1L, updatedClearingCost);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedClearingCost, response.getBody());
    }

    /**
     * Tests deleting an existing clearing cost.
     * Verifies that the {@code deleteClearingCost} method returns an HTTP status of {@code 204 NO CONTENT}.
     */

    @Test
    public void testDeleteClearingCost() {
        ResponseEntity<Void> response = clearingCostController.deleteClearingCost(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}
