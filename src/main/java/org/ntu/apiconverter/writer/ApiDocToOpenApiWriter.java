package org.ntu.apiconverter.writer;

import org.ntu.apiconverter.common.write.JSONObjectApiDocWriterComponent;
import org.ntu.apiconverter.entity.ApiDoc;
import org.ntu.apiconverter.entity.ApiDocEntry;

import java.io.*;

public class ApiDocToOpenApiWriter implements Writer {

    private String outputPath;

    private JSONObjectApiDocWriterComponent jsonObjectApiDocWriterComponent;

    public ApiDocToOpenApiWriter(){
        outputPath = "test.yaml";

        jsonObjectApiDocWriterComponent = new JSONObjectApiDocWriterComponent();
    }


    @Override
    public void write(Object content) {
        if (!content.getClass().isAssignableFrom(ApiDoc.class)){
            return;
        }

        ApiDoc apiDoc = (ApiDoc) content;
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(outputPath);
            writeHeaderInfo(fileWriter, apiDoc);
            writeBodyInfo(fileWriter, apiDoc);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
        } finally {
            try {
                fileWriter.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    public void writeHeaderInfo(FileWriter fileWriter, ApiDoc apiDoc) throws IOException {
        fileWriter.write(jsonObjectApiDocWriterComponent.format(null, apiDoc.getHeaderInfo(), 0, ""));
    }

    public void writeBodyInfo(FileWriter fileWriter, ApiDoc apiDoc) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("paths: \n");
        for (ApiDocEntry apiDocEntry : apiDoc.getApiDocEntries()){
            sb.append(" "+apiDocEntry.getPath()+": \n");
            for (String method : apiDocEntry.getBody().keySet()){
                sb.append(jsonObjectApiDocWriterComponent.format(method.toLowerCase(), apiDocEntry.getBody().get(method),2,""));
            }
        }

        fileWriter.write(sb.toString());
    }
}
