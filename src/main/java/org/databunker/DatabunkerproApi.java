package org.databunkerpro;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Java client for DatabunkerPro API
 */
public class DatabunkerproApi implements AutoCloseable {
    private final String baseURL;
    private final String xBunkerToken;
    private final String xBunkerTenant;
    private final ObjectMapper objectMapper;
    private final CloseableHttpClient httpClient;

    /**
     * Creates a new instance of DatabunkerproApi
     *
     * @param baseURL       The base URL of the DatabunkerPro API
     * @param xBunkerToken  The X-Bunker-Token for authentication
     * @param xBunkerTenant The X-Bunker-Tenant for multi-tenancy
     */
    public DatabunkerproApi(String baseURL, String xBunkerToken, String xBunkerTenant) {
        this.baseURL = baseURL;
        this.xBunkerToken = xBunkerToken;
        this.xBunkerTenant = xBunkerTenant;
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClients.createDefault();
    }

    /**
     * Makes a POST request to the DatabunkerPro API
     *
     * @param endpoint        The API endpoint
     * @param data           The request data
     * @param requestMetadata Additional request metadata
     * @return The response from the API
     * @throws IOException If an I/O error occurs
     */
    private Map<String, Object> makeRequest(String endpoint, Map<String, Object> data, Map<String, Object> requestMetadata) throws IOException {
        String url = baseURL + "/v2/" + endpoint;
        HttpPost request = new HttpPost(url);

        // Set headers
        request.setHeader("Content-Type", "application/json");
        if (xBunkerToken != null && !xBunkerToken.isEmpty()) {
            request.setHeader("X-Bunker-Token", xBunkerToken);
        }
        if (xBunkerTenant != null && !xBunkerTenant.isEmpty()) {
            request.setHeader("X-Bunker-Tenant", xBunkerTenant);
        }

        // Set request body if needed
        if (data != null || requestMetadata != null) {
            Map<String, Object> bodyData = data != null ? data : new HashMap<>();
            if (requestMetadata != null) {
                bodyData.put("request_metadata", requestMetadata);
            }
            StringEntity entity = new StringEntity(objectMapper.writeValueAsString(bodyData));
            request.setEntity(entity);
        }

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity);
            return objectMapper.readValue(responseString, Map.class);
        }
    }

    /**
     * Creates a new user
     *
     * @param profile         User profile data
     * @param options         Additional options
     * @param requestMetadata Additional request metadata
     * @return The API response
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> createUser(Map<String, Object> profile, Map<String, Object> options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("profile", profile);

        if (options != null) {
            if (options.containsKey("groupname")) {
                Object groupname = options.get("groupname");
                if (groupname instanceof Number) {
                    data.put("groupid", groupname);
                } else {
                    data.put("groupname", groupname);
                }
            } else if (options.containsKey("groupid")) {
                data.put("groupid", options.get("groupid"));
            }

            if (options.containsKey("rolename")) {
                Object rolename = options.get("rolename");
                if (rolename instanceof Number) {
                    data.put("roleid", rolename);
                } else {
                    data.put("rolename", rolename);
                }
            } else if (options.containsKey("roleid")) {
                data.put("roleid", options.get("roleid"));
            }

            if (options.containsKey("slidingtime")) {
                data.put("slidingtime", options.get("slidingtime"));
            }
            if (options.containsKey("finaltime")) {
                data.put("finaltime", options.get("finaltime"));
            }
        }

        return makeRequest("UserCreate", data, requestMetadata);
    }

    /**
     * Gets a user by mode and identity
     *
     * @param mode            The mode (e.g., "email", "phone")
     * @param identity        The identity value
     * @param requestMetadata Additional request metadata
     * @return The API response
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> getUser(String mode, String identity, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        return makeRequest("UserGet", data, requestMetadata);
    }

    /**
     * Updates a user's profile
     *
     * @param mode            The mode (e.g., "email", "phone")
     * @param identity        The identity value
     * @param profile         The updated profile data
     * @param requestMetadata Additional request metadata
     * @return The API response
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> updateUser(String mode, String identity, Map<String, Object> profile, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.put("profile", profile);
        return makeRequest("UserUpdate", data, requestMetadata);
    }

    /**
     * Deletes a user
     *
     * @param mode            The mode (e.g., "email", "phone")
     * @param identity        The identity value
     * @param requestMetadata Additional request metadata
     * @return The API response
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> deleteUser(String mode, String identity, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        return makeRequest("UserDelete", data, requestMetadata);
    }

    /**
     * Gets system statistics
     *
     * @param requestMetadata Additional request metadata
     * @return The API response
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> getSystemStats(Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("SystemGetSystemStats", null, requestMetadata);
    }

    // User Management - Additional methods
    public Map<String, Object> requestUserDeletion(String mode, String identity, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        return makeRequest("UserDeleteRequest", data, requestMetadata);
    }

    public Map<String, Object> requestUserUpdate(String mode, String identity, Map<String, Object> profile, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.put("profile", profile);
        return makeRequest("UserUpdateRequest", data, requestMetadata);
    }

    public Map<String, Object> preloginUser(String mode, String identity, String code, String captchacode, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.put("code", code);
        data.put("captchacode", captchacode);
        return makeRequest("UserPrelogin", data, requestMetadata);
    }

    public Map<String, Object> loginUser(String mode, String identity, String smscode, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.put("smscode", smscode);
        return makeRequest("UserLogin", data, requestMetadata);
    }

    // App Data Management - Additional methods
    public Map<String, Object> requestAppDataUpdate(String mode, String identity, String appname, Map<String, Object> data, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("mode", mode);
        requestData.put("identity", identity);
        requestData.put("appname", appname);
        requestData.put("data", data);
        return makeRequest("AppdataUpdateRequest", requestData, requestMetadata);
    }

    // Agreement Management - Additional methods
    public Map<String, Object> requestAgreementCancellation(String mode, String identity, String brief, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.put("brief", brief);
        return makeRequest("AgreementCancelRequest", data, requestMetadata);
    }

    // Role Management - Additional methods
    public Map<String, Object> linkPolicy(String rolename, String policyname, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("rolename", rolename);
        data.put("policyname", policyname);
        return makeRequest("RoleLinkPolicy", data, requestMetadata);
    }

    // Policy Management - Additional methods
    public Map<String, Object> updatePolicy(String policyid, Map<String, Object> data, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> requestData = new HashMap<>(data);
        requestData.put("policyid", policyid);
        return makeRequest("PolicyUpdate", requestData, requestMetadata);
    }

    // Bulk Operations - Additional methods
    public Map<String, Object> bulkListUserRequests(String unlockuuid, int offset, int limit, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("unlockuuid", unlockuuid);
        data.put("offset", offset);
        data.put("limit", limit);
        return makeRequest("BulkListUserRequests", data, requestMetadata);
    }

    public Map<String, Object> bulkListAuditEvents(String unlockuuid, int offset, int limit, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("unlockuuid", unlockuuid);
        data.put("offset", offset);
        data.put("limit", limit);
        return makeRequest("BulkListAuditEvents", data, requestMetadata);
    }

    // System Configuration - Additional methods
    public Map<String, Object> getUIConf() throws IOException {
        return makeRequest("TenantGetUIConf", null, null);
    }

    public Map<String, Object> getTenantConf() throws IOException {
        return makeRequest("TenantGetConf", null, null);
    }

    public Map<String, Object> getUserHTMLReport(String mode, String identity, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        return makeRequest("SystemGetUserHTMLReport", data, requestMetadata);
    }

    public Map<String, Object> getUserReport(String mode, String identity, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        return makeRequest("SystemGetUserReport", data, requestMetadata);
    }

    // Session Management - Additional methods
    public Map<String, Object> upsertSession(String sessionuuid, Map<String, Object> data, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> requestData = new HashMap<>(data);
        requestData.put("sessionuuid", sessionuuid);
        return makeRequest("SessionUpsert", requestData, requestMetadata);
    }

    public Map<String, Object> getSession(String sessionuuid, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("sessionuuid", sessionuuid);
        return makeRequest("SessionGet", data, requestMetadata);
    }

    public Map<String, Object> deleteSession(String sessionuuid, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("sessionuuid", sessionuuid);
        return makeRequest("SessionDelete", data, requestMetadata);
    }

    // Processing Activity Management - Additional methods
    public Map<String, Object> createProcessingActivity(Map<String, Object> data, Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("ProcessingActivityCreate", data, requestMetadata);
    }

    public Map<String, Object> getProcessingActivity(String activityid, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("activityid", activityid);
        return makeRequest("ProcessingActivityGet", data, requestMetadata);
    }

    public Map<String, Object> updateProcessingActivity(String activityid, Map<String, Object> data, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> requestData = new HashMap<>(data);
        requestData.put("activityid", activityid);
        return makeRequest("ProcessingActivityUpdate", requestData, requestMetadata);
    }

    public Map<String, Object> deleteProcessingActivity(String activityid, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("activityid", activityid);
        return makeRequest("ProcessingActivityDelete", data, requestMetadata);
    }

    public Map<String, Object> listProcessingActivities(int offset, int limit, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("offset", offset);
        data.put("limit", limit);
        return makeRequest("ProcessingActivityList", data, requestMetadata);
    }

    // Connector Management - Additional methods
    public Map<String, Object> getConnector(String connectorid, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("connectorid", connectorid);
        return makeRequest("ConnectorGet", data, requestMetadata);
    }

    public Map<String, Object> updateConnector(String connectorid, Map<String, Object> data, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> requestData = new HashMap<>(data);
        requestData.put("connectorid", connectorid);
        return makeRequest("ConnectorUpdate", requestData, requestMetadata);
    }

    public Map<String, Object> deleteConnector(String connectorid, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("connectorid", connectorid);
        return makeRequest("ConnectorDelete", data, requestMetadata);
    }

    public Map<String, Object> getTableMetadata(Map<String, Object> connectorConfig, Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("ConnectorGetTableMetadata", connectorConfig, requestMetadata);
    }

    public Map<String, Object> validateConnectorConnectivity(Map<String, Object> connectorConfig, Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("ConnectorValidateConnectivity", connectorConfig, requestMetadata);
    }

    // Group Management - Additional methods
    public Map<String, Object> createGroup(Map<String, Object> data, Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("GroupCreate", data, requestMetadata);
    }

    public Map<String, Object> getGroup(String groupid, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("groupid", groupid);
        return makeRequest("GroupGet", data, requestMetadata);
    }

    public Map<String, Object> updateGroup(String groupid, Map<String, Object> data, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> requestData = new HashMap<>(data);
        requestData.put("groupid", groupid);
        return makeRequest("GroupUpdate", requestData, requestMetadata);
    }

    public Map<String, Object> deleteGroup(String groupid, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("groupid", groupid);
        return makeRequest("GroupDelete", data, requestMetadata);
    }

    public Map<String, Object> listGroups(int offset, int limit, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("offset", offset);
        data.put("limit", limit);
        return makeRequest("GroupList", data, requestMetadata);
    }

    // Add real API calls that exist in PHP implementation
    public Map<String, Object> createToken(String field, Map<String, Object> record, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("field", field);
        data.put("record", record);
        return makeRequest("TokenCreate", data, requestMetadata);
    }

    public Map<String, Object> getToken(String token, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        return makeRequest("TokenGet", data, requestMetadata);
    }

    public Map<String, Object> deleteToken(String token, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        return makeRequest("TokenDelete", data, requestMetadata);
    }

    public Map<String, Object> createTokensBulk(Map<String, Object>[] records, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("records", records);
        return makeRequest("TokenCreateBulk", data, requestMetadata);
    }

    public Map<String, Object> listTokensBulk(String[] tokens, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("tokens", tokens);
        return makeRequest("TokenListBulk", data, requestMetadata);
    }

    public Map<String, Object> deleteTokensBulk(String[] tokens, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("tokens", tokens);
        return makeRequest("TokenDeleteBulk", data, requestMetadata);
    }

    public Map<String, Object> createXToken(String field, Map<String, Object> record, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("field", field);
        data.put("record", record);
        return makeRequest("XTokenCreate", data, requestMetadata);
    }

    /**
     * Closes the HTTP client
     *
     * @throws IOException If an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        httpClient.close();
    }
} 