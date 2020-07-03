package com.cnnc.random;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class Test01 {

    public static void main(String[] args) throws IOException {
        FileChannel channel = null;

        FileInputStream fis = null;

        FileOutputStream fos = null;

        try {
            RandomAccessFile file = new RandomAccessFile("E:\\1.txt", "rw");

//            file.write("123456789\n".getBytes());

//            file.seek(10);

            byte[] bytes = "A".getBytes();
            for (int i = 0; i < bytes.length; i++) {
                System.out.println(bytes[i]);
            }


            ByteBuffer buffer = ByteBuffer.allocate(1024);

            channel = file.getChannel();
            int read = channel.read(buffer);
            System.out.println(buffer);
            buffer.flip();

            while (true) {
                buffer.flip();
                System.out.println(new String(new byte[]{buffer.get()}));
                buffer.compact();
                System.out.println(buffer);
            }

//            file.write("987654321".getBytes());

//            ByteBuffer direct = ByteBuffer.allocateDirect(10);
//
//            direct.put("123456789\n".getBytes());
//
//            channel = file.getChannel();
//
//            MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 512);
//
//            while (true) {
//                map.put(direct);
//                System.out.println("map ----- write  ");
//                System.in.read();
//            }


//
//            byte[] bytes = "abc".getBytes();
//
//            ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
//
//            byteBuffer.put(bytes);
//
//            channel.write(byteBuffer);

//            fis = new FileInputStream(file.getFD());
//            byte[] b = new byte[1];
//            while (fis.read(b) > 0) {
//                System.out.print(new String(b));
//            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            channel.close();
//            fis.close();
//            fos.close();
        }
    }
}
