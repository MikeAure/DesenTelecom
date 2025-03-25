package com.lu.gademo;

import com.lu.gademo.utils.RecvFileDesen;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Paths;

@SpringBootTest
public class PoiProcessXlsxTest {
    @Autowired
    private RecvFileDesen recvFileDesen;

    @Test
    public void excelTest() throws Exception {
        recvFileDesen.processXlsx(Paths.get("D:\\test_data\\sheets\\comment.xlsx")
                , Paths.get("D:\\test_data\\sheets\\comment_out.xlsx"));
    }
}
