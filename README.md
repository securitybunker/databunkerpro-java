# DatabunkerPro Java Client

A Java client library for interacting with the DatabunkerPro API. DatabunkerPro is a secure, privacy-focused data vault that helps organizations manage and protect sensitive user data.

## Features

- Complete implementation of the DatabunkerPro API
- User management (create, get, update, delete)
- App data management
- Legal basis and agreement management
- Connector management
- Group and role management
- Policy management
- Token management
- Audit management
- Tenant management
- Session management
- System configuration
- Bulk operations
- Thread-safe implementation
- Comprehensive test suite

## Requirements

- Java 11 or higher
- Maven 3.6 or higher

## Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>org.databunker</groupId>
    <artifactId>databunkerpro-java</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Usage

### Basic Setup

```java
import org.databunker.DatabunkerproApi;

// Initialize the API client
String apiUrl = "https://pro.databunker.org";
String apiToken = "your-api-token";
String tenantName = "your-tenant-name";

try (DatabunkerproApi api = new DatabunkerproApi(apiUrl, apiToken, tenantName)) {
    // Use the API client
}
```

### User Management

```java
// Create a user
Map<String, Object> profile = new HashMap<>();
profile.put("email", "user@example.com");
profile.put("name", "John Doe");
profile.put("phone", "+1234567890");

Map<String, Object> result = api.createUser(profile, null, null);
System.out.println("Created user with token: " + result.get("token"));

// Get a user
Map<String, Object> user = api.getUser("email", "user@example.com", null);
System.out.println("User profile: " + user.get("profile"));

// Update a user
Map<String, Object> updates = new HashMap<>();
updates.put("name", "John Smith");
api.updateUser("email", "user@example.com", updates, null);

// Delete a user
api.deleteUser("email", "user@example.com", null);
```

### App Data Management

```java
// Create app data
Map<String, Object> data = new HashMap<>();
data.put("key", "value");
api.createAppData("email", "user@example.com", "appname", data, null);

// Get app data
Map<String, Object> appData = api.getAppData("email", "user@example.com", "appname", null);
```

### System Configuration

```java
// Get system statistics
Map<String, Object> stats = api.getSystemStats(null);
System.out.println("System statistics: " + stats.get("stats"));
```


## Testing

To run the tests use the following command:

```bash
mvn test
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

For support, please contact us at:
- Email: hello@databunker.org
- Website: https://databunker.org
- GitHub Issues: https://github.com/securitybunker/databunkerpro-java/issues
