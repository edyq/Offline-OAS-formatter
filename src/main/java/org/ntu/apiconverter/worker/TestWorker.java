package org.ntu.apiconverter.worker;

import org.bson.Document;
import org.ntu.apiconverter.common.Callback;
import org.ntu.apiconverter.reader.MongoDBReader;

public class TestWorker implements Callback, Worker {
    @Override
    public void execute(Object arg) {
        run(arg);
    }

    @Override
    public void run(Object args) {
        System.out.println("here");
    }

    public static void main(String[] args) {
        MongoDBReader mongoDBReader = new MongoDBReader();

        mongoDBReader.readAndCallback(new Callback() {
            @Override
            public void execute(Object arg) {
                Document document = (Document) arg;
                if (document.containsKey("urlParam")){
                    System.out.println(document.get("urlParam"));
                }
            }
        });
    }
}
