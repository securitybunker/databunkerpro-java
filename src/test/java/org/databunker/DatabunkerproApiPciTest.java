package org.databunker;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DatabunkerproApiPciTest {
    private DatabunkerproApi api;
    private static final String API_URL = "https://pro.databunker.org";
    private static String tenantName;
    private static String apiToken;
    private static boolean serverAvailable = false;

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        // Fetch tenant credentials from DatabunkerPro test environment
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet("https://databunker.org/api/newtenant.php");
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity);
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> data = mapper.readValue(responseString, Map.class);
                
                if (data != null && "ok".equals(data.get("status"))) {
                    tenantName = (String) data.get("tenantname");
                    apiToken = (String) data.get("xtoken");
                    
                    // Test connection with new credentials
                    try (DatabunkerproApi testApi = new DatabunkerproApi(API_URL, apiToken, tenantName)) {
                        Map<String, Object> result = testApi.getSystemStats(null);
                        serverAvailable = result != null && "ok".equals(result.get("status"));
                        if (serverAvailable) {
                            System.out.println("\nSuccessfully connected to DatabunkerPro server");
                            System.out.println("Tenant: " + tenantName);
                            System.out.println("API URL: " + API_URL);
                        }
                    }
                }
            }
        } catch (Exception e) {
            serverAvailable = false;
            System.out.println("\nFailed to connect to DatabunkerPro server: " + e.getMessage());
        }
    }

    @Before
    public void setUp() throws IOException {
        if (!serverAvailable) {
            throw new RuntimeException("DatabunkerPro server is not available or credentials could not be obtained");
        }
        api = new DatabunkerproApi(API_URL, apiToken, tenantName);
    }

    @After
    public void tearDown() throws IOException {
        if (api != null) {
            api.close();
        }
    }

    @Test
    public void testCreditCardTokenization() throws IOException {
        System.out.println("\nTesting credit card tokenization...");

        // Step 1: Tokenize a credit card number
        String creditCardNumber = "5467047429390590";
        Map<String, Object> options = new HashMap<>();
        options.put("slidingtime", "1d");
        options.put("finaltime", "12m");
        options.put("unique", true);
        
        Map<String, Object> tokenResult = api.createToken("creditcard", creditCardNumber, options, null);
        assertNotNull(tokenResult);
        assertEquals("ok", tokenResult.get("status"));
        assertNotNull(tokenResult.get("tokenuuid"));
        assertNotNull(tokenResult.get("tokenbase"));
        System.out.println("Created token for credit card: " + creditCardNumber);

        // Step 2: Retrieve tokenized data using both token types
        Map<String, Object> tokenDataByUUID = api.getToken((String) tokenResult.get("tokenuuid"), null);
        assertNotNull(tokenDataByUUID);
        assertEquals("ok", tokenDataByUUID.get("status"));
        assertEquals(creditCardNumber, tokenDataByUUID.get("record"));

        Map<String, Object> tokenDataByBase = api.getToken((String) tokenResult.get("tokenbase"), null);
        assertNotNull(tokenDataByBase);
        assertEquals("ok", tokenDataByBase.get("status"));
        assertEquals(creditCardNumber, tokenDataByBase.get("record"));
        System.out.println("Successfully retrieved original credit card number using both token types");

        // Step 3: Delete token and verify deletion
        Map<String, Object> deleteResult = api.deleteToken((String) tokenResult.get("tokenuuid"), null);
        assertNotNull(deleteResult);
        assertEquals("ok", deleteResult.get("status"));

        // Verify both token types are deleted
        try {
            Map<String, Object> response = api.getToken((String) tokenResult.get("tokenuuid"), null);
            if (response != null && "error".equals(response.get("status"))) {
                // Expected error response
                assertNotNull(response.get("message"));
                System.out.println("Token deletion verified (UUID): " + response.get("message"));
            } else {
                fail("Token should be deleted");
            }
        } catch (Exception e) {
            // Expected exception
            System.out.println("Token deletion verified (UUID): " + e.getMessage());
        }

        try {
            Map<String, Object> response = api.getToken((String) tokenResult.get("tokenbase"), null);
            if (response != null && "error".equals(response.get("status"))) {
                // Expected error response
                assertNotNull(response.get("message"));
                System.out.println("Token deletion verified (base): " + response.get("message"));
            } else {
                fail("Token should be deleted");
            }
        } catch (Exception e) {
            // Expected exception
            System.out.println("Token deletion verified (base): " + e.getMessage());
        }
        System.out.println("Successfully deleted token and verified deletion");
    }

    @Test
    public void testBulkCreditCardTokenization() throws IOException {
        System.out.println("\nTesting bulk credit card tokenization...");

        // Step 1: Create multiple tokens in bulk
        String[] creditCards = {
            "5467047429390590",
            "4532015112830366",
            "4716182333661234"
        };

        Object[] records = new Object[creditCards.length];
        for (int i = 0; i < creditCards.length; i++) {
            Map<String, Object> record = new HashMap<>();
            record.put("tokentype", "creditcard");
            record.put("record", creditCards[i]);
            records[i] = record;
        }

        Map<String, Object> options = new HashMap<>();
        options.put("slidingtime", "30d");
        options.put("finaltime", "12m");
        options.put("unique", true);

        Map<String, Object> bulkTokens = api.createTokensBulk(records, options, null);
        System.out.println("Bulk tokens: " + bulkTokens);
        assertNotNull(bulkTokens);
        assertEquals("ok", bulkTokens.get("status"));
        assertNotNull(bulkTokens.get("created"));
        assertEquals(3, bulkTokens.get("num"));
        
        // Verify summary
        Map<String, Object> summary = (Map<String, Object>) bulkTokens.get("summary");
        assertNotNull(summary);
        assertEquals(3, summary.get("created"));
        assertEquals(0, summary.get("duplicates"));
        assertEquals(0, summary.get("errors"));
        assertEquals(3, summary.get("total"));
        
        System.out.println("Created bulk tokens for " + creditCards.length + " credit cards");

        // Step 2: Retrieve multiple tokenized records in bulk
        String[] tokenList = new String[creditCards.length];
        List<Map<String, Object>> createdTokens = (List<Map<String, Object>>) bulkTokens.get("created");
        for (int i = 0; i < creditCards.length; i++) {
            tokenList[i] = (String) createdTokens.get(i).get("tokenbase");
        }

        Map<String, Object> bulkData = api.listTokensBulk(tokenList, null);
        System.out.println("Bulk data: " + bulkData);
        assertNotNull(bulkData);
        assertEquals("ok", bulkData.get("status"));
        assertNotNull(bulkData.get("rows"));
        assertEquals(3, bulkData.get("total"));
        
        // Verify rows
        List<Map<String, Object>> rows = (List<Map<String, Object>>) bulkData.get("rows");
        assertEquals(creditCards.length, rows.size());
        
        // Verify each row contains expected fields
        for (Map<String, Object> row : rows) {
            assertNotNull(row.get("tokenuuid"));
            assertNotNull(row.get("tokenbase"));
            assertNotNull(row.get("record"));
            assertEquals("creditcard", row.get("tokentype"));
        }
        
        System.out.println("Successfully retrieved all tokenized records in bulk");

        // Step 3: Delete multiple tokens in bulk
        Map<String, Object> deleteBulkResult = api.deleteTokensBulk(tokenList, null);
        assertNotNull(deleteBulkResult);
        assertEquals("ok", deleteBulkResult.get("status"));

        // Verify all tokens are deleted
        Map<String, Object> remainingTokens = api.listTokensBulk(tokenList, null);
        assertNotNull(remainingTokens);
        assertEquals("ok", remainingTokens.get("status"));
        assertEquals(0, ((List<?>) remainingTokens.get("rows")).size());
        System.out.println("Successfully deleted all tokens in bulk");

        // Additional error handling tests
        try {
            // Test with invalid token
            Map<String, Object> invalidTokenResponse = api.getToken("invalid-token", null);
            if (invalidTokenResponse != null) {
                assertEquals("error", invalidTokenResponse.get("status"));
                assertNotNull(invalidTokenResponse.get("message"));
                System.out.println("Invalid token test passed: " + invalidTokenResponse.get("message"));
            }
        } catch (Exception e) {
            System.out.println("Invalid token test passed with exception: " + e.getMessage());
        }

        try {
            // Test with empty records array
            Map<String, Object> emptyRecordsResponse = api.createTokensBulk(new Object[0], options, null);
            if (emptyRecordsResponse != null) {
                assertEquals("error", emptyRecordsResponse.get("status"));
                assertNotNull(emptyRecordsResponse.get("message"));
                System.out.println("Empty records test passed: " + emptyRecordsResponse.get("message"));
            }
        } catch (Exception e) {
            System.out.println("Empty records test passed with exception: " + e.getMessage());
        }
    }
} 