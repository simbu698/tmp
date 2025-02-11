import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.auth.PlainTextAuthProvider;
import java.net.InetSocketAddress;

public class CassandraConnection {
    public static void main(String[] args) {
        // Replace with your Cassandra details
        String host = "127.0.0.1";
        int port = 9042;
        String datacenter = "datacenter1"; // Replace with your data center name
        String keyspace = "your_keyspace"; // Replace with your keyspace
        String username = "your_username";
        String password = "your_password";

        try (CqlSession session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress(host, port))
                .withAuthProvider(new PlainTextAuthProvider(username, password))
                .withKeyspace(keyspace)
                .withLocalDatacenter(datacenter)
                .build()) {
            System.out.println("Connected to Cassandra!");

            // Example query to check the connection
            session.execute("SELECT release_version FROM system.local").forEach(row -> 
                System.out.println("Cassandra Version: " + row.getString("release_version"))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
