package org.ntu.apiconverter.reader;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.ntu.apiconverter.common.Callback;

@Getter
@Setter
public class MongoDBReader implements DatasourceReader{

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> mongoCollection;


    public MongoDBReader(){
        configure();
    }

    public void configure(){
        // for testing purpose only, things are hard coded
        mongoClient = MongoClients.create(
                "mongodb://127.0.0.1:27017"
        );

        mongoDatabase = mongoClient.getDatabase("passiveProxy");

        mongoCollection = mongoDatabase.getCollection("request");
    }


    @Override
    public void readAndCallback(Callback callback) {
        // tbc: is it similar with any kind of design patterns? association -> dependency
        mongoCollection.find().forEach(
                (document) -> callback.execute(document)
        );
    }
}
