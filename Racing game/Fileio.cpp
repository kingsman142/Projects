#include <iostream>
#include "stdafx.h"
#include <SFML/Graphics.hpp>
#include <fstream>
//#include "libpng-1.6.20/png.h"

using namespace sf;
using namespace std;

class FileIO{
	private:
		fstream file;

	public:
		FileIO(string);
		int readImage();
};

FileIO::FileIO(string filename){
	file.open(filename, ios::binary);
}

//Read the image and store it in an array
int FileIO::readImage(){
	int storedValues[25][25];

	string line;
	if(file.is_open()){
		while(getline(file, line)){
			cout << line << " 1" << endl;
		}
	}

	if(file.is_open()) cout<<"open"<<endl;
	else cout<<"closed"<<endl;
	
	file.close();

	return 0;
}