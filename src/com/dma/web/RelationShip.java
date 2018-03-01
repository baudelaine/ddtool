/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dma.web;

import java.util.UUID;

/**
 *
 * @author Iqbal
 */
public class RelationShip {

	private String Name;
	private String ParentNamespace = "Model";
	private String querySubject_left;
	private String querySubject_right;
	private String expression;
	private String card_left_min = "zero";
	private String card_left_max = "one";
	private String card_right_min = "zero";
	private String card_right_max = "one";

	public RelationShip(String querySubject_left, String querySubject_right) {
		this.querySubject_left = querySubject_left;
		this.querySubject_right = querySubject_right;
		this.Name = UUID.randomUUID().toString();
		// this.Name = "NNN";

	}

	public String getQuerySubject_left() {
		return querySubject_left;
	}

	public void setQuerySubject_left(String querySubject_left) {
		this.querySubject_left = querySubject_left;
	}

	public String getQuerySubject_right() {
		return querySubject_right;
	}

	public void setQuerySubject_right(String querySubject_right) {
		this.querySubject_right = querySubject_right;
	}

	public String getCard_left_min() {
		return card_left_min;
	}

	public void setCard_left_min(String card_left_min) {
		this.card_left_min = card_left_min;
	}

	public String getCard_left_max() {
		return card_left_max;
	}

	public void setCard_left_max(String card_left_max) {
		this.card_left_max = card_left_max;
	}

	public String getCard_right_min() {
		return card_right_min;
	}

	public void setCard_right_min(String card_right_min) {
		this.card_right_min = card_right_min;
	}

	public String getCard_right_max() {
		return card_right_max;
	}

	public void setCard_right_max(String card_right_max) {
		this.card_right_max = card_right_max;
	}

	public String getParentNamespace() {
		return ParentNamespace;
	}

	public void setParentNamespace(String ParentNamespace) {
		this.ParentNamespace = ParentNamespace;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	// @Override
	 public String toString() {
	 return "RelationShip{" + "Name=" + Name + ", ParentNamespace=" +
	 ParentNamespace + ", querySubject_left=" + querySubject_left +
	 ", querySubject_right=" + querySubject_right + ", expression=" +
	 expression + ", card_left_min=" + card_left_min +
	 ", card_left_max=" + card_left_max + ", card_right_min=" +
	 card_right_min + ", card_right_max=" + card_right_max + '}';
	 }
}
