#version 330 core

layout(location = 0) in vec4 position;
layout(location = 1) in vec4 color;

uniform float time;
uniform float fovX;
uniform float fovY;
uniform float nearZ;
uniform float farZ;
uniform vec3 cameraPosition;
out vec4 vColor;

vec3 cross(vec3 a, vec3 b){
	float c1 = (a[1]*b[2]) - (a[2]*b[1]);
	float c2 = (a[0]*b[2]) - (a[2]*b[0]);
	float c3 = (a[0]*b[1]) - (a[1]*b[0]);
	return vec3(c1, c2, c3);
}

vec3 normalize(vec3 c){
	float length = sqrt((c[0]*c[0]) + (c[1]*c[1]) + (c[2]*c[2]));
	return vec3(c[0]/length, c[1]/length, c[2]/length);
}

//Because this is column-major:
//1, 0, 0, 0,
//0, 1, 0, 0,
//0, 0, 1, 0,
//X, Y, Z, 1
mat4 myTranslatingMatrix = mat4(1, 0, 0, 0,
								0, 1, 0, 0,
								0, 0, 1, 0,
								.0, .0, .0, 1);

mat4 myScalingMatrix = mat4(.75, 0, 0, 0,
					 		0, .75, 0, 0,
					 		0, 0, .75, 0,
					 		0, 0, 0, 1);

//Rotate around z-axis					 		
/*mat4 myRotatingMatrix = mat4(cos(time), -sin(time), 0, 0,
							 sin(time), cos(time), 0, 0,
							 0, 0, 1, 0,
							 0, 0, 0, 1);*/
					
//Rotate around x-axis
/*mat4 myRotatingMatrix = mat4(1, 0, 0, 0,
							 0, cos(time), -sin(time), 0,
							 0, sin(time), cos(time), 0,
							 0, 0, 0, 1);*/
							
//Rotate around y-axis
mat4 myRotatingMatrix = mat4(cos(time), 0, -sin(time), 0,
							 0, 1, 0, 0,
							 sin(time), 0, cos(time), 0,
							 0, 0, 0, 1);
							 
vec3 cameraTarget = vec3(0, 0, 0);
vec3 up = vec3(0, 1, 0);

vec3 cameraDirection = normalize(cameraPosition - cameraTarget);
vec3 cameraRight = normalize(cross(up, cameraDirection));
vec3 cameraUp = cross(cameraDirection, cameraRight);

mat4 cameraMatrix = mat4(1, 0, 0, 0,
						 0, 1, 0, 0,
						 0, 0, 1, 0,
						 -cameraPosition[0], -cameraPosition[1], -cameraPosition[2], 1);
					   
//cameraRight
//cameraUp
//cameraDirection
mat4 viewMatrix = mat4(cameraRight, 0,
					   cameraUp, 0,
					   cameraDirection, 0,
					   0, 0, 0, 1);
						 

mat4 projectionMatrix = mat4(1/(1.5*tan(fovX/2)), 0, 0, 0,
							 0, 1/tan(fovY/2), 0, 0,
							 0, 0, (-nearZ-farZ)/(nearZ-farZ), (2*farZ*nearZ)/(nearZ-farZ),
							 0, 0, 0, 1);
							 
/*mat4 projectionMatrix = mat4(1/(1.5*tan(fovX/2)), 0, 0, 0,
							 0, 1/tan(fovY/2), 0, 0,
							 0, 0, (farZ + nearZ)/(farZ - nearZ), 1.0,
							 0, 0, (-2.0*farZ*nearZ)/(farZ - nearZ), 0);*/

mat4 modelMatrix = myTranslatingMatrix * myRotatingMatrix * myScalingMatrix;
mat4 MVPMatrix = projectionMatrix * viewMatrix * modelMatrix;

//Correct way to do it
//mat4 modelMatrix = myTranslatingMatrix * myRotatingMatrix * myScalingMatrix;
//mat4 MVPMatrix = projectionMatrix * viewMatrix * modelMatrix;
//vec4 transformedVertex = MVPMatrix * position;

void main(){
	vColor = color;
							 
	gl_Position = projectionMatrix * cameraMatrix * modelMatrix * position;
	//gl_Position = MVPMatrix * position;
}
