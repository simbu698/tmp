import com.datastax.oss.driver.api.core.CqlSession;
import java.net.InetSocketAddress;

public class CassandraConnection {
    public static void main(String[] args) {
        // Replace with your Cassandra host and port
        String host = "127.0.0.1";
        int port = 9042;

        try (CqlSession session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress(host, port))
                .withLocalDatacenter("datacenter1") // Replace with your data center name
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
