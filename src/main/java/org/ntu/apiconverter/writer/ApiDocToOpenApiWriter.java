package org.ntu.apiconverter.writer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.ntu.apiconverter.common.formatter.ApiDocFormatterMediator;
import org.ntu.apiconverter.common.formatter.JSONArrayApiDocFormatter;
import org.ntu.apiconverter.common.formatter.JSONObjectApiDocFormatter;
import org.ntu.apiconverter.common.formatter.StringApiDocFormatter;
import org.ntu.apiconverter.entity.ApiDoc;
import org.ntu.apiconverter.entity.ApiDocEntry;

import java.io.*;

public class ApiDocToOpenApiWriter implements Writer {

    private String outputPath;

    private JSONObjectApiDocFormatter jsonObjectApiDocFormatter;

    public ApiDocToOpenApiWriter(){
        outputPath = "test.yaml";

        initApiFormatters();
    }

    public void initApiFormatters(){
        ApiDocFormatterMediator apiDocWriterMediator = new ApiDocFormatterMediator();
        jsonObjectApiDocFormatter = new JSONObjectApiDocFormatter(apiDocWriterMediator);
        apiDocWriterMediator.registerApiDocFormatter(String.class, new StringApiDocFormatter());
        apiDocWriterMediator.registerApiDocFormatter(JSONArray.class, new JSONArrayApiDocFormatter(apiDocWriterMediator));
        apiDocWriterMediator.registerApiDocFormatter(JSONObject.class, jsonObjectApiDocFormatter);

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
        fileWriter.write(jsonObjectApiDocFormatter.format(null, apiDoc.getHeaderInfo(), 0, ""));
    }

    public void writeBodyInfo(FileWriter fileWriter, ApiDoc apiDoc) throws IOException {
        StringBuilder sb = new StringBuilder();
        fileWriter.write("paths: \n");
        for (ApiDocEntry apiDocEntry : apiDoc.getApiDocEntries()){
            fileWriter.write(" "+apiDocEntry.getPath()+": \n");
            for (String method : apiDocEntry.getBody().keySet()){
                fileWriter.write(jsonObjectApiDocFormatter.format(method.toLowerCase(), apiDocEntry.getBody().get(method),2,""));
            }
        }

//        fileWriter.write(sb.toString());
    }
}
