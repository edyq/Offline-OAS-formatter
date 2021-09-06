package org.ntu.apiconverter.reader;

import org.ntu.apiconverter.common.Callback;

public interface DatasourceReader {
    void readAndCallback(Callback callback);
}
