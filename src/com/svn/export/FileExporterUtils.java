package com.svn.export;

import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class FileExporterUtils {
	private static  TextArea logArea;
	public static boolean export(String logPath ,String rootPath,String exportPath, TextArea resuletArea) {
		logArea = resuletArea;
		List<String>  fileList = readTxtFile(logPath);//日志文件目录
		String webRootName = rootPath.substring(rootPath.lastIndexOf(File.separator)+1);//webRootName 一般为“WebRoot”，但不排除特殊情况
		String beforeWebRoot = rootPath.substring(0,rootPath.lastIndexOf(File.separator));
		for(String pathFromSVN:fileList){
			if(pathFromSVN.trim().isEmpty()){
				System.out.println("跳过文件列表.txt中的空白行");
				continue;
			}
			String relativeLocalPath =pathFromSVN;
			//构造以根路径开头的路径，根路径通常为：WebRoot
			if(pathFromSVN.indexOf("src/")>-1){
				relativeLocalPath= pathFromSVN.replace("src/",webRootName+"/WEB-INF/classes/");
				relativeLocalPath= StringUtils.trimToEmpty(relativeLocalPath);
				if(relativeLocalPath.endsWith(".java")){
					relativeLocalPath=relativeLocalPath.substring(0,relativeLocalPath.indexOf(".java"))+".class";
				}
			}
			relativeLocalPath = relativeLocalPath.replace("/", File.separator);
			relativeLocalPath = relativeLocalPath.substring(relativeLocalPath.indexOf(File.separator)+1);
			copyFile(rootPath+File.separator+relativeLocalPath,exportPath+File.separator+"导出结果"+File.separator+relativeLocalPath,true);
		}
		return true;
	}
	
	 /** 
     * 复制单个文件 
     *  
     * @param srcFileName 
     *            待复制的文件名 
     * @param descFileName 
     *            目标文件名 
     * @param overlay 
     *            如果目标文件存在，是否覆盖 
     * @return 如果复制成功返回true，否则返回false 
     */  
    public static boolean copyFile(String srcFileName, String destFileName,  boolean overlay) {  
        File srcFile = new File(srcFileName);
        boolean hasBeenOverlay=false;
        // 判断源文件是否存在  
        if (!srcFile.exists()) {  
        	String log = logArea.getText();
        	log = log +"\n"+"源文件不存在：" + srcFileName ;
        	logArea.setText(log);
            return false;  
        } else if (!srcFile.isFile()) {  
        	String log = logArea.getText();
        	log = log +"\n"+ "复制文件失败，不是一个文件：" + srcFileName ;
        	logArea.setText(log);
            return false;  
        }  
        // 判断目标文件是否存在  
        File destFile = new File(destFileName);  
        if (destFile.exists()) {  
            // 如果目标文件存在并允许覆盖  
            if (overlay) {
                // 删除已经存在的目标文件，无论目标文件是目录还是单个文件  
                new File(destFileName).delete();  
                hasBeenOverlay=true;
            }  
        } else {  
            // 如果目标文件所在目录不存在，则创建目录  
            if (!destFile.getParentFile().exists()) {  
                // 目标文件所在目录不存在  
                if (!destFile.getParentFile().mkdirs()) {  
                    // 复制文件失败：创建目标文件所在目录失败  
                	String log = logArea.getText();
                	log = log +"\n"+ "创建目标文件所在目录失败!目录为："+destFile.getParentFile().getPath();
                	logArea.setText(log);
                    return false;  
                }  
            }  
        }  
        // 复制文件  
        int byteread = 0; // 读取的字节数  
        InputStream in = null;  
        OutputStream out = null;  
        try {  
            in = new FileInputStream(srcFile);  
            out = new FileOutputStream(destFile);  
            byte[] buffer = new byte[1024];  
            while ((byteread = in.read(buffer)) != -1) {  
                out.write(buffer, 0, byteread);  
            }
            if(!hasBeenOverlay){
            	String log = logArea.getText();
            	//log = log +"\n"+ "原始文件文件"+srcFile.getPath()+" 被导出到："+destFile.getPath();
            	log = log +"\n"+ "导出成功："+srcFile.getPath();
            	logArea.setText(log);
            }else{
            	String log = logArea.getText();
            	log = log +"\n"+ "文件被覆盖:"+destFile.getPath();
            	logArea.setText(log);
            }
            return true;  
        } catch (FileNotFoundException e) {  
            return false;  
        } catch (IOException e) {  
            return false;  
        } finally {  
            try {  
                if (out != null)  
                    out.close();  
                if (in != null)  
                    in.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
    /**
     * 挨行读取日志文件的内容
     * @param filePath
     * @return
     */
    public static List<String> readTxtFile(String filePath){
		List<String> resultList = null;
		try {
			resultList = new ArrayList<String>();
			String encoding = "GBK";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					resultList.add(lineTxt);
				}
				read.close();
			} else {
            	String log = logArea.getText();
            	log = log +"\n"+ "找不到日志文件："+file.getPath();
            	logArea.setText(log);
			}
		} catch (Exception e) {
			String log = logArea.getText();
        	log = log +"\n"+ "读取日志文件出错！日志路径："+filePath;
        	logArea.setText(log);
			e.printStackTrace();
		}
		return resultList;
	}

}
