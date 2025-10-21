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
import org.databunker.options.BasicOptions;
import org.databunker.options.UserOptions;
import org.databunker.options.SharedRecordOptions;
import org.databunker.options.LegalBasisOptions;

import org.databunker.options.AgreementAcceptOptions;
import org.databunker.options.ConnectorOptions;
import org.databunker.options.TenantOptions;
import org.databunker.options.ProcessingActivityOptions;

import org.databunker.options.GroupOptions;
import org.databunker.options.RoleOptions;
import org.databunker.options.TokenOptions;
import org.databunker.options.PolicyOptions;
import org.databunker.options.PatchOperation;
import org.databunker.options.OptionsConverter;

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
            Map<String, Object> result = objectMapper.readValue(responseString, Map.class);

            if (response.getStatusLine().getStatusCode() < 200 || response.getStatusLine().getStatusCode() >= 300) {
                if (result.containsKey("status")) {
                    return result;
                } else {
                    throw new IOException(result.containsKey("message") ? 
                        (String) result.get("message") : "API request failed");
                }
            }

            return result;
        } catch (Exception error) {
            System.err.println("Error making request: " + error.getMessage());
            throw new IOException("API request failed", error);
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
        } catch (Exception error) {
            System.err.println("Error making raw request: " + error.getMessage());
            throw new IOException("Raw API request failed", error);
        }
    }

    // User Management
    /**
     * Creates a user
     *
     * @param profile         User profile data
     * @param options         User options
     * @param requestMetadata Optional request metadata
     * @return The created user information
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> createUser(Map<String, Object> profile, UserOptions options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("profile", profile);
        if (options != null) {
            if (options.getGroupname() != null) {
                Object groupname = options.getGroupname();
                if (groupname instanceof Number) {
                    data.put("groupid", groupname);
                } else if (groupname instanceof String && ((String) groupname).matches("\\d+")) {
                    data.put("groupid", groupname);
                } else {
                    data.put("groupname", groupname);
                }
            } else if (options.getGroupid() != null) {
                data.put("groupid", options.getGroupid());
            }
            if (options.getRolename() != null) {
                Object rolename = options.getRolename();
                if (rolename instanceof Number) {
                    data.put("roleid", rolename);
                } else if (rolename instanceof String && ((String) rolename).matches("\\d+")) {
                    data.put("roleid", rolename);
                } else {
                    data.put("rolename", rolename);
                }
            } else if (options.getRoleid() != null) {
                data.put("roleid", options.getRoleid());
            }
            if (options.getSlidingtime() != null) {
                data.put("slidingtime", options.getSlidingtime());
            }
            if (options.getFinaltime() != null) {
                data.put("finaltime", options.getFinaltime());
            }
        }
        return makeRequest("UserCreate", data, requestMetadata);
    }

    /**
     * Creates multiple users in bulk
     *
     * @param records         Array of user records to create
     * @param options         Options for bulk user creation
     * @param requestMetadata Optional request metadata
     * @return The created users information
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> createUsersBulk(Map<String, Object>[] records, BasicOptions options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("records", records);
        if (options != null) {
            data.putAll(OptionsConverter.toMap(options));
        }
        return makeRequest("UserCreateBulk", data, requestMetadata);
    }

    public Map<String, Object> getUser(String mode, String identity, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        return makeRequest("UserGet", data, requestMetadata);
    }

    public Map<String, Object> updateUser(String mode, String identity, Map<String, Object> profile, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.put("profile", profile);
        return makeRequest("UserUpdate", data, requestMetadata);
    }

    public Map<String, Object> patchUser(String mode, String identity, Map<String, Object>[] patch, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.put("patch", patch);
        return makeRequest("UserPatch", data, requestMetadata);
    }

    /**
     * Patches a user with typed patch operations
     *
     * @param mode            User identification mode
     * @param identity        User's identifier
     * @param patch           Array of patch operations
     * @param requestMetadata Optional request metadata
     * @return The patched user information
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> patchUser(String mode, String identity, PatchOperation[] patch, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.put("patch", patch);
        return makeRequest("UserPatch", data, requestMetadata);
    }

    public Map<String, Object> requestUserUpdate(String mode, String identity, Map<String, Object> profile, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.put("profile", profile);
        return makeRequest("UserUpdateRequest", data, requestMetadata);
    }

    public Map<String, Object> requestUserPatch(String mode, String identity, Map<String, Object>[] patch, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.put("patch", patch);
        return makeRequest("UserPatchRequest", data, requestMetadata);
    }

    /**
     * Requests a user patch with typed patch operations
     *
     * @param mode            User identification mode
     * @param identity        User's identifier
     * @param patch           Array of patch operations
     * @param requestMetadata Optional request metadata
     * @return The patch request result
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> requestUserPatch(String mode, String identity, PatchOperation[] patch, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.put("patch", patch);
        return makeRequest("UserPatchRequest", data, requestMetadata);
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

    /**
     * Deletes multiple users in bulk
     *
     * @param users           Array of user identifiers to delete
     * @param requestMetadata Optional request metadata
     * @return The bulk deletion result
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> deleteUsersBulk(Map<String, Object>[] users, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("users", users);
        return makeRequest("UserDeleteBulk", data, requestMetadata);
    }

    /**
     * Searches for users using fuzzy matching
     *
     * @param mode            User identification mode
     * @param identity        User identifier to search for
     * @param unlockuuid      UUID from bulk list unlock for search authorization
     * @param requestMetadata Optional request metadata
     * @return The search results
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> searchUsers(String mode, String identity, String unlockuuid, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.put("unlockuuid", unlockuuid);
        return makeRequest("UserSearch", data, requestMetadata);
    }

    /**
     * Lists all versions of a user's profile
     *
     * @param mode            User identification mode
     * @param identity        User identifier
     * @param requestMetadata Optional request metadata
     * @return List of user versions
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> listUserVersions(String mode, String identity, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        return makeRequest("UserListVersions", data, requestMetadata);
    }

    // User Authentication
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

    public Map<String, Object> createCaptcha(Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("CaptchaCreate", null, requestMetadata);
    }

    // Create user API Access Token
    /**
     * Creates an access token for a user
     *
     * @param mode           User identification mode (e.g., 'email', 'phone', 'token')
     * @param identity       User's identifier corresponding to the mode
     * @param options        Optional parameters for token creation
     * @param requestMetadata Optional request metadata
     * @return The created token information
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> createUserXToken(String mode, String identity, BasicOptions options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        if (options != null) {
            data.putAll(OptionsConverter.toMap(options));
        }
        return makeRequest("XTokenCreateForUser", data, requestMetadata);
    }

    /**
     * Creates an access token for a role
     *
     * @param roleref        Role ID or name
     * @param options        Optional parameters for token creation
     * @param requestMetadata Optional request metadata
     * @return The created token information
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> createRoleXToken(String roleref, BasicOptions options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        if (options != null) {
            data.putAll(OptionsConverter.toMap(options));
        }
        if (roleref.matches("\\d+")) {
            data.put("roleid", roleref);
        } else {
            data.put("rolename", roleref);
        }
        return makeRequest("XTokenCreateForRole", data, requestMetadata);
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

    /**
     * Lists user requests with default offset and limit
     *
     * @param mode            User identification mode
     * @param identity        User's identifier
     * @param requestMetadata Optional request metadata
     * @return List of user requests
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> listUserRequests(String mode, String identity, Map<String, Object> requestMetadata) throws IOException {
        return listUserRequests(mode, identity, 0, 10, requestMetadata);
    }

    /**
     * Cancels a user request
     *
     * @param requestuuid     UUID of the request to cancel
     * @param options         Optional parameters for cancellation
     * @param requestMetadata Optional request metadata
     * @return The cancellation result
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> cancelUserRequest(String requestuuid, Map<String, Object> options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("requestuuid", requestuuid);
        if (options != null) {
            data.putAll(options);
        }
        return makeRequest("UserRequestCancel", data, requestMetadata);
    }

    /**
     * Cancels a user request without additional options
     *
     * @param requestuuid     UUID of the request to cancel
     * @param requestMetadata Optional request metadata
     * @return The cancellation result
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> cancelUserRequest(String requestuuid, Map<String, Object> requestMetadata) throws IOException {
        return cancelUserRequest(requestuuid, null, requestMetadata);
    }

    /**
     * Approves a user request
     *
     * @param requestuuid     UUID of the request to approve
     * @param options         Optional parameters for approval
     * @param requestMetadata Optional request metadata
     * @return The approval result
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> approveUserRequest(String requestuuid, Map<String, Object> options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("requestuuid", requestuuid);
        if (options != null) {
            data.putAll(options);
        }
        return makeRequest("UserRequestApprove", data, requestMetadata);
    }

    /**
     * Approves a user request without additional options
     *
     * @param requestuuid     UUID of the request to approve
     * @param requestMetadata Optional request metadata
     * @return The approval result
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> approveUserRequest(String requestuuid, Map<String, Object> requestMetadata) throws IOException {
        return approveUserRequest(requestuuid, null, requestMetadata);
    }

    // App Data Management
    public Map<String, Object> createAppData(String mode, String identity, String appname, Map<String, Object> appdata, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("mode", mode);
        requestData.put("identity", identity);
        requestData.put("appname", appname);
        requestData.put("appdata", appdata);
        return makeRequest("AppdataCreate", requestData, requestMetadata);
    }

    public Map<String, Object> getAppData(String mode, String identity, String appname, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.put("appname", appname);
        return makeRequest("AppdataGet", data, requestMetadata);
    }

    /**
     * Gets app data with version support
     *
     * @param mode            User identification mode
     * @param identity        User identifier
     * @param appname         Application name
     * @param version         Specific version of the app data to retrieve
     * @param requestMetadata Optional request metadata
     * @return App data
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> getAppData(String mode, String identity, String appname, Integer version, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.put("appname", appname);
        if (version != null) {
            data.put("version", version);
        }
        return makeRequest("AppdataGet", data, requestMetadata);
    }

    public Map<String, Object> updateAppData(String mode, String identity, String appname, Map<String, Object> appdata, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("mode", mode);
        requestData.put("identity", identity);
        requestData.put("appname", appname);
        requestData.put("appdata", appdata);
        return makeRequest("AppdataUpdate", requestData, requestMetadata);
    }

    public Map<String, Object> requestAppDataUpdate(String mode, String identity, String appname, Map<String, Object> appdata, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("mode", mode);
        requestData.put("identity", identity);
        requestData.put("appname", appname);
        requestData.put("appdata", appdata);
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

    /**
     * Deletes app data for a user
     *
     * @param mode            User identification mode
     * @param identity        User identifier
     * @param appname         Application name
     * @param requestMetadata Optional request metadata
     * @return Deletion result
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> deleteAppData(String mode, String identity, String appname, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.put("appname", appname);
        return makeRequest("AppdataDelete", data, requestMetadata);
    }

    /**
     * Lists all versions of app data for a user
     *
     * @param mode            User identification mode
     * @param identity        User identifier
     * @param appname         Application name
     * @param requestMetadata Optional request metadata
     * @return List of app data versions
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> listAppDataVersions(String mode, String identity, String appname, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.put("appname", appname);
        return makeRequest("AppdataListVersions", data, requestMetadata);
    }

    // Legal Basis Management
    /**
     * Creates a legal basis with typed options
     *
     * @param options         Typed options for legal basis creation
     * @param requestMetadata Optional request metadata
     * @return The created legal basis information
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> createLegalBasis(LegalBasisOptions options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        if (options != null) {
            data.putAll(OptionsConverter.toMap(options));
        }
        return makeRequest("LegalBasisCreate", data, requestMetadata);
    }

    /**
     * Updates a legal basis with typed options
     *
     * @param brief           Brief identifier of the legal basis
     * @param newbrief        New brief identifier for the legal basis
     * @param options         Typed options for legal basis update (can be null)
     * @param requestMetadata Optional request metadata
     * @return The updated legal basis information
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> updateLegalBasis(String brief, String newbrief, LegalBasisOptions options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("brief", brief);
        data.put("newbrief", newbrief);
        if (options != null) {
            data.putAll(OptionsConverter.toMap(options));
        }
        return makeRequest("LegalBasisUpdate", data, requestMetadata);
    }



    public Map<String, Object> deleteLegalBasis(String brief, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("brief", brief);
        return makeRequest("LegalBasisDelete", data, requestMetadata);
    }

    public Map<String, Object> listAgreements(Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("LegalBasisListAgreements", null, requestMetadata);
    }

    // Agreement Management
    /**
     * Accepts an agreement
     *
     * @param mode            User identification mode
     * @param identity        User's identifier
     * @param brief           Agreement brief identifier
     * @param options         Options for agreement acceptance
     * @param requestMetadata Optional request metadata
     * @return The agreement acceptance result
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> acceptAgreement(String mode, String identity, String brief, AgreementAcceptOptions options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.put("brief", brief);
        if (options != null) {
            data.putAll(OptionsConverter.toMap(options));
        }
        return makeRequest("AgreementAccept", data, requestMetadata);
    }

    /**
     * Accepts an agreement without additional options
     *
     * @param mode            User identification mode
     * @param identity        User's identifier
     * @param brief           Agreement brief identifier
     * @param requestMetadata Optional request metadata
     * @return The agreement acceptance result
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> acceptAgreement(String mode, String identity, String brief, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        data.put("brief", brief);
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

    public Map<String, Object> revokeAllAgreements(String brief, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("brief", brief);
        return makeRequest("AgreementRevokeAll", data, requestMetadata);
    }

    // Processing Activity Management
    public Map<String, Object> listProcessingActivities(Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("ProcessingActivityListActivities", null, requestMetadata);
    }

    /**
     * Creates a processing activity with typed options
     *
     * @param options         Typed options for processing activity creation
     * @param requestMetadata Optional request metadata
     * @return The created processing activity information
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> createProcessingActivity(ProcessingActivityOptions options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        if (options != null) {
            data.putAll(OptionsConverter.toMap(options));
        }
        return makeRequest("ProcessingActivityCreate", data, requestMetadata);
    }





    /**
     * Updates a processing activity with typed options
     *
     * @param activity        Activity identifier
     * @param newactivity     New activity identifier
     * @param options         Typed options for processing activity update
     * @param requestMetadata Optional request metadata
     * @return The updated processing activity information
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> updateProcessingActivity(String activity, String newactivity, ProcessingActivityOptions options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("activity", activity);
        data.put("newactivity", newactivity);
        if (options != null) {
            data.putAll(OptionsConverter.toMap(options));
        }
        return makeRequest("ProcessingActivityUpdate", data, requestMetadata);
    }



    public Map<String, Object> deleteProcessingActivity(String activity, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("activity", activity);
        return makeRequest("ProcessingActivityDelete", data, requestMetadata);
    }

    public Map<String, Object> linkProcessingActivityToLegalBasis(String activity, String brief, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("activity", activity);
        data.put("brief", brief);
        return makeRequest("ProcessingActivityLinkLegalBasis", data, requestMetadata);
    }

    public Map<String, Object> unlinkProcessingActivityFromLegalBasis(String activity, String brief, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("activity", activity);
        data.put("brief", brief);
        return makeRequest("ProcessingActivityUnlinkLegalBasis", data, requestMetadata);
    }

    // Connector Management
    public Map<String, Object> listSupportedConnectors(Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("ConnectorListSupportedConnectors", null, requestMetadata);
    }

    public Map<String, Object> listConnectors(Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("ConnectorListConnectors", null, requestMetadata);
    }

    /**
     * Creates a connector with typed options
     *
     * @param options         Typed options for connector creation
     * @param requestMetadata Optional request metadata
     * @return The created connector information
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> createConnector(ConnectorOptions options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        if (options != null) {
            data.putAll(OptionsConverter.toMap(options));
        }
        return makeRequest("ConnectorCreate", data, requestMetadata);
    }

    /**
     * Updates a connector with typed options
     *
     * @param connectorid     Connector ID
     * @param options         Typed options for connector update
     * @param requestMetadata Optional request metadata
     * @return The updated connector information
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> updateConnector(String connectorid, ConnectorOptions options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("connectorid", connectorid);
        if (options != null) {
            data.putAll(OptionsConverter.toMap(options));
        }
        return makeRequest("ConnectorUpdate", data, requestMetadata);
    }

    /**
     * Validates connector connectivity with typed options
     *
     * @param connectorref    Connector reference (ID or name)
     * @param options         Typed options for connector validation (can be null)
     * @param requestMetadata Optional request metadata
     * @return The validation result
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> validateConnectorConnectivity(String connectorref, ConnectorOptions options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        if (options != null) {
            data.putAll(OptionsConverter.toMap(options));
        }
        if (connectorref.matches("\\d+")) {
            data.put("connectorid", connectorref);
        } else {
            data.put("connectorname", connectorref);
        }
        return makeRequest("ConnectorValidateConnectivity", data, requestMetadata);
    }

    public Map<String, Object> deleteConnector(String connectorref, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        if (connectorref.matches("\\d+")) {
            data.put("connectorid", connectorref);
        } else {
            data.put("connectorname", connectorref);
        }
        return makeRequest("ConnectorDelete", data, requestMetadata);
    }

    /**
     * Gets table metadata with typed options
     *
     * @param connectorref    Connector reference (ID or name)
     * @param options         Typed options for getting table metadata
     * @param requestMetadata Optional request metadata
     * @return The table metadata
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> getTableMetadata(String connectorref, ConnectorOptions options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        if (options != null) {
            data.putAll(OptionsConverter.toMap(options));
        }
        if (connectorref.matches("\\d+")) {
            data.put("connectorid", connectorref);
        } else {
            data.put("connectorname", connectorref);
        }
        return makeRequest("ConnectorGetTableMetaData", data, requestMetadata);
    }

    public Map<String, Object> connectorGetUserData(String mode, String identity, String connectorref, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        if (connectorref.matches("\\d+")) {
            data.put("connectorid", connectorref);
        } else {
            data.put("connectorname", connectorref);
        }
        return makeRequest("ConnectorGetUserData", data, requestMetadata);
    }

    public Map<String, Object> connectorGetUserExtraData(String mode, String identity, String connectorref, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        if (connectorref.matches("\\d+")) {
            data.put("connectorid", connectorref);
        } else {
            data.put("connectorname", connectorref);
        }
        return makeRequest("ConnectorGetUserExtraData", data, requestMetadata);
    }

    public Map<String, Object> connectorDeleteUser(String mode, String identity, String connectorref, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        if (connectorref.matches("\\d+")) {
            data.put("connectorid", connectorref);
        } else {
            data.put("connectorname", connectorref);
        }
        return makeRequest("ConnectorDeleteUser", data, requestMetadata);
    }

    // Group Management
    /**
     * Creates a group with typed options
     *
     * @param options         Typed options for group creation
     * @param requestMetadata Optional request metadata
     * @return The created group information
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> createGroup(GroupOptions options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        if (options != null) {
            data.putAll(OptionsConverter.toMap(options));
        }
        return makeRequest("GroupCreate", data, requestMetadata);
    }

    public Map<String, Object> getGroup(String groupref, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        if (groupref.matches("\\d+")) {
            data.put("groupid", groupref);
        } else {
            data.put("groupname", groupref);
        }
        return makeRequest("GroupGet", data, requestMetadata);
    }

    public Map<String, Object> listAllGroups(Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("GroupListAllGroups", null, requestMetadata);
    }

    public Map<String, Object> listUserGroups(String mode, String identity, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        return makeRequest("GroupListUserGroups", data, requestMetadata);
    }

    /**
     * Updates a group with typed options
     *
     * @param groupid        Group ID
     * @param options        Typed options for group update (can be null)
     * @param requestMetadata Optional request metadata
     * @return The updated group information
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> updateGroup(String groupid, GroupOptions options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("groupid", groupid);
        if (options != null) {
            data.putAll(OptionsConverter.toMap(options));
        }
        return makeRequest("GroupUpdate", data, requestMetadata);
    }

    public Map<String, Object> deleteGroup(String groupref, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        if (groupref.matches("\\d+")) {
            data.put("groupid", groupref);
        } else {
            data.put("groupname", groupref);
        }
        return makeRequest("GroupDelete", data, requestMetadata);
    }

    public Map<String, Object> removeUserFromGroup(String mode, String identity, String groupref, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        if (groupref.matches("\\d+")) {
            data.put("groupid", groupref);
        } else {
            data.put("groupname", groupref);
        }
        return makeRequest("GroupDeleteUser", data, requestMetadata);
    }

    public Map<String, Object> addUserToGroup(String mode, String identity, String groupref, String roleref, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        if (groupref.matches("\\d+")) {
            data.put("groupid", groupref);
        } else {
            data.put("groupname", groupref);
        }
        if (roleref != null) {
            if (roleref.matches("\\d+")) {
                data.put("roleid", roleref);
            } else {
                data.put("rolename", roleref);
            }
        }
        return makeRequest("GroupAddUser", data, requestMetadata);
    }

    /**
     * Adds a user to a group without a specific role
     *
     * @param mode            User identification mode
     * @param identity        User's identifier
     * @param groupref        Group reference (ID or name)
     * @param requestMetadata Optional request metadata
     * @return The result of adding the user to the group
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> addUserToGroup(String mode, String identity, String groupref, Map<String, Object> requestMetadata) throws IOException {
        return addUserToGroup(mode, identity, groupref, null, requestMetadata);
    }

    // Token Management (for example for credit cards)
    /**
     * Creates a token with typed options
     *
     * @param tokentype       Type of token to create
     * @param record          The record to tokenize
     * @param options         Typed options for token creation
     * @param requestMetadata Optional request metadata
     * @return The created token information
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> createToken(String tokentype, String record, TokenOptions options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("tokentype", tokentype);
        data.put("record", record);
        if (options != null) {
            data.putAll(OptionsConverter.toMap(options));
        }
        return makeRequest("TokenCreate", data, requestMetadata);
    }

    /**
     * Creates a token without additional options
     *
     * @param tokentype       Type of token to create
     * @param record          The record to tokenize
     * @param requestMetadata Optional request metadata
     * @return The created token information
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> createToken(String tokentype, String record, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("tokentype", tokentype);
        data.put("record", record);
        return makeRequest("TokenCreate", data, requestMetadata);
    }

    /**
     * Creates tokens in bulk with typed options
     *
     * @param records         Array of records to tokenize
     * @param options         Typed options for token creation (can be null)
     * @param requestMetadata Optional request metadata
     * @return The created tokens information
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> createTokensBulk(Map<String, Object>[] records, TokenOptions options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("records", records);
        if (options != null) {
            data.putAll(OptionsConverter.toMap(options));
        }
        return makeRequest("TokenCreateBulk", data, requestMetadata);
    }

    /**
     * Creates tokens in bulk without additional options
     *
     * @param records         Array of records to tokenize
     * @param requestMetadata Optional request metadata
     * @return The created tokens information
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> createTokensBulk(Map<String, Object>[] records, Map<String, Object> requestMetadata) throws IOException {
        return createTokensBulk(records, null, requestMetadata);
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

    /**
     * Lists user audit events with default offset and limit
     *
     * @param mode            User identification mode
     * @param identity        User's identifier
     * @param requestMetadata Optional request metadata
     * @return List of user audit events
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> listUserAuditEvents(String mode, String identity, Map<String, Object> requestMetadata) throws IOException {
        return listUserAuditEvents(mode, identity, 0, 10, requestMetadata);
    }

    public Map<String, Object> getAuditEvent(String auditeventuuid, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("auditeventuuid", auditeventuuid);
        return makeRequest("AuditGetEvent", data, requestMetadata);
    }

    // Tenant Management
    /**
     * Creates a tenant with typed options
     *
     * @param options         Typed options for tenant creation
     * @param requestMetadata Optional request metadata
     * @return The created tenant information
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> createTenant(TenantOptions options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        if (options != null) {
            data.putAll(OptionsConverter.toMap(options));
        }
        return makeRequest("TenantCreate", data, requestMetadata);
    }

    public Map<String, Object> getTenant(String tenantid, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("tenantid", tenantid);
        return makeRequest("TenantGet", data, requestMetadata);
    }

    /**
     * Updates a tenant with typed options
     *
     * @param tenantid       Tenant ID
     * @param options        Typed options for tenant update (can be null)
     * @param requestMetadata Optional request metadata
     * @return The updated tenant information
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> updateTenant(String tenantid, TenantOptions options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("tenantid", tenantid);
        if (options != null) {
            data.putAll(OptionsConverter.toMap(options));
        }
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

    /**
     * Lists tenants with default offset and limit
     *
     * @param requestMetadata Optional request metadata
     * @return List of tenants
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> listTenants(Map<String, Object> requestMetadata) throws IOException {
        return listTenants(0, 10, requestMetadata);
    }

    // Role Management
    /**
     * Creates a role with typed options
     *
     * @param options         Typed options for role creation
     * @param requestMetadata Optional request metadata
     * @return The created role information
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> createRole(RoleOptions options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        if (options != null) {
            data.putAll(OptionsConverter.toMap(options));
        }
        return makeRequest("RoleCreate", data, requestMetadata);
    }

    /**
     * Updates a role with typed options
     *
     * @param roleid         Role ID
     * @param options        Typed options for role update (can be null)
     * @param requestMetadata Optional request metadata
     * @return The updated role information
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> updateRole(String roleid, RoleOptions options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("roleid", roleid);
        if (options != null) {
            data.putAll(OptionsConverter.toMap(options));
        }
        return makeRequest("RoleUpdate", data, requestMetadata);
    }

    public Map<String, Object> linkPolicy(String roleref, String policyref, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        if (roleref.matches("\\d+")) {
            data.put("roleid", roleref);
        } else {
            data.put("rolename", roleref);
        }
        if (policyref.matches("\\d+")) {
            data.put("policyid", policyref);
        } else {
            data.put("policyname", policyref);
        }
        return makeRequest("RoleLinkPolicy", data, requestMetadata);
    }

    // Policy Management
    /**
     * Creates a policy with typed options
     *
     * @param options         Typed options for policy creation
     * @param requestMetadata Optional request metadata
     * @return The created policy information
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> createPolicy(PolicyOptions options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        if (options != null) {
            data.putAll(OptionsConverter.toMap(options));
        }
        return makeRequest("PolicyCreate", data, requestMetadata);
    }

    /**
     * Updates a policy with typed options
     *
     * @param policyid       Policy ID
     * @param options        Typed options for policy update (can be null)
     * @param requestMetadata Optional request metadata
     * @return The updated policy information
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> updatePolicy(String policyid, PolicyOptions options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("policyid", policyid);
        if (options != null) {
            data.putAll(OptionsConverter.toMap(options));
        }
        return makeRequest("PolicyUpdate", data, requestMetadata);
    }



    public Map<String, Object> getPolicy(String policyref, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        if (policyref != null) {
            if (policyref.matches("\\d+")) {
                data.put("policyid", policyref);
            } else {
                data.put("policyname", policyref);
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

    /**
     * Lists specific users in bulk using search criteria
     *
     * @param unlockuuid      UUID from bulk list unlock
     * @param users           Array of user search criteria
     * @param offset          Offset for pagination
     * @param limit           Limit for pagination
     * @param requestMetadata Optional request metadata
     * @return List of users
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> bulkListUsers(String unlockuuid, Map<String, Object>[] users, int offset, int limit, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("unlockuuid", unlockuuid);
        data.put("users", users);
        data.put("offset", offset);
        data.put("limit", limit);
        return makeRequest("BulkListUsers", data, requestMetadata);
    }

    /**
     * Lists specific users in bulk with default offset and limit
     *
     * @param unlockuuid      UUID from bulk list unlock
     * @param users           Array of user search criteria
     * @param requestMetadata Optional request metadata
     * @return List of users
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> bulkListUsers(String unlockuuid, Map<String, Object>[] users, Map<String, Object> requestMetadata) throws IOException {
        return bulkListUsers(unlockuuid, users, 0, 10, requestMetadata);
    }

    /**
     * Lists all users in bulk
     *
     * @param unlockuuid      UUID from bulk list unlock
     * @param offset          Offset for pagination
     * @param limit           Limit for pagination
     * @param requestMetadata Optional request metadata
     * @return List of all users
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> bulkListAllUsers(String unlockuuid, int offset, int limit, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("unlockuuid", unlockuuid);
        data.put("offset", offset);
        data.put("limit", limit);
        return makeRequest("BulkListAllUsers", data, requestMetadata);
    }

    /**
     * Lists all users in bulk with default offset and limit
     *
     * @param unlockuuid      UUID from bulk list unlock
     * @param requestMetadata Optional request metadata
     * @return List of all users
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> bulkListAllUsers(String unlockuuid, Map<String, Object> requestMetadata) throws IOException {
        return bulkListAllUsers(unlockuuid, 0, 10, requestMetadata);
    }

    public Map<String, Object> bulkListGroupUsers(String unlockuuid, String groupref, int offset, int limit, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("unlockuuid", unlockuuid);
        if (groupref.matches("\\d+")) {
            data.put("groupid", groupref);
        } else {
            data.put("groupname", groupref);
        }
        data.put("offset", offset);
        data.put("limit", limit);
        return makeRequest("BulkListGroupUsers", data, requestMetadata);
    }

    /**
     * Lists group users in bulk with default offset and limit
     *
     * @param unlockuuid      UUID for the bulk unlock operation
     * @param groupref        Group reference (ID or name)
     * @param requestMetadata Optional request metadata
     * @return List of group users
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> bulkListGroupUsers(String unlockuuid, String groupref, Map<String, Object> requestMetadata) throws IOException {
        return bulkListGroupUsers(unlockuuid, groupref, 0, 10, requestMetadata);
    }

    /**
     * Lists all user requests in bulk
     *
     * @param unlockuuid      UUID from bulk list unlock
     * @param offset          Offset for pagination
     * @param limit           Limit for pagination
     * @param requestMetadata Optional request metadata
     * @return List of user requests
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> bulkListAllUserRequests(String unlockuuid, int offset, int limit, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("unlockuuid", unlockuuid);
        data.put("offset", offset);
        data.put("limit", limit);
        return makeRequest("BulkListAllUserRequests", data, requestMetadata);
    }

    /**
     * Lists all user requests in bulk with default offset and limit
     *
     * @param unlockuuid      UUID from bulk list unlock
     * @param requestMetadata Optional request metadata
     * @return List of user requests
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> bulkListAllUserRequests(String unlockuuid, Map<String, Object> requestMetadata) throws IOException {
        return bulkListAllUserRequests(unlockuuid, 0, 10, requestMetadata);
    }

    /**
     * Lists all audit events in bulk
     *
     * @param unlockuuid      UUID from bulk list unlock
     * @param offset          Offset for pagination
     * @param limit           Limit for pagination
     * @param requestMetadata Optional request metadata
     * @return List of audit events
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> bulkListAllAuditEvents(String unlockuuid, int offset, int limit, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("unlockuuid", unlockuuid);
        data.put("offset", offset);
        data.put("limit", limit);
        return makeRequest("BulkListAllAuditEvents", data, requestMetadata);
    }

    /**
     * Lists all audit events in bulk with default offset and limit
     *
     * @param unlockuuid      UUID from bulk list unlock
     * @param requestMetadata Optional request metadata
     * @return List of audit events
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> bulkListAllAuditEvents(String unlockuuid, Map<String, Object> requestMetadata) throws IOException {
        return bulkListAllAuditEvents(unlockuuid, 0, 10, requestMetadata);
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


    // Session Management
    /**
     * Upserts a session with typed options
     *
     * @param sessionuuid    Session UUID
     * @param sessiondata    Session data
     * @param options        Typed options for session upsert (can be null)
     * @param requestMetadata Optional request metadata
     * @return The upserted session
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> upsertSession(String sessionuuid, Map<String, Object> sessiondata, BasicOptions options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("sessionuuid", sessionuuid);
        requestData.put("sessiondata", sessiondata);
        if (options != null) {
            requestData.putAll(OptionsConverter.toMap(options));
        }
        return makeRequest("SessionUpsert", requestData, requestMetadata);
    }

    /**
     * Upserts a session without additional options
     *
     * @param sessionuuid    Session UUID
     * @param sessiondata    Session data
     * @param requestMetadata Optional request metadata
     * @return The upserted session
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> upsertSession(String sessionuuid, Map<String, Object> sessiondata, Map<String, Object> requestMetadata) throws IOException {
        return upsertSession(sessionuuid, sessiondata, null, requestMetadata);
    }

    public Map<String, Object> deleteSession(String sessionuuid, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("sessionuuid", sessionuuid);
        return makeRequest("SessionDelete", data, requestMetadata);
    }

    public Map<String, Object> listUserSessions(String mode, String identity, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        return makeRequest("SessionListUserSessions", data, requestMetadata);
    }

    public Map<String, Object> getSession(String sessionuuid, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("sessionuuid", sessionuuid);
        return makeRequest("SessionGet", data, requestMetadata);
    }

    public Map<String, Object> getSystemStats(Map<String, Object> requestMetadata) throws IOException {
        return makeRequest("SystemGetSystemStats", null, requestMetadata);
    }

    /**
     * Gets user report
     *
     * @param mode            User identification mode
     * @param identity        User identifier
     * @param requestMetadata Optional request metadata
     * @return User report
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> getUserReport(String mode, String identity, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        return makeRequest("SystemGetUserReport", data, requestMetadata);
    }

    /**
     * Gets user HTML report
     *
     * @param mode            User identification mode
     * @param identity        User identifier
     * @param requestMetadata Optional request metadata
     * @return User HTML report
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> getUserHTMLReport(String mode, String identity, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        return makeRequest("SystemGetUserHTMLReport", data, requestMetadata);
    }

    public Map<String, Object> setLicenseKey(String licensekey, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("licensekey", licensekey);
        return makeRequest("SystemSetLicenseKey", data, requestMetadata);
    }

    /**
     * Generates a wrapping key from three Shamir's Secret Sharing keys
     *
     * @param key1            First Shamir secret sharing key
     * @param key2            Second Shamir secret sharing key
     * @param key3            Third Shamir secret sharing key
     * @param requestMetadata Optional request metadata
     * @return Generated wrapping key
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> generateWrappingKey(String key1, String key2, String key3, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("key1", key1);
        data.put("key2", key2);
        data.put("key3", key3);
        return makeRequest("SystemGenerateWrappingKey", data, requestMetadata);
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

    /**
     * Creates a shared record with typed options
     *
     * @param mode            User identification mode
     * @param identity        User's identifier
     * @param options         Typed options for shared record creation (can be null)
     * @param requestMetadata Optional request metadata
     * @return The created shared record
     * @throws IOException If an I/O error occurs
     */
    public Map<String, Object> createSharedRecord(String mode, String identity, SharedRecordOptions options, Map<String, Object> requestMetadata) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("mode", mode);
        data.put("identity", identity);
        if (options != null) {
            data.putAll(OptionsConverter.toMap(options));
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