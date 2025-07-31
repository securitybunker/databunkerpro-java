# Typed Options for DatabunkerPro Java Client

This package provides typed option classes that correspond to the TypeScript interfaces in the JavaScript/TypeScript client. These classes provide better type safety and IDE support compared to using `Map<String, Object>`.

## Available Option Classes

### BasicOptions
Used for time-based operations with `finaltime` and `slidingtime` parameters.

```java
BasicOptions options = BasicOptions.builder()
    .finaltime("100d")
    .slidingtime("30d")
    .build();
```

### UserOptions
Used for user creation and management with group, role, and time parameters.

```java
UserOptions options = UserOptions.builder()
    .groupname("premium")
    .groupid(123)  // Alternative to groupname
    .rolename("user")
    .roleid(456)   // Alternative to rolename
    .finaltime("100d")
    .slidingtime("30d")
    .build();
```

### SharedRecordOptions
Used for creating shared records with field specifications and partner information.

```java
SharedRecordOptions options = SharedRecordOptions.builder()
    .fields("name,email,phone")
    .partner("partner-org")
    .appname("myapp")
    .finaltime("7d")
    .build();
```

### LegalBasisOptions
Used for creating legal basis records with comprehensive legal information.

```java
LegalBasisOptions options = LegalBasisOptions.builder()
    .brief("marketing-consent")
    .status("active")
    .module("marketing")
    .fulldesc("Consent for marketing communications")
    .shortdesc("Marketing consent")
    .basistype("consent")
    .requiredmsg("You must accept marketing communications")
    .requiredflag(true)
    .build();
```

### LegalBasisUpdateOptions
Used for updating legal basis records.

```java
LegalBasisUpdateOptions options = LegalBasisUpdateOptions.builder()
    .status("inactive")
    .module("marketing")
    .fulldesc("Updated consent description")
    .requiredflag(false)
    .build();
```

### AgreementAcceptOptions
Used for accepting agreements with detailed acceptance information.

```java
AgreementAcceptOptions options = AgreementAcceptOptions.builder()
    .agreementmethod("web-form")
    .referencecode("REF123")
    .starttime("10d")
    .finaltime("100d")
    .status("active")
    .lastmodifiedby("admin@company.com")
    .build();
```

### ConnectorOptions
Used for configuring database and API connectors.

```java
ConnectorOptions options = ConnectorOptions.builder()
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
```

### TenantOptions
Used for creating and managing tenants.

```java
TenantOptions options = TenantOptions.builder()
    .tenantname("my-tenant")
    .tenantorg("My Organization")
    .email("admin@myorg.com")
    .build();
```

### ProcessingActivityOptions
Used for creating processing activities.

```java
ProcessingActivityOptions options = ProcessingActivityOptions.builder()
    .activity("data-processing")
    .title("Data Processing Activity")
    .script("process_data.js")
    .fulldesc("Process user data for analytics")
    .applicableto("users")
    .build();
```

### ProcessingActivityUpdateOptions
Used for updating processing activities.

```java
ProcessingActivityUpdateOptions options = ProcessingActivityUpdateOptions.builder()
    .newactivity("updated-processing")
    .title("Updated Processing Activity")
    .script("updated_process_data.js")
    .fulldesc("Updated process description")
    .applicableto("all-users")
    .build();
```

### GroupOptions
Used for creating and managing groups.

```java
GroupOptions options = GroupOptions.builder()
    .groupname("premium-users")
    .grouptype("user-group")
    .groupdesc("Premium user group")
    .build();
```

### RoleOptions
Used for creating and managing roles.

```java
RoleOptions options = RoleOptions.builder()
    .rolename("admin")
    .roledesc("Administrator role")
    .build();
```

### TokenOptions
Used for creating tokens with time-based parameters.

```java
TokenOptions options = TokenOptions.builder()
    .unique(true)
    .slidingtime("1h")
    .finaltime("24h")
    .build();
```

### PolicyOptions
Used for creating and managing policies.

```java
Map<String, Object> policyData = new HashMap<>();
policyData.put("retention_days", 365);
policyData.put("auto_delete", true);

PolicyOptions options = PolicyOptions.builder()
    .policyname("data-retention")
    .policydesc("Data retention policy")
    .policy(policyData)
    .build();
```

## Usage Examples

### Creating a User with Typed Options

```java
Map<String, Object> profile = new HashMap<>();
profile.put("email", "user@example.com");
profile.put("name", "John Doe");

UserOptions options = UserOptions.builder()
    .groupname("premium")
    .finaltime("100d")
    .build();

Map<String, Object> result = api.createUser(profile, options, null);
```

### Creating Access Tokens with Typed Options (Matches TypeScript API)

```java
BasicOptions tokenOptions = BasicOptions.builder()
    .finaltime("24h")
    .slidingtime("1h")
    .build();

// For user tokens (matches TypeScript: createUserXToken(mode, identity, options))
Map<String, Object> userToken = api.createUserXToken("email", "user@example.com", tokenOptions, null);

// For role tokens (matches TypeScript: createRoleXToken(roleref, options))
Map<String, Object> roleToken = api.createRoleXToken("admin", tokenOptions, null);
```

### Creating Shared Records with Typed Options

```java
SharedRecordOptions sharedOptions = SharedRecordOptions.builder()
    .fields("name,email")
    .partner("partner-org")
    .finaltime("7d")
    .build();

Map<String, Object> sharedRecord = api.createSharedRecord("email", "user@example.com", sharedOptions, null);
```

### Creating Legal Basis with Typed Options

```java
LegalBasisOptions options = LegalBasisOptions.builder()
    .brief("marketing-consent")
    .status("active")
    .module("marketing")
    .fulldesc("Consent for marketing communications")
    .shortdesc("Marketing consent")
    .basistype("consent")
    .requiredmsg("You must accept marketing communications")
    .requiredflag(true)
    .build();

Map<String, Object> legalBasis = api.createLegalBasis(options, null);
```

### Updating Legal Basis with Typed Options

```java
LegalBasisUpdateOptions options = LegalBasisUpdateOptions.builder()
    .status("inactive")
    .module("marketing")
    .fulldesc("Updated consent description")
    .requiredflag(false)
    .build();

Map<String, Object> updatedBasis = api.updateLegalBasis("marketing-consent", options, null);
```

### Accepting Agreements with Typed Options

```java
AgreementAcceptOptions options = AgreementAcceptOptions.builder()
    .agreementmethod("web-form")
    .referencecode("REF123")
    .starttime("10d")
    .finaltime("100d")
    .status("active")
    .lastmodifiedby("admin@company.com")
    .build();

Map<String, Object> agreement = api.acceptAgreement("email", "user@example.com", "marketing-consent", options, null);
```

### Creating Connectors with Typed Options

```java
ConnectorOptions options = ConnectorOptions.builder()
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

Map<String, Object> connector = api.createConnector(options, null);
```

### Creating Processing Activities with Typed Options

```java
ProcessingActivityOptions options = ProcessingActivityOptions.builder()
    .activity("data-processing")
    .title("Data Processing Activity")
    .script("process_data.js")
    .fulldesc("Process user data for analytics")
    .applicableto("users")
    .build();

Map<String, Object> activity = api.createProcessingActivity(options, null);
```

### Updating Processing Activities with Typed Options

```java
ProcessingActivityUpdateOptions options = ProcessingActivityUpdateOptions.builder()
    .newactivity("updated-processing")
    .title("Updated Processing Activity")
    .script("updated_process_data.js")
    .fulldesc("Updated process description")
    .applicableto("all-users")
    .build();

Map<String, Object> updatedActivity = api.updateProcessingActivity("data-processing", options, null);
```

### Creating Groups with Typed Options

```java
GroupOptions options = GroupOptions.builder()
    .groupname("premium-users")
    .grouptype("user-group")
    .groupdesc("Premium user group")
    .build();

Map<String, Object> group = api.createGroup(options, null);
```

### Creating Roles with Typed Options

```java
RoleOptions options = RoleOptions.builder()
    .rolename("admin")
    .roledesc("Administrator role")
    .build();

Map<String, Object> role = api.createRole(options, null);
```

### Creating Tokens with Typed Options

```java
TokenOptions options = TokenOptions.builder()
    .unique(true)
    .slidingtime("1h")
    .finaltime("24h")
    .build();

Map<String, Object> token = api.createToken("credit-card", "4111111111111111", options, null);
```

### Creating Policies with Typed Options

```java
Map<String, Object> policyData = new HashMap<>();
policyData.put("retention_days", 365);
policyData.put("auto_delete", true);

PolicyOptions options = PolicyOptions.builder()
    .policyname("data-retention")
    .policydesc("Data retention policy")
    .policy(policyData)
    .build();

Map<String, Object> policy = api.createPolicy(options, null);
```

### Creating Tenants with Typed Options

```java
TenantOptions options = TenantOptions.builder()
    .tenantname("my-tenant")
    .tenantorg("My Organization")
    .email("admin@myorg.com")
    .build();

Map<String, Object> tenant = api.createTenant(options, null);
```

## Benefits

1. **Type Safety**: Compile-time checking of option parameters
2. **IDE Support**: Better autocomplete and refactoring support
3. **Documentation**: Self-documenting code with clear field names
4. **Validation**: Can add validation annotations in the future
5. **Maintainability**: Easier to maintain and extend

## Backward Compatibility

All existing methods that accept `Map<String, Object>` continue to work. The new typed option classes are provided as overloaded methods, so you can gradually migrate your code to use the typed options.

## Conversion

The `OptionsConverter` utility class provides methods to convert typed options to `Map<String, Object>` for backward compatibility:

```java
Map<String, Object> mapOptions = OptionsConverter.toMap(userOptions);
```

## Future Enhancements

Additional option classes can be added for:
- ConnectorOptions
- LegalBasisOptions
- ProcessingActivityOptions
- TenantOptions
- GroupOptions
- RoleOptions
- TokenOptions
- PolicyOptions
- SessionOptions

These would follow the same pattern as the existing option classes. 