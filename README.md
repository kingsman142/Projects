# Projects

## Greyscaler

This repo contains my greyscale program, bitmaps.java - BinaryOutput.png and GreyOutput.png are the two output files associated with the program for the user to see sample output.  Also associated with the greyscale project are: cat.jpg, city.jpg, sunset.jpg, up.jpg, leaf.jpg, all of which are sample images for the user to test with.  However, any image can be used as long as it's in the same directory as bitmaps.java. The last two files greyValues.bmp and bitmap.bmp store the grey values and binary values respectively of the images.

## Racing Game

A game where you can drive around a racetrack with a racecar.  Written in C++, it utilizes the SFML 2.3.2 library for graphics.  In order to be ran, the respective files must be put into a project and linked correctly. NOT FINISHED

## Random Terrain

Generates random terrain and displays it to the user in a JFrame.  Currently, the RandomTerrain.java file generates terrain using the midpoint displacement algorithm, so the terrain is one-dimensional. Its corresponding examples can be seen through MidpointDisplacementExamples.png.  In DiamondSquare.java, terrain is generated using the diamond-square algorithm, so the terrain is outputted to a two-dimensional bitmap but can easily be translated to 3 dimensions.  Its corresponding examples are stored in DiamondSquareExamples.png, with each heightmap being 512x512.

## Networking

Contains two files, IRCClient.java and IRCServer.java.  First, run the server, and then the client.  A one way connection works where the client can send messages to the server and it will display the messages but the server won't respond to the client.  Make sure they're both on the same point using command-line arguments.  I was trying to make an IRC (Internet Relay Chat), but I can't figure out MultiCastSockets right now so I'm delaying this project.

## LWJGL-PC

This project is perhaps the most satisfying of the ones here.  It utilizes LWJGL (a java binding for OpenGL), with OpenGL shaders v3.30 Core to generate 3D random terrain.  In order to generate the terrain, I used my DiamondSquare.java file from the "Random Terrain" project to generate the values for each point.  Currently, using the diamond-square algorithm, the terrain is size 512x512.  With a few modifications, the terrain can not only show the grey value of each point, but can also show if the value should be at grass/ground level, hill level, or mountain top level (0-.33 = ground, .33-.66 = hill, .66-1.00 = mountain top) using green, grey, and white.

## LWJGL

My 3D random terrain project; similar to the early stages of LWJGL-PC.  However, I believe OSX and Windows have different OpenGL drivers so they don't run identically.  I might have to double check that.

## Videos

My archive of videos from projects.  Currently, there is only one video, and it is for the LWJGL-PC project to show users how the program looks with multiple tests and a couple of modifications.
