package org.databunker;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.methods.HttpGet;

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
     * Makes a raw request to the DatabunkerPro API and returns the response as a byte array
     *
     * @param endpoint        The API endpoint
     * @param data           The request data
     * @param requestMetadata Additional request metadata
     * @return The raw response as a byte array
     * @throws IOException If an I/O error occurs
     */
    private byte[] rawRequest(String endpoint, Map<String, Object> data, Map<String, Object> requestMetadata) throws IOException {
        String url = baseURL + "/v2/" + endpoint;
        HttpPost request = new HttpPost(url);

        // Set headers
        request.setHeader("Content-Type", "application/json");
        if (xBunkerToken != null && !xBunkerToken.isEmpty()) {
            request.setHeader("X-Bunker-Token", xBunkerToken);
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
            return EntityUtils.toByteArray(entity);
        }
    }

    // User Management
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

    public Map<String, Object> getUser(String mode, String identity, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        return makeRequest("UserGet", data, requestMetadata);
    }

    public Map<String, Object> deleteUser(String mode, String identity, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        return makeRequest("UserDelete", data, requestMetadata);
    }

    public Map<String, Object> requestUserDeletion(String mode, String identity, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        return makeRequest("UserDeleteRequest", data, requestMetadata);
    }

    public Map<String, Object> updateUser(String mode, String identity, Map<String, Object> profile, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.put("profile", profile);
        return makeRequest("UserUpdate", data, requestMetadata);
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

    public Map<String, Object> createConnector(Map<String, Object> options, Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("ConnectorCreate", options, requestMetadata);
    }

    public Map<String, Object> updateConnector(Map<String, Object> options, Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("ConnectorUpdate", options, requestMetadata);
    }

    public Map<String, Object> validateConnectorConnectivity(Map<String, Object> options, Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("ConnectorValidateConnectivity", options, requestMetadata);
    }

    public Map<String, Object> deleteConnector(String connectorid, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("connectorid", connectorid);
        return makeRequest("ConnectorDelete", data, requestMetadata);
    }

    public Map<String, Object> getTableMetadata(Map<String, Object> options, Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("ConnectorGetTableMetaData", options, requestMetadata);
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
        if (groupname.matches("\\d+")) {
            data.put("groupid", groupname);
        } else {
            data.put("groupname", groupname);
        }
        if (rolename != null) {
            if (rolename.matches("\\d+")) {
                data.put("roleid", rolename);
            } else {
                data.put("rolename", rolename);
            }
        }
        return makeRequest("GroupAddUser", data, requestMetadata);
    }

    // Access Token Management
    public Map<String, Object> createXToken(String mode, String identity, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        return makeRequest("XTokenCreate", data, requestMetadata);
    }

    // Token Management
    public Map<String, Object> createToken(String tokentype, String record, Map<String, Object> options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("tokentype", tokentype);
        data.put("record", record);
        if (options != null) {
            data.putAll(options);
        }
        return makeRequest("TokenCreate", data, requestMetadata);
    }

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

    // Audit Management
    public Map<String, Object> listUserAuditEvents(String mode, String identity, int offset, int limit, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.put("offset", offset);
        data.put("limit", limit);
        return makeRequest("AuditListUserEvents", data, requestMetadata);
    }

    public Map<String, Object> getAuditEvent(String auditeventuuid, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("auditeventuuid", auditeventuuid);
        return makeRequest("AuditGetEvent", data, requestMetadata);
    }

    // Tenant Management
    public Map<String, Object> createTenant(Map<String, Object> data, Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("TenantCreate", data, requestMetadata);
    }

    public Map<String, Object> getTenant(String tenantid, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("tenantid", tenantid);
        return makeRequest("TenantGet", data, requestMetadata);
    }

    public Map<String, Object> updateTenant(String tenantid, String tenantname, String tenantorg, String email, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("tenantid", tenantid);
        data.put("tenantname", tenantname);
        data.put("tenantorg", tenantorg);
        data.put("email", email);
        return makeRequest("TenantUpdate", data, requestMetadata);
    }

    public Map<String, Object> deleteTenant(String tenantid, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("tenantid", tenantid);
        return makeRequest("TenantDelete", data, requestMetadata);
    }

    public Map<String, Object> listTenants(int offset, int limit, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("offset", offset);
        data.put("limit", limit);
        return makeRequest("TenantListTenants", data, requestMetadata);
    }

    // Role Management
    public Map<String, Object> createRole(String rolename, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("rolename", rolename);
        return makeRequest("RoleCreate", data, requestMetadata);
    }

    public Map<String, Object> linkPolicy(String rolename, String policyname, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("rolename", rolename);
        data.put("policyname", policyname);
        return makeRequest("RoleLinkPolicy", data, requestMetadata);
    }

    // Policy Management
    public Map<String, Object> createPolicy(Map<String, Object> data, Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("PolicyCreate", data, requestMetadata);
    }

    public Map<String, Object> updatePolicy(String policyid, Map<String, Object> data, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("policyid", policyid);
        requestData.putAll(data);
        return makeRequest("PolicyUpdate", requestData, requestMetadata);
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

    // Bulk Operations
    public Map<String, Object> bulkListUnlock(Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("BulkListUnlock", null, requestMetadata);
    }

    public Map<String, Object> bulkListUsers(String unlockuuid, int offset, int limit, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("unlockuuid", unlockuuid);
        data.put("offset", offset);
        data.put("limit", limit);
        return makeRequest("BulkListUsers", data, requestMetadata);
    }

    public Map<String, Object> bulkListGroupUsers(String unlockuuid, String groupname, int offset, int limit, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("unlockuuid", unlockuuid);
        if (groupname.matches("\\d+")) {
            data.put("groupid", groupname);
        } else {
            data.put("groupname", groupname);
        }
        data.put("offset", offset);
        data.put("limit", limit);
        return makeRequest("BulkListGroupUsers", data, requestMetadata);
    }

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

    public Map<String, Object> bulkListTokens(String unlockuuid, String[] tokens, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("unlockuuid", unlockuuid);
        data.put("tokens", tokens);
        return makeRequest("BulkListTokens", data, requestMetadata);
    }

    public Map<String, Object> bulkDeleteTokens(String unlockuuid, String[] tokens, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("unlockuuid", unlockuuid);
        data.put("tokens", tokens);
        return makeRequest("BulkDeleteTokens", data, requestMetadata);
    }

    // System Configuration
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

    public Map<String, Object> upsertSession(String sessionuuid, Map<String, Object> data, Map<String, Object> options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("sessionuuid", sessionuuid);
        requestData.put("sessiondata", data);
        if (options != null) {
            if (options.containsKey("slidingtime")) {
                requestData.put("slidingtime", options.get("slidingtime"));
            }
            if (options.containsKey("finaltime")) {
                requestData.put("finaltime", options.get("finaltime"));
            }
        }
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

    public Map<String, Object> getSystemStats(Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("SystemGetSystemStats", null, requestMetadata);
    }

    public Map<String, Object> getSystemMetrics(Map<String, Object> requestMetadata) throws IOException {
        String url = baseURL + "/metrics";
        HttpGet request = new HttpGet(url);

        // Set headers
        if (xBunkerToken != null && !xBunkerToken.isEmpty()) {
            request.setHeader("X-Bunker-Token", xBunkerToken);
        }
        if (xBunkerTenant != null && !xBunkerTenant.isEmpty()) {
            request.setHeader("X-Bunker-Tenant", xBunkerTenant);
        }

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            String metricsText = EntityUtils.toString(entity);
            return parsePrometheusMetrics(metricsText);
        }
    }

    public Map<String, Object> parsePrometheusMetrics(String metricsText) throws IOException {
        Map<String, Object> metrics = new HashMap<>();
        String[] lines = metricsText.split("\n");
        
        for (String line : lines) {
            // Skip comments and empty lines
            if (line.startsWith("#") || line.trim().isEmpty()) {
                continue;
            }
            
            // Parse metric line
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^([a-zA-Z0-9_]+)(?:\\{([^}]+)\\})?\\s+([0-9.]+)$");
            java.util.regex.Matcher matcher = pattern.matcher(line);
            
            if (matcher.find()) {
                String name = matcher.group(1);
                String labels = matcher.group(2);
                String value = matcher.group(3);
                
                String metricKey = labels != null ? name + "{" + labels + "}" : name;
                metrics.put(metricKey, Double.parseDouble(value));
            }
        }
        return metrics;
    }

    public Map<String, Object> createSharedRecord(String mode, String identity, Map<String, Object> options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        if (options != null) {
            data.putAll(options);
        }
        return makeRequest("SharedRecordCreate", data, requestMetadata);
    }

    public Map<String, Object> getSharedRecord(String recorduuid, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("recorduuid", recorduuid);
        return makeRequest("SharedRecordGet", data, requestMetadata);
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