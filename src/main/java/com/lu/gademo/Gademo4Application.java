package com.lu.gademo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan("com.lu.gademo.mapper")
public class Gademo4Application {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(Gademo4Application.class, args);

        //CertStoReqDao certStoReqDao = context.getBean(CertStoReqDao.class);
		/*IndDesenDao indDesenDao = context.getBean(IndDesenDao.class);

		ExecutorService executorService = Executors.newFixedThreadPool(5);

		//监听客户端
		try (ServerSocket serverSocket = new ServerSocket(8888);){
			System.out.println("服务器在监听 port 8888.");

			while (true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println("客户端连接成功!");
				// 处理客户端连接
				executorService.execute(new ClientHandler(clientSocket));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}*/
    }
}
