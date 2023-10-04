/**
 * 
 */
/**
 * 
 */
module CandyFactoryOrdersApp {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires amqp.client;
	requires java.sql;
	requires java.logging;
	exports mdp.candyfactory.order.main;
	exports mdp.candyfactory.order.controllers;
	opens mdp.candyfactory.order.controllers;
}