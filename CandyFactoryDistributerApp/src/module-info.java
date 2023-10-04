/**
 * 
 */
/**
 * 
 */
module CandyFactoryDistributerApp {
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.controls;
	requires java.rmi;
	exports mdp.candyfactory.distributer.main;
	exports mdp.candyfactory.distributer.controllers;
	opens mdp.candyfactory.distributer.controllers;
	exports mdp.candyfactory.distributer.rmi;
}