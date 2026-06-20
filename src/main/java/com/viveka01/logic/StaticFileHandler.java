package com.viveka01.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import com.viveka01.config.PropReader;
import com.viveka01.format.ContentType;
import com.viveka01.format.*;

public class StaticFileHandler {
    private static final String frontEndDir = FilePathHandler.getAbsolutePath(PropReader.getInstance().getProperty("static-file.storage-location"));
    private static final String fileNotFoundPath = FilePathHandler.getAbsolutePath(PropReader.getInstance().getProperty("static-file.file-not-found-storage-location"));
    private static final String homePage = PropReader.getInstance().getProperty("static-file.homepage-storage-location");
    private static final String favicon = PropReader.getInstance().getProperty("static-file.favicon-storage-location");;
    /**
     * @param request request object containing URL to page required
     *                if page isnt found then fileNotFound returned
     *                If any server error happens the server error response is given
     *                else actuall file is sent over
     */
    public static Response getFrontEndPage(Request request) {
        String path = request.getPath();
        if (path.equals("/")) {
            path = homePage;
        }
        if (path.equals("/favicon.ico")){
            path = favicon;
        }
        String filePath;
        try {
            filePath = Paths.get(frontEndDir, path).toString();
        } catch (InvalidPathException e) {
            filePath = fileNotFoundPath;
        }

        System.out.printf("Retrieving file: %s\n", filePath);
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("File doesnt exist");
            return Response.SERVER_ERROR;
        }

        String fileType = filePath.split("\\.")[1].toUpperCase();
        try {
            Response response = new Response(200, ContentType.valueOf(fileType), getFileBytes(filePath));
            return response;
        } catch (IOException e) {
            System.out.println("Failed to get bytes of file, path: "+filePath);
            return Response.SERVER_ERROR;
        }
    }

    /**
     * @param filePath path to file in String
     *                 returns byte array of file specified
     */
    public static byte[] getFileBytes(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream readFile = new FileInputStream(file);
        byte[] body = new byte[readFile.available()];
        readFile.read(body);
        readFile.close();
        return body;
    }
}
