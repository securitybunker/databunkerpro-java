package org.databunkerpro;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.*;

public class DatabunkerproApiTest {
    private DatabunkerproApi api;
    private static final String API_URL = "https://pro.databunker.org";
    private static String tenantName;
    private static String apiToken;
    private static boolean serverAvailable = false;
    private static final Random random = new Random();

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
    public void testCreateUser() throws IOException {
        System.out.println("\nTesting user creation...");
        Map<String, Object> userData = Map.of(
            "email", "test" + random.nextInt(1000000) + "@example.com",
            "name", "Test User " + random.nextInt(1000000),
            "phone", String.valueOf(random.nextInt(1000000))
        );
        Map<String, Object> result = api.createUser(userData, null, null);
        assertNotNull(result);
        assertEquals("ok", result.get("status"));
        assertNotNull(result.get("token"));
        System.out.println("Created user with email: " + userData.get("email"));
    }

    @Test
    public void testGetUser() throws IOException {
        System.out.println("\nTesting user retrieval...");
        String email = "test" + random.nextInt(1000000) + "@example.com";
        Map<String, Object> userData = Map.of(
            "email", email,
            "name", "Test User " + random.nextInt(1000000),
            "phone", String.valueOf(random.nextInt(1000000))
        );
        api.createUser(userData, null, null);
        
        Map<String, Object> result = api.getUser("email", email, null);
        assertNotNull(result);
        assertEquals("ok", result.get("status"));
        assertNotNull(result.get("profile"));
        System.out.println("Retrieved user with email: " + email);
    }

    @Test
    public void testUpdateUser() throws IOException {
        System.out.println("\nTesting user update...");
        String email = "test" + random.nextInt(1000000) + "@example.com";
        Map<String, Object> userData = Map.of(
            "email", email,
            "name", "Test User " + random.nextInt(1000000),
            "phone", String.valueOf(random.nextInt(1000000))
        );
        api.createUser(userData, null, null);
        
        Map<String, Object> updateData = Map.of(
            "name", "Updated Test User",
            "phone", "+9876543210"
        );
        Map<String, Object> result = api.updateUser("email", email, updateData, null);
        assertNotNull(result);
        assertEquals("ok", result.get("status"));
        
        // Verify the update
        Map<String, Object> updatedUser = api.getUser("email", email, null);
        assertEquals(updateData.get("name"), ((Map<String, Object>)updatedUser.get("profile")).get("name"));
        assertEquals(updateData.get("phone"), ((Map<String, Object>)updatedUser.get("profile")).get("phone"));
        System.out.println("Updated user with email: " + email);
        System.out.println("New name: " + updateData.get("name"));
        System.out.println("New phone: " + updateData.get("phone"));
    }

    @Test
    public void testGetSystemStats() throws IOException {
        if (!serverAvailable) {
            return;
        }

        Map<String, Object> result = api.getSystemStats(null);
        assertNotNull(result);
        assertEquals("ok", result.get("status"));
        assertNotNull(result.get("stats"));
    }
} 