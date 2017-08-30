package com.jspeedbox.utils.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.jspeedbox.utils.IOUtils;

public class ZipUtil {

    private List <String> fileList = null;
    private static String OUTPUT_ZIP_FILE = null;
    private static File SOURCE_FOLDER = null;
    private static File ARCHIVE_FOLDER = null;// SourceFolder path

    public ZipUtil(File sourceFolder, File archiveFolder) throws Exception {
    	SOURCE_FOLDER = sourceFolder;
    	ARCHIVE_FOLDER = archiveFolder;
    	if(SOURCE_FOLDER==null||SOURCE_FOLDER.equals("")){
    		throw new Exception("No source folder defined");
    	}
    	if(!IOUtils.directoryExists(SOURCE_FOLDER)){
    		throw new Exception("directory["+SOURCE_FOLDER+"] does not exist");
    	}
    	if(ARCHIVE_FOLDER==null||ARCHIVE_FOLDER.equals("")){
    		throw new Exception("No archive folder defined");
    	}
        fileList = new ArrayList<String>();
        initNewFileName();
        generateFileList(SOURCE_FOLDER);
    }
    
    private void initNewFileName() throws Exception{
    	StringBuffer buffer = new StringBuffer();
    	buffer.append(ARCHIVE_FOLDER.getAbsolutePath()).append("/");
    	buffer.append("archive-").append(System.currentTimeMillis()).append(".zip");
    	OUTPUT_ZIP_FILE = buffer.toString();
    }

    public void zipIt() throws Exception {
    	if(OUTPUT_ZIP_FILE==null||OUTPUT_ZIP_FILE.equals("")){
    		throw new Exception("No output zip file name defined");
    	}
    	
        byte[] buffer = new byte[1024];
        String source = SOURCE_FOLDER.getName();
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(OUTPUT_ZIP_FILE);
            zos = new ZipOutputStream(fos);

            FileInputStream in = null;

            for (String file: this.fileList) {
                ZipEntry ze = new ZipEntry(source + File.separator + file);
                zos.putNextEntry(ze);
                try {
                    in = new FileInputStream(SOURCE_FOLDER + File.separator + file);
                    int len;
                    while ((len = in .read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                } finally {
                    in.close();
                }
            }

            zos.closeEntry();

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void generateFileList(File node) throws Exception {
        // add file only
        if (node.isFile()) {
            fileList.add(generateZipEntry(node.toString()));
        }

        if (node.isDirectory()) {
            String[] subNote = node.list();
            for (String filename: subNote) {
                generateFileList(new File(node, filename));
            }
        }
    }

    private String generateZipEntry(String file) {
        return file.substring(SOURCE_FOLDER.getAbsolutePath().length() + 1, file.length());
    }
}