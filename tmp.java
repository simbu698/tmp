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
                .withAuthCredentials(username, password)
                .withKeyspace(keyspace)
                .withLocalDatacenter(datacenter)
                .withSslContext(DefaultDriverConfigLoader.fromClasspath("application.conf").getSslContext())
                .build()) {
            System.out.println("Connected to Cassandra!");

            // Example query to check the connection with a specified consistency level
            session.execute(
                session.prepare("SELECT release_version FROM system.local")
                    .bind()
                    .setConsistencyLevel(ConsistencyLevel.QUORUM)
            ).forEach(row -> 
                System.out.println("Cassandra Version: " + row.getString("release_version"))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
