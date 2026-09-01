import javax.swing.*;

import java.net.*;

import java.awt.image.*;

import javax.imageio.*;

import java.io.*;



public class Clienthttp {

    public static void main(String args[]) {

        Socket soc = null;

        BufferedImage img = null;

        try {

            System.out.println("Connecting to HTTP Server on localhost:387...");

            soc = new Socket("localhost", 387);

            System.out.println("Client is running");



            File imgFile = new File("network.jpg");

            if (!imgFile.exists()) {

                System.out.println("Warning: network.jpg not found. Creating test image...");

                BufferedImage testImg = new BufferedImage(320, 240, BufferedImage.TYPE_INT_RGB);

                java.awt.Graphics2D g = testImg.createGraphics();

                g.setColor(java.awt.Color.BLUE);

                g.fillRect(0, 0, 320, 240);

                g.setColor(java.awt.Color.WHITE);

                g.drawString("Networks-Commands-Deeply - Networks Lab", 60, 110);

                g.drawString("HTTP Image Transfer Demo", 50, 140);

                g.dispose();

                ImageIO.write(testImg, "jpg", imgFile);

            }



            System.out.println("Reading image from disk: " + imgFile.getAbsolutePath());

            img = ImageIO.read(imgFile);



            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ImageIO.write(img, "jpg", baos);

            baos.flush();



            byte[] bytes = baos.toByteArray();

            baos.close();



            System.out.println("Sending image to server (" + bytes.length + " bytes)...");



            OutputStream out = soc.getOutputStream();

            DataOutputStream dos = new DataOutputStream(out);



            dos.writeInt(bytes.length);

            dos.write(bytes, 0, bytes.length);

            dos.flush();



            System.out.println("Image sent successfully");



            dos.close();

            out.close();

        } catch (Exception e) {

            System.out.println("Exception: " + e.getMessage());

        } finally {

            if (soc != null && !soc.isClosed()) {

                try {

                    soc.close();

                } catch (IOException ignored) {}

            }

        }

    }

}

