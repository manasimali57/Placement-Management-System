import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

class JavaMongoExample {
    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            System.out.println("Created Mongo Connection successfully");

            // Access a database
            MongoDatabase db = mongoClient.getDatabase("placement_db");
            System.out.println("Get database is successful");

            // List all database names
            System.out.println("Below are list of databases present in MongoDB:");
            MongoIterable<String> dbs = mongoClient.listDatabaseNames();
            for (String name : dbs) {
                System.out.println(name);
            }
        } catch (Exception e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
    }
}
