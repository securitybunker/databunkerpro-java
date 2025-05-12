package org.databunker;

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

    // User Request Management
    public Map<String, Object> getUserRequest(String requestuuid, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("requestuuid", requestuuid);
        return makeRequest("UserRequestGet", data, requestMetadata);
    }

    public Map<String, Object> listUserRequests(String mode, String identity, int offset, int limit, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.put("offset", offset);
        data.put("limit", limit);
        return makeRequest("UserRequestListUserRequests", data, requestMetadata);
    }

    public Map<String, Object> cancelUserRequest(String requestuuid, String reason, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("requestuuid", requestuuid);
        data.put("reason", reason);
        return makeRequest("UserRequestCancel", data, requestMetadata);
    }

    public Map<String, Object> approveUserRequest(String requestuuid, String reason, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("requestuuid", requestuuid);
        data.put("reason", reason);
        return makeRequest("UserRequestApprove", data, requestMetadata);
    }

    // App Data Management
    public Map<String, Object> createAppData(String mode, String identity, String appname, Map<String, Object> data, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("mode", mode);
        requestData.put("identity", identity);
        requestData.put("appname", appname);
        requestData.put("data", data);
        return makeRequest("AppdataCreate", requestData, requestMetadata);
    }

    public Map<String, Object> getAppData(String mode, String identity, String appname, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.put("appname", appname);
        return makeRequest("AppdataGet", data, requestMetadata);
    }

    public Map<String, Object> updateAppData(String mode, String identity, String appname, Map<String, Object> data, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("mode", mode);
        requestData.put("identity", identity);
        requestData.put("appname", appname);
        requestData.put("data", data);
        return makeRequest("AppdataUpdate", requestData, requestMetadata);
    }

    public Map<String, Object> requestAppDataUpdate(String mode, String identity, String appname, Map<String, Object> data, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("mode", mode);
        requestData.put("identity", identity);
        requestData.put("appname", appname);
        requestData.put("data", data);
        return makeRequest("AppdataUpdateRequest", requestData, requestMetadata);
    }

    public Map<String, Object> listAppDataNames(String mode, String identity, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        return makeRequest("AppdataListUserAppNames", data, requestMetadata);
    }

    public Map<String, Object> listAppNames(Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("AppdataListAppNames", null, requestMetadata);
    }

    // Legal Basis Management
    public Map<String, Object> createLegalBasis(Map<String, Object> options, Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("LegalBasisCreate", options, requestMetadata);
    }

    public Map<String, Object> updateLegalBasis(Map<String, Object> options, Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("LegalBasisUpdate", options, requestMetadata);
    }

    public Map<String, Object> listAgreements(Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("LegalBasisListAgreements", null, requestMetadata);
    }

    // Agreement Management
    public Map<String, Object> acceptAgreement(String mode, String identity, Map<String, Object> options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.putAll(options);
        return makeRequest("AgreementAccept", data, requestMetadata);
    }

    public Map<String, Object> cancelAgreement(String mode, String identity, String brief, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.put("brief", brief);
        return makeRequest("AgreementCancel", data, requestMetadata);
    }

    public Map<String, Object> requestAgreementCancellation(String mode, String identity, String brief, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.put("brief", brief);
        return makeRequest("AgreementCancelRequest", data, requestMetadata);
    }

    public Map<String, Object> getUserAgreement(String mode, String identity, String brief, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.put("brief", brief);
        return makeRequest("AgreementGet", data, requestMetadata);
    }

    public Map<String, Object> listUserAgreements(String mode, String identity, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        return makeRequest("AgreementListUserAgreements", data, requestMetadata);
    }

    // Processing Activity Management
    public Map<String, Object> listProcessingActivities(Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("ProcessingActivityListActivities", null, requestMetadata);
    }

    // Connector Management
    public Map<String, Object> listSupportedConnectors(Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("ConnectorListSupportedConnectors", null, requestMetadata);
    }

    public Map<String, Object> listConnectors(int offset, int limit, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("offset", offset);
        data.put("limit", limit);
        return makeRequest("ConnectorListConnectors", data, requestMetadata);
    }

    public Map<String, Object> connectorGetUserData(String mode, String identity, String connectorid, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.put("connectorid", connectorid);
        return makeRequest("ConnectorGetUserData", data, requestMetadata);
    }

    public Map<String, Object> connectorGetUserExtraData(String mode, String identity, String connectorid, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.put("connectorid", connectorid);
        return makeRequest("ConnectorGetUserExtraData", data, requestMetadata);
    }

    public Map<String, Object> connectorDeleteUser(String mode, String identity, String connectorid, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.put("connectorid", connectorid);
        return makeRequest("ConnectorDeleteUser", data, requestMetadata);
    }

    // Group Management
    public Map<String, Object> createGroup(String groupname, String groupdesc, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("groupname", groupname);
        data.put("groupdesc", groupdesc);
        return makeRequest("GroupCreate", data, requestMetadata);
    }

    public Map<String, Object> getGroup(String groupid, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("groupid", groupid);
        return makeRequest("GroupGet", data, requestMetadata);
    }

    public Map<String, Object> listAllGroups(Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("GroupListAllGroups", null, requestMetadata);
    }

    public Map<String, Object> addUserToGroup(String mode, String identity, String groupname, String rolename, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.put("groupname", groupname);
        if (rolename != null) {
            data.put("rolename", rolename);
        }
        return makeRequest("GroupAddUser", data, requestMetadata);
    }

    // Role Management
    public Map<String, Object> createRole(String rolename, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("rolename", rolename);
        return makeRequest("RoleCreate", data, requestMetadata);
    }

    // Policy Management
    public Map<String, Object> createPolicy(Map<String, Object> data, Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("PolicyCreate", data, requestMetadata);
    }

    public Map<String, Object> getPolicy(String policyname, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        if (policyname != null) {
            if (policyname.matches("\\d+")) {
                data.put("policyid", policyname);
            } else {
                data.put("policyname", policyname);
            }
        }
        return makeRequest("PolicyGet", data, requestMetadata);
    }

    public Map<String, Object> listPolicies(Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("PolicyListAllPolicies", null, requestMetadata);
    }

    public Map<String, Object> linkPolicy(String rolename, String policyname, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("rolename", rolename);
        data.put("policyname", policyname);
        return makeRequest("PolicyLink", data, requestMetadata);
    }

    public Map<String, Object> updatePolicy(String policyid, Map<String, Object> data, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("policyid", policyid);
        requestData.putAll(data);
        return makeRequest("PolicyUpdate", requestData, requestMetadata);
    }

    // Shared Record Management
    public Map<String, Object> createSharedRecord(String mode, String identity, Map<String, Object> options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.putAll(options);
        return makeRequest("SharedRecordCreate", data, requestMetadata);
    }

    public Map<String, Object> getSharedRecord(String recorduuid, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("recorduuid", recorduuid);
        return makeRequest("SharedRecordGet", data, requestMetadata);
    }

    // Access Token Management
    public Map<String, Object> createXToken(String mode, String identity, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        if (requestMetadata != null) {
            data.put("request_metadata", requestMetadata);
        }
        return makeRequest("XTokenCreate", data, null);
    }

    // Token Management (for example for credit cards)
    /**
     * Creates a token for sensitive data like credit card numbers
     * @param tokentype Type of token (e.g., 'creditcard')
     * @param record The sensitive data to tokenize
     * @param options Optional parameters for token creation
     * @param requestMetadata Optional request metadata
     * @return The created token information
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> createToken(String tokentype, String record, Map<String, Object> options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("tokentype", tokentype);
        data.put("record", record);
        if (options != null) {
            data.putAll(options);
        }
        return makeRequest("TokenCreate", data, requestMetadata);
    }

    /**
     * Creates multiple tokens in bulk for sensitive data
     * @param records Array of records to tokenize, each containing tokentype and record
     * @param options Optional parameters for token creation
     * @param requestMetadata Optional request metadata
     * @return The created tokens information
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> createTokensBulk(Map<String, String>[] records, Map<String, Object> options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("records", records);
        if (options != null) {
            data.putAll(options);
        }
        return makeRequest("TokenCreateBulk", data, requestMetadata);
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

    // Bulk Operations
    /**
     * Initializes a bulk operation session
     * @param requestMetadata Optional request metadata
     * @return The unlock UUID for bulk operations
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> bulkListUnlock(Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("BulkListUnlock", null, requestMetadata);
    }

    /**
     * Lists multiple tokens in bulk
     * @param unlockuuid The unlock UUID from bulkListUnlock
     * @param tokens Array of tokens to list
     * @param requestMetadata Optional request metadata
     * @return The bulk token data
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> bulkListTokens(String unlockuuid, String[] tokens, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("unlockuuid", unlockuuid);
        data.put("tokens", tokens);
        return makeRequest("BulkListTokens", data, requestMetadata);
    }

    /**
     * Deletes multiple tokens in bulk
     * @param unlockuuid The unlock UUID from bulkListUnlock
     * @param tokens Array of tokens to delete
     * @param requestMetadata Optional request metadata
     * @return The bulk deletion result
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> bulkDeleteTokens(String unlockuuid, String[] tokens, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("unlockuuid", unlockuuid);
        data.put("tokens", tokens);
        return makeRequest("BulkDeleteTokens", data, requestMetadata);
    }

    /**
     * Lists multiple users in bulk
     * @param unlockuuid The unlock UUID from bulkListUnlock
     * @param offset The offset for pagination
     * @param limit The limit for pagination
     * @param requestMetadata Optional request metadata
     * @return The bulk user data
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> bulkListUsers(String unlockuuid, int offset, int limit, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("unlockuuid", unlockuuid);
        data.put("offset", offset);
        data.put("limit", limit);
        return makeRequest("BulkListUsers", data, requestMetadata);
    }

    /**
     * Lists multiple group users in bulk
     * @param unlockuuid The unlock UUID from bulkListUnlock
     * @param groupname The group name
     * @param offset The offset for pagination
     * @param limit The limit for pagination
     * @param requestMetadata Optional request metadata
     * @return The bulk group user data
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> bulkListGroupUsers(String unlockuuid, String groupname, int offset, int limit, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("unlockuuid", unlockuuid);
        data.put("groupname", groupname);
        data.put("offset", offset);
        data.put("limit", limit);
        return makeRequest("BulkListGroupUsers", data, requestMetadata);
    }

    /**
     * Lists multiple user requests in bulk
     * @param unlockuuid The unlock UUID from bulkListUnlock
     * @param offset The offset for pagination
     * @param limit The limit for pagination
     * @param requestMetadata Optional request metadata
     * @return The bulk user request data
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> bulkListUserRequests(String unlockuuid, int offset, int limit, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("unlockuuid", unlockuuid);
        data.put("offset", offset);
        data.put("limit", limit);
        return makeRequest("BulkListUserRequests", data, requestMetadata);
    }

    /**
     * Lists multiple audit events in bulk
     * @param unlockuuid The unlock UUID from bulkListUnlock
     * @param offset The offset for pagination
     * @param limit The limit for pagination
     * @param requestMetadata Optional request metadata
     * @return The bulk audit event data
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> bulkListAuditEvents(String unlockuuid, int offset, int limit, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("unlockuuid", unlockuuid);
        data.put("offset", offset);
        data.put("limit", limit);
        return makeRequest("BulkListAuditEvents", data, requestMetadata);
    }

    // UI and Configuration
    public Map<String, Object> getUIConf() throws IOException {
        return makeRequest("UIConfGet", null, null);
    }

    public Map<String, Object> getTenantConf() throws IOException {
        return makeRequest("TenantConfGet", null, null);
    }

    // User Reports
    public Map<String, Object> getUserHTMLReport(String mode, String identity, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        return makeRequest("UserHTMLReportGet", data, requestMetadata);
    }

    public Map<String, Object> getUserReport(String mode, String identity, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        return makeRequest("UserReportGet", data, requestMetadata);
    }

    // Session Management
    public Map<String, Object> upsertSession(String sessionuuid, Map<String, Object> data, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("sessionuuid", sessionuuid);
        requestData.putAll(data);
        return makeRequest("SessionUpsert", requestData, requestMetadata);
    }

    public Map<String, Object> deleteSession(String sessionuuid, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("sessionuuid", sessionuuid);
        return makeRequest("SessionDelete", data, requestMetadata);
    }

    public Map<String, Object> getSession(String sessionuuid, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("sessionuuid", sessionuuid);
        return makeRequest("SessionGet", data, requestMetadata);
    }

    // System Metrics
    public Map<String, Object> getSystemMetrics(Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("SystemGetMetrics", null, requestMetadata);
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