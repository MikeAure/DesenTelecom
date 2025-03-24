package com.lu.gademo;

import com.lu.gademo.utils.OFDMessageFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OFDMessageFactoryTest {
    @Autowired
    OFDMessageFactory factory;
    @Test
    void testCreateOfdMessage() {
        System.out.println(factory.createOfdMessage());
    }

    @Test
    void testCreateAllMessage() throws Exception {
        System.out.println(factory.createOfdMessage("233333333", "233333",
                "D:\\test\\test.ofd", "24242424",
                "D:\\test\\test.ofd", "24242424",
                "D:\\test\\test.ofd", "24242424",
                "32342342342"));
    }
}
