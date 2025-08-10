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

### From GitHub Packages

Add the repository and dependency to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>github</id>
        <name>GitHub Packages</name>
        <url>https://maven.pkg.github.com/securitybunker/databunkerpro-java</url>
    </repository>
</repositories>

<dependency>
    <groupId>org.databunker</groupId>
    <artifactId>databunkerpro-java</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### From JitPack (Alternative)

Add the JitPack repository and dependency to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.securitybunker</groupId>
    <artifactId>databunkerpro-java</artifactId>
    <version>v1.0.0</version>
</dependency>
```

**Note**: Replace `v1.0.0` with your desired version tag (e.g., `v1.0.1`, `v2.0.0`, etc.)

### From Local Maven Repository

For development or internal use:

```bash
# Clone and install locally
git clone https://github.com/securitybunker/databunkerpro-java.git
cd databunkerpro-java
mvn clean install
```

Then add the dependency to your `pom.xml`:

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

## Deployment

The project supports deployment to both GitHub Packages and JitPack.

### GitHub Packages Deployment

The project includes a GitHub Actions workflow that automatically:
- Builds the project on push to main branch
- Runs tests
- Deploys to GitHub Packages when triggered

To deploy:
1. Go to Actions tab in your GitHub repository
2. Select "Build java API" workflow
3. Click "Run workflow"
4. Set "Deploy to GitHub Packages" to true
5. Click "Run workflow"

### JitPack Deployment (Alternative)

JitPack automatically builds and publishes your GitHub repository as a Maven dependency.

1. **Create a Git tag** for your release:
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```

2. **JitPack automatically builds** and publishes the package

3. **Check build status** at: https://jitpack.io/#securitybunker/databunkerpro-java

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
