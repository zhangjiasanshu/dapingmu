package com.example.ydcmepprd.api;

import com.example.ydcmepprd.exeption.BusinessExeption;
import com.example.ydcmepprd.exeption.BusinessExeptionCodeEnum;
import com.example.ydcmepprd.utils.MyStringUtil;
import com.example.ydcmepprd.utils.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @description:wap请求工具类
 * @author: yaolewei
 * @date: 2019-09-10 15:38
 */
@Slf4j
@Service
public class WapRequestClient {

    @Value("${waprequest.url}")
    private String url;
    @Value("${waprequest.username}")
    private String username;
    @Value("${waprequest.password}")
    private String password;
    private Result log;

    public  String client(String request) {
        RestClient restClient = new RestClient(username,password);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        StringBuilder xmlString = new StringBuilder();
        xmlString.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:schemas-microsoft-com:xml-analysis\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <urn:Execute>\n" +
                "         <urn:Command>\n" +
                "            <urn:Statement>")
                .append(request)
                .append("</urn:Statement>\n")
                .append("         </urn:Command>\n" +
                        "         <urn:Properties/>\n" +
                        "      </urn:Execute>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>");
        HttpEntity<String> formEntity = new HttpEntity<>(xmlString.toString(), headers);
        ResponseEntity<String> responseEntity = restClient.postForEntity(url, formEntity, String.class);
        if (responseEntity == null || MyStringUtil.isEmpty(responseEntity.getBody())){
            log.error("SAP接口返回异常，或返回数据为空");
            throw new BusinessExeption(BusinessExeptionCodeEnum.JSON_IS_NULL);
        }
        return responseEntity.getBody();
    }
}
