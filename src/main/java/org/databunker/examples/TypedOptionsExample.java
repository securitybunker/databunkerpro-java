package org.databunker.examples;

import org.databunker.DatabunkerproApi;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Example demonstrating the use of typed options in the DatabunkerPro Java client
 */
public class TypedOptionsExample {
    
    public static void main(String[] args) {
        // Initialize the API client
        String apiUrl = "https://pro.databunker.org";
        String apiToken = "your-api-token";
        String tenantName = "your-tenant-name";
        
        try (DatabunkerproApi api = new DatabunkerproApi(apiUrl, apiToken, tenantName)) {
            
            // Example 1: Create a user with typed UserOptions
            Map<String, Object> profile = new HashMap<>();
            profile.put("email", "user@example.com");
            profile.put("name", "John Doe");
            profile.put("phone", "+1234567890");
            
            UserOptions userOptions = UserOptions.builder()
                .groupname("premium")
                .rolename("user")
                .finaltime("100d")
                .slidingtime("30d")
                .build();
            
            Map<String, Object> result = api.createUser(profile, userOptions, null);
            System.out.println("Created user with token: " + result.get("token"));
            
            // Example 2: Create an access token with typed BasicOptions
            BasicOptions tokenOptions = BasicOptions.builder()
                .finaltime("24h")
                .slidingtime("1h")
                .build();
            
            Map<String, Object> tokenResult = api.createUserXToken("email", "user@example.com", tokenOptions, null);
            System.out.println("Created access token: " + tokenResult.get("token"));
            
            // Example 3: Create a shared record with typed SharedRecordOptions
            SharedRecordOptions sharedOptions = SharedRecordOptions.builder()
                .fields("name,email")
                .partner("partner-org")
                .appname("myapp")
                .finaltime("7d")
                .build();
            
            Map<String, Object> sharedResult = api.createSharedRecord("email", "user@example.com", sharedOptions, null);
            System.out.println("Created shared record: " + sharedResult.get("recorduuid"));
            
            // Example 4: Create a role access token with typed BasicOptions
            BasicOptions roleTokenOptions = BasicOptions.builder()
                .finaltime("7d")
                .slidingtime("1d")
                .build();
            
            Map<String, Object> roleTokenResult = api.createRoleXToken("admin", roleTokenOptions, null);
            System.out.println("Created role access token: " + roleTokenResult.get("token"));
            
            // Example 5: Create legal basis with typed LegalBasisOptions
            LegalBasisOptions legalBasisOptions = LegalBasisOptions.builder()
                .brief("marketing-consent")
                .status("active")
                .module("marketing")
                .fulldesc("Consent for marketing communications")
                .shortdesc("Marketing consent")
                .basistype("consent")
                .requiredmsg("You must accept marketing communications")
                .requiredflag(true)
                .build();
            
            Map<String, Object> legalBasis = api.createLegalBasis(legalBasisOptions, null);
            System.out.println("Created legal basis: " + legalBasis.get("brief"));
            
            // Example 6: Accept agreement with typed AgreementAcceptOptions
            AgreementAcceptOptions agreementOptions = AgreementAcceptOptions.builder()
                .agreementmethod("web-form")
                .referencecode("REF123")
                .starttime("10d")
                .finaltime("100d")
                .status("active")
                .lastmodifiedby("admin@company.com")
                .build();
            
            Map<String, Object> agreement = api.acceptAgreement("email", "user@example.com", "marketing-consent", agreementOptions, null);
            System.out.println("Accepted agreement: " + agreement.get("status"));
            
            // Example 7: Create connector with typed ConnectorOptions
            ConnectorOptions connectorOptions = ConnectorOptions.builder()
                .connectorname("my-connector")
                .connectortype("mysql")
                .apikey("api-key-123")
                .username("dbuser")
                .connectordesc("MySQL database connector")
                .dbhost("localhost")
                .dbport(3306)
                .dbname("mydb")
                .tablename("users")
                .status("active")
                .build();
            
            Map<String, Object> connector = api.createConnector(connectorOptions, null);
            System.out.println("Created connector: " + connector.get("connectorid"));
            
            // Example 8: Create processing activity with typed ProcessingActivityOptions
            ProcessingActivityOptions activityOptions = ProcessingActivityOptions.builder()
                .activity("data-processing")
                .title("Data Processing Activity")
                .script("process_data.js")
                .fulldesc("Process user data for analytics")
                .applicableto("users")
                .build();
            
            Map<String, Object> activity = api.createProcessingActivity(activityOptions, null);
            System.out.println("Created processing activity: " + activity.get("activity"));
            
            // Example 9: Create group with typed GroupOptions
            GroupOptions groupOptions = GroupOptions.builder()
                .groupname("premium-users")
                .grouptype("user-group")
                .groupdesc("Premium user group")
                .build();
            
            Map<String, Object> group = api.createGroup(groupOptions, null);
            System.out.println("Created group: " + group.get("groupid"));
            
            // Example 10: Create role with typed RoleOptions
            RoleOptions roleOptions = RoleOptions.builder()
                .rolename("admin")
                .roledesc("Administrator role")
                .build();
            
            Map<String, Object> role = api.createRole(roleOptions, null);
            System.out.println("Created role: " + role.get("roleid"));
            
            // Example 11: Create token with typed TokenOptions
            TokenOptions tokenCreateOptions = TokenOptions.builder()
                .unique(true)
                .slidingtime("1h")
                .finaltime("24h")
                .build();
            
            Map<String, Object> token = api.createToken("credit-card", "4111111111111111", tokenCreateOptions, null);
            System.out.println("Created token: " + token.get("token"));
            
            // Example 12: Create policy with typed PolicyOptions
            Map<String, Object> policyData = new HashMap<>();
            policyData.put("retention_days", 365);
            policyData.put("auto_delete", true);
            
            PolicyOptions policyOptions = PolicyOptions.builder()
                .policyname("data-retention")
                .policydesc("Data retention policy")
                .policy(policyData)
                .build();
            
            Map<String, Object> policy = api.createPolicy(policyOptions, null);
            System.out.println("Created policy: " + policy.get("policyid"));
            
            // Example 13: Create tenant with typed TenantOptions
            TenantOptions tenantOptions = TenantOptions.builder()
                .tenantname("my-tenant")
                .tenantorg("My Organization")
                .email("admin@myorg.com")
                .build();
            
            Map<String, Object> tenant = api.createTenant(tenantOptions, null);
            System.out.println("Created tenant: " + tenant.get("tenantid"));
            
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
} 