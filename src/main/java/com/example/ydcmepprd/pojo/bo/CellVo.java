package com.example.ydcmepprd.pojo.bo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.ArrayList;

/**
 * @description:sap返回报文解析后的数据vo
 * @author: yaolewei
 * @date: 2019-09-10 16:10
 */
@Data
@JsonFormat(pattern = "Cell")
public class CellVo {

    private ArrayList<String> currency;
    private ArrayList<String> fmtValue;
    private ArrayList<String> value;
}
