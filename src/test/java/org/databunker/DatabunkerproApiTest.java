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
import java.util.Map;
import java.util.Random;
import org.databunker.options.BasicOptions;
import org.databunker.options.SharedRecordOptions;

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

    @Test
    public void testGetUserHTMLReport() throws IOException {
        System.out.println("\nTesting user HTML report generation...");
        String email = "test" + random.nextInt(1000000) + "@example.com";
        Map<String, Object> userData = Map.of(
            "email", email,
            "name", "Test User " + random.nextInt(1000000),
            "phone", String.valueOf(random.nextInt(1000000))
        );
        api.createUser(userData, null, null);

        Map<String, Object> result = api.getUserHTMLReport("email", email, null);
        assertNotNull(result);
        assertEquals("ok", result.get("status"));
        assertNotNull(result.get("html"));
        System.out.println("Successfully generated HTML report for user: " + email);
    }

    // @Test
    // public void testGetUserReport() throws IOException {
    //     System.out.println("\nTesting user report generation...");
    //     String email = "test" + random.nextInt(1000000) + "@example.com";
    //     Map<String, Object> userData = Map.of(
    //         "email", email,
    //         "name", "Test User " + random.nextInt(1000000),
    //         "phone", String.valueOf(random.nextInt(1000000))
    //     );
    //     api.createUser(userData, null, null);

    //     Map<String, Object> result = api.getUserReport("email", email, null);
    //     System.out.println("Full Response: " + result);
    //     assertNotNull(result);
    //     assertEquals("ok", result.get("status"));
    //     assertNotNull(result.get("report"));
    //     System.out.println("Successfully generated report for user: " + email);
    // }

    @Test
    public void testSessionManagement() throws IOException {
        System.out.println("\nTesting session management...");
        String sessionuuid = java.util.UUID.randomUUID().toString();
        Map<String, Object> sessionData = Map.of(
            "userid", "test-user-" + random.nextInt(1000000),
            "ip", "127.0.0.1",
            "useragent", "Test Browser"
        );

        // Create/Update session with options
        BasicOptions options = new BasicOptions.Builder()
            .slidingtime("1h")
            .finaltime("24h")
            .build();
        Map<String, Object> upsertResult = api.upsertSession(sessionuuid, sessionData, options, null);
        assertNotNull(upsertResult);
        assertEquals("ok", upsertResult.get("status"));
        System.out.println("\nSession Upsert Result:");
        System.out.println("Status: " + upsertResult.get("status"));
        System.out.println("Session UUID: " + sessionuuid);
        System.out.println("Successfully created/updated session: " + sessionuuid);

        // Get session
        Map<String, Object> getResult = api.getSession(sessionuuid, null);
        assertNotNull(getResult);
        assertEquals("ok", getResult.get("status"));
        assertNotNull(getResult.get("sessiondata"));
        System.out.println("Successfully retrieved session: " + sessionuuid);

        // Delete session
        Map<String, Object> deleteResult = api.deleteSession(sessionuuid, null);
        assertNotNull(deleteResult);
        assertEquals("ok", deleteResult.get("status"));
        System.out.println("Successfully deleted session: " + sessionuuid);
    }

    @Test
    public void testGetSystemMetrics() throws IOException {
        System.out.println("\nTesting system metrics retrieval...");
        Map<String, Object> result = api.getSystemMetrics(null);
        assertNotNull(result);
        // Verify that we got some metrics
        assertFalse(result.isEmpty());
        // Check for some common metrics that should be present
        assertTrue(result.keySet().stream().anyMatch(key -> key.startsWith("http_requests_total")));
        System.out.println("Successfully retrieved system metrics");
        System.out.println("Metrics found: " + result.keySet());
    }

    @Test
    public void testParsePrometheusMetrics() throws IOException {
        System.out.println("\nTesting Prometheus metrics parsing...");
        String metricsText = "# HELP http_requests_total Total number of HTTP requests\n" +
                           "# TYPE http_requests_total counter\n" +
                           "http_requests_total{method=\"GET\",status=\"200\"} 100\n" +
                           "http_requests_total{method=\"POST\",status=\"200\"} 50\n" +
                           "# HELP cpu_usage CPU usage percentage\n" +
                           "# TYPE cpu_usage gauge\n" +
                           "cpu_usage 75.5";

        Map<String, Object> result = api.parsePrometheusMetrics(metricsText);
        assertNotNull(result);
        assertEquals(100.0, result.get("http_requests_total{method=\"GET\",status=\"200\"}"));
        assertEquals(50.0, result.get("http_requests_total{method=\"POST\",status=\"200\"}"));
        assertEquals(75.5, result.get("cpu_usage"));
        System.out.println("Successfully parsed Prometheus metrics");
    }

    @Test
    public void testSharedRecordManagement() throws IOException {
        System.out.println("\nTesting shared record management...");
        String email = "test" + random.nextInt(1000000) + "@example.com";
        Map<String, Object> userData = Map.of(
            "email", email,
            "name", "Test User " + random.nextInt(1000000),
            "phone", String.valueOf(random.nextInt(1000000))
        );
        api.createUser(userData, null, null);

        // Create shared record
        SharedRecordOptions options = new SharedRecordOptions.Builder()
            .fields("name,email")
            .partner("test-partner")
            .finaltime("1d")
            .build();
        Map<String, Object> createResult = api.createSharedRecord("email", email, options, null);
        assertNotNull(createResult);
        assertEquals("ok", createResult.get("status"));
        assertNotNull(createResult.get("recorduuid"));
        String recorduuid = (String) createResult.get("recorduuid");
        System.out.println("Successfully created shared record: " + recorduuid);

        // Get shared record
        Map<String, Object> getResult = api.getSharedRecord(recorduuid, null);
        assertNotNull(getResult);
        assertEquals("ok", getResult.get("status"));
        assertNotNull(getResult.get("data"));
        System.out.println("Successfully retrieved shared record: " + recorduuid);
    }
} 