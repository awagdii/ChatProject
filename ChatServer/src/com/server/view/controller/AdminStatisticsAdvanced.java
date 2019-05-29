/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.view.controller;

import com.server.dao.UserOperations;
import com.server.main.ChatServer;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author Shall
 */
public class AdminStatisticsAdvanced extends AnchorPane {

    /**
     * Initializes the controller class.
     */
    @FXML
    private PieChart pieChart;
    @FXML
    private ScatterChart scatterChart;
    @FXML
    private BarChart barChart, barChartOnlineOfflineUsers;
    @FXML
    private CategoryAxis barCharXAxis, onlineOfflineBarChartXAxis;

    @FXML
    private BubbleChart bubbleChart;
    @FXML
    private Button bStatisticsAdvancedBack;
    @FXML
    private HBox hbMenu;

    public AdminStatisticsAdvanced() {
        try {
            AdminMenu menu = new AdminMenu();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/server/view/cfg/admin_statistics_advanced.fxml"));
            Platform.runLater(() -> {
                hbMenu.getChildren().add(menu);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        updatePieChart();
                        updateOnlineOfflineBarChart();
                        updateBarChart();
                        updateBubbleChart();
                    }
                }, 0, 10000);

                bStatisticsAdvancedBack.setOnAction((ActionEvent event) -> {
                    ChatServer.primaryStage.setScene(new Scene(new AdminStatistics()));
                    ChatServer.primaryStage.setTitle("Admin Statistics Screen");
                    ChatServer.primaryStage.setHeight(670);
                    ChatServer.primaryStage.setWidth(370);

                });
            });
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();

        } catch (IOException ex) {
            Logger.getLogger(AdminLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updatePieChart() {
        UserOperations userOps = new UserOperations();
        ObservableList<PieChart.Data> pieChartData
                = FXCollections.observableArrayList(
                        new PieChart.Data("Online Users", userOps.getNumberOfOnlineUsers()),
                        new PieChart.Data("Offline Users", userOps.getNumberOfOfflineUsers()));
        Platform.runLater(() -> {
            pieChart.setTitle("Online / Offline Users");
            pieChart.setData(pieChartData);
        });

    }

    //---------------- This method to update the Online/Offline Users  Bar Chart ----------------
    public void updateOnlineOfflineBarChart() {
        UserOperations userOps = new UserOperations();
        ObservableList<String> columns = FXCollections.observableArrayList("Online", "Offline");
        onlineOfflineBarChartXAxis.setCategories(columns);
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>(columns.get(0), userOps.getNumberOfOnlineUsers()));
        series.getData().add(new XYChart.Data<>(columns.get(1), userOps.getNumberOfOfflineUsers()));

        //barChart.setBarGap(1.0);
        Platform.runLater(() -> {
            barChartOnlineOfflineUsers.setCategoryGap(1);
            barChartOnlineOfflineUsers.setMaxHeight(100);
            barChartOnlineOfflineUsers.setTitle("Online / Offline Users");
            barChartOnlineOfflineUsers.getData().add(series);
        });

    }

    public void updateBarChart() {
        UserOperations userOps = new UserOperations();
        ObservableList<String> columns = FXCollections.observableArrayList("Male", "Female");
        barCharXAxis.setCategories(columns);
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>(columns.get(0), userOps.getNumberOfMaleUsers()));
        series.getData().add(new XYChart.Data<>(columns.get(1), userOps.getNumberOfFemaleUsers()));

        //barChart.setBarGap(1.0);
        Platform.runLater(() -> {
            barChart.setCategoryGap(1);
            barChart.setMaxHeight(100);
            barChart.setTitle("Malee / Female Users");
            barChart.getData().add(series);
        });

    }

    public void updateBubbleChart() {
        UserOperations userOps = new UserOperations();
        final NumberAxis xAxis = new NumberAxis(1, 53, 4);
        final NumberAxis yAxis = new NumberAxis(0, 80, 10);
        //final BubbleChart<String,Number> blc = new BubbleChart<String,Number>(xAxis,yAxis);

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Egypt");
        series1.getData().add(new XYChart.Data(1, userOps.getNumberOfEgyptianUsers()));

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("USA");
        series2.getData().add(new XYChart.Data(1, userOps.getNumberOfAmericanUsers()));
        Platform.runLater(() -> {
            bubbleChart.setTitle("Countries Users");
            bubbleChart.getXAxis().setLabel("Country");
            bubbleChart.getYAxis().setLabel("Number Of Users");
            bubbleChart.getData().addAll(series1, series2);
        });

    }
}
