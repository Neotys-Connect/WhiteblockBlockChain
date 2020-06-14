package com.neotys.ethereumJ.common.utils.Whiteblock.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class WhiteblockDataModel {

    public String generateOutPut() throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();
      //  xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String xml = xmlMapper.writeValueAsString(this);
        return xml;
    }

}
