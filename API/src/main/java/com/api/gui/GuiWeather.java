package com.api.gui;

import com.api.Json.JsonWeather.Weath;
import com.api.request.RequestWeather;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GuiWeather {
   private JFrame mainFrame;
   private JPanel mainPanel;
   private JPanel weatherPanel;
   private JPanel backPanel;

    private String lat;
    private String lng;
    private JFrame placeFrame;
    ExecutorService executor = Executors.newFixedThreadPool(10);

    public GuiWeather(String lat, String lng, JFrame placeFrame){
       this.lat = lat;
       this.lng = lng;
       this.placeFrame = placeFrame;

       createPanel();
       createFrame();
       createWeather();
       createBack();

    }

    private void createPanel(){
        Runnable task = () -> {
            mainPanel = new JPanel(new BorderLayout());
            mainPanel.setPreferredSize(new Dimension(placeFrame.getWidth(), placeFrame.getHeight()));

            weatherPanel = new JPanel(new BorderLayout());

            backPanel = new JPanel(new BorderLayout());
        };
        SwingUtilities.invokeLater(task);
    }

    private void createFrame(){
        Runnable task = () -> {
            mainFrame = new JFrame();
            mainFrame.add(mainPanel);
            mainFrame.setTitle("Weather");
            mainFrame.pack();
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            mainFrame.setLocation(placeFrame.getLocation());
            mainFrame.setVisible(true);
        };
        SwingUtilities.invokeLater(task);
    }

    private void createWeather(){
        RequestWeather requestWeather = new RequestWeather();

        CompletableFuture
                .supplyAsync(() -> requestWeather.request(lat, lng),executor)
                .thenAcceptAsync(taskResult -> createLabelWeather(taskResult), executor);
    }

    private void createLabelWeather(Weath weath){
            JLabel weatherLabel = new JLabel();
            if (weath != null) {
                Runnable task = () -> {
                    weatherLabel.setText(weath.getWeather());
                    weatherPanel.add(weatherLabel, BorderLayout.CENTER);
                    mainPanel.add(weatherPanel, BorderLayout.CENTER);
                    mainFrame.revalidate();
                    mainFrame.repaint();
                    SwingUtilities.updateComponentTreeUI(mainFrame);
                };
                SwingUtilities.invokeLater(task);
            } else {
                Runnable task = () -> {
                    weatherLabel.setText("Error");
                    weatherPanel.add(weatherLabel, BorderLayout.CENTER);
                    mainPanel.add(weatherPanel, BorderLayout.CENTER);
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
