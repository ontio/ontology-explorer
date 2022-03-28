package com.github.ontio.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CsvUtil {

    /**
     * CSV文件列分隔符
     */
    private static final String CSV_COLUMN_SEPARATOR = ",";

    /**
     * CSV文本用引号+"\t"，防止科学计数法
     */
    private static final String CSV_QUOTATION_START = "\"";

    private static final String CSV_QUOTATION_END = "\t\"";

    /**
     * CSV文件行分隔符
     */
    private static final String CSV_ROW_SEPARATOR = System.lineSeparator();

    /**
     * @param dataList 集合数据
     * @param titles   表头部数据
     * @param keys     表内容的键值
     * @param os       输出流
     */
    public static void doExport(List<Map<String, Object>> dataList, List<String> titles, List<String> keys, OutputStream os) throws Exception {
        StringBuilder buf = new StringBuilder();

        // 组装表头
        for (String title : titles) {
            buf.append(title).append(CSV_COLUMN_SEPARATOR);
        }
        buf.append(CSV_ROW_SEPARATOR);

        // 组装数据
        if (!CollectionUtils.isEmpty(dataList)) {
            for (Map<String, Object> data : dataList) {
                for (String key : keys) {
                    buf.append(CSV_QUOTATION_START).append(data.get(key)).append(CSV_QUOTATION_END).append(CSV_COLUMN_SEPARATOR);
                }
                buf.append(CSV_ROW_SEPARATOR);
            }
        }

        // 写出响应
        os.write(buf.toString().getBytes("UTF-8"));
        os.flush();
    }

    /**
     * 设置Header
     *
     * @param fileName
     * @param response
     * @throws UnsupportedEncodingException
     */
    public static void responseSetProperties(String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        // 读取字符编码
        String utf = "UTF-8";

        // 设置响应
//        response.setContentType("application/ms-txt.numberformat:@");
        response.setContentType("application/csv");
        response.setCharacterEncoding(utf);
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=60");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, utf));
    }

}
