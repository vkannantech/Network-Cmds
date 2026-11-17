import java.net.*;

import java.io.*;

import java.awt.image.*;

import javax.imageio.*;

import javax.swing.*;



public class Serverhttp {

    public static void main(String args[]) {

        ServerSocket server = null;

        Socket socket = null;

        try {

            server = new ServerSocket(387);

            System.out.println("HTTP Server listening on TCP port 387...");

            System.out.println("Server waiting for image");



            socket = server.accept();

            System.out.println("Client connected: " + socket.getRemoteSocketAddress());



            InputStream in = socket.getInputStream();

            DataInputStream dis = new DataInputStream(in);



            int len = dis.readInt();

            System.out.println("Image Size: " + (len / 1024) + " KB (" + len + " bytes)");



            byte data[] = new byte[len];

            dis.readFully(data);



            InputStream ian = new ByteArrayInputStream(data);

            BufferedImage bImage = ImageIO.read(ian);



            if (bImage != null) {

                JFrame f = new JFrame("Server - Received Image (Networks-Commands-Deeply)");

                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                ImageIcon icon = new ImageIcon(bImage);

                JLabel l = new JLabel();

                l.setIcon(icon);

                f.add(l);

                f.pack();

                f.setLocationRelativeTo(null);

                f.setVisible(true);

                System.out.println("Image rendered successfully in Swing Window.");

            } else {

                System.out.println("Warning: Received byte stream could not be decoded as image.");

            }



            dis.close();

            in.close();

        } catch (Exception e) {

            System.out.println("Exception: " + e.getMessage());

        } finally {

            try {

                if (socket != null && !socket.isClosed()) socket.close();

                if (server != null && !server.isClosed()) server.close();

            } catch (IOException ignored) {}

        }

    }

}

