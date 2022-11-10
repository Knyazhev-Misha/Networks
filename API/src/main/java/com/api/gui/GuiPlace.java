package com.api.gui;

import com.api.JsonPlace.Place;
import com.api.request.RequestPlace;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GuiPlace {
    private JFrame mainFrame;

    private JPanel mainPanel;
    private JPanel answerPanel;
    private JPanel searchPanel;


    private JTextField searchField;
    private JScrollPane listScroller;

    ExecutorService executor = Executors.newFixedThreadPool(10);

    public GuiPlace(){
        createPanel();
        createSearchField();
        createScroll();
        createFrame();
    }

    private void createSearchField(){
        searchField = new JTextField();

        searchPanel.add(searchField, BorderLayout.CENTER);
        mainPanel.add(searchField, BorderLayout.NORTH);

          searchField.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                  RequestPlace requestPlace = new RequestPlace();

                  CompletableFuture
                          .supplyAsync(() -> requestPlace.request(searchField.getText()),executor)
                          .thenAcceptAsync(taskResult -> redrawing(taskResult), executor);
              }
          });
    }

    private void createPanel(){

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setPreferredSize( new Dimension(500, 500));

        answerPanel = new JPanel(new VerticalLayout());

        searchPanel = new JPanel(new BorderLayout());
    }

    private  void createWeatherButton(JButton weatherButton, String lat, String lng){
        weatherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.setVisible(false);
                GuiWeather guiWeather = new GuiWeather(lat, lng, mainFrame);
            }
        });
    }

    private void createFrame(){
        mainFrame = new JFrame();
        mainFrame.add(mainPanel);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setTitle("Sercher");
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    public void createScroll(){
        listScroller = new JScrollPane(answerPanel);
        listScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        listScroller.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(listScroller, BorderLayout.CENTER);
    }

    private void redrawing(Place places){
        if(places != null) {
            Runnable task = () -> {
                answerPanel.removeAll();

                for (int i = 0; i < places.getLength(); i += 1){

                    JLabel answerLabel = new JLabel();
                    JButton weather = new JButton("weather");
                    JPanel answerPane = new JPanel();

                    createWeatherButton(weather, places.getLat(i), places.getLng(i));
                    answerLabel.setText(places.getPlace(i));
                    answerLabel.setBorder(BorderFactory.createTitledBorder(places.getName(i)));

                    answerPane.add(answerLabel);
                    answerPane.add(weather);
                    answerPanel.add(answerPane);
                    listScroller.revalidate();
                }

                mainFrame.revalidate();
                mainFrame.repaint();
                mainFrame.setVisible(true);
                SwingUtilities.updateComponentTreeUI(mainFrame);

                searchField.setText(searchField.getText());
            };
            SwingUtilities.invokeLater(task);
        }

        else {
            Runnable task = () -> {
                JLabel answerLabel = new JLabel();
                               JPanel answerPane = new JPanel();

                answerLabel.setText("<html>lError <br />try again");
                answerPane.add(answerLabel);

                answerPanel.add(answerPane);
                listScroller.revalidate();

                mainFrame.revalidate();
                mainFrame.repaint();
                mainFrame.setVisible(true);
                SwingUtilities.updateComponentTreeUI(mainFrame);

                searchField.setText(searchField.getText());
            };
            SwingUtilities.invokeLater(task);
        }

    }
}
