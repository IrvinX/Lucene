package com.irvin.service.spark;

import com.irvin.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

/**
 * @author irvin
 * @date Create in 下午3:50 2017/8/19
 * @description
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class FileTest {

	@Test
	public void getFolder(){
		File file = new File("index");
		// get the folder list
		File[] array = file.listFiles();

		for(int i=0;i<array.length;i++){
			if(!array[i].isFile()){
				System.out.println("*****" + array[i].getPath());
			}
		}
	}
}
