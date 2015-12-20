#include "stdafx.h"
#include <SFML/Graphics.hpp>
#include <iostream>


using namespace std;
using namespace sf;

#define PI 3.14159265

class Racecar : public Sprite, public FloatRect{
	private:
		const double ACCELERATION = .1;
		const double MAX_SPEED = 3.5;
		const float FORWARD_TURN_RATE = 4;
		const float BACKWARD_TURN_RATE = 2;
		double velocity = 0.0;

		Texture racecarTexture;

	public:
		Racecar(const int, const int);
		double getAcceleration();
		double getMaxSpeed();
		double getVelocity();
		float getForwardTurn();
		float getBackwardTurn();
		Texture getTexture();
		void updateVelocity(double);
		RectangleShape& getBounds();
		void updateRect();

		Vector2f minCoord;
		Vector2f maxCoord;
};

Racecar::Racecar(const int SCREEN_WIDTH, const int SCREEN_HEIGHT){
	racecarTexture.loadFromFile("racecar.png");
	setTexture(racecarTexture);
	setPosition(SCREEN_WIDTH / 2.0f, SCREEN_HEIGHT / 2.0f);
	setScale(.15, .15);
	minCoord.x = getPosition().x - racecarTexture.getSize().x/2;
	minCoord.y = getPosition().y - racecarTexture.getSize().y/2;
	maxCoord.x = getPosition().x + racecarTexture.getSize().x/2;
	maxCoord.y = getPosition().y + racecarTexture.getSize().y/2;

	updateRect();
}

double Racecar::getAcceleration(){
	return ACCELERATION;
}

double Racecar::getMaxSpeed(){
	return MAX_SPEED;
}

double Racecar::getVelocity(){
	return velocity;
}

float Racecar::getForwardTurn(){
	return FORWARD_TURN_RATE;
}

float Racecar::getBackwardTurn(){
	return BACKWARD_TURN_RATE;
}

void Racecar::updateVelocity(double newValue){
	velocity += newValue;
}

void Racecar::updateRect(){
	width = racecarTexture.getSize().x * getScale().x * 1.2;
	height = racecarTexture.getSize().y * getScale().y * .8;
	left = getPosition().x - width / 2.0;
	top = getPosition().y - height / 2.0;
}

Texture Racecar::getTexture(){
	return racecarTexture;
}

RectangleShape& Racecar::getBounds(){
	RectangleShape boundingBox(Vector2f((float) racecarTexture.getSize().x, (float) racecarTexture.getSize().y));
	return boundingBox;
}