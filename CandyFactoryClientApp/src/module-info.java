/**
 * 
 */
/**
 * 
 */
module CandyFactoryClientApp {
	exports mdp.candyfactory.client.main;
	exports mdp.candyfactory.client.controllers;
	requires javafx.controls;
	requires javafx.fxml;
	requires json.simple;
	requires com.google.gson;
	requires amqp.client;
	requires jakarta.xml.bind;
	requires javafx.graphics;
	opens mdp.candyfactory.client.controllers;
	exports mdp.candyfactory.client.model;
	opens mdp.candyfactory.client.model;
}