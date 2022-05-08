package org.ntu.apiconverter;

import org.ntu.apiconverter.handler.ApiDocEntryFormatHandler;
import org.ntu.apiconverter.handler.DuplicationDetectionHandler;
import org.ntu.apiconverter.merger.PathParameterIdentifier;
import org.ntu.apiconverter.reader.DatasourceReader;
import org.ntu.apiconverter.reader.MongoDBReader;
import org.ntu.apiconverter.worker.HandlerChainWorker;
import org.ntu.apiconverter.writer.ApiDocToOpenApiWriter;
import org.ntu.apiconverter.writer.Writer;

public class Main {
    public static void main(String[] args) {
        HandlerChainWorker handlerChainWorker = new HandlerChainWorker.HandlerChainWorkerBuilder()
                .addHandler(new DuplicationDetectionHandler())
                .addHandler(new ApiDocEntryFormatHandler())
                .build();
        DatasourceReader datasourceReader = new MongoDBReader();
        Writer writer = new ApiDocToOpenApiWriter();
        datasourceReader.readAndCallback(handlerChainWorker);

        PathParameterIdentifier identifier = new PathParameterIdentifier();
        writer.write(identifier.update(handlerChainWorker.getApiDoc()));
    }
}
