// Load SSL context from a secure location (update the path as needed)
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, null, new java.security.SecureRandom());

        try (CqlSession session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress(host, port))
                .withAuthCredentials(username, password)
                .withKeyspace(keyspace)
                .withLocalDatacenter(datacenter)
                .withSslContext(sslContext)
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
