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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class ClearingCostControllerTest {

    @Mock
    private IClearingCostService clearingCostService;

    @InjectMocks
    private ClearingCostController clearingCostController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateClearingCost() {
        ClearingCost clearingCost = new ClearingCost(1L, "US", new BigDecimal("5.00"));
        when(clearingCostService.saveClearingCost(any(ClearingCost.class))).thenReturn(clearingCost);

        ResponseEntity<ClearingCost> response = clearingCostController.createClearingCost(clearingCost);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(clearingCost, response.getBody());
    }

    @Test
    public void testGetAllClearingCosts() {
        ClearingCost cost1 = new ClearingCost(1L, "US", new BigDecimal("5.00"));
        ClearingCost cost2 = new ClearingCost(2L, "GR", new BigDecimal("15.00"));
        when(clearingCostService.findAll()).thenReturn(Arrays.asList(cost1, cost2));

        ResponseEntity<List<ClearingCost>> response = clearingCostController.getAllClearingCosts();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetClearingCostByCountry() {
        ClearingCost clearingCost = new ClearingCost(1L, "US", new BigDecimal("5.00"));
        when(clearingCostService.findByCountryCode("US")).thenReturn(clearingCost);

        ResponseEntity<ClearingCost> response = clearingCostController.getClearingCostByCountry("US");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clearingCost, response.getBody());
    }

    @Test
    public void testUpdateClearingCost() {
        ClearingCost updatedClearingCost = new ClearingCost(1L, "US", new BigDecimal("10.00"));
        when(clearingCostService.updateClearingCost(anyLong(), any(ClearingCost.class))).thenReturn(updatedClearingCost);

        ResponseEntity<ClearingCost> response = clearingCostController.updateClearingCost(1L, updatedClearingCost);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedClearingCost, response.getBody());
    }

    @Test
    public void testDeleteClearingCost() {
        ResponseEntity<Void> response = clearingCostController.deleteClearingCost(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}
