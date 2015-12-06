#include "stdafx.h"
#include <SFML/Graphics.hpp>
#include <iostream>

using namespace std;
using namespace sf;

class Block : public RectangleShape, public FloatRect {
private:

public:
	Vector2f minCoord;
	Vector2f maxCoord;
	Block(double, double);
	void updateRect();
};

Block::Block(double Width, double Height) {
	float newWidth = (float) Width;
	float newHeight = (float) Height;

	setSize(Vector2f(newWidth, newHeight));
	setFillColor(Color(255, 0, 0, 100));
	setPosition(100, 200);

	width = Width;
	height = Height;

	updateRect();
}

void Block::updateRect() {
	left = getPosition().x;
	top = getPosition().y;
}