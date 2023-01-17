package com.api.gui;

import com.api.Json.JsonInteresting.Interesting;
import com.api.request.RequestInteresting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GuiInteresting {
    private JFrame mainFrame;
    private JPanel mainPanel;
    private JPanel interestingPanel;
    private JPanel backPanel;
    private JScrollPane listScroller;

    private String lat;
    private String lng;
    private JFrame placeFrame;
    ExecutorService executor = Executors.newFixedThreadPool(10);

    public GuiInteresting(String lat, String lng, JFrame placeFrame){
        this.lat = lat;
        this.lng = lng;
        this.placeFrame = placeFrame;

        createPanel();
        createFrame();
        createBack();
        createScroll();
        createInteresting();
    }
    private void createPanel(){

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setPreferredSize( new Dimension(placeFrame.getWidth(), placeFrame.getHeight()));

        interestingPanel = new JPanel(new VerticalLayout());

        backPanel = new JPanel(new BorderLayout());
    }

    private void createFrame(){
        Runnable task = () -> {
            mainFrame = new JFrame();
            mainFrame.add(mainPanel);
            mainFrame.setTitle("Interesting Place");
            mainFrame.pack();
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            mainFrame.setLocation(placeFrame.getLocation());
            mainFrame.setVisible(true);
        };
        SwingUtilities.invokeLater(task);
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

    public void createScroll(){
        Runnable task = () -> {
            listScroller = new JScrollPane(interestingPanel);
            listScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            listScroller.setBorder(BorderFactory.createEmptyBorder());
            mainPanel.add(listScroller, BorderLayout.CENTER);
        };
        SwingUtilities.invokeLater(task);
    }

    private void createInteresting(){
        RequestInteresting requestInteresting = new RequestInteresting();

        CompletableFuture
                .supplyAsync(() -> requestInteresting.request(lat, lng),executor)
                .thenAcceptAsync(taskResult -> createLabelInteresting(taskResult), executor);
    }

    private void createDescription(JButton description, String id) {
        description.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.setVisible(false);
                GuiDescription guiDescription = new GuiDescription(id, mainFrame);
            }
        });
    }

    private void createLabelInteresting(Interesting interesting){
        if(interesting != null){
            Runnable task = () -> {

                for (int i = 0; i < interesting.getLength(); i += 1){
                    if(!interesting.isEmpty(i)) {
                        JLabel interestingLabel = new JLabel();
                        JPanel interestingPane = new JPanel();
                        JButton description = new JButton("description");

                        createDescription(description, interesting.getId(i));

                        interestingLabel.setText(interesting.getInteresting(i));
                        interestingLabel.setBorder(BorderFactory.createTitledBorder(interesting.getName(i)));

                        interestingPane.add(interestingLabel);
                        interestingPane.add(description);

                        interestingPanel.add(interestingPane);
                        listScroller.revalidate();
                    }
                }

                mainFrame.revalidate();
                mainFrame.repaint();
                mainFrame.setVisible(true);
                SwingUtilities.updateComponentTreeUI(mainFrame);
            };
            SwingUtilities.invokeLater(task);
        }

        else {
            Runnable task = () -> {
                JLabel answerLabel = new JLabel();
                JPanel answerPane = new JPanel();

                answerLabel.setText("<html>lError <br />try again");
                answerPane.add(answerLabel);

                interestingPanel.add(answerPane);
                listScroller.revalidate();

                mainFrame.revalidate();
                mainFrame.repaint();
                mainFrame.setVisible(true);
                SwingUtilities.updateComponentTreeUI(mainFrame);
            };
            SwingUtilities.invokeLater(task);
        }
    }
}
