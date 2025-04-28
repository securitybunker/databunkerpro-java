# DatabunkerPro Java Client

A Java client library for interacting with the DatabunkerPro API.

## Features

- User management (create, get, update, delete)
- User request management
- App data management
- Legal basis and agreement management
- Processing activity management
- Connector management
- Group and role management
- Policy management
- Token management
- Audit management
- Tenant management
- Session management
- System configuration
- Bulk operations
- Easy to use Java API
- Thread-safe implementation
- Comprehensive test suite

## Requirements

- Java 11 or higher
- Maven 3.6 or higher

## Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>org.databunkerpro</groupId>
    <artifactId>databunkerpro-java</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Usage

### Basic Setup

```java
import org.databunkerpro.DatabunkerproApi;
import java.util.Map;
import java.util.HashMap;

public class Example {
    public static void main(String[] args) {
        String baseURL = "https://pro.databunker.org";
        String xBunkerToken = "your-api-token";
        String xBunkerTenant = "your-tenant-name";

        try (DatabunkerproApi api = new DatabunkerproApi(baseURL, xBunkerToken, xBunkerTenant)) {
            // Use the API here
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### User Management

#### Creating a User

```java
Map<String, Object> profile = new HashMap<>();
profile.put("email", "user@example.com");
profile.put("name", "John Doe");
profile.put("phone", "+1234567890");

Map<String, Object> options = new HashMap<>();
options.put("groupname", "users");
options.put("rolename", "member");

Map<String, Object> result = api.createUser(profile, options, null);
```

#### Getting a User

```java
Map<String, Object> user = api.getUser("email", "user@example.com", null);
```

#### Updating a User

```java
Map<String, Object> updateProfile = new HashMap<>();
updateProfile.put("name", "John Smith");
updateProfile.put("phone", "+9876543210");

Map<String, Object> result = api.updateUser("email", "user@example.com", updateProfile, null);
```

#### Deleting a User

```java
Map<String, Object> result = api.deleteUser("email", "user@example.com", null);
```

## Testing

To run the tests, you need to set the following environment variables:

```bash
export DATABUNKER_TENANT=your-tenant-name
export DATABUNKER_TOKEN=your-api-token
```

Then run:

```bash
mvn test
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
