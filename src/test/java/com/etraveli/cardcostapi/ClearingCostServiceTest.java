package com.etraveli.cardcostapi;

import com.etraveli.cardcostapi.dto.BinlistDto;
import com.etraveli.cardcostapi.service.BinlistService;
import com.etraveli.cardcostapi.entity.ClearingCost;
import com.etraveli.cardcostapi.repository.ClearingCostRepository;
import com.etraveli.cardcostapi.service.ClearingCostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ClearingCostService}.
 * This class uses JUnit and Mockito to test the functionality of {@link ClearingCostService}.
 * The {@link ClearingCostRepository} and {@link BinlistService} are mocked to verify the interactions
 * and ensure that the service methods behave as expected.
 * Each test method focuses on a specific function within the {@link ClearingCostService} class.
 *
 * @see org.mockito.Mock
 * @see org.mockito.InjectMocks
 * @see org.mockito.MockitoAnnotations
 * @see org.junit.jupiter.api.Test
 */

public class ClearingCostServiceTest {

    @Mock
    private ClearingCostRepository clearingCostRepository;

    @Mock
    private BinlistService binlistService;

    @InjectMocks
    private ClearingCostService clearingCostService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the save functionality of clearing cost.
     * Verifies that the {@code saveClearingCost} method correctly saves a new clearing cost entity
     * and that the returned entity contains the expected country code and cost.
     */
    @Test
    public void testSaveClearingCost() {
        ClearingCost clearingCost = new ClearingCost(1L, "US", new BigDecimal("5.00"));
        when(clearingCostRepository.save(any(ClearingCost.class))).thenReturn(clearingCost);

        ClearingCost result = clearingCostService.saveClearingCost(clearingCost);
        assertEquals("US", result.getCountryCode());
        assertEquals(new BigDecimal("5.00"), result.getCost());
    }

    /**
     * Tests the retrieval of all clearing costs.
     * Verifies that the {@code findAll} method returns a list of all clearing costs
     * and that the list size and contents are as expected.
     */
    @Test
    public void testFindAll() {
        ClearingCost cost1 = new ClearingCost(1L, "US", new BigDecimal("5.00"));
        ClearingCost cost2 = new ClearingCost(2L, "GR", new BigDecimal("15.00"));
        when(clearingCostRepository.findAll()).thenReturn(Arrays.asList(cost1, cost2));

        List<ClearingCost> result = clearingCostService.findAll();
        assertEquals(2, result.size());
        assertEquals("US", result.get(0).getCountryCode());
        assertEquals("GR", result.get(1).getCountryCode());
    }

    /**
     * Tests the retrieval of a clearing cost by country code.
     * Verifies that the {@code findByCountryCode} method correctly finds the clearing cost
     * and that the returned entity contains the expected country code and cost.
     */
    @Test
    public void testFindByCountryCode() {
        ClearingCost clearingCost = new ClearingCost(1L, "US", new BigDecimal("5.00"));
        when(clearingCostRepository.findByCountryCode("US")).thenReturn(Optional.of(clearingCost));

        ClearingCost result = clearingCostService.findByCountryCode("US");
        assertEquals("US", result.getCountryCode());
        assertEquals(new BigDecimal("5.00"), result.getCost());
    }

    /**
     * Tests the update functionality for clearing costs.
     * Verifies that the {@code updateClearingCost} method successfully updates an existing
     * clearing cost and returns the updated clearing cost entity.
     */
    @Test
    public void testUpdateClearingCost() {
        ClearingCost existingCost = new ClearingCost(1L, "US", new BigDecimal("5.00"));
        ClearingCost updatedCost = new ClearingCost(1L, "US", new BigDecimal("10.00"));

        when(clearingCostRepository.findById(anyLong())).thenReturn(Optional.of(existingCost));
        when(clearingCostRepository.save(any(ClearingCost.class))).thenReturn(updatedCost);

        ClearingCost result = clearingCostService.updateClearingCost(1L, updatedCost);
        assertEquals(new BigDecimal("10.00"), result.getCost());
    }

    /**
     * Tests the deletion of a clearing cost.
     * Verifies that the {@code deleteClearingCost} method deletes the specified clearing cost entity.
     */
    @Test
    public void testDeleteClearingCost() {
        ClearingCost clearingCost = new ClearingCost(1L, "US", new BigDecimal("5.00"));
        when(clearingCostRepository.findById(anyLong())).thenReturn(Optional.of(clearingCost));

        clearingCostService.deleteClearingCost(1L);
        verify(clearingCostRepository, times(1)).deleteById(1L);
    }

    /**
     * Tests the calculation of clearing cost using card number.
     * Verifies that the {@code calculateClearingCost} method correctly retrieves country information
     * from the {@link BinlistService}, and calculates the cost by querying the {@link ClearingCostRepository}.
     */
    @Test
    public void testCalculateClearingCost() {
        // Set up the mock BinlistResponse
        BinlistDto.Country country = new BinlistDto.Country();
        country.setAlpha2("US");
        BinlistDto binlistDto = new BinlistDto();
        binlistDto.setCountry(country);
        when(binlistService.getCountryCodeByCardNumber("45717360")).thenReturn(binlistDto);
        // Set up the repository response for cost calculation
        ClearingCost clearingCost = new ClearingCost(1L, "US", new BigDecimal("5.00"));
        when(clearingCostRepository.findByCountryCode("US")).thenReturn(Optional.of(clearingCost));

        BigDecimal cost = clearingCostService.calculateClearingCost("45717360");
        assertEquals(new BigDecimal("5.00"), cost);
    }
}

