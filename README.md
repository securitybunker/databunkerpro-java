# DatabunkerPro Java Client

A Java client library for interacting with the DatabunkerPro API. DatabunkerPro is a secure, privacy-focused data vault that helps organizations manage and protect sensitive user data.

## Features

- Complete implementation of the DatabunkerPro API
- User management (create, get, update, delete, patch)
- App data management
- File storage (encrypted per-user files with tags and expiration)
- Legal basis and agreement management
- Connector management
- Group and role management
- Policy management
- Token management (including bulk operations)
- Audit management
- Tenant management
- Session management
- System configuration and metrics
- Bulk operations
- Shared record management
- Wrapping key generation for Shamir's Secret Sharing
- Typed options classes for better type safety
- Thread-safe implementation
- Comprehensive test suite
- Enhanced error handling
- Continuous security scanning (Semgrep SAST, pinned CI actions)

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
    <version>1.1.0</version>
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
    <version>v1.1.0</version>
</dependency>
```

**Note**: Replace `v1.1.0` with your desired version tag (e.g., `v1.1.1`, `v2.0.0`, etc.)

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
    <version>1.1.1-SNAPSHOT</version>
</dependency>
```

## Quickstart

You need a Databunker Pro instance to talk to. Demo mode gives you one in a single command — no database, no configuration, everything held in memory:

```bash
docker run -p 3000:3000 -d --rm --name databunkerpro securitybunker/databunkerpro demo
```

Check that it came up:

```bash
docker logs databunkerpro
```

```
 Databunker Pro demo is ready
  Web UI:            http://localhost:3000/
  Root access token: DEMO
  Database:          in-memory, erased on restart
```

The root access token in demo mode is the fixed string `DEMO`. Save this as `Quickstart.java`:

```java
import org.databunker.DatabunkerproApi;
import org.databunker.options.FileOptions;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Quickstart {
    public static void main(String[] args) throws Exception {
        DatabunkerproApi api = new DatabunkerproApi("http://localhost:3000", "DEMO", null);

        // Create a user record. The vault encrypts the profile and returns a user token.
        Map<String, Object> profile = new HashMap<>();
        profile.put("email", "john@javatest.com");
        profile.put("name", "John Doe");
        profile.put("phone", "+15551234567");

        Map<String, Object> created = api.createUser(profile, null, null);
        System.out.println("User token: " + created.get("token"));

        // Read the record back by any indexed field: token, login, email, phone, custom.
        Map<String, Object> user = api.getUser("email", "john@javatest.com", null);
        System.out.println("Profile: " + user.get("profile"));

        // Store an encrypted file against that user, tagged by document type.
        String filedata = Base64.getEncoder()
                .encodeToString("fake passport scan bytes".getBytes());
        FileOptions options = FileOptions.builder()
                .tags(Arrays.asList("passport", "kyc"))
                .build();

        Map<String, Object> file = api.createFile(
                "email", "john@javatest.com", "passport.jpg", filedata, options, null);
        System.out.println("File uuid: " + file.get("fileuuid") + " | tags: " + file.get("tags"));

        // List the user's files, filtered by tag.
        Map<String, Object> listing = api.listUserFiles("email", "john@javatest.com", "kyc", null);
        System.out.println("Files tagged kyc: " + listing.get("files"));

        // Fetch the file back. Content returns base64-encoded in filedata.
        Map<String, Object> fetched = api.getFile(
                "email", "john@javatest.com", (String) file.get("fileuuid"), null);
        byte[] bytes = Base64.getDecoder().decode((String) fetched.get("filedata"));
        System.out.println("Decrypted: " + new String(bytes));

        // Delete user record.
        api.deleteUser("email", "john@javatest.com", null);
        System.out.println("User deleted");
    }
}
```

```bash
mvn compile exec:java -Dexec.mainClass=Quickstart
```

```
User token: 91db0180-829d-9e9d-000a-e6975c746366
Profile: {email=john@javatest.com, name=John Doe, phone=+15551234567}
File uuid: 94e13382-b117-2a6e-ac2f-edc8ae2fb743 | tags: [kyc, passport]
Files tagged kyc: [{filename=passport.jpg, fileuuid=94e13382-b117-2a6e-ac2f-edc8ae2fb743, tags=[kyc, passport], ...}]
Decrypted: fake passport scan bytes
User deleted
```

Every method takes its optional arguments explicitly, since Java has no default parameters — pass `null` for the options and request-metadata parameters when you do not need them. Tags are lowercased, de-duplicated and sorted on write, which is why they come back in a different order than they were sent.

When you are done, stop the instance. It was started with `--rm`, so the container and its in-memory database are discarded:

```bash
docker stop databunkerpro
```

> **Demo mode is for evaluation only.** The database is in memory, the wrapping key is a fixed public value, and the root token is the well-known string `DEMO`. Never point it at real personal data. For a real deployment see the [installation guide](https://docs.databunker.org/pro/installation/docker-compose).

## New Features in Latest Version

### New in 1.1.0

- **File API**: Store, retrieve, list, retag and delete encrypted per-user files
  (`createFile`, `getFile`, `listUserFiles`, `replaceFileTags`, `deleteFile`), plus
  `bulkListFilesByTag` for bulk lookups. Options are passed with the typed `FileOptions`
  builder (mimetype, tags, `finaltime`, `slidingtime`).
- **Apache HttpClient 5**: Migrated off end-of-life HttpClient 4.x. If your project pins
  HttpClient transitively, it now resolves `org.apache.httpcomponents.client5:httpclient5`.
- **Removed internal portal endpoints**: `preloginUser`, `loginUser`, `createCaptcha`,
  `getUIConf` and `getTenantConf` were internal to the DatabunkerPro web portal and are
  no longer part of the client.

### Enhanced API Methods
- **Wrapping Key Generation**: Generate wrapping keys from Shamir's Secret Sharing keys
- **Typed Patch Operations**: Use structured `PatchOperation` objects for user updates
- **Bulk Token Operations**: Create multiple tokens efficiently with typed options
- **Bulk User Deletion**: Delete multiple users efficiently in a single operation
- **License Key Management**: Set system license keys for enhanced functionality
- **Shared Record Management**: Create and retrieve shared records with partner organizations

### Improved Error Handling
- Better HTTP status code handling
- Enhanced error messages and logging
- Graceful handling of API errors

### Type Safety Improvements
- Structured patch operations with `PatchOperation` class
- Builder pattern for complex options
- Enhanced error handling

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

// Patch a user with typed operations
PatchOperation[] patchOps = {
    new PatchOperation("replace", "/profile/name", "Jane Doe"),
    new PatchOperation("add", "/profile/age", 25)
};
Map<String, Object> patchResult = api.patchUser("email", "user@example.com", patchOps, null);
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

### File Storage

```java
// Store a file. The content is passed base64-encoded.
String filedata = Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get("passport.pdf")));
FileOptions fileOptions = FileOptions.builder()
    .mimetype("application/pdf")
    .tags(Arrays.asList("kyc", "passport"))
    .finaltime("365d")
    .build();
Map<String, Object> created = api.createFile("email", "user@example.com", "passport.pdf", filedata, fileOptions, null);
String fileuuid = (String) created.get("fileuuid");

// Get a file by uuid
Map<String, Object> file = api.getFile("email", "user@example.com", fileuuid, null);

// Get a file by name (the newest match is returned)
Map<String, Object> byName = api.getFile("email", "user@example.com", null, "passport.pdf", false, null);

// List the metadata of a user's files, optionally filtered by a single tag
Map<String, Object> allFiles = api.listUserFiles("email", "user@example.com", null);
Map<String, Object> kycFiles = api.listUserFiles("email", "user@example.com", "kyc", null);

// Replace the complete tag set on a file
api.replaceFileTags("email", "user@example.com", fileuuid, Arrays.asList("kyc", "verified"), null);

// Delete a file
api.deleteFile("email", "user@example.com", fileuuid, null);
```

Tags are lowercased and de-duplicated by the server, must match `^[a-z0-9][a-z0-9._-]{0,49}$`,
and at most 16 are kept per file.

### System Configuration

```java
// Get system statistics
Map<String, Object> stats = api.getSystemStats(null);
System.out.println("System statistics: " + stats.get("stats"));

// Generate wrapping key from Shamir's Secret Sharing keys
Map<String, Object> wrappingKey = api.generateWrappingKey("key1", "key2", "key3", null);
System.out.println("Generated wrapping key: " + wrappingKey.get("wrappingkey"));

// Get system metrics
Map<String, Object> metrics = api.getSystemMetrics(null);
System.out.println("System metrics: " + metrics);

// Set system license key
Map<String, Object> licenseResult = api.setLicenseKey("your-license-key", null);
System.out.println("License key setting result: " + licenseResult.get("status"));
```

### Token Management

```java
// Create a single token
TokenOptions tokenOptions = TokenOptions.builder()
    .slidingtime("1d")
    .finaltime("10d")
    .unique(true)
    .build();
Map<String, Object> token = api.createToken("creditcard", "1234567890", tokenOptions, null);

// Create multiple tokens in bulk
Map<String, Object>[] tokenRecords = new Map[2];
tokenRecords[0] = new HashMap<>();
tokenRecords[0].put("tokentype", "creditcard");
tokenRecords[0].put("record", "1234567890");
tokenRecords[1] = new HashMap<>();
tokenRecords[1].put("tokentype", "email");
tokenRecords[1].put("record", "user@example.com");

Map<String, Object> bulkTokens = api.createTokensBulk(tokenRecords, tokenOptions, null);
```

### Shared Record Management

```java
// Create a shared record
SharedRecordOptions sharedOptions = SharedRecordOptions.builder()
    .fields("name,email")
    .partner("partner-org")
    .appname("myapp")
    .finaltime("100d")
    .build();
Map<String, Object> sharedRecord = api.createSharedRecord("email", "user@example.com", sharedOptions, null);

// Get a shared record
Map<String, Object> retrievedRecord = api.getSharedRecord("record-uuid", null);
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
   git tag v1.1.0
   git push origin v1.1.0
   ```

2. **JitPack automatically builds** and publishes the package

3. **Check build status** at: https://jitpack.io/#securitybunker/databunkerpro-java

## Security

This library is scanned on every push and pull request, with a weekly scheduled sweep to catch drift:

- **SAST (Semgrep):** static analysis using the `p/java`, `p/secrets`, `p/security-audit`, and `p/owasp-top-ten` rulesets. A finding fails the check, and results are published to the repository's **Code Scanning** tab. See [`.github/workflows/semgrep.yml`](.github/workflows/semgrep.yml).
- **Supply-chain hardening:** every GitHub Action is pinned to a full commit SHA, so a mutable tag (`@v4`) cannot be silently repointed to malicious code.

Reproduce the SAST scan locally:

```bash
pip install semgrep
semgrep scan \
    --config p/java \
    --config p/secrets \
    --config p/security-audit \
    --config p/owasp-top-ten
```

To report a security vulnerability, please email hello@databunker.org rather than opening a public issue.

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
