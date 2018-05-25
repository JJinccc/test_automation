package com.ffan.qa.utils;

import java.io.*;

public class FileUtil {

    //文件路径+名称
    private static String filenameTemp;

    /**
     * 创建文件
     *
     * @param filePath 文件名称
     * @return 是否创建成功，成功则返回true
     */
    public static boolean createFile(String filePath) {
        Boolean bool = false;
        filenameTemp = filePath;//文件路径+名称+文件类型
        File file = new File(filenameTemp);
        try {
            //如果文件不存在，则创建新的文件
            if (!file.exists()) {
                file.createNewFile();
                bool = true;
                System.out.println("success create file,the file is " + filenameTemp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bool;
    }

    /**
     * 向文件中写入内容
     *
     * @param filepath 文件路径与名称
     * @param newstr   写入的内容
     * @return
     * @throws IOException
     */
    public static boolean writeFileContent(String filepath, String newstr) throws IOException {
        Boolean bool = false;
        String filein = newstr + "\n";//新写入的行，换行
        String temp = "";

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
            File file = new File(filepath);//文件路径(包括文件名称)
            //将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();

            //文件原有内容
            for (int i = 0; (temp = br.readLine()) != null; i++) {
                buffer.append(temp);
                // 行与行之间的分隔符 相当于“\n”
                buffer = buffer.append(System.getProperty("line.separator"));
            }
            buffer.append(filein);

            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buffer.toString().toCharArray());
            pw.flush();
            bool = true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            //不要忘记关闭
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return bool;
    }

    /**
     * 删除文件
     *
     * @param filePath 文件名称
     * @return
     */
    public static boolean delFile(String filePath) {
        Boolean bool = false;
        filenameTemp = filePath;
        File file = new File(filenameTemp);
        try {
            if (file.exists()) {
                file.delete();
                bool = true;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return bool;
    }

    /**
     * 读取文件
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static String readFileContent(String path) throws IOException {
        File file = new File(path);
        if (!file.exists() || file.isDirectory())
            throw new FileNotFoundException();
        FileInputStream fis = new FileInputStream(file);
        byte[] buf = new byte[1024];
        StringBuffer sb = new StringBuffer();
        while ((fis.read(buf)) != -1) {
            sb.append(new String(buf));
            buf = new byte[1024];//重新生成，避免和上次读取的数据重复
        }
        return sb.toString();
    }

    /**
     * 复制文件
     *
     * @param src
     * @param dest
     * @throws IOException
     */
    public static void copyFile(String src, String dest) throws IOException {
        FileInputStream in = new FileInputStream(src);
        File file = new File(dest);
        if (!file.exists())
            file.createNewFile();
        FileOutputStream out = new FileOutputStream(file);
        int c;
        byte buffer[] = new byte[1024];
        while ((c = in.read(buffer)) != -1) {
            for (int i = 0; i < c; i++)
                out.write(buffer[i]);
        }
        in.close();
        out.close();
    }

    /**
     * 文件是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean exists(String filePath) {
        return new File(filePath).exists();
    }

    /**
     * 创建文件夹
     *
     * @param path
     */
    public static void createDir(String path) {
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdir();
    }

    /**
     * 删除目录
     *
     * @param path
     */
    public static void delDir(String path) {
        File dir = new File(path);
        if (dir.exists()) {
            File[] tmp = dir.listFiles();
            for (int i = 0; i < tmp.length; i++) {
                if (tmp[i].isDirectory()) {
                    delDir(path + "/" + tmp[i].getName());
                } else {
                    tmp[i].delete();
                }
            }
            dir.delete();
        }
    }

    /**
     * 复制目录
     * @param src
     * @param des
     * @throws Exception
     */
    public static void copyDir(String src, String des) throws Exception {
        //初始化文件复制
        File file1 = new File(src);
        //把文件里面内容放进数组
        File[] fs = file1.listFiles();
        //初始化文件粘贴
        File file2 = new File(des);
        //判断是否有这个文件有不管没有创建
        if (!file2.exists()) {
            file2.mkdirs();
        }
        //遍历文件及文件夹
        for (File f : fs) {
            if (f.isFile()) {
                //文件
                copyFile(f.getPath(), des + File.separator + f.getName()); //调用文件拷贝的方法
            } else if (f.isDirectory()) {
                //文件夹
                copyDir(f.getPath(), des + File.separator + f.getName());//继续调用复制方法      递归的地方,自己调用自己的方法,就可以复制文件夹的文件夹了
            }
        }
    }

    /**
     * 生成路径
     * @param path
     * @param parts
     * @return
     */
    public static String pathCombine(String path, String... parts) {
        String newPath = path;
        for (String part:
             parts) {
            newPath += File.separator + part;
        }
        return newPath;
    }
}
