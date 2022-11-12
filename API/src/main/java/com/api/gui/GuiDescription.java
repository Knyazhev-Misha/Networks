package com.api.gui;

import com.api.Json.JsonDescription.Description;
import com.api.request.RequestDescription;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GuiDescription {
    private JFrame mainFrame;
    private JPanel mainPanel;
    private JPanel descriptionPanel;
    private JPanel backPanel;

    private String id;
    private JFrame placeFrame;
    ExecutorService executor = Executors.newFixedThreadPool(10);

    public GuiDescription(String id, JFrame placeFrame){
        this.id = id;
        this.placeFrame = placeFrame;

        createPanel();
        createFrame();
        createBack();
        createDescription();
    }

    private void createPanel(){
        Runnable task = () -> {
            mainPanel = new JPanel(new BorderLayout());
            mainPanel.setPreferredSize(new Dimension(placeFrame.getWidth(), placeFrame.getHeight()));

            descriptionPanel = new JPanel(new BorderLayout());

            backPanel = new JPanel(new BorderLayout());
        };
        SwingUtilities.invokeLater(task);
    }

    private void createFrame(){
        Runnable task = () -> {
            mainFrame = new JFrame();
            mainFrame.add(mainPanel);
            mainFrame.setTitle("Description");
            mainFrame.pack();
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            mainFrame.setLocation(placeFrame.getLocation());
            mainFrame.setVisible(true);
        };
        SwingUtilities.invokeLater(task);
    }

    private void createDescription(){
        RequestDescription requestDescription = new RequestDescription();

        CompletableFuture
                .supplyAsync(() -> requestDescription.request(id),executor)
                .thenAcceptAsync(taskResult -> createLabelDescription(taskResult), executor);
    }

    private void createLabelDescription(Description description){
        JLabel descriptionLabel = new JLabel();
        if (description != null && !description.isEmpty()) {
            Runnable task = () -> {
                JTextArea text = new JTextArea();
                text.setLineWrap(true);
                text.setEditable(false);
                text.setText(description.getDescription());

               // descriptionLabel.add(text);
                descriptionPanel.add(text, BorderLayout.NORTH);


                Image image = null;
                URL url = null;
                try {
                    url = new URL(description.getImage());
                    image = ImageIO.read(url);
                    ImageIcon icon = new ImageIcon(image);
                    JLabel picture = new JLabel(icon);
                    descriptionPanel.add(picture, BorderLayout.CENTER);
                } catch (MalformedURLException e) {
                    descriptionLabel.setText(description.getDescription() + "\n\n Can't get source");
                } catch (IOException e) {
                    descriptionLabel.setText(description.getDescription() + "\n\n Can't get source" +
                            "\n\n Can't get image" );
                }

                mainPanel.add(descriptionPanel, BorderLayout.CENTER);
                mainFrame.revalidate();
                mainFrame.repaint();
                SwingUtilities.updateComponentTreeUI(mainFrame);
            };
            SwingUtilities.invokeLater(task);
        } else {
            Runnable task = () -> {
                descriptionLabel.setText("Error");
                descriptionPanel.add(descriptionLabel);
                mainPanel.add(descriptionLabel, BorderLayout.CENTER);
                mainFrame.revalidate();
                mainFrame.repaint();
                SwingUtilities.updateComponentTreeUI(mainFrame);
            };
            SwingUtilities.invokeLater(task);
        }
    }

    private void createBack(){
        JButton back = new JButton("←");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Runnable task = () -> {
                    mainFrame.removeAll();
                    mainFrame.setVisible(false);
                    mainFrame.dispose();
                    executor.shutdownNow();

                    placeFrame.setSize(mainFrame.getWidth(), mainFrame.getHeight());
                    placeFrame.setLocation(mainFrame.getLocation());
                    placeFrame.setVisible(true);
                };

                SwingUtilities.invokeLater(task);
            }
        });

        Runnable task = () -> {
            backPanel.add(back, BorderLayout.CENTER);
            mainPanel.add(backPanel, BorderLayout.SOUTH);
            mainFrame.revalidate();
            mainFrame.repaint();
            mainFrame.setVisible(true);
            SwingUtilities.updateComponentTreeUI(mainFrame);
        };

        SwingUtilities.invokeLater(task);
    }
}
