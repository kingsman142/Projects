#include "stdafx.h"
#include <SFML/Graphics.hpp>
#include <iostream>
#include <fstream>
#include <math.h>
#include <vector>

#include "Racecar.cpp"
#include "Block.cpp"
#include "FileIO.cpp"

#define PI 3.14159265
#define FRICTION .02

using namespace std;
using namespace sf;

const int HEIGHT = 500;
const int WIDTH = 1000;

void manageInput(Racecar&);
bool checkCollision(Racecar&, vector<Block>);
vector<Block> convertArrayToVector(int[][25]);

//THIS IS THE RACETRACK MAP
int map[][25] = {  { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				   { 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0 },
				   { 0, 0, 0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0, 0 },
				   { 0, 0, 0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0, 0 },
				   { 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0, 0 },
				   { 0, 1, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 1, 0, 0, 0, 0 },
				   { 0, 1, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 1, 0, 0, 0, 0 },
				   { 0, 1, 2, 2, 2, 2, 2, 2, 2, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0, 0 },
				   { 0, 1, 2, 2, 2, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0, 0 },
				   { 0, 1, 2, 2, 2, 1, 0, 0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 0, 0, 0, 0 },
				   { 0, 1, 2, 2, 2, 1, 0, 0, 0, 0, 1, 2, 2, 2, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0 },
				   { 0, 1, 2, 2, 2, 1, 0, 0, 0, 0, 1, 2, 2, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				   { 0, 1, 2, 2, 2, 1, 0, 0, 0, 0, 1, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 },
				   { 0, 1, 2, 2, 2, 1, 0, 0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0 },
				   { 0, 1, 2, 2, 2, 1, 0, 0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0 },
				   { 0, 1, 2, 2, 2, 1, 0, 0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0 },
				   { 0, 1, 2, 2, 2, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 1, 0 },
				   { 0, 1, 2, 2, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 2, 2, 1, 0 },
				   { 0, 1, 2, 2, 2, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 1, 0 },
				   { 0, 1, 2, 2, 2, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0 },
				   { 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0 },
				   { 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0 },
				   { 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 2, 2, 2, 2, 2, 2, 1, 0 },
				   { 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0 },
				   { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, };

int main()
{
	RenderWindow window(VideoMode(WIDTH, HEIGHT), "SFML works!");
	window.setFramerateLimit(60);

	//CREATE THE RACECAR
	Racecar racecar(WIDTH, HEIGHT);
	Block block(100.0, 100.0);

	//CREATE ALL TEXTS FOR DEBUGGING
	Font system;
	system.loadFromFile("Retro Computer_DEMO.ttf");
	Text velocity("Velocity: 0", system, 15);
	Text rotation("Rotation: 0", system, 15);
	rotation.setPosition(0, 20);
	Text collision("Collision: ", system, 15);
	collision.setPosition(0, 40);

	//DECLARE SOME VARIABLES
	RectangleShape racecarBox;
	vector<Block> blocks;
	//blocks.push_back(block);
	blocks = convertArrayToVector(::map);

	FileIO mapFile("map.png");
	mapFile.readImage();

	while (window.isOpen())
	{
		racecar.setOrigin(racecar.getLocalBounds().left + racecar.getTexture().getSize().x / 2.0f, racecar.getLocalBounds().top + racecar.getTexture().getSize().y / 2.0f);

		double currentAngle = (double) racecar.getRotation() + 90.0;
		double xMovement = cos(currentAngle*PI / 180.0) * (float) -racecar.getVelocity();
		double yMovement = sin(currentAngle*PI / 180.0) * (float) -racecar.getVelocity();

		Event event;

		while (window.pollEvent(event))
		{
			switch (event.type) {
				case Event::Closed:
					window.close();
					break;
				default:
					break;
			}
		}

		//ADD FRICTION TO THE MOVEMENT OF THE CAR, AND HANDLE ALL OF THE OTHER INPUT OF THE CAR
		if(racecar.getVelocity() > 0) racecar.updateVelocity(-FRICTION);
		else if(racecar.getVelocity() < 0) racecar.updateVelocity(FRICTION);
		//if(racecar.getVelocity() <= -.1 && racecar.getVelocity() >= .1) racecar.updateVelocity(FRICTION);
		manageInput(racecar);
		racecar.move((float) xMovement, (float) yMovement);

		//PRINT OUT THE DEBUGGING STATS TO THE SCREEN
		velocity.setString("Velocity: " + to_string(racecar.getVelocity()));
		rotation.setString("Rotation: " + to_string(racecar.getRotation()));
		if(checkCollision(racecar, blocks)){
			collision.setString("Collision: YES");
			collision.setColor(Color::Green);
		} else{
			collision.setString("Collision: NO");
			collision.setColor(Color::Red);
		}

		//THIS WOULD BE THE HITBOX OF THE RACECAR IN AN IDEAL WORLD
		/*racecarBox.setPosition(racecar.getPosition().x, racecar.getPosition().y);
		racecarBox.setSize(Vector2f(racecar.width, racecar.height));
		racecarBox.setOutlineColor(Color::Yellow);
		racecarBox.setOutlineThickness(1.0f);
		racecarBox.setFillColor(Color(0, 0, 0, 0));
		racecarBox.setOrigin(racecar.width/2.0, racecar.height/2.0);
		racecarBox.setRotation(racecar.getRotation());*/

		//THIS IS WHAT THE RACECAR'S HITBOX ACTUALLY LOOKS LIKE
		racecarBox.setPosition(racecar.left, racecar.top);
		racecarBox.setSize(Vector2f(racecar.width, racecar.height));
		racecarBox.setOutlineColor(Color(255, 255, 0, 255));
		racecarBox.setOutlineThickness(1.0f);
		racecarBox.setFillColor(Color(0, 0, 0, 0));

		//DRAW EVERYTHING TO THE SCREEN
		window.clear();
		for (Block block : blocks) {
			window.draw(block);
		}
		window.draw(racecar);
		//window.draw(block);
		window.draw(velocity);
		window.draw(rotation);
		window.draw(collision);
		window.draw(racecarBox);
		window.display();
	}

	return 0;
}

void manageInput(Racecar& racecar){
	if (Keyboard::isKeyPressed(Keyboard::W)) {
		if (abs(racecar.getVelocity()) < racecar.getMaxSpeed() || racecar.getVelocity() <= racecar.getMaxSpeed()) {
			racecar.updateVelocity(racecar.getAcceleration());
		}
	}
	else if (Keyboard::isKeyPressed(Keyboard::S)) {
		if (abs(racecar.getVelocity()) < racecar.getMaxSpeed() || racecar.getVelocity() >= racecar.getMaxSpeed()) {
			racecar.updateVelocity(-racecar.getAcceleration());
		}
	}

	if (Keyboard::isKeyPressed(Keyboard::A)) {
		if(racecar.getVelocity() >= 0.0) racecar.rotate(-racecar.getForwardTurn());
		else if(racecar.getVelocity() < 0.0) racecar.rotate(-racecar.getBackwardTurn());
	}
	else if (Keyboard::isKeyPressed(Keyboard::D)) {
		if (racecar.getVelocity() >= 0.0) racecar.rotate(racecar.getForwardTurn());
		else if (racecar.getVelocity() < 0.0) racecar.rotate(racecar.getBackwardTurn());
	}		
}

//CHECK TO SEE IF THE RACECAR COLLIDES WITH ANYTHING
bool checkCollision(Racecar& racecar, vector<Block> blocks){
	racecar.updateRect();
	for(int i = 0; i < blocks.size(); i++){
		blocks.at(i).updateRect();
		if(racecar.intersects(blocks.at(i)) && blocks.at(i).getFillColor() == Color::Red){
			if(racecar.getVelocity() > 0) racecar.updateVelocity(-racecar.getVelocity() - .2);
			//else if(racecar.getVelocity() < 0) racecar.updateVelocity(-racecar.getVelocity() + .2);
			return true;
		}
	}

	return false;
}

//ADD EVERY BLOCK IN THE GAME TO AN ARRAY FOR EASIER COLLISION CHECKING
vector<Block> convertArrayToVector(int map[][25]){
	vector<Block> vectorMap;

	for(int i = 0; i < 25; i++){
		for(int j = 0; j < 25; j++){
			Block block(20, 20);
			block.setPosition(j * 20, i * 20);

			if(map[i][j] == 0){
				block.setFillColor(Color::Green);
				vectorMap.push_back(block);
			} else if(map[i][j] == 1){
				block.setFillColor(Color::Red);
				vectorMap.push_back(block);
			} else if(map[i][j] == 2){
				block.setFillColor(Color::Black);
				vectorMap.push_back(block);
			}
		}
	}
	
	return vectorMap;
}