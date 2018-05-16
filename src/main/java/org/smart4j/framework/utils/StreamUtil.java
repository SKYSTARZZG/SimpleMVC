package org.smart4j.framework.utils;

import com.fasterxml.jackson.core.util.BufferRecycler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;

/**
 * @Author Administrator
 * @Date 2018-05-03
 * @since 1.0.0
 */
public class StreamUtil {

    private static final Logger LOGGER= LoggerFactory.getLogger(StringUtil.class);
    public static String getString(InputStream is) {
        StringBuilder sb=new StringBuilder();
        try{
            BufferedReader reader=new BufferedReader(new InputStreamReader(is));
            String line;
            while((line=reader.readLine())!=null){
                sb.append(line);
            }
        } catch (IOException e) {
            LOGGER.error("get String failture",e);
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}
