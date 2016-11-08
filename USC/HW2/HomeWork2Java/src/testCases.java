import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Scanner;

import org.junit.Test;

import junit.framework.Assert;
public class testCases {

	@Test
	public void testHomeWork() throws NumberFormatException, IOException{
		String folderName = "testcases5";
		String infileEnding = ".in";
		String outfileEnding = ".out";

		File dir = new File(folderName);
		File[] foundFiles = dir.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.endsWith(infileEnding);
		    }
		});

		for (File file : foundFiles) {
			String nameOfFile = file.getName().split(infileEnding)[0];
			System.out.println("Running for testcase "+nameOfFile);
			String answer = homework.runHomeWork(folderName+"/"+nameOfFile+infileEnding);
			answer = answer.trim();
			String content = new Scanner(new File(folderName+"/"+nameOfFile+outfileEnding)).useDelimiter("\\Z").next();
			content = content.trim();
			content=content.replaceAll("[\r]", "");
			try{
				Assert.assertEquals(content, answer);
			}catch(Error e){
				System.out.println("Fail "+nameOfFile);
				throw e;
			}
		}    
	}
}
